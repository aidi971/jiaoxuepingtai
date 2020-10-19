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

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 课程章节Entity
 * @author xiaoxie
 * @version 2019-12-17
 */
@Table(name="ed_chapter", alias="a", columns={
		@Column(name="chapter_id", attrName="chapterId", label="章节id", isPK=true),
		@Column(name="course_id", attrName="course.courseId", label="课程id"),
		@Column(name="template_id", attrName="template.templateId", label="模板id"),
		@Column(name="name", attrName="name", label="章节名称", queryType=QueryType.LIKE),
		@Column(name="suffix_name", attrName="suffixName", label="名称前缀| 第几章"),
		@Column(name="chapter_num", attrName="chapterNum", label="章节序号"),
		@Column(name="state", attrName="state", label="章节状态 | 字典集 ed_chapter_state"),
		@Column(name="is_shift_down", attrName="isShiftDown", label="是否下移"),
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
	},  orderBy="a.chapter_num,a.update_date",extColumnKeys="barrageState"
)
@ApiModel(description = "课程章节")
public class Chapter extends DataEntity<Chapter> {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("章节id")
	private String chapterId;

	@ApiModelProperty("课程id")
	private Course course;

	@ApiModelProperty("模板id")
	private Template template;

	@ApiModelProperty("名称前缀| 第几章")
	private String suffixName;

	@ApiModelProperty(value = "章节名称",required = true)
	private String name;

	@ApiModelProperty(value = "章节序号",required = true)
	private Integer chapterNum;

	@ApiModelProperty("章节状态 | 字典集 ed_chapter_state")
	private String state;

	@ApiModelProperty(value = "是否下移",hidden = true)
	private String isShiftDown;

	/**
	 * 一下为VO对象
	 */
	@ApiModelProperty("对象类型")
	private String objectType = "chapter";
	@ApiModelProperty("课堂id")
	private String teachingClassId;
	@ApiModelProperty("是否开启弹幕 (0为开启  非0关闭)")
	private String barrageState;
	@ApiModelProperty("是否已经推送 (0为已推送  非0未推送)")
	private String isPush;

	@ApiModelProperty("关联‘节’列表")
	private List<Lesson> lessonList = ListUtils.newArrayList();
	
	public Chapter() {
		this(null);
	}

	public Chapter(String id){
		super(id);
	}
	
	public String getChapterId() {
		return chapterId;
	}

	public void setChapterId(String chapterId) {
		this.chapterId = chapterId;
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

	@NotBlank(message="章节名称不能为空")
	@Length(min=0, max=64, message="章节名称长度不能超过 64 个字符")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getChapterNum() {
		return chapterNum;
	}

	public void setChapterNum(Integer chapterNum) {
		this.chapterNum = chapterNum;
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSuffixName() {
		return suffixName;
	}

	public void setSuffixName(String suffixName) {
		this.suffixName = suffixName;
	}

	public String getIsShiftDown() {
		return isShiftDown;
	}

	public void setIsShiftDown(String isShiftDown) {
		this.isShiftDown = isShiftDown;
	}

	public List<Lesson> getLessonList() {
		return lessonList;
	}

	public void setLessonList(List<Lesson> lessonList) {
		this.lessonList = lessonList;
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

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	@Override
	public void setId_in(String[] ids) {
		if (ids == null || ids.length <= 0){
			ids = new String[]{"NONE_EXIST"};
		}
		this.sqlMap.getWhere().and(this.getIdColumnName(), QueryType.IN, ids);
	}

	public void selectBarrageState(String teachingClassId){
		//联表查询弹幕开关情况
		String barrageState = "(SELECT count(1) FROM ass_teach_class_barrage WHERE  chapter_id = a.chapter_id and teaching_class_id= "
				+ teachingClassId +" ) AS barrageState";
		this.getSqlMap().add("barrageState", barrageState);
	}

}