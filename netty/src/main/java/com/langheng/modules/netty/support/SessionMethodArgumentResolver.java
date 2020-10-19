package com.langheng.modules.netty.support;

import com.langheng.modules.netty.pojo.Session;
import io.netty.channel.Channel;
import org.springframework.core.MethodParameter;

import static com.langheng.modules.netty.pojo.PojoEndpointServer.SESSION_KEY;

public class SessionMethodArgumentResolver implements MethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Session.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Channel channel, Object object) throws Exception {
        Session session = channel.attr(SESSION_KEY).get();
        return session;
    }
}
