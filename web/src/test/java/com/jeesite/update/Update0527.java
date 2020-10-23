//package com.jeesite.update;
//
//import com.jeesite.modules.Application;
//import com.langheng.modules.ed.entity.Course;
//import com.langheng.modules.ed.entity.Template;
//import com.langheng.modules.ed.service.CourseService;
//import com.langheng.modules.ed.service.TemplateService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.List;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes= Application.class)
//@Rollback(false)
//public class Update0527 {
//
//    @Autowired
//    private CourseService courseService;
//    @Autowired
//    private TemplateService templateService;
//
//    /**
//     * 为lessonTask 添加chapterId
//     */
//    @Test
//    public void adjustLessonTaskNum(){
//
//        List<Course> courseList = courseService.findList(new Course());
//
//        courseList.forEach(course -> {
//            try{
//                courseService.updateAllLessonTaskNum(course.getCourseId());
//            }catch (Exception e){
//
//            }
//        });
//    }
//
//    @Test
//    public void adjustTemplateLessonTaskNum(){
//
//        List<Template> templateList = templateService.findList(new Template());
//        templateList.forEach(template -> {
//            try{
//                templateService.updateAllLessonTaskNum(template.getTemplateId());
//            }catch (Exception e){ }
//        });
//    }
//}
