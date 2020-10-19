package com.langheng.modules.base.utils;

import com.jeesite.common.cache.CacheUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.exception.LicenseException;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.common.utils.RedisUtils;
import com.langheng.modules.base.webservice.JaxWsDynamicClientFactory;
import org.apache.cxf.endpoint.Client;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public class LicenseUtils {

    public static void clearCache(){
        CacheUtils.remove("license", Global.getConfig("jwt.secret"));
    }

    private static String getLicenseInfo() throws Exception{

        String result =  CacheUtils.get("license", Global.getConfig("jwt.secret"));
        if (StringUtils.isBlank(result)){
            JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
            String port = Global.getConfig("license.base-function.port","20020");
            Client client = dcf.createClient("http://localhost:" + port + "/Service1");
            Object[] objects = new Object[0];
            objects = client.invoke("GetData", 337926);
            result =  (String)objects[0];
            CacheUtils.put("license", Global.getConfig("jwt.secret"),result);
        }

        return result;
    }

    public static void checkLicense(boolean isLogin){

        // 开发环境直接return
        if (true){
            return;
        }

        String licenseErrorMsg =  "未经授权，面临法律制裁！";
        if (isLogin){
            licenseErrorMsg = "未检测到加密锁！";
        }

        try{
            String result = getLicenseInfo();
            Map<String,Object> licenseInfoMap = JsonMapper.fromJson(result, Map.class);
            if (licenseInfoMap.get("code").equals(0)){
                if ("永久".equals(licenseInfoMap.get("EndTime"))){
                    if (isLogin && !checkLicensePoints((Integer)licenseInfoMap.get("Points"))){
                        throw new LicenseException("当前在线人数已达授权上限！");
                    }
                }else {
                    Date endTime = new Date(Long.parseLong(licenseInfoMap.get("EndTime").toString()) * 1000);
                    if (new Date().after(endTime)){
                        throw new LicenseException("授权过期！");
                    }else if (isLogin && !checkLicensePoints((Integer)licenseInfoMap.get("Points"))){
                        throw new LicenseException("当前在线人数已达授权上限！");
                    }
                }
            }else  if (licenseInfoMap.get("code").equals(401)){
                throw new LicenseException("未检测到加密锁！");
            } else {
                throw new LicenseException(licenseErrorMsg);
            }
        }catch (LicenseException e){
            // 向上抛
            throw e;
        }catch (Exception e){
            e.printStackTrace();
            throw new LicenseException(licenseErrorMsg);
        }
    }

    /**
     * 校验授权点数
     */
    private static boolean checkLicensePoints(Integer points) {
        Set set = RedisUtils.keys("user:token:*");
        if (set!= null){
           if ( set.size() > points){
               return false;
           }
        }
        return true;
    }

}
