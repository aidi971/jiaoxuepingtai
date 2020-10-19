package com.langheng.modules.ed.web.studentResource;

import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.ed.entity.Chapter;
import com.langheng.modules.ed.entity.Course;
import com.langheng.modules.ed.entity.TeachingClass;
import com.langheng.modules.ed.service.CourseService;
import com.langheng.modules.ed.service.TeachingClassService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(description = "学生端资源库")
@RestController
@RequestMapping(value = "api/resourcePoolStudent")
public class resourcePoolStudent extends BaseApiController {
    @Autowired
    private TeachingClassService teachingClassService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private StudentResourceService studentResourceService;

    @ApiOperation(value = "学生端-资源库（章节展示）")
    @PostMapping(value = "studentResource")
    @ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true)
    public ResultBean<List<Chapter>> HadPushLessonTaskStruct(@RequestParam String teachingClassId) {
        TeachingClass teachingClass = teachingClassService.get(teachingClassId);
        Course course = courseService.get(teachingClass.getCourse());
        List<Chapter> chapters = studentResourceService.HadPushLessonTaskStruct(course, teachingClassId);
        return success(chapters,"获取成功！");
    }

//    @ApiOperation(value = "学生端-资源库（类别展示）")
//    @PostMapping(value = "studentFindCourseType")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "teachingClassId", value = "课堂id", required = true),
//            @ApiImplicitParam(name = "type", value = "类型", required = true),
//            @ApiImplicitParam(name = "fileNameOrFileSer", value = "搜索", required = true,defaultValue = ""),
//    })
//    public ResultBean<List<Chapter>> studentFindCourseType( @RequestParam(value = "teachingClassId") String teachingClassId,
//                                             @RequestParam(value = "type 0课件，1视频 |ed_lesson_task_type") String type,
//                                             @RequestParam(value = "fileNameOrFileSer") String fileNameOrFileSer) {
//        BaseUser user = BaseUserUtils.getUser();
//        TeachingClass teachingClass = teachingClassService.get(teachingClassId);
//        Course course = courseService.get(teachingClass.getCourse());
//        LessonTask lessonTask = new LessonTask();
//        lessonTask.selectIsFinishState(user.getId(),teachingClassId);
//        if (StringUtils.isBlank(type)) {
//            lessonTask.setType_in(new String[]{LessonTaskType.VIDEO.value(),LessonTaskType.COURSEWARE.value(),LessonTaskType.CORRECT.value()});
//        }else {
//            lessonTask.setType(type);
//        }
//        if (StringUtils.isNotBlank(fileNameOrFileSer)) {
//            lessonTask.setFileNameOrFileSer(fileNameOrFileSer);
//        }
//        lessonTask.setFileNameOrFileSer(fileNameOrFileSer);
//        List<Chapter> chapters = studentResourceService.StudentFindCourseType(course, teachingClassId,lessonTask);
//        return success(chapters,"获取成功！");
//    }
}
