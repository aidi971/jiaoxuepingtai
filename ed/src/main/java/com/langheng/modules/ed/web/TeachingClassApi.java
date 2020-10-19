/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.web;

import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.base.service.StudentClassesService;
import com.langheng.modules.ed.entity.TeachingClass;
import com.langheng.modules.ed.service.TeachingClassService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 课堂Controller
 * @author xiaoxie
 * @version 2020-02-11
 */
@Api(description = "学生的课堂 | 学生端")
@RestController
@RequestMapping(value = "api/teachingClass")
public class TeachingClassApi extends BaseApiController {

	@Autowired
	private TeachingClassService teachingClassService;
	@Autowired
	private StudentClassesService studentClassesService;
	/**
	 * 获取课堂数据
	 */
	@ModelAttribute
	public TeachingClass get(String teachingClassId, boolean isNewRecord) {
		return teachingClassService.get(teachingClassId, isNewRecord);
	}

	@ApiImplicitParam(name = "state",value = "课堂状态 ed_teaching_class_state")
 	@ApiOperation(value = "查询我的课堂列表数据")
	@PostMapping(value = "findList")
	public ResultBean<List<TeachingClass>> findList(String state) {
		TeachingClass teachingClassCriteria = new TeachingClass();
		teachingClassCriteria.setState(state);
		List<TeachingClass> teachingClassList = teachingClassService.findCurrentStudentList(teachingClassCriteria);
		return success(teachingClassList);
	}




}