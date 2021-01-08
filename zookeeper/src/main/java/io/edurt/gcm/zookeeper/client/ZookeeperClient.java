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
package io.edurt.gcm.zookeeper.client;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class ZookeeperClient
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperClient.class);

    private CuratorFramework client;

    public ZookeeperClient(CuratorFramework client)
    {
        if (ObjectUtils.isEmpty(client)) {
            LOGGER.error("Unable to initialize zookeeper client, please check whether the configuration is correct!");
            throw new RuntimeException("Unable to initialize zookeeper client instance!");
        }
        this.client = client;
    }

    /**
     * Create a new node
     *
     * @param nodeName Node name
     * @return Create result, return path after success, return null if failure
     */
    public String createNode(String nodeName)
    {
        try {
            startClient();
            String node = client.create().forPath(formatNodePath(nodeName));
            return node;
        }
        catch (Exception ex) {
            LOGGER.error("Failed to create node {}, exception information {}", nodeName, ex);
        }
        finally {
            closeClient();
        }
        return null;
    }

    /**
     * Create temporary node
     *
     * @param nodeName Node name
     * @return Create result, return path after success, return null if failure
     */
    public String createEphemeralNode(String nodeName)
    {
        return createEphemeralNode(nodeName, null);
    }

    public String createEphemeralNode(String nodeName, String value)
    {
        try {
            startClient();
            String node;
            if (ObjectUtils.isNotEmpty(value)) {
                node = client.create()
                        .creatingParentContainersIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        .forPath(formatNodePath(nodeName), value.getBytes(Charset.defaultCharset()));
            }
            else {
                node = client.create()
                        .creatingParentContainersIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        .forPath(formatNodePath(nodeName));
            }
            return node;
        }
        catch (Exception ex) {
            LOGGER.error("Failed to create node {}, exception information {}", nodeName, ex);
        }
        finally {
            closeClient();
        }
        return null;
    }

    /**
     * Check whether the node exists
     *
     * @param nodeName Node name
     * @return The check result shows that the true flag already exists and the false flag has not
     */
    public Boolean existsNode(String nodeName)
    {
        try {
            startClient();
            Stat stat = client.checkExists().forPath(formatNodePath(nodeName));
            if (ObjectUtils.isNotEmpty(stat)) {
                return Boolean.TRUE;
            }
        }
        catch (Exception ex) {
            LOGGER.error("Failed to check node {}, exception information {}", nodeName, ex);
        }
        finally {
            closeClient();
        }
        return Boolean.FALSE;
    }

    /**
     * Delete node
     *
     * @param nodeName Node name
     * @return Delete node status, true flag delete success, false flag delete failure
     */
    public Boolean deleteNode(String nodeName)
    {
        try {
            startClient();
            client.delete()
                    .guaranteed() // If the deletion fails, the client will continue to delete until the node is deleted
                    .deletingChildrenIfNeeded()   // Delete related child nodes
                    .withVersion(-1)    // Ignore version, delete directly
                    .forPath(formatNodePath(nodeName));
            return Boolean.TRUE;
        }
        catch (Exception ex) {
            LOGGER.error("Failed to delete node {}, exception information {}", nodeName, ex);
        }
        finally {
            closeClient();
        }
        return Boolean.FALSE;
    }

    /**
     * Update node
     *
     * @param nodeName Node name
     * @return Update the node status, the true flag is deleted successfully, and the false flag is deleted failed
     */
    public Boolean updateNode(String nodeName, String value)
    {
        try {
            startClient();
            client.setData()
                    .withVersion(-1)
                    .forPath(formatNodePath(nodeName), value.getBytes(Charset.defaultCharset()));
            return Boolean.TRUE;
        }
        catch (Exception ex) {
            LOGGER.error("Failed to update node {}, exception information {}", nodeName, ex);
        }
        finally {
            closeClient();
        }
        return Boolean.FALSE;
    }

    /**
     * Get node information
     *
     * @param nodeName Node name
     * @return Node information
     */
    public String getNode(String nodeName)
    {
        try {
            startClient();
            byte[] bytes = client.getData().forPath(formatNodePath(nodeName));
            if (ObjectUtils.isNotEmpty(bytes) && bytes.length > 0) {
                return new String(bytes, Charset.forName("UTF-8"));
            }
        }
        catch (Exception ex) {
            LOGGER.error("Failed to update node {}, exception information {}", nodeName, ex);
        }
        finally {
            closeClient();
        }
        return null;
    }

    /**
     * Start zookeeper client
     */
    private void startClient()
    {
        if (ObjectUtils.isNotEmpty(this.client)) {
            CuratorFrameworkState state = this.client.getState();
            if (ObjectUtils.isNotEmpty(state)) {
                if (CuratorFrameworkState.STARTED != state || CuratorFrameworkState.STOPPED == state) {
                    this.client.start();
                }
            }
        }
    }

    /**
     * Close zookeeper client
     */
    private void closeClient()
    {
        if (ObjectUtils.isNotEmpty(this.client)) {
//            CloseableUtils.closeQuietly(this.client);
        }
    }

    /**
     * Format node name
     * <p>Zookeeper node name must start with /. If the node name is not changed, the system will add it automatically</p>
     *
     * @param nodeName Node name
     * @return Node name
     */
    private String formatNodePath(String nodeName)
    {
        if (!nodeName.startsWith("/")) {
            nodeName = "/" + nodeName;
        }
        return nodeName;
    }
}
