package com.langheng.modules.netty.manager;

import com.jeesite.common.collect.SetUtils;
import com.jeesite.common.entity.TokenInfo;
import com.jeesite.common.enumn.RedisKeyConst;
import com.jeesite.common.utils.JsonUtils;
import com.jeesite.common.utils.RedisUtils;
import com.jeesite.common.web.response.GlobalStatusCode;
import com.langheng.modules.base.utils.ClassesUtils;
import com.langheng.modules.base.utils.JwtUtils;
import com.langheng.modules.netty.model.Message;
import com.langheng.modules.netty.model.NettyProto;
import com.langheng.modules.netty.model.SysMsgProto;
import com.langheng.modules.netty.model.TeamProto;
import com.langheng.modules.netty.utils.NettyUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @ClassName AbstractChannelManager
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-03-25 15:35
 * @Version 1.0
 */
public abstract class AbstractMultiChannelManager {

    protected final Logger logger = LoggerFactory.getLogger(AbstractMultiChannelManager.class);
    //读写锁
    protected ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(true);
    // 用户多开窗口  多次token连接
    protected ConcurrentMap<String, Set<Channel>> userIdMultiChannelSetMap = new ConcurrentHashMap<>();
    protected ConcurrentMap<Channel, TokenInfo> channelTokenMap = new ConcurrentHashMap<>();
    protected ConcurrentMap<String, String> userIdTokenMap = new ConcurrentHashMap<>();
    
    //总在线用户数
    protected AtomicInteger userCount = new AtomicInteger(0);

    /**
     * 添加认证通道信息
     * @param channel
     */
    public void addChannel(Channel channel, TokenInfo tokenInfo,Boolean isMainChannel) {
        boolean isNewLogin = true;
        String remoteAddress = NettyUtils.parseChannelRemoteAddr(channel);

        if (!channel.isActive()){
            logger.error("通道还没就绪, 地址: {}", remoteAddress);
        }
        
        if(userIdTokenMap.containsKey(tokenInfo.getUserId())){
            String oldToken = userIdTokenMap.get(tokenInfo.getUserId());
            // 判断当前token  与当前缓存的token是否一致。
            if ( !oldToken.equals(tokenInfo.getToken()) ){
                Set<Channel> oldChannelSet = getMultiTokenSet(tokenInfo.getUserId());
                // 发送验证失败消息
                defaultErrorAuthMessage(oldChannelSet);
                // 重置所有的缓存
                resetAllCache(tokenInfo.getUserId());
            }else {
                isNewLogin = false;
            }
        }

        if (isNewLogin){
            // 增加一个认证用户
            userCount.incrementAndGet();
            userIdTokenMap.putIfAbsent(tokenInfo.getUserId(),tokenInfo.getToken());
            ClassesUtils.addCacheAndUpdateOnlineNum(tokenInfo.getUserId());
            logger.info("增加一个认证用户, userId: {}", tokenInfo.getUserId());
        }

        tokenInfo.setMainChannel(isMainChannel);
        channelTokenMap.putIfAbsent(channel,tokenInfo);
        pushUserIdMultiChannelSet(tokenInfo.getUserId(),channel);
    }

    public void resetAllCache(String userId){
        Set<Channel> multiTokenSet = getMultiTokenSet(userId);
        // 删除多余的 channelTokenMap
        fixChannelTokenMap(multiTokenSet);
        // 删除记录
        userIdTokenMap.remove(userId);
        userIdMultiChannelSetMap.remove(userId);
    }

    public void fixChannelTokenMap(Set<Channel> oldChannelSet){
        oldChannelSet.forEach(channel -> {
            channelTokenMap.remove(channel);
        });
    }

    public TokenInfo getTokenInfo(Channel channel) {
        return channelTokenMap.get(channel);
    }

    public void pushUserIdMultiChannelSet(String userId,Channel channel){
        Set<Channel> multiTokenSet = userIdMultiChannelSetMap.get(userId);
        if (multiTokenSet == null){
            synchronized (ConcurrentSkipListSet.class){
                if (multiTokenSet == null){
                    multiTokenSet = new ConcurrentSkipListSet<>();
                    userIdMultiChannelSetMap.putIfAbsent(userId,multiTokenSet);
                }
            }
        }
        if (multiTokenSet != null){
            multiTokenSet.add(channel);
        }
    }

    public Set<Channel> getMultiTokenSet(String userId){
        Set<Channel> multiTokenSet = userIdMultiChannelSetMap.get(userId);
        if (multiTokenSet == null){
            multiTokenSet = SetUtils.newHashSet();
        }
        return multiTokenSet;
    }

    public void removeMultiTokenSet(String userId){
        Set<Channel> multiTokenSet = userIdMultiChannelSetMap.get(userId);
        if (multiTokenSet != null){
            multiTokenSet.forEach(channel->{
                channelTokenMap.remove(channel);
                channel.close();
            });
        }
        userIdMultiChannelSetMap.remove(userId);
    }


    /**
     * 广播系统消息  授权失败
     */
    public void broadCastErrorAuth(List<String> receiveIds, String mess) {
        try {
            rwLock.readLock().lock();
            receiveIds.forEach(userId->{
                Set<Channel> multiTokenSet = getMultiTokenSet(userId);
                Set<Channel> failChannelSet = SetUtils.newHashSet();
                for (Channel channel: multiTokenSet){
                    if (channel.isActive()){
                        defaultErrorAuthMessage(channel,mess);
                    }else {
                        failChannelSet.add(channel);
                    }
                }
                if (!failChannelSet.isEmpty()){
                    // 移除过期的通道
                    multiTokenSet.removeAll(failChannelSet);
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
                Set<Channel> multiTokenSet = getMultiTokenSet(userId);
                Set<Channel> failChannelSet = SetUtils.newHashSet();
                for (Channel channel: multiTokenSet){
                    if (channel.isActive()){
                        writeAndFlush(channel, new TextWebSocketFrame(NettyProto.buildSysMsgProto(SysMsgProto.SYS_MSG_SERVER_SEND_MSG, contentJson)));
                    }else {
                        failChannelSet.add(channel);
                    }
                }
                if (!failChannelSet.isEmpty()){
                    // 移除过期的通道
                    multiTokenSet.removeAll(failChannelSet);
                }
            });
        } finally {
            rwLock.readLock().unlock();
        }
    }

    /**
     * 移除主socket
     * @param channel
     */
    public void removeMainChannel(Channel channel) {
        try {
            logger.warn("移除通道, 地址:{}", NettyUtils.parseChannelRemoteAddr(channel));
            rwLock.writeLock().lock();
            TokenInfo tokenInfo = channelTokenMap.get(channel);
            Set<Channel> multiTokenSet = getMultiTokenSet(tokenInfo.getUserId());
            multiTokenSet.remove(channel);
            channelTokenMap.remove(channel);
            channel.close();
            // 判断主socket是否全部消亡
            if (checkMainChannelAllDie(tokenInfo.getUserId())){
                // 延期处理   （防止页面刷新引起掉线）
                String lockKey = RedisKeyConst.REMOVE_MAIN_CHANNEL_LOCK_KEY_PREFIX + tokenInfo.getUserId();
                RedisUtils.set(lockKey,tokenInfo.getUserId());
                RedisUtils.expire(lockKey,15);
                delayToRemoveMinorSocket(tokenInfo);
            }
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public void delayToRemoveMinorSocket(TokenInfo tokenInfo){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                if (checkMainChannelAllDie(tokenInfo.getUserId())){
                    String lockKey = RedisKeyConst.REMOVE_MAIN_CHANNEL_LOCK_KEY_PREFIX + tokenInfo.getUserId();
                    if (RedisUtils.get(lockKey) == null){  // 无锁的情况   防止重复刷新
                        Set<Channel> multiTokenSet = getMultiTokenSet(tokenInfo.getUserId());
                        // 延期处理 （防止页面刷新引起掉线）
                        defaultErrorAuthMessage(multiTokenSet,"您的帐号已下线，请重新登录");
                        resetAllCache(tokenInfo.getUserId());
                        JwtUtils.removeTokenInfo(tokenInfo);
                        // 清除缓存 更新在线人数
                        ClassesUtils.cleanCacheAndUpdateOnlineNum(tokenInfo.getUserId());
                    }
                }
            }
        }, 15000);// 设定指定的时间time,此处为15000毫秒
    }

    /**
     * 移除次要socket
     * @param channel
     */
    public void removeMinorChannel(Channel channel) {
        try {
            logger.warn("移除通道, 地址:{}", NettyUtils.parseChannelRemoteAddr(channel));
            rwLock.writeLock().lock();
            TokenInfo tokenInfo = channelTokenMap.get(channel);
            Set<Channel> multiTokenSet = getMultiTokenSet(tokenInfo.getUserId());
            multiTokenSet.remove(channel);
            channelTokenMap.remove(channel);
            channel.close();
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    /**
     * 检查是否有存活的主scoket， 没有则返回true
     * @param userId
     * @return
     */
    private boolean checkMainChannelAllDie(String userId) {
        Set<Channel> multiTokenSet = getMultiTokenSet(userId);
        for (Channel channel: multiTokenSet){
            TokenInfo tokenInfo = channelTokenMap.get(channel);
            if (tokenInfo != null && tokenInfo.getMainChannel() && channel.isActive()){
                return false;
            }
        }
        return true;
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
        defaultErrorAuthMessage(ch,"您的帐号在其它地方登录，您已被迫下线");
    }

    /**
     * 错误信息
     * @param channelSet
     */
    public void defaultErrorAuthMessage( Set<Channel> channelSet,String mess){
        channelSet.forEach(channel -> {
            defaultErrorAuthMessage(channel,mess);
        });
    }

    /**
     * 错误信息
     * @param channelSet
     */
    public void defaultErrorAuthMessage( Set<Channel> channelSet){
        channelSet.forEach(channel -> {
            defaultErrorAuthMessage(channel);
        });
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
     * 广播下载消息（向客户端发送）
     */
    public void broadCastDownloadMsg(String userId, String contentJson) {
        try {
            rwLock.readLock().lock();
            Set<Channel> multiTokenSet = getMultiTokenSet(userId);
            Set<Channel> failChannelSet = SetUtils.newHashSet();
            for (Channel channel: multiTokenSet){
                if (channel.isActive()){
                    writeAndFlush(channel, new TextWebSocketFrame(NettyProto.buildSysMsgProto(TeamProto.TEAM_DOWNLOAD_ERROR, contentJson)));
                }else {
                    failChannelSet.add(channel);
                }
            }
            if (!failChannelSet.isEmpty()){
                // 移除过期的通道
                multiTokenSet.removeAll(failChannelSet);
            }
        } finally {
            rwLock.readLock().unlock();
        }
    }


}
