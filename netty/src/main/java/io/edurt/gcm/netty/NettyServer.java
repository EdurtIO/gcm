package io.edurt.gcm.netty;

import com.google.inject.Inject;
import com.google.inject.Injector;
import io.edurt.gcm.common.utils.PropertiesUtils;
import io.edurt.gcm.netty.configuration.NettyConfiguration;
import io.edurt.gcm.netty.configuration.NettyConfigurationDefault;
import io.edurt.gcm.netty.handler.HttpRequestHandler;
import io.edurt.gcm.netty.router.RouterScan;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class NettyServer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);

    private static Properties configuration;
    @Inject
    private Injector injector;
    @Inject
    private EventLoopGroup bossGroup;
    @Inject
    private EventLoopGroup workerGroup;

    public static final void binder(Properties properties)
    {
        configuration = properties;
        // scan controller from configuration
        scanController();
    }

    private static final void scanController()
    {
        String scanPackage = PropertiesUtils.getStringValue(configuration,
                NettyConfiguration.CONTROLLER_PACKAGE,
                NettyConfigurationDefault.CONTROLLER_PACKAGE);
        LOGGER.debug("Scan controller from configuration path {}", scanPackage);
        RouterScan.getRouters(scanPackage);
    }

    public void run()
            throws Exception
    {
        String host = PropertiesUtils.getStringValue(configuration,
                NettyConfiguration.HOST,
                NettyConfigurationDefault.HOST);
        int port = PropertiesUtils.getIntValue(configuration,
                NettyConfiguration.PORT,
                NettyConfigurationDefault.PORT);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(channelInitializer())
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            LOGGER.info("Running ServerBootstrap on {}:{}", host, port);
            ChannelFuture f = bootstrap.bind(host, port).sync();
            f.channel().closeFuture().sync();
        }
        finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    private ChannelInitializer channelInitializer()
    {
        return new ChannelInitializer<SocketChannel>()
        {
            @Override
            protected void initChannel(SocketChannel ch)
            {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new HttpServerCodec());
                pipeline.addLast(new HttpObjectAggregator(65536));
                pipeline.addLast(new HttpRequestHandler(injector, "/socket", configuration));
            }
        };
    }
}
