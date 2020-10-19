package com.langheng.modules.base.enumn;

/**
 * @ClassName StudentOriginType
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-02-18 16:30
 * @Version 1.0
 */
public enum  StudentOriginType {

    ADD("0", "新增"),
    IMPORT("1", "导入"),
    VIRTUAL("2", "虚拟"),
    INVITE("3", "邀请");

    // 成员变量
    private String value;
    private String label;

    // 构造方法
    StudentOriginType(String value, String label) {
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
