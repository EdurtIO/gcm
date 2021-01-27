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

import org.apache.commons.lang3.ObjectUtils;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.List;

public class RedisListClient
        extends RedisClient
{
    public RedisListClient(ShardedJedisPool pool)
    {
        super(pool);
    }

    public synchronized Long listSet(String key, String... arrays)
    {
        try (ShardedJedis jedis = this.pool.getResource()) {
            return jedis.lpush(key, arrays);
        }
    }

    public synchronized List<String> listGet(String key)
    {
        return listGet(key, null, null);
    }

    /**
     * Get the data set under all keys according to the provided key
     *
     * @param key Specify the key to query
     * @param start Specifies the starting offset value of the data set. The default value is 0
     * @param end Specifies the end offset value of the data set. The default value is 0
     * @return Data set
     */
    public synchronized List<String> listGet(String key, Long start, Long end)
    {
        if (ObjectUtils.isEmpty(start)) {
            start = 0L;
        }
        if (ObjectUtils.isEmpty(end)) {
            end = Long.MAX_VALUE;
        }
        try (ShardedJedis jedis = this.pool.getResource()) {
            return jedis.lrange(key, start, end);
        }
    }

    /**
     * Delete the specified value data under the specified key
     *
     * @param key Specify the key
     * @param deleteDuplicateCount The total number of duplicate data deleted. The default is all
     * @param deleteValue Specified value data to be deleted
     * @return Delete the number of affected rows
     */
    public synchronized Long listDelete(String key, Long deleteDuplicateCount, String deleteValue)
    {
        if (ObjectUtils.isEmpty(deleteDuplicateCount)) {
            deleteDuplicateCount = Long.MAX_VALUE;
        }
        try (ShardedJedis jedis = this.pool.getResource()) {
            return jedis.lrem(key, deleteDuplicateCount, deleteValue);
        }
    }
}
