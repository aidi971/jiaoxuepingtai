package com.langheng.modules.base.enumn;

/**
 * @ClassName ClassesApplyState
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-03-30 11:21
 * @Version 1.0
 */
public enum  ClassesApplyState {

    PENDING("0", "申请中"),
    AGREE("1", "已同意"),
    REJECT("2", "已拒绝"),
    OVERDUE("3", "已过期");

    // 成员变量
    private String value;
    private String label;

    // 构造方法
    ClassesApplyState(String value, String label) {
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
