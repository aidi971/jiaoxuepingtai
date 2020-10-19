package com.langheng.modules.ed.vo.dataReduction;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jeesite.common.collect.ListUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NoteDataReductionVo {
    @ApiModelProperty("笔记数")
    private Integer noteCount;
    @ApiModelProperty("字数")
    private Integer wordCount;
    @ApiModelProperty("最多笔记资源名称")
    private String maxNoteSourceName;
    @ApiModelProperty("最多笔记资源数量")
    private Integer maxNoteSourceCount;
    @ApiModelProperty("最多笔记学生名称")
    private String maxNoteUserName;
    @ApiModelProperty("最多笔记学生数量")
    private Integer maxNoteUserCount;
    @ApiModelProperty("章节统计集合")
    private List<Map> list= ListUtils.newArrayList();

    public List<Map> getList() {
        return list;
    }

    public void setList(List<Map> list) {
        this.list = list;
    }
}
