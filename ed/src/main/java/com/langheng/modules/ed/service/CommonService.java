package com.langheng.modules.ed.service;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.enumn.SysYesNoEnum;
import com.jeesite.common.idgen.IdGen;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.framework.uitls.EntityUtils;
import com.langheng.modules.base.service.StudentClassesService;
import com.langheng.modules.base.utils.TeacherUtils;
import com.langheng.modules.ed.entity.*;
import com.langheng.modules.ed.enumn.StructureType;
import com.langheng.modules.ed.enumn.TemplateState;
import com.langheng.modules.ed.enumn.TemplateType;
import com.langheng.modules.ed.util.CourseUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ComonService
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-03-02 16:35
 * @Version 1.0
 */
@Service
@Transactional(readOnly=true)
public class CommonService {

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
    @Autowired
    private StudentClassesService studentClassesService;

    /**
     * 根据章节获取节（或小节）
     * @return
     */
    public List<Lesson> findLessonsByChapter(Chapter chapter){
        List<Lesson> allLessons = ListUtils.newArrayList();
        Lesson lessonCriteria = new Lesson();
        lessonCriteria.setChapter(chapter);
        List<Lesson> lessonList = lessonService.findList(lessonCriteria);

        Lesson subLessonCriteria = new Lesson();
        subLessonCriteria.setParentLessonId_in(EntityUtils.getBaseEntityIds(lessonList));
        List<Lesson> subLessonList = lessonService.findList(subLessonCriteria);

        allLessons.addAll(lessonList); allLessons.addAll(subLessonList);
        return allLessons;
    }

    /**
     * 根据课程获取任务
     * @return
     */
    public List<LessonTask> findLessonTasksByCourse(Course course){
        Chapter chapterCriteria = new Chapter();
        chapterCriteria.setCourse(course);
        List<Chapter> chapterList = chapterService.findList(chapterCriteria);

        Lesson lessonCriteria = new Lesson();
        Chapter lcCriteria = new Chapter();
        lcCriteria.setId_in(EntityUtils.getBaseEntityIds(chapterList));
        lessonCriteria.setChapter(lcCriteria);
        List<Lesson> lessonList = lessonService.findList(lessonCriteria);

        Lesson subLessonCriteria = new Lesson();
        subLessonCriteria.setParentLessonId_in(EntityUtils.getBaseEntityIds(lessonList));
        List<Lesson> subLessonList = lessonService.findList(subLessonCriteria);

        // 获取所有‘节’或者小节的资源
        List<Lesson> allLessons = ListUtils.newArrayList();
        allLessons.addAll(lessonList); allLessons.addAll(subLessonList);
        LessonTask lessonTaskCriteria = new LessonTask();
        lessonTaskCriteria.setLessonId_in(EntityUtils.getBaseEntityIds(allLessons));
        List<LessonTask> lessonTaskList = lessonTaskService.findList(lessonTaskCriteria);
        return lessonTaskList;
    }

    /**
     * 根据章节获取任务
     * @return
     */
    public List<LessonTask> findLessonTasksByChapter(Chapter chapter){
        List<LessonTask> lessonTaskList = ListUtils.newArrayList();
        List<Lesson> allLessons = findLessonsByChapter(chapter);
        if (!allLessons.isEmpty()){
            LessonTask lessonTaskCriteria = new LessonTask();
            lessonTaskCriteria.setLessonId_in(EntityUtils.getBaseEntityIds(allLessons));
            lessonTaskList = lessonTaskService.findList(lessonTaskCriteria);
        }

        return lessonTaskList;
    }

    /**
     * 根据节获取任务
     * @return
     */
    public List<LessonTask> findLessonTasksByLesson(Lesson lesson){
        Lesson subLessonCriteria = new Lesson();
        subLessonCriteria.setParentLesson(lesson);
        List<Lesson> subLessonList = lessonService.findList(subLessonCriteria);
        // 也查询 自己
        subLessonList.add(lesson);

        LessonTask lessonTaskCriteria = new LessonTask();
        lessonTaskCriteria.setLessonId_in(EntityUtils.getBaseEntityIds(subLessonList));
        List<LessonTask> lessonTaskList = lessonTaskService.findList(lessonTaskCriteria);
        return lessonTaskList;
    }

    /**
     * 根据小节获取任务
     * @return
     */
    public List<LessonTask> findLessonTasksBySubLesson(Lesson subLesson){
        LessonTask lessonTaskCriteria = new LessonTask();
        lessonTaskCriteria.setLessonId(subLesson.getLessonId());
        List<LessonTask> lessonTaskList = lessonTaskService.findList(lessonTaskCriteria);
        return lessonTaskList;
    }

    public List<Chapter> expandByCourse(Course course){
        if(course != null && StringUtils.isNotBlank(course.getId())){
            Chapter chapterCriteria = new Chapter();
            chapterCriteria.setCourse(course);
            List<Chapter> chapterList = chapterService.findList(chapterCriteria);
            return expand(chapterList);
        }
        return ListUtils.newArrayList();
    }

    public List<Chapter> expandByTemplate(Template template){
        if(template != null && StringUtils.isNotBlank(template.getId())){
            Chapter chapterCriteria = new Chapter();
            chapterCriteria.setTemplate(template);
            List<Chapter> chapterList = chapterService.findList(chapterCriteria);
            return expand(chapterList);
        }
        return ListUtils.newArrayList();
    }

    private List<Chapter> expand(List<Chapter> chapterList){
        Lesson lessonCriteria = new Lesson();
        Chapter chapterCriteria = new Chapter();
        chapterCriteria.setId_in(EntityUtils.getBaseEntityIds(chapterList));
        lessonCriteria.setChapter(chapterCriteria);
        List<Lesson> lessonList = lessonService.findList(lessonCriteria);

        Lesson subLessonCriteria = new Lesson();
        subLessonCriteria.setParentLessonId_in(EntityUtils.getBaseEntityIds(lessonList));
        List<Lesson> subLessonList = lessonService.findList(subLessonCriteria);

        // 获取所有‘节’或者小节的资源
        List<Lesson> allLessons = ListUtils.newArrayList();
        allLessons.addAll(lessonList); allLessons.addAll(subLessonList);
        LessonTask lessonTaskCriteria = new LessonTask();
        lessonTaskCriteria.setLessonId_in(EntityUtils.getBaseEntityIds(allLessons));
        List<LessonTask> lessonTaskList = lessonTaskService.findList(lessonTaskCriteria);

        buildStructChapter(chapterList,lessonList,subLessonList,lessonTaskList);
        return chapterList;
    }

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
     * 调整节前缀
     */
    private void adjustLessonSuffixName(Integer chapterNum,Lesson lesson){
        StringBuffer suffixNameBuffer = new StringBuffer();
        suffixNameBuffer.append(chapterNum).append(".");
        suffixNameBuffer.append(lesson.getLessonNum());
        lesson.setSuffixName(suffixNameBuffer.toString());
    }

    /**
     * 调整小节前缀
     * @param subLesson
     */
    private void adjustSubLessonSuffixName(String suffixName,Lesson subLesson){
        StringBuffer suffixNameBuffer = new StringBuffer();
        suffixNameBuffer.append(suffixName).append(".");
        suffixNameBuffer.append(subLesson.getLessonNum());
        subLesson.setSuffixName(suffixNameBuffer.toString());
    }

    /**
     * 课程另存
     * @param oldCourse
     */
    @Transactional(readOnly = false)
    public Course saveAsCourse(Course oldCourse){

        // 创建新的课程
        Course newCourse = new Course();
        BeanUtils.copyProperties(oldCourse,newCourse);
        newCourse.setId(IdGen.nextId());
        newCourse.setIsNewRecord(true);
        newCourse.setIterations("0");
        newCourse.setVersion(CourseUtils.getNextVersion(newCourse));
        courseService.save(newCourse);

        // 获取所有的章节
        Chapter chapterCriteria = new Chapter();
        chapterCriteria.setCourse(new Course(oldCourse.getId()));
        List<Chapter> chapterList = chapterService.findList(chapterCriteria);

        copyChapterEntire(chapterList,newCourse);

        return newCourse;

    }

    /**
     * 课程申请公开
     * @param course
     */
    @Transactional(readOnly = false)
    public void applyPublic(Course course) {

        Template template = new Template();
        template.setParentTemplate(course.getTemplate());
        template.setTeacher(course.getTeacher());
        template.setName(course.getName());
        template.setFullName(course.getFullName());
        template.setApplyTime(new Date());
        template.setType(TemplateType.PERSONAL.value());
        template.setIsPublic(SysYesNoEnum.NO.value());
        template.setIsEnabled(SysYesNoEnum.NO.value());
        template.setState(TemplateState.UN_AUDITED.value());
        template.setTraceSourceId(course.getId());
        template.setIntroductions(course.getIntroduce());
        template.setImgSrc(course.getImgSrc());
        template.setVersion(course.getVersion());
        template.setStructureType(StructureType.CHAPTER.value());
        // 暂不做处理
        template.setVideoNum(course.getVideoNum());
        template.setCoursewareNum(course.getCoursewareNum());
        template.setHomeworkNum(course.getHomeworkNum());
        template.setCiteNum(0);
        templateService.save(template);

        // 获取所有的章节
        Chapter chapterCriteria = new Chapter();
        chapterCriteria.setCourse(course);
        List<Chapter> chapterList = chapterService.findList(chapterCriteria);

        copyChapterEntire(chapterList,template);

    }

    /**
     * 模板引用（或者收藏）转存课程
     */
    @Transactional(readOnly = false)
    public Course templateSaveAsCourse(Template template) {

        Course newCourse = new Course();
        newCourse.setTeacher(TeacherUtils.getTeacher());
        newCourse.setTemplate(template);
        // 如果引用的是标准模板
        newCourse.setFromStandardTemplate(true);
        newCourse.setIterations(CourseUtils.getNewIterations(template.getId()));

        newCourse.setImgSrc(template.getImgSrc());
        newCourse.setIntroduce(template.getIntroductions());
        newCourse.setName(template.getName());
        newCourse.setVideoNum(template.getVideoNum());
        newCourse.setHomeworkNum(template.getHomeworkNum());
        newCourse.setCoursewareNum(template.getCoursewareNum());
        courseService.save(newCourse);

        //引用次数加一
        template.setCiteNum(template.getCiteNum() + 1);
        template.setIsEnabled(SysYesNoEnum.YES.value());
        templateService.save(template);

      // 获取所有的章节
        Chapter chapterCriteria = new Chapter();
        chapterCriteria.setTemplate(template);
        List<Chapter> chapterList = chapterService.findList(chapterCriteria);

        copyChapterEntire(chapterList,newCourse);

        return newCourse;
    }

    /**
     * 拷贝 章节，节，小节，资源
     * @param chapterList
     * @param parent
     */
    @Transactional(readOnly = false)
    public void copyChapterEntire(List<Chapter> chapterList,Object parent){

        //获取所有‘节’
        Lesson lessonCriteria = new Lesson();
        Chapter chapterCriteria = new Chapter();
        chapterCriteria.setId_in(EntityUtils.getBaseEntityIds(chapterList));
        lessonCriteria.setChapter(chapterCriteria);
        List<Lesson> lessonList = lessonService.findList(lessonCriteria);

        //获取所有‘小节’
        Lesson subLessonCriteria = new Lesson();
        subLessonCriteria.setParentLessonId_in(EntityUtils.getBaseEntityIds(lessonList));
        List<Lesson> subLessonList = lessonService.findList(subLessonCriteria);

        Map<String,String> chapterIdMapToNewChapterId = MapUtils.newHashMap();
        Map<String,String> lessonIdMapToNewLessonId = MapUtils.newHashMap();
        Map<String,String> subLessonIdMapToNewSubLessonId = MapUtils.newHashMap();
        Map<String,String> lessonIdMapToChapterId = MapUtils.newHashMap();

        // 复制来源
        String copyForm = new String();
        if (parent instanceof Course){
            copyForm = "Course";
        }else if (parent instanceof Template){
            copyForm = "Template";
        }

        // 将章节复制一份
        List<Chapter> newChapterList = ListUtils.newArrayList();
        for (Chapter chapter : chapterList){
            Chapter newChapter = new Chapter();
            BeanUtils.copyProperties(chapter,newChapter);
            if ("Course".equals(copyForm)){
                newChapter.setCourse((Course)parent);
                newChapter.setTemplate(null);
            }else if ("Template".equals(copyForm)){
                newChapter.setTemplate((Template)parent);
                newChapter.setCourse(null);
            }
            newChapter.setId(IdGen.nextId());
            newChapter.setIsNewRecord(true);

            chapterIdMapToNewChapterId.put(chapter.getId(),newChapter.getId());
            newChapterList.add(newChapter);
        }
        // 保存新的章节
        if (!newChapterList.isEmpty()){
            chapterService.insertBatch(newChapterList);
        }
        List<Lesson> newLessonList = ListUtils.newArrayList();
        // 将章节旗下的‘节’，关联到新的章节
        for (Lesson lesson : lessonList){
            Lesson newLesson = new Lesson();
            BeanUtils.copyProperties(lesson,newLesson);
            String newChapterId = chapterIdMapToNewChapterId.get(lesson.getChapter().getId());
            Chapter newChapter = chapterService.get(newChapterId);
            newLesson.setChapter(newChapter);
            newLesson.setId(IdGen.nextId());
            newLesson.setIsNewRecord(true);
            // 新‘节’和旧‘节’，建立关系
            lessonIdMapToNewLessonId.put(lesson.getId(),newLesson.getId());
            lessonIdMapToChapterId.put(newLesson.getId(),newChapter.getId());
            // 将新‘节’加入，待插入列表
            newLessonList.add(newLesson);
        }
        // 保存新的‘节’
        if (!newLessonList.isEmpty()){
            lessonService.insertBatch(newLessonList);
        }
        List<Lesson> newSubLessonList = ListUtils.newArrayList();
        for (Lesson subLesson : subLessonList){
            Lesson newSubLesson = new Lesson();
            Lesson parentLesson = subLesson.getParentLesson();
            String newParentLessonId = lessonIdMapToNewLessonId.get(parentLesson.getId());
            Lesson newParentLesson = lessonService.get(newParentLessonId);

            BeanUtils.copyProperties(subLesson,newSubLesson);
            newSubLesson.setId(IdGen.nextId());
            newSubLesson.setIsNewRecord(true);
            newSubLesson.setParentLesson(newParentLesson);
            // 新‘小节’和旧‘小节’，建立关系
            subLessonIdMapToNewSubLessonId.put(subLesson.getId(),newSubLesson.getId());
            if (newParentLesson.getChapter() != null){
                lessonIdMapToChapterId.put(newSubLesson.getId(),newParentLesson.getChapter().getId());
            }
            newSubLessonList.add(newSubLesson);
        }
        // 保存新的小节
        if (!newSubLessonList.isEmpty()){
            lessonService.insertBatch(newSubLessonList);
        }


        // 获取所有‘节’或者小节的课件
        List<Lesson> allLessons = ListUtils.newArrayList();
        allLessons.addAll(lessonList); allLessons.addAll(subLessonList);
        LessonTask lessonTaskCriteria = new LessonTask();
        lessonTaskCriteria.setLessonId_in(EntityUtils.getBaseEntityIds(allLessons));
        List<LessonTask> lessonTaskList = ListUtils.newArrayList();
        if (!allLessons.isEmpty()){
            lessonTaskList = lessonTaskService.findList(lessonTaskCriteria);
        }

        List<LessonTask> newLessonTaskList = ListUtils.newArrayList();
        for (LessonTask lessonTask : lessonTaskList){
            String newLessonId = subLessonIdMapToNewSubLessonId.get(lessonTask.getLessonId());
            if (StringUtils.isBlank(newLessonId)){
                newLessonId =lessonIdMapToNewLessonId.get(lessonTask.getLessonId());
            }
            if (StringUtils.isNotBlank(newLessonId)){
                LessonTask newLessonTask = new LessonTask();
                BeanUtils.copyProperties(lessonTask,newLessonTask);
                newLessonTask.setId(IdGen.nextId());
                newLessonTask.setIsNewRecord(true);
                newLessonTask.setLessonId(newLessonId);
                newLessonTask.setChapterId(lessonIdMapToChapterId.get(newLessonId));
                if ("Course".equals(copyForm)){
                    // 引用为课程的时候  把资源给为  未修改 状态
                    newLessonTask.setIsModify(SysYesNoEnum.NO.value());
                }
                newLessonTaskList.add(newLessonTask);
            }

        }
        // 保存新的资源
        if (!newLessonTaskList.isEmpty()){
            lessonTaskService.insertBatch(newLessonTaskList);
        }
    }

    /**
     * 查看资源结构
     * @param course
     * @return
     */
    public List<Chapter> lessonTaskStruct(Course course,String teachingClassId) {
        Chapter chapterCriteria = new Chapter();
        chapterCriteria.setCourse(course);
        List<Chapter> chapterList = chapterService.findList(chapterCriteria);

        Lesson lessonCriteria = new Lesson();
        Chapter lcCriteria = new Chapter();
        lcCriteria.setId_in(EntityUtils.getBaseEntityIds(chapterList));
        lessonCriteria.setChapter(lcCriteria);
        List<Lesson> lessonList = lessonService.findList(lessonCriteria);

        Lesson subLessonCriteria = new Lesson();
        subLessonCriteria.setParentLessonId_in(EntityUtils.getBaseEntityIds(lessonList));
        List<Lesson> subLessonList = lessonService.findList(subLessonCriteria);

        // 获取所有‘节’或者小节的资源
        List<Lesson> allLessons = ListUtils.newArrayList();
        allLessons.addAll(lessonList); allLessons.addAll(subLessonList);
        LessonTask lessonTaskCriteria = new LessonTask();
        lessonTaskCriteria.selectIsPush(teachingClassId);
        lessonTaskCriteria.setLessonId_in(EntityUtils.getBaseEntityIds(allLessons));
        List<LessonTask> lessonTaskList = lessonTaskService.findList(lessonTaskCriteria);

        // 结构化 ‘节’
        buildStructChapter(chapterList,lessonList,subLessonList,lessonTaskList);
        // 统计数量
        course.setTotalLessonNum(allLessons.size());

        return chapterList;
    }

    /**
     * 查看已推送资源结构
     * @param course
     * @return
     */
    public List<Chapter> HadPushLessonTaskStruct(Course course,String teachingClassId) {
        Chapter chapterCriteria = new Chapter();
        chapterCriteria.setCourse(course);
        chapterCriteria.setTeachingClassId(teachingClassId);
        List<Chapter> chapterList = chapterService.findListHadPush(chapterCriteria);

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
        LessonTask lessonTaskCriteria = new LessonTask();
        lessonTaskCriteria.setTeachingClassId(teachingClassId);
        // 右联接 查询已经推送的任务
        lessonTaskCriteria.setLessonId_in(EntityUtils.getBaseEntityIds(allLessons));
        List<LessonTask> lessonTaskList = lessonTaskService.findListHadPush(lessonTaskCriteria);

        // 结构化 ‘节’
        buildStructChapter(chapterList,lessonList,subLessonList,lessonTaskList);

        // 统计数量
        course.setHadPushLessonNum(allLessons.size());
        course.setHadPushLessonTaskNum(lessonTaskList.size());

        return chapterList;
    }


    /**
     * 获取章节进度
     * @param course
     * @param teachingClassId
     * @return
     */
    public List<Chapter> progressStruct(Course course, String teachingClassId,String classesId) {

        Long stuNum = studentClassesService.getStudentNum(classesId);

        Chapter chapterCriteria = new Chapter();
        chapterCriteria.setCourse(course);
        chapterCriteria.setTeachingClassId(teachingClassId);
        List<Chapter> chapterList = chapterService.findListHadPush(chapterCriteria);

        Lesson lessonCriteria = new Lesson();
        Chapter lcCriteria = new Chapter();
        lcCriteria.setId_in(EntityUtils.getBaseEntityIds(chapterList));
        lessonCriteria.setChapter(lcCriteria);
        lessonCriteria.setTeachingClassId(teachingClassId);
        lessonCriteria.selectClassStuHadFinishLessonTaskNum(teachingClassId);
        List<Lesson> lessonList = lessonService.findListHadPush(lessonCriteria);

        Lesson subLessonCriteria = new Lesson();
        subLessonCriteria.setParentLessonId_in(EntityUtils.getBaseEntityIds(lessonList));
        subLessonCriteria.setTeachingClassId(teachingClassId);
        subLessonCriteria.selectClassStuHadFinishLessonTaskNum(teachingClassId);
        List<Lesson> subLessonList = lessonService.findListHadPush(subLessonCriteria);

        // 获取所有‘节’或者小节的资源
        List<Lesson> allLessons = ListUtils.newArrayList();
        allLessons.addAll(lessonList); allLessons.addAll(subLessonList);
        LessonTask lessonTaskCriteria = new LessonTask();
        lessonTaskCriteria.setTeachingClassId(teachingClassId);
        lessonTaskCriteria.setLessonId_in(EntityUtils.getBaseEntityIds(allLessons));
        List<LessonTask> lessonTaskList = lessonTaskService.findListHadPush(lessonTaskCriteria);

        // 结构化 ‘节’
        buildStructChapter(chapterList,lessonList,subLessonList,lessonTaskList);

        // 处理进度问题
        // 未推送的节或者小节  不统计进度  进度为0
        allLessons.forEach(lesson -> {
            List<LessonTask> ltList = lesson.getLessonTaskList();
            // 小节 和 没有关联小节的‘节’  要统计进度
            if (lesson.isSubLesson() || (lesson.isLesson() && lesson.getSubLessonList().isEmpty())){
                if (ltList.isEmpty()){
                    // 如果节或小节没有资源，则设置进度为100%
                    lesson.setFinishProgress(1f);
                }else{
                    // 总的任务数
                    Long totalStuTask = ltList.size() * stuNum;
                    Float finishLesson = lesson.getClassStuHadFinishLessonTaskNum().floatValue() / totalStuTask.floatValue();
                    lesson.setFinishProgress(finishLesson);
                }
            }
        });

        return chapterList;
    }

    public List<Chapter> studentProgressStruct(Course course, String teachingClassId, String studentId) {
        Chapter chapterCriteria = new Chapter();
        chapterCriteria.setCourse(course);
        chapterCriteria.setTeachingClassId(teachingClassId);
        List<Chapter> chapterList = chapterService.findListHadPush(chapterCriteria);

        Lesson lessonCriteria = new Lesson();
        Chapter lcCriteria = new Chapter();
        lcCriteria.setId_in(EntityUtils.getBaseEntityIds(chapterList));
        lessonCriteria.setChapter(lcCriteria);
        lessonCriteria.setTeachingClassId(teachingClassId);
        lessonCriteria.selectHadFinishLessonTaskNum(teachingClassId,studentId);
        List<Lesson> lessonList = lessonService.findListHadPush(lessonCriteria);

        Lesson subLessonCriteria = new Lesson();
        subLessonCriteria.setParentLessonId_in(EntityUtils.getBaseEntityIds(lessonList));
        subLessonCriteria.setTeachingClassId(teachingClassId);
        subLessonCriteria.selectHadFinishLessonTaskNum(teachingClassId,studentId);
        List<Lesson> subLessonList = lessonService.findListHadPush(subLessonCriteria);

        // 获取所有‘节’或者小节的资源
        List<Lesson> allLessons = ListUtils.newArrayList();
        allLessons.addAll(lessonList); allLessons.addAll(subLessonList);
        LessonTask lessonTaskCriteria = new LessonTask();
        lessonTaskCriteria.setTeachingClassId(teachingClassId);
        lessonTaskCriteria.setLessonId_in(EntityUtils.getBaseEntityIds(allLessons));
        lessonTaskCriteria.selectModifyScore(teachingClassId);
        List<LessonTask> lessonTaskList = lessonTaskService.findListStuFinishDetail(lessonTaskCriteria,studentId);

        // 结构化 ‘节’
        buildStructChapter(chapterList,lessonList,subLessonList,lessonTaskList);

        // 处理进度问题
        // 未推送的节或者小节  不统计进度  进度为0
        for(Lesson lesson: allLessons){
            List<LessonTask> ltList = lesson.getLessonTaskList();
            // 小节 和 没有关联小节的‘节’  要统计进度
            if (lesson.isSubLesson() || (lesson.isLesson() && lesson.getSubLessonList().isEmpty())){
                lesson.setLessonTaskNum(ltList.size());
                if (ltList.isEmpty()){
                    // 如果节或小节没有资源，则设置进度为100%
                    lesson.setHadFinishLessonTaskNum(0);
                    lesson.setFinishProgress(1f);
                }else{
                    // 总的任务数
                    lesson.setFinishProgress(lesson.getHadFinishLessonTaskNum()/lesson.getLessonTaskNum().floatValue());
                }
            }
        }
        return chapterList;
    }
}
