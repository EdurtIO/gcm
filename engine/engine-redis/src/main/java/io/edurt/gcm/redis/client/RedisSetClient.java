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

import java.util.Set;

public class RedisSetClient
        extends RedisClient
{
    public RedisSetClient(ShardedJedisPool pool)
    {
        super(pool);
    }

    public synchronized Long set(String key, String... value)
    {
        try (ShardedJedis jedis = this.pool.getResource()) {
            return jedis.sadd(key, value);
        }
    }

    public synchronized Long deleteValue(String key, String... value)
    {
        try (ShardedJedis jedis = this.pool.getResource()) {
            return jedis.srem(key, value);
        }
    }

    public synchronized Set<String> getAll(String key)
    {
        try (ShardedJedis jedis = this.pool.getResource()) {
            return jedis.smembers(key);
        }
    }
}
