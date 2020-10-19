package com.langheng.modules.netty.listener;

import com.langheng.modules.base.event.TeamEvent;
import com.langheng.modules.netty.manager.SystemChannelManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
public class TeamListener {

    @Autowired
    private SystemChannelManager systemChannelManager;

    @EventListener
    public void sendSystemEvent(TeamEvent teamEvent){
        String userId = teamEvent.getUserId();
        String contentJson = teamEvent.getContentJson();
        if (!userId.isEmpty()){
            systemChannelManager.broadCastDownloadMsg(userId,contentJson);
        }

    }

}
