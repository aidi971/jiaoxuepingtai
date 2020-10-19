package com.langheng.modules.base.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelField.Align;
import com.jeesite.common.utils.excel.annotation.ExcelFields;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @ClassName Student
 * @Description TODO
 * @Author xiaoxie
 * @Date 2019-12-18 8:45
 * @Version 1.0
 */
@Table(name="sys_student", alias="a", columns={
        @Column(name="student_id", attrName="studentId", label="学生id", isPK=true),
        @Column(name="student_name", attrName="studentName", label="学生姓名", queryType = QueryType.LIKE),
        @Column(name="origin_type", attrName="originType", label="学生来源"),
        @Column(name="is_can_send_msg", attrName="isCanSendMsg", label="发言状态"),
        @Column(name="classes_id", attrName="classesId", label="班级id"),
        @Column(includeEntity=DataEntity.class),
},  joinTable={
        @JoinTable(type=Type.LEFT_JOIN, entity= BaseUser.class,attrName="this",  alias="u11",
                on="u11.user_code = a.student_id", columns={
                @Column(includeEntity = BaseUser.class),
        }),
},   orderBy="u11.login_code"
)
@ApiModel(description = "学生")
public class Student extends BaseUser {
    @Valid
    @ExcelFields({
            @ExcelField(title="序号", attrName="serialNumber", align=Align.CENTER, sort=10,type = ExcelField.Type.EXPORT),
            @ExcelField(title="登录ID", attrName="loginCode", align=Align.CENTER, sort=20),
            @ExcelField(title="学生姓名", attrName="studentName", align=Align.CENTER, sort=30),
            @ExcelField(title="密码", attrName="password", align=Align.CENTER, sort=40),
            @ExcelField(title="来源", attrName="originType", dictType="student_origin_type", width=10*256, align=Align.CENTER, sort=50,type = ExcelField.Type.EXPORT),
    })
    @ApiModelProperty("学生id")
    private String studentId;

    @ApiModelProperty(value = "学生姓名",required = true)
    @NotBlank(message="学生名不能为空")
    @Length(min=2, max=64, message="学生名长度长度限制 为2到64个字符")
    private String studentName;

    @ApiModelProperty("学生来源")
    private String originType;

    @ApiModelProperty("是否能正常发言 0禁言,1正常")
    private String isCanSendMsg;

    @ApiModelProperty("班级id")
    private String classesId;

    // 以下为Vo对象
    private String classesName;

    private Integer serialNumber;

    public Student(){
        super(null);
    }

    public Student(String studentId) {
        super(studentId);
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getOriginType() {
        return originType;
    }

    public void setOriginType(String originType) {
        this.originType = originType;
    }

    public String getIsCanSendMsg() {
        return isCanSendMsg;
    }

    public void setIsCanSendMsg(String isCanSendMsg) {
        this.isCanSendMsg = isCanSendMsg;
    }

    public String getClassesName() {
        return classesName;
    }

    public void setClassesName(String classesName) {
        this.classesName = classesName;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getClassesId() {
        return classesId;
    }

    public void setClassesId(String classesId) {
        this.classesId = classesId;
    }

    public void setStudentId_in(String[] ids){
        if (ids == null || ids.length == 0){
            this.sqlMap.getWhere().and("student_id",QueryType.IN,"NOT_EXIST_ID");
        }else {
            this.sqlMap.getWhere().and("student_id",QueryType.IN,ids);
        }

    }

    public void setLoginCodeOrUserName(String LoginCodeOrUserName){
        sqlMap.getWhere().andBracket("u11.user_name",QueryType.LIKE,LoginCodeOrUserName)
                .or("u11.login_code",QueryType.LIKE,LoginCodeOrUserName)
                .endBracket();
    }

    public void selectClassesId_isNull() {
        sqlMap.getWhere().and("classes_id",QueryType.IS_NULL,null);
    }
}
