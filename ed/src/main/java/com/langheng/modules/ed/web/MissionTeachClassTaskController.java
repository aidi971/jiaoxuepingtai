/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.web;


import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.ed.entity.*;
import com.langheng.modules.ed.service.*;
import com.langheng.modules.ed.util.MissionCommonUtils;
import com.langheng.modules.ed.util.TeachClassMsgUtils;
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
@Api(description = "任务型-课堂任务推送 | 教师端")
@RestController
@RequestMapping(value = "admin/missionTeachClassTask")
public class MissionTeachClassTaskController extends BaseApiController {
	@Autowired
	private TeachingClassService teachingClassService;
	@Autowired
	private MissionCourseService missionCourseService;
	@Autowired
	private MissionService missionService;
	@Autowired
	private TeachClassLessonTaskService teachClassLessonTaskService;
	@Autowired
	private MissionLessonTaskService missionLessonTaskService;
	@Autowired
	private TeachClassMissionService teachClassMissionService;
	@Autowired
	private MissionUserTaskService missionUserTaskService;
	@Autowired
	private UserMissionService userMissionService;

	@ApiOperation(value = "课程所有结构，显示所有章节-节-小节-资源")
	@PostMapping(value = "lessonTaskStruct")
	public ResultBean<List<Mission>>  lessonTaskStruct(@RequestParam String teachingClassId) {
		TeachingClass teachingClass = teachingClassService.get(teachingClassId);
		Course course = missionCourseService.get(teachingClass.getCourse());
		return success(MissionCommonUtils.lessonTaskStruct(course,teachingClassId),"获取成功！",course.getTotalLessonNum());
	}

	@ApiOperation(value = "课程已经推送结构，显示所有章节-节-小节-资源")
	@PostMapping(value = "HadPushLessonTaskStruct")
	public ResultBean<List<Mission>>  HadPushLessonTaskStruct(@RequestParam String teachingClassId) {
		TeachingClass teachingClass = teachingClassService.get(teachingClassId);
		Course course = missionCourseService.get(teachingClass.getCourse());
		return success(MissionCommonUtils.HadPushLessonTaskStruct(course,teachingClassId),"获取成功！",course.getHadPushLessonNum());
	}

	@ApiOperation(value = "(拖拽)推送整个Mission")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "missionId", value = "章节id", required = true),
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
	})
	@PostMapping(value = "pushMission")
	public ResultBean pushMission(@RequestParam String teachingClassId,
								  @RequestParam String missionId) {
		Mission mission = missionService.get(missionId);
		if (mission != null && StringUtils.isNotBlank(mission.getMissionId())){

			TeachClassMission tcc = new TeachClassMission();
			tcc.setMissionId(missionId);
			tcc.setTeachingClassId(teachingClassId);
			//teachClassMissionService.flushSave(tcc);

			// 发送学习任务通知
			TeachClassMsgUtils.pushTeachClassMsg(teachingClassId);
			// 刷新用户任务
			//userMissionService.flushUserTask(mission,teachingClassId);
		}
		return success();
	}

	@ApiOperation(value = "撤回整个Mission")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "missionId", value = "课程任务项id", required = true),
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
	})
	@PostMapping(value = "recallMission")
	public ResultBean recallMission(@RequestParam String missionId,
									@RequestParam String teachingClassId) {
		Mission mission = missionService.get(missionId);
		if (mission != null && StringUtils.isNotBlank(mission.getMissionId())){

			UserTask userTask = new UserTask();
			userTask.setMissionId(missionId);
			userTask.setTeachingClassId(teachingClassId);
			missionUserTaskService.phyDeleteByEntity(userTask);
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
			missionUserTaskService.phyDeleteByEntity(userTask);
		}
		return success();
	}
}