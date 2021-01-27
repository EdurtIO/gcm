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

import io.edurt.gcm.redis.client.RedisSetClient;

import javax.inject.Provider;

import java.util.Properties;

public class RedisSetProvider
        extends RedisProvider
        implements Provider<RedisSetClient>
{
    public RedisSetProvider(Properties configuration)
    {
        super(configuration);
    }

    @Override
    public RedisSetClient get()
    {
        return new RedisSetClient(this.getJedisPool());
    }
}
