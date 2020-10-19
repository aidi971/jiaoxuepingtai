package com.langheng.modules.base.event;

import com.langheng.modules.base.entity.Teacher;
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
public class TeacherEvent extends ApplicationEvent {

    // 删除教师
    public final static String DELETE_TEACHER = "1";
    // 回复教师
    public final static String DISABLE_TEACHER = "2";


    //关联对象
    private Teacher teacher;

    // 事件类型
    private String eventType;

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public TeacherEvent(Object source, String eventType, Teacher teacher ) {
        super(source);
        this.eventType = eventType;
        this.teacher = teacher;
    }
}
