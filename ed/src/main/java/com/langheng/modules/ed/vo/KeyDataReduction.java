package com.langheng.modules.ed.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;


public class KeyDataReduction {
    @ApiModelProperty("资源id")
    private String keyId;
    @ApiModelProperty("资源名称")
    private String keyName;
    @ApiModelProperty("资源类型")
    private String keyType;
    @ApiModelProperty("资源序号")
    private String keyNumber;
    @ApiModelProperty("学生得分")
    private Float studentScore;
    @ApiModelProperty("完成时间")
    private Date finishTime;
    @ApiModelProperty("创建时间")
    private Date creatDate;
    @ApiModelProperty("弹幕数量")
    private Integer barrageCount;
    @ApiModelProperty("数据集合")
    private List dateList;
    @ApiModelProperty("完成数")
    private Integer userTaskCount;
    @ApiModelProperty("任务总数")
    private Integer sumUserTaskCount;
    @ApiModelProperty("对题数")
    private Integer correctCount;
    @ApiModelProperty("错题数")
    private Integer errorCount;
    @ApiModelProperty("重做数")
    private Integer toAnswerCount;
    @ApiModelProperty("附件数")
    private Integer libraryCount;
    @ApiModelProperty("学习数")
    private Integer lookCount;
    @ApiModelProperty("被使用数")
    private Integer usedCount;
    @ApiModelProperty("资源时长")
    private String keyTime;
    @ApiModelProperty("字数")
    private Integer wordCount;
    @ApiModelProperty("笔记数")
    private Integer noteCount;
    @ApiModelProperty("最高分")
    private Float maxScore;
    @ApiModelProperty("平均分分")
    private Float aveScore;

    public Float getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Float maxScore) {
        this.maxScore = maxScore;
    }

    public Float getAveScore() {
        return aveScore;
    }

    public void setAveScore(Float aveScore) {
        this.aveScore = aveScore;
    }

    public Integer getSumUserTaskCount() {
        return sumUserTaskCount;
    }

    public void setSumUserTaskCount(Integer sumUserTaskCount) {
        this.sumUserTaskCount = sumUserTaskCount;
    }

    public Integer getWordCount() {
        return wordCount;
    }

    public void setWordCount(Integer wordCount) {
        this.wordCount = wordCount;
    }

    public Integer getNoteCount() {
        return noteCount;
    }

    public void setNoteCount(Integer noteCount) {
        this.noteCount = noteCount;
    }

    public Integer getUserTaskCount() {
        return userTaskCount;
    }

    public void setUserTaskCount(Integer userTaskCount) {
        this.userTaskCount = userTaskCount;
    }

    public Integer getLookCount() {
        return lookCount;
    }

    public void setLookCount(Integer lookCount) {
        this.lookCount = lookCount;
    }

    public Integer getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(Integer usedCount) {
        this.usedCount = usedCount;
    }

    public String getKeyTime() {
        return keyTime;
    }

    public void setKeyTime(String keyTime) {
        this.keyTime = keyTime;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public String getKeyNumber() {
        return keyNumber;
    }

    public void setKeyNumber(String keyNumber) {
        this.keyNumber = keyNumber;
    }

    public Float getStudentScore() {
        return studentScore;
    }

    public void setStudentScore(Float studentScore) {
        this.studentScore = studentScore;
    }

    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm"
    )
    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm"
    )
    public Date getCreatDate() {
        return creatDate;
    }

    public void setCreatDate(Date creatDate) {
        this.creatDate = creatDate;
    }

    public Integer getBarrageCount() {
        return barrageCount;
    }

    public void setBarrageCount(Integer barrageCount) {
        this.barrageCount = barrageCount;
    }

    public List getDateList() {
        return dateList;
    }

    public void setDateList(List dateList) {
        this.dateList = dateList;
    }

    public Integer getCorrectCount() {
        return correctCount;
    }

    public void setCorrectCount(Integer correctCount) {
        this.correctCount = correctCount;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    public Integer getToAnswerCount() {
        return toAnswerCount;
    }

    public void setToAnswerCount(Integer toAnswerCount) {
        this.toAnswerCount = toAnswerCount;
    }

    public Integer getLibraryCount() {
        return libraryCount;
    }

    public void setLibraryCount(Integer libraryCount) {
        this.libraryCount = libraryCount;
    }
}
