/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.web;

import com.jeesite.common.entity.Page;
import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.base.entity.StudentClasses;
import com.langheng.modules.base.service.StudentClassesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 学生班级Controller
 * @author xiaoxie
 * @version 2019-12-18
 */
@Api(description = "学生班级相关接口")
@RestController
@RequestMapping(value = "admin/studentClasses")
public class StudentClassesController extends BaseApiController {

	@Autowired
	private StudentClassesService studentClassesService;
	
	/**
	 * 获取学生班级数据
	 */
	@ModelAttribute
	public StudentClasses get(String studentClassesId, boolean isNewRecord) {
		return studentClassesService.get(studentClassesId, isNewRecord);
	}
	
 	@ApiOperation(value = "分页查询学生班级列表数据")
	@PostMapping(value = "findPage")
	public ResultBean<Page<StudentClasses>>  findPage(StudentClasses studentClasses,
								@RequestParam(defaultValue = "10") int pageSize,
                                @RequestParam(defaultValue = "1") int pageNo ) {
		studentClasses.setPage(new Page<>(pageNo, pageSize));
		Page<StudentClasses> page = studentClassesService.findPage(studentClasses);
		return success(page);
	}


	@ApiOperation(value = "保存学生班级")
	@PostMapping(value = "save")
	public ResultBean save(@Validated StudentClasses studentClasses) {
		studentClassesService.save(studentClasses);
		return success();
	}

	@ApiOperation(value = "删除学生班级")
	@PostMapping(value = "delete")
	public ResultBean delete(StudentClasses studentClasses) {
		studentClassesService.delete(studentClasses);
		return success();
	}


}