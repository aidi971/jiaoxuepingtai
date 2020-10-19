package com.langheng.modules.ed.enumn;

/**
 * @ClassName TemplateType
 * @Description TODO
 * @Author xiaoxie
 * @Date 2019-12-19 15:11
 * @Version 1.0
 */
public enum UserTaskToAnswer {

    NOTAGAIN("0", "否"),
    AGAIN("1", "重新做答");

    // 成员变量
    private String value;
    private String label;

    // 构造方法
    UserTaskToAnswer(String value, String label) {
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
