package com.langheng.modules.base.schedule;

import com.jeesite.common.enumn.SysYesNoEnum;
import com.jeesite.common.utils.SpringUtils;
import com.langheng.modules.base.entity.Classes;
import com.langheng.modules.base.entity.ClassesApply;
import com.langheng.modules.base.enumn.ClassesApplyState;
import com.langheng.modules.base.service.ClassesApplyService;
import com.langheng.modules.base.service.ClassesService;
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
public class ClassesSchedule {

    ClassesService classesService = SpringUtils.getBean(ClassesService.class);
    ClassesApplyService classesApplyService = SpringUtils.getBean(ClassesApplyService.class);

    /**
     * 定时推送公告
     * 每隔一小时扫描一次
     */
    @Scheduled(cron="0 */1 * * * ?")
    public void closeInvite(){

        Classes classesCriteria = new Classes();
        classesCriteria.setOpenInvite(SysYesNoEnum.YES.value());
        classesCriteria.setInviteCloseTime_lte(new Date());

        List<Classes> classesList = classesService.findList(classesCriteria);
        classesList.forEach(classes -> {
            // 关闭邀请
            classes.setOpenInvite(SysYesNoEnum.NO.value());
            classes.setInvitationCode("");
            classesService.save(classes);

            ClassesApply classesApply = new ClassesApply();
            classesApply.setClasses(classes);
            classesApply.setState(ClassesApplyState.PENDING.value());

            List<ClassesApply> classesApplyList = classesApplyService.findList(classesApply);

            classesApplyList.forEach(item->{
                item.setState(ClassesApplyState.OVERDUE.value());
                classesApplyService.save(item);
            });

        });
    }
}
