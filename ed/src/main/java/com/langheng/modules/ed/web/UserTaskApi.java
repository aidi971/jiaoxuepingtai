/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.web;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.entity.Page;
import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.base.utils.BaseUserUtils;
import com.langheng.modules.ed.entity.*;
import com.langheng.modules.ed.enumn.UserTaskStatue;
import com.langheng.modules.ed.service.*;
import com.langheng.modules.ed.util.CommonUtils;
import com.langheng.modules.ed.util.TeachingClassUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
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
public class UserTaskApi extends BaseApiController {

	@Autowired
	private UserTaskService userTaskService;
	@Autowired
	private UserChapterService userChapterService;
	@Autowired
	private UserLessonService userLessonService;
	@Autowired
	private ChapterService chapterService;
	@Autowired
	private LessonService lessonService;
	@Autowired
	private LessonTaskService lessonTaskService;
	@Autowired
	private TeachingClassService teachingClassService;
	@Autowired
	private UserLessonRecordService userLessonRecordService;
	@Autowired
	private CommonService commonService;

	@ApiOperation(value = "根据lessonTaskId获取userTask")
	@PostMapping(value = "getUserTask")
	public ResultBean getUserTask(@RequestParam String lessonTaskId,
								  @RequestParam String userId){
		UserTask userTask = new UserTask();
		userTask.setLessonTaskId(lessonTaskId);
		userTask.setUserId(userId);

		List<UserTask> userTaskList = userTaskService.findList(userTask);
		if (!userTaskList.isEmpty()){
			return success(userTaskList.get(0));
		}
		return success();
	}


	@ApiOperation(value = "获取已推送的节或小节结构")
	@PostMapping(value = "findHadPushLessonVo")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
			@ApiImplicitParam(name = "lessonTaskType", value = "资源类型", required = true),
	})
	public ResultBean<List<LessonVo>>  findHadPushLesson(@RequestParam String teachingClassId,
														 @RequestParam(defaultValue = "2") String lessonTaskType) {
		List<LessonVo> lessonVoList = ListUtils.newArrayList();

		TeachingClass teachingClass = teachingClassService.get(teachingClassId);
		List<Chapter> chapterList = commonService.HadPushLessonTaskStruct(teachingClass.getCourse(),teachingClassId);
		Map<String,Integer> lessonTaskNumMap = CommonUtils.getLessonTaskNumMap(teachingClass.getCourse());
		for (Chapter chapter: chapterList){
			for (Lesson lesson: chapter.getLessonList()){
				StringBuffer stb = new StringBuffer();
				stb.append(chapter.getChapterNum()).append(".").append(lesson.getLessonNum());
				if (lesson.getSubLessonList().isEmpty()){
					for (LessonTask lessonTask: lesson.getLessonTaskList()) {
						if (lessonTaskType.equals(lessonTask.getType())){
							LessonVo lessonVo = new LessonVo();
							lessonVo.setLessonId(lesson.getId());
							lessonVo.setLessonName(lesson.getName());
							lessonVo.setSuffixNum(stb.toString());
							lessonVo.setTaskNum(lessonTaskNumMap.get(lesson.getId()));
							lessonVo.setLessonTaskId(lessonTask.getId());
							lessonVo.setLessonTaskName(lessonTask.getTaskName());
							lessonVoList.add(lessonVo);
						}
					}
				}else {
					stb.append(".");
					for (Lesson subLesson: lesson.getSubLessonList()){
						for (LessonTask lessonTask: subLesson.getLessonTaskList()) {
							if (lessonTaskType.equals(lessonTask.getType())){
								LessonVo lessonVo = new LessonVo();
								lessonVo.setLessonId(subLesson.getId());
								lessonVo.setLessonName(subLesson.getName());
								lessonVo.setSuffixNum(stb.toString() + subLesson.getLessonNum());
								lessonVo.setTaskNum(lessonTaskNumMap.get(subLesson.getId()));
								lessonVo.setLessonTaskId(lessonTask.getId());
								lessonVo.setLessonTaskName(lessonTask.getTaskName());
								lessonVoList.add(lessonVo);
							}
						}
					}
				}
			}
		}
		return success(lessonVoList);
	}

	@ApiOperation(value = "获取进行中的章节列表")
	@PostMapping(value = "findMyPendingChapterList")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
	})
	public ResultBean<List<Chapter>>  findMyPendingChapterList(@RequestParam String teachingClassId) {
		List<Chapter> chapterList = userChapterService.findMyPendingChapterList(teachingClassId);
		return success(chapterList);
	}

	@ApiOperation(value = "根据进行中的节或者小节  按章归档")
	@PostMapping(value = "findMyPendingLessonListPackageByChapter")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
	})
	public ResultBean<Map<String,List<Lesson>>>
			findMyPendingLessonListPackageByChapter(@RequestParam String teachingClassId) {
		Map<String,List<Lesson>> result = MapUtils.newHashMap();
		TeachingClass teachingClass = teachingClassService.get(teachingClassId);
		List<Lesson> lessonList = userLessonService.findMyPendingLessonList(teachingClassId);
		lessonList.forEach(lesson -> {
			List<Lesson> subLessonList = userLessonService.findMyPendingSubLessonList(teachingClassId,lesson.getLessonId());
			lesson.setSubLessonList(subLessonList);

			if (lesson.getChapter() != null
					&& StringUtils.isNotBlank(lesson.getChapter().getChapterId())){
				if (result.containsKey(lesson.getChapter().getChapterId())){
					result.get(lesson.getChapter().getChapterId()).add(lesson);
				}else {
					List<Lesson> lessons = ListUtils.newArrayList(lesson);
					result.put(lesson.getChapter().getChapterId(),lessons);
				}
			}
		});
		CommonUtils.buildLessonTaskNum(teachingClass.getCourse(),lessonList);
		return success(result);
	}


	@ApiOperation(value = "根据进行中的节或者小节")
	@PostMapping(value = "findMyPendingLessonList")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
			@ApiImplicitParam(name = "chapterId", value = "章节id", required = true),
	})
	public ResultBean<List<Lesson>>  findMyPendingLessonList(@RequestParam String teachingClassId,
													@RequestParam String chapterId) {
		// 矫正数据
		userChapterService.checkAndSaveIfFinishAllLesson(teachingClassId,chapterId);

		TeachingClass teachingClass = teachingClassService.get(teachingClassId);
		List<Lesson> lessonList = userLessonService.findMyPendingLessonList(teachingClassId,chapterId);
		lessonList.forEach(lesson -> {
			List<Lesson> subLessonList = userLessonService.findMyPendingSubLessonList(teachingClassId,lesson.getLessonId());
			lesson.setSubLessonList(subLessonList);
		});
		lessonList = CommonUtils.buildLessonTaskNum(teachingClass.getCourse(),lessonList);
		return success(lessonList);
	}

	@ApiOperation(value = "获取我的资源（作业）")
	@PostMapping(value = "findMyLessonTaskList")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
			@ApiImplicitParam(name = "lessonId", value = "节（或小节）id", required = true),
	})
	public ResultBean<List<LessonTask>>  findMyLessonTaskList(@RequestParam String teachingClassId,
													@RequestParam String lessonId) {
		// 获取前矫正数据
		userTaskService.checkAndSaveIfFinishAllSubOrLesson(teachingClassId,lessonId);
		List<LessonTask> lessonTaskList = userTaskService.findMyLessonTaskList(teachingClassId,lessonId);
		return success(lessonTaskList);
	}

	@ApiOperation(value = "获取我的已完成资源（作业）")
	@PostMapping(value = "findMyFinishLessonTaskList")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
			@ApiImplicitParam(name = "lessonId", value = "节（或小节）id", required = true),
	})
	public ResultBean<List<LessonTask>>  findMyFinishLessonTaskList(@RequestParam String teachingClassId,
															  @RequestParam String lessonId) {
		List<LessonTask> lessonTaskList = userTaskService.findMyLessonTaskList(teachingClassId,lessonId);
		return success(lessonTaskList);
	}

	@ApiOperation(value = "获取已完成的章节列表")
	@PostMapping(value = "findMyFinishChapterList")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
	})
	public ResultBean<List<Chapter>>  findMyFinishChapterList(@RequestParam String teachingClassId) {
		List<Chapter> chapterList = userChapterService.findMyFinishingChapterList(teachingClassId);
		return success(chapterList);
	}


	@ApiOperation(value = "根据已完成的节或者小节  按章归档")
	@PostMapping(value = "findMyFinishLessonListPackageByChapter")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
	})
	public ResultBean<Map<String,List<Lesson>>>
	findMyFinishLessonListPackageByChapter(@RequestParam String teachingClassId) {
		Map<String,List<Lesson>> result = MapUtils.newHashMap();

		List<Lesson> finishingLessonList = ListUtils.newArrayList();
		TeachingClass teachingClass = teachingClassService.get(teachingClassId);
		List<Lesson> lessonList = userLessonService.findMyFinishingLessonList(teachingClassId);
		Map<String,Lesson> lessonMap = MapUtils.newHashMap();
		lessonList.forEach(lesson -> {
			lessonMap.put(lesson.getId(),lesson);
		});

		List<Lesson> finishLessonList = userLessonService.findMyFinishLessonList(teachingClassId);

		for (Lesson lesson: finishLessonList){
			if (!lessonMap.containsKey(lesson.getId())){
				finishingLessonList.add(lesson);
			}
		}
		finishingLessonList.addAll(lessonList);
		finishingLessonList = ListUtils.listOrderBy(finishingLessonList,"lessonNum");

		finishingLessonList.forEach(lesson -> {
			if (lesson.isLesson()){
				List<Lesson> subLessonList = userLessonService.findMyFinishSubLessonList(teachingClassId,lesson.getLessonId());
				lesson.setSubLessonList(subLessonList);
			}
			if (lesson.getChapter() != null
					&& StringUtils.isNotBlank(lesson.getChapter().getChapterId())){
				if (result.containsKey(lesson.getChapter().getChapterId())){
					result.get(lesson.getChapter().getChapterId()).add(lesson);
				}else {
					List<Lesson> lessons = ListUtils.newArrayList(lesson);
					result.put(lesson.getChapter().getChapterId(),lessons);
				}
			}
		});

		CommonUtils.buildLessonTaskNum(teachingClass.getCourse(),finishingLessonList);

		return success(result);
	}


	@ApiOperation(value = "获取已完成的节或者小节")
	@PostMapping(value = "findMyFinishLessonList")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
			@ApiImplicitParam(name = "chapterId", value = "章节id", required = true),
	})
	public ResultBean<List<Lesson>>  findMyFinishLessonList(@RequestParam String teachingClassId,
															 @RequestParam String chapterId) {
		List<Lesson> finishingLessonList = ListUtils.newArrayList();
		TeachingClass teachingClass = teachingClassService.get(teachingClassId);
		List<Lesson> lessonList = userLessonService.findMyFinishingLessonList(teachingClassId,chapterId);
		Map<String,Lesson> lessonMap = MapUtils.newHashMap();
		lessonList.forEach(lesson -> {
			lessonMap.put(lesson.getId(),lesson);
		});

		List<Lesson> finishLessonList = userLessonService.findMyFinishLessonList(teachingClassId,chapterId);

		for (Lesson lesson: finishLessonList){
			if (!lessonMap.containsKey(lesson.getId())){
				finishingLessonList.add(lesson);
			}
		}
		finishingLessonList.addAll(lessonList);
		finishingLessonList = ListUtils.listOrderBy(finishingLessonList,"lessonNum");

		lessonList.forEach(lesson -> {
			List<Lesson> subLessonList = userLessonService.findMyFinishSubLessonList(teachingClassId,lesson.getLessonId());
			lesson.setSubLessonList(subLessonList);
		});

		finishingLessonList = CommonUtils.buildLessonTaskNum(teachingClass.getCourse(),finishingLessonList);
		return success(finishingLessonList);
	}

	@ApiOperation(value = "创建学生作业任务")
	@PostMapping(value = "createUserTask")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
			@ApiImplicitParam(name = "lessonTaskId", value = "lessonTaskId", required = true),
	})
	public ResultBean  createUserTask(@RequestParam String teachingClassId,
									  @RequestParam String lessonTaskId) {

		UserTask userTask = new UserTask();
		userTask.setTeachingClassId(teachingClassId);
		userTask.setLessonTaskId(lessonTaskId);
		userTask.setUserId(BaseUserUtils.getUser().getId());
		if (!userTaskService.isExist(userTask)){
			TeachingClass teachingClass = teachingClassService.get(userTask.getTeachingClassId());
			// 保存课堂和课程关联信息
			userTask.setTeachingClassId(teachingClass.getTeachingClassId());
			userTask.setCourseId(teachingClass.getCourse().getCourseId());
			userTask.setClassesId(teachingClass.getClasses().getClassesId());

			LessonTask lessonTask = lessonTaskService.get(userTask.getLessonTaskId());
			// 保存章节
			userTask.setTaskId(lessonTask.getTaskId());
			userTask.setType(lessonTask.getType());
			Lesson lesson = lessonService.get(lessonTask.getLessonId());
			Chapter chapter = null;
			if (lesson.isLesson()){
				userTask.setParentLessonId(lesson.getLessonId());
				chapter = lesson.getChapter();
			}
			if (lesson.isSubLesson()){
				Lesson parentLesson = lessonService.get(lesson.getParentLesson());
				userTask.setLessonId(lesson.getLessonId());
				userTask.setParentLessonId(parentLesson.getLessonId());
				chapter = parentLesson.getChapter();
			}
			if (chapter!= null){
				userTask.setChapterId(chapter.getChapterId());
			}

			userTask.setLessonId(lessonTask.getLessonId());
			userTask.setState(UserTaskStatue.PROGRESSING.value());
			// 保存自身关联信息
			userTask.setUserId(BaseUserUtils.getUser().getId());
			userTask.setStudentName(BaseUserUtils.getUser().getUserName());

			userTaskService.save(userTask);
		}
		return success(userTask.getId());
	}

	@ApiOperation(value = "已经完成任务")
	@PostMapping(value = "finishUserTask")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
			@ApiImplicitParam(name = "lessonTaskId", value = "lessonTaskId", required = true),
	})
	public ResultBean  finishUserTask(@RequestParam String teachingClassId,
									  @RequestParam String lessonTaskId) {
		UserTask userTaskCriteria = new UserTask();
		userTaskCriteria.setTeachingClassId(teachingClassId);
		userTaskCriteria.setLessonTaskId(lessonTaskId);
		userTaskCriteria.setUserId(BaseUserUtils.getUser().getId());

		List<UserTask> userTaskList = userTaskService.findList(userTaskCriteria);
		if (!userTaskList.isEmpty()){
			UserTask userTask = userTaskList.get(0);
			userTask.setState(UserTaskStatue.FINISHED.value());
			userTask.setFinishTime(new java.sql.Date(new Date().getTime()));
			userTaskService.save(userTask);
			// 校验是否完成所有资源  有则保存进度
			userTaskService.checkAndSaveIfFinishAllLessonTask(userTask);
		}

		return success();
	}

	@ApiOperation(value = "分页查询小节任务记录列表数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
	})
	@PostMapping(value = "findMyLessonRecordPage")
	public ResultBean<Page<UserLessonRecord>>  findMyLessonRecordPage(UserLessonRecord userLessonRecord,
														@RequestParam(defaultValue = "10") int pageSize,
														@RequestParam(defaultValue = "1") int pageNo ) {
		userLessonRecord.setPage(new Page<>(pageNo, pageSize));
		userLessonRecord.setStudent(BaseUserUtils.getUser());
		Page<UserLessonRecord> page = userLessonRecordService.findPage(userLessonRecord);
		return success(page);
	}

	@ApiOperation(value = "获取任务进度")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
	})
	@PostMapping(value = "getTaskRate")
	public ResultBean<Double>  getTaskRate(@RequestParam String teachingClassId) {
		UserTaskDto userTaskDto = new UserTaskDto();
		userTaskDto.setTeachingClassId(teachingClassId);
		userTaskDto.setUserId(BaseUserUtils.getUser().getId());
		Long unFinishCount = userTaskService.findUnFinishLessonTaskCount(userTaskDto);
		Long totalCount =  userTaskService.findTotalLessonTaskCount(userTaskDto);

		Long finishCount = totalCount - unFinishCount;
		return success(finishCount.doubleValue()/totalCount.doubleValue());
	}

	@ApiOperation(value = "获取课堂详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
	})
	@PostMapping(value = "getTeachingClass")
	public ResultBean<TeachingClass>  getTeachingClass(@RequestParam String teachingClassId) {
		TeachingClass teachingClass = TeachingClassUtils.getTeachingClass(teachingClassId);
		return success(teachingClass);
	}
}