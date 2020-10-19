/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 课程进度Entity
 * @author xiaoxie
 * @version 2019-12-17
 */
@Table(name="ed_classes_progress", alias="a", columns={
		@Column(name="progress_id", attrName="progressId", label="班级进度id", isPK=true),
		@Column(name="classes_id", attrName="classesId", label="班级"),
		@Column(name="biz_id", attrName="bizId", label="关联业务主键", comment="关联业务主键(可为: 课程id，章节id，小节id）"),
		@Column(name="type", attrName="type", label="类型 字典集 ed_classes_progress_type"),
		@Column(name="state", attrName="state", label="进度状态 | 字典集 ed_classes_progress_state"),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.update_date DESC"
)
@ApiModel(description = "课程进度")
public class ClassesProgress extends DataEntity<ClassesProgress> {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("班级进度id")
	private String progressId;

	@ApiModelProperty("班级")
	private String classesId;

	@ApiModelProperty("关联业务主键(可为: 课程id，章节id，小节id）")
	private String bizId;

	@ApiModelProperty("类型 字典集 ed_classes_progress_type")
	private String type;

	@ApiModelProperty("进度状态 | 字典集 ed_classes_progress_state")
	private String state;
	
	public ClassesProgress() {
		this(null);
	}

	public ClassesProgress(String id){
		super(id);
	}
	
	public String getProgressId() {
		return progressId;
	}

	public void setProgressId(String progressId) {
		this.progressId = progressId;
	}
	
	@NotBlank(message="班级不能为空")
	@Length(min=0, max=64, message="班级长度不能超过 64 个字符")
	public String getClassesId() {
		return classesId;
	}

	public void setClassesId(String classesId) {
		this.classesId = classesId;
	}
	
	@NotBlank(message="关联业务主键不能为空")
	@Length(min=0, max=64, message="关联业务主键长度不能超过 64 个字符")
	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}
	
	@Length(min=0, max=16, message="类型 字典集 ed_classes_progress_type长度不能超过 16 个字符")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@NotBlank(message="进度状态 | 字典集 ed_classes_progress_state不能为空")
	@Length(min=0, max=64, message="进度状态 | 字典集 ed_classes_progress_state长度不能超过 64 个字符")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
}