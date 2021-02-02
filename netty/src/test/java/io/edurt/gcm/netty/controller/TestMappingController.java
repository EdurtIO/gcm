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
package io.edurt.gcm.netty.controller;

import io.edurt.gcm.netty.annotation.GetMapping;
import io.edurt.gcm.netty.annotation.PostMapping;
import io.edurt.gcm.netty.annotation.RequestParam;
import io.edurt.gcm.netty.annotation.ResponseBody;
import io.edurt.gcm.netty.annotation.RestController;

@RestController
public class TestMappingController
{
    @ResponseBody
    @GetMapping(value = {"/api/test/mapping/get"})
    public String getMapping(@RequestParam(value = "value") String value)
    {
        return String.format("This is @GetMapping test case, value %s", value);
    }

    @ResponseBody
    @PostMapping(value = {"/api/test/mapping/post"})
    public String postMapping(@RequestParam(value = "value") String value)
    {
        return String.format("This is @PostMapping test case, value %s", value);
    }
}
