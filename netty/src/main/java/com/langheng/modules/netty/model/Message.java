package com.langheng.modules.netty.model;

/**
 * @ClassName Message
 * @Description TODO
 * @Author xiaoxie
 * @Date 2019-10-31 8:33
 * @Version 1.0
 */
public class Message<T> {

    private String receiveId;   // 接受者id

    private Integer code;   // 消息码

    private T data;      // 业务端自定义消息

    public Message() {
    }

    public Message(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
