/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.web;

import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.base.service.StudentClassesService;
import com.langheng.modules.ed.entity.*;
import com.langheng.modules.ed.enumn.LessonTaskType;
import com.langheng.modules.ed.enumn.UserTaskStatue;
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
import java.util.Map;

/**
 * 课堂Controller
 * @author xiaoxie
 * @version 2020-02-11
 */
@Api(description = "课堂教学质量管理")
@RestController
@RequestMapping(value = "admin/teachQuality")
public class TeachQualityController extends BaseApiController {

	@Autowired
	private TeachingClassService teachingClassService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private CommonService commonService;
	@Autowired
	UserTaskService userTaskService;
	@Autowired
	private StudentClassesService studentClassesService;
	@Autowired
	private LessonTaskService lessonTaskService;

 	@ApiOperation(value = "质量矩阵")
	@PostMapping(value = "qualityMatrix")
	public ResultBean<List<QualityMatrixVo>>  qualityMatrix(@RequestParam String teachingClassId,
													 String chapterId) {
		TeachingClass teachingClass = teachingClassService.get(teachingClassId);
		UserTaskDto userTaskDto = new UserTaskDto();
		userTaskDto.setChapterId(chapterId);
		userTaskDto.setTeachingClassId(teachingClass.getTeachingClassId());
		userTaskDto.setClassesId(teachingClass.getClasses().getClassesId());
		List<QualityMatrixVo> qualityMatrixVos = userTaskService.qualityMatrix(userTaskDto);
		return success(qualityMatrixVos);
	}

	@ApiOperation(value = "课堂统计完成度和平均分 |  课堂详情首页统计")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
	})
	@PostMapping(value = "statisticQuality")
	public ResultBean statisticQuality(String teachingClassId) {
		TeachingClass teachingClass = teachingClassService.get(teachingClassId);
		Long studentNum = studentClassesService.getStudentNum(teachingClass.getClasses().getClassesId());

		LessonTask objLessonTask = new LessonTask();
		objLessonTask.setTeachingClassId(teachingClassId);
		objLessonTask.setType(LessonTaskType.OBJECTIVES.value());
		Long objLessonTaskNum = lessonTaskService.findCountHadPush(objLessonTask);

		UserTask objUserTask =  new UserTask();
		objUserTask.setTeachingClassId(teachingClassId);
		objUserTask.setType(LessonTaskType.OBJECTIVES.value());
		objUserTask.setState(UserTaskStatue.FINISHED.value());
		Long objHadFinishedLessonTaskNum = userTaskService.findCount(objUserTask);

		UserTaskDto userTaskDto = new UserTaskDto();
		userTaskDto.setTeachingClassId(teachingClassId);
		userTaskDto.setLessonTaskType(LessonTaskType.OBJECTIVES.value());
		Float objAverageScore = userTaskService.getAverageScore(userTaskDto);


		// 主观题统计
		LessonTask subLessonTask = new LessonTask();
		subLessonTask.setTeachingClassId(teachingClassId);
		subLessonTask.setType(LessonTaskType.SUBJECTIVITY.value());
		Long subLessonTaskNum = lessonTaskService.findCountHadPush(subLessonTask);

		UserTask subUserTask =  new UserTask();
		subUserTask.setTeachingClassId(teachingClassId);
		subUserTask.setType(LessonTaskType.SUBJECTIVITY.value());
		subUserTask.setState(UserTaskStatue.FINISHED.value());
		Long subHadFinishedLessonTaskNum = userTaskService.findCount(subUserTask);

		userTaskDto.setLessonTaskType(LessonTaskType.SUBJECTIVITY.value());
		Float subAverageScore = userTaskService.getAverageScore(userTaskDto);

		// 视频统计
		LessonTask videoLessonTask = new LessonTask();
		videoLessonTask.setTeachingClassId(teachingClassId);
		videoLessonTask.setType(LessonTaskType.VIDEO.value());
		Long videoLessonTaskNum = lessonTaskService.findCount(videoLessonTask);

		UserTask videoUserTask =  new UserTask();
		videoUserTask.setTeachingClassId(teachingClassId);
		videoUserTask.setType(LessonTaskType.VIDEO.value());
		videoUserTask.setState(UserTaskStatue.FINISHED.value());
		Long videoHadFinishedLessonTaskNum = userTaskService.findCount(videoUserTask);


		Map<String,Object> result = MapUtils.newHashMap();
		if (studentNum*objLessonTaskNum == 0){
			result.put("objProgress",0);
			result.put("objAverageScore",0);
		}else {
			result.put("objProgress",objHadFinishedLessonTaskNum.doubleValue()/(studentNum*objLessonTaskNum));
			result.put("objAverageScore",objAverageScore);
		}

		if (studentNum*subLessonTaskNum == 0){
			result.put("subProgress",0);
			result.put("subAverageScore",0);
		}else {
			result.put("subProgress",subHadFinishedLessonTaskNum.doubleValue()/(studentNum*subLessonTaskNum));
			result.put("subAverageScore",subAverageScore);
		}

		if (studentNum*videoLessonTaskNum == 0){
			result.put("videoProgress",1);
		}else {
			result.put("videoProgress",videoHadFinishedLessonTaskNum.doubleValue()/(studentNum*videoLessonTaskNum));
		}
		return success(result);
	}

}