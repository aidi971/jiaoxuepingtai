///**
// * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
// */
//package com.jeesite.modules.stat.web;
//
//import com.jeesite.common.collect.MapUtils;
//import com.jeesite.common.entity.ResultBean;
//import com.jeesite.common.web.BaseApiController;
//import com.langheng.modules.ass.service.DiscussService;
//import com.langheng.modules.ass.service.SignInService;
//import com.langheng.modules.base.entity.BaseUser;
//import com.langheng.modules.base.entity.LoginLog;
//import com.langheng.modules.base.service.BaseUserService;
//import com.langheng.modules.base.service.LoginLogService;
//import com.langheng.modules.ed.service.CourseService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Map;
//
///**
// * 课堂Controller
// * @author xiaoxie
// * @version 2020-02-11
// */
//@Api(description = "平台的-统计管理")
//@RestController
//@RequestMapping(value = "admin/platform/statistics")
//public class PlatformStatisticsController extends BaseApiController {
//
//	@Autowired
//	private DiscussService discussService;
//	@Autowired
//	private CourseService courseService;
//	@Autowired
//	LoginLogService loginLogService;
//	@Autowired
//	private SignInService signInService;
//	@Autowired
//	private BaseUserService baseUserService;
//
//	@ApiOperation(value = "讨论的统计")
//	@PostMapping(value = "discussStatistics")
//	public ResultBean discussStatistics(){
//		Map<String,Object> nominateStatisticsMap = MapUtils.newHashMap();
//		nominateStatisticsMap.put("totalDiscuss",5201314);
//		return success(nominateStatisticsMap);
//	}
//
//	@ApiOperation(value = "访问量统计")
//	@PostMapping(value = "loginStatistics")
//	public ResultBean loginStatistics(){
//		LoginLog loginLog = new LoginLog();
//		Long loginCount = loginLogService.findCount(loginLog);
//
//		BaseUser baseUser = new BaseUser();
//		Long userCount = baseUserService.findCount(baseUser);
//
//		Map<String,Integer> monthLoginMap = MapUtils.newHashMap();
//		monthLoginMap.put("10月",12);
//		monthLoginMap.put("11月",22);
//		monthLoginMap.put("12月",52);
//		monthLoginMap.put("13月",92);
//		monthLoginMap.put("14月",34);
//
//
//		Map<String,Object> resultMap = MapUtils.newHashMap();
//		resultMap.put("loginCount",loginCount);
//		resultMap.put("userCount",userCount);
//		resultMap.put("monthLoginMap",monthLoginMap);
//		return success(resultMap);
//	}
//
//}