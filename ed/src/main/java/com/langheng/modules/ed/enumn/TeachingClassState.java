package com.langheng.modules.ed.enumn;

/**
 * @ClassName TemplateType
 * @Description TODO
 * @Author xiaoxie
 * @Date 2019-12-19 15:11
 * @Version 1.0
 */
public enum TeachingClassState {

    HAD_NOT_STARTED("0", "未开始"),
    PROGRESSING("1", "进行中"),
    FINISHED("2", "已结束");

    // 成员变量
    private String value;
    private String label;

    // 构造方法
    TeachingClassState(String value, String label) {
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
