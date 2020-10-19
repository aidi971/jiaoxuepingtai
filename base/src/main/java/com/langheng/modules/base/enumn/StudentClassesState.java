package com.langheng.modules.base.enumn;

/**
 * @ClassName StudentClassesState
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-02-20 11:56
 * @Version 1.0
 */
public enum  StudentClassesState {

    NORMAL("0", "正常"),
    DISABLE("2", "停用");

    // 成员变量
    private String value;
    private String label;

    // 构造方法
    StudentClassesState(String value, String label) {
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
