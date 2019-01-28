package com.iot.server.netty.context;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ChannelHandlerContext 管理类
 * 设备发送注册指令时将业务参数与channel绑定
 *
 * @author feiyang.duan
 * @createTime 2018年08月06日
 */
@Slf4j
public class HandlerContextManger {

    private HandlerContextManger() {
    }

    /**
     * 设备iMei -> channelContext映射容器
     */
    public static Map<String, ChannelHandlerContext> ctxMap = new ConcurrentHashMap();

    /**
     * @param deviceNo 设备号
     * @param channelHandlerContext
     * @return
     */
    public static ChannelHandlerContext put(String deviceNo, ChannelHandlerContext channelHandlerContext) {
        ctxMap.put(deviceNo, channelHandlerContext);
        return channelHandlerContext;
    }

    public static String remove(ChannelHandlerContext channelHandlerContext) {
        for (Map.Entry entry : ctxMap.entrySet()) {
            if (entry.getValue() == channelHandlerContext) {
                ctxMap.remove(entry.getKey());
                return entry.getKey().toString();
            }
        }
        return null;
    }

    public static ChannelHandlerContext get(String iMei) {
        return ctxMap.get(iMei);
    }

}
