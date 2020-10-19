package com.langheng.modules.ed.listener;

import com.langheng.modules.base.entity.Classes;
import com.langheng.modules.base.entity.Teacher;
import com.langheng.modules.base.event.TeacherEvent;
import com.langheng.modules.base.service.ClassesService;
import com.langheng.modules.base.service.StudentService;
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
public class TeacherListener {

    @Autowired
    private TeachingClassService teachingClassService;
    @Autowired
    ClassesService classesService;
    @Autowired
    private StudentService studentService;


    @EventListener
    public void teachingClassEvent(TeacherEvent teacherEvent){

        String eventType = teacherEvent.getEventType();
        switch (eventType){
            case TeacherEvent.DELETE_TEACHER :{}
            case TeacherEvent.DISABLE_TEACHER : {
                Teacher teacher =  teacherEvent.getTeacher();
                Classes classesCriteria = new Classes();
                classesCriteria.setTeacher(new Teacher(teacher.getTeacherId()));
                List<Classes> classesList = classesService.findList(classesCriteria);
                for (Classes classes: classesList){
                    classesService.delete(classes);
                }

                TeachingClass teachClassCriteria = new TeachingClass();
                teachClassCriteria.setTeacher(new Teacher(teacher.getTeacherId()));
                List<TeachingClass> teachingClassList = teachingClassService.findList(teachClassCriteria);
                for (TeachingClass teachingClass: teachingClassList){
                    teachingClassService.delete(teachingClass);
                }
                return;
            }default:{

            }
        }

    }

}
