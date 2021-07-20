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

public class RedisClient
{
    protected final ShardedJedisPool pool;

    public RedisClient(ShardedJedisPool pool)
    {
        this.pool = pool;
    }

    public synchronized Long delete(String key)
    {
        try (ShardedJedis jedis = this.pool.getResource()) {
            return jedis.del(key);
        }
    }

    public synchronized Long persist(String key)
    {
        try (ShardedJedis jedis = this.pool.getResource()) {
            return jedis.persist(key);
        }
    }

    public synchronized Long persist(byte[] key)
    {
        try (ShardedJedis jedis = this.pool.getResource()) {
            return jedis.persist(key);
        }
    }

    public synchronized Long ttl(String key)
    {
        try (ShardedJedis jedis = this.pool.getResource()) {
            return jedis.ttl(key);
        }
    }

    public synchronized Long ttl(byte[] key)
    {
        try (ShardedJedis jedis = this.pool.getResource()) {
            return jedis.ttl(key);
        }
    }

    public synchronized Long expire(byte[] key, int seconds)
    {
        try (ShardedJedis jedis = this.pool.getResource()) {
            return jedis.expire(key, seconds);
        }
    }

    public synchronized Long expire(String key, int seconds)
    {
        try (ShardedJedis jedis = this.pool.getResource()) {
            return jedis.expire(key, seconds);
        }
    }

    public synchronized Long pexpire(String key,  long milliseconds)
    {
        try (ShardedJedis jedis = this.pool.getResource()) {
            return jedis.pexpire(key, milliseconds);
        }
    }

    public synchronized Long pexpire(byte[] key,  long milliseconds)
    {
        try (ShardedJedis jedis = this.pool.getResource()) {
            return jedis.pexpire(key, milliseconds);
        }
    }
}
