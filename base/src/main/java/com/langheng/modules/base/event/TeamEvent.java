package com.langheng.modules.base.event;


import lombok.Getter;
import org.springframework.context.ApplicationEvent;


@Getter
public class TeamEvent extends ApplicationEvent {

    private String userId;

    private String contentJson;

    public TeamEvent(Object source, String userId, String contentJson) {
        super(source);
        this.userId = userId;
        this.contentJson = contentJson;
    }
}