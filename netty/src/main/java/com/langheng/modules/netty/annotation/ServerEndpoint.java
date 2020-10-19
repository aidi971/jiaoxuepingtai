package com.langheng.modules.netty.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 当ServerEndpointExporter类通过Spring配置进行声明并被使用，
 * 它将会去扫描带有@ServerEndpoint注解的类 被注解的类将被注册成为一个WebSocket端点
 * 所有的配置项都在这个注解的属性中 ( 如:@ServerEndpoint("/ws") )
 * @author Yeauty
 * @version 1.0
 */
@Component
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ServerEndpoint {

    @AliasFor("path")
    String value() default "/";

    @AliasFor("value")
    String path() default "/";

    String host() default "0.0.0.0";

    String port() default "20203";

    String bossLoopGroupThreads() default "0";

    String workerLoopGroupThreads() default "0";

    String useCompressionHandler() default "false";

    //------------------------- option -------------------------

    String optionConnectTimeoutMillis() default "30000";

    String optionSoBacklog() default "128";

    //------------------------- childOption -------------------------

    String childOptionWriteSpinCount() default "16";

    String childOptionWriteBufferHighWaterMark() default "65536";

    String childOptionWriteBufferLowWaterMark() default "32768";

    String childOptionSoRcvbuf() default "-1";

    String childOptionSoSndbuf() default "-1";

    String childOptionTcpNodelay() default "true";

    String childOptionSoKeepalive() default "false";

    String childOptionSoLinger() default "-1";

    String childOptionAllowHalfClosure() default "false";

    //------------------------- idleEvent -------------------------

    String readerIdleTimeSeconds() default "0";

    String writerIdleTimeSeconds() default "0";

    String allIdleTimeSeconds() default "0";

    //------------------------- handshake -------------------------

    String maxFramePayloadLength() default "65536";

}
