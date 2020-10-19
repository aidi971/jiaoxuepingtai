package com.langheng.modules.netty.model;

/**
 * @ClassName StatusCode
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-03-25 17:14
 * @Version 1.0
 */
public enum StatusCode {

   PING_CODE("10015","ping消息"),
   PONG_CODE("10016","pong消息");

    // 成员变量
    private String value;
    private String label;

    // 构造方法
    StatusCode(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String label() {
        return this.label;
    }

    public String value() {
        return this.value;
    }
}
