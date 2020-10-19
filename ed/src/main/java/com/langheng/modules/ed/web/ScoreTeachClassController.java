/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.web;

import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.ed.entity.*;
import com.langheng.modules.ed.service.*;
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

/**
 * 课堂答案管理Controller
 * @author xiaoxie
 * @version 2020-04-26
 */
@Api(description = "课堂赋分管理")
@RestController
@RequestMapping(value = "admin/scoreTeachClass")
public class ScoreTeachClassController extends BaseApiController {

	@Autowired
	private TeachingClassService teachingClassService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private ChapterService chapterService;
	@Autowired
	private LessonService lessonService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private TeachClassScoreService teachClassScoreService;
	@Autowired
	private LessonTaskService lessonTaskService;

	@ApiOperation(value = "课程所有结构，显示所有章节-节-小节-资源")
	@PostMapping(value = "scoreStruct")
	public ResultBean<List<Chapter>>  scoreStruct(@RequestParam String teachingClassId) {
		TeachingClass teachingClass = teachingClassService.get(teachingClassId);
		Course course = courseService.get(teachingClass.getCourse());
		return success(teachClassScoreService.scoreStruct(course,teachingClassId));
	}

	@ApiOperation(value = "任务赋分")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "lessonTaskIds", value = "任务id，如果多个用逗号分隔", required = true),
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
			@ApiImplicitParam(name = "scores", value = "分数", required = true),
	})
	@PostMapping(value = "lessonTaskSetScore")
	public ResultBean lessonTaskSetScore(@RequestParam String lessonTaskIds,
									  @RequestParam String teachingClassId,
									  @RequestParam String scores) {
		String [] lessonTaskIdArr = lessonTaskIds.split(",");
		String [] scoreArr = scores.split(",");
		if (lessonTaskIdArr.length != scoreArr.length){
			return error("参数异常！");
		}
		for (int index=0 ; index < lessonTaskIdArr.length; index ++){
			String lessonTaskId = lessonTaskIdArr[index];
			Float score = Float.parseFloat(scoreArr[index]);

			LessonTask lessonTask = lessonTaskService.get(lessonTaskId);
			if (lessonTask != null && StringUtils.isNotBlank(lessonTask.getLessonId())){
				// 先删除原先的
				TeachClassScore teachClassScore = new TeachClassScore();
				teachClassScore.setLessonTaskId(lessonTaskId);
				teachClassScore.setTeachingClassId(teachingClassId);
				teachClassScoreService.deleteByEntity(teachClassScore);
				teachClassScore.setScore(score);
				teachClassScore.setIsNewRecord(true);

				teachClassScoreService.save(teachClassScore);
			}
		}

		return success();
	}

}