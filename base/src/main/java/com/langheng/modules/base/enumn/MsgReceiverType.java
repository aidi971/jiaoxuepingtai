package com.langheng.modules.base.enumn;

/**
 * @ClassName ClassesApplyState
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-03-30 11:21
 * @Version 1.0
 */
public enum MsgReceiverType {

    CLASSES("0", "班级"),
    PERSON("1", "个人");

    // 成员变量
    private String value;
    private String label;

    // 构造方法
    MsgReceiverType(String value, String label) {
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
