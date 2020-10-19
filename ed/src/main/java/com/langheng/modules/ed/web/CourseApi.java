/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.web;


import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.ed.entity.*;
import com.langheng.modules.ed.service.*;
import com.langheng.modules.ed.util.CommonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 课程‘节’或者小节Controller
 * @author xiaoxie
 * @version 2019-12-17
 */
@Api(description = "课程管理 | 学生端")
@RestController
@RequestMapping(value = "api/course")
public class CourseApi extends BaseApiController {

	@Autowired
	private LessonService lessonService;
	@Autowired
	private ChapterService chapterService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private LessonTaskService lessonTaskService;
	@Autowired
	private TeachingClassService teachingClassService;

    @ApiOperation(value = "获取课程详情")
	@PostMapping(value = "getCourse")
	public ResultBean<Course> getCourse(@RequestParam String courseId) {
		return success(courseService.get(courseId));
	}

	@ApiOperation(value = "获取章节详情")
	@PostMapping(value = "getChapter")
	public ResultBean<Chapter> getChapter(@RequestParam String chapterId) {
		return success(chapterService.get(chapterId));
	}

	@ApiOperation(value = "获取节或者小节详情")
	@PostMapping(value = "getLesson")
	public ResultBean<Lesson> getLesson(@RequestParam String lessonId) {
		return success(lessonService.get(lessonId));
	}

	@ApiOperation(value = "获取资源详情")
	@PostMapping(value = "getLessonTask")
	public ResultBean<LessonTask> getLessonTask(@RequestParam String lessonTaskId) {
		return success(lessonTaskService.get(lessonTaskId));
	}

	@ApiOperation(value = "展开课程章节-节-小节结构")
	@PostMapping(value = "getCourseStruct")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
	})
	public ResultBean getCourseStruct(@RequestParam String teachingClassId) {
		TeachingClass teachingClass = teachingClassService.get(teachingClassId);
		Course course = courseService.get(teachingClass.getCourse());
		return success(CommonUtils.getCourseStruct(course));
	}

}