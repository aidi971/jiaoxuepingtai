/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.web;

import com.jeesite.common.entity.Page;
import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.base.entity.Register;
import com.langheng.modules.base.entity.Student;
import com.langheng.modules.base.enumn.EventHandlingType;
import com.langheng.modules.base.enumn.RegisterApplyState;
import com.langheng.modules.base.service.RegisterService;
import com.langheng.modules.base.service.StudentService;
import com.langheng.modules.base.utils.BaseUserUtils;
import com.langheng.modules.base.utils.RegisterUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注册管理Controller
 * @author xiaoxie
 * @version 2020-07-29
 */
@Api(description = "注册管理")
@RestController
@RequestMapping(value = "admin/register")
public class RegisterController extends BaseApiController {

	@Autowired
	private RegisterService registerService;
	@Autowired
	private StudentService studentService;

	@ApiOperation(value = "分页查询已注册列表数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userNameOrLoginCode", value = "登录ID或姓名"),
			@ApiImplicitParam(name = "classes.className", value = "班级名称"),
			@ApiImplicitParam(name = "teacher.teacherName", value = "教师名称"),
			@ApiImplicitParam(name = "applyState", value = "申请状态"),
			@ApiImplicitParam(name = "registerTime_lte", value = "注册时间。小于"),
			@ApiImplicitParam(name = "registerTime_gte", value = "注册时间。大于"),
	})
	@PostMapping(value = "findHadRegisterPage")
	public ResultBean<Page<Register>>  findHadRegisterPage(Register register,String userNameOrLoginCode,
														   @RequestParam(defaultValue = "10") int pageSize,
														   @RequestParam(defaultValue = "1") int pageNo ) {
		register.setPage(new Page<>(pageNo, pageSize));
		if (!StringUtils.isNotBlank(register.getApplyState())){
			register.setApplyState_in(new String[]{RegisterApplyState.UN_APPLY.value(),RegisterApplyState.PENDING.value(),RegisterApplyState.REJECT.value()});
		}
		register.selectRevokeType_is_null();
		register.selectUserNameOrLoginCode(userNameOrLoginCode);
		Page<Register> page = registerService.findPage(register);
		return success(page);
	}


	@ApiOperation(value = "注销账号")
	@PostMapping(value = "logoff")
	public ResultBean logoff(String userIds){
		String[] userIdArr = userIds.split(",");
		String handlerName = BaseUserUtils.getUser().getUserName();
		for (String studentId: userIdArr){
			Student student = new Student(studentId);
			studentService.phyDelete(student);
			RegisterUtils.revokeUser(student,handlerName, EventHandlingType.MANUAL);
		}
		return success();
	}



	@ApiOperation(value = "分页查询使用中列表数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userNameOrLoginCode", value = "登录ID或姓名"),
			@ApiImplicitParam(name = "classes.className", value = "班级名称"),
			@ApiImplicitParam(name = "teacher.teacherName", value = "教师名称"),
			@ApiImplicitParam(name = "state", value = "账号状态"),
            @ApiImplicitParam(name = "registerTime_lte", value = "注册时间。小于"),
            @ApiImplicitParam(name = "registerTime_gte", value = "注册时间。大于"),
	})
	@PostMapping(value = "findInUseRegisterPage")
	public ResultBean<Page<Register>>  findInUseRegisterPage(Register register,String userNameOrLoginCode,
														   @RequestParam(defaultValue = "10") int pageSize,
														   @RequestParam(defaultValue = "1") int pageNo ) {
		register.setPage(new Page<>(pageNo, pageSize));
		register.setApplyState(RegisterApplyState.AGREE.value());
		register.selectUserNameOrLoginCode(userNameOrLoginCode);
		register.selectRevokeType_is_null();
		Page<Register> page = registerService.findInUseRegisterPage(register);
		return success(page);
	}


	@ApiOperation(value = "分页查询已注销列表数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userNameOrLoginCode", value = "登录ID或姓名"),
			@ApiImplicitParam(name = "classes.className", value = "班级名称"),
			@ApiImplicitParam(name = "teacher.teacherName", value = "教师名称"),
			@ApiImplicitParam(name = "handlerName", value = "执行人"),
			@ApiImplicitParam(name = "registerTime_lte", value = "注册时间。小于"),
			@ApiImplicitParam(name = "registerTime_gte", value = "注册时间。大于"),
			@ApiImplicitParam(name = "revokeTime_lte", value = "注销时间。小于"),
			@ApiImplicitParam(name = "revokeTime_gte", value = "注销时间。大于"),
	})
	@PostMapping(value = "findHadRevokeRegisterPage")
	public ResultBean<Page<Register>>  findHadRevokeRegisterPage(Register register,String userNameOrLoginCode,
															 @RequestParam(defaultValue = "10") int pageSize,
															 @RequestParam(defaultValue = "1") int pageNo ) {
		register.setPage(new Page<>(pageNo, pageSize));
		register.selectRevokeType_is_not_null();
		register.selectUserNameOrLoginCode(userNameOrLoginCode);
		Page<Register> page = registerService.findPage(register);
		return success(page);
	}


}