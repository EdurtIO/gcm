package io.edurt.gcm.test.runner.junit;

public class TestServiceImpl
        implements TestService
{
    @Override
    public void println(String message)
    {
        System.out.println(message);
    }
}
