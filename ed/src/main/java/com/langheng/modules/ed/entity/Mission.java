/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.entity;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * 课程任务Entity
 * @author xiaoxie
 * @version 2020-08-06
 */
@Table(name="ed_mission", alias="a", columns={
		@Column(name="mission_id", attrName="missionId", label="任务id", isPK=true),
		@Column(name="course_id", attrName="course.courseId", label="课程id"),
		@Column(name="template_id", attrName="template.templateId", label="模板id"),
		@Column(name="mission_num", attrName="missionNum", label="任务序号"),
		@Column(name="mission_name", attrName="missionName", label="任务名称", queryType=QueryType.LIKE),
		@Column(name="introduce", attrName="introduce", label="任务介绍"),
		@Column(includeEntity=DataEntity.class),
	},joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=Course.class, attrName="course", alias="u11",
				on="u11.course_id = a.course_id", columns={
				@Column(includeEntity = Course.class),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=Template.class, attrName="template", alias="u12",
				on="u12.template_id = a.template_id", columns={
				@Column(includeEntity = Template.class),
		}),
	}, orderBy="a.update_date DESC"
)
@ApiModel(description = "课程任务")
public class Mission extends DataEntity<Mission> {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("任务id")
	private String missionId;

	@ApiModelProperty("课程id")
	private Course course;

	@ApiModelProperty("模板id")
	private Template template;

	@ApiModelProperty("任务序号")
	private Integer missionNum;

	@ApiModelProperty("任务名称")
	private String missionName;

	@ApiModelProperty("任务介绍")
	private String introduce;

	private List<LessonTask> lessonTaskList = ListUtils.newArrayList();


	public Mission() {
		this(null);
	}

	public Mission(String id){
		super(id);
	}
	
	public String getMissionId() {
		return missionId;
	}

	public void setMissionId(String missionId) {
		this.missionId = missionId;
	}
	
	public Integer getMissionNum() {
		return missionNum;
	}

	public void setMissionNum(Integer missionNum) {
		this.missionNum = missionNum;
	}
	
	@Length(min=0, max=128, message="任务名称长度不能超过 128 个字符")
	public String getMissionName() {
		return missionName;
	}

	public void setMissionName(String missionName) {
		this.missionName = missionName;
	}
	
	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public List<LessonTask> getLessonTaskList() {
		return lessonTaskList;
	}

	public void setLessonTaskList(List<LessonTask> lessonTaskList) {
		this.lessonTaskList = lessonTaskList;
	}
}