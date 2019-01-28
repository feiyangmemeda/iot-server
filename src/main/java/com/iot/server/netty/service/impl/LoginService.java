package com.iot.server.netty.service.impl;

import com.iot.server.netty.bean.Message;
import com.iot.server.netty.context.HandlerContextManger;
import com.iot.server.netty.service.CommandReceiveService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 设备登录处理
 *
 * @author feiyang.d
 * @date 2018/11/8
 */
@Service
@Slf4j
public class LoginService implements CommandReceiveService {


    @Override
    public boolean dealCommand(Message message, ChannelHandlerContext channelHandlerContext) {
        // 绑定设备号与channel
        HandlerContextManger.put(message.getDeviceNo(), channelHandlerContext);
        return true;
    }

    @Override
    public Message getResponse(Message message) {
        return message;
    }


}
