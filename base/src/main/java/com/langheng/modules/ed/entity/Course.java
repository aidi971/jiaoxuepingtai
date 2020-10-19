/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.langheng.modules.base.entity.Teacher;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 课程Entity
 * @author xiaoxie
 * @version 2019-12-17
 */
@Table(name="ed_course", alias="a", columns={
		@Column(name="course_id", attrName="courseId", label="课程id", isPK=true),
		@Column(name="template_id", attrName="template.templateId", label="关联模板id"),
		@Column(name="teacher_id", attrName="teacher.teacherId", label="教师id"),
		@Column(name="structure_type", attrName="structureType", label="课程结构类型"),
		@Column(name="version", attrName="version", label="版本号"),
		@Column(name="iterations", attrName="iterations", label="课程迭代次数"),
		@Column(name="name", attrName="name", label="课程名称", queryType=QueryType.LIKE),
		@Column(name="full_name", attrName="fullName", label="课程完整名称"),
		@Column(name="img_src", attrName="imgSrc", label="图片路径"),
		@Column(name="state", attrName="state", label="课程状态| 字典集 ed_course_state"),
		@Column(name="is_enable_barrage", attrName="isEnableBarrage", label="是否开启弹幕"),
		@Column(name="introduce", attrName="introduce", label="课程介绍"),
		@Column(name="video_num", attrName="videoNum", label="视频数量"),
		@Column(name="courseware_num", attrName="coursewareNum", label="课件数量"),
		@Column(name="homework_num", attrName="homeworkNum", label="作业数量"),
		@Column(includeEntity=DataEntity.class),
	}, joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=Teacher.class, attrName="teacher", alias="u11",
				on="u11.teacher_id = a.teacher_id", columns={
				@Column(includeEntity = Teacher.class),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=Template.class, attrName="template", alias="u12",
				on="u12.template_id = a.template_id", columns={
				@Column(includeEntity = Template.class),
		}),
	},  orderBy="a.update_date DESC"
)
@ApiModel(description = "课程")
public class Course extends DataEntity<Course> {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("课程id")
	private String courseId;
	@ApiModelProperty("课程版本号")
	private String version;
	@ApiModelProperty("课程迭代次数")
	private String iterations;
	@ApiModelProperty("关联模板 | 引用模板产生的课程，要传")
	private Template template;
	@ApiModelProperty("教师")
	private Teacher teacher;
	@ApiModelProperty(value = "课程名称",required = true)
	private String name;
	@ApiModelProperty(value ="完整课程名| teacher.userName-name-(iterations).version")
	private String fullName;
	@ApiModelProperty("图片路径")
	private String imgSrc;
	@ApiModelProperty("课程结构类型")
	private String structureType;
	@ApiModelProperty("课程状态| 字典集 ed_course_state")
	private String state;
	@ApiModelProperty("是否开启弹幕|（0关闭, 1开启）")
	private String isEnableBarrage;
	@ApiModelProperty(value = "课程介绍",required = true)
	private String introduce;
	@ApiModelProperty("视频数量")
	private Integer videoNum;
	@ApiModelProperty("课件数量")
	private Integer coursewareNum;
	@ApiModelProperty("作业数量")
	private Integer homeworkNum;

	/**
	 * 以下为VO对象
	 */
	@ApiModelProperty("总的任务数量")
	private Integer totalLessonNum;
	@ApiModelProperty("已推送的任务数量")
	private Integer hadPushLessonNum;
	@ApiModelProperty("已推送的资源数量")
	private Integer HadPushLessonTaskNum;
	@ApiModelProperty("是否来源标准模板")
	private Boolean isFromStandardTemplate = false;

	public Course() {
		this(null);
	}

	public Course(String id){
		super(id);
	}
	
	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}


	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	@NotBlank(message="课程名称不能为空")
	@Length(min=0, max=64, message="课程名称长度不能超过 64 个字符")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=225, message="图片路径长度不能超过 225 个字符")
	public String getImgSrc() {
		return imgSrc;
	}

	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getIsEnableBarrage() {
		return isEnableBarrage;
	}

	public void setIsEnableBarrage(String isEnableBarrage) {
		this.isEnableBarrage = isEnableBarrage;
	}

	public String getStructureType() {
		return structureType;
	}

	public void setStructureType(String structureType) {
		this.structureType = structureType;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getIntroduce() {
		return introduce;
	}

	public String getIterations() {
		return iterations;
	}

	public void setIterations(String iterations) {
		this.iterations = iterations;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Integer getTotalLessonNum() {
		return totalLessonNum;
	}

	public void setTotalLessonNum(Integer totalLessonNum) {
		this.totalLessonNum = totalLessonNum;
	}

	public Integer getHadPushLessonNum() {
		return hadPushLessonNum;
	}

	public void setHadPushLessonNum(Integer hadPushLessonNum) {
		this.hadPushLessonNum = hadPushLessonNum;
	}

	public Integer getHadPushLessonTaskNum() {
		return HadPushLessonTaskNum;
	}

	public void setHadPushLessonTaskNum(Integer hadPushLessonTaskNum) {
		HadPushLessonTaskNum = hadPushLessonTaskNum;
	}

	public Boolean getFromStandardTemplate() {
		return isFromStandardTemplate;
	}

	public void setFromStandardTemplate(Boolean fromStandardTemplate) {
		isFromStandardTemplate = fromStandardTemplate;
	}

	public Integer getVideoNum() {
		return videoNum;
	}

	public void setVideoNum(Integer videoNum) {
		this.videoNum = videoNum;
	}

	public Integer getCoursewareNum() {
		return coursewareNum;
	}

	public void setCoursewareNum(Integer coursewareNum) {
		this.coursewareNum = coursewareNum;
	}

	public Integer getHomeworkNum() {
		return homeworkNum;
	}

	public void setHomeworkNum(Integer homeworkNum) {
		this.homeworkNum = homeworkNum;
	}

    public void setIterations_not_eq(String iterations) {
		sqlMap.getWhere().and("iterations", QueryType.NE,iterations);
    }
}