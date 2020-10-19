package com.langheng.modules.netty.listener;

import com.langheng.modules.base.event.SystemEvent;
import com.langheng.modules.netty.manager.SystemChannelManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName SystemListener
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-04-01 15:07
 * @Version 1.0
 */
@Component
public class SystemListener {

    @Autowired
    private SystemChannelManager systemChannelManager;

    @EventListener
    public void sendSystemEvent(SystemEvent systemEvent){

        List<String> receiverIds = systemEvent.getReceiverIds();
        String contentJson = systemEvent.getContentJson();
        if (!receiverIds.isEmpty()){
            systemChannelManager.broadCastErrorAuth(receiverIds,"您已被强制下线，如有需要请重新登录");
        }

    }

}
