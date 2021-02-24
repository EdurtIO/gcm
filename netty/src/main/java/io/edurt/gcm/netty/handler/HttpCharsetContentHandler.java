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
package io.edurt.gcm.netty.handler;

import io.edurt.gcm.netty.type.Charseter;
import io.edurt.gcm.netty.type.ContentType;
import org.apache.commons.lang3.ObjectUtils;

public class HttpCharsetContentHandler
{
    public String getContentAndCharset(Charseter character, ContentType contentType)
    {
        return getDefault(character, contentType);
    }

    public String getDefault(Charseter character, ContentType contentType)
    {
        if (ObjectUtils.isEmpty(character)) {
            character = Charseter.UTF8;
        }
        if (ObjectUtils.isEmpty(contentType)) {
            contentType = ContentType.TEXT_PLAN;
        }
        return String.join("; charset=", contentType.getValue(), character.getValue());
    }
}
