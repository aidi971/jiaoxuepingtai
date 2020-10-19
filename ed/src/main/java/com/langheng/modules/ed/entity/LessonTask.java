/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.langheng.modules.ed.enumn.UserTaskStatue;
import com.langheng.modules.ed.vo.KeyDataReduction;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 小节任务（文件）Entity
 * @author xiaoxie
 * @version 2019-12-17
 */
@Table(name="ed_lesson_task", alias="a", columns={
		@Column(name="lesson_task_id", attrName="lessonTaskId", label="小节文件id", isPK=true),
		@Column(name="lesson_id", attrName="lessonId", label="小节id"),
		@Column(name="chapter_id", attrName="chapterId", label="章节id"),
		@Column(name="mission_id", attrName="missionId", label="课程任务项id"),
		@Column(name="task_id", attrName="taskId", label="任务id"),
		@Column(name="task_name", attrName="taskName", label="任务名称"),
		@Column(name="task_num", attrName="taskNum", label="任务序号"),
		@Column(name="task_url", attrName="taskUrl", label="任务封面"),
		@Column(name="task_status", attrName="taskStatus", label="任务添加权限标签 |taskStatus"),
		@Column(name="task_suffix", attrName="taskSuffix", label="文件后缀"),
		@Column(name="task_correct_value", attrName="taskCorrectValue", label="任务答案（fileid）"),
		@Column(name="task_correct_status", attrName="taskCorrectStatus", label="答案状态 0不存在1存在"),
		@Column(name="type", attrName="type", label="类型 |字典集 ed_lesson_task_type"),
		@Column(name="score", attrName="score", label="试题分数"),
		@Column(name="is_modify", attrName="isModify", label="是否有修改", comment="是否有修改（1是 0否)"),
		@Column(includeEntity=DataEntity.class),
	},orderBy="a.sort", extColumnKeys="isPush,isPushAnswer,modifyScore,finishState,videoTestCount,videoType"
)
@ApiModel(description = "小节任务（文件）")
public class LessonTask extends DataEntity<LessonTask> {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("小节文件id")
	private String lessonTaskId;
	@ApiModelProperty("小节id")
	private String lessonId;
	@ApiModelProperty("关联章节id")
	private String chapterId;
	@ApiModelProperty("课程任务项id")
	private String missionId;
	@ApiModelProperty("任务id")
	private String taskId;
	@ApiModelProperty("任务名称")
	private String taskName;
	@ApiModelProperty("任务编号")
	private String taskNum;
	@ApiModelProperty("任务封面")
	private String taskUrl;
	@ApiModelProperty("任务文件后缀")
	private String taskSuffix;
	@ApiModelProperty("任务答案")
	private String taskCorrectValue;
	@ApiModelProperty("任务状态")
	private String taskCorrectStatus;
	@ApiModelProperty("类型 |字典集 ed_lesson_task_type")
	private String type;
	@ApiModelProperty("试题分数")
	private Float score;
	@ApiModelProperty("是否有修改或新增 （1是 0否)")
	private String isModify;
	@ApiModelProperty("任务添加权限标签 |taskStatus")
	private Integer taskStatus;

	/**
	 * 以下为VO对象
	 */
	@ApiModelProperty("对象类型")
	private String objectType = "lessonTask";
	@ApiModelProperty("完成情况 (0为开启  非0关闭)")
	private String finishState;
	@ApiModelProperty("是否已经推送 (0为已推送  非0未推送)")
	private String isPush;
	@ApiModelProperty("是否已经推送答案 (0为已推送  非0未推送)")
	private String isPushAnswer;
	@ApiModelProperty("课堂id")
	private String teachingClassId;
	@ApiModelProperty("修改后的分数")
	private Float modifyScore;
	@ApiModelProperty("学生得分")
	private Float studentScore;
	@ApiModelProperty("学生任务id")
	private String userTaskId;
	@ApiModelProperty("是否为文件  0是1否")
	private String whetherFile;
	@ApiModelProperty("主观题文件id")
	private String subjectivityFileId;
	@ApiModelProperty("重新做答0否 1重新做答")
	private String isRepeatAnswer;
	@ApiModelProperty("学生id")
	private String studentId;
	@ApiModelProperty("数据集合Vo")
	private KeyDataReduction keyDataReduction;
	@ApiModelProperty("视频试题数")
	private Integer videoTestCount;
	@ApiModelProperty("视频试题数")
	private String videoType;




	/**
	 * 以下为DtO对象
	 */
	public String getVideoType() {
		return videoType;
	}

	public void setVideoType(String videoType) {
		if (videoType == null){
			videoType = "2";
		}
		this.videoType = videoType;
	}

	public Integer getVideoTestCount() {
		return videoTestCount;
	}

	public void setVideoTestCount(Integer videoTestCount) {
		this.videoTestCount = videoTestCount;
	}

	public Integer getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(Integer taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getSubjectivityFileId() {
		return subjectivityFileId;
	}

	public void setSubjectivityFileId(String subjectivityFileId) {
		this.subjectivityFileId = subjectivityFileId;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public String getTaskSuffix() {
		return taskSuffix;
	}

	public void setTaskSuffix(String taskSuffix) {
		this.taskSuffix = taskSuffix;
	}

	public String getTaskCorrectValue() {
		return taskCorrectValue;
	}

	public void setTaskCorrectValue(String taskCorrectValue) {
		this.taskCorrectValue = taskCorrectValue;
	}

	public String getTaskCorrectStatus() {
		return taskCorrectStatus;
	}

	public void setTaskCorrectStatus(String taskCorrectStatus) {
		this.taskCorrectStatus = taskCorrectStatus;
	}

	public String getTaskNum() {
		return taskNum;
	}

	public void setTaskNum(String taskNum) {
		this.taskNum = taskNum;
	}

	public String getTaskUrl() {
		return taskUrl;
	}

	public void setTaskUrl(String taskUrl) {
		this.taskUrl = taskUrl;
	}

	public LessonTask() {
		this(null);
	}

	public LessonTask(String id){
		super(id);
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

	@NotBlank(message="任务id不能为空")
	@Length(min=0, max=64, message="任务id长度不能超过 64 个字符")
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	@NotBlank(message="小节id不能为空")
	@Length(min=0, max=64, message="小节id长度不能超过 64 个字符")
	public String getLessonId() {
		return lessonId;
	}

	public void setLessonId(String lessonId) {
		this.lessonId = lessonId;
	}

	public String getChapterId() {
		return chapterId;
	}

	public void setChapterId(String chapterId) {
		this.chapterId = chapterId;
	}

	@NotBlank(message="类型 |字典集 ed_lesson_task_type不能为空")
	@Length(min=0, max=64, message="类型 |字典集 ed_lesson_task_type长度不能超过 64 个字符")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getIsModify() {
		return isModify;
	}

	public void setIsModify(String isModify) {
		this.isModify = isModify;
	}

	public String getIsPush() {
		return isPush;
	}

	public void setIsPush(String isPush) {
		this.isPush = isPush;
	}

	public String getIsPushAnswer() {
		return isPushAnswer;
	}

	public void setIsPushAnswer(String isPushAnswer) {
		this.isPushAnswer = isPushAnswer;
	}

	public String getTeachingClassId() {
		return teachingClassId;
	}

	public void setTeachingClassId(String teachingClassId) {
		this.teachingClassId = teachingClassId;
	}

	public Float getModifyScore() {
		return modifyScore;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public void setModifyScore(Float modifyScore) {
		if (modifyScore != null && modifyScore != 0){
			this.score = modifyScore;
		}
		this.modifyScore = modifyScore;
	}

	public String getFinishState() {
		return finishState;
	}

	public void setFinishState(String finishState) {
		if (StringUtils.isNotBlank(finishState)){
			this.finishState = finishState;
		}else {
			this.finishState =  UserTaskStatue.HAD_NOT_STARTED.value();
		}
	}

	public Float getStudentScore() {
		return studentScore;
	}

	public void setStudentScore(Float studentScore) {
		if (studentScore == null){
			studentScore = 0F;
		}
		this.studentScore = studentScore;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getUserTaskId() {
		return userTaskId;
	}

	public void setUserTaskId(String userTaskId) {
		this.userTaskId = userTaskId;
	}

	public String getWhetherFile() {
		return whetherFile;
	}

	public void setWhetherFile(String whetherFile) {
		this.whetherFile = whetherFile;
	}

	public String getIsRepeatAnswer() {
		return isRepeatAnswer;
	}

	public void setIsRepeatAnswer(String isRepeatAnswer) {
		if (isRepeatAnswer == null){
			this.isRepeatAnswer = "0";
		}
		this.isRepeatAnswer = isRepeatAnswer;
	}

	public  String[] getLessonId_in(){
		return sqlMap.getWhere().getValue("lesson_id",QueryType.IN);
	}

	public void setLessonId_in(String [] lessonIds){
		if (lessonIds == null || lessonIds.length <= 0){
			lessonIds = new String[]{"NONE_EXIST"};
		}
		sqlMap.getWhere().and("lesson_id",QueryType.IN,lessonIds);
	}

	public  String[] getLessonTaskId_in(){
		return sqlMap.getWhere().getValue("lesson_task_id",QueryType.IN);
	}

	public void setLessonTaskId_in(String [] lessonTaskIds){
		if (lessonTaskIds == null || lessonTaskIds.length <= 0){
			lessonTaskIds = new String[]{"NONE_EXIST"};
		}
		sqlMap.getWhere().and("lesson_task_id",QueryType.IN,lessonTaskIds);
	}
	public void setTaskId_in(String [] TaskIds){
		sqlMap.getWhere().and("task_id",QueryType.IN,TaskIds);
	}

	public void setFileNameOrFileSer(String FileNameOrFileSer){
		sqlMap.getWhere().andBracket("task_name",QueryType.LIKE,FileNameOrFileSer)
				.or("task_num",QueryType.LIKE,FileNameOrFileSer)
				.endBracket();
	}

	public void setType_in(String[] types) {
		sqlMap.getWhere().and("type",QueryType.IN,types);
	}
	// 联表查询是否完成任务
	public void selectIsFinishState(String userId,String teachingClassId){
		String finishState = "(SELECT eut.state FROM ed_user_task as eut WHERE eut.lesson_task_id = a.lesson_task_id and eut.user_id= '"
				+ userId +"' and eut.teaching_class_id= '"+teachingClassId+"') AS finishState";
		this.getSqlMap().add("finishState", finishState);
	}

	// 联表查询是否已经推送过该资源
	public void selectIsPush(String teachingClassId){
		String isPush = "(SELECT count(1) FROM ed_teaching_class_lesson_task as tclt WHERE tclt.lesson_task_id = a.lesson_task_id and tclt.teaching_class_id= '"
				+ teachingClassId +"' ) AS isPush";
		this.getSqlMap().add("isPush", isPush);
	}

	// 联表查询是否已经推送过资源答案
	public void selectIsPushAnswer(String teachingClassId){
		String isPushAnswer = "(SELECT count(1) FROM ed_teaching_class_answer as tca WHERE  tca.lesson_task_id = a.lesson_task_id and tca.teaching_class_id= '"
				+ teachingClassId +"' ) AS isPushAnswer";
		this.getSqlMap().add("isPushAnswer", isPushAnswer);
	}

	// 联表查询是否已重新赋分
	public void selectModifyScore(String teachingClassId){
		String modifyScore = "(SELECT tcs.score FROM ed_teaching_class_score as tcs WHERE  tcs.lesson_task_id = a.lesson_task_id and tcs.teaching_class_id= '"
				+ teachingClassId +"' ) AS modifyScore ";
		this.getSqlMap().add("modifyScore", modifyScore);
	}

	// 联表查询是否包含试题和验证类型
	public void selectVideoTest(String teachingClassId){
		String videoTestCount =
				"(select count(1) from sys_video_test svt where svt.file_id=a.task_id and svt.file_status=a.task_status) AS videoTestCount,";
		this.getSqlMap().add("videoTestCount", videoTestCount);
		String videoType =
				"(select etcvt.type from ed_teaching_class_video_test etcvt " +
						"where etcvt.teaching_class_id='" + teachingClassId +"' and etcvt.lesson_task_id=a.lesson_task_id) AS videoType";
		this.getSqlMap().add("videoType", videoType);
	}

	public void setChapterId_in(String[] chapterIds) {
		if (chapterIds == null || chapterIds.length <= 0){
			chapterIds = new String[]{"NONE_EXIST"};
		}
		sqlMap.getWhere().and("chapter_id",QueryType.IN,chapterIds);
	}

	public void setMissionId_in(String[] missionIds) {
		sqlMap.getWhere().and("mission_id",QueryType.IN,missionIds);
	}

	public KeyDataReduction getKeyDataReduction() {
		return keyDataReduction;
	}

	public void setKeyDataReduction(KeyDataReduction keyDataReduction) {
		this.keyDataReduction = keyDataReduction;
	}
}