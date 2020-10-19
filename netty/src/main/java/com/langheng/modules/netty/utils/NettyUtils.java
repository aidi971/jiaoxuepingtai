package com.langheng.modules.netty.utils;

import io.netty.channel.Channel;

import java.net.SocketAddress;

/**
 * @ClassName NettyUtils
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-03-25 15:55
 * @Version 1.0
 */
public class NettyUtils {

    /**
     * 获取Channel的远程IP地址
     * @param channel
     * @return
     */
    public static String parseChannelRemoteAddr(final Channel channel) {

        if(channel == null) {
            return "";
        }

        SocketAddress remote = channel.remoteAddress();
        final String addr = remote != null ? remote.toString() : "";

        if (addr.length() > 0) {
            int index = addr.lastIndexOf("/");
            if (index >= 0){
                return addr.substring(index + 1);
            }
            return addr;
        }
        return "";
    }


}
