package io.edurt.gcm.example.databases.nosql;

import com.google.inject.Guice;
import com.google.inject.Inject;
import io.edurt.gcm.elasticsearch.ElasticsearchModule;
import io.edurt.gcm.elasticsearch.client.ElasticsearchClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticsearchModuleExample
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchModuleExample.class);

    @Inject
    private ElasticsearchClient client;

    public static void main(String[] args)
    {
        String classpath = ElasticsearchModuleExample.class.getResource("/databases/nosql/elasticsearch.properties").getPath();
        LOGGER.info("Load configuration from {}", classpath);
        ElasticsearchModuleExample example = Guice.createInjector(new ElasticsearchModule(classpath)).getInstance(ElasticsearchModuleExample.class);
        System.out.println(example.client.getRestClient());
    }
}
