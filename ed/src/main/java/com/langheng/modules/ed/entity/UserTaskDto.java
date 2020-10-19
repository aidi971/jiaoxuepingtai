package com.langheng.modules.ed.entity;

public class UserTaskDto {

    private String teachingClassId;

    private String classesId;

    private String userId;

    private String chapterId;

    private String lessonId;

    private String subLessonId;

    private String lessonTaskId;

    private String lessonTaskType;

    public String getTeachingClassId() {
        return teachingClassId;
    }

    public void setTeachingClassId(String teachingClassId) {
        this.teachingClassId = teachingClassId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public String getSubLessonId() {
        return subLessonId;
    }

    public void setSubLessonId(String subLessonId) {
        this.subLessonId = subLessonId;
    }

    public String getLessonTaskId() {
        return lessonTaskId;
    }

    public void setLessonTaskId(String lessonTaskId) {
        this.lessonTaskId = lessonTaskId;
    }

    public String getClassesId() {
        return classesId;
    }

    public void setClassesId(String classesId) {
        this.classesId = classesId;
    }

    public String getLessonTaskType() {
        return lessonTaskType;
    }

    public void setLessonTaskType(String lessonTaskType) {
        this.lessonTaskType = lessonTaskType;
    }
}
