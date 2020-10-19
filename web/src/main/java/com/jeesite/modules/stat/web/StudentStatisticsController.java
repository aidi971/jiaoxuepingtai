//package com.jeesite.modules.stat.web;
//
//import com.jeesite.common.collect.ListUtils;
//import com.jeesite.common.collect.MapUtils;
//import com.jeesite.common.entity.ResultBean;
//import com.jeesite.common.web.BaseApiController;
//import com.jeesite.modules.stat.entity.DiscussStatVo;
//import com.langheng.modules.ass.entity.SignIn;
//import com.langheng.modules.ass.service.SignInService;
//import com.langheng.modules.base.entity.Classes;
//import com.langheng.modules.base.entity.LoginLog;
//import com.langheng.modules.base.service.LoginLogService;
//import com.langheng.modules.ed.entity.TeachingClass;
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
//@Api(description = "学生的，数据统计相关接口")
//@RestController
//@RequestMapping(value = "admin/student/statistics")
//public class StudentStatisticsController extends BaseApiController {
//
//    @Autowired
//    LoginLogService loginLogService;
//    @Autowired
//    private TeachingClassService teachingClassService;
//    @Autowired
//    private SignInService signInService;
//
//    @ApiOperation(value = "统计访问次数-登录次数")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
//            @ApiImplicitParam(name = "studentId", value = "学生id", required = true),
//    })
//    @PostMapping(value = "loginStatistics")
//    public ResultBean loginStatistics(@RequestParam String teachingClassId,
//                                      @RequestParam String studentId){
//        TeachingClass teachingClass = teachingClassService.get(teachingClassId);
//        Classes classes = teachingClass.getClasses();
//        LoginLog loginLogCriteria = new LoginLog();
//        loginLogCriteria.setClassesId(teachingClass.getTeachingClassId());
//        Long totalCount = loginLogService.findCount(loginLogCriteria);
//
//        SignIn signIn = new SignIn();
//        signIn.setTeachingClassId(teachingClass.getTeachingClassId());
//        Long totalSignCount = signInService.findCount(signIn);
//        if (totalSignCount == 0){
//            totalSignCount = 1l;
//        }
//        signIn.setUserId(studentId);
//        Long userSignCount = signInService.findUserHadSignCount(signIn);
//
//        Map everyMountMap = MapUtils.newHashMap();
//        List<Map<String,Object>> everyMouthList = loginLogService.everyMouthStatByUser(studentId);
//        for (Map<String,Object> itemMap: everyMouthList){
//            everyMountMap.put(itemMap.get("loginMouth"),itemMap.get("loginNum"));
//        }
//
//        Map<String,Object> resultMap = MapUtils.newHashMap();
//        resultMap.put("totalCount",totalCount);
//        resultMap.put("signRate",userSignCount/totalSignCount.doubleValue());
//        resultMap.put("everyMountMap",everyMountMap);
//
//        return success(resultMap);
//    }
//
//
//    @ApiOperation(value = "章节进度")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
//            @ApiImplicitParam(name = "studentId", value = "学生id", required = true),
//    })
//    @PostMapping(value = "chapterProgress")
//    public ResultBean chapterProgress(@RequestParam String teachingClassId){
//        Map<String,Double> chapterProgressMap = MapUtils.newHashMap();
//        chapterProgressMap.put("第一章",0.13);
//        chapterProgressMap.put("第二章",0.23);
//        chapterProgressMap.put("第三章",0.53);
//        return success(chapterProgressMap);
//    }
//
//    @ApiOperation(value = "点名统计")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
//            @ApiImplicitParam(name = "studentId", value = "学生id", required = true),
//    })
//    @PostMapping(value = "nominateStatistics")
//    public ResultBean nominateStatistics(@RequestParam String teachingClassId){
//        Map<String,Object> nominateStatisticsMap = MapUtils.newHashMap();
//        nominateStatisticsMap.put("firstNominate","2020年8月2日11:08");
//        nominateStatisticsMap.put("totalNominate",4);
//        nominateStatisticsMap.put("nominateRate",0.23);
//        return success(nominateStatisticsMap);
//    }
//
//    @ApiOperation(value = "讨论统计")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
//            @ApiImplicitParam(name = "studentId", value = "学生id", required = true),
//    })
//    @PostMapping(value = "discussStatistics")
//    public ResultBean discussStatistics(@RequestParam String teachingClassId){
//        List<DiscussStatVo> discussStatVoList = ListUtils.newArrayList();
//        discussStatVoList.add(new DiscussStatVo("第一章",12));
//        discussStatVoList.add(new DiscussStatVo("第二章",42));
//        discussStatVoList.add(new DiscussStatVo("第三章",52));
//        discussStatVoList.add(new DiscussStatVo("第四章",82));
//
//        Map<String,Object> discussStatisticsMap = MapUtils.newHashMap();
//        discussStatisticsMap.put("joinDiscussCount",36);
//        discussStatisticsMap.put("totalWordCount",1240);
//        discussStatisticsMap.put("discussChapterList",discussStatVoList);
//
//        discussStatisticsMap.put("mostLessonTaskName","XXXX资源");
//        discussStatisticsMap.put("mostCount",20);
//        return success(discussStatisticsMap);
//    }
//}
