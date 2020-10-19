package com.langheng.modules.ed.util;

import com.langheng.modules.base.entity.Msg;
import com.langheng.modules.base.entity.Teacher;
import com.langheng.modules.base.enumn.MsgReceiverType;
import com.langheng.modules.base.utils.MsgUtils;
import com.langheng.modules.ed.entity.Template;

public class TemplateMsgUtils {


    public static void pushTemplateAuditMsg(Template template, Teacher teacher,Boolean isAgree){
        try{
            Msg msg = new Msg();
            if (isAgree){
                msg.setContent(template.getFullName() + "  已通过");
            }else {
                msg.setContent(template.getFullName() + "  未通过");
            }
            msg.setReceiverType(MsgReceiverType.PERSON.value());
            msg.setReceiverKey(teacher.getTeacherId());
            msg.setBizKey(template.getTemplateId());
            MsgUtils.pushTemplateAuditMsg(msg);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
