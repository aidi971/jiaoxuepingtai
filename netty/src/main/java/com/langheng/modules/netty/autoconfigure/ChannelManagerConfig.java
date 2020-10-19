package com.langheng.modules.netty.autoconfigure;

import com.langheng.modules.netty.manager.SystemChannelManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChannelManagerConfig {

    @Bean
    public SystemChannelManager getSystemChannelManager(){
        return SystemChannelManager.getInstance();
    }


}
