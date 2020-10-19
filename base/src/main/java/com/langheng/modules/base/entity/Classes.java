/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.entity;

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
import java.util.Date;

/**
 * 班级Entity
 * @author xiaoxie
 * @version 2019-12-18
 */
@Table(name="sys_classes", alias="a", columns={
		@Column(name="classes_id", attrName="classesId", label="班级id", isPK=true),
		@Column(name="class_name", attrName="className", label="教学班名称", queryType=QueryType.LIKE),
		@Column(name="student_num", attrName="studentNum", label="学员数"),
		@Column(name="stu_online_num", attrName="stuOnlineNum", label="在线人数"),
		@Column(name="cover_img", attrName="coverImg", label="封面"),
		@Column(name="open_invite", attrName="openInvite", label="是否开启邀请码"),
		@Column(name="invite_close_time", attrName="inviteCloseTime", label="邀请关闭时间"),
		@Column(name="is_need_audit", attrName="isNeedAudit", label="是否需要审核"),
		@Column(name="invitation_code", attrName="invitationCode", label="邀请码"),
		@Column(name="teacher_id", attrName="teacher.teacherId", label="教师ID"),
		@Column(name="academy_id", attrName="academy.academyId", label="学院ID"),
		@Column(name="major_id", attrName="major.majorId", label="专业ID"),
		@Column(includeEntity=DataEntity.class),
	}, joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=Teacher.class, attrName="teacher", alias="u11",
				on="u11.teacher_id = a.teacher_id", columns={
				@Column(includeEntity = Teacher.class),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=Academy.class, attrName="academy", alias="u13",
				on="u13.academy_id = a.academy_id", columns={
				@Column(includeEntity = Academy.class),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=Major.class, attrName="major", alias="u12",
				on="u12.major_id = a.major_id", columns={
				@Column(includeEntity = Major.class),
		}),
	}, orderBy="a.update_date DESC"
)
@ApiModel(description = "班级")
public class Classes extends DataEntity<Classes> {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("班级id")
	private String classesId;

	@ApiModelProperty(value = "教学班名称",required = true)
	private String className;

	@ApiModelProperty("教师")
	private Teacher teacher;

	@ApiModelProperty("学员数")
	private Long studentNum;

	@ApiModelProperty(value = "班级封面",required = true)
	private String coverImg;

	@ApiModelProperty("是否开启邀请码 | 字典集 sys_yes_no")
	private String openInvite;

	@ApiModelProperty("邀请码")
	private String invitationCode;

	@ApiModelProperty("邀请关闭事件")
	private Date inviteCloseTime;

	@ApiModelProperty("是否需要审核 | 字典集 sys_yes_no")
	private String isNeedAudit;

	@ApiModelProperty("学生在线人数")
	private Integer stuOnlineNum;

	@ApiModelProperty(value = "所属学院",required = true)
	private Academy academy;

	@ApiModelProperty(value = "所属专业",required = true)
	private Major major;

	/**
	 * 查询专用
	 */
	private String teacherNameOrClassesName;

	public Classes() {
		this(null);
	}

	public Classes(String id){
		super(id);
	}
	
	public String getClassesId() {
		return classesId;
	}

	public void setClassesId(String classesId) {
		this.classesId = classesId;
	}
	
	@NotBlank(message="教学班名称不能为空")
	@Length(min=0, max=64, message="教学班名称长度不能超过 64 个字符")
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Long getStudentNum() {
		return studentNum;
	}

	public void setStudentNum(Long studentNum) {
		this.studentNum = studentNum;
	}


	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public String getCoverImg() {
		return coverImg;
	}

	public void setCoverImg(String coverImg) {
		this.coverImg = coverImg;
	}

	public String getInvitationCode() {
		return invitationCode;
	}

	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}

	public String getOpenInvite() {
		return openInvite;
	}

	public void setOpenInvite(String openInvite) {
		this.openInvite = openInvite;
	}

	public Academy getAcademy() {
		return academy;
	}

	public void setAcademy(Academy academy) {
		this.academy = academy;
	}

	public Major getMajor() {
		return major;
	}

	public void setMajor(Major major) {
		this.major = major;
	}

	public Integer getStuOnlineNum() {
		return stuOnlineNum;
	}

	public void setStuOnlineNum(Integer stuOnlineNum) {
		this.stuOnlineNum = stuOnlineNum;
	}

	public String getIsNeedAudit() {
		return isNeedAudit;
	}

	public void setIsNeedAudit(String isNeedAudit) {
		this.isNeedAudit = isNeedAudit;
	}

	public Date getInviteCloseTime() {
		return inviteCloseTime;
	}

	public void setInviteCloseTime(Date inviteCloseTime) {
		this.inviteCloseTime = inviteCloseTime;
	}

	public String getTeacherNameOrClassesName() {
		return teacherNameOrClassesName;
	}

	public void setTeacherNameOrClassesName(String teacherNameOrClassesName) {
		this.teacherNameOrClassesName = teacherNameOrClassesName;
	}

	public void disableStatus(){
		sqlMap.getWhere().disableAutoAddStatusWhere();
		this.status = "";
	}

	public void setInviteCloseTime_lte(Date inviteCloseTime){
		this.getSqlMap().getWhere().and("invite_close_time",QueryType.LTE,inviteCloseTime);

	}
}