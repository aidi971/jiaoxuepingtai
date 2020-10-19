package com.langheng.modules.netty.manager;

import com.jeesite.common.entity.TokenInfo;
import com.langheng.modules.base.utils.JwtUtils;
import com.langheng.modules.netty.pojo.Session;

/**
 * @ClassName SystemChannelManager
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-04-02 9:38
 * @Version 1.0
 */
public class SystemChannelManager extends AbstractMultiChannelManager {

    private static SystemChannelManager systemChannelManager = new SystemChannelManager();


    private SystemChannelManager() {
    }

    public static SystemChannelManager getInstance(){
        return systemChannelManager;
    }


    /**
     * 通过socket的方式续期
     * @param session
     */
    public void keepAlive(Session session) {
        TokenInfo tokenInfo = channelTokenMap.get(session.channel());
        JwtUtils.refreshTokenCache(tokenInfo.getUserId());
    }

}
