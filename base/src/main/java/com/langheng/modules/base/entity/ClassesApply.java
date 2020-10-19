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
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 加入班级申请Entity
 * @author xiaoxie
 * @version 2020-03-30
 */
@Table(name="sys_classes_apply", alias="a", columns={
		@Column(name="classes_apply_id", attrName="classesApplyId", label="classes_apply_id", isPK=true),
		@Column(name="student_id", attrName="student.studentId", label="学生id"),
		@Column(name="stu_name", attrName="stuName", label="学生名"),
		@Column(name="login_code", attrName="loginCode", label="登陆id"),
		@Column(name="classes_id", attrName="classes.classesId", label="班级id"),
		@Column(name="teacher_id", attrName="teacherId", label="关联教师id"),
		@Column(name="state", attrName="state", label="申请状态", comment="申请状态(字典集 |  classes_apply_state  )"),
		@Column(includeEntity=DataEntity.class),
	},joinTable={
		@JoinTable(type=JoinTable.Type.LEFT_JOIN, entity=Student.class, attrName="student", alias="u12",
				on="u12.student_id = a.student_id", columns={
				@Column(includeEntity = Student.class),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity= BaseUser.class,attrName="student",  alias="u11",
				on="u11.user_code = u12.student_id", columns={
				@Column(includeEntity = BaseUser.class),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=Classes.class, attrName="classes", alias="u13",
				on="u13.classes_id = a.classes_id", columns={
				@Column(includeEntity = Classes.class),
		}),
}, orderBy="a.update_date DESC"
)
@ApiModel(description = "加入班级申请")
public class ClassesApply extends DataEntity<ClassesApply> {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("classes_apply_id")
	private String classesApplyId;

	@ApiModelProperty("关联学生")
	private Student student;

	@ApiModelProperty("学生名")
	private String stuName;

	@ApiModelProperty("登陆名")
	private String loginCode;

	@ApiModelProperty("关联班级")
	private Classes classes;

	@ApiModelProperty(value = "关联教师id",hidden = true)
	private String teacherId;

	@ApiModelProperty("申请状态(字典集 |  classes_apply_state  )")
	private String state;
	
	public ClassesApply() {
		this(null);
	}

	public ClassesApply(String id){
		super(id);
	}
	
	public String getClassesApplyId() {
		return classesApplyId;
	}

	public void setClassesApplyId(String classesApplyId) {
		this.classesApplyId = classesApplyId;
	}
	
	@NotBlank(message="申请状态不能为空")
	@Length(min=0, max=1, message="申请状态长度不能超过 1 个字符")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Classes getClasses() {
		return classes;
	}

	public void setClasses(Classes classes) {
		this.classes = classes;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getStuName() {
		return stuName;
	}

	public void setStuName(String stuName) {
		this.stuName = stuName;
	}

	public String getLoginCode() {
		return loginCode;
	}

	public void setLoginCode(String loginCode) {
		this.loginCode = loginCode;
	}
}