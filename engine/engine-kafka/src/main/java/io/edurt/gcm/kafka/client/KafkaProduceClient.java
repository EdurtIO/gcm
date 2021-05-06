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
package io.edurt.gcm.kafka.client;

import io.edurt.gcm.common.utils.GsonUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class KafkaProduceClient
{
    private final KafkaProducer<String, String> kafkaProducer;

    public KafkaProduceClient(KafkaProducer<String, String> kafkaProducer)
    {
        this.kafkaProducer = kafkaProducer;
    }

    public void sendMessage(ProducerRecord record)
    {
        try {
            this.kafkaProducer.send(record);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String topic, String msg)
    {
        ProducerRecord record = new ProducerRecord(topic, msg);
        this.sendMessage(record);
    }

    public void sendMessage(String topic, Object object)
    {
        this.sendMessage(topic, GsonUtils.toJson(object));
    }

    public void flush()
    {
        this.kafkaProducer.flush();
    }
}

