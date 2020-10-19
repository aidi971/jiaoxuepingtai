/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.web;

import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.enumn.SysYesNoEnum;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.base.entity.Classes;
import com.langheng.modules.base.entity.ClassesApply;
import com.langheng.modules.base.entity.Student;
import com.langheng.modules.base.entity.StudentClasses;
import com.langheng.modules.base.enumn.ClassesApplyState;
import com.langheng.modules.base.enumn.StudentClassesState;
import com.langheng.modules.base.enumn.StudentOriginType;
import com.langheng.modules.base.service.ClassesApplyService;
import com.langheng.modules.base.service.ClassesService;
import com.langheng.modules.base.service.StudentClassesService;
import com.langheng.modules.base.service.StudentService;
import com.langheng.modules.base.utils.ClassesMsgUtils;
import com.langheng.modules.base.utils.ClassesUtils;
import com.langheng.modules.base.utils.RegisterUtils;
import com.langheng.modules.base.utils.StudentUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * 班级Controller
 * @author xiaoxie
 * @version 2019-12-18
 */
@Api(description = "学生班级相关接口 | 学生端")
@RestController
@RequestMapping("api/classes")
public class ClassesApi extends BaseApiController {

	@Autowired
	private ClassesService classesService;
	@Autowired
	private StudentClassesService studentClassesService;
	@Autowired
	private ClassesApplyService classesApplyService;
	@Autowired
	private StudentService studentService;

	/**
	 * 获取班级数据
	 */
	@ModelAttribute
	public Classes get(String classesId, boolean isNewRecord) {
		return classesService.get(classesId, isNewRecord);
	}

	@ApiOperation(value = "学生获取自己的班级")
	@PostMapping(value = "getClasses")
	public ResultBean<Classes> getClasses(){
		Student student = StudentUtils.getStudent();
		Classes classes = studentClassesService.getCurrentStudentClasses(student);
		if (StringUtils.isBlank(student.getClassesId())){
			student.setClassesId(classes.getClassesId());
			studentService.save(student);
		}
		return success(classes);
	}

	@ApiOperation(value = "检查是加入过班级状态 | (hadJoin 已经加入班级)，（hadApplied 已经申请，但未审核），（needApply 需要申请）")
	@PostMapping(value = "checkJoinClassesState")
	public ResultBean checkJoinClassesState(){
		Student student = StudentUtils.getStudent();
		StudentClasses studentClasses = new StudentClasses();
		studentClasses.setStudentId(student.getStudentId());

		long count = studentClassesService.findCount(studentClasses);
		if (count != 0){
			return success("hadJoin","已加入班级的学生不需要输入邀请码");
		}
		ClassesApply classesApply = new ClassesApply();
		classesApply.setStudent(student);
		count = classesApplyService.findCount(classesApply);
		if (count != 0){
			return success("hadApplied","您已经提交申请！请勿重复提交");
		}
		return success("needApply","请输入邀请码！");
	}

	@ApiOperation(value = "申请加入班级（输入邀请码）")
	@PostMapping(value = "applyJoinClasses")
	@Transactional(readOnly = false)
	public ResultBean applyJoinClasses(@RequestParam String invitationCode){
		Student student = StudentUtils.getStudent();
		Classes classes = classesService.getClassesByInvitationCode(invitationCode);
		if (classes == null){
			return error("该邀请码找不到班级！");
		}
		ClassesApply classesApplyCriteria = new ClassesApply();
		classesApplyCriteria.setStudent(student);
		long count = classesApplyService.findCount(classesApplyCriteria);
		if (count != 0){
			return error("您已经提交申请!请勿重复提交!");
		}

		if (SysYesNoEnum.YES.value().equals(classes.getIsNeedAudit())){
			ClassesApply classesApply = new ClassesApply();
			classesApply.setClasses(classes);
			classesApply.setStudent(student);
			classesApply.setStuName(student.getUserName());
			classesApply.setLoginCode(student.getLoginCode());
			classesApply.setTeacherId(classes.getTeacher().getTeacherId());
			classesApply.setState(ClassesApplyState.PENDING.value());
			classesApplyService.save(classesApply);

			RegisterUtils.applyJoinClasses(student,classes);

			// 发送班级通知
			ClassesMsgUtils.pushClassesMsg(student,classes,false);

			return success(null,"已发送加入班级申请，等待教师审核");
		}else {
			// 不需要审核，直接加入班级
			StudentClasses studentClasses = new StudentClasses();
			studentClasses.setClassesId(classes.getId());
			studentClasses.setStudentId(student.getId());
			studentClasses.setState(StudentClassesState.NORMAL.value());
			studentClassesService.save(studentClasses);

			student.setOriginType(StudentOriginType.INVITE.value());
			student.setClassesId(classes.getClassesId());
			studentService.save(student);

			// 调整班级数量
			classesService.adjustStudentNum(classes);
			// 更新班级在线人数
			ClassesUtils.addOnlineStu(classes,student.getStudentId());

			RegisterUtils.hadJoinClasses(student,classes);

			// 发送班级通知
			ClassesMsgUtils.pushClassesMsg(student,classes,true);

			return success(null,"加入班级成功！");

		}

	}
}