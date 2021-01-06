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
package io.edurt.gcm.redis.configuration;

public class RedisConfiguration
{
    public static final String DATABASE = "redis.database";
    public static final String HOST = "redis.host";
    public static final String PORT = "redis.port";
    public static final String PASSWORD = "redis.password";
    public static final String POOL_MAX_ACTIVE = "redis.pool.max-active";
    public static final String POOL_MAX_IDLE = "redis.pool.max-idle";
    public static final String POOL_MAX_WAIT = "redis.pool.max-wait";
    public static final String POOL_MIN_IDLE = "redis.pool.min-idle";
    public static final String TIMEOUT = "redis.timeout";

    private RedisConfiguration()
    {}
}
