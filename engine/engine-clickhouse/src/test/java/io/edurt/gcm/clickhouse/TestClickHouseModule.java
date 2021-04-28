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
package io.edurt.gcm.clickhouse;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.edurt.gcm.clickhouse.mapper.TestMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestClickHouseModule
{
    private String path;

    @Before
    public void before()
    {
        this.path = this.getClass().getResource("/clickhouse.properties").getPath();
    }

    @After
    public void after()
    {
    }

    @Test
    public void test()
    {
        Injector injector = Guice.createInjector(new ClickHouseModule(path));
        TestMapper mapper = injector.getInstance(TestMapper.class);
        mapper.findAll().forEach(System.out::println);
    }
}
