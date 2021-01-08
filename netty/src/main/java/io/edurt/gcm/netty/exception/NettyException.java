package io.edurt.gcm.netty.exception;

public class NettyException
        extends Exception
{
    private final short code;
    private final String message;

    public NettyException(final short code, final String message)
    {
        super(message);
        this.code = code;
        this.message = message;
    }

    public short getCode()
    {
        return code;
    }

    @Override
    public String getMessage()
    {
        return message;
    }
}
