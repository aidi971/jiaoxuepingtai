/**
 * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
 */
package com.langheng.modules.base.web;

import com.jeesite.common.entity.Page;
import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseApiController;
import com.jeesite.framework.uitls.EntityUtils;
import com.langheng.modules.base.entity.BaseUser;
import com.langheng.modules.base.entity.Classes;
import com.langheng.modules.base.entity.LoginLog;
import com.langheng.modules.base.entity.Teacher;
import com.langheng.modules.base.service.ClassesService;
import com.langheng.modules.base.service.LoginLogService;
import com.langheng.modules.base.utils.BaseUserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 系统登录日志Controller
 * @author xiaoxie
 * @version 2020-02-17
 */
@Api(description = "系统登录日志")
@RestController
@RequestMapping(value = "admin/loginLog")
public class LoginLogController extends BaseApiController {

	@Autowired
	private LoginLogService loginLogService;
	@Autowired
	private ClassesService classesService;
	
	/**
	 * 获取系统登录日志数据
	 */
	@ModelAttribute
	public LoginLog get(String infoId, boolean isNewRecord) {
		return loginLogService.get(infoId, isNewRecord);
	}
	
 	@ApiOperation(value = "分页查询系统登录日志列表数据")
	@PostMapping(value = "findPage")
	public ResultBean<Page<LoginLog>>  findPage(LoginLog loginLog,
								@RequestParam(defaultValue = "10") int pageSize,
                                @RequestParam(defaultValue = "1") int pageNo ) {
		loginLog.setPage(new Page<>(pageNo, pageSize));
		loginLog.setLoginTime_gle(DateUtils.addDays(new Date(),-7));
		Page<LoginLog> page = loginLogService.findPage(loginLog);
		return success(page);
	}

	@ApiOperation(value = "分页查询我的登录日志列表数据")
	@PostMapping(value = "findMyPage")
	public ResultBean<Page<LoginLog>>  findMyPage(LoginLog loginLog,
												@RequestParam(defaultValue = "10") int pageSize,
												@RequestParam(defaultValue = "1") int pageNo ) {
		loginLog.setPage(new Page<>(pageNo, pageSize));
		loginLog.setUser(new BaseUser(BaseUserUtils.getUser().getUserCode()));
		loginLog.setLoginTime_gle(DateUtils.addDays(new Date(),-7));
		Page<LoginLog> page = loginLogService.findPage(loginLog);
		return success(page);
	}


	@ApiOperation(value = "教师端-分页查询学生的登录日志列表数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userNameOrLoginCode", value = "登录ID或姓名"),
			@ApiImplicitParam(name = "classesId", value = "班级id"),
	})
	@PostMapping(value = "findMyStudentPage")
	public ResultBean<Page<LoginLog>>  findMyStudentPage(LoginLog loginLog, String userNameOrLoginCode,
												@RequestParam(defaultValue = "10") int pageSize,
												@RequestParam(defaultValue = "1") int pageNo ) {

		if (StringUtils.isNotBlank(loginLog.getClassesId())){
			loginLog.selectClassesId(loginLog.getClassesId());
		}else {
			Classes classesCriteria = new Classes();
			classesCriteria.setTeacher(new Teacher(BaseUserUtils.getUser().getUserCode()));
			List<Classes> myClassesList = classesService.findList(classesCriteria);
			loginLog.setClassesId_in(EntityUtils.getBaseEntityIds(myClassesList));
		}
		loginLog.setPage(new Page<>(pageNo, pageSize));
		loginLog.setLoginTime_gle(DateUtils.addDays(new Date(),-7));
		loginLog.selectUserNameOrLoginCode(userNameOrLoginCode);
		Page<LoginLog> page = loginLogService.findPageJoinClasses(loginLog);
		return success(page);
	}

}