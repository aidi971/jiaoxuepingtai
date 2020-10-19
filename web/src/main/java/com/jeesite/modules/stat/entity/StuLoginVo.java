package com.jeesite.modules.stat.entity;

public class StuLoginVo {
    private String stuName;
    private Integer loginCount;

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public StuLoginVo(String stuName, Integer loginCount) {
        this.stuName = stuName;
        this.loginCount = loginCount;
    }
}
