/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.entity;


import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.annotation.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 教师学院Entity
 * @author xiaoxie
 * @version 2019-12-20
 */
@Table(name="sys_teacher_academy", alias="a", columns={
		@Column(name="tea_aca_id", attrName="teaAcaId", label="教师部门id", isPK=true),
		@Column(name="teacher_id", attrName="teacherId", label="关联教师id"),
		@Column(name="academy_id", attrName="academyId", label="学院id"),
		@Column(includeEntity=DataEntity.class),
	}, joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=Teacher.class, attrName="teacher", alias="u11",
				on="u11.teacher_id = a.teacher_id", columns={
				@Column(includeEntity = Teacher.class),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=Academy.class, attrName="academy", alias="u12",
				on="u12.academy_id = a.academy_id", columns={
				@Column(includeEntity = Academy.class),
		}),
	}, orderBy="a.update_date DESC"
)
@ApiModel(description = "教师学院")
public class TeacherAcademy extends DataEntity<TeacherAcademy> {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("教师部门id")
	private String teaAcaId;

	@ApiModelProperty("关联教师")
	private Teacher teacher;

	@ApiModelProperty("学院")
	private Academy academy;
	
	public TeacherAcademy() {
		this(null);
	}

	public TeacherAcademy(String id){
		super(id);
	}
	
	public String getTeaAcaId() {
		return teaAcaId;
	}

	public void setTeaAcaId(String teaAcaId) {
		this.teaAcaId = teaAcaId;
	}


	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public Academy getAcademy() {
		return academy;
	}

	public void setAcademy(Academy academy) {
		this.academy = academy;
	}
}