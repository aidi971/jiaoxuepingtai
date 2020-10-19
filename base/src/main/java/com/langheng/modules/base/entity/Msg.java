/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 系统消息管理Entity
 * @author xiaoxie
 * @version 2020-05-08
 */
@Table(name="sys_msg", alias="a", columns={
		@Column(name="msg_id", attrName="msgId", label="编号", isPK=true),
		@Column(name="title", attrName="title", label="消息标题", queryType=QueryType.LIKE),
		@Column(name="teaching_class_id", attrName="teachingClassId", label="课堂id"),
		@Column(name="type", attrName="type", label="类型 | 字典集 sys_msg_type"),
		@Column(name="biz_key", attrName="bizKey", label="业务主键"),
		@Column(name="state", attrName="state", label="状态"),
		@Column(name="content", attrName="content", label="消息内容"),
		@Column(name="send_time", attrName="sendTime", label="发送时间"),
		@Column(name="sender_code", attrName="senderCode", label="发送者"),
		@Column(name="sender_name", attrName="senderName", label="发送者姓名", queryType=QueryType.LIKE),
		@Column(name="receiver_type", attrName="receiverType", label="接收者类型"),
		@Column(name="receiver_key", attrName="receiverKey", label="接收者键值"),
		@Column(name="receive_codes", attrName="receiveCodes", label="接受者字符串"),
		@Column(name="receive_names", attrName="receiveNames", label="接受者名称字符串, queryType=QueryType.LIKE"),
	}, orderBy="a.send_time DESC"
)
@ApiModel(description = "系统消息管理")
public class Msg extends DataEntity<Msg> {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("消息id")
	private String msgId;

	@ApiModelProperty("消息标题")
	private String title;

	@ApiModelProperty("课堂id")
	private String teachingClassId;

	@ApiModelProperty("类型 | 字典集 sys_msg_type")
	private String type;

	@ApiModelProperty("状态")
	private String state;

	@ApiModelProperty("业务主键")
	private String bizKey;

	@ApiModelProperty("消息内容")
	private String content;

	@ApiModelProperty("发送时间")
	private Date sendTime;

	@ApiModelProperty("发送者")
	private String senderCode;

	@ApiModelProperty("发送者姓名")
	private String senderName;

	@ApiModelProperty("接收者类型")
	private String receiverType;

	@ApiModelProperty("接收者键值")
	private String receiverKey;

	private String receiveCodes;

	private String receiveNames;

	// 是否保存
	private boolean isSave = true;

	public Msg() {
		this(null);
	}

	public Msg(String id){
		super(id);
	}
	
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
	
	@Length(min=0, max=1, message="状态长度不能超过 1 个字符")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	public String getReceiverType() {
		return receiverType;
	}

	public void setReceiverType(String receiverType) {
		this.receiverType = receiverType;
	}

	public String getReceiverKey() {
		return receiverKey;
	}

	public void setReceiverKey(String receiverKey) {
		this.receiverKey = receiverKey;
	}

	public String getReceiveCodes() {
		return receiveCodes;
	}

	public void setReceiveCodes(String receiveCodes) {
		this.receiveCodes = receiveCodes;
	}

	public String getReceiveNames() {
		return receiveNames;
	}

	public void setReceiveNames(String receiveNames) {
		this.receiveNames = receiveNames;
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

	public boolean isSave() {
		return isSave;
	}

	public void setSave(boolean save) {
		isSave = save;
	}

	public void setType_not_in(String[] types) {
		sqlMap.getWhere().and("type",QueryType.NOT_IN,types);
    }
}