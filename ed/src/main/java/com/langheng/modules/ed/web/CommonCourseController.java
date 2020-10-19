/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.web;


import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.base.entity.UserHead;
import com.langheng.modules.base.service.UserHeadService;
import com.langheng.modules.base.utils.TeacherUtils;
import com.langheng.modules.ed.entity.Course;
import com.langheng.modules.ed.entity.TeachingClass;
import com.langheng.modules.ed.service.CourseService;
import com.langheng.modules.ed.service.TeachingClassService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 课程Controller
 * @author xiaoxie
 * @version 2019-12-17
 */
@Api(description = "共用-课程管理 | 教师端")
@RestController
@RequestMapping(value = "admin/course")
public class CommonCourseController extends BaseApiController {

	@Autowired
	private CourseService courseService;
	@Autowired
	private UserHeadService userHeadService;
	@Autowired
	private TeachingClassService teachingClassService;

	@ModelAttribute
	public Course get(String courseId, boolean isNewRecord) {
		return courseService.get(courseId, isNewRecord);
	}
	
 	@ApiOperation(value = "查询教师的课程列表数据")
	@PostMapping(value = "findListAllType")
	public ResultBean<List<Course>> findListAllType(Course course) {
		course.setTeacher(TeacherUtils.getTeacher());
		List<Course> courseList = courseService.findList(course);
		return success(courseList);
	}


	@ApiOperation(value = "停用课程")
	@PostMapping(value = "disable")
	public ResultBean disable(@RequestParam String courseId) {
		Course course = courseService.get(courseId);
		course.setStatus(Course.STATUS_DISABLE);
		courseService.updateStatus(course);
		return success();
	}


	@ApiOperation(value = "启用课程")
	@PostMapping(value = "enable")
	public ResultBean enable(@RequestParam String courseId) {
		Course course = courseService.get(courseId);
		courseService.updateStatus(course);
		return success();
	}


	@ApiOperation(value = "删除课程")
	@PostMapping(value = "delete")
	public ResultBean delete(@RequestParam String courseId) {
		Course course = courseService.get(courseId);

		TeachingClass teachingClass = new TeachingClass();
		teachingClass.setCourse(new Course(course.getId()));
		if (teachingClassService.findCount(teachingClass) > 0){
			return error("该课程已经关联课堂，不可删除！");
		}
		courseService.delete(course);
		return success();
	}

	@ApiOperation("随机封面")
	@PostMapping("/randomCoverImg")
	public ResultBean randomHeadImg() {
		UserHead userHead = userHeadService.getUserHead(7);
		return success(userHead.getHeadUrl());
	}
}