/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.edurt.gcm.presto.client.api;

import com.google.common.collect.Lists;
import io.airlift.units.Duration;
import io.edurt.gcm.presto.client.Client;
import io.prestosql.client.Column;
import io.prestosql.client.QueryError;
import io.prestosql.client.QueryStatusInfo;
import io.prestosql.client.StatementClient;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class PrestoApiClient
        implements Client
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PrestoApiClient.class);

    @Inject
    private PrestoStatementClient prestoStatementClient;

    @Override
    public Map<String, Object> execute(String query)
    {
        Map<String, Object> allData = new ConcurrentHashMap<>();
        StatementClient client = prestoStatementClient.createStatementClient(query);
        if (ObjectUtils.isEmpty(client)) {
            LOGGER.error("Client not connected, stop this query!");
        }
        else {
            try {
                LOGGER.info("Start execute session query {}", System.currentTimeMillis());
                Duration queryMaxRunTime = new Duration(3600, TimeUnit.SECONDS);
                long start = System.currentTimeMillis();
                allData.put("startTime", start);
                while (client.isRunning() && ObjectUtils.isEmpty(client.currentData().getData())) {
                    try {
                        client.advance();
                    }
                    catch (RuntimeException e) {
                        e.printStackTrace();
                        QueryStatusInfo results = client.isRunning() ? client.currentStatusInfo() : client.finalStatusInfo();
                        String queryId = results.getId();
                        LOGGER.error("Query failed (#{}) in %s: presto internal error message=%s", queryId, null, e.getMessage());
                    }
                    Long end = System.currentTimeMillis();
                    if ((end - start) > queryMaxRunTime.toMillis()) {
                        LOGGER.debug("Query timeout start time {}, end time {}", start, end);
                    }
                }
                // running or finished
                if (client.isRunning() || (client.isFinished() && ObjectUtils.isEmpty(client.finalStatusInfo().getError()))) {
                    QueryStatusInfo results = client.isRunning() ? client.currentStatusInfo() : client.finalStatusInfo();
                    String queryId = results.getId();
                    allData.put("taskId", queryId);
                    LOGGER.info("Current query \n{}, \ntask id {}", query, queryId);
                    if (ObjectUtils.isEmpty(results.getColumns())) {
                        LOGGER.debug("Query column is empty!");
                    }
                    else {
                        List<String> columns = Lists.transform(results.getColumns(), Column::getName);
                        allData.put("columns", columns);
                        Iterable<List<Object>> rowData = client.currentData().getData();
                        allData.put("values", rowData);
                        long end = System.currentTimeMillis();
                        allData.put("endTime", end);
                        allData.put("elapsedTime", (end - start));
                        LOGGER.info("End execute session query {}", System.currentTimeMillis());
                    }
                }
                if (client.isClientAborted()) {
                    LOGGER.debug("Query aborted by user!");
                }
                if (client.isClientError()) {
                    LOGGER.debug("Query is gone (server restarted?)!");
                }
                try {
                    if (ObjectUtils.isNotEmpty(client.finalStatusInfo().getError())) {
                        QueryError queryError = client.finalStatusInfo().getError();
                        allData.put("errorMessage", queryError.getMessage());
                        allData.put("errorCode", queryError.getErrorCode());
                        allData.put("errorType", queryError.getErrorType());
                    }
                }
                catch (Exception warning) {
                    // skip warning
                }
            }
            catch (Exception ex) {
                LOGGER.error("Query error", ex);
            }
            finally {
                if (ObjectUtils.isNotEmpty(client)) {
                    LOGGER.info("Query end close client connect!");
                    client.close();
                }
            }
        }
        return allData;
    }
}
