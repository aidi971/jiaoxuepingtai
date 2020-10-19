package com.langheng.modules.ed.enumn;

public enum  BizType {

    CHAPTER("chapter", "章"),
    LESSON("lesson", "节"),
    SUB_LESSON("subLesson", "小节"),
    LESSON_TASK("lessonTask", "资源");

    // 成员变量
    private String value;
    private String label;

    // 构造方法
    BizType(String value, String label) {
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
