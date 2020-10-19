//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.langheng.modules.base.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.validator.PatternValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Table(
        name = "sys_user",
        alias = "a",
        columns = {@Column(
                name = "user_code",
                attrName = "userCode",
                label = "用户编码",
                isPK = true
        ), @Column(
                name = "login_code",
                attrName = "loginCode",
                label = "登录账号"
        ), @Column(
                name = "user_name",
                attrName = "userName",
                label = "用户名",
                queryType = QueryType.LIKE
        ), @Column(
                name = "password",
                attrName = "password",
                label = "密码"
        ), @Column(
                name = "sex",
                attrName = "sex",
                label = "性别"
        ),@Column(
                name="motto",
                attrName="motto",
                label="个性签名"),
        @Column(
                name = "user_type",
                attrName = "userType",
                label = "用户类型"
        ), @Column(
                name = "cover_img",
                attrName = "coverImg",
                label = "用户头像"
        ), @Column(
                name = "first_login",
                attrName = "firstLogin",
                label = "是否首次登陆"
        ), @Column(
                name = "login_date",
                attrName = "loginDate",
                label = "登录日期"
        ), @Column(
                name = "host",
                attrName = "host",
                label = "主机ip"
        ), @Column(
                name = "last_login_date",
                attrName = "lastLoginDate",
                label = "上次登录"
        ), @Column(
                name = "last_host",
                attrName = "lastHost",
                label = "上次登录主机ip"
        ), @Column(
                name = "freeze_date",
                attrName = "freezeDate",
                label = "冻结日期"
        ), @Column(
                name = "login_count",
                attrName = "loginCount",
                label = "登录次数"
        ), @Column(
                includeEntity = DataEntity.class
        )},
        orderBy = "a.update_date DESC"
)
@ApiModel(
        description = "用户"
)
public class BaseUser extends DataEntity<BaseUser> {
    @ApiModelProperty("用户id")
    private String userCode;
    @ApiModelProperty("登录账号")
    @NotBlank(message = "登录账号不能为空")
    @PatternValue(
            value = "web.validator.user.loginCode",
            regexp = "[a-zA-Z0-9]{6,12}",
            message = "登录账号长度应为 6 到 20 个字符，并且只能包含字母、数字"
    )
    private String loginCode;
    @ApiModelProperty("用户名（不是登录账号）")
    private String userName;
    @JsonIgnore
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("密码")
    private String userPassword;
    @ApiModelProperty("用户类型")
    private String userType;
    @ApiModelProperty("性别")
    private String sex;
    @ApiModelProperty("用户头像")
    private String coverImg;
    @ApiModelProperty("个性签名")
    private String motto;
    @ApiModelProperty("首次登陆")
    private String firstLogin;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @ApiModelProperty("登录时间")
    private Date loginDate;
    @ApiModelProperty("登录ip")
    private String host;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @ApiModelProperty("上次登录时间")
    private Date lastLoginDate;
    @ApiModelProperty("上次登录ip")
    private String lastHost;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @ApiModelProperty("冻结时间")
    private Date freezeDate;
    @ApiModelProperty("登录次数")
    private Integer loginCount;

    /**
     * vo对象
     */
    @ApiModelProperty("是否在线 sys_yes_no")
    private String isOnline = Global.NO;

    public static final String SUPER_ADMIN_CODE = Global.getProperty("user.superAdminCode", "system");

    public BaseUser() {
        super((String)null);
    }

    public BaseUser(String userCode) {
        super(userCode);
    }

    public String getUserCode() {
        return this.userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getLoginCode() {
        return this.loginCode;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return this.userType;
    }

    public String getCoverImg() {
        return this.coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getLoginDate() {
        return this.loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Date getLastLoginDate() {
        return this.lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getLastHost() {
        return this.lastHost;
    }

    public void setLastHost(String lastHost) {
        this.lastHost = lastHost;
    }

    public Date getFreezeDate() {
        return this.freezeDate;
    }

    public void setFreezeDate(Date freezeDate) {
        this.freezeDate = freezeDate;
    }

    public String getFirstLogin() {
        return this.firstLogin;
    }

    public void setFirstLogin(String firstLogin) {
        this.firstLogin = firstLogin;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void disableStatus() {
        this.setStatus("");
        this.sqlMap.getWhere().disableAutoAddStatusWhere();
    }

    public void setLoginCodeOrUserId(String LoginCodeOrUserId){
        sqlMap.getWhere().andBracket("user_name",QueryType.LIKE,LoginCodeOrUserId)
                .or("login_code",QueryType.LIKE,LoginCodeOrUserId)
                .endBracket();
    }
}
