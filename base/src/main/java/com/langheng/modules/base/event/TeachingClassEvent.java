package com.langheng.modules.base.event;

import com.langheng.modules.base.entity.Classes;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @ClassName TeachingClassEvent
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-04-01 15:04
 * @Version 1.0
 */
@Getter
public class TeachingClassEvent extends ApplicationEvent {

    // 删除课堂
    public final static String DELETE_TEACHING_CLASS = "1";
    // 回复课堂
    public final static String ENABLE_TEACHING_CLASS = "2";
    // 物理删除
    public final static String PHY_DELETE_TEACHING_CLASS = "3";


    //关联对象
    private Classes classes;

    // 事件类型
    private String eventType;

    public Classes getClasses() {
        return classes;
    }

    public void setClasses(Classes classes) {
        this.classes = classes;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public TeachingClassEvent(Object source,String eventType,Classes classes ) {
        super(source);
        this.eventType = eventType;
        this.classes = classes;
    }
}
