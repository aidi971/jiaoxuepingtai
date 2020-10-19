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
 * 学生课程任务项Entity
 * @author xiaoxie
 * @version 2020-08-11
 */
@Table(name="ed_user_mission", alias="a", columns={
		@Column(name="mission_id", attrName="missionId", label="小节id", isPK=true),
		@Column(name="user_id", attrName="userId", label="学生id", isPK=true),
	}, orderBy="a.mission_id DESC, a.user_id DESC"
)
@ApiModel(description = "学生课程任务项")
public class UserMission extends DataEntity<UserMission> {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("小节id")
	private String missionId;

	@ApiModelProperty("学生id")
	private String userId;
	
	public UserMission() {
		this(null);
	}

	public UserMission(String id){
		super(id);
	}
	
	public String getMissionId() {
		return missionId;
	}

	public void setMissionId(String missionId) {
		this.missionId = missionId;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}