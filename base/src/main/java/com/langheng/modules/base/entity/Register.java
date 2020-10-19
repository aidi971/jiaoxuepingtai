/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 注册管理Entity
 * @author xiaoxie
 * @version 2020-07-29
 */
@Table(name="sys_register", alias="a", columns={
		@Column(name="user_code", attrName="userCode", label="UUId", isPK=true),
		@Column(name="classes_id", attrName="classes.classesId", label="班级id"),
		@Column(name="teacher_id", attrName="teacher.teacherId", label="教师id"),
		@Column(name="user_name", attrName="userName", label="用户名", queryType=QueryType.LIKE),
		@Column(name="login_code", attrName="loginCode", label="登陆账号"),
		@Column(name="apply_state", attrName="applyState", label="申请状态 sys_register_apply_state"),
		@Column(name="host", attrName="host", label="当前登录主机ip"),
		@Column(name="ip_address", attrName="ipAddress", label="ip所在地"),
		@Column(name="register_time", attrName="registerTime", label="注册时间"),
		@Column(name="revoke_type", attrName="revokeType", label="吊销方式"),
		@Column(name="revoke_time", attrName="revokeTime", label="吊销时间"),
		@Column(name="handler_name", attrName="handlerName", label="执行人", queryType=QueryType.LIKE),
	},  joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity= Classes.class,attrName="classes",  alias="u11",
				on="u11.classes_id = a.classes_id", columns={
				@Column(name="classes_id", attrName="classesId", label="班级id"),
				@Column(name="class_name", attrName="className", label="教学班名称", queryType=QueryType.LIKE),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity= Teacher.class,attrName="teacher",  alias="u12",
				on="u12.teacher_id = a.teacher_id", columns={
				@Column(name="teacher_id", attrName="teacherId", label="教师id"),
				@Column(name="teacher_name", attrName="teacherName", label="教师姓名", queryType = QueryType.LIKE),
		}),
	}, orderBy="a.register_time DESC",extColumnKeys = "stateKey"
)
@ApiModel(description = "注册管理")
public class Register extends DataEntity<Register> {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("UUId")
	private String userCode;

	@ApiModelProperty("班级")
	private Classes classes;

	@ApiModelProperty("教师id")
	private Teacher teacher;

	@ApiModelProperty("用户名")
	private String userName;

	@ApiModelProperty("登陆账号")
	private String loginCode;

	@ApiModelProperty("申请状态 sys_register_apply")
	private String applyState;

	@ApiModelProperty("当前登录主机ip")
	private String host;

	@ApiModelProperty("ip所在地")
	private String ipAddress;

	@ApiModelProperty("账号状态")
	private String state;

	@ApiModelProperty("注册时间")
	private Date registerTime;

	@ApiModelProperty("吊销方式")
	private String revokeType;

	@ApiModelProperty("吊销时间")
	private Date revokeTime;

	@ApiModelProperty("执行人")
	private String handlerName;
	
	public Register() {
		this(null);
	}

	public Register(String id){
		super(id);
	}
	
	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	
	@NotBlank(message="用户名不能为空")
	@Length(min=0, max=64, message="用户名长度不能超过 64 个字符")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@NotBlank(message="登陆账号不能为空")
	@Length(min=0, max=64, message="登陆账号长度不能超过 64 个字符")
	public String getLoginCode() {
		return loginCode;
	}

	public void setLoginCode(String loginCode) {
		this.loginCode = loginCode;
	}
	
	@Length(min=0, max=1, message="申请状态 sys_register_apply长度不能超过 1 个字符")
	public String getApplyState() {
		return applyState;
	}

	public void setApplyState(String applyState) {
		this.applyState = applyState;
	}
	
	@Length(min=0, max=64, message="当前登录主机ip长度不能超过 64 个字符")
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
	
	@Length(min=0, max=1, message="账号状态长度不能超过 1 个字符")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}
	
	@Length(min=0, max=1, message="吊销方式长度不能超过 1 个字符")
	public String getRevokeType() {
		return revokeType;
	}

	public void setRevokeType(String revokeType) {
		this.revokeType = revokeType;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getRevokeTime() {
		return revokeTime;
	}

	public void setRevokeTime(Date revokeTime) {
		this.revokeTime = revokeTime;
	}
	
	@Length(min=0, max=64, message="执行人长度不能超过 64 个字符")
	public String getHandlerName() {
		return handlerName;
	}

	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}

	public Classes getClasses() {
		return classes;
	}

	public void setClasses(Classes classes) {
		this.classes = classes;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void setApplyState_in(String[] applyStates) {
		this.sqlMap.getWhere().and("apply_state",QueryType.IN,applyStates);
	}

	public void selectUserNameOrLoginCode(String userNameOrLoginCode) {
		this.sqlMap.getWhere().andBracket("user_name",QueryType.LIKE,userNameOrLoginCode)
				.or("login_code",QueryType.LIKE,userNameOrLoginCode).endBracket();
	}

	public void setRevokeTime_lte(Date revokeTime){
		this.sqlMap.getWhere().and("revoke_time",QueryType.LTE,revokeTime);
	}

	public void setRevokeTime_gte(Date revokeTime){
		this.sqlMap.getWhere().and("revoke_time",QueryType.GTE,revokeTime);
	}

	public void setRegisterTime_lte(Date registerTime){
		this.sqlMap.getWhere().and("register_time",QueryType.LTE,registerTime);
	}

	public void setRegisterTime_gte(Date registerTime){
		this.sqlMap.getWhere().and("register_time",QueryType.GTE,registerTime);
	}

	public void selectRevokeType_is_not_null() {
		this.sqlMap.getWhere().and("revoke_type",QueryType.IS_NOT_NULL,"");
	}

	public void selectRevokeType_is_null() {
		this.sqlMap.getWhere().and("revoke_type",QueryType.IS_NULL,"");
	}

}