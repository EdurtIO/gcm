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
package io.edurt.gcm.common.jdk;

import io.edurt.gcm.common.utils.ObjectUtils;
import io.edurt.gcm.common.utils.StringUtils;

import java.util.Optional;

public class Optionals
{
    private Optionals()
    {}

    /**
     * Check if there is data
     *
     * @param value Optional source data
     * @return Verification results
     */
    public static Boolean isHasValue(Optional value)
    {
        if (value.isPresent()) {
            if (value.get() instanceof String) {
                if (StringUtils.isEmpty(((String) value.get()).trim())) {
                    return !true;
                }
            }
            return !ObjectUtils.isEmpty(value.get());
        }
        return false;
    }
}
