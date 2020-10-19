/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 学校Entity
 * @author xiaoxie
 * @version 2019-12-20
 */
@Table(name="sys_school", alias="a", columns={
		@Column(name="school_id", attrName="schoolId", label="学校id", isPK=true),
		@Column(name="school_name", attrName="schoolName", label="学校名称", queryType=QueryType.LIKE),
		@Column(name="logo", attrName="logo", label="学校logo"),
		@Column(name="college_code", attrName="collegeCode", label="学校编码"),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.update_date DESC"
)
@ApiModel(description = "学校")
public class School extends DataEntity<School> {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("学校id")
	private String schoolId;

	@ApiModelProperty("学校名称")
	private String schoolName;

	@ApiModelProperty("学校logo")
	private String logo;

	@ApiModelProperty("学校编码")
	private String collegeCode;
	
	public School() {
		this(null);
	}

	public School(String id){
		super(id);
	}
	
	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	
	@NotBlank(message="学校名称不能为空")
	@Length(min=0, max=50, message="学校名称长度不能超过 50 个字符")
	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	
	@NotBlank(message="学校logo不能为空")
	@Length(min=0, max=255, message="学校logo长度不能超过 255 个字符")
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	@NotBlank(message="学校编码不能为空")
	@Length(min=0, max=50, message="学校编码长度不能超过 50 个字符")
	public String getCollegeCode() {
		return collegeCode;
	}

	public void setCollegeCode(String collegeCode) {
		this.collegeCode = collegeCode;
	}
	
}