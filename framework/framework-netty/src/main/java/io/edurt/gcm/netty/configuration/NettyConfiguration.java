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
package io.edurt.gcm.netty.configuration;

public class NettyConfiguration
{
    public static final String HOST = "netty.host";
    public static final String PORT = "netty.port";
    public static final String CONTROLLER_PACKAGE = "netty.controller.package";
    public static final String ROUTER_PRINT = "netty.router.print";
    public static final String VIEW_TEMPLATE_PATH = "netty.view.path";
    public static final String VIEW_TEMPLATE_SUFFIX = "netty.view.suffix";
    public static final String VIEW_TEMPLATE_STATIC = "netty.view.static";
    public static final String MAX_CONTENT_LENGTH = "netty.max.content.length";

    private NettyConfiguration() {}
}
