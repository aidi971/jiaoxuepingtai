package com.langheng.modules.base.utils;

import com.jeesite.common.utils.SpringUtils;
import com.langheng.modules.base.entity.UserHead;
import com.langheng.modules.base.service.UserHeadService;

/**
 * @ClassName UserHeadUtils
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-02-18 17:24
 * @Version 1.0
 */
public class CoverImgUtils {

    private static UserHeadService userHeadService = SpringUtils.getBean(UserHeadService.class);

    public static String getCoverImg(String type){
       UserHead userHead =  userHeadService.getUserHead(Integer.parseInt(type));
       if (userHead == null){
           return null;
       }
       return userHead.getHeadUrl();
    }
}
