package com.langheng.modules.ed.vo;

import lombok.Data;
//分数排行榜
@Data
public class ScoreRanking {
        private Integer ranking;
        private String userId;
        private String coverImg;
        private String loginCode;
        private String studentName;
        private Float count;
}
