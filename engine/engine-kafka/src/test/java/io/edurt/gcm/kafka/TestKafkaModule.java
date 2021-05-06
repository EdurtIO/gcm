package io.edurt.gcm.kafka;

import com.google.inject.Inject;
import io.edurt.gcm.kafka.client.KafkaProduceClient;
import io.edurt.gcm.test.annotation.JunitModuleLoader;
import io.edurt.gcm.test.runner.JunitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JunitRunner.class)
@JunitModuleLoader(value = {KafkaModule.class})
public class TestKafkaModule
{
    @Inject
    private KafkaProduceClient kafkaProduceClient;

    @Test
    public void test()
    {
        kafkaProduceClient.sendMessage("test", "String");
    }
}