package com.jeesite.modules.stat.entity;

public class DiscussStatVo {

    private String chapterName;

    private Integer discussCount;

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public Integer getDiscussCount() {
        return discussCount;
    }

    public void setDiscussCount(Integer discussCount) {
        this.discussCount = discussCount;
    }

    public DiscussStatVo(String chapterName, Integer discussCount) {
        this.chapterName = chapterName;
        this.discussCount = discussCount;
    }
}
