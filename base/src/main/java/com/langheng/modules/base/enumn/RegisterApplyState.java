package com.langheng.modules.base.enumn;

public enum  RegisterApplyState {

    UN_APPLY("0", "未申请"),
    AGREE("1", "已同意"),
    PENDING("2", "审核中"),
    REJECT("3", "拒绝");

    // 成员变量
    private String value;
    private String label;

    // 构造方法
    RegisterApplyState(String value, String label) {
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
