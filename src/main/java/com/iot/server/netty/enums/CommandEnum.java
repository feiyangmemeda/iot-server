package com.iot.server.netty.enums;

/**
 * 与设备交互的命令枚举
 *
 * @author feiyang.d
 * @date 2018/11/8
 */
public enum CommandEnum {

    LOGIN("login", "loginService", "设备登录"),
    ;

    private String command;
    private String handler;
    private String desc;

    CommandEnum(String command, String handler, String desc) {
        this.command = command;
        this.handler = handler;
        this.desc = desc;
    }

    public String getCommand() {
        return command;
    }

    public String getHandler() {
        return handler;
    }

    public String getDesc() {
        return desc;
    }

    public static CommandEnum getEnumByCommand(String command) {
        for (CommandEnum e : CommandEnum.values()) {
            if (e.getCommand().equals(command)) {
                return e;
            }
        }
        return null;
    }
}
