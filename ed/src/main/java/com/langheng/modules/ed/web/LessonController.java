/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.web;


import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.enumn.SysYesNoEnum;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.ed.entity.Chapter;
import com.langheng.modules.ed.entity.Lesson;
import com.langheng.modules.ed.enumn.LessonState;
import com.langheng.modules.ed.enumn.LessonType;
import com.langheng.modules.ed.service.ChapterService;
import com.langheng.modules.ed.service.LessonService;
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
@Api(description = "课程‘节’管理 | 教师端")
@RestController
@RequestMapping(value = "admin/lesson")
public class LessonController extends BaseApiController {

	@Autowired
	private LessonService lessonService;

	@Autowired
	private ChapterService chapterService;
	
	/**
	 * 获取课程‘节’或者小节数据
	 */
	@ModelAttribute
	public Lesson get(String lessonId, boolean isNewRecord) {
		return lessonService.get(lessonId, isNewRecord);
	}

    @ApiOperation(value = "查询课程‘节’列表数据")
	@PostMapping(value = "findLessonList")
	public ResultBean<List<Lesson>> findLessonList(@RequestParam String chapterId) {
		Lesson lesson = new Lesson();
		lesson.setChapter(new Chapter(chapterId));
		List<Lesson> lessonList = lessonService.findList(lesson);
		return success(lessonList);
	}

	@ApiOperation(value = "保存课程‘节’| 记得传chapter.chapterId")
	@PostMapping(value = "saveLesson")
	public ResultBean saveLesson(@Validated Lesson lesson) {
		if (lesson.getChapter() == null){
			return error("关联章节id不能为空");
		}
		if (lesson.getIsNewRecord()){
			lesson.setLessonType(LessonType.LESSON.value());
		}
		lessonService.save(lesson);
		lesson = lessonService.get(lesson);
		return success(lesson);
	}

	@ApiOperation(value = "调整课程的‘节’顺序")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "chapterId", value = "章节id", required = true),
			@ApiImplicitParam(name = "lessonId", value = "调整的‘节’id"),
			@ApiImplicitParam(name = "lessonNum", value = "调整后的‘节’序号"),
	})
	@PostMapping("adjustChapterLessonNum")
	public ResultBean adjustCourseChapterNum(@RequestParam String chapterId,
											 @RequestParam String lessonId,
											 @RequestParam Integer lessonNum){

		Chapter chapter = chapterService.get(chapterId);
		Lesson lesson = lessonService.get(lessonId);
		Chapter oldChapter = chapterService.get(lesson.getChapter());
		if (chapter == null
				|| lesson == null
				|| oldChapter == null){
			return error("禁止操作！");
		}
		if (!LessonType.LESSON.value().equals(lesson.getLessonType())){
			return error("禁止操作！");
		}
		if (lesson.getLessonNum() < lessonNum){
			lesson.setIsShiftDown(SysYesNoEnum.YES.value());
		}
		lesson.setChapter(chapter);
		lesson.setLessonNum(lessonNum);
		lesson.setChapter(chapter);
		lessonService.save(lesson);
		lessonService.adjustLessonNumByChapter(chapter);
		// 如果是跨章节拖动
		if (!oldChapter.getId().equals(chapter.getId())){
			lessonService.adjustLessonNumByChapter(oldChapter);
		}
		return success();
	}


	@ApiOperation(value = "开启节")
	@PostMapping(value = "enable")
	public ResultBean enable(Lesson lesson){
		lesson.setState(LessonState.PROGRESSING.value());
		lessonService.save(lesson);
		return success();
	}

	@ApiOperation(value = "删除课程‘节’")
	@PostMapping(value = "delete")
	public ResultBean delete(Lesson lesson) {
		lessonService.delete(lesson);
		return success();
	}


}