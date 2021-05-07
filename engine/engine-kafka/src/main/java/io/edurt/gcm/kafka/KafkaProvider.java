package io.edurt.gcm.kafka;

import com.google.inject.Provider;
import io.edurt.gcm.common.utils.ObjectUtils;
import io.edurt.gcm.common.utils.PropertiesUtils;
import io.edurt.gcm.kafka.client.KafkaProduceClient;
import io.edurt.gcm.kafka.configuration.KafkaConfiguration;
import io.edurt.gcm.kafka.configuration.KafkaConfigurationDefault;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class KafkaProvider
        implements Provider<KafkaProduceClient>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProvider.class);
    private final Properties properties;
    private KafkaProducer<String, String> kafkaProducer;

    public KafkaProvider(Properties configuration)
    {
        LOGGER.info("Build kafka configuration");
        properties = new Properties();
        properties.put("bootstrap.servers", PropertiesUtils.getStringValue(configuration,
                KafkaConfiguration.SERVERS,
                KafkaConfigurationDefault.SERVERS));
        properties.put("acks", PropertiesUtils.getStringValue(configuration,
                KafkaConfiguration.ACK,
                KafkaConfigurationDefault.ACK));
        properties.put("retries", PropertiesUtils.getStringValue(configuration,
                KafkaConfiguration.RETRIES,
                KafkaConfigurationDefault.RETRIES));
        properties.put("batch.size", PropertiesUtils.getStringValue(configuration,
                KafkaConfiguration.BATCH_SIZE,
                KafkaConfigurationDefault.BATCH_SIZE));
        properties.put("linger.ms", PropertiesUtils.getStringValue(configuration,
                KafkaConfiguration.LINGER_MS,
                KafkaConfigurationDefault.LINGER_MS));
        properties.put("buffer.memory", PropertiesUtils.getStringValue(configuration,
                KafkaConfiguration.BUFFER_MEMORY,
                KafkaConfigurationDefault.BUFFER_MEMORY));
        properties.put("key.serializer", PropertiesUtils.getStringValue(configuration,
                KafkaConfiguration.KEY_SERIALIZER,
                KafkaConfigurationDefault.KEY_SERIALIZER));
        properties.put("value.serializer", PropertiesUtils.getStringValue(configuration,
                KafkaConfiguration.VALUE_SERIALIZER,
                KafkaConfigurationDefault.VALUE_SERIALIZER));
    }

    private final KafkaProducer<String, String> builderKafkaProducer()
    {
        if (ObjectUtils.isEmpty(kafkaProducer)) {
            synchronized (this) {
                if (ObjectUtils.isEmpty(kafkaProducer)) {
                    kafkaProducer = new KafkaProducer<>(this.properties);
                }
            }
        }
        return kafkaProducer;
    }

    @Override
    public KafkaProduceClient get()
    {
        return new KafkaProduceClient(builderKafkaProducer());
    }
}
