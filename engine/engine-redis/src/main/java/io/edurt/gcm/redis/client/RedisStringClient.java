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
package io.edurt.gcm.redis.client;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedisStringClient
        extends RedisClient
{
    public RedisStringClient(ShardedJedisPool pool)
    {
        super(pool);
    }

    public synchronized String set(String key, String value)
    {
        try (ShardedJedis jedis = this.pool.getResource()) {
            return jedis.set(key, value);
        }
    }

    public synchronized String setByte(byte[] key, byte[] value)
    {
        try (ShardedJedis jedis = this.pool.getResource()) {
            return jedis.set(key, value);
        }
    }
}
