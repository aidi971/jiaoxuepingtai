package com.langheng.modules.base.web.api;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.entity.TokenInfo;
import com.jeesite.common.enumn.StatusEnum;
import com.jeesite.common.enumn.SysYesNoEnum;
import com.jeesite.common.exception.AuthorizationException;
import com.jeesite.common.exception.LicenseException;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseApiController;
import com.jeesite.common.web.response.GlobalStatusCode;
import com.jeesite.common.web.utils.JsonResultUtils;
import com.jeesite.framework.uitls.IpUtils;
import com.langheng.modules.base.entity.BaseUser;
import com.langheng.modules.base.entity.Student;
import com.langheng.modules.base.enumn.UserType;
import com.langheng.modules.base.manager.AsyncManager;
import com.langheng.modules.base.manager.factory.AsyncFactory;
import com.langheng.modules.base.service.BaseUserService;
import com.langheng.modules.base.service.StudentService;
import com.langheng.modules.base.utils.BaseUserUtils;
import com.langheng.modules.base.utils.JwtUtils;
import com.langheng.modules.base.utils.LicenseUtils;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.base.Captcha;
import com.wf.captcha.utils.CaptchaUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RestController
@RequestMapping("/auth")
@Api(description = "用户授权接口")
public class AuthorizationApi extends BaseApiController {

    @Autowired
    private BaseUserService baseUserService;
    @Autowired
    private StudentService studentService;

    @ApiOperation(value = "判断用户状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginCode", value = "登录账号", required = true),
    })
    @PostMapping("/checkLoginState")
    public ResultBean checkLoginState(@RequestParam String loginCode,
                                              HttpServletRequest request) {
        BaseUser baseUserCriteria = new BaseUser();
        baseUserCriteria.setLoginCode(loginCode);
        BaseUser baseUser = baseUserService.getByLoginCode(baseUserCriteria);
        if (baseUser != null) {
            // 校验用户登录状态
            try{
                BaseUserUtils.checkLoginState(baseUser,request);
            }catch (Exception e){
                return error(e.getMessage());
            }
        }
        return success();
    }

    @ApiOperation(value = "用户登录接口 | 如果上一次登录请求response的header键名为validCode的值为ture，则需要输入验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginCode", value = "登录账号", required = true),
            @ApiImplicitParam(name = "password", value = "密码", required = true),
            @ApiImplicitParam(name = "validCode", value = "验证码", required = false),
            @ApiImplicitParam(name = "isForceLogin", value = "是否强制登录，重复登录情况（0否，1是）", required = false),
    })
    @PostMapping("/login")
    public ResultBean<String> login(@RequestParam String loginCode,
                                    @RequestParam String password, HttpServletRequest request) {
        BaseUser baseUserCriteria = new BaseUser();
        baseUserCriteria.setLoginCode(loginCode);
        BaseUser baseUser = baseUserService.getByLoginCode(baseUserCriteria);
        // 处理多数据源 查询错库情况
        clearDataSourceName(baseUser);
        if (baseUser != null) {
            baseUser = baseUserService.get(baseUser);
            if (StatusEnum.DELETE.value().equals(baseUser.getStatus())
                    || StatusEnum.DISABLE.value().equals(baseUser.getStatus())) {
                return error("该用户被冻结或已删除！");
            }
            try {
                // 校验授权信息
                LicenseUtils.checkLicense(false);
                // 校验用户登录状态
                BaseUserUtils.checkLoginState(baseUser,request);
                // 检查密码是否正确 并处理登录失败
                BaseUserUtils.checkPassword(baseUser,password);
                // 处理多客户端登录情况
                BaseUserUtils.dealLoginMultiClient(baseUser);
                // 判断是否已经登录
                TokenInfo tokenInfo = JwtUtils.checkIsHadLogin(baseUser);
                if (tokenInfo == null
                        || Global.FALSE.equals(Global.getConfig("sys.login.allowLoginMultiClient", "false")) ){
                    // 未登录则重新生成token
                    tokenInfo = JwtUtils.generateTokenAndCache(baseUser);
                }
                // 移除账户锁定账户
                JwtUtils.removeLockAccount(tokenInfo);

                boolean isFirstLogin = false;
                // 记录登录
                baseUser.setLastHost(baseUser.getHost());
                baseUser.setLastLoginDate(baseUser.getLoginDate());
                baseUser.setHost(IpUtils.getIpAddress(request));
                baseUser.setLoginDate(new Date());
                baseUser.setLoginCount(baseUser.getLoginCount() + 1);
                if (checkIsFirstLogin(baseUser)){
                    isFirstLogin = true;
                    baseUser.setFirstLogin(SysYesNoEnum.NO.value());
                }
                baseUserService.save(baseUser);
                // 异步记录用户登录日志
                AsyncManager.me().execute(AsyncFactory.recordLoginLog(baseUser,true,request));

                return success(tokenInfo.getToken(),"登录成功！",isFirstLogin?"true":"false");
            }catch (LicenseException e){
                return JsonResultUtils.errorResult(e.getMessage(), GlobalStatusCode.CODE_LICENSE_ERROR.code());
            }catch (AuthorizationException e){
                return error(e.getMessage());
            }catch (Exception e){
                return error("网络异常，请刷新再试");
            }

        }

        return error("该账号不存在");
    }

    private void clearDataSourceName(BaseUser baseUser) {
        try{
            baseUserService.save(baseUser);
        }catch (Exception e){}
    }

    /**
     * 判断是否是第一次登录
     * @param baseUser
     * @return
     */
    private boolean checkIsFirstLogin(BaseUser baseUser) {
        if (!SysYesNoEnum.NO.value().equals(baseUser.getFirstLogin())){
            // 如果是学生 还要判断是否加入班级
            if (UserType.STUDENT.value().equals(baseUser.getUserType())){
                Student student = studentService.get(baseUser.getId());
                // 只有加入班级  才算是第一次登陆
                if (student != null && StringUtils.isNotBlank(student.getClassesId())){
                    return true;
                }
            }else {
                return true;
            }
        }

        return false;
    }


    @ApiOperation(value = "获取验证码")
    @GetMapping({"/getValidCodeImg"})
    public void validCode(@RequestParam(defaultValue = "4") Integer bit,
                          HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 算术类型
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 48);
        captcha.setFont(Captcha.FONT_6);

        CaptchaUtil.out(captcha, request, response);
    }

}