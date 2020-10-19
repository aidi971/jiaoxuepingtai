package com.langheng.modules.netty.annotation;

import com.langheng.modules.netty.standard.ServerEndpointExporter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnMissingBean(ServerEndpointExporter.class)
@Configuration
public class NettyWebSocketSelector {

    @Bean
    public ServerEndpointExporter serverEndpointExporter22() {
        return new ServerEndpointExporter();
    }
}
