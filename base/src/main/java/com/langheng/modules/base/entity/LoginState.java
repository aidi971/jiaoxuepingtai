package com.langheng.modules.base.entity;

/**
 * @ClassName LoginState
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-03-26 14:18
 * @Version 1.0
 */

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName User
 * @Description TODO
 * @Author xiaoxie
 * @Date 2019-10-17 14:36
 * @Version 1.0
 */
public class LoginState implements Serializable {

    /**
     * 用户名
     */
    private String userId;

    /**
     * 登录名
     */
    private String loginCode;
    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;
    /**
     * 是否能够登录
     */
    private Boolean isCanLogin;
    /**
     * 锁定时间
     */
    private Date lockTime;
    /**
     * 登录失败次数
     */
    private Integer failedNum;
    /**
     * 是否需要校验验证码
     */
    private Boolean isNeedValidCode;

    private static final long serialVersionUID = 1L;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLoginCode() {
        return loginCode;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getNeedValidCode() {
        return isNeedValidCode;
    }

    public void setNeedValidCode(Boolean needValidCode) {
        isNeedValidCode = needValidCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getCanLogin() {
        return isCanLogin;
    }

    public void setCanLogin(Boolean canLogin) {
        isCanLogin = canLogin;
    }

    public Date getLockTime() {
        return lockTime;
    }

    public void setLockTime(Date lockTime) {
        this.lockTime = lockTime;
    }

    public Integer getFailedNum() {
        return failedNum;
    }

    public void setFailedNum(Integer failedNum) {
        this.failedNum = failedNum;
    }
}
