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

import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathHandler
{
    public static final String PATTERN = "(\\{[^}]*})";

    private PathHandler()
    {}

    /**
     * Verify whether the client URL matches the routing URL
     * <p>For example: client delivery<code>/path/123</code>in the matching route<code>/path/${id}</code></p>
     *
     * @param requestUrl Client request URL
     * @param routerUrl Routing URL
     * @return Match status, true match, false mismatch
     */
    public static boolean verify(String requestUrl, String routerUrl)
    {
        Matcher keyMatcher = Pattern.compile(PATTERN).matcher(routerUrl);
        String replacePattern = keyMatcher.replaceAll("(.*)");
        // Transfer special characters
        replacePattern = replacePattern.replace("{", "\\{").replace("}", "\\}");
        Matcher valueMatcher = Pattern.compile(replacePattern).matcher(requestUrl);
        return valueMatcher.matches();
    }

    /**
     * Get client request parameters
     * <p>For example: client requests<code>/path/123</code>in routing<code>/path/${id}</code></p>
     * <p>The obtained parameter is<code>{"id":"123"}</code></p>
     *
     * @param requestUrl Client request URL
     * @param routerUrl Routing URL
     * @return Client request parameters
     */
    public static Map<String, String> getParams(String requestUrl, String[] routerUrl)
    {
        Map<String, String> params = new ConcurrentHashMap<>(16);
        Matcher keyMatcher = matcherPath(routerUrl);
        List<String> keys = new ArrayList<>(16);
        List<String> values = new ArrayList<>(16);
        while (keyMatcher.find()) {
            keys.add(keyMatcher.group(1).replace("{", "").replace("}", ""));
        }
        String replacePattern = keyMatcher.replaceAll("(.*)");
        Matcher valueMatcher = Pattern.compile(replacePattern).matcher(requestUrl);
        if (valueMatcher.find()) {
            int count = valueMatcher.groupCount();
            for (int i = 1; i <= count; i++) {
                values.add(valueMatcher.group(i));
            }
        }
        int valueSize = values.size();
        for (int i = 0; i < keys.size(); i++) {
            String value = i < valueSize ? values.get(i) : "";
            params.put(keys.get(i), value);
        }
        return params;
    }

    /**
     * Match the address array passed in the route. When multiple addresses are matched, the first one is selected by default
     *
     * @param routerUrl Routing URL
     * @return If the route is not matched, null will be returned
     */
    private static Matcher matcherPath(String[] routerUrl)
    {
        Matcher matcher = null;
        if (ObjectUtils.isNotEmpty(routerUrl) && routerUrl.length > 0) {
            Optional<Matcher> matcherOptional = Arrays.stream(routerUrl)
                    .map(v -> Pattern.compile(PATTERN).matcher(v))
                    .findFirst();
            if (matcherOptional.isPresent()) {
                matcher = matcherOptional.get();
            }
        }
        return matcher;
    }
}
