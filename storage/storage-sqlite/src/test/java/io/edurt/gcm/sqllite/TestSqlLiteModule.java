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
package io.edurt.gcm.sqllite;

import io.edurt.gcm.common.jdk.ObjectBuilder;
import io.edurt.gcm.sqllite.mapper.TestMapper;
import io.edurt.gcm.sqllite.model.TestModel;
import io.edurt.gcm.test.annotation.JunitModuleLoader;
import io.edurt.gcm.test.runner.JunitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

@RunWith(value = JunitRunner.class)
@JunitModuleLoader(value = SQLLiteModule.class)
public class TestSqlLiteModule
{
    @Inject
    private TestMapper mapper;

    @Test
    public void testSave()
    {
        TestModel model = ObjectBuilder.of(TestModel::new)
                .with(TestModel::setName, "Test")
                .build();
        mapper.insert(model);
    }

    @Test
    public void testGetAll()
    {
        mapper.findAll().forEach(System.out::println);
    }
}
