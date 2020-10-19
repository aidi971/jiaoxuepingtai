package com.langheng.modules.ed.util;

import com.jeesite.common.cache.CacheUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.SpringUtils;
import com.langheng.modules.ed.entity.*;
import com.langheng.modules.ed.service.CommonService;
import com.langheng.modules.ed.service.CourseService;
import com.langheng.modules.ed.service.TeachingClassService;
import com.langheng.modules.ed.service.UserTaskService;

import java.util.List;

public class TeachingClassUtils {
    // 课程展开
    public static final String TEACHING_CLASS = "teaching_class";

    private static TeachingClassService teachingClassService = SpringUtils.getBean(TeachingClassService.class);
    private static CourseService courseService = SpringUtils.getBean(CourseService.class);
    private static UserTaskService userTaskService = SpringUtils.getBean(UserTaskService.class);
    private static CommonService commonService = SpringUtils.getBean(CommonService.class);

    public static List<StudentVo> getStudentProgress(String teachingClassId) {
        TeachingClass teachingClass = teachingClassService.get(teachingClassId);

        List<StudentVo> studentVoList = CacheUtils.get("teaching_class_student_progress",teachingClassId);

        if (studentVoList != null){
            return studentVoList;
        }

        Course course = courseService.get(teachingClass.getCourse());
        List<Chapter> chapterList = commonService.HadPushLessonTaskStruct(course,teachingClassId);

        // 最新章
        Chapter newChapter = new Chapter();
        // 最新章的总资源数
        Integer newChapterLessonTaskNum = 0;

        if (! chapterList.isEmpty()){
            // 最新章
            newChapter = chapterList.get(chapterList.size() -1);
            // 最新章的总资源数
            newChapterLessonTaskNum = getLessonTaskNum(newChapter);
        }

        Integer hadPushLessonTaskNum = course.getHadPushLessonTaskNum();

        UserTaskDto userTaskDto = new UserTaskDto();
        userTaskDto.setTeachingClassId(teachingClassId);
        userTaskDto.setChapterId(newChapter.getChapterId());
        List<StudentVo> studentVos = userTaskService.findStudentProgress(userTaskDto);

        for (int index= 0 ;index< studentVos.size(); index++){
            StudentVo studentVo = studentVos.get(index);
            studentVo.setOrder(index + 1);
            studentVo.setLessonTaskNum(hadPushLessonTaskNum);
            studentVo.setTotalStuNum(studentVos.size());
            studentVo.setNewChapterLessonTaskNum(newChapterLessonTaskNum);
            if (studentVo.getGrossScore() == null){
                studentVo.setGrossScore(0f);
            }
        }

        CacheUtils.put("teaching_class_student_progress",teachingClassId,studentVos);

        return studentVos;
    }


    public static Integer getLessonTaskNum(Chapter chapter){
        Integer lessonTaskNum = 0;

        for(Lesson lesson : chapter.getLessonList()){
            lessonTaskNum += lesson.getLessonTaskList().size();
            for (Lesson subLesson: lesson.getSubLessonList()){
                lessonTaskNum += subLesson.getLessonTaskList().size();
            }
        }
        return lessonTaskNum;
    }


    /**
     * 静态内部类，延迟加载，懒汉式，线程安全的单例模式
     */
    private static final class Static {
        private static TeachingClassService teachingClassService = SpringUtils.getBean(TeachingClassService.class);
        private static CourseService courseService = SpringUtils.getBean(CourseService.class);
    }

    public static TeachingClass getTeachingClass(String teachingClassId) {
        TeachingClass teachingClass = CacheUtils.get(TEACHING_CLASS,teachingClassId);
        if (teachingClass == null){
            teachingClass = Static.teachingClassService.get(teachingClassId);
            CacheUtils.put(TEACHING_CLASS,teachingClassId,teachingClass);
        }
        return teachingClass;
    }

    public static void clearCache(String teachingClassId){
        CacheUtils.remove(TEACHING_CLASS,teachingClassId);
    }

    public static void clearCacheByCourse(String courseId){
        if (StringUtils.isNotBlank(courseId)){
            TeachingClass teachingClassCriteria = new TeachingClass();
            teachingClassCriteria.setCourse(new Course(courseId));
            List<TeachingClass> teachingClassList = Static.teachingClassService.findList(teachingClassCriteria);
            teachingClassList.forEach(teachingClass->{
                CacheUtils.remove(TEACHING_CLASS,teachingClass.getTeachingClassId());
            });
        }
    }
}
