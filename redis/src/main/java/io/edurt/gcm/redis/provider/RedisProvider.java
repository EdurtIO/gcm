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

import io.edurt.gcm.common.utils.PropertiesUtils;
import io.edurt.gcm.redis.configuration.RedisConfiguration;
import io.edurt.gcm.redis.configuration.RedisConfigurationDefault;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

public class RedisProvider
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisProvider.class);
    private static ShardedJedisPool pool;
    private final Properties configuration;

    public RedisProvider(Properties configuration)
    {
        this.configuration = configuration;
    }

    /**
     * builder jedis pool configuration
     *
     * @return jedis pool configuration
     */
    private JedisPoolConfig getPoolConfig()
    {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(PropertiesUtils.getIntValue(this.configuration,
                RedisConfiguration.POOL_MAX_ACTIVE,
                RedisConfigurationDefault.POOL_MAX_ACTIVE));
        jedisPoolConfig.setMaxWaitMillis(PropertiesUtils.getIntValue(this.configuration,
                RedisConfiguration.POOL_MAX_WAIT,
                RedisConfigurationDefault.POOL_MAX_WAIT));
        jedisPoolConfig.setMaxIdle(PropertiesUtils.getIntValue(this.configuration,
                RedisConfiguration.POOL_MAX_IDLE,
                RedisConfigurationDefault.POOL_MAX_IDLE));
        jedisPoolConfig.setMinIdle(PropertiesUtils.getIntValue(this.configuration,
                RedisConfiguration.POOL_MIN_IDLE,
                RedisConfigurationDefault.POOL_MIN_IDLE));
        return jedisPoolConfig;
    }

    /**
     * builder jedis shard info
     *
     * @return jedis shard info
     */
    public JedisShardInfo getShardInfo()
    {
        JedisShardInfo jedisShardInfo = new JedisShardInfo(PropertiesUtils.getStringValue(this.configuration,
                RedisConfiguration.HOST,
                RedisConfigurationDefault.HOST),
                PropertiesUtils.getIntValue(this.configuration,
                        RedisConfiguration.PORT,
                        RedisConfigurationDefault.PORT),
                PropertiesUtils.getIntValue(this.configuration,
                        RedisConfiguration.TIMEOUT,
                        RedisConfigurationDefault.TIMEOUT));
        String password = PropertiesUtils.getStringValue(this.configuration,
                RedisConfiguration.PASSWORD,
                RedisConfigurationDefault.PASSWORD);
        if (ObjectUtils.isNotEmpty(password) && !password.isEmpty()) {
            jedisShardInfo.setPassword(password);
        }
        try {
            // set database
            Class<? extends JedisShardInfo> clazz = jedisShardInfo.getClass();
            Field declaredField = clazz.getDeclaredField("db");
            declaredField.setAccessible(true);
            declaredField.set(jedisShardInfo, PropertiesUtils.getIntValue(this.configuration,
                    RedisConfiguration.DATABASE,
                    RedisConfigurationDefault.DATABASE));
        }
        catch (Exception exception) {
            LOGGER.error("set databases error, skip it!", exception);
        }
        return jedisShardInfo;
    }

    public ShardedJedisPool getJedisPool()
    {
        if (Objects.isNull(pool)) {
            pool = new ShardedJedisPool(getPoolConfig(), Arrays.asList(getShardInfo()));
        }
        return pool;
    }
}
