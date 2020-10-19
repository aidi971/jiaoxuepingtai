package com.langheng.modules.netty.model;

/**
 * @ClassName BarrageProto
 * @Description TODO
 *
 * @Author xiaoxie
 * @Date 2020-03-31 9:52
 * @Version 1.0
 */
public class SystemProto {

    public static final int SYS_MSG_PROTO = 63000; //系统消息
    public static final int SYS_KEEP_ALIVE = 63001; // 客户端告知服务器续期
    public static final int SYS_MSG_SUCCESS = 63200; //公告操作成功消息
    public static final int  SYS_MSG_SERVER_SEND_MSG = 63004;  // 服务端群发系统消息(客户端收到系统消息)


}
