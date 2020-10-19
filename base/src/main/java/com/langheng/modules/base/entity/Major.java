/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * sys_majorEntity
 * @author xiaoxie
 * @version 2020-02-15
 */
@Table(name="sys_major", alias="a", columns={
		@Column(name="major_id", attrName="majorId", label="专业id", isPK=true),
		@Column(name="academy_id", attrName="academyId", label="关联学院id"),
		@Column(name="major_name", attrName="majorName", label="专业名称"),
		@Column(name="full_name", attrName="fullName", label="全称"),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.create_date ASC"
)
@ApiModel(description = "sys_major")
public class Major extends DataEntity<Major> {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("专业id")
	private String majorId;

	@ApiModelProperty("关联学院id")
	private String academyId;

	@ApiModelProperty("专业名称")
	private String majorName;

	@ApiModelProperty("全称(经济学院/会计)")
	private String fullName;
	
	public Major() {
		this(null);
	}

	public Major(String id){
		super(id);
	}

	public String getMajorId() {
		return majorId;
	}

	public void setMajorId(String majorId) {
		this.majorId = majorId;
	}

	public String getAcademyId() {
		return academyId;
	}

	public void setAcademyId(String academyId) {
		this.academyId = academyId;
	}

	@NotBlank(message="专业名称不能为空")
	@Length(min=0, max=64, message="专业名称长度不能超过 64 个字符")
	public String getMajorName() {
		return majorName;
	}

	public void setMajorName(String majorName) {
		this.majorName = majorName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
}