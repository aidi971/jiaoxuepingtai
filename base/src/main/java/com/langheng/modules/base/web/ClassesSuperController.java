/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.web;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.utils.RedisUtils;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.base.entity.Classes;
import com.langheng.modules.base.entity.StudentClasses;
import com.langheng.modules.base.service.BaseUserService;
import com.langheng.modules.base.service.ClassesService;
import com.langheng.modules.base.service.StudentClassesService;
import com.langheng.modules.base.utils.BaseUserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 班级Controller
 * @author xiaoxie
 * @version 2019-12-18
 */
@Api(description = "班级管理（学生管理）| 管理员端")
@RestController
@RequestMapping("admin/classes")
public class ClassesSuperController extends BaseApiController {

	@Autowired
	private ClassesService classesService;

	@Autowired
	private StudentClassesService studentClassesService;

	@Autowired
	private BaseUserService baseUserService;


	/**
	 * 获取班级数据
	 */
	@ModelAttribute
	public Classes get(String classesId, boolean isNewRecord) {
		return classesService.get(classesId, isNewRecord);
	}


	@ApiOperation("所有在线学生")
	@PostMapping("onlineStudentNum")
	public ResultBean<Integer>  onlineStudentNum() {
		Set set = RedisUtils.keys("user:classes:*");
		return success(set.size());
	}


	@ApiOperation("查询所有班级列表数据")
	@PostMapping("findAllClassesList")
	public ResultBean<List<Classes>>  findList(String teacherNameOrClassesName) {
		Classes  classes = new Classes();
		classes.setTeacherNameOrClassesName(teacherNameOrClassesName);
		List<Classes> classesList = classesService.findList(classes);
		return success(classesList);
	}

	@ApiOperation("强制下线")
	@PostMapping("tickOut")
	public ResultBean<List<Classes>>  tickOut(@RequestParam String classesId ) {
		StudentClasses studentClassesCriteria = new StudentClasses();
		studentClassesCriteria.setClassesId(classesId);
		List<StudentClasses> studentClassesList = studentClassesService.findList(studentClassesCriteria);
		List<String> studentIds = ListUtils.newArrayList();
		for (StudentClasses studentClasses : studentClassesList){
			studentIds.add(studentClasses.getStudentId());
		}
		BaseUserUtils.tickOut(studentIds.toArray(new String[]{}));

		// 学生在线人数 设为 0
		Classes classes = classesService.get(classesId);
		classes.setStuOnlineNum(0);
		classesService.save(classes);
		// 清除缓存
		RedisUtils.delByPattern("user:classes:" + classes.getClassesId() + ":*");

		return success(null,"强制下线成功！");
	}
}