package com.langheng.modules.ed.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class StudentVo {

    @ApiModelProperty("学生id")
    private String id;
    @ApiModelProperty("学生登录账号")
    private String loginCode;
    @ApiModelProperty("学生姓名")
    private String userName;
    @ApiModelProperty("学生头像")
    private String coverImg;
    @ApiModelProperty("已经完成的资源数量")
    private Integer hadFinishLessonTaskNum;
    @ApiModelProperty("资源总数量")
    private Integer lessonTaskNum;
    @ApiModelProperty("排名")
    private Integer order;
    @ApiModelProperty("总学生人数")
    private Integer totalStuNum;
    @ApiModelProperty("新的章节资源总数")
    private Integer newChapterLessonTaskNum;
    @ApiModelProperty("新的章节已经完成的资源数")
    private Integer newChapterHadFinishLessonTaskNum;
    @ApiModelProperty("累计得分")
    private Float grossScore;

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

    public Integer getLessonTaskNum() {
        return lessonTaskNum;
    }

    public void setLessonTaskNum(Integer lessonTaskNum) {
        this.lessonTaskNum = lessonTaskNum;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getTotalStuNum() {
        return totalStuNum;
    }

    public void setTotalStuNum(Integer totalStuNum) {
        this.totalStuNum = totalStuNum;
    }

    public Integer getNewChapterLessonTaskNum() {
        return newChapterLessonTaskNum;
    }

    public void setNewChapterLessonTaskNum(Integer newChapterLessonTaskNum) {
        this.newChapterLessonTaskNum = newChapterLessonTaskNum;
    }

    public Integer getNewChapterHadFinishLessonTaskNum() {
        return newChapterHadFinishLessonTaskNum;
    }

    public void setNewChapterHadFinishLessonTaskNum(Integer newChapterHadFinishLessonTaskNum) {
        this.newChapterHadFinishLessonTaskNum = newChapterHadFinishLessonTaskNum;
    }

    public Float getGrossScore() {
        return grossScore;
    }

    public void setGrossScore(Float grossScore) {
        this.grossScore = grossScore;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }
}
