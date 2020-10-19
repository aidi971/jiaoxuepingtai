/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 学生任务Entity
 * @author xiaoxie
 * @version 2019-12-17
 */
@Table(name="ed_user_task", alias="a", columns={
		@Column(name="user_task_id", attrName="userTaskId", label="学生任务id", isPK=true),
		@Column(name="user_id", attrName="userId", label="学生id"),
		@Column(name="student_name", attrName="studentName", label="学生姓名", queryType=QueryType.LIKE),
		@Column(name="lesson_task_id", attrName="lessonTaskId", label="任务id"),
		@Column(name="task_id", attrName="taskId", label="文件id"),
		@Column(name="teaching_class_id", attrName="teachingClassId", label="课堂id"),
		@Column(name="mission_id", attrName="missionId", label="课程任务项id"),
		@Column(name="chapter_id", attrName="chapterId", label="章节id"),
		@Column(name="lesson_id", attrName="lessonId", label="小节id"),
		@Column(name="parent_lesson_id", attrName="parentLessonId", label="节id"),
		@Column(name="course_id", attrName="courseId", label="课程id"),
		@Column(name="classes_id", attrName="classesId", label="班级id"),
		@Column(name="type", attrName="type", label="类型 |字典集 ed_lesson_task_type"),
		@Column(name="state", attrName="state", label="状态 |字典集 ed_user_task_state"),
		@Column(name="score", attrName="score", label="分数"),
		@Column(name="gross_score", attrName="grossScore", label="总分数"),
		@Column(name="to_answer", attrName="toAnswer", label="重新作答 0否1重新做答"),
		@Column(name="finish_time", attrName="finishTime", label="完成时间"),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.update_date DESC",extColumnKeys="loginCode"
)
@ApiModel(description = "学生任务")
public class UserTask extends DataEntity<UserTask> {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("学生任务id")
	private String userTaskId;
	@ApiModelProperty("学生id")
	private String userId;
	@ApiModelProperty("学生姓名")
	private String studentName;
	@ApiModelProperty("任务id")
	private String lessonTaskId;
	@ApiModelProperty("文件id")
	private String taskId;
	@ApiModelProperty("课堂id")
	private String teachingClassId;
	@ApiModelProperty("课程任务项id")
	private String missionId;
	@ApiModelProperty("章节id")
	private String chapterId;
	@ApiModelProperty("小节id")
	private String lessonId;
	@ApiModelProperty("节id")
	private String parentLessonId;
	@ApiModelProperty("课程id")
	private String courseId;
	@ApiModelProperty("班级id")
	private String classesId;
	@ApiModelProperty("类型 |字典集 ed_lesson_task_type")
	private String type;
	@ApiModelProperty("状态 |字典集 ed_user_task_state")
	private String state;
	@ApiModelProperty("分数")
	private Float score;
	@ApiModelProperty("总分数")
	private Float grossScore;
	@ApiModelProperty("重新做答0否 1重新做答")
	private String toAnswer;
	@ApiModelProperty("完成时间")
	private Date finishTime;
	@ApiModelProperty("用户名")
	private String loginCode;

	@JsonFormat(
			pattern = "yyyy-MM-dd HH:mm"
	)
	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public String getToAnswer() {
		return toAnswer;
	}

	public void setToAnswer(String toAnswer) {
		this.toAnswer = toAnswer;
	}

	public String getLoginCode() {
		return loginCode;
	}

	public void setLoginCode(String loginCode) {
		this.loginCode = loginCode;
	}

	public String getTeachingClassId() {
		return teachingClassId;
	}

	public void setTeachingClassId(String teachingClassId) {
		this.teachingClassId = teachingClassId;
	}

	public String getClassesId() {
		return classesId;
	}

	public void setClassesId(String classesId) {
		this.classesId = classesId;
	}

	public Float getGrossScore() {
		return grossScore;
	}

	public void setGrossScore(Float grossScore) {
		this.grossScore = grossScore;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public UserTask() {
		this(null);
	}

	public UserTask(String id){
		super(id);
	}
	
	public String getUserTaskId() {
		return userTaskId;
	}

	public void setUserTaskId(String userTaskId) {
		this.userTaskId = userTaskId;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	public String getLessonId() {
		return lessonId;
	}

	public void setLessonId(String lessonId) {
		this.lessonId = lessonId;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public String getLessonTaskId() {
		return lessonTaskId;
	}

	public void setLessonTaskId(String lessonTaskId) {
		this.lessonTaskId = lessonTaskId;
	}

	public String getMissionId() {
		return missionId;
	}

	public void setMissionId(String missionId) {
		this.missionId = missionId;
	}

	public String getParentLessonId() {
		return parentLessonId;
	}

	public void setParentLessonId(String parentLessonId) {
		this.parentLessonId = parentLessonId;
	}

	public String getChapterId() {
		return chapterId;
	}

	public void setChapterId(String chapterId) {
		this.chapterId = chapterId;
	}

	public String[] getCategoryCodeId_in(){ return sqlMap.getWhere().getValue("user_task_id", QueryType.IN); }

	public void setCategoryCodeId_in(String[] ids){ sqlMap.getWhere().and("user_task_id", QueryType.IN, ids); }

	public String[] getUserId_in(){ return sqlMap.getWhere().getValue("user_id", QueryType.IN); }

	public void setUserId_in(String[] ids){ sqlMap.getWhere().and("user_id", QueryType.IN, ids); }

	public String[] getCategoryCode_in(){ return sqlMap.getWhere().getValue("type", QueryType.IN); }

	public void setCategoryCode_in(String[] codes){ sqlMap.getWhere().and("type", QueryType.IN, codes); }

	public void setUserNameOrUserId(String UserNameOrUserId){
		sqlMap.getWhere().andBracket("student_name",QueryType.LIKE,UserNameOrUserId)
				.or("user_id",QueryType.LIKE,UserNameOrUserId)
				.endBracket();
	}
	public void selectLoginCode(){
		//联表查询用户loginCode
		String loginCode = "(select login_code from sys_user where user_code= user_id) AS loginCode";
		this.getSqlMap().add("loginCode", loginCode);
	}

    public void setLessonTaskId_in(String [] lessonTaskIds) {
		if (lessonTaskIds.length > 0){
			this.getSqlMap().getWhere().and("lesson_task_id",QueryType.IN,lessonTaskIds);
		}else {
			this.getSqlMap().getWhere().and("lesson_task_id", QueryType.IN,"NOT_EXIST");
		}
    }
	public void setTaskId_in(String [] taskIds) {
		this.getSqlMap().getWhere().and("task_id", QueryType.IN,taskIds);
	}

}