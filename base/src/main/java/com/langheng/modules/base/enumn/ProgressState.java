package com.langheng.modules.base.enumn;

public enum  ProgressState {

    UN_START("0", "未开始"),
    HAD_FINISHED("1", "已完成"),
    IN_PROGRESS("2", "进行中"),
    UN_FINISHED("3", "未完成");

    // 成员变量
    private String value;
    private String label;

    // 构造方法
    ProgressState(String value, String label) {
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
