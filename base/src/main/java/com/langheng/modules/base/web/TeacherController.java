package com.langheng.modules.base.web;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.io.FileUtils;
import com.jeesite.common.io.ResourceUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.base.entity.*;
import com.langheng.modules.base.event.TeacherEvent;
import com.langheng.modules.base.service.*;
import com.langheng.modules.base.utils.uplocadUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * @ClassName TeacherController
 * @Description TODO
 * @Author xiaoxie
 * @Date 2019-12-14 10:37
 * @Version 1.0
 */
@RestController
@RequestMapping("/admin/teacher")
@Api(description = "管理教师信息相关接口")
public class TeacherController extends BaseApiController {
    
    @Autowired
    private TeacherService teacherService;

    @Autowired
    private UserHeadService userHeadService;

    @Autowired
    private AcademyService academyService;

    @Autowired
    private MajorService majorService;

    @Autowired
    private BaseUserService baseUserService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public Teacher get(String id, boolean isNewRecord) {
        return teacherService.get(id, isNewRecord);
    }


    @ApiOperation(value = "分页获取教师信息")
    @PostMapping(value = "findPage")
    public ResultBean<Page<Teacher>> findPage(Teacher teacher,
                               @RequestParam(defaultValue = "10") int pageSize,
                               @RequestParam(defaultValue = "1") int pageNo ) {
        teacher.setPage(new Page(pageNo,pageSize));
        if (StringUtils.isBlank(teacher.getStatus())){
            teacher.disableStatus();
            teacher.setStatus_in(new String[]{"0","2"});
        }
        Page<Teacher> page = teacherService.findPage(teacher);
        List<Teacher> teacherList = page.getList();
        teacherList.forEach(teacherItem->{
            teacherItem.setUserPassword(teacherItem.getPassword());
        });
        return success(page);
    }

    @ApiOperation(value = "新增或更新教师")
    @PostMapping(value = "save")
    public ResultBean<Teacher> save(Teacher teacher){
       try{
           teacher.setUserName(teacher.getTeacherName());
           teacherService.save(teacher);
       }catch (Exception e){
           return error(e.getMessage());
       }
        return success(teacher);
    }

    @ApiOperation(value = "移除教师")
    @PostMapping(value = "remove")
    public ResultBean remove(String teacherIds){
        String [] teacherArr = teacherIds.split(",");
        for (String teacherId: teacherArr){
            Teacher teacher = teacherService.get(teacherId);
            teacherService.delete(teacher);
            // 课堂及班级将停用
            SpringUtils.getApplicationContext()
                    .publishEvent(new TeacherEvent(this,TeacherEvent.DELETE_TEACHER,teacher));
        }
        return success();
    }


    @ApiOperation(value = "停用教师")
    @PostMapping("/disable")
    @Transactional
    public ResultBean disable(String teacherIds){
        String [] teacherArr = teacherIds.split(",");
        for (String teacherId: teacherArr){
            Teacher teacher = teacherService.get(teacherId);
            teacherService.disable(teacher);
            // 课堂及班级将停用
            SpringUtils.getApplicationContext()
                    .publishEvent(new TeacherEvent(this,TeacherEvent.DISABLE_TEACHER,teacher));
        }
        return success();
    }

    @ApiOperation(value = "启用教师")
    @PostMapping("/enable")
    @Transactional
    public ResultBean enable(String teacherIds){
        String [] teacherArr = teacherIds.split(",");
        for (String teacherId: teacherArr){
            Teacher teacher = teacherService.get(teacherId);
            teacherService.enable(teacher);
        }
        return success();
    }

    @ApiOperation(value = "重置密码")
    @PostMapping("/repeatPassword")
    public ResultBean repeatPassword(@RequestParam String teacherIds){
        String [] teacherArr = teacherIds.split(",");
        for (String teacherId: teacherArr){
            BaseUser baseUser = baseUserService.get(teacherId);
            baseUser.setPassword(Global.getConfig("sys.user.initPassword", "123456"));
            baseUserService.update(baseUser);
        }
        return success();
    }


    @ApiOperation(value = "上传用户头像")
    @PostMapping("/uploadHeadImg")
    public ResultBean uploadHeadImg(HttpServletRequest request, @RequestParam(value = "file", required = true) MultipartFile file) {
        String realPath = null;
        String executeUpload = null;
        try {
            realPath =Global.getConfig("web.upload-path")+ Global.getConfig("uploadF.userHeadPath");
            FileUtils.createDirectory(realPath);
            executeUpload = uplocadUtil.executeUpload(realPath, file,Global.getConfig("uploadF.userHeadPath"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success(executeUpload);
    }

    @ApiOperation(value = "随机头像")
    @PostMapping("/randomHeadImg")
    public ResultBean randomHeadImg() {
        UserHead userHead = userHeadService.getUserHead(1);
        return success(userHead);
    }

    @ApiOperation(value = "头像列表")
    @PostMapping("/HeadImg")
    public ResultBean HeadImg() {
        UserHead head = new UserHead();
        head.setHeadType(1);
        List<UserHead> list = userHeadService.findList(head);
        return success(list);
    }

    @ApiOperation(value = "下载导入教师信息模板")
    @PostMapping(value = "importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException {
        List<String> academyAndMajorList = ListUtils.newArrayList();

        List<Academy> academyList = academyService.findList(new Academy());
        academyList.forEach(academy -> {
            Major majorCriteria = new Major();
            majorCriteria.setAcademyId(academy.getAcademyId());
            List<Major> majorList = majorService.findList(majorCriteria);
            majorList.forEach(major -> {
                academyAndMajorList.add(academy.getAcademyName() + "/" + major.getMajorName());
            });
        });


        String fileName = "教师信息模板.xlsx";
        InputStream inputStream = ResourceUtils.getResourceFileStream("classpath:/other/importTeacherTlt.xlsx");
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);
        XSSFSheet xssfSheet = xssfWorkbook.getSheet("data");

        for (int index=0; index< academyAndMajorList.size(); index ++){
            XSSFCell xssfCell = xssfSheet.createRow(index).createCell(0);
            xssfCell.setCellValue(academyAndMajorList.get(index));
        }

        response.reset();
        response.setContentType("application/octet-stream; charset=utf-8");
        response.addHeader("Content-Disposition", "attachment; filename*=utf-8'zh_cn'"+ EncodeUtils.encodeUrl(fileName));
        OutputStream outputStream = response.getOutputStream();

        xssfWorkbook.write(outputStream);
    }

    @ApiOperation(value = "导入教师信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "Excel文件", required = true),
    })
    @PostMapping(value = "importData")
    @ResponseBody
    public ResultBean importData(MultipartFile file) {
        try {
            List<Teacher> teacherList = ListUtils.newArrayList();
            String message = teacherService.importData(file,teacherList);
            return success(teacherList,message);
        } catch (Exception ex) {
            return error(ex.getMessage());
        }
    }

}
