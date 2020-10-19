package com.langheng.modules.base.utils;

import com.jeesite.common.utils.SpringUtils;
import com.langheng.modules.base.entity.Classes;
import com.langheng.modules.base.service.ClassesService;

/**
 * @ClassName InvitationCodeUtils
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-02-18 14:53
 * @Version 1.0
 */
public class InvitationCodeUtils {

    private static ClassesService classesService = SpringUtils.getBean(ClassesService.class);

    public static String getInvitationCode(){
        StringBuilder sb = new StringBuilder();
        String strAll = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < 4; i++) {
            int f = (int) (Math.random()*62);
            sb.append(strAll.charAt(f));
        }
        Classes classes = new Classes();
        classes.setInvitationCode(sb.toString());
        long count = classesService.findCount(classes);
        if (count > 0){
            return getInvitationCode();
        }
        return sb.toString();
    }

}
