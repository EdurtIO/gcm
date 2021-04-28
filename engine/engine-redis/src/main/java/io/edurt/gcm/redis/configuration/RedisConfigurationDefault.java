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

public class RedisConfigurationDefault
{
    public static final int DATABASE = 0;
    public static final String HOST = "localhost";
    public static final int PORT = 6379;
    public static final String PASSWORD = "";
    public static final int POOL_MAX_ACTIVE = 10;
    public static final int POOL_MAX_IDLE = 10;
    public static final int POOL_MAX_WAIT = 10;
    public static final int POOL_MIN_IDLE = 10;
    public static final int TIMEOUT = 60;

    private RedisConfigurationDefault()
    {}
}
