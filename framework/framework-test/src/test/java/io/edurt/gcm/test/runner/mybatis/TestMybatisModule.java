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

import com.google.inject.name.Names;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.builtin.PooledDataSourceProvider;

public class TestMybatisModule
        extends MyBatisModule
{
    @Override
    protected void initialize()
    {
        binder().bindConstant().annotatedWith(Names.named("JDBC.driver")).to("com.mysql.jdbc.Driver");
        binder().bindConstant().annotatedWith(Names.named("JDBC.url")).to("jdbc:mysql://localhost:3306/test");
        binder().bindConstant().annotatedWith(Names.named("JDBC.username")).to("root");
        binder().bindConstant().annotatedWith(Names.named("JDBC.password")).to("123456");
        binder().bindConstant().annotatedWith(Names.named("mybatis.environment.id")).to("Test");
        bindDataSourceProviderType(PooledDataSourceProvider.class);
        bindTransactionFactoryType(JdbcTransactionFactory.class);
        addMapperClasses("io.edurt.gcm.test.runner.mybatis.mapper");
    }
}
