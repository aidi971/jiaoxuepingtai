package com.langheng.modules.base.web;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.base.entity.Student;
import com.langheng.modules.base.entity.UserHead;
import com.langheng.modules.base.service.StudentService;
import com.langheng.modules.base.service.UserHeadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName StudentController
 * @Description TODO
 * @Author xiaoxie
 * @Date 2019-12-14 10:37
 * @Version 1.0
 */
@RestController
@RequestMapping("/admin/student")
@Api(description = "管理学生信息相关接口")
public class StudentController extends BaseApiController {
    
    @Autowired
    private StudentService studentService;
    @Autowired
    private UserHeadService userHeadService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public Student get(String id, boolean isNewRecord) {
        return studentService.get(id, isNewRecord);
    }


    @ApiOperation(value = "分页获取学生信息")
    @PostMapping(value = "findPage")
    public ResultBean<Page<Student>> findPage(Student student,
                               @RequestParam(defaultValue = "10") int pageSize,
                               @RequestParam(defaultValue = "1") int pageNo ) {
        student.setPage(new Page(pageNo,pageSize));
        Page<Student> page = studentService.findPage(student);
        return success(page);
    }

    @ApiOperation(value = "获取学生信息列表")
    @PostMapping(value = "findList")
    public ResultBean<List<Student>> findList(Student student ) {
        List<Student> list = studentService.findList(student);
        return success(list);
    }

    @ApiOperation(value = "修改学生")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "studentId", value = "学生id", required = true),
            @ApiImplicitParam(name = "studentName", value = "姓名", required = true),
            @ApiImplicitParam(name = "password", value = "密码", required = true),
            @ApiImplicitParam(name = "coverImg", value = "头像", required = true),
    })
    @PostMapping(value = "updateStudent")
    public ResultBean<Student> updateStudent(Student student){

        Student oldStudent = studentService.get(student);
        oldStudent.setPassword(student.getPassword());
        oldStudent.setCoverImg(student.getCoverImg());
        oldStudent.setStudentName(student.getStudentName());
        oldStudent.setUserName(student.getStudentName());
        studentService.save(oldStudent);
        return success(oldStudent);
    }


    @ApiOperation(value = "重置密码")
    @PostMapping("/repeatPassword")
    public ResultBean repeatPassword(@RequestParam String studentId){
        List<String> studentIds = Arrays.asList(studentId.split(","));
        studentIds.forEach(id->{
            Student student = studentService.get(id);
            student.setPassword("666666");
            studentService.save(student);
        });
        return success();
    }

    @ApiOperation(value = "停用学生")
    @PostMapping("/disable")
    public ResultBean disable(@RequestParam String studentId){
        List<String> studentIds = Arrays.asList(studentId.split(","));
        studentIds.forEach(id->{
            Student student = studentService.get(id);
            studentService.disable(student);
        });
        return success();
    }

    @ApiOperation(value = "启用学生")
    @PostMapping("/enable")
    public ResultBean enable(@RequestParam String studentId){
        List<String> studentIds = Arrays.asList(studentId.split(","));
        studentIds.forEach(id->{
            Student student = studentService.get(id);
            studentService.enable(student);
        });
        return success();
    }

    @ApiOperation(value = "删除学生")
    @PostMapping(value = "delete")
    public ResultBean delete(Student student){
        studentService.delete(student);
        return success();
    }

    @ApiIgnore
    @ApiOperation(value = "导出学生信息")
    @PostMapping(value = "exportData")
    public ResultBean exportData(Student student,  HttpServletResponse response) {
        List<Student> list =studentService.findList(student);
        String fileName = "学生信息" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
        try(ExcelExport ee = new ExcelExport("学生信息", Student.class)){
            ee.setDataList(list).write(response, fileName);
        }
        return success();
    }

    @ApiIgnore
    @ApiOperation(value = "下载导入学生信息模板")
    @PostMapping(value = "importTemplate")
    public void importTemplate(HttpServletResponse response) {
        Student student = new Student();
        student.setLoginCode("student1");
        student.setStudentName("测试学生姓名");
        student.setPassword("666666");

        List<Student> list = ListUtils.newArrayList(student);
        String fileName = "学生信息模板.xlsx";
        try(ExcelExport ee = new ExcelExport("学生信息", Student.class)){
            ee.setDataList(list).write(response, fileName);
        }
    }


    @ApiIgnore
    @ApiOperation(value = "导入学生信息")
    @ResponseBody
    @PostMapping(value = "importData")
    public ResultBean importData(MultipartFile file, String updateSupport) {
        try {
            boolean isUpdateSupport = Global.YES.equals(updateSupport);
            String message = studentService.importData(file, isUpdateSupport);
            return success(message);
        } catch (Exception ex) {
            return error(ex.getMessage());
        }
    }

    @ApiOperation(value = "随机头像")
    @PostMapping("/randomHeadImg")
    public ResultBean randomHeadImg() {
        UserHead userHead = userHeadService.getUserHead(0);
        return success(userHead);
    }

    @ApiOperation(value = "头像列表")
    @PostMapping("/HeadImg")
    public ResultBean HeadImg() {
        UserHead head = new UserHead();
        head.setHeadType(0);
        List<UserHead> list = userHeadService.findList(head);
        return success(list);
    }

}
