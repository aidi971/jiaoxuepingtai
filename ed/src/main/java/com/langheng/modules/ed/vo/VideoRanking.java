package com.langheng.modules.ed.vo;

import lombok.Data;

@Data
//视频排行榜
public class VideoRanking {
    private Integer ranking;
    private String userId;
    private String coverImg;
    private String loginCode;
    private String studentName;
    private Integer count;

}