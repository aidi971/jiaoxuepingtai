package com.langheng.modules.base.utils;

import com.jeesite.common.utils.SpringUtils;
import com.langheng.modules.base.entity.Classes;
import com.langheng.modules.base.entity.Register;
import com.langheng.modules.base.entity.Student;
import com.langheng.modules.base.enumn.EventHandlingType;
import com.langheng.modules.base.enumn.RegisterApplyState;
import com.langheng.modules.base.service.RegisterService;
import org.springframework.beans.BeanUtils;

import java.util.Date;

public class RegisterUtils {

    private  static RegisterService registerService = SpringUtils.getBean(RegisterService.class);


    public static void recordRegister(Student student) {
        Register register = new Register();
        BeanUtils.copyProperties(student,register);
        register.setUserCode(student.getId());
        register.setUserName(student.getStudentName());
        register.setApplyState(RegisterApplyState.UN_APPLY.value());
        register.setIpAddress(BaiduAiUtils.getAddressByIP(student.getHost()));
        register.setRegisterTime(new Date());
        register.setIsNewRecord(true);
        registerService.save(register);
    }

    /**
     * 已加入班级
     * @param student
     * @param classes
     */
    public static void hadJoinClasses(Student student, Classes classes) {
        Register register = registerService.get(student.getId());
        if (register != null){
            register.setApplyState(RegisterApplyState.AGREE.value());
            register.setClasses(classes);
            register.setTeacher(classes.getTeacher());

            registerService.save(register);
        }
    }

    /**
     * 申请加入班级
     * @param student
     * @param classes
     */
    public static void applyJoinClasses(Student student, Classes classes) {
        Register register = registerService.get(student.getId());
        if (register != null){
            register.setApplyState(RegisterApplyState.PENDING.value());
            register.setClasses(classes);
            register.setTeacher(classes.getTeacher());

            registerService.save(register);
        }
    }

    public static void revokeUser(Student student, String handlerName, EventHandlingType eventHandlingType) {
        Register register = registerService.get(student.getId());
        if (register != null){
            register.setRevokeType(eventHandlingType.value());
            register.setHandlerName(handlerName);
            register.setRevokeTime(new Date());
            registerService.save(register);
        }
    }

    public static void rejectJoinClasses(Student student, Classes classes) {
        Register register = registerService.get(student.getId());
        if (register != null){
            register.setApplyState(RegisterApplyState.REJECT.value());
            register.setClasses(classes);
            register.setTeacher(classes.getTeacher());

            registerService.save(register);
        }
    }
}
