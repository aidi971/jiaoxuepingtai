/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.web;


import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.base.entity.Teacher;
import com.langheng.modules.base.utils.TeacherUtils;
import com.langheng.modules.ed.entity.Chapter;
import com.langheng.modules.ed.entity.Course;
import com.langheng.modules.ed.entity.Template;
import com.langheng.modules.ed.enumn.TemplateState;
import com.langheng.modules.ed.service.CommonService;
import com.langheng.modules.ed.service.CourseService;
import com.langheng.modules.ed.service.TemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 课程Controller
 * @author xiaoxie
 * @version 2019-12-17
 */
@Api(description = "课程管理 | 教师端")
@RestController
@RequestMapping(value = "admin/course")
public class CourseController extends BaseApiController {

	@Autowired
	private CourseService courseService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private TemplateService templateService;

	@ModelAttribute
	public Course get(String courseId, boolean isNewRecord) {
		return courseService.get(courseId, isNewRecord);
	}
	
 	@ApiOperation(value = "查询教师的课程列表数据")
	@PostMapping(value = "findList")
	public ResultBean<List<Course>> findList(Course course) {
		course.setTeacher(TeacherUtils.getTeacher());
		List<Course> courseList = courseService.findList(course);
		return success(courseList);
	}

	@ApiOperation(value = "新增或修改课程")
	@PostMapping(value = "save")
	public ResultBean save(@Validated Course course) {
		if (course.getIsNewRecord()) {
			Course courseCriteria = new Course();
			courseCriteria.setName(course.getName());
			courseCriteria.setTeacher(new Teacher(TeacherUtils.getTeacher().getTeacherId()));
			Long count = courseService.findCount(courseCriteria);
			if (count > 0){
				return error("该课程名称已存在!");
			}
		}
		courseService.save(course);
		return success(course);
	}

	@ApiOperation(value = "展开课程，显示所有章节-节-小节-资源")
	@PostMapping(value = "expand")
	public ResultBean<List<Chapter>>  expand(@RequestParam String courseId) {
	    Course course = courseService.get(courseId);
		return success(commonService.expandByCourse(course));
	}

	@ApiOperation(value = "课程另存")
	@PostMapping(value = "saveAs")
	public ResultBean saveAs(@RequestParam String courseId,
							 @RequestParam String name) {
		Course course = courseService.get(courseId);
		course.setName(name);
		Course newCourse = commonService.saveAsCourse(course);
		return success(newCourse.getFullName());
	}

	@ApiOperation(value = "课程申请公开")
	@PostMapping(value = "applyPublic")
	public ResultBean applyPublic(@RequestParam String courseId) {
		Course course = courseService.get(courseId);
		Template templateCriteria = new Template();
		templateCriteria.setTeacher(TeacherUtils.getTeacher());
		templateCriteria.setTraceSourceId(course.getId());
		List<Template> templateList = templateService.findList(templateCriteria);
		if (!templateList.isEmpty()){
			for (Template template : templateList){
				if (TemplateState.HAD_PUBLIC.value().equals(template.getState())){
					return error("标准模板/公共模板中已存在同名称的，请先另存后再申请！");
				}else if (TemplateState.UN_AUDITED.value().equals(template.getState())){
					return error("该课程审核已提交,请耐心等待！");
				}
			}
		}
		commonService.applyPublic(course);
		return success();
	}

}