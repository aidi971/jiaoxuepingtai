package com.langheng.modules.base.config;

import com.jeesite.common.entity.TokenInfo;
import com.jeesite.common.enumn.RedisKeyConst;
import com.jeesite.common.exception.AuthorizationException;
import com.jeesite.common.exception.LicenseException;
import com.jeesite.common.exception.PermissionException;
import com.jeesite.common.exception.VerifyCodeException;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.JsonUtils;
import com.jeesite.common.utils.RedisUtils;
import com.jeesite.common.web.response.GlobalStatusCode;
import com.jeesite.common.web.utils.JsonResultUtils;
import com.langheng.modules.base.enumn.UserType;
import com.langheng.modules.base.utils.AuthUtils;
import com.langheng.modules.base.utils.BaseUserUtils;
import com.langheng.modules.base.utils.JwtUtils;
import com.langheng.modules.base.utils.LicenseUtils;
import com.wf.captcha.utils.CaptchaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @ClassName AuthIntercepter
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-03-26 16:07
 * @Version 1.0
 */
public class BaseAuthInterceptor implements HandlerInterceptor {

    Logger logger = LoggerFactory.getLogger(BaseAuthInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        boolean ifNeedToVerifyCode = AuthUtils.isMatchVerifyPath(request.getServletPath());

        try {
            // 授权验证
            LicenseUtils.checkLicense(true);

            TokenInfo tokenInfo = JwtUtils.validateTokenAndGetTokenInfo(request, response);
            Assert.notNull(tokenInfo,"身份认证失败！");

            // 校验token状态
            verifyToken(tokenInfo);

            // 校验操作验证码
            if (ifNeedToVerifyCode){
                verifyCode(request,response);
            }
        }catch (LicenseException e){
            returnJson(response,JsonUtils.getJsonString(JsonResultUtils.errorResult(e.getMessage(),GlobalStatusCode.CODE_LICENSE_ERROR.code())));
            return false;
        }catch (AuthorizationException e){
            returnJson(response,JsonUtils.getJsonString(JsonResultUtils.errorResult(e.getMessage(),GlobalStatusCode.CODE_AUTH_ERROR.code())));
            return false;
        }catch (ValidationException e){
            returnJson(response,JsonUtils.getJsonString(JsonResultUtils.errorResult(e.getMessage(),GlobalStatusCode.CODE_INVALID_PARAMETER.code())));
            return false;
        }catch (VerifyCodeException e){
            returnJson(response,JsonUtils.getJsonString(JsonResultUtils.errorResult(e.getMessage(),GlobalStatusCode.CODE_VERIFY_ERROR.code())));
            return false;
        }catch (Exception e){
            e.printStackTrace();
            returnJson(response,JsonUtils.getJsonString(JsonResultUtils.errorResult(GlobalStatusCode.CODE_AUTH_ERROR)));
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    /**
     * 校验权限
     * @param tokenInfo
     */
    private void verifyPermission(HttpServletRequest request,TokenInfo tokenInfo){
        String servletPath = request.getServletPath();
        boolean isMatchStudentPath = AuthUtils.isMatchPath("/api/**",servletPath);
        boolean isMatchTeacherOrAdminPath = AuthUtils.isMatchPath("/admin/**",servletPath);
        if (isMatchStudentPath && !UserType.STUDENT.value().equals(tokenInfo.getUserType())){
            throw new PermissionException("您没有学生权限！");
        }
        if(isMatchTeacherOrAdminPath
                && !(UserType.TEACHER.value().equals(tokenInfo.getUserType())
                || UserType.ADMIN.value().equals(tokenInfo.getUserType()))){
            throw new PermissionException("您没有教师权限,或者管理员权限！");
        }
    }

    /**
     * 校验验证码
     * @param request
     */
    private void verifyCode(HttpServletRequest request,HttpServletResponse response) {
        String validCode = request.getParameter("validCode");
        if (StringUtils.isBlank(validCode)) {
            response.setHeader("validCode", "true");
            throw new VerifyCodeException("请输入验证码");
        }

        boolean isValidate = CaptchaUtil.ver(validCode,request);
        if (!isValidate) {
            response.setHeader("validCode", "true");
            throw new ValidationException("验证码不匹配，请重新输入");
        }else {
            // 清除验证码
            CaptchaUtil.clear(request);
        }
    }

    /**
     * 校验token是否有效  返回true为校验通过
     *
     * @param tokenInfo
     * @return
     */
    public void verifyToken(TokenInfo tokenInfo) {
        // 校验用户登录状态
        BaseUserUtils.checkLoginState(tokenInfo);

        // 要存在 最晚登录的token
        String tokenRedisKey = RedisKeyConst.USER_TOKEN_KEY_PREFIX + tokenInfo.getUserId();
        String lastUpdateToken = (String) RedisUtils.get(tokenRedisKey);
        if (StringUtils.isBlank(lastUpdateToken)) {
            throw new AuthorizationException("token已经过期！");
        }

        if (!tokenInfo.getToken().equals(lastUpdateToken)) {
            throw new AuthorizationException("token已经过期！");
        }

        // 重置token有效时间。
        JwtUtils.refreshTokenCache(tokenInfo.getUserId());
    }

    private void returnJson(HttpServletResponse response, String json){
        PrintWriter writer = null;
        response.setContentType("application/json;charset=UTF-8");
        try {
            writer = response.getWriter();
            writer.print(json);
        } catch (IOException e) {
            logger.error("response error",e);
        }finally {
            if (writer != null){
                writer.close();
            }
        }
    }
}
