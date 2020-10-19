/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 系统访问记录Entity
 * @author xiaoxie
 * @version 2020-02-17
 */
@Table(name="sys_login_log", alias="a", columns={
		@Column(name="info_id", attrName="infoId", label="访问ID", isPK=true),
		@Column(name="user_id", attrName="user.userCode", label="关联用户id"),
		@Column(name="ipaddr", attrName="ipaddr", label="登录IP地址"),
		@Column(name="location", attrName="location", label="登录地点"),
		@Column(name="browser", attrName="browser", label="浏览器类型"),
		@Column(name="os", attrName="os", label="操作系统"),
		@Column(name="msg", attrName="msg", label="提示消息"),
		@Column(name="type", attrName="type", label="类型"),
		@Column(name="login_time", attrName="loginTime", label="访问时间"),
		@Column(name="login_time", attrName="loginTime", label="访问时间"),
		@Column(name="user_agent", attrName="userAgent", label="用户代理"),
	},  joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity= BaseUser.class,attrName="user",  alias="u11",
				on="u11.user_code = a.user_id", columns={
				@Column( name = "user_code", attrName = "userCode", label = "用户编码"),
				@Column(name = "login_code",attrName = "loginCode",label = "登录账号",queryType = QueryType.LIKE),
				@Column(name = "user_name",attrName = "userName",label = "用户名",queryType = QueryType.LIKE),
				@Column(name = "sex",attrName = "sex",label = "性别"),
				@Column(name = "user_type",attrName = "userType",label = "用户类型"),
				@Column(name = "cover_img",attrName = "coverImg",label = "用户头像"),
				@Column(name = "first_login",attrName = "firstLogin",label = "是否首次登陆"),
				@Column(name = "login_date",attrName = "loginDate",label = "登录日期"),
				@Column(name = "host",attrName = "host",label = "主机ip"),
				@Column(name = "last_login_date",attrName = "lastLoginDate",label = "上次登录"),
				@Column( name = "last_host",attrName = "lastHost",label = "上次登录主机ip"),
				@Column(name = "freeze_date",attrName = "freezeDate",label = "冻结日期"),
				@Column(name = "login_count",attrName = "loginCount",label = "登录次数")
		})
	}, orderBy="a.login_time DESC"
)
@ApiModel(description = "系统访问记录")
public class LoginLog extends DataEntity<LoginLog> {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("访问ID")
	private String infoId;

	@ApiModelProperty("关联用户")
	private BaseUser user;

	@ApiModelProperty("登录IP地址")
	private String ipaddr;

	@ApiModelProperty("登录地点")
	private String location;

	@ApiModelProperty("浏览器类型")
	private String browser;

	@ApiModelProperty("操作系统")
	private String os;

	@ApiModelProperty("提示消息")
	private String msg;

	@ApiModelProperty("访问时间")
	private Date loginTime;

	@ApiModelProperty("类型 | 0：登录，1：登出")
	private String type;

	@ApiModelProperty("用户代理")
	private String userAgent;

	/**
	 * 下面是Vo对象
	 */
	private String classesId;

	private String className;
	
	public LoginLog() {
		this(null);
	}

	public LoginLog(String id){
		super(id);
	}
	
	public String getInfoId() {
		return infoId;
	}

	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}

	public BaseUser getUser() {
		return user;
	}

	public void setUser(BaseUser user) {
		this.user = user;
	}

	@Length(min=0, max=50, message="登录IP地址长度不能超过 50 个字符")
	public String getIpaddr() {
		return ipaddr;
	}

	public void setIpaddr(String ipaddr) {
		this.ipaddr = ipaddr;
	}
	
	@Length(min=0, max=255, message="登录地点长度不能超过 255 个字符")
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	@Length(min=0, max=50, message="浏览器类型长度不能超过 50 个字符")
	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}
	
	@Length(min=0, max=50, message="操作系统长度不能超过 50 个字符")
	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}
	
	@Length(min=0, max=255, message="提示消息长度不能超过 255 个字符")
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getClassesId() {
		return classesId;
	}

	public void setClassesId(String classesId) {
		this.classesId = classesId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setLoginTime_gle(Date loginTime){
		loginTime = DateUtils.getOfDayFirst(loginTime);
		sqlMap.getWhere().and("login_time",QueryType.GTE,loginTime);
	}

    public void setClassesId_in(String[] classesIds) {
		sqlMap.getWhere().and("u13.classes_id",QueryType.IN,classesIds);
    }

	public void selectUserNameOrLoginCode(String userNameOrLoginCode) {
		this.sqlMap.getWhere().andBracket("u11.user_name",QueryType.LIKE,userNameOrLoginCode)
				.or("u11.login_code",QueryType.LIKE,userNameOrLoginCode).endBracket();
	}

	public void selectClassesId(String classesId) {
		this.sqlMap.getWhere().and("u13.classes_id",QueryType.EQ,classesId);
	}
}