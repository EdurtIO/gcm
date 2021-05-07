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

import io.edurt.gcm.netty.annotation.Controller;
import io.edurt.gcm.netty.annotation.GetMapping;
import io.edurt.gcm.netty.annotation.RequestMapping;
import io.edurt.gcm.netty.type.RequestMethod;
import io.edurt.gcm.netty.view.ParamModel;

@Controller
public class TestViewController
{
    @RequestMapping(value = "/api/test/view/index", method = {RequestMethod.GET})
    public String index()
    {
        return "index";
    }

    @GetMapping(value = "/api/test/view/parameter")
    public String parameter(ParamModel paramModel)
    {
        paramModel.addAttribute("hello", "This Hello!");
        return "parameter";
    }

    @GetMapping(value = "/api/test/view/css")
    public String css()
    {
        return "css";
    }
}
