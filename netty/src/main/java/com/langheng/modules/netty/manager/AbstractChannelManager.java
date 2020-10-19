package com.langheng.modules.netty.manager;

import com.jeesite.common.entity.TokenInfo;
import com.jeesite.common.utils.JsonUtils;
import com.jeesite.common.web.response.GlobalStatusCode;
import com.langheng.modules.netty.model.Message;
import com.langheng.modules.netty.model.NettyProto;
import com.langheng.modules.netty.model.SysMsgProto;
import com.langheng.modules.netty.utils.NettyUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @ClassName AbstractChannelManager
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-03-25 15:35
 * @Version 1.0
 */
public abstract class AbstractChannelManager {

    protected final Logger logger = LoggerFactory.getLogger(AbstractChannelManager.class);
    //读写锁
    protected ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(true);
    protected ConcurrentMap<Channel, TokenInfo> channelTokenMap = new ConcurrentHashMap<>();
    protected ConcurrentMap<String, Channel> userChannelMap = new ConcurrentHashMap<>();
    //总在线用户数
    protected AtomicInteger userCount = new AtomicInteger(0);

    public TokenInfo getTokenInfo(Channel channel) {
        return channelTokenMap.get(channel);
    }

    /**
     * 添加认证通道信息
     * @param channel
     */
    public void addChannel(Channel channel, TokenInfo tokenInfo) {

        String remoteAddress = NettyUtils.parseChannelRemoteAddr(channel);

        if (!channel.isActive()){
            logger.error("通道还没就绪, 地址: {}", remoteAddress);
        }

        if(userChannelMap.containsKey(tokenInfo.getUserId())){
            Channel oldChannel = userChannelMap.get(tokenInfo.getUserId());
            // 移除当前用户
            removeAuthUser(oldChannel);
        }

        // 增加一个认证用户
        userCount.incrementAndGet();
        userChannelMap.putIfAbsent(tokenInfo.getUserId(), channel);
        channelTokenMap.putIfAbsent(channel,tokenInfo);
        logger.info("增加一个认证用户, userId: {}", tokenInfo.getUserId());

    }


    /**
     * 广播系统消息  授权失败
     */
    public void broadCastErrorAuth(List<String> receiveIds, String mess) {
        try {
            rwLock.readLock().lock();
            receiveIds.forEach(userId->{
                Channel channel = userChannelMap.get(userId);
                if (channel != null && channel.isActive()){
                    defaultErrorAuthMessage(channel,mess);
                }
            });
        } finally {
            rwLock.readLock().unlock();
        }
    }

    /**
     * 广播系统消息（向客户端发送系统消息）
     */
    public void broadCastMsg(List<String> receiveIds, String contentJson) {
        try {
            rwLock.readLock().lock();
            receiveIds.forEach(userId->{
                Channel channel = userChannelMap.get(userId);
                if (channel != null && channel.isActive()){
                    writeAndFlush(channel, new TextWebSocketFrame(NettyProto.buildSysMsgProto(SysMsgProto.SYS_MSG_SERVER_SEND_MSG, contentJson)));
                }
            });
        } finally {
            rwLock.readLock().unlock();
        }
    }


    /**
     * 从缓存中移除Channel，并且关闭Channel
     * @param channel
     */
    public void removeChannel(Channel channel) {
        try {
            rwLock.writeLock().lock();
            TokenInfo tokenInfo = channelTokenMap.get(channel);
            if (tokenInfo != null){
                logger.warn("移除通道, 地址:{}", NettyUtils.parseChannelRemoteAddr(channel));
                logger.info("去掉一个认证用户, userId: {}", tokenInfo.getUserId());
                userCount.decrementAndGet();
                channelTokenMap.remove(channel);
                userChannelMap.remove(tokenInfo.getUserId());
                channel.close();
            }
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    /**
     * 移除认证用户
     * @param channel
     */
    public void removeAuthUser(Channel channel) {
        // 发送验证失败消息
        defaultErrorAuthMessage(channel);
        removeChannel(channel);
    }


    /**
     * 向单个用户发送信息
     * @param message
     */
    public void sendMessage(Channel channel,Message message){
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
    public  void authMessage(boolean isSuccess, Channel ch){
        writeAndFlush(ch, new TextWebSocketFrame(NettyProto.buildAuthProto(isSuccess)));
    }

    /**
     * 错误信息
     * @param code
     * @param mess
     * @param ch
     */
    public  void errorMessage(int code, String mess, Channel ch){
        writeAndFlush(ch, new TextWebSocketFrame(NettyProto.buildErrorProto(code, mess)));
    }

    /**
     * 错误信息
     * @param ch
     */
    public void defaultErrorAuthMessage( Channel ch,String mess){
        writeAndFlush(ch, new TextWebSocketFrame(NettyProto.buildErrorProto(GlobalStatusCode.CODE_AUTH_ERROR.code(),mess)));
    }


    /**
     * 错误信息
     * @param ch
     */
    public void defaultErrorAuthMessage( Channel ch){
        defaultErrorAuthMessage(ch,"您被迫下线，如这不是您所为，您的密码很有可能泄漏");
    }


    /**
     * 发送系统消息
     *
     * @param code
     * @param mess
     */
    public  void sendInfo(Channel channel, int code, Object mess) {
        writeAndFlush(channel, new TextWebSocketFrame(NettyProto.buildSystProto(code, mess)));
    }

    /**
     * 发送pong请求
     * @param channel
     */
    public  void sendPong(Channel channel) {
        writeAndFlush(channel, new TextWebSocketFrame(NettyProto.buildPongProto()));
    }

    /**
     * 向通道推送消息
     * @param channel
     * @param textWebSocketFrame
     */
    public void writeAndFlush(Channel channel, TextWebSocketFrame textWebSocketFrame){
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

}
