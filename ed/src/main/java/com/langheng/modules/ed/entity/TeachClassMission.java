/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.entity;


import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 课程任务关联Entity
 * @author xiaoxie
 * @version 2020-08-11
 */
@Table(name="ed_teaching_class_mission", alias="a", columns={
		@Column(name="teaching_class_id", attrName="teachingClassId", label="课堂id", isPK=true),
		@Column(name="mission_id", attrName="missionId", label="课程任务项id", isPK=true),
	}, orderBy="a.teaching_class_id DESC, a.mission_id DESC"
)
@ApiModel(description = "课程任务关联")
public class TeachClassMission extends DataEntity<TeachClassMission> {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("课堂id")
	private String teachingClassId;

	@ApiModelProperty("课程任务项id")
	private String missionId;
	
	public TeachClassMission() {
		this(null);
	}

	public TeachClassMission(String id){
		this.missionId = missionId;
	}
	
	public String getTeachingClassId() {
		return teachingClassId;
	}

	public void setTeachingClassId(String teachingClassId) {
		this.teachingClassId = teachingClassId;
	}
	
	public String getMissionId() {
		return missionId;
	}

	public void setMissionId(String missionId) {
		this.missionId = missionId;
	}
	
}