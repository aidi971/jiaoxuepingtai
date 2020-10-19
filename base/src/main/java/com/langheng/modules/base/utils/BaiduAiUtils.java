package com.langheng.modules.base.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.config.Global;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class BaiduAiUtils {

    /**
     * 此方法调用百度AIP来查询IP所在地域(YYR)
     * @param strIP（传入的IP地址）
     * @return
     */
    public static String getAddressByIP(String strIP) {
        try {

            String baseUrl = "https://api.map.baidu.com/location/ip?ak=hGalBqk8ciqKybGWj9HDDyYpBE9jPjGH&ip={0}&coor=bd09ll";
            String url = Global.getText(baseUrl,strIP);
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> httpEntity = new HttpEntity<String>(null, new HttpHeaders());
            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
            JSONObject resultJsonObject= JSON.parseObject(response.getBody());
            if("0".equals(resultJsonObject.get("status").toString())){
                JSONObject obj2= JSON.parseObject(resultJsonObject.get("content").toString());
                return obj2.get("address").toString();
            }else{
                return "局域网地址";
            }
        } catch (Exception e) {
            return "局域网地址";
        }
    }

    public static void main(String[] args) {
        BaiduAiUtils.getAddressByIP("218.19.137.69");
    }
}
