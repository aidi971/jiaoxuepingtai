/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.web;


import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.ed.entity.LessonTask;
import com.langheng.modules.ed.enumn.BizType;
import com.langheng.modules.ed.service.ChapterService;
import com.langheng.modules.ed.service.LessonService;
import com.langheng.modules.ed.service.LessonTaskService;
import com.langheng.modules.ed.service.TeachingClassService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 课堂任务推送Controller
 * @author xiaoxie
 * @version 2019-12-17
 */
@Api(description = "课堂任务批量推送，定时推送 | 教师端")
@RestController
@RequestMapping(value = "admin/teachClassTask")
public class TeachClassBatchTaskController extends BaseApiController {
	@Autowired
	private TeachingClassService teachingClassService;
	@Autowired
	TeachClassTaskController teachClassTaskController;
	@Autowired
	private ChapterService chapterService;
	@Autowired
	private LessonService lessonService;
	@Autowired
	private LessonTaskService lessonTaskService;

	@ApiOperation(value = "批量推送")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
			@ApiImplicitParam(name = "ids", value = "章，节，小节，或者资源id。多个则用逗号隔开", required = true),
			@ApiImplicitParam(name = "types", value = "章:chapter,节:lesson,小节:subLesson,资源:lessonTask", required = true),
	})
	@PostMapping(value = "batchPush")
	@Transactional
	public ResultBean batchPush(@RequestParam String ids,
									   @RequestParam String types,
									   @RequestParam String teachingClassId) {

		String[] idArr = ids.split(",");
		String [] typeArr = types.split(",");
		if (idArr.length != typeArr.length){
			return error("参数错误!");
		}

		for (int index=0; index< idArr.length; index ++){
			String id = idArr[index];
			String type = typeArr[index];
			if (BizType.CHAPTER.value().equals(type)){
				// 推送的是章
				teachClassTaskController.pushChapter(id,teachingClassId);
			}else if (BizType.LESSON.value().equals(type)){
				// 推送的是节
				teachClassTaskController.pushLesson(id,teachingClassId);
			}else if (BizType.SUB_LESSON.value().equals(type)){
				// 推送的是小节
				teachClassTaskController.pushSubLesson(id,teachingClassId);
			}else if (BizType.LESSON_TASK.value().equals(type)){
				LessonTask lessonTask = lessonTaskService.get(id);
				// 推送的是资源
				teachClassTaskController.pushLessonTask(lessonTask.getLessonId(),id,teachingClassId);
			}
		}

		return success();
	}


	@ApiOperation(value = "批量撤回")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
			@ApiImplicitParam(name = "ids", value = "章，节，小节，或者资源id。多个则用逗号隔开", required = true),
			@ApiImplicitParam(name = "types", value = "章:chapter,节:lesson,小节:subLesson,资源:lessonTask", required = true),
	})
	@PostMapping(value = "batchRecall")
	@Transactional
	public ResultBean batchRecall(@RequestParam String ids,
								@RequestParam String types,
								@RequestParam String teachingClassId) {

		String[] idArr = ids.split(",");
		String [] typeArr = types.split(",");
		if (idArr.length != typeArr.length){
			return error("参数错误!");
		}

		for (int index=0; index< idArr.length; index ++){
			String id = idArr[index];
			String type = typeArr[index];
			if (BizType.CHAPTER.value().equals(type)){
				// 撤回的是章
				teachClassTaskController.recallChapter(id,teachingClassId);
			}else if (BizType.LESSON.value().equals(type)){
				// 撤回的是节
				teachClassTaskController.recallLesson(id,teachingClassId);
			}else if (BizType.SUB_LESSON.value().equals(type)){
				// 撤回的是小节
				teachClassTaskController.recallSubLesson(id,teachingClassId);
			}else if (BizType.LESSON_TASK.value().equals(type)){
				// 撤回的是资源
				teachClassTaskController.recallLessonTask(id,teachingClassId);
			}
		}

		return success();
	}


	@ApiOperation(value = "定时推送 ids只能是关联资源的上一级id，或者资源id")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
			@ApiImplicitParam(name = "ids", value = "章，节，小节，或者资源id。多个则用逗号隔开", required = true),
			@ApiImplicitParam(name = "types", value = "节:lesson,小节:subLesson,资源:lessonTask", required = true),
			@ApiImplicitParam(name = "pullTime", value = "推送的时间", required = true),
	})
	@PostMapping(value = "timingPull")
	@Transactional
	public ResultBean timingPull(@RequestParam String ids,
								   @RequestParam String types,
								   @RequestParam Date pullTime,
								   @RequestParam String teachingClassId) {
		if (pullTime.before(DateUtils.addMinutes(new Date(),10))){
			return error("推送的时间最早为10分钟后");
		}

		String[] idArr = ids.split(",");
		String [] typeArr = types.split(",");
		if (idArr.length != typeArr.length){
			return error("参数错误!");
		}
		for (int index=0; index< idArr.length; index ++){
			String id = idArr[index];
			String type = typeArr[index];
			if (BizType.LESSON.value().equals(type)){
				// 推送的是节
			}else if (BizType.SUB_LESSON.value().equals(type)){
				// 推送的是小节
			}else if (BizType.LESSON_TASK.value().equals(type)){
				// 推送的是资源
			}
		}


		return success();
	}

	@ApiOperation(value = "定时撤回   ids只能是关联资源的上一级id，或者资源id")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
			@ApiImplicitParam(name = "ids", value = "章，节，小节，或者资源id。多个则用逗号隔开", required = true),
			@ApiImplicitParam(name = "types", value = "节:lesson,小节:subLesson,资源:lessonTask", required = true),
			@ApiImplicitParam(name = "recallTime", value = "撤回的时间", required = true),
	})
	@PostMapping(value = "timingRecall")
	@Transactional
	public ResultBean timingRecall(@RequestParam String ids,
								  @RequestParam String types,
								   @RequestParam Date recallTime,
								  @RequestParam String teachingClassId) {
		if (recallTime.before(DateUtils.addMinutes(new Date(),10))){
			return error("撤回的时间最早为10分钟后");
		}

		String[] idArr = ids.split(",");
		String [] typeArr = types.split(",");
		if (idArr.length != typeArr.length){
			return error("参数错误!");
		}
		for (int index=0; index< idArr.length; index ++){
			String id = idArr[index];
			String type = typeArr[index];
			if (BizType.LESSON.value().equals(type)){
				// 推送的是节
			}else if (BizType.SUB_LESSON.value().equals(type)){
				// 推送的是小节
			}else if (BizType.LESSON_TASK.value().equals(type)){
				// 推送的是资源
			}
		}
		return success();
	}
}