package com.langheng.modules.ed.util;

import com.jeesite.common.utils.SpringUtils;
import com.langheng.modules.base.entity.Msg;
import com.langheng.modules.base.enumn.MsgReceiverType;
import com.langheng.modules.base.utils.MsgUtils;
import com.langheng.modules.ed.entity.TeachingClass;
import com.langheng.modules.ed.service.TeachClassLessonService;
import com.langheng.modules.ed.service.TeachingClassService;

public class TeachClassMsgUtils {

    private static  TeachingClassService teachingClassService = SpringUtils.getBean(TeachingClassService.class);
    private static TeachClassLessonService teachClassLessonService = SpringUtils.getBean(TeachClassLessonService.class);

    public static void pushTeachClassMsg(String teachingClassId){
        try{
            TeachingClass teachingClass = teachingClassService.get(teachingClassId);
            String fullLessonName = teachClassLessonService.findLastPush(teachingClassId);
            Msg msg = new Msg();
            msg.setContent(fullLessonName);
            msg.setReceiverType(MsgReceiverType.CLASSES.value());
            msg.setReceiverKey(teachingClass.getClasses().getClassesId());
            msg.setBizKey(teachingClassId);
            msg.setTeachingClassId(teachingClassId);
            MsgUtils.pushTeachClassTaskMsg(msg);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
