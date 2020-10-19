/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.web;

import com.jeesite.common.entity.Page;
import com.jeesite.common.entity.ResultBean;
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
import com.langheng.modules.base.utils.BaseUserUtils;
import com.langheng.modules.base.utils.RegisterUtils;
import com.langheng.modules.base.utils.TeacherUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 加入班级申请Controller
 * @author xiaoxie
 * @version 2020-03-30
 */
@Api("加入班级申请管理 | 教师端")
@RestController
@RequestMapping(value = "admin/classesApply")
public class ClassesApplyController extends BaseApiController {

	@Autowired
	private ClassesApplyService classesApplyService;
	@Autowired
	private StudentClassesService studentClassesService;
	@Autowired
	private ClassesService classesService;
	@Autowired
	private StudentService studentService;
	
	/**
	 * 获取加入班级申请数据
	 */
	@ModelAttribute
	public ClassesApply get(String classesApplyId, boolean isNewRecord) {
		return classesApplyService.get(classesApplyId, isNewRecord);
	}

	@ApiOperation(value = "分页查询加入班级申请列表数据")
	@PostMapping(value = "applyCount")
	public ResultBean<Long>  applyCount(String classesId) {
		ClassesApply classesApply = new ClassesApply();
		classesApply.setClasses(new Classes(classesId));
		classesApply.setState(ClassesApplyState.PENDING.value());
		return success(classesApplyService.findCount(classesApply));
	}

 	@ApiOperation(value = "分页查询加入班级申请列表数据")
	@PostMapping(value = "findPage")
	public ResultBean<Page<ClassesApply>>  findPage(String classesId,
								@RequestParam(defaultValue = "10") int pageSize,
                                @RequestParam(defaultValue = "1") int pageNo ) {
		ClassesApply classesApply = new ClassesApply();
		classesApply.setClasses(new Classes(classesId));
		if (!BaseUserUtils.isSuperAdmin()) {
			classesApply.setTeacherId(TeacherUtils.getTeacher().getTeacherId());
		}
		classesApply.setPage(new Page<>(pageNo, pageSize));
		Page<ClassesApply> page = classesApplyService.findPage(classesApply);
		return success(page);
	}


	@ApiOperation(value = "审核通过")
	@PostMapping(value = "agree")
	public ResultBean agree(@RequestParam String classesApplyIds) {

		String [] classesApplyIdArr = classesApplyIds.split(",");

		for (String classesApplyId: classesApplyIdArr){
			ClassesApply classesApply = classesApplyService.get(classesApplyId);
			Student student = classesApply.getStudent();
			Classes classes = classesApply.getClasses();

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

			// 修改申请状态
			classesApply.setState(ClassesApplyState.AGREE.value());
			classesApplyService.save(classesApply);

			RegisterUtils.hadJoinClasses(student,classes);

		}

		return success();
	}

	@ApiOperation(value = "审核拒绝")
	@PostMapping(value = "reject")
	public ResultBean reject(@RequestParam String classesApplyIds,
							 String  reason) {

		String [] classesApplyIdArr = classesApplyIds.split(",");

		for (String classesApplyId: classesApplyIdArr){
			ClassesApply classesApply = classesApplyService.get(classesApplyId);
			// 修改申请状态
			classesApply.setState(ClassesApplyState.REJECT.value());
			classesApplyService.save(classesApply);

			Classes classes = classesService.get(classesApply.getClasses());
			RegisterUtils.rejectJoinClasses(classesApply.getStudent(),classes);
		}

		return success();
	}
}