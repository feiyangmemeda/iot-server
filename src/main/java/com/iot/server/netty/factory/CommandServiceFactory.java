package com.iot.server.netty.factory;

import com.iot.server.netty.enums.CommandEnum;
import com.iot.server.netty.service.CommandReceiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 设备消息->命令处理工厂
 * @author feiyang.d
 * @date 2018/11/8
 */
@Component
public class CommandServiceFactory {

    @Autowired
    private Map<String, CommandReceiveService> commandServiceMap;

    public CommandReceiveService getServiceByCommand(String command) {
        return commandServiceMap.get(CommandEnum.getEnumByCommand(command).getHandler());
    }

}
