package com.langheng.modules.base.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Table(name="sys_license_information", alias="a", columns={
        @Column(name="license_id", attrName="licenseId", label="id", isPK=true),
        @Column(name="manufacturer", attrName="manufacturer", label="厂商名称"),
        @Column(name="school_name", attrName="schoolName", label="学校名称"),
        @Column(name="license_count", attrName="licenseCount", label="授权点数"),
        @Column(name="expiration_time", attrName="expirationTime", label="过期时间"),
        @Column(includeEntity=DataEntity.class),
},  orderBy="a.update_date DESC"
)
@ApiModel(description = "授权信息")
@Data

public class LicenseInformation extends DataEntity<LicenseInformation> {

    @ApiModelProperty("id")
    private String licenseId;
    @ApiModelProperty("厂商名称")
    private String manufacturer;
    @ApiModelProperty("学校名称")
    private String schoolName;
    @ApiModelProperty("授权点数")
    private String licenseCount;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @ApiModelProperty("过期时间")
    private String expirationTime;

}
