package com.langheng.modules.base.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @ClassName TeachingClassEvent
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-04-01 15:04
 * @Version 1.0
 */
@Getter
public class SystemEvent extends ApplicationEvent {

    private List<String> receiverIds;

    //关联对象
    private String contentJson;

    public SystemEvent(Object source, List<String> receiverIds, String contentJson ) {
        super(source);
        this.contentJson = contentJson;
        this.receiverIds = receiverIds;
    }
}
