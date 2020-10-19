package com.langheng.modules.base.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.io.FileUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseApiController;
import com.langheng.modules.base.entity.Teacher;
import com.langheng.modules.base.entity.UserHead;
import com.langheng.modules.base.service.TeacherService;
import com.langheng.modules.base.service.UserHeadService;
import com.langheng.modules.base.utils.TeacherUtils;
import com.langheng.modules.base.utils.uplocadUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/teacher")
@Api(description = "教师相关接口 | 教师端")
public class TeacherApi extends BaseApiController {
    
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private UserHeadService userHeadService;
    @Value("${web.upload-path}")
    private String filePath;  //获取上传地址
    @Value("${uploadF.userHeadPath}")
    public  String logo;

    @ApiOperation(value = "教师修改自己的信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "teacherName",value = "姓名"),
            @ApiImplicitParam(name = "coverImg",value = "头像路径")
    })
    @PostMapping("/updateTeacher")
    public ResultBean updateTeacher(@RequestParam String teacherName,
                                    @RequestParam String coverImg){
        Teacher teacher = TeacherUtils.getTeacher();
        teacher.setTeacherName(teacherName);
        teacher.setUserName(teacherName);
        teacher.setCoverImg(coverImg);
        teacherService.save(teacher);
        return success();
    }


    @ApiOperation(value = "教师修改自己密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "originalPassword",value = "原密码"),
            @ApiImplicitParam(name = "newPassword",value = "新密码"),
            @ApiImplicitParam(name = "confirmPassword",value = "重复确认密码"),
    })
    @PostMapping("/updatePassword")
    public ResultBean updatePassword(@RequestParam String originalPassword,
                                    @RequestParam String newPassword,
                                    @RequestParam String confirmPassword){
        Teacher teacher = TeacherUtils.getTeacher();
        if (originalPassword.equals(newPassword)){
            return error("新密码不能和旧密码不能一样！");
        }
        if (!originalPassword.equals(teacher.getPassword())){
            return error("原密码错误！");
        }
        if (StringUtils.isBlank(newPassword)
                || StringUtils.isBlank(confirmPassword)){
            return error("密码不能为空！");
        }
        if (!confirmPassword.equals(newPassword)){
            return error("两次密码不一致！");
        }
        teacher.setPassword(newPassword);
        teacherService.save(teacher);
        return success();
    }

    @ApiOperation(value = "上传用户头像")
    @PostMapping("/uploadHeadImg")
    public ResultBean uploadHeadImg(HttpServletRequest request, @RequestParam(value = "file", required = true) MultipartFile file) {
        String realPath = null;
        String executeUpload = null;
        try {
            realPath =filePath+logo;
            FileUtils.createDirectory(realPath);
            executeUpload = uplocadUtil.executeUpload(realPath, file, Global.getConfig("uploadF.userHeadPath"));
        } catch (Exception e) {
            e.printStackTrace();
            return error("上传失败!");
        }
        return success(executeUpload);
    }

    @ApiOperation(value = "随机头像")
    @PostMapping("/randomHeadImg")
    public ResultBean randomHeadImg() {
        UserHead userHead = userHeadService.getUserHead(1);
        return success(userHead);
    }
    
}
