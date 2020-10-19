//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.jeesite.common.entity;

import java.io.Serializable;
import java.util.Date;

public class TokenInfo implements Serializable {
    private String userId;
    private String loginCode;
    private String userName;
    private String userType;
    private String teachingClassId;
    private String token;
    private Date loginTime;
    private Date expireTime;

    /**
     * 是否是主socket
     */
    private Boolean isMainChannel;
    /**
     * 是否在执行业务
     */
    private Boolean isBusy = false;

    public TokenInfo() {
    }

    public String getUserId() {
        return this.userId;
    }

    public String getLoginCode() {
        return this.loginCode;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getUserType() {
        return this.userType;
    }

    public String getToken() {
        return this.token;
    }

    public Date getLoginTime() {
        return this.loginTime;
    }

    public Date getExpireTime() {
        return this.expireTime;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public void setLoginCode(final String loginCode) {
        this.loginCode = loginCode;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public void setUserType(final String userType) {
        this.userType = userType;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public void setLoginTime(final Date loginTime) {
        this.loginTime = loginTime;
    }

    public void setExpireTime(final Date expireTime) {
        this.expireTime = expireTime;
    }

    public Boolean getBusy() {
        return isBusy;
    }

    public void setBusy(Boolean busy) {
        isBusy = busy;
    }

    public String getTeachingClassId() {
        return teachingClassId;
    }

    public void setTeachingClassId(String teachingClassId) {
        this.teachingClassId = teachingClassId;
    }

    public Boolean getMainChannel() {
        return isMainChannel;
    }

    public void setMainChannel(Boolean mainChannel) {
        isMainChannel = mainChannel;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof TokenInfo)) {
            return false;
        } else {
            TokenInfo other = (TokenInfo)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label95: {
                    Object this$userId = this.getUserId();
                    Object other$userId = other.getUserId();
                    if (this$userId == null) {
                        if (other$userId == null) {
                            break label95;
                        }
                    } else if (this$userId.equals(other$userId)) {
                        break label95;
                    }

                    return false;
                }

                Object this$loginCode = this.getLoginCode();
                Object other$loginCode = other.getLoginCode();
                if (this$loginCode == null) {
                    if (other$loginCode != null) {
                        return false;
                    }
                } else if (!this$loginCode.equals(other$loginCode)) {
                    return false;
                }

                Object this$userName = this.getUserName();
                Object other$userName = other.getUserName();
                if (this$userName == null) {
                    if (other$userName != null) {
                        return false;
                    }
                } else if (!this$userName.equals(other$userName)) {
                    return false;
                }

                label74: {
                    Object this$userType = this.getUserType();
                    Object other$userType = other.getUserType();
                    if (this$userType == null) {
                        if (other$userType == null) {
                            break label74;
                        }
                    } else if (this$userType.equals(other$userType)) {
                        break label74;
                    }

                    return false;
                }

                label67: {
                    Object this$token = this.getToken();
                    Object other$token = other.getToken();
                    if (this$token == null) {
                        if (other$token == null) {
                            break label67;
                        }
                    } else if (this$token.equals(other$token)) {
                        break label67;
                    }

                    return false;
                }

                Object this$loginTime = this.getLoginTime();
                Object other$loginTime = other.getLoginTime();
                if (this$loginTime == null) {
                    if (other$loginTime != null) {
                        return false;
                    }
                } else if (!this$loginTime.equals(other$loginTime)) {
                    return false;
                }

                Object this$expireTime = this.getExpireTime();
                Object other$expireTime = other.getExpireTime();
                if (this$expireTime == null) {
                    if (other$expireTime != null) {
                        return false;
                    }
                } else if (!this$expireTime.equals(other$expireTime)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof TokenInfo;
    }

}
