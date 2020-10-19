package com.langheng.modules.netty.listener;

import com.langheng.modules.base.event.MsgEvent;
import com.langheng.modules.netty.manager.SystemChannelManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName TeachingClassListener
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-04-01 15:07
 * @Version 1.0
 */
@Component
public class MsgListener {

    @Autowired
    private SystemChannelManager systemChannelManager;

    @EventListener
    public void sendMsgEvent(MsgEvent msgEvent){

        List<String> receiverIds = msgEvent.getReceiverIds();
        String contentJson = msgEvent.getContentJson();
        if (!receiverIds.isEmpty()){
            systemChannelManager.broadCastMsg(receiverIds,contentJson);
        }

    }

}
