package com.langheng.modules.ed.util;

import com.jeesite.common.cache.CacheUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.utils.SpringUtils;
import com.langheng.modules.ed.entity.Chapter;
import com.langheng.modules.ed.entity.Course;
import com.langheng.modules.ed.entity.Lesson;
import com.langheng.modules.ed.service.ChapterService;
import com.langheng.modules.ed.service.CommonService;
import com.langheng.modules.ed.service.LessonService;

import java.util.List;
import java.util.Map;

public class CommonUtils {
    // 课程展开
    public static final String CACHE_COURSE_STRUCT = "course_struct";
    public static final String CACHE_COURSE_TASK_NUM = "course_task_num";


    /**
     * 静态内部类，延迟加载，懒汉式，线程安全的单例模式
     */
    private static final class Static {
        private static CommonService commonService = SpringUtils.getBean(CommonService.class);
        private static LessonService lessonService = SpringUtils.getBean(LessonService.class);
        private static ChapterService chapterService = SpringUtils.getBean(ChapterService.class);
    }

    public static List<Chapter> getCourseStruct(Course course){
        List<Chapter> chapterList = CacheUtils.get(CACHE_COURSE_STRUCT,course.getCourseId());
        if (chapterList == null){
            cacheCourseStructAndLessonTaskNumMap(course);
        }
        return CacheUtils.get(CACHE_COURSE_STRUCT,course.getCourseId());
    }

    public static Map<String,Integer> getLessonTaskNumMap(Course course){
        Map<String,Integer> lessonTaskNumMap = CacheUtils.get(CACHE_COURSE_TASK_NUM,course.getCourseId());
        if (lessonTaskNumMap == null){
            cacheCourseStructAndLessonTaskNumMap(course);
        }
        return CacheUtils.get(CACHE_COURSE_TASK_NUM,course.getCourseId());
    }

    /**
     * 赋值任务序号
     * @param course
     * @param lessonList
     * @return
     */
    public static List<Lesson> buildLessonTaskNum(Course course,List<Lesson> lessonList){
        Map<String,Integer> lessonTaskNumMap = getLessonTaskNumMap(course);
        if (!lessonTaskNumMap.isEmpty() && !lessonList.isEmpty()){
            lessonList.forEach(lesson -> {
                if (!lesson.getSubLessonList().isEmpty()){
                    lesson.getSubLessonList().forEach(subLesson->{
                        subLesson.setTaskNum(lessonTaskNumMap.get(subLesson.getLessonId()));
                    });
                }
                lesson.setTaskNum(lessonTaskNumMap.get(lesson.getLessonId()));
            });
        }
        return lessonList;
    }

    /**
     * 赋值任务序号
     * @return
     */
    public static Integer getTaskNum(String courseId,String lessonId){
        Map<String,Integer> lessonTaskNumMap = getLessonTaskNumMap(new Course(courseId));
        return lessonTaskNumMap.get(lessonId);
    }

    private static void  cacheCourseStructAndLessonTaskNumMap(Course course){
        Map<String,Integer> lessonTaskNumMap = MapUtils.newLinkedHashMap();
        List<Chapter> chapterList = Static.commonService.expandByCourse(course);
        Integer lessonTaskNum = 1;
        for (Chapter chapter: chapterList){
            for (Lesson lesson : chapter.getLessonList()){
                if (lesson.isLesson()){
                    List<Lesson> subLessonList = lesson.getSubLessonList();
                    if (subLessonList.isEmpty()){
                        lessonTaskNumMap.put(lesson.getLessonId(),lessonTaskNum);
                        lessonTaskNum++;
                    }else{
                        for (Lesson subLesson: subLessonList){
                            lessonTaskNumMap.put(subLesson.getLessonId(),lessonTaskNum);
                            lessonTaskNum++;
                        }
                    }
                }
                if (lesson.isSubLesson()){
                    lessonTaskNumMap.put(lesson.getLessonId(),lessonTaskNum);
                    lessonTaskNum++;
                }
            }
        }

        CacheUtils.put(CACHE_COURSE_STRUCT,course.getCourseId(),chapterList);
        CacheUtils.put(CACHE_COURSE_TASK_NUM,course.getCourseId(),lessonTaskNumMap);
    }


    public static void clearCache(Course course){
        CacheUtils.remove(CACHE_COURSE_STRUCT,course.getCourseId());
        CacheUtils.remove(CACHE_COURSE_TASK_NUM,course.getCourseId());
    }

    public static void clearCache(Lesson lesson){
        Course course = null;
        if (lesson.isSubLesson()){
            Lesson parentLesson = Static.lessonService.get(lesson.getParentLesson());
            Chapter chapter = Static.chapterService.get(parentLesson.getChapter());
            course = chapter.getCourse();
        }
        if (lesson.isLesson()){
            Chapter chapter = Static.chapterService.get(lesson.getChapter());
            course = chapter.getCourse();
        }

        if (course!=null){
            clearCache(course);
        }
    }

    /**
     * 获取小节全名
     * @param lesson
     * @return
     */
    public static String getFullLessonName(Lesson lesson) {
        return lesson.getName();
    }

}
