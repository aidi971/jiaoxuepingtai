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
 * 课堂任务推送Controller
 * @author xiaoxie
 * @version 2019-12-17
 */
@Api(description = "答案推送 | 教师端")
@RestController
@RequestMapping(value = "admin/answerTeachClass")
public class AnswerTeachClassController extends BaseApiController {
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
	private TeachClassAnswerService teachClassAnswerService;
	@Autowired
	private LessonTaskService lessonTaskService;

	@ApiOperation(value = "课程所有结构，显示所有章节-节-小节-资源")
	@PostMapping(value = "answerStruct")
	public ResultBean<List<Chapter>>  answerStruct(@RequestParam String teachingClassId) {
		TeachingClass teachingClass = teachingClassService.get(teachingClassId);
		Course course = courseService.get(teachingClass.getCourse());
		return success(teachClassAnswerService.answerStruct(course,teachingClassId));
	}

	@ApiOperation(value = "开启课程")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
	})
	@PostMapping(value = "openByCourse")
	public ResultBean openByCourse(@RequestParam String teachingClassId) {
		TeachingClass teachingClass = teachingClassService.get(teachingClassId);
		if (teachingClass.getCourse() != null && StringUtils.isNotBlank(teachingClass.getCourse().getCourseId())){
			// 先删除原先的
			TeachClassAnswer teachClassAnswer = new TeachClassAnswer();
			teachClassAnswer.setTeachingClassId(teachingClassId);
			teachClassAnswerService.deleteByEntity(teachClassAnswer);

			List<LessonTask> lessonTaskList = commonService.findLessonTasksByCourse(teachingClass.getCourse());
			teachClassAnswerService.saveByLessonTasks(lessonTaskList,teachingClassId);
		}
		return success();
	}

	@ApiOperation(value = "关闭课程")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
	})
	@PostMapping(value = "closeByCourse")
	public ResultBean closeByCourse(@RequestParam String teachingClassId) {
		TeachClassAnswer teachClassAnswer = new TeachClassAnswer();
		teachClassAnswer.setTeachingClassId(teachingClassId);
		teachClassAnswerService.deleteByEntity(teachClassAnswer);
		return success();
	}

	@ApiOperation(value = "关闭章节答案")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "chapterId", value = "章节id", required = true),
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
	})
	@PostMapping(value = "closeByChapter")
	public ResultBean closeByChapter(@RequestParam String chapterId,
									 @RequestParam String teachingClassId) {
		TeachClassAnswer teachClassAnswer = new TeachClassAnswer();
		teachClassAnswer.setChapterId(chapterId);
		teachClassAnswer.setTeachingClassId(teachingClassId);
		teachClassAnswerService.deleteByEntity(teachClassAnswer);
		return success();
	}

	@ApiOperation(value = "开启章节答案")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "chapterId", value = "章节id", required = true),
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
	})
	@PostMapping(value = "openByChapter")
	public ResultBean openByChapter(@RequestParam String chapterId,
									@RequestParam String teachingClassId) {

		Chapter chapter = chapterService.get(chapterId);
		if (chapter != null && StringUtils.isNotBlank(chapter.getChapterId())){
			List<Lesson> lessonList = commonService.findLessonsByChapter(chapter);
			if (lessonList.isEmpty()){
				return error("该章内容为空，请完善后再推送");
			}
			// 先删除原先的
			TeachClassAnswer teachClassAnswer = new TeachClassAnswer();
			teachClassAnswer.setChapterId(chapterId);
			teachClassAnswer.setTeachingClassId(teachingClassId);
			teachClassAnswerService.deleteByEntity(teachClassAnswer);

			List<LessonTask> lessonTaskList = commonService.findLessonTasksByChapter(chapter);
			teachClassAnswerService.saveByLessonTasks(lessonTaskList,teachingClassId);
		}

		return success();
	}

	@ApiOperation(value = "关闭节答案")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "lessonId", value = "节id", required = true),
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
	})
	@PostMapping(value = "closeByLesson")
	public ResultBean closeByLesson(@RequestParam String lessonId,
									@RequestParam String teachingClassId) {
		TeachClassAnswer teachClassAnswer = new TeachClassAnswer();
		teachClassAnswer.setLessonId(lessonId);
		teachClassAnswer.setTeachingClassId(teachingClassId);
		teachClassAnswerService.deleteByEntity(teachClassAnswer);
		return success();

	}


	@ApiOperation(value = "开启节答案")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "lessonId", value = "节id", required = true),
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
	})
	@PostMapping(value = "openByLesson")
	public ResultBean openByLesson(@RequestParam String lessonId,
								   @RequestParam String teachingClassId) {
		Lesson lesson = lessonService.get(lessonId);
		if (lesson != null && StringUtils.isNotBlank(lesson.getLessonId())){
			// 先删除原先的
			TeachClassAnswer teachClassAnswer = new TeachClassAnswer();
			teachClassAnswer.setLessonId(lessonId);
			teachClassAnswer.setTeachingClassId(teachingClassId);
			teachClassAnswerService.deleteByEntity(teachClassAnswer);

			List<LessonTask> lessonTaskList = commonService.findLessonTasksByLesson(lesson);
			teachClassAnswerService.saveByLessonTasks(lessonTaskList,teachingClassId);
		}

		return success();
	}

	@ApiOperation(value = "关闭小节答案")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "subLessonId", value = "小节id", required = true),
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
	})
	@PostMapping(value = "closeBySubLesson")
	public ResultBean closeBySubLesson(@RequestParam String subLessonId,
									   @RequestParam String teachingClassId) {
		TeachClassAnswer teachClassAnswer = new TeachClassAnswer();
		teachClassAnswer.setSubLessonId(subLessonId);
		teachClassAnswer.setTeachingClassId(teachingClassId);
		teachClassAnswerService.deleteByEntity(teachClassAnswer);
		return success();
	}


	@ApiOperation(value = "开启小节答案")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "subLessonId", value = "小节id", required = true),
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
	})
	@PostMapping(value = "openBySubLesson")
	public ResultBean openBySubLesson(@RequestParam String subLessonId,
									  @RequestParam String teachingClassId) {
		Lesson subLesson = lessonService.get(subLessonId);
		if (subLesson != null && StringUtils.isNotBlank(subLesson.getLessonId())){
			// 先删除原先的
			TeachClassAnswer teachClassAnswer = new TeachClassAnswer();
			teachClassAnswer.setLessonId(subLessonId);
			teachClassAnswer.setTeachingClassId(teachingClassId);
			teachClassAnswerService.deleteByEntity(teachClassAnswer);

			List<LessonTask> lessonTaskList = commonService.findLessonTasksBySubLesson(subLesson);
			teachClassAnswerService.saveByLessonTasks(lessonTaskList,teachingClassId);
		}

		return success();
	}
}