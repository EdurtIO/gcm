package io.edurt.gcm.netty.controller;

import com.google.inject.Singleton;
import io.edurt.gcm.netty.annotation.RequestMapping;
import io.edurt.gcm.netty.annotation.RequestParam;
import io.edurt.gcm.netty.annotation.ResponseBody;
import io.edurt.gcm.netty.annotation.RestController;
import io.edurt.gcm.netty.type.RequestMethod;

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
}
