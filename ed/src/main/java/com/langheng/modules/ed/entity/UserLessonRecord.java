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
import com.langheng.modules.base.entity.BaseUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 小节任务记录Entity
 * @author xiaoxie
 * @version 2020-04-29
 */
@Table(name="ed_user_lesson_record", alias="a", columns={
		@Column(name="record_id", attrName="recordId", label="记录id", isPK=true),
		@Column(name="teaching_class_id", attrName="teachingClassId", label="课堂id"),
		@Column(name="user_id", attrName="student.userCode", label="学生id"),
		@Column(name="chapter_id", attrName="chapter.chapterId", label="章节id"),
		@Column(name="lesson_id", attrName="lesson.lessonId", label="小节id"),
		@Column(name="finish_time", attrName="finishTime", label="完成时间"),
		@Column(name="lesson_task_name", attrName="lessonTaskName", label="资源名称"),
		@Column(name="task_num", attrName="taskNum", label="任务序号"),
	}, joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=Lesson.class, attrName="Lesson", alias="u11",
				on="u11.lesson_id = a.lesson_id", columns={
				@Column(includeEntity = Lesson.class),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=Chapter.class, attrName="chapter", alias="u12",
				on="u12.chapter_id = a.chapter_id", columns={
				@Column(includeEntity = Chapter.class),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=BaseUser.class, attrName="student", alias="u13",
				on="u13.user_code = a.user_id", columns={
				@Column(includeEntity = BaseUser.class),
		}),
	}, orderBy="a.finish_time DESC"
)
@ApiModel(description = "小节任务记录")
public class UserLessonRecord extends DataEntity<UserLessonRecord> {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("记录id")
	private String recordId;
	@ApiModelProperty("课堂id")
	private String teachingClassId;
	@ApiModelProperty("学生")
	private BaseUser student;
	@ApiModelProperty("章节")
	private Chapter chapter;
	@ApiModelProperty("小节或节")
	private Lesson lesson;
	@ApiModelProperty("完成时间")
	private Date finishTime;
	@ApiModelProperty("资源名称")
	private String lessonTaskName;
	@ApiModelProperty("任务序号")
	private Integer taskNum;

	
	public UserLessonRecord() {
		this(null);
	}

	public UserLessonRecord(String id){
		super(id);
	}
	
	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public BaseUser getStudent() {
		return student;
	}

	public void setStudent(BaseUser student) {
		this.student = student;
	}

	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

	public Lesson getLesson() {
		return lesson;
	}

	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
	}

	public String getTeachingClassId() {
		return teachingClassId;
	}

	public void setTeachingClassId(String teachingClassId) {
		this.teachingClassId = teachingClassId;
	}

	public String getLessonTaskName() {
		return lessonTaskName;
	}

	public void setLessonTaskName(String lessonTaskName) {
		this.lessonTaskName = lessonTaskName;
	}

	public Integer getTaskNum() {
		return taskNum;
	}

	public void setTaskNum(Integer taskNum) {
		this.taskNum = taskNum;
	}
}