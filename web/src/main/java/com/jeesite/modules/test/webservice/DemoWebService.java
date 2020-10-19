package com.jeesite.modules.test.webservice;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

public class DemoWebService {

    public static void main(String[] args) {
        // 创建动态客户端
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient("http://localhost:20020/Service1");
        // 需要密码的情况需要加上用户名和密码
        // client.getOutInterceptors().add(new ClientLoginInterceptor(USER_NAME, PASS_WORD));
        Object[] objects = new Object[0];
        try {
            // invoke("方法名",参数1,参数2,参数3....);
            objects = client.invoke("GetData", 337926);
            // code = 401  没插入加密狗
            //  { code = 0, msg = 获取信息成功, Points = 60, EndTime = "永久" }
            //  { code = 0, msg = 获取信息成功, Points = 60, EndTime = "Date().getTime()" }
            System.out.println("返回数据:" + objects[0]);
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }

}
