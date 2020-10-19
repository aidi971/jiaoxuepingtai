package com.jeesite.update;

import com.jeesite.modules.Application;
import com.langheng.modules.base.entity.Classes;
import com.langheng.modules.base.entity.Student;
import com.langheng.modules.base.service.StudentClassesService;
import com.langheng.modules.base.service.StudentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
@Rollback(false)
public class Update0819 {

    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentClassesService studentClassesService;

    @Test
    public void fixStudentClasses(){

        Student stuCriteria = new Student();
        stuCriteria.selectClassesId_isNull();
        List<Student> studentList = studentService.findList(stuCriteria);

        for (Student student: studentList){
            try{
                Classes classes = studentClassesService.getCurrentStudentClasses(student);
                if (classes!=null){
                    student.setClassesId(classes.getClassesId());
                    studentService.update(student);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
