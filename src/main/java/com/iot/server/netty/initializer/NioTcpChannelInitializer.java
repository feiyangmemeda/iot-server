package com.iot.server.netty.initializer;


import com.iot.server.netty.handler.NioTcpInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author feiyang.duan
 * @date 2018/11/8
 * 用于初始化channel pipeline
 */
@Component
public class NioTcpChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private NioTcpInboundHandler nioTcpInboundHandler;

    @Value("${readerIdleTime}")
    private int readerIdleTime;

    @Value("${writerIdleTime}")
    private int writerIdleTime;

    @Value("${allIdleTime}")
    private int allIdleTime;

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

        //添加心跳检测handler
        pipeline.addLast("idleStateHandler",
                new IdleStateHandler(readerIdleTime, writerIdleTime, allIdleTime,TimeUnit.SECONDS));
        //解码器
        pipeline.addLast("commandDecoder", new StringDecoder());
        //编码器
        pipeline.addLast("commandEncoder", new StringEncoder());
        //添加自定义的handler
        pipeline.addLast("nioTcpHandler", nioTcpInboundHandler);
    }
}
