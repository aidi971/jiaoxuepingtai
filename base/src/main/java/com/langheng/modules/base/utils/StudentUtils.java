package com.langheng.modules.base.utils;

import com.jeesite.common.exception.PermissionException;
import com.jeesite.common.utils.SpringUtils;
import com.langheng.modules.base.entity.BaseUser;
import com.langheng.modules.base.entity.Student;
import com.langheng.modules.base.enumn.UserType;
import com.langheng.modules.base.service.StudentService;

/**
 * @ClassName StudentUtils
 * @Description TODO
 * @Author xiaoxie
 * @Date 2019-12-19 14:08
 * @Version 1.0
 */
public class StudentUtils {

    private static StudentService studentService = SpringUtils.getBean(StudentService.class);

    public static Student getStudent(){
        BaseUser user = BaseUserUtils.getUser();
        if (user != null && UserType.STUDENT.value().equals(user.getUserType())){
            return studentService.get(user.getId());
        }
        throw new PermissionException("您无权限操作！");
    }
}
