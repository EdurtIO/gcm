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
package io.edurt.gcm.example.general;

import com.google.inject.Guice;
import com.google.inject.Inject;
import io.edurt.gcm.zookeeper.ZookeeperModule;
import io.edurt.gcm.zookeeper.client.ZookeeperClient;

public class ZookeeperExample
{
    @Inject
    private ZookeeperClient zookeeperClient;

    public static void main(String[] args)
    {
        String classpath = ZookeeperExample.class.getResource("/general/zookeeper.properties").getPath();
        ZookeeperExample instance = Guice.createInjector(new ZookeeperModule(classpath))
                .getInstance(ZookeeperExample.class);
        System.out.println(instance.zookeeperClient.getNode("localhost"));
    }
}
