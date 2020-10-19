/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.web;

import com.jeesite.common.entity.Page;
import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.ed.entity.ClassesProgress;
import com.langheng.modules.ed.service.ClassesProgressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 课程进度Controller
 * @author xiaoxie
 * @version 2019-12-17
 */
@Api(description = "课程进度| 教师端")
@RestController
@RequestMapping(value = "admin/classesProgress")
public class ClassesProgressController extends BaseApiController {

	@Autowired
	private ClassesProgressService classesProgressService;
	
	/**
	 * 获取课程进度数据
	 */
	@ModelAttribute
	public ClassesProgress get(String progressId, boolean isNewRecord) {
		return classesProgressService.get(progressId, isNewRecord);
	}
	
 	@ApiOperation(value = "分页查询课程进度列表数据")
	@PostMapping(value = "findPage")
	public ResultBean<Page<ClassesProgress>> findPage(ClassesProgress classesProgress, HttpServletRequest request, HttpServletResponse response) {
		classesProgress.setPage(new Page<>(request, response));
		Page<ClassesProgress> page = classesProgressService.findPage(classesProgress);
		return success(page);
	}


    @ApiOperation(value = "查询课程进度列表数据")
	@PostMapping(value = "findList")
	public ResultBean<Page<ClassesProgress>>  findList(ClassesProgress classesProgress, HttpServletRequest request, HttpServletResponse response) {
		classesProgress.setPage(new Page<>(request, response));
		Page<ClassesProgress> page = classesProgressService.findPage(classesProgress);
		return success(page);
	}


	@ApiOperation(value = "保存课程进度")
	@PostMapping(value = "save")
	public ResultBean save(@Validated ClassesProgress classesProgress) {
		classesProgressService.save(classesProgress);
		return success();
	}







	@ApiOperation(value = "删除课程进度")
	@PostMapping(value = "delete")
	public ResultBean delete(ClassesProgress classesProgress) {
		classesProgressService.delete(classesProgress);
		return success();
	}


}