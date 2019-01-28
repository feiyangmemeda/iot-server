package com.iot.server.netty.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author feiyang.d
 * @date 2019/1/28
 */
@Data
public class Message implements Serializable {
    private String command;
    private String deviceNo;
}
