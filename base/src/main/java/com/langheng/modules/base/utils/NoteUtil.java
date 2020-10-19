package com.langheng.modules.base.utils;

import java.util.UUID;

public class NoteUtil {

    public static String getUUIDInOrderId() {
        Integer orderId = UUID.randomUUID().toString().hashCode();
        orderId = orderId < 0 ? -orderId : orderId; //String.hashCode() 值会为空
        return orderId.toString();
    }

    public static String createID() {
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();
        return id.replace("-", "");
    }
}