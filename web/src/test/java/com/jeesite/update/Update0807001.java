package com.jeesite.update;

import com.jeesite.modules.Application;
import com.langheng.modules.base.entity.Student;
import com.langheng.modules.base.enumn.StudentOriginType;
import com.langheng.modules.base.service.StudentService;
import com.langheng.modules.base.utils.RegisterUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
@Rollback(false)
public class Update0807001 {

    @Autowired
    private StudentService studentService;
    @Test
    public void registerStudent(){

        for (int index=121; index<140; index++){
            try {
                String loginCode = "testStu" + index;
                String studentName = "testStuName" + index;

                Student student = new Student();
                student.setLoginCode(loginCode);
                student.setStudentName(studentName);
                student.setPassword("666666");
                student.setOriginType(StudentOriginType.INVITE.value());
                student.setHost("127.0.0.0");
                studentService.save(student);

                // 记录注册
                RegisterUtils.recordRegister(student);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

}
