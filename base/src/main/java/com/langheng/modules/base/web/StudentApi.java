package com.langheng.modules.base.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.enumn.RedisKeyConst;
import com.jeesite.common.io.FileUtils;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.RedisUtils;
import com.jeesite.common.web.BaseApiController;
import com.jeesite.framework.uitls.IpUtils;
import com.langheng.modules.base.entity.BaseUser;
import com.langheng.modules.base.entity.Student;
import com.langheng.modules.base.entity.UserHead;
import com.langheng.modules.base.enumn.StudentOriginType;
import com.langheng.modules.base.service.BaseUserService;
import com.langheng.modules.base.service.StudentService;
import com.langheng.modules.base.service.UserHeadService;
import com.langheng.modules.base.utils.BaseUserUtils;
import com.langheng.modules.base.utils.RegisterUtils;
import com.langheng.modules.base.utils.StudentUtils;
import com.langheng.modules.base.utils.uplocadUtil;
import com.wf.captcha.utils.CaptchaUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName StudentController
 * @Description TODO
 * @Author xiaoxie
 * @Date 2019-12-14 10:37
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/student")
@Api(description = "学生相关接口 | 学生端")
public class StudentApi extends BaseApiController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private BaseUserService baseUserService;
    @Autowired
    private UserHeadService userHeadService;
    @Value("${web.upload-path}")
    private String filePath;  //获取上传地址
    @Value("${uploadF.userHeadPath}")
    public  String logo;

    private static Logger logger = LoggerFactory.getLogger(StudentApi.class);

    /**
     * 获取数据
     */
    @ModelAttribute
    public Student get(String id, boolean isNewRecord) {
        return studentService.get(id, isNewRecord);
    }

    @ApiOperation(value = "学生注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginCode", value = "登录账号", required = true),
            @ApiImplicitParam(name = "studentName", value = "学生姓名", required = true),
            @ApiImplicitParam(name = "validCode", value = "验证码", required = true),
    })
    @PostMapping(value = "register")
    @Transactional
    public ResultBean<Student> register(@RequestParam String loginCode,
                                        @RequestParam String studentName,
                                        @RequestParam String validCode,
                                        HttpServletRequest request){
        boolean isValidate = CaptchaUtil.ver(validCode,request);
        if (!isValidate){
            return error("验证码不正确！");
        }

        //校验登陆名是否存在
        baseUserService.checkLoginCode("",loginCode);
        // 校验注册频率
        checkRegisterFrequency(request);

        Student student = new Student();
        student.setLoginCode(loginCode);
        student.setStudentName(studentName);
        student.setPassword("666666");
        student.setOriginType(StudentOriginType.INVITE.value());
        student.setHost(IpUtils.getIpAddress(request));
        studentService.save(student);

        afterRegister(request);
        // 记录注册
        RegisterUtils.recordRegister(student);
        // 清除验证码
        CaptchaUtil.clear(request);
        return success(student);
    }

    private void checkRegisterFrequency(HttpServletRequest request){
        String registerIp = IpUtils.getIpAddress(request);
        if (StringUtils.isNotBlank(registerIp)){
            registerIp = registerIp.replaceAll("\\.","_");
            Long registerTime = (Long) RedisUtils.get(RedisKeyConst.SYS_REGISTER_ID_KEY_PREFIX + registerIp);
            if (registerTime!= null){
                Integer minute = Integer.parseInt(Global.getConfig("sys.account.register.limit.frequency", "3"));
                if (new Date().before(DateUtils.addMinutes(new Date(registerTime),minute))){
                    String errMsg =" 注册太过频繁，请3分钟再试!";
                    logger.info( registerIp +  errMsg);
                    throw new ValidationException(errMsg);
                }
            }
        }
    }

    private void afterRegister(HttpServletRequest request){
        String registerIp = IpUtils.getIpAddress(request);
        if (StringUtils.isNotBlank(registerIp)){
            registerIp = registerIp.replaceAll("\\.","_");
            Integer minute = Integer.parseInt(Global.getConfig("sys.account.register.limit.frequency", "3"));
            Date limitTime = DateUtils.addMinutes(new Date(),minute);
            RedisUtils.set(RedisKeyConst.SYS_REGISTER_ID_KEY_PREFIX + registerIp, limitTime.getTime());
            RedisUtils.expire(RedisKeyConst.SYS_REGISTER_ID_KEY_PREFIX + registerIp, minute, TimeUnit.MINUTES);
        }
    }

    @ApiOperation(value = "学生获取自己的信息")
    @PostMapping("/getInfo")
    public ResultBean getInfo(){
        BaseUser baseUser = BaseUserUtils.getUser();
        Student student = studentService.get(baseUser.getId());
        return success(student);
    }

    @ApiOperation(value = "学生修改自己的信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "studentName",value = "姓名"),
            @ApiImplicitParam(name = "motto",value = "个性签名"),
            @ApiImplicitParam(name = "coverImg",value = "头像路径"),
    })
    @PostMapping("/updateStudent")
    public ResultBean updateStudent( @RequestParam String studentName,
                                     @RequestParam String motto,
                                     @RequestParam String coverImg){
        Student student = StudentUtils.getStudent();
        student.setStudentName(studentName);
        student.setUserName(studentName);
        student.setMotto(motto);
        student.setCoverImg(coverImg);
        studentService.save(student);
        return success();
    }


    @ApiOperation(value = "学生修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "originalPassword",value = "原密码"),
            @ApiImplicitParam(name = "newPassword",value = "新密码"),
            @ApiImplicitParam(name = "confirmPassword",value = "重复确认密码"),
    })
    @PostMapping("/updatePassword")
    public ResultBean updatePassword( @RequestParam String originalPassword,
                                     @RequestParam String newPassword,
                                     @RequestParam String confirmPassword){
        Student student = StudentUtils.getStudent();
        if (originalPassword.equals(newPassword)){
            return error("新密码不能和旧密码不能一样！");
        }
        if (!originalPassword.equals(student.getPassword())){
            return error("原密码错误！");
        }
        if (StringUtils.isBlank(newPassword)
                || StringUtils.isBlank(confirmPassword)){
            return error("密码不能为空！");
        }
        if (!confirmPassword.equals(newPassword)){
            return error("两次密码不一致！");
        }
        student.setPassword(newPassword);
        studentService.save(student);
        return success();
    }

    @ApiOperation(value = "随机头像")
    @PostMapping("/randomHeadImg")
    public ResultBean randomHeadImg() {
        UserHead userHead = userHeadService.getUserHead(0);
        return success(userHead);
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

}
