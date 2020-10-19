package com.langheng.modules.base.enumn;

/**
 * @ClassName SysYesNOEnum
 * @Description TODO
 * @Author xiaoxie
 * @Date 2019-09-12 12:59
 * @Version 1.0
 */
public enum UserType {

    STUDENT("1", "学生"),
    TEACHER("2", "教师"),
    JURY("4", "评委"),
    ADMIN("3", "管理员");

    // 成员变量
    private String value;
    private String label;

    // 构造方法
    UserType(String value, String label) {
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
