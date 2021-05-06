package io.edurt.gcm.kafka.configuration;

public class KafkaConfiguration
{
    public static final String SERVERS = "component.kafka.servers";
    public static final String ACK = "component.kafka.ack";
    public static final String RETRIES = "component.kafka.retries";
    public static final String BATCH_SIZE = "component.kafka.batch.size";
    public static final String LINGER_MS = "component.kafka.linger.ms";
    public static final String BUFFER_MEMORY = "component.kafka.buffer.memory";
    public static final String KEY_SERIALIZER = "component.kafka.key.serializer";
    public static final String VALUE_SERIALIZER = "component.kafka.value.serializer";

    private KafkaConfiguration()
    {}
}
