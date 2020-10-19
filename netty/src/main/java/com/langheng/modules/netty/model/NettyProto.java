package com.langheng.modules.netty.model;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName NettyProto
 * @Description TODO
 * @Author xiaoxie
 * @Date 2019-10-29 11:45
 * @Version 1.0
 */
public class NettyProto {
    public static final int PING_PROTO = 10000; //ping消息
    public static final int PONG_PROTO = 10001; //pong消息
    public static final int SYS_PROTO = 70000; //系统消息
    public static final int ERROR_PROTO = 50000; //错误消息
    public static final int AUTH_PROTO = 40000; //认证消息
    public static final int MESS_PROTO = 20001; //普通消息
    public static final int BARRAGE_PROTO = 61000; //弹幕消息
    public static final int NOTICE_PROTO = 62000; //公告消息
    public static final int SYS_MSG_PROTO = 63000; //系统消息
    public static final int ASS_MSG_PROTO = 64000; //ass消息
    public static final int EDRACE_PROTO = 71000; //工具消息

    private int version = 1;
    private int uri;
    private String body;
    private Map<String, Object> extend = new HashMap<>();

    public NettyProto(int head, String body) {
        this.uri = head;
        this.body = body;
    }

    public static String buildPingProto() {
        return buildProto(PING_PROTO, null);
    }

    public static String buildPongProto() {
        return buildProto(PONG_PROTO, null);
    }

    public static String buildSystProto(int code, Object mess) {
        NettyProto nettyProto = new NettyProto(SYS_PROTO, null);
        nettyProto.extend.put("code", code);
        nettyProto.extend.put("mess", mess);
        return JSONObject.toJSONString(nettyProto);
    }

    public static String buildAuthProto(boolean isSuccess) {
        NettyProto nettyProto = new NettyProto(AUTH_PROTO, null);
        nettyProto.extend.put("isSuccess", isSuccess);
        return JSONObject.toJSONString(nettyProto);
    }

    public static String buildErrorProto(int code, String mess) {
        NettyProto nettyProto = new NettyProto(ERROR_PROTO, null);
        nettyProto.extend.put("code", code);
        nettyProto.extend.put("mess", mess);
        return JSONObject.toJSONString(nettyProto);
    }

    public static String buildBarrageProto(int code, String mess) {
        NettyProto nettyProto = new NettyProto(BARRAGE_PROTO, null);
        nettyProto.extend.put("code", code);
        nettyProto.extend.put("mess", mess);
        return JSONObject.toJSONString(nettyProto);
    }

    public static String buildAssMsgProto(int code, String mess) {
        NettyProto nettyProto = new NettyProto(ASS_MSG_PROTO, null);
        nettyProto.extend.put("code", code);
        nettyProto.extend.put("mess", mess);
        return JSONObject.toJSONString(nettyProto);
    }

    public static String buildSysMsgProto(int code, String mess) {
        NettyProto nettyProto = new NettyProto(SYS_MSG_PROTO, null);
        nettyProto.extend.put("code", code);
        nettyProto.extend.put("mess", mess);
        return JSONObject.toJSONString(nettyProto);
    }

    public static String buildRaceProto(int code, String mess) {
        NettyProto nettyProto = new NettyProto(EDRACE_PROTO, null);
        nettyProto.extend.put("code", code);
        nettyProto.extend.put("mess", mess);
        return JSONObject.toJSONString(nettyProto);
    }

    public static String buildNoticeProto(int code, String mess) {
        NettyProto nettyProto = new NettyProto(NOTICE_PROTO, null);
        nettyProto.extend.put("code", code);
        nettyProto.extend.put("mess", mess);
        return JSONObject.toJSONString(nettyProto);
    }

    public static String buildMessProto(String mess) {
        NettyProto nettyProto = new NettyProto(MESS_PROTO, mess);
        return JSONObject.toJSONString(nettyProto);
    }

    public static String buildProto(int head, String body) {
        NettyProto nettyProto = new NettyProto(head, body);
        return JSONObject.toJSONString(nettyProto);
    }

    public int getUri() {
        return uri;
    }

    public void setUri(int uri) {
        this.uri = uri;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Map<String, Object> getExtend() {
        return extend;
    }

    public void setExtend(Map<String, Object> extend) {
        this.extend = extend;
    }
}
