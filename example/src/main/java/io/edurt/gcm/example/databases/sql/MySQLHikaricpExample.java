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
package io.edurt.gcm.example.databases.sql;

import com.google.inject.Guice;
import com.google.inject.Inject;
import io.edurt.gcm.example.databases.sql.service.UserService;
import io.edurt.gcm.mysql.hikari.HikariMySQLModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySQLHikaricpExample
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MySQLHikaricpExample.class);

    @Inject
    private UserService userService;

    public static void main(String[] args)
    {
        String classpath = MySQLHikaricpExample.class.getResource("/databases/sql/mysql-hikaricp.properties").getPath();
        LOGGER.info("Load configuration from {}", classpath);
        MySQLHikaricpExample example = Guice.createInjector(new HikariMySQLModule(classpath)).getInstance(MySQLHikaricpExample.class);
        example.userService.getAll().forEach(v -> System.out.println(v));
    }
}
