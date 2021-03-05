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
package io.edurt.gcm.elasticsearch;

import io.edurt.gcm.common.utils.PropertiesUtils;
import io.edurt.gcm.elasticsearch.client.ElasticsearchClient;
import io.edurt.gcm.elasticsearch.configuration.ElasticsearchConfiguration;
import io.edurt.gcm.elasticsearch.configuration.ElasticsearchConfigurationDefault;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ElasticsearchProvider
        implements Provider<ElasticsearchClient>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchProvider.class);

    private Properties configuration;

    public ElasticsearchProvider(Properties configuration)
    {
        this.configuration = configuration;
    }

    private RestHighLevelClient getHighLevelClient()
    {
        String scheme = PropertiesUtils.getStringValue(this.configuration,
                ElasticsearchConfiguration.SCHEME,
                ElasticsearchConfigurationDefault.SCHEME);
        String urls = PropertiesUtils.getStringValue(this.configuration,
                ElasticsearchConfiguration.URL,
                ElasticsearchConfigurationDefault.URL);
        if (ObjectUtils.isEmpty(urls)) {
            throw new RuntimeException("Elasticsearch connect url must null!");
        }
        List<HttpHost> hosts = new ArrayList<>();
        Arrays.asList(urls.split(",")).forEach(v -> {
            String[] hostInfo = v.split(":");
            hosts.add(new HttpHost(hostInfo[0], Integer.valueOf(hostInfo[1]), scheme));
        });
        RestClient lowLevelRestClient = RestClient.builder(hosts.toArray(new HttpHost[0])).build();
        RestHighLevelClient client =
                new RestHighLevelClient(lowLevelRestClient);
        return client;
    }

    @Override
    public ElasticsearchClient get()
    {
        return new ElasticsearchClient(getHighLevelClient());
    }
}
