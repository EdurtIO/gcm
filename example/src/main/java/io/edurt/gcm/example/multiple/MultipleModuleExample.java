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
package io.edurt.gcm.example.multiple;

import com.google.inject.Guice;
import com.google.inject.Inject;
import io.edurt.gcm.clickhouse.ClickHouseMultipleModule;
import io.edurt.gcm.example.bigdata.client.ClickHouseHikaricpExample;
import io.edurt.gcm.example.bigdata.client.service.ContributorsService;
import io.edurt.gcm.example.databases.sql.MySQLHikaricpExample;
import io.edurt.gcm.example.databases.sql.service.UserService;
import io.edurt.gcm.mysql.hikari.MySQLHikariMultipleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultipleModuleExample
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MultipleModuleExample.class);

    @Inject
    private UserService userService;

    @Inject
    private ContributorsService contributorsService;

    public static void main(String[] args)
    {
        String mysql = MySQLHikaricpExample.class.getResource("/databases/sql/mysql-hikaricp.properties").getPath();
        LOGGER.info("Load mysql configuration from {}", mysql);
        String clickhouse = ClickHouseHikaricpExample.class.getResource("/bigdata/clickhouse-hikaricp.properties").getPath();
        LOGGER.info("Load clickhouse configuration from {}", clickhouse);
        MultipleModuleExample example = Guice.createInjector(
                new MySQLHikariMultipleModule(mysql),
                new ClickHouseMultipleModule(clickhouse)).getInstance(MultipleModuleExample.class);
        LOGGER.info("print data from clickhouse datasource");
        example.contributorsService.getAll().forEach(v -> System.out.println(v));
        LOGGER.info("print data from mysql datasource");
        example.userService.getAll().forEach(v -> System.out.println(v));
    }
}
