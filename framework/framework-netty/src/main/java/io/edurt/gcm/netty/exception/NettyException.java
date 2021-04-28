package io.edurt.gcm.netty.exception;

public class NettyException
        extends Exception
{
    private final int code;
    private final String message;

    public NettyException(final int code, final String message)
    {
        super(message);
        this.code = code;
        this.message = message;
    }

    public int getCode()
    {
        return code;
    }

    @Override
    public String getMessage()
    {
        return message;
    }
}
