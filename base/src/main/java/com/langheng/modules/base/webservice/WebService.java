package com.langheng.modules.base.webservice;

import com.alibaba.fastjson.JSON;
import com.jeesite.common.codec.AesUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.RedisUtils;
import com.langheng.modules.base.entity.Authorization;
import com.langheng.modules.base.utils.date.DateVUtil;
import org.apache.cxf.endpoint.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class WebService {
    @Value("${license.base-function.port}")
    public String port;

    public static final String YONGJIU="永久";

    public String webService() {
        // 创建动态客户端
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient("http://localhost:"+port+"/Service1");
        Object[] objects = new Object[0];
        try {
            // invoke("方法名",参数1,参数2,参数3....);
            objects = client.invoke("GetData", 558374);
            return objects[0].toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//时间戳对比
    public Boolean endTimeVerify(Authorization authorization) {
        if (authorization.getEndTime()!=null) {
            boolean currentTimeMillis = DateVUtil.currentTimeMillis(DateVUtil.getTimeMillis(), Integer.parseInt(authorization.getEndTime()));
            return currentTimeMillis;
        }
        return false;
    }
//时间戳转格式
    public String endTimeDate(Authorization authorization) {
        if (authorization.getEndTime()!=null) {
            if (authorization.getEndTime().equals(YONGJIU)) {
                return YONGJIU;
            }
            if (!authorization.getEndTime().equals(YONGJIU)) {
                String date = DateVUtil.timeStamp2Date(authorization.getEndTime(), null);
                Boolean endTimeVerify = endTimeVerify(authorization);
                if (endTimeVerify==false) {
                date=date+"(授权过期)";
            }
                return date;
            }
        }
        return null;
    }
    public Authorization getWebService(Boolean state) {
        String webService="";
        if (state) {
            webService= (String) RedisUtils.get("sys:jiaoxuepingtai:verify");
            if (StringUtils.isBlank(webService)) {
                state=false;
            }else {
                webService = AesUtils.decode(webService);
            }
        }
        if (state==false) {
            webService = webService();
            String encode = AesUtils.encode(webService);
            RedisUtils.set("sys:jiaoxuepingtai:verify", encode,28800);
        }
        Authorization authorization = JSON.parseObject(webService, Authorization.class);

        return authorization;
    }

    public Boolean endTimeVerify() {
        Authorization authorization = getWebService(true);
        if (authorization.getEndTime()!=null) {
            if (authorization.getEndTime().equals(YONGJIU)) {
                return true;
            }
            boolean currentTimeMillis = DateVUtil.currentTimeMillis(DateVUtil.getTimeMillis(), Integer.parseInt(authorization.getEndTime()));
            return currentTimeMillis;
        }
        return false;
    }
    //锁状态判断
    public Boolean codeVerify() {
        Authorization authorization = getWebService(true);
        if (authorization.getCode().equals("0")) {
            return true;
        }
        if (authorization.getCode().equals("401")) {
            return false;
        }

        return false;
    }


//    public static void main(String[] args) {
//        WebService webService = new WebService();
//        webService.webService();
//        System.out.println("date = " + date);
//        long a=1621299212;
//        ZoneId zonr = ZoneId.systemDefault();
//        LocalDateTime test = LocalDateTime.ofInstant(zonr,a);
//        Date utilDate = sdf.parse(time);
//        System.out.println(utilDate);//查看utilDate的值
//    }

}
