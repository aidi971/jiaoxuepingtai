package com.langheng.modules.base.webservice.enumn;

public enum WebVerify {
    TIMEAUTHORIZATION(30407, "授权过期"),
    ENCRYPTEDAUTHORIZATION(30406, "未经授权，面临法律制裁");
    // 成员变量
    private Integer value;
    private String label;

    // 构造方法
    WebVerify(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    public String label() {
        return this.label;
    }

    public Integer value() {
        return this.value;
    }

}
