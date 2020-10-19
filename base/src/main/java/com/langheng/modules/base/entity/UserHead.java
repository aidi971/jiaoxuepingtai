package com.langheng.modules.base.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Table(name="sys_userhead", alias="a", columns={
        @Column(name="head_id", attrName="headID", label="id", isPK=true),
        @Column(name="head_name", attrName="headName", label="名称"),
        @Column(name="head_type", attrName="headType", label="类型（0学生，1教师）",queryType = QueryType.LIKE),
        @Column(name="head_url", attrName="headUrl", label="路径"),
        @Column(includeEntity= DataEntity.class),
},   orderBy="a.update_date DESC"
)
@ApiModel(description = "头像")
@Data
public class UserHead extends DataEntity<UserHead> {
    @ApiModelProperty("id")
    private Integer headID;
    @ApiModelProperty("名称")
    private String headName;
    @ApiModelProperty("类型（0学生，1教师）")
    private Integer headType;
    @ApiModelProperty("路径")
    private String headUrl;
}
