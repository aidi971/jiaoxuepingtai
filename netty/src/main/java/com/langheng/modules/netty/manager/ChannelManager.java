package com.langheng.modules.netty.manager;

import com.jeesite.common.entity.TokenInfo;
import com.jeesite.common.utils.JsonUtils;
import com.jeesite.common.web.response.GlobalStatusCode;
import com.langheng.modules.netty.model.Message;
import com.langheng.modules.netty.model.NettyProto;
import com.langheng.modules.netty.utils.NettyUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @ClassName ChannelManager
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-03-25 15:35
 * @Version 1.0
 */
public class ChannelManager {

    final static Logger logger = LoggerFactory.getLogger(ChannelManager.class);
    //读写锁
    protected static ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(true);

    protected static ConcurrentMap<Channel, TokenInfo> channelTokenMap = new ConcurrentHashMap<>();
    protected static ConcurrentMap<String, Channel> userChannelMap = new ConcurrentHashMap<>();

    //总在线用户数
    protected static AtomicInteger userCount = new AtomicInteger(0);


    /**
     * 添加认证通道信息
     * @param channel
     */
    public static void addChannel(Channel channel,TokenInfo tokenInfo) {
        String remoteAddress = NettyUtils.parseChannelRemoteAddr(channel);

        if (!channel.isActive()){
            logger.error("通道还没就绪, 地址: {}", remoteAddress);
        }

        if(userChannelMap.containsKey(tokenInfo.getUserId())){
            Channel oldChannel = userChannelMap.get(tokenInfo.getUserId());
            defaultErrorAuthMessage(oldChannel);
            removeChannel(oldChannel);
            userChannelMap.remove(tokenInfo.getUserId());
        }

        // 增加一个认证用户
        userCount.incrementAndGet();
        userChannelMap.putIfAbsent(tokenInfo.getUserId(), channel);
        channelTokenMap.putIfAbsent(channel,tokenInfo);
        logger.info("增加一个认证用户, userId: {}", tokenInfo.getUserId());
    }


    /**
     * 从缓存中移除Channel，并且关闭Channel
     * @param channel
     */
    public static void removeChannel(Channel channel) {
        try {
            logger.warn("移除通道, 地址:{}", NettyUtils.parseChannelRemoteAddr(channel));
            rwLock.writeLock().lock();
            channel.close();
            TokenInfo tokenInfo = channelTokenMap.get(channel);
            if (tokenInfo == null){
                return;
            }
            logger.info("去掉一个认证用户, userId: {}", tokenInfo.getUserId());

            TokenInfo tmp = channelTokenMap.remove(channel);
            if (tmp != null){
                // 减去一个认证用户
                userCount.decrementAndGet();
            }
            userChannelMap.remove(tokenInfo.getUserId());
        } finally {
            rwLock.writeLock().unlock();
        }
    }


    /**
     * 从缓存中移除Channel，并且关闭Channel
     */
    public static void removeChannel(String userId) {
        try {
            if (userChannelMap.containsKey(userId)){
                Channel channel = userChannelMap.get(userId);
                // 移除前先发送通知
                defaultErrorAuthMessage(channel);
                logger.warn("移除通道, 地址:{}", NettyUtils.parseChannelRemoteAddr(channel));
                rwLock.writeLock().lock();
                channel.close();
                TokenInfo tokenInfo = channelTokenMap.get(channel);
                if (tokenInfo == null){
                    return;
                }
                logger.info("去掉一个认证用户, userId: {}", tokenInfo.getUserId());

                TokenInfo tmp = channelTokenMap.remove(channel);
                if (tmp != null ){
                    // 减去一个认证用户
                    userCount.decrementAndGet();
                }
                userChannelMap.remove(tokenInfo.getUserId());
            }
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    /**
     * 广播普通消息
     *
     * @param message
     */
    public static void broadcastMess(int uid, String userId, String message) {
        if (!StringUtils.isBlank(message)) {
            try {
                rwLock.readLock().lock();
                Set<Channel> keySet = channelTokenMap.keySet();
                for (Channel ch : keySet) {
                    TokenInfo tokenInfo = channelTokenMap.get(ch);
                    if (tokenInfo == null ) continue;
//                    ch.writeAndFlush(new TextWebSocketFrame(NettyProto.buildMessProto(uid, nick, message)));
                }
            } finally {
                rwLock.readLock().unlock();
            }
        }
    }

    /**
     * 向单个用户发送信息
     * @param message
     */
    public static void sendMessage(Message message){

        try {
            rwLock.readLock().lock();
            Channel channel = userChannelMap.get(message.getReceiveId());
            if (channel != null){
                message.setReceiveId(null);
                String jsonData = JsonUtils.getJsonString(message);
                logger.info("发送消息 =  {}, receiveId = {}, channel = {}, code = {}, 通道状态 = {}", jsonData, message.getReceiveId(), channel,message.getCode(), channel.isActive());
                writeAndFlush(channel, new TextWebSocketFrame(NettyProto.buildMessProto(jsonData)));
            }
        } finally {
            rwLock.readLock().unlock();
        }
    }

    /**
     * 向单个用户发送信息
     * @param message
     */
    public static void sendMessage(Channel channel,Message message){

        try {
            rwLock.readLock().lock();
            if (channel != null){
                String jsonData = JsonUtils.getJsonString(message);
                logger.info("发送消息 =  {}, receiveId = {}, channel = {}, code = {}, 通道状态 = {}", jsonData, message.getReceiveId(), channel,message.getCode(), channel.isActive());
                writeAndFlush(channel, new TextWebSocketFrame(NettyProto.buildMessProto(jsonData)));
            }
        } finally {
            rwLock.readLock().unlock();
        }
    }

    /**
     * 授权成功信息
     * @param isSuccess
     * @param ch
     */
    public static void authMessage(boolean isSuccess, Channel ch){
        writeAndFlush(ch, new TextWebSocketFrame(NettyProto.buildAuthProto(isSuccess)));
    }

    /**
     * 错误信息
     * @param code
     * @param mess
     * @param ch
     */
    public static void errorMessage(int code, String mess, Channel ch){
        writeAndFlush(ch, new TextWebSocketFrame(NettyProto.buildErrorProto(code, mess)));
    }

    /**
     * 错误信息
     * @param ch
     */
    public static void defaultErrorAuthMessage( Channel ch){
        writeAndFlush(ch, new TextWebSocketFrame(NettyProto.buildErrorProto(GlobalStatusCode.CODE_AUTH_ERROR.code(), "token已过期，或者被踢下线！")));
    }

    /**
     * 广播系统消息
     */
    public static void broadCastInfo(int code, Object mess) {
        try {
            rwLock.readLock().lock();
            Set<Channel> keySet = channelTokenMap.keySet();
            for (Channel ch : keySet) {
                TokenInfo tokenInfo = channelTokenMap.get(ch);
                if (tokenInfo == null ) continue;
                writeAndFlush(ch, new TextWebSocketFrame(NettyProto.buildSystProto(code, mess)));
            }
        } finally {
            rwLock.readLock().unlock();
        }
    }

    /**
     * 发送ping指令心跳
     */
    public static void broadCastPing() {
        try {
            rwLock.readLock().lock();
            logger.info("进行ping操作 用户总数: {}", userCount.intValue());
            Set<Channel> keySet = channelTokenMap.keySet();
            for (Channel ch : keySet) {
                TokenInfo tokenInfo = channelTokenMap.get(ch);
                if (tokenInfo == null) continue;
                writeAndFlush(ch, new TextWebSocketFrame(NettyProto.buildPingProto()));
            }
        } finally {
            rwLock.readLock().unlock();
        }
    }

    /**
     * 发送系统消息
     *
     * @param code
     * @param mess
     */
    public static void sendInfo(Channel channel, int code, Object mess) {
        writeAndFlush(channel, new TextWebSocketFrame(NettyProto.buildSystProto(code, mess)));
    }

    /**
     * 发送pong请求
     * @param channel
     */
    public static void sendPong(Channel channel) {
        writeAndFlush(channel, new TextWebSocketFrame(NettyProto.buildPongProto()));
    }

    /**
     * 扫描并关闭失效的Channel
     */
    public static void scanNotActiveChannel() {
        Set<Channel> keySet = channelTokenMap.keySet();
        for (Channel ch : keySet) {
            TokenInfo tokenInfo = channelTokenMap.get(ch);
            if (tokenInfo == null) continue;
            if (!ch.isOpen() || !ch.isActive()
                    || (System.currentTimeMillis() - tokenInfo.getExpireTime().getTime() > 10000)) {
                removeChannel(ch);
            }
        }
    }

    public static TokenInfo getTokenInfo(Channel channel) {
        return channelTokenMap.get(channel);
    }

    public static Channel getChannel(String appId, String userId){
        return userChannelMap.get(getKey(appId, userId));
    }

    public static ConcurrentMap<Channel, TokenInfo> getUserInfos() {
        return channelTokenMap;
    }

    public static int getAuthUserCount() {
        return userCount.get();
    }

    protected static void writeAndFlush(Channel channel, TextWebSocketFrame textWebSocketFrame){
        String text = textWebSocketFrame.text();
        channel.writeAndFlush(textWebSocketFrame).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()){
                    logger.info("消息:{}发送成功.", text);
                    //todo 成功处理
                } else {
                    logger.info("消息:{}发送失败.", text);
                    //todo 失败处理
                }
            }
        });
    }

    public static String getKey(String appId, String userId){
        return String.format("%s_%s", appId, userId);
    }
}
