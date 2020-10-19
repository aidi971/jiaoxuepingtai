/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

/**
 * 课堂答案管理Entity
 * @author xiaoxie
 * @version 2020-04-26
 */
@Table(name="ed_teaching_class_answer", alias="a", columns={
		@Column(name="teach_class_answer_id", attrName="teachClassAnswerId", label="课堂弹幕", isPK=true),
		@Column(name="teaching_class_id", attrName="teachingClassId", label="课堂id"),
		@Column(name="lesson_id", attrName="lessonId", label="节id"),
		@Column(name="chapter_id", attrName="chapterId", label="章id"),
		@Column(name="sub_lesson_id", attrName="subLessonId", label="小节id"),
		@Column(name="lesson_task_id", attrName="lessonTaskId", label="任务id"),
	}, orderBy="a.teach_class_answer_id DESC"
)
@ApiModel(description = "课堂答案管理")
public class TeachClassAnswer extends DataEntity<TeachClassAnswer> {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("课堂弹幕")
	private String teachClassAnswerId;

	@ApiModelProperty("课堂id")
	private String teachingClassId;

	@ApiModelProperty("节id")
	private String lessonId;

	@ApiModelProperty("章id")
	private String chapterId;

	@ApiModelProperty("小节id")
	private String subLessonId;

	@ApiModelProperty("任务id")
	private String lessonTaskId;


	public TeachClassAnswer() {
		this(null);
	}

	public TeachClassAnswer(String id){
		super(id);
	}
	
	public String getTeachClassAnswerId() {
		return teachClassAnswerId;
	}

	public void setTeachClassAnswerId(String teachClassAnswerId) {
		this.teachClassAnswerId = teachClassAnswerId;
	}
	
	@Length(min=0, max=64, message="课堂id长度不能超过 64 个字符")
	public String getTeachingClassId() {
		return teachingClassId;
	}

	public void setTeachingClassId(String teachingClassId) {
		this.teachingClassId = teachingClassId;
	}
	
	@Length(min=0, max=64, message="节id长度不能超过 64 个字符")
	public String getLessonId() {
		return lessonId;
	}

	public void setLessonId(String lessonId) {
		this.lessonId = lessonId;
	}
	
	@Length(min=0, max=64, message="章id长度不能超过 64 个字符")
	public String getChapterId() {
		return chapterId;
	}

	public void setChapterId(String chapterId) {
		this.chapterId = chapterId;
	}
	
	@Length(min=0, max=64, message="小节id长度不能超过 64 个字符")
	public String getSubLessonId() {
		return subLessonId;
	}

	public void setSubLessonId(String subLessonId) {
		this.subLessonId = subLessonId;
	}
	
	@Length(min=0, max=64, message="任务id长度不能超过 64 个字符")
	public String getLessonTaskId() {
		return lessonTaskId;
	}

	public void setLessonTaskId(String lessonTaskId) {
		this.lessonTaskId = lessonTaskId;
	}
	

}