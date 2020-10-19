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

/**
 * 学生班级Entity
 * @author xiaoxie
 * @version 2019-12-18
 */
@Table(name="sys_student_classes", alias="a", columns={
		@Column(name="student_classes_id", attrName="studentClassesId", label="学生班级id", isPK=true),
		@Column(name="student_id", attrName="studentId", label="学生id"),
		@Column(name="classes_id", attrName="classesId", label="班级id"),
		@Column(name="state", attrName="state", label="状态"),
		@Column(includeEntity=DataEntity.class),
	} ,orderBy="a.update_date DESC"
)
@ApiModel(description = "学生班级")
public class StudentClasses extends DataEntity<StudentClasses> {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("学生班级id")
	private String studentClassesId;

	@ApiModelProperty("学生Id")
	private String studentId;

	@ApiModelProperty("班级Id")
	private String classesId;

	@ApiModelProperty("学生班级状态")
	private String state;

	public StudentClasses() {
		this(null);
	}

	public StudentClasses(String id){
		super(id);
	}
	
	public String getStudentClassesId() {
		return studentClassesId;
	}

	public void setStudentClassesId(String studentClassesId) {
		this.studentClassesId = studentClassesId;
	}
	
	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getClassesId() {
		return classesId;
	}

	public void setClassesId(String classesId) {
		this.classesId = classesId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setStudentId_in(String [] studentIds) {
		getSqlMap().getWhere().and("student_id",QueryType.IN,studentIds);
	}

}