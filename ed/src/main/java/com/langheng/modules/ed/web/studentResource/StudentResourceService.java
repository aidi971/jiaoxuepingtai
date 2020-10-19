package com.langheng.modules.ed.web.studentResource;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.framework.uitls.EntityUtils;
import com.langheng.modules.base.entity.BaseUser;
import com.langheng.modules.base.utils.BaseUserUtils;
import com.langheng.modules.ed.entity.Chapter;
import com.langheng.modules.ed.entity.Course;
import com.langheng.modules.ed.entity.Lesson;
import com.langheng.modules.ed.entity.LessonTask;
import com.langheng.modules.ed.enumn.LessonTaskType;
import com.langheng.modules.ed.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly=true)
public class StudentResourceService {
    @Autowired
    private CourseService courseService;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private LessonTaskService lessonTaskService;

    /**
     * 章节数据格式化，让其找到对应的节点
     * @param chapterList
     * @param lessonList
     * @param subLessonList
     * @param lessonTaskList
     */
    public void buildStructChapter(List<Chapter> chapterList, List<Lesson> lessonList,
                                   List<Lesson> subLessonList, List<LessonTask> lessonTaskList ){
        // 结构化 ‘节’
        for (Lesson lesson : lessonList){
            for (Chapter chapter : chapterList){
                if (EntityUtils.equalsBaseEntityId(lesson.getChapter(),chapter)){
                    chapter.getLessonList().add(lesson);
                }
            }
            for (LessonTask lessonTask : lessonTaskList){
                if (lesson.getId().equals(lessonTask.getLessonId())){
                    lesson.getLessonTaskList().add(lessonTask);
                }
            }
        }

        // 结构化小节
        for (Lesson subLesson : subLessonList){
            for (Lesson lesson : lessonList){
                if (EntityUtils.equalsBaseEntityId(subLesson.getParentLesson(),lesson)){
                    lesson.getSubLessonList().add(subLesson);
                }
            }
            for (LessonTask lessonTask : lessonTaskList){
                if (subLesson.getId().equals(lessonTask.getLessonId())){
                    subLesson.getLessonTaskList().add(lessonTask);
                }
            }
        }
    }
    /**
     * 查看已推送资源结构
     * @param course
     * @return
     */
    public List<Chapter> HadPushLessonTaskStruct(Course course, String teachingClassId) {
        Chapter chapterCriteria = new Chapter();
        chapterCriteria.setCourse(course);
        chapterCriteria.setTeachingClassId(teachingClassId);
        List<Chapter> chapterList = chapterService.findList(chapterCriteria);

        Lesson lessonCriteria = new Lesson();
        Chapter lcCriteria = new Chapter();
        lcCriteria.setId_in(EntityUtils.getBaseEntityIds(chapterList));
        lessonCriteria.setChapter(lcCriteria);
        lessonCriteria.setTeachingClassId(teachingClassId);
        List<Lesson> lessonList = lessonService.findListHadPush(lessonCriteria);

        Lesson subLessonCriteria = new Lesson();
        subLessonCriteria.setTeachingClassId(teachingClassId);
        subLessonCriteria.setParentLessonId_in(EntityUtils.getBaseEntityIds(lessonList));
        List<Lesson> subLessonList = lessonService.findListHadPush(subLessonCriteria);

        // 获取所有‘节’或者小节的资源
        List<Lesson> allLessons = ListUtils.newArrayList();
        allLessons.addAll(lessonList); allLessons.addAll(subLessonList);
        BaseUser user = BaseUserUtils.getUser();
        LessonTask lessonTaskCriteria = new LessonTask();
        //课件视频类型
        lessonTaskCriteria.setType_in(new String[]{LessonTaskType.VIDEO.value(),LessonTaskType.COURSEWARE.value(),LessonTaskType.CORRECT.value()});
        lessonTaskCriteria.setTeachingClassId(teachingClassId);
        lessonTaskCriteria.selectIsFinishState(user.getId(),teachingClassId);
        lessonTaskCriteria.getSqlMap().getOrder().setOrderBy("finishState ASC,a.task_num DESC");
        // 右联接 查询已经推送的任务
        lessonTaskCriteria.setLessonId_in(EntityUtils.getBaseEntityIds(allLessons));
        List<LessonTask> lessonTaskList = lessonTaskService.findListHadPush(lessonTaskCriteria);

        // 结构化 ‘节’
        buildStructChapter(chapterList,lessonList,subLessonList,lessonTaskList);

        // 统计数量
        course.setHadPushLessonNum(allLessons.size());

        List<Chapter> hadPushChapterList = ListUtils.newArrayList();

        chapterList.forEach(chapter -> {
            if (!chapter.getLessonList().isEmpty()){
                hadPushChapterList.add(chapter);
            }
        });

        return hadPushChapterList;
    }

//    public List<Chapter> StudentFindCourseType(Course course, String teachingClassId, LessonTask lessonTask) {
//        Chapter chapterCriteria = new Chapter();
//        chapterCriteria.setCourse(course);
//        chapterCriteria.setTeachingClassId(teachingClassId);
//        List<Chapter> chapterList = chapterService.findList(chapterCriteria);
//
//        Lesson lessonCriteria = new Lesson();
//        Chapter lcCriteria = new Chapter();
//        lcCriteria.setId_in(EntityUtils.getBaseEntityIds(chapterList));
//        lessonCriteria.setChapter(lcCriteria);
//        lessonCriteria.setTeachingClassId(teachingClassId);
//        List<Lesson> lessonList = lessonService.findListHadPush(lessonCriteria);
//
//        Lesson subLessonCriteria = new Lesson();
//        subLessonCriteria.setTeachingClassId(teachingClassId);
//        subLessonCriteria.setParentLessonId_in(EntityUtils.getBaseEntityIds(lessonList));
//        List<Lesson> subLessonList = lessonService.findListHadPush(subLessonCriteria);
//
//        // 获取所有‘节’或者小节的资源
//        List<Lesson> allLessons = ListUtils.newArrayList();
//        allLessons.addAll(lessonList); allLessons.addAll(subLessonList);
//
//        //课件视频类型
////        lessonTaskCriteria.setType_in(new String[]{LessonTaskType.VIDEO.value(),LessonTaskType.COURSEWARE.value()});
//        lessonTask.setTeachingClassId(teachingClassId);
//        // 右联接 查询已经推送的任务
//        lessonTask.setLessonId_in(EntityUtils.getBaseEntityIds(allLessons));
//        List<LessonTask> lessonTaskList = lessonTaskService.findListHadPush(lessonTask);
//
//        // 结构化 ‘节’
//        buildStructChapter(chapterList,lessonList,subLessonList,lessonTaskList);
//
//        // 统计数量
//        course.setHadPushLessonNum(allLessons.size());
//
//        List<Chapter> hadPushChapterList = ListUtils.newArrayList();
//
//        chapterList.forEach(chapter -> {
//            if (!chapter.getLessonList().isEmpty()){
//                hadPushChapterList.add(chapter);
//            }
//        });
//
//        return hadPushChapterList;
//    }
}
