package com.iot.server.listener;


import com.iot.server.netty.lanuch.Launcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author feiyang.d
 * @date 2018/8/2
 */
@Component
@Slf4j
public class ApplicationStartListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private Launcher launcher;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            log.info("【Spring Start Success】");
            new Thread(() -> launcher.launch()).start();
        }
    }
}
