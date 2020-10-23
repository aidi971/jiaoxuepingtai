//package com.jeesite.update;
//
//import com.jeesite.common.lang.StringUtils;
//import com.jeesite.modules.Application;
//import com.langheng.modules.ed.entity.Lesson;
//import com.langheng.modules.ed.entity.LessonTask;
//import com.langheng.modules.ed.service.LessonService;
//import com.langheng.modules.ed.service.LessonTaskService;
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
//public class Update0511 {
//
//    @Autowired
//    private LessonTaskService lessonTaskService;
//    @Autowired
//    private LessonService lessonService;
//
//    /**
//     * 为lessonTask 添加chapterId
//     */
//    @Test
//    public void addChapterIdToLessonTask(){
//        List<LessonTask> lessonTaskList = lessonTaskService.findList(new LessonTask());
//
//        lessonTaskList.forEach(lessonTask -> {
//            if (StringUtils.isNotBlank(lessonTask.getLessonId())){
//                Lesson lesson = lessonService.get(lessonTask.getLessonId());
//                if (lesson == null) return;
//                if (lesson.isLesson()){
//                    lessonTask.setChapterId(lesson.getChapter().getChapterId());
//                }else if (lesson.isSubLesson()){
//                    Lesson parentLesson = lessonService.get(lesson.getParentLesson());
//                    if (parentLesson.getChapter() != null){
//                        lessonTask.setChapterId(parentLesson.getChapter().getChapterId());
//                    }
//                }
//               try{
//                   lessonTaskService.save(lessonTask);
//               }catch (Exception e){
//
//               }
//            }
//        });
//
//    }
//
//}
