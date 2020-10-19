package com.langheng.modules.base.utils;

import com.jeesite.common.exception.PermissionException;
import com.jeesite.common.utils.SpringUtils;
import com.langheng.modules.base.entity.BaseUser;
import com.langheng.modules.base.entity.Teacher;
import com.langheng.modules.base.enumn.UserType;
import com.langheng.modules.base.service.TeacherService;

/**
 * @ClassName TeacherUtils
 * @Description TODO
 * @Author xiaoxie
 * @Date 2019-12-19 14:08
 * @Version 1.0
 */
public class TeacherUtils {

    private static TeacherService teacherService = SpringUtils.getBean(TeacherService.class);

    public static Teacher getTeacher(){
        BaseUser user = BaseUserUtils.getUser();
        if (user != null && UserType.TEACHER.value().equals(user.getUserType())){
            return teacherService.get(user.getId());
        }
        throw new PermissionException("您无权限操作！");
    }
}
