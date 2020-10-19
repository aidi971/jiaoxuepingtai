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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 用户消息Entity
 * @author xiaoxie
 * @version 2020-05-08
 */
@Table(name="sys_msg_user", alias="a", columns={
		@Column(name="user_msg_id", attrName="userMsgId", label="用户系统消息id", isPK=true),
		@Column(name="msg_id", attrName="msg.msgId", label="系统消息id"),
		@Column(name="user_code", attrName="userCode", label="用户编码"),
		@Column(name="review_time", attrName="reviewTime", label="查看时间"),
		@Column(name="state", attrName="state", label="状态"),
	}, joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=Msg.class, attrName="msg", alias="u11",
				on="u11.msg_id = a.msg_id", columns={
				@Column(includeEntity = Msg.class),
		}),
	}, orderBy="a.state ASC,u11.send_time DESC"
)
@ApiModel(description = "用户消息")
public class MsgUser extends DataEntity<MsgUser> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty("用户系统消息id")
	private String userMsgId;

	@ApiModelProperty("系统消息id")
	private Msg msg;

	@ApiModelProperty("用户编码")
	private String userCode;

	@ApiModelProperty("查看时间")
	private Date reviewTime;

	@ApiModelProperty("状态")
	private String state;
	
	public MsgUser() {
		this(null);
	}

	public MsgUser(String id){
		super(id);
	}
	
	public String getUserMsgId() {
		return userMsgId;
	}

	public void setUserMsgId(String userMsgId) {
		this.userMsgId = userMsgId;
	}

	public Msg getMsg() {
		return msg;
	}

	public void setMsg(Msg msg) {
		this.msg = msg;
	}

	@NotBlank(message="用户编码不能为空")
	@Length(min=0, max=64, message="用户编码长度不能超过 64 个字符")
	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(Date reviewTime) {
		this.reviewTime = reviewTime;
	}
	
	@NotBlank(message="状态不能为空")
	@Length(min=0, max=1, message="状态长度不能超过 1 个字符")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
}