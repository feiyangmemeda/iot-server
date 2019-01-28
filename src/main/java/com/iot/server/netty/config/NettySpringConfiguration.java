package com.iot.server.netty.config;

import com.iot.server.netty.initializer.NioTcpChannelInitializer;
import com.iot.server.netty.lanuch.Launcher;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author feiyang.d
 * @date 2019/1/28
 */
@Configuration
public class NettySpringConfiguration {

    @Autowired
    private NioTcpChannelInitializer nioTcpChannelInitializer;

    @Value("${netty.server.port}")
    private int port;

    @Bean
    @Scope(scopeName = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Launcher launcher(){
        Launcher launcher = new Launcher();
        launcher.setServerBootstrap(new ServerBootstrap());
        launcher.setParentGroup(new NioEventLoopGroup(1));
        launcher.setChildGroup(new NioEventLoopGroup());

        HashMap<String, Object> childOptions = new HashMap<>();
        childOptions.put("TCP_NODELAY",Boolean.TRUE);
        childOptions.put("SO_BACKLOG",128);
        launcher.setChildOptions(childOptions);

        List<ChannelHandler> channelHandlers = new ArrayList<>();
        channelHandlers.add(nioTcpChannelInitializer);
        launcher.setChildHandlers(channelHandlers);

        launcher.setPort(port);

        return launcher;
    }
}
