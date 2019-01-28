package com.iot.server.netty.service;

import com.iot.server.netty.bean.Message;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author feiyang.d
 * @date 2018/11/8
 */
public interface CommandReceiveService {
    /**
     * 接收并处理设备发送的命令
     * @return
     */
    boolean dealCommand(Message message, ChannelHandlerContext channelHandlerContext);

    /**
     * 获取命令回应
     * @return
     */
    Message getResponse(Message deviceTcpBean);
}
