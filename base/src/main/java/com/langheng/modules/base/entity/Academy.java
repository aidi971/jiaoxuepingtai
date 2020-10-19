/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.entity;

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

/**
 * 学院Entity
 * @author xiaoxie
 * @version 2019-12-20
 */
@Table(name="sys_academy", alias="a", columns={
		@Column(name="academy_id", attrName="academyId", label="学院id", isPK=true),
		@Column(name="academy_name", attrName="academyName", label="学院名称"),
		@Column(name="academy_status", attrName="academyStatus", label="学院状态"),
		@Column(name="academy_logo", attrName="academyLogo", label="学院logo"),
		@Column(name="school_id", attrName="school.schoolId", label="学校id"),
		@Column(includeEntity=DataEntity.class),
	}, joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=School.class, attrName="school", alias="u11",
				on="u11.school_id = a.school_id", columns={
				@Column(includeEntity = School.class),
		}),
	}, orderBy="a.create_date ASC"
)
@ApiModel(description = "学院")
public class Academy extends DataEntity<Academy> {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("学院id")
	private String academyId;

	@ApiModelProperty("学院名称")
	private String academyName;

	@ApiModelProperty("学院状态")
	private String academyStatus;

	@ApiModelProperty("学院logo")
	private String academyLogo;

	@ApiModelProperty("学校")
	private School school;

	public String getAcademyStatus() {
		return academyStatus;
	}

	public void setAcademyStatus(String academyStatus) {
		this.academyStatus = academyStatus;
	}

	public Academy() {
		this(null);
	}

	public Academy(String id){
		super(id);
	}
	
	public String getAcademyId() {
		return academyId;
	}

	public void setAcademyId(String academyId) {
		this.academyId = academyId;
	}
	
	@NotBlank(message="学院名称不能为空")
	@Length(min=0, max=64, message="学院名称长度不能超过 64 个字符")
	public String getAcademyName() {
		return academyName;
	}

	public void setAcademyName(String academyName) {
		this.academyName = academyName;
	}
	
	@Length(min=0, max=255, message="学院logo长度不能超过 255 个字符")
	public String getAcademyLogo() {
		return academyLogo;
	}

	public void setAcademyLogo(String academyLogo) {
		this.academyLogo = academyLogo;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public String getacademyName_like() {
		return sqlMap.getWhere().getValue("academy_name", QueryType.LIKE);
	}

	public void setacademyName_like(String academyName) {
		sqlMap.getWhere().and("academy_name", QueryType.LIKE, academyName);
	}

}