package com.jeesite.common.enumn;

/**
 * @ClassName SysYesNoEnum
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-03-26 14:27
 * @Version 1.0
 */
public enum  SysYesNoEnum {

    NO("0", "否"),
    YES("1", "是");

    // 成员变量
    private String value;
    private String label;

    // 构造方法
    SysYesNoEnum(String value, String label) {
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
