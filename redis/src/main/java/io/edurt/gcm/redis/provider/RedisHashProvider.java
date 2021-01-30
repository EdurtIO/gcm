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
package io.edurt.gcm.redis.provider;

import io.edurt.gcm.redis.client.RedisHashClient;

import javax.inject.Provider;

import java.util.Properties;

public class RedisHashProvider
        extends RedisProvider
        implements Provider<RedisHashClient>
{
    public RedisHashProvider(Properties configuration)
    {
        super(configuration);
    }

    @Override
    public RedisHashClient get()
    {
        return new RedisHashClient(this.getJedisPool());
    }
}
