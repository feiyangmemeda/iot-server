package com.iot.server.netty.handler;


import com.alibaba.fastjson.JSON;
import com.iot.server.netty.bean.Message;
import com.iot.server.netty.context.HandlerContextManger;
import com.iot.server.netty.factory.CommandServiceFactory;
import com.iot.server.netty.service.CommandReceiveService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * channel 事件处理
 *
 * @author feiyang.duan
 * @createTime 2018/08/04
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ChannelHandler.Sharable
@Slf4j
public class NioTcpInboundHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private CommandServiceFactory commandServiceFactory;


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("来自ip为{}的设备与服务器已创建tcp连接", ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Message message = null;
        try {
            message = JSON.parseObject(msg.toString(),Message.class);
        } catch (Exception e) {
            log.error("报文解析错误,message-body={},来自ip为{}的设备", msg.toString(), ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress());
            return;
        }
        log.debug("接收到客户端消息message = {}", message.toString());
        CommandReceiveService service = commandServiceFactory.getServiceByCommand(message.getCommand());
        if (Objects.isNull(service)) {
            log.error("找不到该命令处理类,deviceNo={},command={}", message.getDeviceNo(), message.getCommand());
            return;
        }
        if (service.dealCommand(message, ctx)) {
            Message response = service.getResponse(message);
            ctx.writeAndFlush(response.toString());
            log.debug("服务端向客户端deviceNo = {},发送消息message = {}", response.getCommand(), response.toString());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(cause.getMessage(), cause);
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                // 10分钟服务端没有收到该channel消息则断开连接，将数据库改为离线,删除上下文
                String deviceNo = HandlerContextManger.remove(ctx);
                if (!StringUtil.isNullOrEmpty(deviceNo)) {
                    log.info("设备号为{}的设备与服务器长时间没有消息交互，断开连接", deviceNo);
                    // todo 下线操作
                }
                ctx.close();
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        String deviceNo = HandlerContextManger.remove(ctx);
        if (!StringUtil.isNullOrEmpty(deviceNo)) {
            log.info("设备号为{}的设备与服务器主动断开连接", deviceNo);
            // todo 下线操作
        }
        ctx.close();
    }


}
