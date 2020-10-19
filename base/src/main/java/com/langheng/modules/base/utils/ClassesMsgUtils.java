package com.langheng.modules.base.utils;

import com.langheng.modules.base.entity.Classes;
import com.langheng.modules.base.entity.Msg;
import com.langheng.modules.base.entity.Student;
import com.langheng.modules.base.enumn.MsgReceiverType;

public class ClassesMsgUtils {

    public static void pushClassesMsg(Student student, Classes classes, boolean hadJoin) {
        try{
            Msg msg = new Msg();
            if (hadJoin){
                msg.setTitle("学生加入班级申请通知");
                msg.setContent(student.getStudentName() + "  申请加入" + classes.getClassName() + "班级");
            }else {
                msg.setTitle("学生加入班级通知");
                msg.setContent(student.getStudentName() + "  加入" + classes.getClassName() + "班级");
            }
            msg.setReceiverType(MsgReceiverType.PERSON.value());
            msg.setReceiverKey(classes.getTeacher().getTeacherId());
            msg.setBizKey(classes.getClassesId());
            MsgUtils.pushClassesMsg(msg);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
