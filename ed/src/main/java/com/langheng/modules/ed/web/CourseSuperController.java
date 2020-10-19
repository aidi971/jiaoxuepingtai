/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.web;

import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.ed.entity.Course;
import com.langheng.modules.ed.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 课程Controller
 * @author xiaoxie
 * @version 2019-12-17
 */
@Api(description = "课程管理 | 管理员端")
@RestController
@RequestMapping(value = "admin/course")
public class CourseSuperController extends BaseApiController {

	@Autowired
	private CourseService courseService;

	@ModelAttribute
	public Course get(String courseId, boolean isNewRecord) {
		return courseService.get(courseId, isNewRecord);
	}
	
 	@ApiOperation(value = "查询所有的课程列表数据")
	@PostMapping(value = "findAllList")
	public ResultBean<List<Course>> findAllList() {
		List<Course> courseList = courseService.findList(new Course());
		return success(courseList);
	}


}