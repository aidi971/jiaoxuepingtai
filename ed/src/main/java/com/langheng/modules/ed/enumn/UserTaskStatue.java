package com.langheng.modules.ed.enumn;

public enum UserTaskStatue {
    HAD_NOT_STARTED("0", "未开始"),
    PROGRESSING("1", "进行中"),
    FINISHED("2", "已完成");

    // 成员变量
    private String value;
    private String label;

    // 构造方法
    UserTaskStatue(String value, String label) {
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

