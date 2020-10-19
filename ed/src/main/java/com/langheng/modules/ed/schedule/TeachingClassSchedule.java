package com.langheng.modules.ed.schedule;

import com.jeesite.common.utils.SpringUtils;
import com.langheng.modules.ed.entity.TeachingClass;
import com.langheng.modules.ed.enumn.TeachingClassState;
import com.langheng.modules.ed.service.TeachingClassService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@EnableScheduling
public class TeachingClassSchedule {

    private TeachingClassService teachingClassService = SpringUtils.getBean(TeachingClassService.class);

    /**
     * 每隔小时扫描一次
     */
    @Scheduled(cron="0 0 * * * ?")
    public void dealTeachingClassState(){
        List<TeachingClass> teachingClassList = teachingClassService.findList(new TeachingClass());
        teachingClassList.forEach(teachingClass -> {
            // 当前时间 小于开始时间
            if (new Date().before(teachingClass.getBeginTime())){
                if (!TeachingClassState.HAD_NOT_STARTED.value().equals(teachingClass.getState())){
                    teachingClass.setState(TeachingClassState.HAD_NOT_STARTED.value());
                    teachingClassService.save(teachingClass);
                }
            } // 当前时间 大于开始时间  小于结束
            else if(new Date().after(teachingClass.getBeginTime()) && new Date().before(teachingClass.getEndTime())){
                if (!TeachingClassState.PROGRESSING.value().equals(teachingClass.getState())){
                    teachingClass.setState(TeachingClassState.PROGRESSING.value());
                    teachingClassService.save(teachingClass);
                }
            } // 当前时间 大于结束时间
            else if(new Date().after(teachingClass.getEndTime())){
                if (!TeachingClassState.FINISHED.value().equals(teachingClass.getState())){
                    teachingClass.setState(TeachingClassState.FINISHED.value());
                    teachingClassService.save(teachingClass);
                }
            }

        });

    }
}
