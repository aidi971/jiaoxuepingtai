package com.langheng.test;

import com.langheng.modules.netty.annotation.*;
import com.langheng.modules.netty.pojo.Session;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.Map;

/**
 * @ClassName MyWebSocket
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-03-25 11:05
 * @Version 1.0
 */
@ServerEndpoint(path = "/ws/testArg")
public class TestWebSocket {

    /**
     * 当有新的连接进入时调用
     * @param session
     * @param headers
     * @param req
     * @param reqMap
     * @param arg
     * @param pathMap
     */
    @BeforeHandshake
    public void handshake(Session session, HttpHeaders headers, @RequestParam String req, @RequestParam MultiValueMap reqMap, @PathVariable String arg, @PathVariable Map pathMap){
        session.setSubprotocols("stomp");
    }

    /**
     * 新的WebSocket连接完成时
     * @param session
     * @param headers
     * @param req
     * @param reqMap
     * @param arg
     * @param pathMap
     */
    @OnOpen
    public void onOpen(Session session, HttpHeaders headers, @RequestParam String req, @RequestParam MultiValueMap reqMap, @PathVariable String arg, @PathVariable Map pathMap){
        System.out.println("new connection");
        System.out.println(req);
    }

    /**
     * 当有WebSocket连接关闭时
     * @param session
     * @throws IOException
     */
    @OnClose
    public void onClose(Session session) throws IOException {
        System.out.println("one connection closed");
    }

    /**
     * 当有WebSocket抛出异常时
     * @param session
     * @param throwable
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }


    /**
     * 当接收到字符串消息时
      * @param session
     * @param message
     */
    @OnMessage
    public void onMessage(Session session, String message) {
        System.out.println(message);
        session.sendText("Hello Netty!");
    }

    /**
     * 当接收到二进制消息时
     * @param session
     * @param bytes
     */
    @OnBinary
    public void onBinary(Session session, byte[] bytes) {
        for (byte b : bytes) {
            System.out.println(b);
        }
        session.sendBinary(bytes);
    }


    /**
     * 当接收到Netty的事件时
     * @param session
     * @param evt
     */
    @OnEvent
    public void onEvent(Session session, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    System.out.println("read idle");
                    break;
                case WRITER_IDLE:
                    System.out.println("write idle");
                    break;
                case ALL_IDLE:
                    System.out.println("all idle");
                    break;
                default:
                    break;
            }
        }
    }

}