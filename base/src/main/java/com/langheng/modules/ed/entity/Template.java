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
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.langheng.modules.base.entity.Teacher;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 模板Entity
 * @author xiaoxie
 * @version 2019-12-17
 */
@Table(name="ed_template", alias="a", columns={
		@Column(name="template_id", attrName="templateId", label="模板id", isPK=true),
		@Column(name="trace_source_id", attrName="traceSourceId", label="溯源id"),
		@Column(name="parent_template_id", attrName="parentTemplate.templateId", label="关联模板id"),
		@Column(name="teacher_id", attrName="teacher.teacherId", label="教师id"),
		@Column(name="name", attrName="name", label="模板名称", queryType=QueryType.LIKE),
		@Column(name="full_name", attrName="fullName", label="课程完整名称"),
		@Column(name="version", attrName="version", label="版本号"),
		@Column(name="structure_type", attrName="structureType", label="模板结构类型"),
		@Column(name="type", attrName="type", label="模板类型 | 字典集 ed_template_type"),
		@Column(name="state", attrName="state", label="模板状态 | 字典集 ed_template_state"),
		@Column(name="is_public", attrName="isPublic", label="是否公开", comment="是否公开（1是 0否)"),
		@Column(name="is_enabled", attrName="isEnabled", label="是否启用", comment="是否启用（1是 0否)"),
		@Column(name="is_can_see", attrName="isCanSee", label="是否可见", comment="是否可见（1是 0否)"),
		@Column(name="is_modify", attrName="isModify", label="是否有修改", comment="是否有修改（1是 0否)"),
		@Column(name="introductions", attrName="introductions", label="课程描述"),
		@Column(name="img_src", attrName="imgSrc", label="图片路径"),
		@Column(name="video_num", attrName="videoNum", label="视频数量"),
		@Column(name="courseware_num", attrName="coursewareNum", label="课件数量"),
		@Column(name="homework_num", attrName="homeworkNum", label="作业数量"),
		@Column(name="cite_num", attrName="citeNum", label="引用次数"),
		@Column(name="apply_time", attrName="applyTime", label="申请时间"),
		@Column(name="audit_time", attrName="auditTime", label="审核时间"),
		@Column(name="cancel_time", attrName="cancelTime", label="取消公开时间"),
		@Column(includeEntity=DataEntity.class),
	}, joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=Teacher.class, attrName="teacher", alias="u11",
				on="u11.teacher_id = a.teacher_id", columns={
				@Column(includeEntity = Teacher.class),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=Template.class, attrName="parentTemplate", alias="u12",
				on="u12.template_id = a.parent_template_id", columns={
				@Column(includeEntity = Template.class),
		}),
	}, orderBy="a.apply_time DESC"
)
@ApiModel(description = "模板")
public class Template extends DataEntity<Template> {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("模板id")
	private String templateId;

	@ApiModelProperty("溯源id")
	private String traceSourceId;

	@ApiModelProperty("关联父模板")
	private Template parentTemplate;

	@ApiModelProperty("关联的教师")
	private Teacher teacher;

	@ApiModelProperty("模板名称")
	private String name;

	@ApiModelProperty("模板完整名称")
	private String fullName;

	@ApiModelProperty("课程版本号")
	private String version;

	@ApiModelProperty("模板结构类型")
	private String structureType;

	@ApiModelProperty("模板类型 | 字典集 ed_template_type")
	private String type;

	@ApiModelProperty("模板审核状态 | 字典集 ed_template_state")
	private String state;

	@ApiModelProperty(value = "是否公开（1是 0否)",hidden = true)
	private String isPublic;

	@ApiModelProperty(value = "是否启用（1是 0否)")
	private String isEnabled;

	@ApiModelProperty(value = "是否可见（1是 0否)")
	private String isCanSee;

	@ApiModelProperty(value = "是否有修改（1是 0否)")
	private String isModify;

	@ApiModelProperty("课程描述")
	private String introductions;

	@ApiModelProperty("图片路径")
	private String imgSrc;

	@ApiModelProperty("视频数量")
	private Integer videoNum;

	@ApiModelProperty("课件数量")
	private Integer coursewareNum;

	@ApiModelProperty("作业数量")
	private Integer homeworkNum;

	@ApiModelProperty("引用次数")
	private Integer citeNum;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	@ApiModelProperty("申请时间")
	private Date applyTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty("审核时间")
	private Date auditTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty("取消公开时间")
	private Date cancelTime;

	public String getIsCanSee() {
		return isCanSee;
	}

	public void setIsCanSee(String isCanSee) {
		this.isCanSee = isCanSee;
	}

	public String getTraceSourceId() {
		return traceSourceId;
	}

	public void setTraceSourceId(String traceSourceId) {
		this.traceSourceId = traceSourceId;
	}

	public Template() {
		this(null);
	}

	public Template(String id){
		super(id);
	}
	
	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public Template getParentTemplate() {
		return parentTemplate;
	}

	public void setParentTemplate(Template parentTemplate) {
		this.parentTemplate = parentTemplate;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	@NotBlank(message="模板名称不能为空")
	@Length(min=0, max=64, message="模板名称长度不能超过 64 个字符")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=16, message="模板类型 | 字典集 ed_template_type长度不能超过 16 个字符")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@NotBlank(message="是否公开不能为空")
	@Length(min=0, max=1, message="是否公开长度不能超过 1 个字符")
	public String getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(String isPublic) {
		this.isPublic = isPublic;
	}
	
	public String getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	@Length(min=0, max=225, message="课程描述长度不能超过 225 个字符")
	public String getIntroductions() {
		return introductions;
	}

	public void setIntroductions(String introductions) {
		this.introductions = introductions;
	}
	
	@Length(min=0, max=225, message="图片路径长度不能超过 225 个字符")
	public String getImgSrc() {
		return imgSrc;
	}

	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getCiteNum() {
		return citeNum;
	}

	public void setCiteNum(Integer citeNum) {
		this.citeNum = citeNum;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getIsModify() {
		return isModify;
	}

	public void setIsModify(String isModify) {
		this.isModify = isModify;
	}

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

	public Date getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getStructureType() {
		return structureType;
	}

	public void setStructureType(String structureType) {
		this.structureType = structureType;
	}
}