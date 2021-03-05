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
package io.edurt.gcm.common.file;

import java.io.File;

import static java.lang.String.join;

public class Paths
{
    public static String getProjectHome()
    {
        return System.getProperty("user.dir");
    }

    public static String getProjectConfigurationHome(String... paths)
    {
        return join(File.separator, getProjectHome(), join(File.separator, paths));
    }
}
