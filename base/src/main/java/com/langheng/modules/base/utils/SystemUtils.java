package com.langheng.modules.base.utils;

import com.jeesite.common.collect.ListUtils;
import com.langheng.modules.base.manager.AsyncManager;
import com.langheng.modules.base.manager.factory.AsyncFactory;

import java.util.List;

public class SystemUtils {

    public static void tickOutUser(String userId){
        // 异步发送通知
        AsyncManager.me().execute(AsyncFactory.pushTickOutMsg(ListUtils.newArrayList(userId)));
    }

    public static void tickOutUsers(List<String> userIds){
        // 异步发送通知
        AsyncManager.me().execute(AsyncFactory.pushTickOutMsg(userIds));
    }
}
