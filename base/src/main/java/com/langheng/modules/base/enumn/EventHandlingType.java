package com.langheng.modules.base.enumn;

/**
 * @ClassName TemplateType
 * @Description TODO
 * @Author xiaoxie
 * @Date 2019-12-19 15:11
 * @Version 1.0
 */
public enum EventHandlingType {

    MANUAL("1", "手动"),
    AUTOMATIC("2", "自动");

    // 成员变量
    private String value;
    private String label;

    // 构造方法
    EventHandlingType(String value, String label) {
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
