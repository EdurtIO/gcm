package io.edurt.gcm.netty.controller;

import com.google.inject.Singleton;
import io.edurt.gcm.netty.annotation.GetMapping;
import io.edurt.gcm.netty.annotation.PathVariable;
import io.edurt.gcm.netty.annotation.PostMapping;
import io.edurt.gcm.netty.annotation.RequestBody;
import io.edurt.gcm.netty.annotation.RequestMapping;
import io.edurt.gcm.netty.annotation.RequestParam;
import io.edurt.gcm.netty.annotation.ResponseBody;
import io.edurt.gcm.netty.annotation.RestController;
import io.edurt.gcm.netty.type.RequestMethod;

import java.util.Map;

@Singleton
@RestController
public class TestController
{
    @ResponseBody
    @RequestMapping(value = {"/api/test", "/api/test1"}, method = {RequestMethod.POST, RequestMethod.GET})
    public Object println(@RequestParam("message") String message)
    {
        return message;
    }

    @PostMapping(value = {"/test"})
    public String say(@RequestParam("message") String message, @RequestBody Map<String, String> params)
    {
        return String.join("\n", message, params.toString());
    }

    @GetMapping(value = "/test/{id}")
    public String pathParam(@PathVariable(value = "id") String id)
    {
        return id;
    }

    @GetMapping(value = "/test/user/123")
    public String pathParam2(@PathVariable(value = "id") String id)
    {
        return id;
    }

    @GetMapping(value = "test/non/parameter")
    public String nonParameter()
    {
        return "NonParameter";
    }

    @GetMapping(value = "/path/{id}")
    public Object path(@PathVariable(value = "id") String id,
            @RequestParam(value = "name") String name)
    {
        return String.join(":", id, name);
    }
}
