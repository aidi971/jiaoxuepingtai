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
 * 课程章节管理Entity
 * @author xiaoxie
 * @version 2020-04-21
 */
@Table(name="ed_teaching_class_chapter", alias="a", columns={
		@Column(name="teaching_class_id", attrName="teachingClassId", label="teaching_class_id",isPK = true),
		@Column(name="chapter_id", attrName="chapterId", label="chapter_id",isPK = true),
	}
)
@ApiModel(description = "课程章节管理")
public class TeachClassChapter extends DataEntity<TeachClassChapter> {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("teaching_class_id")
	private String teachingClassId;

	@ApiModelProperty("chapter_id")
	private String chapterId;


	public TeachClassChapter() {
		this(null);
	}

	public TeachClassChapter(String id){
		super(id);
	}

	@NotBlank(message="teaching_class_id不能为空")
	@Length(min=0, max=64, message="teaching_class_id长度不能超过 64 个字符")
	public String getTeachingClassId() {
		return teachingClassId;
	}

	public void setTeachingClassId(String teachingClassId) {
		this.teachingClassId = teachingClassId;
	}
	
	@NotBlank(message="chapter_id不能为空")
	@Length(min=0, max=64, message="chapter_id长度不能超过 64 个字符")
	public String getChapterId() {
		return chapterId;
	}

	public void setChapterId(String chapterId) {
		this.chapterId = chapterId;
	}
	

}