package io.edurt.gcm.netty.controller;

import com.google.inject.Singleton;
import io.edurt.gcm.netty.annotation.RequestParam;
import io.edurt.gcm.netty.annotation.ResponseBody;

@Singleton
public class TestController
{
    @ResponseBody
    public Object println(@RequestParam("message") String message)
    {
        return message;
    }
}
