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
 * 课堂赋分管理Entity
 * @author xiaoxie
 * @version 2020-04-27
 */
@Table(name="ed_teaching_class_score", alias="a", columns={
		@Column(name="teach_class_answer_id", attrName="teachClassAnswerId", label="课堂弹幕", isPK=true),
		@Column(name="teaching_class_id", attrName="teachingClassId", label="课堂id"),
		@Column(name="lesson_task_id", attrName="lessonTaskId", label="任务id"),
		@Column(name="score", attrName="score", label="分数"),
	}, orderBy="a.teach_class_answer_id DESC"
)
@ApiModel(description = "课堂赋分管理")
public class TeachClassScore extends DataEntity<TeachClassScore> {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("课堂弹幕")
	private String teachClassAnswerId;

	@ApiModelProperty("课堂id")
	private String teachingClassId;

	@ApiModelProperty("任务id")
	private String lessonTaskId;

	@ApiModelProperty("分数")
	private Float score;
	
	public TeachClassScore() {
		this(null);
	}

	public TeachClassScore(String id){
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
	
	@Length(min=0, max=64, message="任务id长度不能超过 64 个字符")
	public String getLessonTaskId() {
		return lessonTaskId;
	}

	public void setLessonTaskId(String lessonTaskId) {
		this.lessonTaskId = lessonTaskId;
	}
	
	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}
	
}