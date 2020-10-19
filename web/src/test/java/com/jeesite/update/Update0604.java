package com.jeesite.update;

import com.jeesite.modules.Application;
import com.langheng.modules.base.entity.ClassesApply;
import com.langheng.modules.base.entity.Student;
import com.langheng.modules.base.service.ClassesApplyService;
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
public class Update0604 {

    @Autowired
    private ClassesApplyService classesApplyService;

    @Test
    public void adjustClassesApply(){
        List<ClassesApply> classesApplyList = classesApplyService.findList(new ClassesApply());
        classesApplyList.forEach(classesApply -> {
            try{
                Student student = classesApply.getStudent();
                classesApply.setStuName(student.getUserName());
                classesApply.setLoginCode(student.getLoginCode());
                classesApplyService.save(classesApply);
            }catch (Exception e){ }
        });
    }

}
