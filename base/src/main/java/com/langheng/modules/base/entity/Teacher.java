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
 * @ClassName Teacher
 * @Description TODO
 * @Author xiaoxie
 * @Date 2019-12-18 8:45
 * @Version 1.0
 */
@Table(name="sys_teacher", alias="a", columns={
        @Column(name="teacher_id", attrName="teacherId", label="教师id", isPK=true),
        @Column(name="major_id", attrName="major.majorId", label="关联专业id"),
        @Column(name="teacher_name", attrName="teacherName", label="教师姓名", queryType = QueryType.LIKE),
        @Column(includeEntity=DataEntity.class),
    },  joinTable={
        @JoinTable(type=Type.LEFT_JOIN, entity= BaseUser.class,attrName="this",  alias="u11",
                on="u11.user_code = a.teacher_id", columns={
                @Column(includeEntity = BaseUser.class),
        }),
        @JoinTable(type=Type.LEFT_JOIN, entity=Major.class, attrName="major", alias="u12",
                on="u12.major_id = a.major_id", columns={
                @Column(includeEntity = Major.class),
        }),
    },   orderBy="a.update_date DESC"
)
@ApiModel(description = "教师")
public class Teacher extends BaseUser {


    @Valid
    @ExcelFields({
            @ExcelField(title="登录ID", attrName="loginCode", align=Align.CENTER, sort=10),
            @ExcelField(title="姓名", attrName="teacherName", align=Align.CENTER, sort=20),
            @ExcelField(title="所属院系", attrName="academyAndMajor", align=Align.CENTER, sort=30),
            @ExcelField(title="密码", attrName="password", align=Align.CENTER, sort=40),
    })

    @ApiModelProperty("教师id")
    private String teacherId;
    @ApiModelProperty("教师姓名")
    @NotBlank(message="教师姓名不能为空")
    @Length(min=2, max=64, message="教师姓名长度长度限制 为2到64个字符")
    private String teacherName;
    @ApiModelProperty("所在部门（专业）")
    private Major major;

    /**
     * vo对象
     */
    private String academyAndMajor;

    public Teacher(){
        super(null);
    }

    public Teacher(String teacherId) {
       super(teacherId);
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }

    public String getAcademyAndMajor() {
        return academyAndMajor;
    }

    public void setAcademyAndMajor(String academyAndMajor) {
        this.academyAndMajor = academyAndMajor;
    }
}
