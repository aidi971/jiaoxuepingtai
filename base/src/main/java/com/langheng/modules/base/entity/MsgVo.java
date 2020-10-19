/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 系统消息管理Entity
 * @author xiaoxie
 * @version 2020-05-08
 */
public class MsgVo {

	@ApiModelProperty("消息id")
	private String msgId;

	@ApiModelProperty("消息标题")
	private String title;

	@ApiModelProperty("类型 | 字典集 sys_msg_type")
	private String type;

	@ApiModelProperty("业务主键")
	private String bizKey;

	@ApiModelProperty("消息内容")
	private String content;

	@ApiModelProperty("发送时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Date sendTime;

	@ApiModelProperty("发送者")
	private String senderCode;

	@ApiModelProperty("发送者姓名")
	private String senderName;

	@ApiModelProperty("课堂id")
	private String teachingClassId;

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	@NotBlank(message="消息标题不能为空")
	@Length(min=0, max=200, message="消息标题长度不能超过 200 个字符")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Length(min=0, max=1, message="类型 | 字典集 sys_msg_type长度不能超过 1 个字符")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@NotBlank(message="消息内容不能为空")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	
	@Length(min=0, max=64, message="发送者长度不能超过 64 个字符")
	public String getSenderCode() {
		return senderCode;
	}

	public void setSenderCode(String senderCode) {
		this.senderCode = senderCode;
	}
	
	@Length(min=0, max=128, message="发送者姓名长度不能超过 128 个字符")
	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getBizKey() {
		return bizKey;
	}

	public void setBizKey(String bizKey) {
		this.bizKey = bizKey;
	}

	public String getTeachingClassId() {
		return teachingClassId;
	}

	public void setTeachingClassId(String teachingClassId) {
		this.teachingClassId = teachingClassId;
	}
}