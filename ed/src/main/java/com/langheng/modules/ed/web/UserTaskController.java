/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.web;

import com.jeesite.common.entity.Page;
import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseApiController;
import com.jeesite.framework.uitls.EntityUtils;
import com.langheng.modules.base.entity.BaseUser;
import com.langheng.modules.base.enumn.UserType;
import com.langheng.modules.base.service.BaseUserService;
import com.langheng.modules.ed.entity.*;
import com.langheng.modules.ed.enumn.LessonTaskType;
import com.langheng.modules.ed.enumn.UserTaskStatue;
import com.langheng.modules.ed.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;

/**
 * 学生任务Controller
 * @author xiaoxie
 * @version 2019-12-17
 */
@Api(description = "学生任务 | 教师端")
@RestController
@RequestMapping(value = "admin/userTask")
public class UserTaskController extends BaseApiController {

	@Autowired
	private BaseUserService baseUserService;
	@Autowired
	private UserTaskService userTaskService;
	@Autowired
	private ChapterService chapterService;
	@Autowired
	private LessonService lessonService;
	@Autowired
	private UserChapterService userChapterService;
	@Autowired
	private UserLessonService userLessonService;
	@Autowired
	private LessonTaskService lessonTaskService;

	/**
	 * 获取学生任务数据
	 */
	@ModelAttribute
	public UserTask get(String userTaskId, boolean isNewRecord) {
		return userTaskService.get(userTaskId, isNewRecord);
	}
	
 	@ApiOperation(value = "分页查询学生任务列表数据")
	@PostMapping(value = "findPage")
	public ResultBean<Page<UserTask>> findPage(UserTask userTask, HttpServletRequest request, HttpServletResponse response) {
		userTask.setPage(new Page<>(request, response));
		Page<UserTask> page = userTaskService.findPage(userTask);
		return success(page);
	}

    @ApiOperation(value = "查询学生任务列表数据")
	@PostMapping(value = "findList")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
			@ApiImplicitParam(name = "lessonTaskId", value = "小节任务id", required = true),
			@ApiImplicitParam(name = "taskId", value = "任务id", required = true),
			@ApiImplicitParam(name = "type", value = "类型 |字典集 ed_lesson_task_type", required = true),

	})
	public ResultBean  findList(UserTask userTask, HttpServletRequest request, HttpServletResponse response) {

		List<UserTask> list = userTaskService.findList(userTask);
		return success(list);
	}

	@ApiOperation(value = "客观题任务列表，模糊查询学生任务")
	@PostMapping(value = "findTestList")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
			@ApiImplicitParam(name = "lessonTaskId", value = "小节任务id", required = true),
			@ApiImplicitParam(name = "taskId", value = "任务id", required = true),
			@ApiImplicitParam(name = "UserNameOrUserId", value = "搜索内容", required = true),
	})
	public ResultBean  findTestList(UserTask userTask,
								  @RequestParam(value = "UserNameOrUserId", required = false)  String UserNameOrUserId) {
		if (StringUtils.isNotBlank(UserNameOrUserId)) {
			BaseUser baseUser = new BaseUser();
			baseUser.setUserType(UserType.STUDENT.value());
			baseUser.setLoginCodeOrUserId(UserNameOrUserId);
			List<BaseUser> baseUsers = baseUserService.findList(baseUser);
			String[] baseEntityIds = EntityUtils.getBaseEntityIds(baseUsers);
			userTask.setUserId_in(baseEntityIds);
		}
		userTask.setState(UserTaskStatue.FINISHED.value());
		userTask.setType(LessonTaskType.OBJECTIVES.value());
		userTask.getSqlMap().getOrder().setOrderBy("finishTime DESC");
		List<UserTask> list = userTaskService.findList(userTask);
		return success(list);
	}

	@ApiOperation(value = "实践题任务列表，模糊查询学生任务")
	@PostMapping(value = "findPracticeList")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
			@ApiImplicitParam(name = "lessonTaskId", value = "小节任务id", required = true),
			@ApiImplicitParam(name = "taskId", value = "任务id", required = true),
			@ApiImplicitParam(name = "UserNameOrUserId", value = "搜索内容", required = true),
	})
	public ResultBean  findPracticeList(UserTask userTask,
									@RequestParam(value = "UserNameOrUserId", required = false)  String UserNameOrUserId) {
		if (StringUtils.isNotBlank(UserNameOrUserId)) {
			BaseUser baseUser = new BaseUser();
			baseUser.setUserType(UserType.STUDENT.value());
			baseUser.setLoginCodeOrUserId(UserNameOrUserId);
			List<BaseUser> baseUsers = baseUserService.findList(baseUser);
			String[] baseEntityIds = EntityUtils.getBaseEntityIds(baseUsers);
			userTask.setUserId_in(baseEntityIds);
		}
		userTask.setState(UserTaskStatue.FINISHED.value());
		userTask.setType(LessonTaskType.PRACTICE.value());
		userTask.getSqlMap().getOrder().setOrderBy("finishTime DESC");
		List<UserTask> list = userTaskService.findList(userTask);
		return success(list);
	}

	@ApiOperation(value = "已完成学生平均分")
	@PostMapping(value = "getAveScore")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
			@ApiImplicitParam(name = "lessonTaskId", value = "小节任务id", required = true),
			@ApiImplicitParam(name = "taskId", value = "任务id", required = true),
			@ApiImplicitParam(name = "type", value = "类型 |字典集 ed_lesson_task_type", required = true),
	})
	public ResultBean getAveScore(@Validated UserTask userTask) {
		Float sum=0F;
		userTask.setState(UserTaskStatue.FINISHED.value());
		List<UserTask> list = userTaskService.findList(userTask);

		LessonTask lessonTask = new LessonTask();
		lessonTask.setLessonTaskId(userTask.getLessonTaskId());
		lessonTask.selectModifyScore(userTask.getTeachingClassId());
		LessonTask lessonTaskInfo = lessonTaskService.get(lessonTask);

		for (UserTask task : list) {
			sum=sum+task.getScore();
		}
		long count = userTaskService.findCount(userTask);
		Float result=0f;
		if (count!=0) {
			Float ave=sum/count;
			NumberFormat numberFormat = NumberFormat.getInstance();
			// 设置精确到小数点后2位
			numberFormat.setMaximumFractionDigits(2);
			result = Float.valueOf(numberFormat.format(ave));//所占百分比
		}

		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("count",count);
		hashMap.put("ave",result);
		hashMap.put("sum",lessonTaskInfo.getScore());
		return success(hashMap);
	}

	@ApiOperation(value = "保存学生任务")
	@PostMapping(value = "save")
	public ResultBean save(@Validated UserTask userTask) {
		userTaskService.save(userTask);
		return success();
	}

	@ApiOperation(value = "修改学生分数")
	@PostMapping(value = "updateSocre")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userTaskId", value = "学生任务id", required = true),
			@ApiImplicitParam(name = "score", value = "分数", required = true),

	})
	public ResultBean updateSocre(@Validated UserTask userTask) {
		userTaskService.save(userTask);
		return success();
	}

	@ApiOperation(value = "批量修改学生分数")
	@PostMapping(value = "batchUpdateSocre")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userTaskIds", value = "学生任务id", required = true),
			@ApiImplicitParam(name = "score", value = "分数", required = true),
	})
	@Transactional(readOnly = true)
	public ResultBean batchUpdateSocre(@RequestParam("userTaskIds") List<String> userTaskIds,
									   @RequestParam("score") Float score) {
		for (String userTaskId : userTaskIds) {
			UserTask userTask = new UserTask();
			userTask.setScore(score);
			userTask.setUserTaskId(userTaskId);
			userTaskService.save(userTask);
		}

		return success();
	}

	@ApiOperation(value = "删除学生任务")
	@PostMapping(value = "delete")
	public ResultBean delete(UserTask userTask) {
		userTaskService.delete(userTask);
		return success();
	}


	@ApiOperation(value = "清空课程答题记录")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
	})
	@PostMapping(value = "clearByCourse")
	public ResultBean clearByCourse(@RequestParam String teachingClassId) {
		UserTask userTaskCriteria = new UserTask();
		userTaskCriteria.setTeachingClassId(teachingClassId);
		List<UserTask> userTaskList = userTaskService.findList(userTaskCriteria);
		userTaskService.phyDeleteByEntity(userTaskCriteria);

		userTaskList.forEach(userTask->{
			if (StringUtils.isNotBlank(userTask.getChapterId())){
				UserChapter userChapter = new UserChapter();
				userChapter.setChapterId(userTask.getChapterId());
				userChapterService.deleteByEntity(userChapter);
			}

			if (StringUtils.isNotBlank(userTask.getLessonId())){
				UserLesson userLesson = new UserLesson();
				userLesson.setLessonId(userTask.getLessonId());
				userLessonService.deleteByEntity(userLesson);
			}

			if (StringUtils.isNotBlank(userTask.getParentLessonId())){
				UserLesson userLesson = new UserLesson();
				userLesson.setLessonId(userTask.getParentLessonId());
				userLessonService.deleteByEntity(userLesson);
			}

		});

		return success();
	}

	@ApiOperation(value = "清空章节答题记录")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "chapterIds", value = "章节id，多个则用逗号分隔", required = true),
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
	})
	@PostMapping(value = "clearByChapter")
	public ResultBean clearByChapter(@RequestParam String chapterIds,
									 @RequestParam String teachingClassId) {
		String [] chapterIdArr = chapterIds.split(",");
		for (String chapterId: chapterIdArr){
			UserTask userTaskCriteria = new UserTask();
			userTaskCriteria.setChapterId(chapterId);
			userTaskCriteria.setTeachingClassId(teachingClassId);
			List<UserTask> userTaskList = userTaskService.findList(userTaskCriteria);
			userTaskService.phyDeleteByEntity(userTaskCriteria);

			userTaskList.forEach(userTask->{

				if (StringUtils.isNotBlank(userTask.getLessonId())){
					UserLesson userLesson = new UserLesson();
					userLesson.setLessonId(userTask.getLessonId());
					userLessonService.deleteByEntity(userLesson);
				}

				if (StringUtils.isNotBlank(userTask.getParentLessonId())){
					UserLesson userLesson = new UserLesson();
					userLesson.setLessonId(userTask.getParentLessonId());
					userLessonService.deleteByEntity(userLesson);
				}

			});

			UserChapter userChapter = new UserChapter();
			userChapter.setChapterId(chapterId);
			userChapterService.deleteByEntity(userChapter);
		}
		return success();
	}

	@ApiOperation(value = "清空‘节’或者小节答题记录")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "lessonIds", value = "节或者小节id，多个则用逗号分隔", required = true),
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
	})
	@PostMapping(value = "clearByLesson")
	public ResultBean clearByLesson(@RequestParam String lessonIds,
									   @RequestParam String teachingClassId) {

		String [] lessonIdArr = lessonIds.split(",");
		for (String lessonId : lessonIdArr){
			Lesson lesson = lessonService.get(lessonId);

			UserTask userTaskCriteria = new UserTask();
			if (lesson.isSubLesson()){
				userTaskCriteria.setLessonId(lesson.getLessonId());
				userTaskCriteria.setTeachingClassId(teachingClassId);
				userTaskService.phyDeleteByEntity(userTaskCriteria);
			}else if (lesson.isLesson()){
				userTaskCriteria.setParentLessonId(lesson.getLessonId());
				userTaskCriteria.setTeachingClassId(teachingClassId);
				userTaskService.phyDeleteByEntity(userTaskCriteria);
			}

			UserLesson userLesson = new UserLesson();
			userLesson.setLessonId(lesson.getLessonId());
			userLessonService.deleteByEntity(userLesson);
		}
		return success();
	}
}