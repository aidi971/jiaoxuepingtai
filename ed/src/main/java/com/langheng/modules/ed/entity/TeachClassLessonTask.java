/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 课堂节任务管理Entity
 * @author xiaoxie
 * @version 2020-04-21
 */
@Table(name="ed_teaching_class_lesson_task", alias="a", columns={
		@Column(name="teaching_class_id", attrName="teachingClassId", label="课堂id",isPK = true),
		@Column(name="lesson_task_id", attrName="lessonTaskId", label="任务id",isPK = true),
	}
)
@ApiModel(description = "课堂节任务管理")
public class TeachClassLessonTask extends DataEntity<TeachClassLessonTask> {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("课堂id")
	private String teachingClassId;

	@ApiModelProperty("任务id")
	private String lessonTaskId;

	public TeachClassLessonTask() {
		this(null);
	}

	public TeachClassLessonTask(String id){
		super(id);
	}
	

	@NotBlank(message="teaching_class_id不能为空")
	@Length(min=0, max=64, message="teaching_class_id长度不能超过 64 个字符")
	public String getTeachingClassId() {
		return teachingClassId;
	}

	public void setTeachingClassId(String teachingClassId) {
		this.teachingClassId = teachingClassId;
	}

	@NotBlank(message="lesson_task_id不能为空")
	@Length(min=0, max=64, message="lesson_task_id长度不能超过 64 个字符")
	public String getLessonTaskId() {
		return lessonTaskId;
	}

	public void setLessonTaskId(String lessonTaskId) {
		this.lessonTaskId = lessonTaskId;
	}

	public void setLessonTaskId_in(String [] lessonTaskIds){
		sqlMap.getWhere().and("lesson_task_id",QueryType.IN,lessonTaskIds);
	}

}