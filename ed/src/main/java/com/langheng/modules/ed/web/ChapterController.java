/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.web;

import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.enumn.SysYesNoEnum;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.ed.entity.Chapter;
import com.langheng.modules.ed.entity.Course;
import com.langheng.modules.ed.enumn.ChapterState;
import com.langheng.modules.ed.service.ChapterService;
import com.langheng.modules.ed.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 课程章节Controller
 * @author xiaoxie
 * @version 2019-12-17
 */
@Api(description = "课程章节管理 | 教师端")
@RestController
@RequestMapping(value = "admin/chapter")
public class ChapterController extends BaseApiController {

	@Autowired
	private ChapterService chapterService;
	@Autowired
	private CourseService courseService;
	
	/**
	 * 获取课程章节数据
	 */
	@ModelAttribute
	public Chapter get(String chapterId, boolean isNewRecord) {
		return chapterService.get(chapterId, isNewRecord);
	}
	
 	@ApiOperation(value = "分页查询课程的章节列表数据| 课程的")
	@PostMapping(value = "findCourseChapterList")
	public ResultBean<List<Chapter>> findCourseChapterList(Chapter chapter, HttpServletRequest request, HttpServletResponse response) {
		if (chapter.getCourse() == null ){
			return error("关联的课程不能为空！");
		}
		List<Chapter> chapterList = chapterService.findList(chapter);
		return success(chapterList);
	}

	@ApiOperation(value = "保存课程章节| 课程的")
	@PostMapping(value = "saveCourseChapter")
	public ResultBean saveCourseChapter(@Validated Chapter chapter) {
		if (chapter.getCourse() == null ){
			return error("关联的课程不能为空！");
		}
		chapterService.save(chapter);
		chapter = chapterService.get(chapter);
		return success(chapter);
	}

	@ApiOperation(value = "删除章节")
	@PostMapping(value = "delete")
	public ResultBean delete(Chapter chapter) {
		chapterService.delete(chapter);
		return success();
	}

	@ApiOperation(value = "调整课程的章节顺序")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "courseId", value = "课程id", required = true),
			@ApiImplicitParam(name = "chapterId", value = "调整的章节id"),
			@ApiImplicitParam(name = "chapterNum", value = "调整后的章节序号"),
	})
	@PostMapping("adjustCourseChapterNum")
	public ResultBean adjustCourseChapterNum(@RequestParam String courseId,
											 @RequestParam String chapterId,
											 @RequestParam Integer chapterNum){

		Chapter chapter = chapterService.get(chapterId);
		Course course = courseService.get(courseId);

		if (chapter == null
				|| course == null){
			return error("禁止操作！");
		}

		chapter.setCourse(course);
		if (chapter.getChapterNum() < chapterNum){
			chapter.setIsShiftDown(SysYesNoEnum.YES.value());
		}
		chapter.setChapterNum(chapterNum);
		chapterService.save(chapter);

		chapterService.adjustChapterNumByCourse(course);

		return success();
	}

	@ApiIgnore
	@ApiOperation(value = "分页查询模板的章节列表数据| 模板的")
	@PostMapping(value = "findTemplateChapterList")
	public ResultBean<List<Chapter>>  findTemplateChapterList(Chapter chapter, HttpServletRequest request, HttpServletResponse response) {
		if (chapter.getTemplate() == null ){
			return error("关联的模板不能为空！");
		}
		List<Chapter> chapterList = chapterService.findList(chapter);
		return success(chapterList);
	}


	@ApiIgnore
	@ApiOperation(value = "保存模板章节 | 模板的")
	@PostMapping(value = "saveTemplateChapter")
	public ResultBean saveTemplateChapter(@Validated Chapter chapter) {
		if (chapter.getTemplate() == null){
			return error("关联的模板不能为空！");
		}
		if (chapter.getIsNewRecord()){
			chapter.setState(ChapterState.HAD_NOT_STARTED.value());

			Chapter chapterCriteria = new Chapter();
			chapterCriteria.setTemplate(chapter.getTemplate());
			long count = chapterService.findCount(chapterCriteria);
			chapter.setChapterNum((int)++count);
		}
		chapterService.save(chapter);
		return success(chapter);
	}


	@ApiIgnore
	@ApiOperation(value = "开启课程章节")
	@PostMapping(value = "enable")
	public ResultBean enable(Chapter chapter){
		chapter.setState(ChapterState.PROGRESSING.value());
		chapterService.save(chapter);
		return success();
	}




}