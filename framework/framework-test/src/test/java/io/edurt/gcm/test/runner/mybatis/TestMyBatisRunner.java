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
package io.edurt.gcm.test.runner.mybatis;

import com.google.inject.Inject;
import io.edurt.gcm.test.annotation.JunitModuleLoader;
import io.edurt.gcm.test.annotation.mybatis.MapperClasses;
import io.edurt.gcm.test.runner.mybatis.mapper.TestMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(value = MyBatisRunner.class)
@JunitModuleLoader(value = {TestMybatisModule.class})
@MapperClasses(value = {TestMapper.class})
public class TestMyBatisRunner
{
    @Inject
    private TestMapper mapper;

    @Test
    public void findAll()
    {
        this.mapper.findAll().forEach(v -> System.out.println(v));
    }

    @Test
    public void save()
    {
        Assert.assertTrue(this.mapper.save() == 2);
    }
}
