/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 学生小节Entity
 * @author xiaoxie
 * @version 2019-12-17
 */
@Table(name="ed_user_lesson", alias="a", columns={
		@Column(name="lesson_id", attrName="lessonId", label="节或小节id",isPK = true),
		@Column(name="user_id", attrName="userId", label="学生id",isPK = true),
	}
)
@ApiModel(description = "学生小节")
public class UserLesson extends DataEntity<UserLesson> {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("小节id")
	private String lessonId;

	@ApiModelProperty("学生id")
	private String userId;

	public UserLesson() {
		this(null);
	}

	public UserLesson(String id){
		super(id);
	}
	
	@NotBlank(message="小节id不能为空")
	@Length(min=0, max=64, message="小节id长度不能超过 64 个字符")
	public String getLessonId() {
		return lessonId;
	}

	public void setLessonId(String lessonId) {
		this.lessonId = lessonId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


    public void setLessonId_id(String[] lessonIdIds) {
		if (lessonIdIds.length > 0){
			this.getSqlMap().getWhere().and("lesson_id", QueryType.IN,lessonIdIds);
		}else {
			this.getSqlMap().getWhere().and("lesson_id", QueryType.IN,"NOT_EXIST");
		}

    }
}