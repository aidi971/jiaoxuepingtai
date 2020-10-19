/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.web;

import com.jeesite.common.entity.Page;
import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.base.entity.TeacherAcademy;
import com.langheng.modules.base.service.TeacherAcademyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 教师学院Controller
 * @author xiaoxie
 * @version 2019-12-20
 */
@Api(description = "教师学院")
@RestController
@RequestMapping(value = "admin/teacherAcademy")
public class TeacherAcademyController extends BaseApiController {

	@Autowired
	private TeacherAcademyService teacherAcademyService;
	
	/**
	 * 获取教师学院数据
	 */
	@ModelAttribute
	public TeacherAcademy get(String teaAcaId, boolean isNewRecord) {
		return teacherAcademyService.get(teaAcaId, isNewRecord);
	}
	
 	@ApiOperation(value = "分页查询教师学院列表数据")
	@PostMapping(value = "findPage")
	public ResultBean<Page<TeacherAcademy>>  findPage(TeacherAcademy teacherAcademy,
								@RequestParam(defaultValue = "10") int pageSize,
                                @RequestParam(defaultValue = "1") int pageNo ) {
		teacherAcademy.setPage(new Page<>(pageNo, pageSize));
		Page<TeacherAcademy> page = teacherAcademyService.findPage(teacherAcademy);
		return success(page);
	}

	@ApiOperation(value = "保存教师学院")
	@PostMapping(value = "save")
	public ResultBean save(@Validated TeacherAcademy teacherAcademy) {
		teacherAcademyService.save(teacherAcademy);
		return success();
	}


	@ApiOperation(value = "删除教师学院")
	@PostMapping(value = "delete")
	public ResultBean delete(TeacherAcademy teacherAcademy) {
		teacherAcademyService.delete(teacherAcademy);
		return success();
	}


}