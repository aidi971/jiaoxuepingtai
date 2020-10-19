package com.langheng.modules.ed.entity;

import io.swagger.annotations.ApiModelProperty;

public class LessonVo {

    @ApiModelProperty("小节id")
    private String lessonId;
    @ApiModelProperty("作业id")
    private String lessonTaskId;
    @ApiModelProperty("任务序号")
    private Integer taskNum;
    @ApiModelProperty("名称")
    private String lessonName;
    @ApiModelProperty("作业名称")
    private String lessonTaskName;
    @ApiModelProperty("名称前缀")
    private String suffixNum;

    public Integer getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(Integer taskNum) {
        this.taskNum = taskNum;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public String getSuffixNum() {
        return suffixNum;
    }

    public void setSuffixNum(String suffixNum) {
        this.suffixNum = suffixNum;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public String getLessonTaskName() {
        return lessonTaskName;
    }

    public void setLessonTaskName(String lessonTaskName) {
        this.lessonTaskName = lessonTaskName;
    }

    public String getLessonTaskId() {
        return lessonTaskId;
    }

    public void setLessonTaskId(String lessonTaskId) {
        this.lessonTaskId = lessonTaskId;
    }
}
