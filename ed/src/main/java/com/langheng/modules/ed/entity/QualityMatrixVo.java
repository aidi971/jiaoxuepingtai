package com.langheng.modules.ed.entity;

import io.swagger.annotations.ApiModelProperty;

public class QualityMatrixVo {

    @ApiModelProperty("学生id")
    private String id;
    @ApiModelProperty("学生登录账号")
    private String loginCode;
    @ApiModelProperty("学生姓名")
    private String userName;
    @ApiModelProperty("已经完成的资源数量")
    private Integer hadFinishLessonTaskNum;
    @ApiModelProperty("客观题累计得分")
    private Float grossObjectiveScore;
    @ApiModelProperty("综合等分")
    private Float synthesisScore;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginCode() {
        return loginCode;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getHadFinishLessonTaskNum() {
        return hadFinishLessonTaskNum;
    }

    public void setHadFinishLessonTaskNum(Integer hadFinishLessonTaskNum) {
        this.hadFinishLessonTaskNum = hadFinishLessonTaskNum;
    }

    public Float getGrossObjectiveScore() {
        return grossObjectiveScore;
    }

    public void setGrossObjectiveScore(Float grossObjectiveScore) {
        this.grossObjectiveScore = grossObjectiveScore;
    }

    public Float getSynthesisScore() {
        return synthesisScore;
    }

    public void setSynthesisScore(Float synthesisScore) {
        this.synthesisScore = synthesisScore;
    }
}
