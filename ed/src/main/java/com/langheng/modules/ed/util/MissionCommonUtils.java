package com.langheng.modules.ed.util;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.enumn.SysYesNoEnum;
import com.jeesite.common.idgen.IdGen;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.framework.uitls.EntityUtils;
import com.langheng.modules.base.utils.TeacherUtils;
import com.langheng.modules.ed.entity.Course;
import com.langheng.modules.ed.entity.LessonTask;
import com.langheng.modules.ed.entity.Mission;
import com.langheng.modules.ed.entity.Template;
import com.langheng.modules.ed.enumn.StructureType;
import com.langheng.modules.ed.enumn.TemplateState;
import com.langheng.modules.ed.enumn.TemplateType;
import com.langheng.modules.ed.service.MissionCourseService;
import com.langheng.modules.ed.service.MissionLessonTaskService;
import com.langheng.modules.ed.service.MissionService;
import com.langheng.modules.ed.service.MissionTemplateService;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MissionCommonUtils {

    private static MissionCourseService missionCourseService
            = SpringUtils.getBean(MissionCourseService.class);

    private static MissionTemplateService missionTemplateService
            = SpringUtils.getBean(MissionTemplateService.class);

    private static MissionService missionService
            = SpringUtils.getBean(MissionService.class);

    private static MissionLessonTaskService missionLessonTaskService
            = SpringUtils.getBean(MissionLessonTaskService.class);


    /**
     * 展开任务型课程的结构
     * @param course
     * @return
     */
    public static List<Mission> expandByCourse(Course course) {

        Mission missionCriteria = new Mission();
        missionCriteria.setCourse(new Course(course.getId()));
        List<Mission> missionList = missionService.findList(missionCriteria);
        Map<String,Mission> missionMap
                = missionList.stream().collect(Collectors.toMap(Mission::getMissionId,Mission -> Mission));

        LessonTask lTaskCriteria= new LessonTask();
        lTaskCriteria.setMissionId_in(EntityUtils.getBaseEntityIds(missionList));
        List<LessonTask> lessonTaskList = missionLessonTaskService.findList(lTaskCriteria);

        for (LessonTask lessonTask: lessonTaskList){
            if (missionMap.containsKey(lessonTask.getMissionId())){
                Mission mission = missionMap.get(lessonTask.getMissionId());
                mission.getLessonTaskList().add(lessonTask);
            }
        }

        return missionList;
    }


    /**
     * 展开任务型课程的结构
     * @param template
     * @return
     */
    public static List<Mission> expandByTemplate(Template template) {

        Mission missionCriteria = new Mission();
        missionCriteria.setTemplate(new Template(template.getId()));
        List<Mission> missionList = missionService.findList(missionCriteria);
        Map<String,Mission> missionMap
                = missionList.stream().collect(Collectors.toMap(Mission::getMissionId,Mission -> Mission));

        LessonTask lTaskCriteria= new LessonTask();
        lTaskCriteria.setMissionId_in(EntityUtils.getBaseEntityIds(missionList));
        List<LessonTask> lessonTaskList = missionLessonTaskService.findList(lTaskCriteria);

        for (LessonTask lessonTask: lessonTaskList){
            if (missionMap.containsKey(lessonTask.getMissionId())){
                Mission mission = missionMap.get(lessonTask.getMissionId());
                mission.getLessonTaskList().add(lessonTask);
            }
        }

        return missionList;
    }

    public static Course saveAsCourse(Course oldCourse) {
        // 创建新的课程
        Course newCourse = new Course();
        BeanUtils.copyProperties(oldCourse,newCourse);
        newCourse.setId(IdGen.nextId());
        newCourse.setIsNewRecord(true);
        newCourse.setIterations("0");
        newCourse.setVersion(CourseUtils.getNextVersion(newCourse));
        missionCourseService.save(newCourse);

        copyMissionAndLessonTask(oldCourse,newCourse);

        return newCourse;
    }

    /**
     * 申请公开
     * @param course
     */
    public static void applyPublic(Course course) {
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
        template.setStructureType(StructureType.MISSION.value());
        // 暂不做处理
        template.setVideoNum(course.getVideoNum());
        template.setCoursewareNum(course.getCoursewareNum());
        template.setHomeworkNum(course.getHomeworkNum());
        template.setCiteNum(0);
        missionTemplateService.save(template);

        // 获取所有的章节

        copyMissionAndLessonTask(course,template);
    }

    private static void copyMissionAndLessonTask(Course oldCourse,Object parent) {
        List<Mission> missionListStruct =  expandByCourse(oldCourse);
        copyMissionAndLessonTask(missionListStruct,parent);
    }

    private static void copyMissionAndLessonTask(List<Mission> missionListStruct,Object parent){
        List<Mission> newMissionList = ListUtils.newArrayList();
        List<LessonTask> newLessonTaskList = ListUtils.newArrayList();

        // 复制来源
        String copyForm = new String();
        if (parent instanceof Course){
            copyForm = "Course";
        }else if (parent instanceof Template){
            copyForm = "Template";
        }

        for (Mission oldMission: missionListStruct){
            Mission newMission = new Mission();
            BeanUtils.copyProperties(oldMission,parent);
            newMission.setId(IdGen.nextId());
            if ("Course".equals(copyForm)){
                newMission.setCourse((Course)parent);
                newMission.setTemplate(null);
            }else if ("Template".equals(copyForm)){
                newMission.setTemplate((Template)parent);
                newMission.setCourse(null);
            }

            // 后续再添加
            newMissionList.add(newMission);

            List<LessonTask> lessonTaskList = oldMission.getLessonTaskList();
            for (LessonTask oldLessonTask: lessonTaskList){
                LessonTask newLessonTask = new LessonTask();
                BeanUtils.copyProperties(oldLessonTask,newLessonTask);

                newLessonTask.setId(IdGen.nextId());
                newLessonTask.setMissionId(newMission.getId());

                newLessonTaskList.add(newLessonTask);
            }
        }

        //批量添加课程任务，lessonTask
        missionService.insertBatch(newMissionList);
        missionLessonTaskService.insertBatch(newLessonTaskList);
    }

    public static Course templateSaveAsCourse(Template template) {
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
        missionCourseService.save(newCourse);

        //引用次数加一
        template.setCiteNum(template.getCiteNum() + 1);
        template.setIsEnabled(SysYesNoEnum.YES.value());
        missionTemplateService.save(template);

        // 获取所有的章节
        List<Mission> missionListStruct = expandByTemplate(template);

        copyMissionAndLessonTask(missionListStruct,newCourse);

        return newCourse;
    }

    /**
     * 获取任务型课程结构
     */
    public static List<Mission> lessonTaskStruct(Course course, String teachingClassId) {
        Mission missCriteria = new Mission();
        missCriteria.setCourse(new Course(course.getId()));
        List<Mission> missionList = missionService.findList(missCriteria);
        Map<String,Mission> missionMap
                = missionList.stream().collect(Collectors.toMap(Mission::getMissionId,Mission -> Mission));

        LessonTask lTaskCriteria= new LessonTask();
        lTaskCriteria.setMissionId_in(EntityUtils.getBaseEntityIds(missionList));
        List<LessonTask> lessonTaskList = missionLessonTaskService.findList(lTaskCriteria);

        for (LessonTask lessonTask: lessonTaskList){
            if (missionMap.containsKey(lessonTask.getMissionId())){
                Mission mission = missionMap.get(lessonTask.getMissionId());
                mission.getLessonTaskList().add(lessonTask);
            }
        }

        return missionList;
    }

    public static List<Mission> HadPushLessonTaskStruct(Course course, String teachingClassId) {
        Mission missCriteria = new Mission();
        missCriteria.setCourse(new Course(course.getId()));
        List<Mission> missionList = missionService.findListHadPush(missCriteria);
        Map<String,Mission> missionMap
                = missionList.stream().collect(Collectors.toMap(Mission::getMissionId,Mission -> Mission));

        LessonTask lTaskCriteria= new LessonTask();
        lTaskCriteria.setMissionId_in(EntityUtils.getBaseEntityIds(missionList));
        List<LessonTask> lessonTaskList = missionLessonTaskService.findList(lTaskCriteria);

        for (LessonTask lessonTask: lessonTaskList){
            if (missionMap.containsKey(lessonTask.getMissionId())){
                Mission mission = missionMap.get(lessonTask.getMissionId());
                mission.getLessonTaskList().add(lessonTask);
            }
        }

        return missionList;
    }
}
