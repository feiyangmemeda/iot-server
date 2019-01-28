package com.iot.server.listener;

import com.iot.server.netty.lanuch.Launcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 * @author feiyang.d
 * @date 2018/8/13
 */
@Component
@Slf4j
public class ApplicationCloseListener implements ApplicationListener<ContextClosedEvent> {

    @Autowired
    private Launcher launcher;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.info("【spring context is closed】");
        launcher.close();
        log.info("【netty is closed】");
    }
}
