/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.ed.web;

import com.jeesite.common.cache.CacheUtils;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.base.entity.Student;
import com.langheng.modules.base.service.StudentClassesService;
import com.langheng.modules.base.service.StudentService;
import com.langheng.modules.ed.entity.Chapter;
import com.langheng.modules.ed.entity.Course;
import com.langheng.modules.ed.entity.StudentVo;
import com.langheng.modules.ed.entity.TeachingClass;
import com.langheng.modules.ed.service.CommonService;
import com.langheng.modules.ed.service.CourseService;
import com.langheng.modules.ed.service.TeachingClassService;
import com.langheng.modules.ed.service.UserTaskService;
import com.langheng.modules.ed.util.TeachingClassUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 课堂Controller
 * @author xiaoxie
 * @version 2020-02-11
 */
@Api(description = "课堂进度管理")
@RestController
@RequestMapping(value = "admin/teachClassProgress")
public class TeachClassProgressController extends BaseApiController {

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
	private StudentService studentService;

	/**
	 * 获取课堂数据
	 */
	@ModelAttribute
	public TeachingClass get(String teachingClassId, boolean isNewRecord) {
		return teachingClassService.get(teachingClassId, isNewRecord);
	}
	
 	@ApiOperation(value = "课堂进度结构  章-节-小节")
	@PostMapping(value = "progressStruct")
	public ResultBean<List<Chapter>>  progressStruct(@RequestParam String teachingClassId) {
		TeachingClass teachingClass = teachingClassService.get(teachingClassId);
		Course course = courseService.get(teachingClass.getCourse());
		return success(commonService.progressStruct(course,teachingClassId,teachingClass.getClasses().getClassesId()));
	}

	@ApiOperation(value = "某章的完成进度")
	@PostMapping(value = "chapterProgress")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
			@ApiImplicitParam(name = "chapterId", value = "章节id", required = true),
	})
	public ResultBean  chapterProgress(@RequestParam String teachingClassId,
									   @RequestParam String chapterId) {
		Map<String,Object> resultMap = MapUtils.newHashMap();
		TeachingClass teachingClass = teachingClassService.get(teachingClassId);
		List<Student> studentList = studentClassesService.findStudents(teachingClass.getClasses().getClassesId());
		if (studentList.isEmpty()){
			return error("该课堂没有任何学生！");
		}

		// 进行中
		List<StudentVo> pendingStudents = ListUtils.newArrayList();

		List<StudentVo> unStartStudents = userTaskService.findUnStartStudents(teachingClassId,chapterId);
		List<StudentVo> hadFinishStudents = userTaskService.findHadFinishStudents(teachingClassId,chapterId);

		studentList.forEach(student -> {
			boolean isExist = false;
			for (StudentVo unStartStudent: unStartStudents){
				if (student.getId().equals(unStartStudent.getId())){
					isExist = true;
					break;
				}
			}
			for (StudentVo hadFinishStudent: hadFinishStudents){
				if (student.getId().equals(hadFinishStudent.getId())){
					isExist = true;
					break;
				}
			}
			if (!isExist){
				StudentVo studentVo = new StudentVo();
				studentVo.setId(student.getId());
				studentVo.setLoginCode(student.getLoginCode());
				studentVo.setUserName(student.getUserName());
				pendingStudents.add(studentVo);
			}

		});

		resultMap.put("pendingRate",pendingStudents.size()/new Double(studentList.size()));
		resultMap.put("pendingStudents",pendingStudents);

		resultMap.put("hadFinishRate",hadFinishStudents.size()/new Double(studentList.size()));
		resultMap.put("hadFinishStudents",hadFinishStudents);

		resultMap.put("unStartRate",unStartStudents.size()/new Double(studentList.size()));
		resultMap.put("unStartStudents",unStartStudents);

		return success(resultMap);
	}


	@ApiOperation(value = "学生进度列表")
	@PostMapping(value = "studentProgressList")
	public ResultBean<List<StudentVo>>  studentProgressList(@RequestParam String teachingClassId) {
		CacheUtils.remove("teaching_class_student_progress",teachingClassId);
		List<StudentVo> studentVoList
				= TeachingClassUtils.getStudentProgress(teachingClassId);
		return success(studentVoList);
	}

	@ApiOperation(value = "学生的进度结构  个人进度 章-节-小节")
	@PostMapping(value = "studentProgressStruct")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
			@ApiImplicitParam(name = "studentId", value = "学生id", required = true),
	})
	public ResultBean<List<Chapter>>  studentProgressStruct(@RequestParam String teachingClassId,
															@RequestParam String studentId) {
		TeachingClass teachingClass = teachingClassService.get(teachingClassId);
		Course course = courseService.get(teachingClass.getCourse());
		return success(commonService.studentProgressStruct(course,teachingClassId,studentId));
	}

	@ApiOperation(value = "学生个人进度统计")
	@PostMapping(value = "studentProgress")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
			@ApiImplicitParam(name = "studentId", value = "学生id", required = true),
	})
	public ResultBean<StudentVo> studentProgress(@RequestParam String teachingClassId,
															@RequestParam String studentId) {
		List<StudentVo> studentVoList
				= TeachingClassUtils.getStudentProgress(teachingClassId);

		for (StudentVo studentVo: studentVoList){
			if (studentVo.getId().equals(studentId)){
				return success(studentVo);
			}
		}
		return success(null);
	}
}