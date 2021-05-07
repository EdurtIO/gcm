package io.edurt.gcm.kafka.configuration;

public class KafkaConfigurationDefault
{
    public static final String SERVERS = "localhost:9092";
    public static final String ACK = "all";
    public static final String RETRIES = "0";
    public static final String BATCH_SIZE = "16384";
    public static final String LINGER_MS = "1";
    public static final String BUFFER_MEMORY = "33554432";
    public static final String KEY_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";
    public static final String VALUE_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";

    private KafkaConfigurationDefault()
    {}
}
