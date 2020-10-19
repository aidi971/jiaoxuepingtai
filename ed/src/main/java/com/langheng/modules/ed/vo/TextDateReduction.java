//package com.langheng.modules.ed.vo;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import io.swagger.annotations.ApiModelProperty;
//
//
//import javax.xml.soap.Text;
//import java.util.Date;
//
//public class TextDateReduction {
//    @ApiModelProperty("创建时间")
//    private Date creatDate;
//    @ApiModelProperty("文本内容")
//    private String text;
//
//    @JsonFormat(
//            pattern = "yyyy-MM-dd HH:mm"
//    )
//    public Date getCreatDate() {
//        return creatDate;
//    }
//
//    public void setCreatDate(Date creatDate) {
//        this.creatDate = creatDate;
//    }
//
//    public String getText() {
//        return text;
//    }
//
//    public void setText(String text) {
//        this.text = text;
//    }
//}
