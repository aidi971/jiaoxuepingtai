package com.langheng.modules.ed.listener;

import com.jeesite.common.enumn.StatusEnum;
import com.jeesite.common.lang.StringUtils;
import com.langheng.modules.base.entity.Classes;
import com.langheng.modules.base.entity.Student;
import com.langheng.modules.base.enumn.EventHandlingType;
import com.langheng.modules.base.event.TeachingClassEvent;
import com.langheng.modules.base.service.StudentService;
import com.langheng.modules.base.utils.RegisterUtils;
import com.langheng.modules.ed.entity.TeachingClass;
import com.langheng.modules.ed.service.TeachingClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName TeachingClassListener
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-04-01 15:07
 * @Version 1.0
 */
@Component
public class TeachingClassListener {

    @Autowired
    private TeachingClassService teachingClassService;
    @Autowired
    private StudentService studentService;


    @EventListener
    public void teachingClassEvent(TeachingClassEvent teachingClassEvent){

        String eventType = teachingClassEvent.getEventType();
        switch (eventType){
            case TeachingClassEvent.DELETE_TEACHING_CLASS : {   // 删除课堂
                Classes classes =  teachingClassEvent.getClasses();
                if (classes != null && StringUtils.isNotBlank(classes.getClassesId())){
                    TeachingClass teachingClass = new TeachingClass();
                    teachingClass.setClasses(classes);
                    List<TeachingClass> teachingClassList = teachingClassService.findList(teachingClass);
                    teachingClassList.forEach(item->{
                        teachingClassService.delete(item);
                    });

                    Student stuCriteria = new Student();
                    stuCriteria.setClassesId(classes.getClassesId());
                    List<Student> studentList = studentService.findList(stuCriteria);
                    studentList.forEach(student -> {
                        studentService.disable(student);
                    });
                }
                return;
            }case TeachingClassEvent.ENABLE_TEACHING_CLASS : {    // 恢复课堂
                Classes classes =  teachingClassEvent.getClasses();
                if (classes != null && StringUtils.isNotBlank(classes.getClassesId())){
                    TeachingClass teachingClass = new TeachingClass();
                    teachingClass.setClasses(classes);
                    teachingClass.disableStatus();
                    teachingClass.setStatus_in(new String[]{StatusEnum.DELETE.value()});
                    List<TeachingClass> teachingClassList = teachingClassService.findList(teachingClass);
                    teachingClassList.forEach(item->{
                        item.setStatus(StatusEnum.NORMAL.value());
                        teachingClassService.updateStatus(item);
                    });

                    List<Student> studentList = studentService.findListByClasses(new Student(),classes.getClassesId());
                    studentList.forEach(student -> {
                        studentService.enable(student);
                    });

                }
                return;
            } case TeachingClassEvent.PHY_DELETE_TEACHING_CLASS: {
                Classes classes =  teachingClassEvent.getClasses();
                if (classes != null && StringUtils.isNotBlank(classes.getClassesId())){
                    List<Student> studentList = studentService.findListByClasses(new Student(),classes.getClassesId() );
                    studentList.forEach(student -> {
                        // 物理删除学生
                        studentService.phyDelete(student);
                        RegisterUtils.revokeUser(student,"系统", EventHandlingType.AUTOMATIC);
                    });
                }
                return;
            }default:{

            }
        }

    }

}
