/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.langheng.modules.base.entity.Classes;
import com.langheng.modules.base.entity.Teacher;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 课堂Entity
 * @author xiaoxie
 * @version 2020-02-11
 */
@Table(name="ed_teaching_class", alias="a", columns={
		@Column(name="teaching_class_id", attrName="teachingClassId", label="课堂id", isPK=true),
		@Column(name="course_id", attrName="course.courseId", label="课程id"),
		@Column(name="teacher_id", attrName="teacher.teacherId", label="教师id"),
		@Column(name="classes_id", attrName="classes.classesId", label="班级id"),
		@Column(name="class_code", attrName="classCode", label="课程编码"),
		@Column(name="img_src", attrName="imgSrc", label="图片路径"),
		@Column(name="state", attrName="state", label="课程状态| 字典集 ed_teaching_class_state"),
		@Column(name="type", attrName="type", label="课程类型| 字典集 ed_teaching_class_type"),
		@Column(name="is_enable_barrage", attrName="isEnableBarrage", label="是否开启弹幕"),
		@Column(name="is_stop", attrName="isStop", label="是否暂停教学班"),
		@Column(name="begin_time", attrName="beginTime", label="课程开始时间"),
		@Column(name="end_time", attrName="endTime", label="课程结束时间"),
		@Column(includeEntity=DataEntity.class),
	},joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=Teacher.class, attrName="teacher", alias="u11",
				on="u11.teacher_id = a.teacher_id", columns={
				@Column(includeEntity = Teacher.class),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=Course.class, attrName="course", alias="u12",
				on="u12.course_id = a.course_id", columns={
				@Column(includeEntity = Course.class),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=Classes.class, attrName="classes", alias="u13",
				on="u13.classes_id = a.classes_id", columns={
				@Column(includeEntity = Classes.class),
		}),
	},  orderBy="a.create_date DESC",
		extColumnKeys = "extColumn"
)
@ApiModel(description = "课堂")
public class TeachingClass extends DataEntity<TeachingClass> {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("课堂id")
	private String teachingClassId;

	@ApiModelProperty("课程编号")
	private String classCode;

	@ApiModelProperty(value = "课程",required = true)
	private Course course;

	@ApiModelProperty(value = "班级",required = true)
	private Classes classes;

	@ApiModelProperty("教师")
	private Teacher teacher;

	@ApiModelProperty("课程类型| 字典集 ed_teaching_class_type")
	private String type;

	@ApiModelProperty("课程状态| 字典集 ed_teaching_class_state")
	private String state;

	@ApiModelProperty("是否开启弹幕")
	private String isEnableBarrage;

	@ApiModelProperty(value = "是否暂停教学班",hidden = true)
	private String isStop;

	@JsonFormat( pattern = "yyyy-MM-dd HH:mm" )
	@ApiModelProperty("课程开始时间")
	private Date beginTime;

	@JsonFormat( pattern = "yyyy-MM-dd HH:mm" )
	@ApiModelProperty("课程结束时间")
	private Date endTime;

	@ApiModelProperty("图片路径")
	private String imgSrc;


	public TeachingClass() {
		this(null);
	}

	public TeachingClass(String id){
		super(id);
	}
	
	public String getTeachingClassId() {
		return teachingClassId;
	}

	public void setTeachingClassId(String teachingClassId) {
		this.teachingClassId = teachingClassId;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	@Length(min=0, max=8, message="课程状态| 字典集 ed_teaching_class_state长度不能超过 8 个字符")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	@Length(min=0, max=1, message="是否开启弹幕长度不能超过 1 个字符")
	public String getIsEnableBarrage() {
		return isEnableBarrage;
	}

	public void setIsEnableBarrage(String isEnableBarrage) {
		this.isEnableBarrage = isEnableBarrage;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getIsStop() {
		return isStop;
	}

	public void setIsStop(String isStop) {
		this.isStop = isStop;
	}

	public Classes getClasses() {
		return classes;
	}

	public void setClasses(Classes classes) {
		this.classes = classes;
	}

	public String getImgSrc() {
		return imgSrc;
	}

	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}

	public void disableStatus(){
		sqlMap.getWhere().disableAutoAddStatusWhere();
		this.status = "";
	}

	public void setClassesIds_in(String [] classesIds){
		this.sqlMap.getWhere().and("classes_id",QueryType.IN,classesIds);
	}
}