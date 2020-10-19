package com.langheng.modules.ed.enumn;

/**
 * @ClassName TemplateType
 * @Description TODO
 * @Author xiaoxie
 * @Date 2019-12-19 15:11
 * @Version 1.0
 */
public enum TemplateState {

    UN_AUDITED("0", "未审核"),
    HAD_PUBLIC("1", "已公开"),
    CANCEL("2", "已取消"),
    HAD_REJECTED("3", "已拒绝");

    // 成员变量
    private String value;
    private String label;

    // 构造方法
    TemplateState(String value, String label) {
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
