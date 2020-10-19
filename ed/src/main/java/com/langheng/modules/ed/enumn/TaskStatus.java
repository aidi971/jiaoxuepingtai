package com.langheng.modules.ed.enumn;

/**
 * @ClassName TemplateType
 * @Description TODO
 * @Author xiaoxie
 * @Date 2019-12-19 15:11
 * @Version 1.0
 */
public enum TaskStatus {

    PRIVATELY(0, "私有"),
    OPEN(1, "公开"),
    STANDARD(2, "标准");

    // 成员变量
    private Integer value;
    private String label;

    // 构造方法
    TaskStatus(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    public String label() {
        return this.label;
    }

    public Integer value() {
        return this.value;
    }
}
