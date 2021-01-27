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

import java.util.Map;

public class RedisHashClient
        extends RedisClient
{
    public RedisHashClient(ShardedJedisPool pool)
    {
        super(pool);
    }

    public synchronized Long hashSet(String key, Map<String, String> value)
    {
        try (ShardedJedis jedis = this.pool.getResource()) {
            return jedis.hset(key, value);
        }
    }

    public synchronized String hashUpdate(String key, Map<String, String> value)
    {
        try (ShardedJedis jedis = this.pool.getResource()) {
            return jedis.hmset(key, value);
        }
    }

    public synchronized Map<String, String> hashGetAll(String key)
    {
        try (ShardedJedis jedis = this.pool.getResource()) {
            return jedis.hgetAll(key);
        }
    }

    public synchronized Long hashDelete(String group, String key)
    {
        try (ShardedJedis jedis = this.pool.getResource()) {
            return jedis.hdel(group, key);
        }
    }

    public synchronized String hashGet(String group, String key)
    {
        try (ShardedJedis jedis = this.pool.getResource()) {
            return jedis.hget(group, key);
        }
    }
}
