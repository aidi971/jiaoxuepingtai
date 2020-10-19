/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.web;

import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.enumn.SysYesNoEnum;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.base.service.UserHeadService;
import com.langheng.modules.base.utils.BaseUserUtils;
import com.langheng.modules.base.utils.TeacherUtils;
import com.langheng.modules.ed.entity.TeachingClass;
import com.langheng.modules.ed.service.TeachingClassService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
@Api(description = "课堂管理")
@RestController
@RequestMapping(value = "admin/teachingClass")
public class TeachingClassController extends BaseApiController {

	@Autowired
	private TeachingClassService teachingClassService;
	@Autowired
	private UserHeadService userHeadService;

	/**
	 * 获取课堂数据
	 */
	@ModelAttribute
	public TeachingClass get(String teachingClassId, boolean isNewRecord) {
		return teachingClassService.get(teachingClassId, isNewRecord);
	}
	
 	@ApiOperation(value = "查询课堂列表数据")
	@PostMapping(value = "findList")
	public ResultBean<List<TeachingClass>> findPage(TeachingClass teachingClass) {
		if (!BaseUserUtils.isSuperAdmin()) {
			teachingClass.setTeacher(TeacherUtils.getTeacher());
		}
		List<TeachingClass> list = teachingClassService.findList(teachingClass);
		return success(list);
	}

	@ApiOperation(value = "新增或修改课堂")
	@PostMapping(value = "save")
	public ResultBean save(@Validated TeachingClass teachingClass) {
		if (teachingClass.getIsNewRecord()){
			teachingClass.setImgSrc( userHeadService.getUserHead(7).getHeadUrl());
		}
		teachingClassService.save(teachingClass);
		return success(teachingClass);
	}

	@ApiOperation(value = "获取下一个课堂编码")
	@PostMapping(value = "getNextClassCode")
	public ResultBean getNextClassCode() {
		return success(teachingClassService.findCountIgnoreStatus() + 1);
	}

	@ApiOperation(value = "暂停教学班")
	@PostMapping(value = "stopTeaching")
	public ResultBean stopTeaching(String teachingClassId) {
		TeachingClass teachingClass = teachingClassService.get(teachingClassId);
		teachingClass.setIsStop(SysYesNoEnum.YES.value());
		teachingClassService.save(teachingClass);
		return success();
	}

	@ApiOperation(value = "开启教学班")
	@PostMapping(value = "unStopTeaching")
	public ResultBean unStopTeaching(String teachingClassId) {
		TeachingClass teachingClass = teachingClassService.get(teachingClassId);
		teachingClass.setIsStop(SysYesNoEnum.NO.value());
		teachingClassService.save(teachingClass);
		return success();
	}

}