package com.langheng.modules.ed.enumn;

public enum LessonTaskType {
    COURSEWARE("0", "课件"),
    VIDEO("1", "视频"),
    SUBJECTIVITY("2", "主观"),
    OBJECTIVES("3", "客观"),
    CASE("4", "案例"),
    CORRECT("5", "答案"),
    PRACTICE("6", "实践");



    // 成员变量
    private String value;
    private String label;

    // 构造方法
    LessonTaskType(String value, String label) {
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
