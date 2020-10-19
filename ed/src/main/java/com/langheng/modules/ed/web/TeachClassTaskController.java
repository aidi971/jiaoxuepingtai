/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.web;


import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseApiController;
import com.jeesite.framework.uitls.EntityUtils;
import com.langheng.modules.ed.entity.*;
import com.langheng.modules.ed.enumn.LessonType;
import com.langheng.modules.ed.service.*;
import com.langheng.modules.ed.util.TeachClassMsgUtils;
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

import java.util.List;

/**
 * 课堂任务推送Controller
 * @author xiaoxie
 * @version 2019-12-17
 */
@Api(description = "课堂任务推送 | 教师端")
@RestController
@RequestMapping(value = "admin/teachClassTask")
public class TeachClassTaskController extends BaseApiController {
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
	private TeachClassLessonTaskService teachClassLessonTaskService;
	@Autowired
	private LessonTaskService lessonTaskService;
	@Autowired
	private TeachClassLessonService teachClassLessonService;
	@Autowired
	private TeachClassChapterService teachClassChapterService;
	@Autowired
	private UserChapterService userChapterService;
	@Autowired
	private UserLessonService userLessonService;
	@Autowired
	private UserTaskService userTaskService;

	@ApiOperation(value = "课程所有结构，显示所有章节-节-小节-资源")
	@PostMapping(value = "lessonTaskStruct")
	public ResultBean<List<Chapter>>  lessonTaskStruct(@RequestParam String teachingClassId) {
		TeachingClass teachingClass = teachingClassService.get(teachingClassId);
		Course course = courseService.get(teachingClass.getCourse());
		return success(commonService.lessonTaskStruct(course,teachingClassId),"获取成功！",course.getTotalLessonNum());
	}

	@ApiOperation(value = "课程已经推送结构，显示所有章节-节-小节-资源")
	@PostMapping(value = "HadPushLessonTaskStruct")
	public ResultBean<List<Chapter>>  HadPushLessonTaskStruct(@RequestParam String teachingClassId) {
		TeachingClass teachingClass = teachingClassService.get(teachingClassId);
		Course course = courseService.get(teachingClass.getCourse());
		return success(commonService.HadPushLessonTaskStruct(course,teachingClassId),"获取成功！",course.getHadPushLessonNum());
	}

	@ApiOperation(value = "(拖拽)推送整个章")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "chapterId", value = "章节id", required = true),
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
	})
	@PostMapping(value = "pushChapter")
	public ResultBean pushChapter(@RequestParam String teachingClassId,
								  @RequestParam String chapterId) {
		Chapter chapter = chapterService.get(chapterId);
		if (chapter != null && StringUtils.isNotBlank(chapter.getChapterId())){
			List<Lesson> lessonList = commonService.findLessonsByChapter(chapter);
			if (lessonList.isEmpty()){
				return error("该章内容为空，请完善后再推送");
			}
			List<LessonTask> lessonTaskList = commonService.findLessonTasksByChapter(chapter);
			teachClassLessonTaskService.saveByLessonTasks(lessonTaskList,teachingClassId);
			teachClassLessonService.saveByLessonList(lessonList,teachingClassId);


			TeachClassChapter tcc = new TeachClassChapter();
			tcc.setChapterId(chapterId);
			tcc.setTeachingClassId(teachingClassId);
			teachClassChapterService.flushSave(tcc);

			// 发送学习任务通知
			TeachClassMsgUtils.pushTeachClassMsg(teachingClassId);
			// 刷新用户任务
			userChapterService.flushUserTask(chapter,teachingClassId);
		}
		return success();
	}

	@ApiOperation(value = "撤回整个章")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "chapterId", value = "章节id", required = true),
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
	})
	@PostMapping(value = "recallChapter")
	public ResultBean recallChapter(@RequestParam String chapterId,
									@RequestParam String teachingClassId) {
		Chapter chapter = chapterService.get(chapterId);
		if (chapter != null && StringUtils.isNotBlank(chapter.getChapterId())){
			List<Lesson> lessonList = commonService.findLessonsByChapter(chapter);
			List<LessonTask> lessonTaskList = commonService.findLessonTasksByChapter(chapter);
			teachClassLessonTaskService.deleteByLessonTasks(lessonTaskList,teachingClassId);
			teachClassLessonService.deleteByLessonList(lessonList, teachingClassId);

			TeachClassChapter tcc = new TeachClassChapter();
			tcc.setChapterId(chapterId);
			tcc.setTeachingClassId(teachingClassId);
			teachClassChapterService.deleteByEntity(tcc);

			// 清空小节，用户任务
			UserChapter userChapter = new UserChapter();
			userChapter.setChapterId(chapterId);
			userChapterService.deleteByEntity(userChapter);

			if (!lessonList.isEmpty()){
				UserLesson subUserLesson = new UserLesson();
				subUserLesson.setLessonId_id(EntityUtils.getBaseEntityIds(lessonList));
				userLessonService.deleteByEntity(subUserLesson);
			}

			UserTask userTask = new UserTask();
			userTask.setChapterId(chapterId);
			userTask.setTeachingClassId(teachingClassId);
			userTaskService.phyDeleteByEntity(userTask);
		}
		return success();
	}

	@ApiOperation(value = "(拖拽)推送整节")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "lessonId", value = "节id", required = true),
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
	})
	@PostMapping(value = "pushLesson")
	@Transactional(readOnly = false)
	public ResultBean pushLesson(@RequestParam String lessonId,
								 @RequestParam String teachingClassId) {
		Lesson lesson = lessonService.get(lessonId);
		if (lesson != null && StringUtils.isNotBlank(lesson.getLessonId())){
			List<LessonTask> lessonTaskList = commonService.findLessonTasksByLesson(lesson);
			teachClassLessonTaskService.saveByLessonTasks(lessonTaskList,teachingClassId);

			// 推送章
			if (lesson.getChapter()!= null &&StringUtils.isNotBlank(lesson.getChapter().getChapterId())){
				TeachClassChapter tcc = new TeachClassChapter();
				tcc.setTeachingClassId(teachingClassId);
				tcc.setChapterId(lesson.getChapter().getChapterId());
				teachClassChapterService.flushSave(tcc);
			}

			// 推送节
			TeachClassLesson tcl = new TeachClassLesson();
			tcl.setTeachingClassId(teachingClassId);
			tcl.setLessonId(lesson.getLessonId());
			teachClassLessonService.flushSave(tcl);

			// 并推送旗下的小节
			Lesson subLessonCriteria = new Lesson();
			subLessonCriteria.setParentLesson(lesson);
			List<Lesson> subLessonList = lessonService.findList(subLessonCriteria);
			teachClassLessonService.saveByLessonList(subLessonList,teachingClassId);

			// 发送学习任务通知
			TeachClassMsgUtils.pushTeachClassMsg(teachingClassId);
            // 刷新用户任务
            userLessonService.flushUserTask(teachingClassId,lesson.getId());
		}
		return success();
	}


	@ApiOperation(value = "撤回整节")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "lessonId", value = "节id", required = true),
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
	})
	@PostMapping(value = "recallLesson")
	public ResultBean recallLesson(@RequestParam String lessonId,
								   @RequestParam String teachingClassId) {
		Lesson lesson = lessonService.get(lessonId);
		if (lesson != null && StringUtils.isNotBlank(lesson.getLessonId())){
			List<LessonTask> lessonTaskList = commonService.findLessonTasksByLesson(lesson);
			teachClassLessonTaskService.deleteByLessonTasks(lessonTaskList,teachingClassId);

			TeachClassLesson teachClassLesson = new TeachClassLesson();
			teachClassLesson.setLessonId(lessonId);
			teachClassLesson.setTeachingClassId(teachingClassId);
			teachClassLessonService.deleteByEntity(teachClassLesson);

			// 并删除旗下的小节
			Lesson subLessonCriteria = new Lesson();
			subLessonCriteria.setParentLesson(lesson);
			List<Lesson> subLessonList = lessonService.findList(subLessonCriteria);
			teachClassLessonService.deleteByLessonList(subLessonList,teachingClassId);

			// 清空小节，用户任务
			UserLesson userLesson = new UserLesson();
			userLesson.setLessonId(lessonId);
			userLessonService.deleteByEntity(userLesson);

			if (!subLessonList.isEmpty()){
				UserLesson subUserLesson = new UserLesson();
				subUserLesson.setLessonId_id(EntityUtils.getBaseEntityIds(subLessonList));
				userLessonService.deleteByEntity(subUserLesson);
			}

			UserTask userTask = new UserTask();
			userTask.setParentLessonId(lessonId);
			userTask.setTeachingClassId(teachingClassId);
			userTaskService.phyDeleteByEntity(userTask);
		}
		return success();
	}



	@ApiOperation(value = "(拖拽)推送整小节")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "subLessonId", value = "小节id", required = true),
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
	})
	@PostMapping(value = "pushSubLesson")
	public ResultBean pushSubLesson(@RequestParam String subLessonId,
									@RequestParam String teachingClassId) {
		Lesson subLesson = lessonService.get(subLessonId);
		if (subLesson != null && StringUtils.isNotBlank(subLesson.getLessonId())){
			List<LessonTask> lessonTaskList = commonService.findLessonTasksBySubLesson(subLesson);
			teachClassLessonTaskService.saveByLessonTasks(lessonTaskList,teachingClassId);

			TeachClassLesson tcl = new TeachClassLesson();
			tcl.setTeachingClassId(teachingClassId);
			tcl.setLessonId(subLessonId);
			teachClassLessonService.flushSave(tcl);

			// 如果是小节，还需推送上级的节 还有章
			if (LessonType.SUB_LESSON.value().equals(subLesson.getLessonType())){
				Lesson parentLesson = lessonService.get(subLesson.getParentLesson());
				TeachClassLesson parentTcl = new TeachClassLesson();
				parentTcl.setTeachingClassId(teachingClassId);
				parentTcl.setLessonId(parentLesson.getLessonId());
				teachClassLessonService.flushSave(parentTcl);

				// 推送章
				if (parentLesson.getChapter()!= null &&StringUtils.isNotBlank(parentLesson.getChapter().getChapterId())){
					TeachClassChapter tcc = new TeachClassChapter();
					tcc.setTeachingClassId(teachingClassId);
					tcc.setChapterId(parentLesson.getChapter().getChapterId());
					teachClassChapterService.flushSave(tcc);
				}
			}

			// 发送学习任务通知
			TeachClassMsgUtils.pushTeachClassMsg(teachingClassId);
            // 刷新用户任务
			userLessonService.flushUserTask(teachingClassId,subLesson.getId());
		}
		return success();
	}


	@ApiOperation(value = "撤回整小节")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "subLessonId", value = "小节id", required = true),
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
	})
	@PostMapping(value = "recallSubLesson")
	public ResultBean recallSubLesson(@RequestParam String subLessonId,
									  @RequestParam String teachingClassId) {
		Lesson subLesson = lessonService.get(subLessonId);
		if (subLesson != null && StringUtils.isNotBlank(subLesson.getLessonId())){
			List<LessonTask> lessonTaskList = commonService.findLessonTasksBySubLesson(subLesson);
			if (!lessonTaskList.isEmpty()){
				teachClassLessonTaskService.deleteByLessonTasks(lessonTaskList,teachingClassId);
			}

			TeachClassLesson teachClassLesson = new TeachClassLesson();
			teachClassLesson.setLessonId(subLessonId);
			teachClassLesson.setTeachingClassId(teachingClassId);
			teachClassLessonService.deleteByEntity(teachClassLesson);


			// 清空小节，用户任务
			UserLesson userLesson = new UserLesson();
			userLesson.setLessonId(subLessonId);
			userLessonService.deleteByEntity(userLesson);

			UserTask userTask = new UserTask();
			userTask.setLessonId(subLessonId);
			userTask.setTeachingClassId(teachingClassId);
			userTaskService.phyDeleteByEntity(userTask);

		}
		return success();
	}

	@ApiOperation(value = "(拖拽)推送整一个或多个资源")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "lessonId", value = "关联的节或小节id", required = true),
			@ApiImplicitParam(name = "lessonTaskIds", value = "资源id，用逗号分隔", required = true),
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
	})
	@PostMapping(value = "pushLessonTask")
	public ResultBean pushLessonTask(@RequestParam String lessonId,
									 @RequestParam String lessonTaskIds,
									 @RequestParam String teachingClassId) {
		String [] lessonTaskIdArr = lessonTaskIds.split(",");
		if (lessonTaskIdArr.length > 0){
			LessonTask lessonTask = new LessonTask();
			lessonTask.setId_in(lessonTaskIdArr);
			List<LessonTask> lessonTaskList = lessonTaskService.findList(lessonTask);
			teachClassLessonTaskService.saveByLessonTasks(lessonTaskList,teachingClassId);

			String chapterId = StringUtils.EMPTY;

			Lesson lesson = lessonService.get(lessonId);
			TeachClassLesson tcl = new TeachClassLesson();
			tcl.setTeachingClassId(teachingClassId);
			tcl.setLessonId(lesson.getLessonId());
			teachClassLessonService.flushSave(tcl);
			// 如果是小节，还需推送上级的节
			if (LessonType.SUB_LESSON.value().equals(lesson.getLessonType())){
				Lesson parentLesson = lessonService.get(lesson.getParentLesson());
				TeachClassLesson parentTcl = new TeachClassLesson();
				parentTcl.setTeachingClassId(teachingClassId);
				parentTcl.setLessonId(parentLesson.getLessonId());
				teachClassLessonService.flushSave(parentTcl);

				if (parentLesson.getChapter()!= null &&StringUtils.isNotBlank(parentLesson.getChapter().getChapterId())){
					chapterId = parentLesson.getChapter().getChapterId();
				}
			}else if (LessonType.LESSON.value().equals(lesson.getLessonType())){
				if (lesson.getChapter()!= null &&StringUtils.isNotBlank(lesson.getChapter().getChapterId())){
					chapterId = lesson.getChapter().getChapterId();
				}
			}

			// 推送章
			if (StringUtils.isNotBlank(chapterId)){
				TeachClassChapter tcc = new TeachClassChapter();
				tcc.setTeachingClassId(teachingClassId);
				tcc.setChapterId(chapterId);
				teachClassChapterService.flushSave(tcc);
			}

			// 发送学习任务通知
			TeachClassMsgUtils.pushTeachClassMsg(teachingClassId);
            // 刷新用户任务
            userLessonService.flushUserTask(teachingClassId,lesson.getId());
		}
		return success();
	}


	@ApiOperation(value = "撤回整一个或多个资源")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "lessonId", value = "关联的节或小节id", required = true),
			@ApiImplicitParam(name = "lessonTaskIds", value = "资源id，用逗号分隔", required = true),
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
	})
	@PostMapping(value = "recallLessonTask")
	public ResultBean recallLessonTask(@RequestParam String lessonTaskIds,
									 @RequestParam String teachingClassId) {
		String [] lessonTaskIdArr = lessonTaskIds.split(",");
		if (lessonTaskIdArr.length > 0){
			TeachClassLessonTask tclt = new TeachClassLessonTask();
			tclt.setLessonTaskId_in(lessonTaskIdArr);
			tclt.setTeachingClassId(teachingClassId);
			teachClassLessonTaskService.deleteByEntity(tclt);

			// 物理删除学生任务
			UserTask userTask = new UserTask();
			userTask.setLessonTaskId_in(lessonTaskIdArr);
			userTask.setTeachingClassId(teachingClassId);
			userTaskService.phyDeleteByEntity(userTask);
		}
		return success();
	}
}