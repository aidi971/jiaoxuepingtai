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
 * 课堂节或者'小节'管理Entity
 * @author xiaoxie
 * @version 2020-04-21
 */
@Table(name="ed_teaching_class_lesson", alias="a", columns={
		@Column(name="teaching_class_id", attrName="teachingClassId", label="teaching_class_id",isPK = true),
		@Column(name="lesson_id", attrName="lessonId", label="lesson_id",isPK = true),
	}
)
@ApiModel(description = "课堂节或者'小节'管理")
public class TeachClassLesson extends DataEntity<TeachClassLesson> {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("teaching_class_id")
	private String teachingClassId;

	@ApiModelProperty("lesson_id")
	private String lessonId;

	@ApiModelProperty("state")
	private String state;
	
	public TeachClassLesson() {
		this(null);
	}

	public TeachClassLesson(String id){
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
	
	@NotBlank(message="lesson_id不能为空")
	@Length(min=0, max=64, message="lesson_id长度不能超过 64 个字符")
	public String getLessonId() {
		return lessonId;
	}

	public void setLessonId(String lessonId) {
		this.lessonId = lessonId;
	}
	
	@Length(min=0, max=1, message="state长度不能超过 1 个字符")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setLessonId_in(String [] lessonIds){
		sqlMap.getWhere().and("lesson_id", QueryType.IN,lessonIds);
	}

}