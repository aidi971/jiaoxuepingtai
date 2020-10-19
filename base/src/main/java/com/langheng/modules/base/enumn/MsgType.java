package com.langheng.modules.base.enumn;

/**
 * @ClassName ClassesApplyState
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-03-30 11:21
 * @Version 1.0
 */
public enum MsgType {

    NOTICE("0", "公告信息"),
    TEACH_CLASS_TASK("1", "学习任务信息"),
    CLASSES("2", "班级信息"),
    DISCUSS("3", "讨论区信息"),
    TEMPLATE_AUDIT("4", "模板审核信息");

    // 成员变量
    private String value;
    private String label;

    // 构造方法
    MsgType(String value, String label) {
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
