///**
// * Copyright (c) 2019-Now http://jeesite.com All rights reserved.
// */
//package com.jeesite.modules.stat.web;
//
//import com.jeesite.common.collect.ListUtils;
//import com.jeesite.common.collect.MapUtils;
//import com.jeesite.common.entity.ResultBean;
//import com.jeesite.common.web.BaseApiController;
//import com.jeesite.modules.stat.entity.DiscussStatVo;
//import com.jeesite.modules.stat.entity.StuLoginVo;
//import com.langheng.modules.ass.service.SignInService;
//import com.langheng.modules.base.entity.Classes;
//import com.langheng.modules.base.service.LoginLogService;
//import com.langheng.modules.ed.entity.TeachingClass;
//import com.langheng.modules.ed.service.CourseService;
//import com.langheng.modules.ed.service.TeachingClassService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * 课堂Controller
// * @author xiaoxie
// * @version 2020-02-11
// */
//@Api(description = "课堂的-统计管理")
//@RestController
//@RequestMapping(value = "admin/teachClass/statistics")
//public class TeachClassStatisticsController extends BaseApiController {
//
//	@Autowired
//	private TeachingClassService teachingClassService;
//	@Autowired
//	private CourseService courseService;
//	@Autowired
//	LoginLogService loginLogService;
//	@Autowired
//	private SignInService signInService;
//
//	@ApiOperation(value = "统计访问次数-登录次数")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
//	})
//	@PostMapping(value = "loginStatistics")
//	public ResultBean loginStatistics(@RequestParam String teachingClassId){
//		TeachingClass teachingClass = teachingClassService.get(teachingClassId);
//		Classes classes = teachingClass.getClasses();
//
//		Map everyMountMap = MapUtils.newHashMap();
//		List<Map<String,Object>> everyMouthList = loginLogService.everyMouthStatByClasses(classes.getClassesId());
//		for (Map<String,Object> itemMap: everyMouthList){
//			everyMountMap.put(itemMap.get("loginMouth"),itemMap.get("loginNum"));
//		}
//
//		List<StuLoginVo> stuLoginVoList = ListUtils.newArrayList();
//		stuLoginVoList.add(new StuLoginVo("林同学",13));
//		stuLoginVoList.add(new StuLoginVo("刘同学",53));
//		stuLoginVoList.add(new StuLoginVo("张同学",76));
//		stuLoginVoList.add(new StuLoginVo("黄同学",176));
//
//		Map<String,Object> resultMap = MapUtils.newHashMap();
//		resultMap.put("everyMountMap",everyMountMap);
//		resultMap.put("stuRankingList",stuLoginVoList);
//
//		return success(resultMap);
//	}
//
//
//	@ApiOperation(value = "章节进度")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
//	})
//	@PostMapping(value = "chapterProgress")
//	public ResultBean chapterProgress(@RequestParam String teachingClassId){
//		Map<String,Double> chapterProgressMap = MapUtils.newHashMap();
//		chapterProgressMap.put("第一章",0.13);
//		chapterProgressMap.put("第二章",0.23);
//		chapterProgressMap.put("第三章",0.53);
//		chapterProgressMap.put("第四章",0.73);
//		return success(chapterProgressMap);
//	}
//
//
//	@ApiOperation(value = "讨论统计")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
//	})
//	@PostMapping(value = "discussStatistics")
//	public ResultBean discussStatistics(@RequestParam String teachingClassId){
//		List<DiscussStatVo> discussStatVoList = ListUtils.newArrayList();
//		discussStatVoList.add(new DiscussStatVo("第一章",112));
//		discussStatVoList.add(new DiscussStatVo("第二章",412));
//		discussStatVoList.add(new DiscussStatVo("第三章",512));
//		discussStatVoList.add(new DiscussStatVo("第四章",282));
//
//		Map<String,Object> discussStatisticsMap = MapUtils.newHashMap();
//		discussStatisticsMap.put("totalDiscussCount",306);
//		discussStatisticsMap.put("totalWordCount",12240);
//		discussStatisticsMap.put("discussChapterList",discussStatVoList);
//
//		discussStatisticsMap.put("mostLessonTaskName","XXXX资源");
//		discussStatisticsMap.put("mostCount",210);
//
//		discussStatisticsMap.put("mostStuName","家华");
//		discussStatisticsMap.put("mostStuCount",610);
//		return success(discussStatisticsMap);
//	}
//
//}