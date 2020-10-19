/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.web;

import com.jeesite.common.entity.Page;
import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.web.BaseApiController;
import com.jeesite.framework.uitls.EntityUtils;
import com.langheng.modules.ed.entity.Chapter;
import com.langheng.modules.ed.entity.Lesson;
import com.langheng.modules.ed.entity.LessonTask;
import com.langheng.modules.ed.entity.Template;
import com.langheng.modules.ed.service.ChapterService;
import com.langheng.modules.ed.service.LessonService;
import com.langheng.modules.ed.service.LessonTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 小节任务（文件）Controller
 * @author xiaoxie
 * @version 2019-12-17
 */
@Api(description = "小节任务（文件）|教师端")
@RestController
@RequestMapping(value = "admin/lessonTask")
public class LessonTaskController extends BaseApiController {

	@Autowired
	private LessonService lessonService;
	@Autowired
	private ChapterService chapterService;
	@Autowired
	private LessonTaskService lessonTaskService;
	
	/**
	 * 获取小节任务（文件）数据
	 */
	@ModelAttribute
	public LessonTask get(String lessonTaskId, boolean isNewRecord) {
		return lessonTaskService.get(lessonTaskId, isNewRecord);
	}
	
 	@ApiOperation(value = "分页查询小节任务（文件）列表数据")
	@PostMapping(value = "findPage")
	public ResultBean<Page<LessonTask>> findPage(LessonTask lessonTask, HttpServletRequest request, HttpServletResponse response) {
		lessonTask.setPage(new Page<>(request, response));
		Page<LessonTask> page = lessonTaskService.findPage(lessonTask);
		return success(page);
	}


    @ApiOperation(value = "查询小节任务（文件）列表数据")
	@PostMapping(value = "findList")
	public ResultBean findList(LessonTask lessonTask) {
		List<LessonTask> list = lessonTaskService.findList(lessonTask);
		return success(list);
	}
	@ApiOperation(value = "查询模板小节任务（文件）列表数据")
	@PostMapping(value = "findAllList")
	public ResultBean findAllList(String templateId) {
		List<Lesson> list = new ArrayList<>();
		List<LessonTask> lessonTasks = new ArrayList<>();
		Chapter chapter = new Chapter();
		Template template = new Template();
		template.setTemplateId(templateId);
		chapter.setTemplate(template);
		List<Chapter> chapterList = chapterService.findList(chapter);
		if (chapterList.size()==0) {
			return success("该模板或课程无资源");
		}
		for (Chapter chapter1 : chapterList) {
			Lesson lesson = new Lesson();
			lesson.setChapter(chapter1);
			List<Lesson> lessonList = lessonService.findList(lesson);
			LessonTask lessonTask = new LessonTask();
			lessonTask.setLessonId_in(EntityUtils.getBaseEntityIds(lessonList));
			List<LessonTask> lessonTaskServiceList = lessonTaskService.findList(lessonTask);
			lessonTasks.addAll(lessonTaskServiceList);

			for (Lesson lesson1 : lessonList) {
				Lesson parentLesson = new Lesson();
				parentLesson.setParentLesson(lesson1);
				List<Lesson> parentLessonList = lessonService.findList(parentLesson);
				LessonTask lessonTaskc = new LessonTask();
				lessonTaskc.setLessonId_in(EntityUtils.getBaseEntityIds(parentLessonList));
				List<LessonTask> lessonTaskServiceListc = lessonTaskService.findList(lessonTaskc);
				lessonTasks.addAll(lessonTaskServiceListc);
			}
		}
		return success(lessonTasks);
	}


	@ApiOperation(value = "查询课程小节任务（文件）列表数据")
	@PostMapping(value = "chFindAllList")
	public ResultBean chFindAllList(Chapter chapter) {
		List<Lesson> list = new ArrayList<>();
		List<Chapter> chapterList = chapterService.findList(chapter);
		if (chapterList.size()==0) {
			return success("该模板或课程无资源");
		}
		for (Chapter chapter1 : chapterList) {
			Lesson lesson = new Lesson();
			lesson.setChapter(chapter1);
			List<Lesson> lessonList = lessonService.findList(lesson);
			list.addAll(lessonList);
			for (Lesson lesson1 : lessonList) {
				Lesson parentLesson = new Lesson();
				parentLesson.setParentLesson(lesson1);
				List<Lesson> parentLessonList = lessonService.findList(parentLesson);
				list.addAll(parentLessonList);
			}
		}

		LessonTask lessonTask = new LessonTask();
		lessonTask.setLessonId_in(EntityUtils.getBaseEntityIds(list));
		List<LessonTask> lessonTaskServiceList = lessonTaskService.findList(lessonTask);
		return success(lessonTaskServiceList);
	}

	@ApiOperation(value = "查询课程小节任务（文件）分类数量")
	@PostMapping(value = "chFindCount")
	public ResultBean chFindCount(Chapter chapter) {
		List<Lesson> list = new ArrayList<>();
		List<Chapter> chapterList = chapterService.findList(chapter);
		if (chapterList.size()==0) {
			return success("该模板或课程无资源");
		}
		for (Chapter chapter1 : chapterList) {
			Lesson lesson = new Lesson();
			lesson.setChapter(chapter1);
			List<Lesson> lessonList = lessonService.findList(lesson);
			list.addAll(lessonList);
			for (Lesson lesson1 : lessonList) {
				Lesson parentLesson = new Lesson();
				parentLesson.setParentLesson(lesson1);
				List<Lesson> parentLessonList = lessonService.findList(parentLesson);
				list.addAll(parentLessonList);
			}
		}
		LessonTask lessonTask = new LessonTask();
		lessonTask.setLessonId_in(EntityUtils.getBaseEntityIds(list));
		Map groupCount = lessonTaskService.findGroupCount(lessonTask);
		return success(groupCount);
	}

	@ApiOperation(value = "删除小节任务")
	@PostMapping(value = "delete")
	public ResultBean delete(LessonTask lessonTask) {
		lessonTaskService.delete(lessonTask);
		return success();
	}

	@ApiOperation(value = "修改(添加，删除答案)")
	@PostMapping(value = "update")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "lessonTaskId", value = "任务id", required = true),
			@ApiImplicitParam(name = "taskCorrectValue", value = "主观题答案id", required = true),
	})
	public ResultBean update(LessonTask lessonTask) {
		if (!lessonTask.getTaskCorrectValue().equals(null)) {
			lessonTask.setTaskCorrectStatus("1");
		}else {
			lessonTask.setTaskCorrectStatus("0");
			lessonTask.setTaskCorrectValue("");
		}
		lessonTaskService.save(lessonTask);
		return success();
	}


}