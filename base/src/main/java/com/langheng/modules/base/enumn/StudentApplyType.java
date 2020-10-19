package com.langheng.modules.base.enumn;

/**
 * @ClassName StudentApplyType
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-02-20 17:21
 * @Version 1.0
 */
public enum  StudentApplyType {

    ENTER_CLASSES("0", "加入班级申请");

    // 成员变量
    private String value;
    private String label;

    // 构造方法
    StudentApplyType(String value, String label) {
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
