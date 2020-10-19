/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.web;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.ed.entity.Lesson;
import com.langheng.modules.ed.entity.TeachingClass;
import com.langheng.modules.ed.service.LessonService;
import com.langheng.modules.ed.service.TeachingClassService;
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

import java.util.List;
import java.util.Map;

/**
 * 学生任务Controller
 * @author xiaoxie
 * @version 2019-12-17
 */
@Api(description = "进行中和已完成的学生任务 | 学生端")
@RestController
@RequestMapping(value = "api/userTask")
public class LearnTaskApi extends BaseApiController {

	@Autowired
	private LessonService lessonService;
	@Autowired
	private TeachingClassService teachingClassService;


	@ApiOperation(value = "获取课堂 学习任务列表 （任务一  任务二）")
	@PostMapping(value = "findListLearnTask")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
	})
	public ResultBean<List<Lesson>>  findListLearnTask(@RequestParam String teachingClassId) {
		TeachingClass teachingClass= teachingClassService.get(teachingClassId);
		Map<String,Integer> lessonTaskNumMap = CommonUtils.getLessonTaskNumMap(teachingClass.getCourse());

		List<Lesson> lessonList = ListUtils.newArrayList();
		if (!lessonTaskNumMap.keySet().isEmpty()){
			Lesson lessonCriteria = new Lesson();
			lessonCriteria.setId_in(lessonTaskNumMap.keySet().toArray(new String[]{}));
			lessonList = lessonService.findList(lessonCriteria);
			lessonList.forEach(lesson -> {
				lesson.setTaskNum(lessonTaskNumMap.get(lesson.getId()));
			});
		}

		return success(lessonList);
	}


}