/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.web;


import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.base.entity.Teacher;
import com.langheng.modules.base.utils.TeacherUtils;
import com.langheng.modules.ed.entity.Course;
import com.langheng.modules.ed.entity.Mission;
import com.langheng.modules.ed.entity.Template;
import com.langheng.modules.ed.enumn.TemplateState;
import com.langheng.modules.ed.service.MissionCourseService;
import com.langheng.modules.ed.service.MissionService;
import com.langheng.modules.ed.service.MissionTemplateService;
import com.langheng.modules.ed.service.TeachingClassService;
import com.langheng.modules.ed.util.MissionCommonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 课程Controller
 * @author xiaoxie
 * @version 2019-12-17
 */
@Api(description = "任务型-课程管理 | 教师端")
@RestController
@RequestMapping(value = "admin/missionCourse")
public class MissionCourseController extends BaseApiController {

	@Autowired
	private MissionCourseService missionCourseService;
	@Autowired
	private MissionTemplateService missionTemplateService;
	@Autowired
	private MissionService missionService;
	@Autowired
	private TeachingClassService teachingClassService;

	@ModelAttribute
	public Course get(String courseId, boolean isNewRecord) {
		return missionCourseService.get(courseId, isNewRecord);
	}
	
	@ApiOperation(value = "新增或修改课程")
	@PostMapping(value = "save")
	public ResultBean save(@Validated Course course) {
		if (course.getIsNewRecord()) {
			Course courseCriteria = new Course();
			courseCriteria.setName(course.getName());
			courseCriteria.setTeacher(new Teacher(TeacherUtils.getTeacher().getTeacherId()));
			Long count = missionCourseService.findCount(courseCriteria);
			if (count > 0){
				return error("该课程名称已存在!");
			}
		}
		missionCourseService.save(course);
		return success(course);
	}

	@ApiOperation(value = "展开课程，显示所有章节-节-小节-资源")
	@PostMapping(value = "expand")
	public ResultBean<List<Mission>>  expand(@RequestParam String courseId) {
	    Course course = missionCourseService.get(courseId);
	    List<Mission> missionList = MissionCommonUtils.expandByCourse(course);
		return success(missionList);
	}

	@ApiOperation(value = "课程另存")
	@PostMapping(value = "saveAs")
	public ResultBean saveAs(@RequestParam String courseId,
							 @RequestParam String name) {
		Course course = missionCourseService.get(courseId);
		course.setName(name);
		Course newCourse = MissionCommonUtils.saveAsCourse(course);
		return success(newCourse.getFullName());
	}

	@ApiOperation(value = "课程申请公开")
	@PostMapping(value = "applyPublic")
	public ResultBean applyPublic(@RequestParam String courseId) {
		Course course = missionCourseService.get(courseId);
		Template templateCriteria = new Template();
		templateCriteria.setTeacher(TeacherUtils.getTeacher());
		templateCriteria.setTraceSourceId(course.getId());
		List<Template> templateList = missionTemplateService.findList(templateCriteria);
		if (!templateList.isEmpty()){
			for (Template template : templateList){
				if (TemplateState.HAD_PUBLIC.value().equals(template.getState())){
					return error("标准模板/公共模板中已存在同名称的，请先另存后再申请！");
				}else if (TemplateState.UN_AUDITED.value().equals(template.getState())){
					return error("该课程审核已提交,请耐心等待！");
				}
			}
		}
		MissionCommonUtils.applyPublic(course);
		return success();
	}

}