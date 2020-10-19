/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.web;

import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.enumn.SysYesNoEnum;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.ed.entity.Lesson;
import com.langheng.modules.ed.entity.LessonTask;
import com.langheng.modules.ed.enumn.LessonState;
import com.langheng.modules.ed.enumn.LessonType;
import com.langheng.modules.ed.service.LessonService;
import com.langheng.modules.ed.service.LessonTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 课程‘节’或者小节Controller
 * @author xiaoxie
 * @version 2019-12-17
 */
@Api(description = "课程小节管理 | 教师端")
@RestController
@RequestMapping(value = "admin/subLesson")
public class SubLessonController extends BaseApiController {

	@Autowired
	private LessonService lessonService;
	@Autowired
	private LessonTaskService lessonTaskService;

	/**
	 * 获取课程‘节’或者小节数据
	 */
	@ModelAttribute
	public Lesson get(String lessonId, boolean isNewRecord) {
		return lessonService.get(lessonId, isNewRecord);
	}
	
	@ApiOperation(value = "查询课程小节列表数据")
	@PostMapping(value = "findSubLessonList")
	public ResultBean<List<Lesson>> findSubLessonList(@RequestParam String parentLessonId) {
		Lesson lesson = new Lesson();
		lesson.setParentLesson(new Lesson(parentLessonId));
		List<Lesson> lessonList = lessonService.findList(lesson);
		return success(lessonList);
	}

	@ApiOperation(value = "保存课程小节 | 记得传parentLesson.lessonId")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "isForce", value = "是否强添加，关联的节存在资源的情况（0否，1是）", required = false),
	})
	@PostMapping(value = "saveSubLesson")
	public ResultBean saveSubLesson(@Validated Lesson lesson,String isForce) {
		if (lesson.getParentLesson() == null
				&& StringUtils.isNotBlank(lesson.getParentLesson().getId())){
			return error("关联‘节’id不能为空");
		}
		if (lesson.getIsNewRecord()){
			LessonTask lessonTask = new LessonTask();
			lessonTask.setLessonId(lesson.getParentLesson().getLessonId());
			if (lessonTaskService.findCount(lessonTask) != 0){
				if (SysYesNoEnum.YES.value().equals(isForce)){
					lessonTaskService.deleteByEntity(lessonTask);
				}else {
					return error("该节下已有的资源将会被删除，是否添加小节？");
				}
			}

			lesson.setLessonType(LessonType.SUB_LESSON.value());
		}
		lessonService.save(lesson);
		lesson = lessonService.get(lesson);
		return success(lesson);
	}

	@ApiOperation(value = "调整课程的小节的顺序")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "parentLessonId", value = "关联的‘节’id", required = true),
			@ApiImplicitParam(name = "subLessonId", value = "被调整的小节id"),
			@ApiImplicitParam(name = "lessonNum", value = "调整后的小节序号"),
	})
	@PostMapping("adjustLessonSubLessonNum")
	public ResultBean adjustLessonSubLessonNum(@RequestParam String parentLessonId,
											 @RequestParam String subLessonId,
											 @RequestParam Integer lessonNum){

		Lesson parentLesson = lessonService.get(parentLessonId);
		Lesson subLesson = lessonService.get(subLessonId);
		Lesson oldParentLesson = lessonService.get(subLesson.getParentLesson());
		if (parentLesson == null
				|| parentLesson == null
				|| oldParentLesson == null){
			return error("禁止操作！");
		}
		if (!LessonType.SUB_LESSON.value().equals(subLesson.getLessonType())){
			return error("禁止操作！");
		}

		if (subLesson.getLessonNum() < lessonNum){
			subLesson.setIsShiftDown(SysYesNoEnum.YES.value());
		}
		subLesson.setParentLesson(parentLesson);
		subLesson.setLessonNum(lessonNum);
		subLesson.setParentLesson(parentLesson);
		lessonService.save(subLesson);
		lessonService.adjustLessonNumByParentLesson(parentLesson);
		// 如果是跨章节拖动
		if (!oldParentLesson.getId().equals(parentLesson.getId())){
			lessonService.adjustLessonNumByParentLesson(oldParentLesson);
		}
		return success();
	}


	@ApiOperation(value = "开启小节")
	@PostMapping(value = "enable")
	public ResultBean enable(Lesson lesson){
		lesson.setState(LessonState.PROGRESSING.value());
		lessonService.save(lesson);
		return success();
	}

	@ApiOperation(value = "删除课程小节")
	@PostMapping(value = "delete")
	public ResultBean delete(Lesson lesson) {
		lessonService.delete(lesson);
		return success();
	}


}