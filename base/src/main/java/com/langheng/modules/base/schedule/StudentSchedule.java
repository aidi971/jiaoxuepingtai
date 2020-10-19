package com.langheng.modules.base.schedule;

import com.jeesite.common.config.Global;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.utils.SpringUtils;
import com.langheng.modules.base.entity.Student;
import com.langheng.modules.base.enumn.EventHandlingType;
import com.langheng.modules.base.service.StudentService;
import com.langheng.modules.base.utils.RegisterUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


/**
 * 班级任务调度
 * @ClassName NoticeSchedule
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-04-02 9:08
 * @Version 1.0
 */
@Component
@EnableScheduling
public class StudentSchedule {

    StudentService studentService = SpringUtils.getBean(StudentService.class);

    /**
     * 删除超时未加入班级的学生
     * 每隔1分钟扫描一次
     */
    @Scheduled(cron="0 */1 * * * ?")
    public void deleteUnJoinClassesStudent(){
        Integer minute = Integer.parseInt(Global.getConfig("sys.account.register.expiry.time", "1440"));
        Date limitTime = DateUtils.addMinutes(new Date(),- minute);
        List<String> studentIds = studentService.selectUnJoinClassesStudent(limitTime);
        studentIds.forEach(studentId->{
            Student student = new Student(studentId);
            studentService.phyDelete(student);
            RegisterUtils.revokeUser(student,"系统", EventHandlingType.AUTOMATIC);
        });
    }
}
