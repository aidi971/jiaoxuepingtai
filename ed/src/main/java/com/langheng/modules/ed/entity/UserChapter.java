/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 学生章节Entity
 * @author xiaoxie
 * @version 2019-12-17
 */
@Table(name="ed_user_chapter", alias="a", columns={
		@Column(name="chapter_id", attrName="chapterId", label="章节id",isPK = true),
		@Column(name="user_id", attrName="userId", label="学生id",isPK = true),
	}
)
@ApiModel(description = "学生章节")
public class UserChapter extends DataEntity<UserChapter> {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("章节id")
	private String chapterId;

	@ApiModelProperty("学生id")
	private String userId;

	@NotBlank(message="章节id不能为空")
	@Length(min=0, max=64, message="章节id长度不能超过 64 个字符")
	public String getChapterId() {
		return chapterId;
	}

	public void setChapterId(String chapterId) {
		this.chapterId = chapterId;
	}
	
	@NotBlank(message="学生id不能为空")
	@Length(min=0, max=64, message="学生id长度不能超过 64 个字符")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	

}