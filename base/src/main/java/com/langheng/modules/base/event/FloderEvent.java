package com.langheng.modules.base.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;


@Getter
public class FloderEvent extends ApplicationEvent {

    // 创建文件夹
    public final static String CREAT_USER_FIODER = "1";

    // 事件类型
    private String userId;

    // 事件类型
    private String eventType;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public FloderEvent(Object source, String eventType,String userId  ) {
        super(source);
        this.eventType = eventType;
        this.userId = userId;

    }
}
