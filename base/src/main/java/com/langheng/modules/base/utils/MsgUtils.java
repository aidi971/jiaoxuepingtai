package com.langheng.modules.base.utils;

import com.jeesite.common.lang.StringUtils;
import com.langheng.modules.base.entity.BaseUser;
import com.langheng.modules.base.entity.Msg;
import com.langheng.modules.base.enumn.MsgType;
import com.langheng.modules.base.manager.AsyncManager;
import com.langheng.modules.base.manager.factory.AsyncFactory;

import java.util.Date;

public class MsgUtils {

    private static String TEACH_CLASS_TASK_TITLE = "学习任务通知";
    private static String NOTICE_TITLE = "公告通知";
    private static String DISCUSS_TITLE = "讨论区消息";
    private static String TEMPLATE_AUDIT_TITLE = "模板审核结果通知";


    /**
     * 推送课堂任务
     * @param msg
     */
    public static void pushTeachClassTaskMsg(Msg msg){
        msg.setTitle(TEACH_CLASS_TASK_TITLE);
        msg.setType(MsgType.TEACH_CLASS_TASK.value());
        BaseUser currentUser = BaseUserUtils.getUser();
        msg.setSenderCode(currentUser.getUserCode());
        msg.setSenderName(currentUser.getUserName());
        //调用公告推送的方法
        push(msg);
    }

    /**
     * 推送公告
     * @param msg
     */
    public static void pushNoticeMsg(Msg msg){
        if (StringUtils.isBlank(msg.getTitle())){
            msg.setTitle(NOTICE_TITLE);
        }
        msg.setType(MsgType.NOTICE.value());
        msg.setSenderCode("system");
        msg.setSenderName("系统消息");
        //调用公告推送的方法
        push(msg);
    }

    /**
     * 推送讨论相关公告
     * @param msg
     */
    public static void pushDiscussMsg(Msg msg){
        msg.setTitle(DISCUSS_TITLE);
        msg.setType(MsgType.DISCUSS.value());
        msg.setSenderCode("system");
        msg.setSenderName("系统消息");
        //调用班级的方法
        push(msg);
    }

    /**
     * 推送班级
     * @param msg
     */
    public static void pushClassesMsg(Msg msg){
        msg.setType(MsgType.CLASSES.value());
        msg.setSenderCode("system");
        msg.setSenderName("系统消息");
        //调用班级的方法
        push(msg);
    }

    /**
     * 推送模板审核消息
     * @param msg
     */
    public static void pushTemplateAuditMsg(Msg msg) {
        msg.setTitle(TEMPLATE_AUDIT_TITLE);
        msg.setType(MsgType.TEMPLATE_AUDIT.value());
        BaseUser currentUser = BaseUserUtils.getUser();
        msg.setSenderCode(currentUser.getUserCode());
        msg.setSenderName(currentUser.getUserName());
        push(msg);
    }

    public static void push(Msg msg){
        msg.setSendTime(new Date());
        // 异步发送通知
        AsyncManager.me().execute(AsyncFactory.pushSysMsg(msg));
    }


}
