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
import com.langheng.modules.ed.enumn.LessonType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 课程小节Entity
 * @author xiaoxie
 * @version 2019-12-17
 */
@Table(name="ed_lesson", alias="a", columns={
		@Column(name="lesson_id", attrName="lessonId", label="小节id", isPK=true),
		@Column(name="parent_lesson_id", attrName="parentLesson.lessonId", label="关联‘节’id"),
		@Column(name="chapter_id", attrName="chapter.chapterId", label="章节id"),
		@Column(name="name", attrName="name", label="小节名称", queryType=QueryType.LIKE),
		@Column(name="lesson_num", attrName="lessonNum", label="小节序号"),
		@Column(name="introduce", attrName="introduce", label="‘节’或小节的介绍"),
		@Column(name="lesson_type", attrName="lessonType", label="标记是‘节’还是小节，字典集 ed_lesson_type"),
		@Column(name="state", attrName="state", label="小节状态  参考字典集 ed_lesson_state"),
		@Column(name="is_shift_down", attrName="isShiftDown", label="是否下移"),
		@Column(includeEntity=DataEntity.class),
	}, joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=Lesson.class, attrName="Lesson", alias="u11",
				on="u11.lesson_id = a.parent_lesson_id", columns={
				@Column(includeEntity = Lesson.class),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=Chapter.class, attrName="chapter", alias="u12",
				on="u12.chapter_id = a.chapter_id", columns={
				@Column(includeEntity = Chapter.class),
		}),
	}, orderBy="a.lesson_num,a.update_date",extColumnKeys="barrageState,isPush,comma,classStuHadFinishLessonTaskNum,hadFinishLessonTaskNum"
)
@ApiModel(description = "课程‘节’或小节")
public class Lesson extends DataEntity<Lesson> {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("‘节’或小节id")
	private String lessonId;

	@ApiModelProperty("关联的‘节’（小节才会关联‘节’）")
	private Lesson parentLesson;

	@ApiModelProperty("关联的章节，（‘节’才会关联章节）")
	private Chapter chapter;

	@ApiModelProperty("标记是‘节’还是小节，字典集 ed_lesson_type")
	private String lessonType;

	@ApiModelProperty("‘节’或小节的名称")
	private String name;

	@ApiModelProperty("名称前缀")
	private String suffixName;

	@ApiModelProperty("‘节’或小节的序号")
	private Integer lessonNum;

	@ApiModelProperty("‘节’或小节的介绍")
	private String introduce;

	@ApiModelProperty("‘节’或小节的状态  参考字典集 ed_lesson_state")
	private String state;

	@ApiModelProperty(value = "是否下移",hidden = true)
	private String isShiftDown;

	/**
	 * 以下为VO对象
	 */
	@ApiModelProperty("对象类型")
	private String objectType = "lesson";
	@ApiModelProperty("是否开启弹幕 (0为开启  非0关闭)")
	private String barrageState;
	@ApiModelProperty("是否已经推送 (0为已推送  非0未推送)")
	private String isPush;
	@ApiModelProperty("课堂id")
	private String teachingClassId;
	@ApiModelProperty("总资源数")
	private Integer lessonTaskNum;
	@ApiModelProperty("班级学生已完成的资源数")
	private Integer classStuHadFinishLessonTaskNum;
	@ApiModelProperty("任务序号")
	private Integer taskNum;
	@ApiModelProperty("本人已经完成的任务数")
	private Integer hadFinishLessonTaskNum;
	@ApiModelProperty("完成进度")
	private Float finishProgress = 0F;
	@ApiModelProperty("关联小节列表")
	private List<Lesson> subLessonList = ListUtils.newArrayList();

	@ApiModelProperty("关联文件资源列表")
	private List<LessonTask> lessonTaskList = ListUtils.newArrayList();
	
	public Lesson() {
		this(null);
	}

	public Lesson(String id){
		super(id);
	}
	
	public String getLessonId() {
		return lessonId;
	}

	public void setLessonId(String lessonId) {
		this.lessonId = lessonId;
	}

	public Lesson getParentLesson() {
		return parentLesson;
	}

	public void setParentLesson(Lesson parentLesson) {
		this.parentLesson = parentLesson;
	}

	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getLessonNum() {
		return lessonNum;
	}

	public void setLessonNum(Integer lessonNum) {
		this.lessonNum = lessonNum;
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLessonType() {
		return lessonType;
	}

	public void setLessonType(String lessonType) {
		this.lessonType = lessonType;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getIsShiftDown() {
		return isShiftDown;
	}

	public void setIsShiftDown(String isShiftDown) {
		this.isShiftDown = isShiftDown;
	}

	public String getSuffixName() {
		return suffixName;
	}

	public void setSuffixName(String suffixName) {
		this.suffixName = suffixName;
	}

	public List<Lesson> getSubLessonList() {
		return subLessonList;
	}

	public void setSubLessonList(List<Lesson> subLessonList) {
		this.subLessonList = subLessonList;
	}

	public List<LessonTask> getLessonTaskList() {
		return lessonTaskList;
	}

	public void setLessonTaskList(List<LessonTask> lessonTaskList) {
		this.lessonTaskList = lessonTaskList;
	}

	public String getBarrageState() {
		return barrageState;
	}

	public void setBarrageState(String barrageState) {
		this.barrageState = barrageState;
	}

	public String getIsPush() {
		return isPush;
	}

	public void setIsPush(String isPush) {
		this.isPush = isPush;
	}

	public String getTeachingClassId() {
		return teachingClassId;
	}

	public void setTeachingClassId(String teachingClassId) {
		this.teachingClassId = teachingClassId;
	}

	public Integer getLessonTaskNum() {
		return lessonTaskNum;
	}

	public void setLessonTaskNum(Integer lessonTaskNum) {
		this.lessonTaskNum = lessonTaskNum;
	}

	public Integer getTaskNum() {
		return taskNum;
	}

	public void setTaskNum(Integer taskNum) {
		this.taskNum = taskNum;
	}

	public Integer getClassStuHadFinishLessonTaskNum() {
		return classStuHadFinishLessonTaskNum;
	}

	public void setClassStuHadFinishLessonTaskNum(Integer classStuHadFinishLessonTaskNum) {
		this.classStuHadFinishLessonTaskNum = classStuHadFinishLessonTaskNum;
	}

	public boolean isLesson(){
		return LessonType.LESSON.value().equals(this.getLessonType());
	}

	public boolean isSubLesson(){
		return LessonType.SUB_LESSON.value().equals(this.getLessonType());
	}

	public Float getFinishProgress() {
		return finishProgress;
	}

	public void setFinishProgress(Float finishProgress) {
		this.finishProgress = finishProgress;
	}

	public Integer getHadFinishLessonTaskNum() {
		return hadFinishLessonTaskNum;
	}

	public void setHadFinishLessonTaskNum(Integer hadFinishLessonTaskNum) {
		this.hadFinishLessonTaskNum = hadFinishLessonTaskNum;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public void setParentLessonId_in(String[] ids) {
		if (ids == null || ids.length <= 0){
			ids = new String[]{"NONE_EXIST"};
		}
		this.sqlMap.getWhere().and("parent_lesson_id", QueryType.IN, ids);
	}

	//联表查询弹幕开关情况
	public void selectLessonBarrageState(String teachingClassId){
		String barrageState = "(SELECT count(1) FROM ass_teach_class_barrage WHERE lesson_id = a.lesson_id and teaching_class_id= '"
				+ teachingClassId +"' ) AS barrageState";
		this.getSqlMap().add("barrageState", barrageState);
	}

	public void selectSubLessonBarrageState(String teachingClassId){
		String barrageState = "(SELECT count(1) FROM ass_teach_class_barrage WHERE sub_lesson_id = a.lesson_id and teaching_class_id= '"
				+ teachingClassId +"' ) AS barrageState";
		this.getSqlMap().add("barrageState", barrageState);
	}

	// 联表查询是否已经推送过该资源
	public void selectIsPush(String teachingClassId){
		String isPush = "(SELECT count(1) FROM ed_teaching_class_lesson as tcl WHERE tcl.lesson_id = a.lesson_id and tcl.teaching_class_id= '"
				+ teachingClassId +"' ) AS isPush";
		this.getSqlMap().add("isPush", isPush);
	}

	/**
	 * 获取班级学生已经完成的数量
	 */
	public void selectClassStuHadFinishLessonTaskNum(String teachingClassId){
		String classStuHadFinishLessonTaskNum = "(SELECT count(1) FROM ed_user_task AS ut" +
				" WHERE  (ut.parent_lesson_id = a.lesson_id OR ut.lesson_id = a.lesson_id) AND  ut.state= '2' AND ut.teaching_class_id ='"
				+ teachingClassId +"' ) AS classStuHadFinishLessonTaskNum";
		this.getSqlMap().add("classStuHadFinishLessonTaskNum", classStuHadFinishLessonTaskNum);
	}

	/**
	 * 获取某个学生已经完成的数量
	 */
	public void selectHadFinishLessonTaskNum(String teachingClassId,String studentId){
		String hadFinishLessonTaskNum = "(SELECT count(1) FROM ed_user_task AS ut" +
				" WHERE  (ut.parent_lesson_id = a.lesson_id OR ut.lesson_id = a.lesson_id) AND ut.state='2' AND ut.teaching_class_id ='"
				+ teachingClassId +"' AND ut.user_id='"+ studentId +"' ) AS hadFinishLessonTaskNum";
		this.getSqlMap().add("hadFinishLessonTaskNum", hadFinishLessonTaskNum);
	}

	/**
	 * 多自定义列连接符
	 */
	public void setJoinExtColumn(){
		this.getSqlMap().add("comma", ",");
	}
}