package com.langheng.modules.ed.enumn;

/**
 * @ClassName TemplateType
 * @Description TODO
 * @Author xiaoxie
 * @Date 2019-12-19 15:11
 * @Version 1.0
 */
public enum  TemplateType {

    STANDARD("standard", "标准模板"),
    PERSONAL("personal", "个人模板");

    // 成员变量
    private String value;
    private String label;

    // 构造方法
    TemplateType(String value, String label) {
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
