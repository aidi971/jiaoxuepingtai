//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.langheng.modules.base.utils;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.TokenInfo;
import com.jeesite.common.enumn.RedisKeyConst;
import com.jeesite.common.enumn.SysYesNoEnum;
import com.jeesite.common.enumn.TokenConst;
import com.jeesite.common.exception.AuthorizationException;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.RedisUtils;
import com.jeesite.common.utils.SpringUtils;
import com.langheng.modules.base.entity.BaseUser;
import com.langheng.modules.base.entity.LoginState;
import com.langheng.modules.base.service.BaseUserService;
import com.langheng.modules.base.service.StudentClassesService;
import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class BaseUserUtils {
    private static BaseUserService baseUserService =  SpringUtils.getBean(BaseUserService.class);
    private static StudentClassesService studentClassesService = SpringUtils.getBean(StudentClassesService.class);

    public BaseUserUtils() {
    }

    public static BaseUser getUser() {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        String token = request.getHeader(TokenConst.HEADER_TOKEN_KEY);
        if (StringUtils.isNotBlank(token)) {
            TokenInfo tokenInfo = JwtUtils.validateTokenAndGetTokenInfo(token);
            if (tokenInfo != null && StringUtils.isNotBlank(tokenInfo.getUserId())) {
                return  baseUserService.get(tokenInfo.getUserId());
            }
        }
        return null;
    }

    public static BaseUser getUser(String token) {
        if (StringUtils.isNotBlank(token)) {
            TokenInfo tokenInfo = JwtUtils.validateTokenAndGetTokenInfo(token);
            if (tokenInfo != null && StringUtils.isNotBlank(tokenInfo.getUserId())) {
                return  baseUserService.get(tokenInfo.getUserId());
            }
        }
        return null;
    }

    public static BaseUser get(String userCode) {
        return baseUserService.get(userCode);
    }

    public static boolean isSuperAdmin() {
        BaseUser baseUser = getUser();
        return baseUser != null && StringUtils.isNotBlank(baseUser.getUserCode()) && StringUtils.inString(baseUser.getUserCode(), BaseUser.SUPER_ADMIN_CODE.split(","));
    }

    public static boolean isSuperAdmin(String userCode) {
        return userCode != null && StringUtils.inString(userCode, BaseUser.SUPER_ADMIN_CODE.split(","));
    }

    public static LoginState dealLoginFail(BaseUser baseUser) {
        String redisLockAccountKey = "user:lock:account:" + baseUser.getUserCode();
        LoginState loginState = (LoginState)RedisUtils.get(redisLockAccountKey, LoginState.class);
        if (loginState == null) {
            loginState = new LoginState();
            loginState.setCanLogin(true);
            loginState.setUserId(baseUser.getId());
            loginState.setLoginCode(baseUser.getLoginCode());
            loginState.setUserName(baseUser.getUserName());
            loginState.setFailedNum(0);
        }

        loginState.setFailedNum(loginState.getFailedNum() + 1);
        if (loginState.getFailedNum() >= new Integer(Global.getConfig("sys.login.failedNumAfterValidCode", "5"))) {
            HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getResponse();
            response.setHeader("validCode", "true");
            loginState.setNeedValidCode(true);
        }

        if (loginState.getFailedNum() >= new Integer(Global.getConfig("sys.login.failedNumAfterLockAccount", "5"))) {
            loginState.setCanLogin(false);
            double failedNumAfterLockMinute = new Double(Global.getConfig("sys.login.failedNumAfterLockMinute", "5"));
            int failedNumAfterLockSeconds = (int)(failedNumAfterLockMinute * 60.0D);
            loginState.setLockTime(DateUtils.addSeconds(new Date(), failedNumAfterLockSeconds));
            baseUser.setFreezeDate(DateUtils.addSeconds(new Date(), failedNumAfterLockSeconds));
            baseUserService.update(baseUser);
        }

        RedisUtils.set(redisLockAccountKey, loginState);
        RedisUtils.expire(redisLockAccountKey, 30L, TimeUnit.MINUTES);

        return loginState;
    }

    public static void dealLoginMultiClient(BaseUser baseUser) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        // 判断是否已经登录
        TokenInfo tokenInfo = JwtUtils.checkIsHadLogin(baseUser);
        if (tokenInfo != null){
            String isForceLogin = request.getParameter("isForceLogin");
            if (!SysYesNoEnum.YES.value().equals(isForceLogin)){
                throw new AuthorizationException("该账号已经登录，是否继续?");
            }
        }

    }

    public static void checkLoginState(TokenInfo tokenInfo) {
        BaseUser baseUser = new BaseUser();
        baseUser.setUserCode(tokenInfo.getUserId());
        baseUser.setLoginCode(tokenInfo.getLoginCode());
        baseUser.setUserName(tokenInfo.getUserName());
        checkLoginState(baseUser);
    }

    public static void checkLoginState(BaseUser baseUser) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        checkLoginState(baseUser, request);
    }

    public static void checkLoginState(BaseUser baseUser, HttpServletRequest request) {
        HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getResponse();
        String redisLockAccountKey = RedisKeyConst.USER_LOCK_ACCOUNT_KEY_PREFIX + baseUser.getUserCode();
        LoginState loginState = (LoginState)RedisUtils.get(redisLockAccountKey, LoginState.class);
        if (loginState != null && !loginState.getCanLogin()) {
            Date lockTime = loginState.getLockTime();
            long second = 0L;
            if (lockTime.getTime() > System.currentTimeMillis()) {
                second = (lockTime.getTime() - System.currentTimeMillis()) / 1000L;
                if (loginState.getNeedValidCode()) {
                    response.setHeader("validCode", "true");
                }

                throw new AuthorizationException(String.format("您的账户已被锁定，解锁时间剩余%s秒!", second));
            }
        }

        if (loginState != null && loginState.getNeedValidCode()) {
            String validCode = request.getParameter("validCode");
            if (StringUtils.isBlank(validCode)) {
                response.setHeader("validCode", "true");
                throw new AuthorizationException("请输入验证码");
            }
            boolean isValidate = CaptchaUtil.ver(validCode,request);
            if (!isValidate) {
                response.setHeader("validCode", "true");
                throw new AuthorizationException("验证码不匹配，请重新输入");
            }else {
                // 清除验证码
                CaptchaUtil.clear(request);
            }
        }

    }

    public static void logout(HttpServletRequest request, HttpServletResponse response) {
        TokenInfo tokenInfo = JwtUtils.validateTokenAndGetTokenInfo(request, response);
        if (tokenInfo == null) return;
        String cacheToken = (String) RedisUtils.get(RedisKeyConst.USER_TOKEN_KEY_PREFIX + tokenInfo.getUserId());
        if (tokenInfo.getToken().equals(cacheToken)) {
            JwtUtils.removeTokenInfo(tokenInfo.getUserId());
        }
    }

    public static void checkPassword(BaseUser baseUser,String password) {
        if (!baseUser.getPassword().equals(password)) {
            String passwordErrMsg = "输入密码错误！";
            if (Global.TRUE.equals(Global.getConfig("sys.login.failedLockAccount"))) {
                LoginState loginState = BaseUserUtils.dealLoginFail(baseUser);
                if (loginState.getFailedNum() >= 5){
                    passwordErrMsg += "<br>您的账户已被锁定，解锁时间剩余30秒!";
                }
            }
            throw new AuthorizationException(passwordErrMsg);
        }
    }


    public static void tickOut(String[] userIds) {
        String[] var2 = userIds;
        int var3 = userIds.length;
        List<String> onlineUserIds = ListUtils.newArrayList();
        BaseUser currentUser = BaseUserUtils.getUser();

        for(int var4 = 0; var4 < var3; ++var4) {
            String userId = var2[var4];
            if (!userId.equals(currentUser.getId())) {
                String tokenRedisKey = RedisKeyConst.USER_TOKEN_KEY_PREFIX + userId;
                String token = (String)RedisUtils.get(tokenRedisKey);
                if (StringUtils.isNotBlank(token)) {
                    RedisUtils.del(new String[]{tokenRedisKey});
                    onlineUserIds.add(userId);
                }
            }
        }

        if (!onlineUserIds.isEmpty()){
            // 发送通知踢掉用户
            SystemUtils.tickOutUsers(onlineUserIds);
        }

    }

    public static void tickOut(String userId) {
        if (StringUtils.isNotBlank(userId)){
            JwtUtils.removeTokenInfo(userId);
            // 发送通知踢掉用户
            SystemUtils.tickOutUser(userId);
        }

    }

    public static void tickOutAll() {
        Set<String> redisKeys = RedisUtils.keys("user:token:*");
        List<String> userIdList = new ArrayList();
        Iterator var3 = redisKeys.iterator();

        while(var3.hasNext()) {
            String redisKey = (String)var3.next();
            String userId = redisKey.substring("user:token:".length(), redisKey.length());
            userIdList.add(userId);
        }

        tickOut(userIdList.toArray(new String[0]));
    }
}
