package com.jeesite.common.web.response;

/**
 * @ClassName GlobalStatusCode
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-03-26 13:55
 * @Version 1.0
 */
public enum GlobalStatusCode {
    CODE_LICENSE_ERROR(10300, "许可证授权错误"),
    CODE_AUTH_ERROR(40300, "授权失败"),
    CODE_VERIFY_ERROR(40301, "请输入验证码！"),
    CODE_PERMISSION_ERROR(40100, "没有权限"),
    CODE_SUCCESS(20000, "请求成功"),
    CODE_NOT_FOUND(40400, "请求找不到！"),
    CODE_INVALID_PARAMETER(40000, "参数异常！"),
    CODE_ERROR(50000, "服务器忙,请稍后再试！"),
    CODE_SQL_ERROR(960229, "sql异常，重新请求");

    // 成员变量
    private int code;
    private String value;

    // 构造方法
    GlobalStatusCode(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer code() {
        return code;
    }


    public String value() {
        return value;
    }

}
