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

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.airlift.units.Duration;
import io.edurt.gcm.presto.configuration.PrestoConfiguration;
import io.prestosql.client.ClientSession;
import io.prestosql.client.OkHttpUtil;
import io.prestosql.client.StatementClient;
import io.prestosql.client.StatementClientFactory;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;
import javax.inject.Named;

import java.net.URI;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static io.prestosql.client.OkHttpUtil.setupTimeouts;

@ThreadSafe
public class PrestoStatementClient
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PrestoStatementClient.class);
    private OkHttpClient httpClient;

    @Inject
    @Named(value = PrestoConfiguration.USERNAME)
    private String username;

    @Inject
    @Named(value = PrestoConfiguration.PASSWORD)
    private String password;

    @Inject
    @Named(value = PrestoConfiguration.URL)
    private String url;

    @Inject
    @Named(value = PrestoConfiguration.URL_BACKUP)
    private String backupUrl;

    @Inject
    @Named(value = PrestoConfiguration.CATALOG)
    private String catalog;

    @Inject
    @Named(value = PrestoConfiguration.SCHEMA)
    private String schema;

    @Inject
    @Named(value = PrestoConfiguration.SOURCE)
    private String source;

    public PrestoStatementClient()
    {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        setupTimeouts(builder, 5, TimeUnit.SECONDS);
        httpClient = builder.build();
    }

    public StatementClient createStatementClient(String query)
    {
        LOGGER.debug("Init presto client session by {}", username);
        try {
            if (ObjectUtils.isEmpty(url)) {
                url = backupUrl;
            }
            LOGGER.debug("Presto catalog <{}> schema <{}> source <{}> username <{}>", catalog, schema, source, username);
            if (ObjectUtils.isNotEmpty(username) && ObjectUtils.isNotEmpty(password)) {
                ClientSession clientSession = new ClientSession(
                        URI.create(url), username, source, Optional.empty(), ImmutableSet.of(), null, catalog,
                        schema, null, ZoneId.systemDefault(), Locale.getDefault(),
                        ImmutableMap.of(), ImmutableMap.of(), Collections.emptyMap(), Collections.emptyMap(), ImmutableMap.of(), null, new Duration(2, TimeUnit.MINUTES));
                if (clientSession.getServer().getScheme().equalsIgnoreCase("https")) {
                    LOGGER.error("Authentication using username/password requires HTTPS to be enabled");
                }
                Preconditions.checkArgument(clientSession.getServer().getScheme().equalsIgnoreCase("https"),
                        "Authentication using username/password requires HTTPS to be enabled");
                OkHttpClient.Builder clientBuilder = httpClient.newBuilder();
                clientBuilder.addInterceptor(OkHttpUtil.basicAuth(username, password));
                LOGGER.debug("Authentication using username/password, this query sql {}", query);
                return StatementClientFactory.newStatementClient(clientBuilder.build(), clientSession, query);
            }
            ClientSession clientSession = new ClientSession(
                    URI.create(url), username, source, Optional.empty(), ImmutableSet.of(), null, catalog,
                    schema, null, ZoneId.systemDefault(), Locale.getDefault(),
                    ImmutableMap.of(), ImmutableMap.of(), Collections.emptyMap(), Collections.emptyMap(), ImmutableMap.of(), null, new Duration(2, TimeUnit.MINUTES));
            return StatementClientFactory.newStatementClient(httpClient, clientSession, query);
        }
        catch (Exception ex) {
            LOGGER.error("Init presto client error", ex);
            return null;
        }
    }
}
