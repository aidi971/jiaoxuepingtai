package com.jeesite.common.enumn;

public enum StatusEnum {

    NORMAL("0", "正常"),
    DISABLE("2", "停用"),
    DELETE("1", "删除");

    // 成员变量
    private String value;
    private String label;

    // 构造方法
    StatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String label() {
        return this.label;
    }

    public String value() {
        return this.value;
    }
}
