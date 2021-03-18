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
package io.edurt.gcm.example.bigdata.client;

import com.google.inject.Guice;
import com.google.inject.Inject;
import io.edurt.gcm.example.bigdata.client.service.ContributorsService;
import io.edurt.gcm.hive.HiveModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HiveHikaricpExample
{
    private static final Logger LOGGER = LoggerFactory.getLogger(HiveHikaricpExample.class);

    @Inject
    private ContributorsService contributorsService;

    public static void main(String[] args)
    {
        String classpath = HiveHikaricpExample.class.getResource("/bigdata/hive-hikaricp.properties").getPath();
        LOGGER.info("Load configuration from {}", classpath);
        HiveHikaricpExample example = Guice.createInjector(new HiveModule(classpath)).getInstance(HiveHikaricpExample.class);
        example.contributorsService.getAll().forEach(v -> System.out.println(v));
    }
}
