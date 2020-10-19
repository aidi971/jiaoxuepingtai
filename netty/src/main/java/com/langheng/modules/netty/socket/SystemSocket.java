package com.langheng.modules.netty.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeesite.common.entity.TokenInfo;
import com.jeesite.common.utils.TokenUtils;
import com.langheng.modules.netty.annotation.*;
import com.langheng.modules.netty.manager.SystemChannelManager;
import com.langheng.modules.netty.model.Message;
import com.langheng.modules.netty.model.NettyProto;
import com.langheng.modules.netty.model.SystemProto;
import com.langheng.modules.netty.pojo.Session;
import com.langheng.modules.netty.utils.NettyUtils;
import io.netty.handler.codec.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @ClassName Socket
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-03-25 14:47
 * @Version 1.0
 */
@ServerEndpoint(path = "/ws/system/{arg}",port = "${socket.port.system}")
public class SystemSocket {

    @Autowired
    private SystemChannelManager systemChannelManager;

    final static Logger logger = LoggerFactory.getLogger(SystemSocket.class);

    @BeforeHandshake
    public void handshake(Session session, HttpHeaders headers, @RequestParam String req,
                          @RequestParam MultiValueMap reqMap, @PathVariable String arg, @PathVariable Map pathMap){
    }

    @OnOpen
    public void onOpen(Session session, HttpHeaders headers, @RequestParam String req, @RequestParam MultiValueMap reqMap, @PathVariable String arg, @PathVariable Map pathMap){
        TokenInfo tokenInfo = TokenUtils.validateTokenAndGetTokenInfo(arg);
        if (tokenInfo == null){
            systemChannelManager.defaultErrorAuthMessage(session.channel(),"您被迫下线，如这不是您所为，您的密码很有可能泄漏");
            session.close();
        }else {
            Boolean isMainChannel = checkIsMainChannel(reqMap);
            systemChannelManager.addChannel(session.channel(),tokenInfo,isMainChannel);
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        TokenInfo tokenInfo = systemChannelManager.getTokenInfo(session.channel());
        if (tokenInfo == null) return;
        if (tokenInfo.getMainChannel()){
            systemChannelManager.removeMainChannel(session.channel());
        }else {
            systemChannelManager.removeMinorChannel(session.channel());
        }

    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }


    @OnMessage
    public void onMessage(Session session, String msg) {
       try {
           TokenInfo tokenInfo =  systemChannelManager.getTokenInfo(session.channel());
           if (tokenInfo == null){
               systemChannelManager.defaultErrorAuthMessage(session.channel());
               return;
           }

           ObjectMapper objectMapper = new ObjectMapper();
           Message message = objectMapper.readValue(msg, Message.class);
           logger.info("接收信息: {}", message.toString());
           Integer code = message.getCode();
           switch (code){
               case NettyProto.PING_PROTO : {
                   logger.info("接收ping信息, 地址: {}", NettyUtils.parseChannelRemoteAddr(session.channel()));
                   return;
               }case NettyProto.PONG_PROTO: {
                   logger.info("接收pong信息, 地址: {}", NettyUtils.parseChannelRemoteAddr(session.channel()));
                   return;
               } case SystemProto .SYS_KEEP_ALIVE: {
                   systemChannelManager.keepAlive(session);
                   return;
               }default:{
                   systemChannelManager.sendMessage(session.channel(),defaultMessage(code));
                   logger.warn("消息码[{}]没经过验证!!!", code);
               }
           }
       }catch (Exception e){
           e.printStackTrace();
       }

    }

    private Message defaultMessage(Integer code){
        Message resultMessage = new Message();
        resultMessage.setCode(SystemProto.SYS_MSG_PROTO);
        resultMessage.setData("消息码" + code +"没经过验证!!!");
        return resultMessage;
    }

    private Boolean checkIsMainChannel(MultiValueMap reqMap) {
        List args = ((List)reqMap.get("isMainChannel"));
        if (args !=null && !args.isEmpty()){
            if ("true".equals(args.get(0))){
                return true;
            }
        }
        return false;
    }

}
