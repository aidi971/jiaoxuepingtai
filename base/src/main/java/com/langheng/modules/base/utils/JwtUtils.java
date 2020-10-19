//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.langheng.modules.base.utils;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.TokenInfo;
import com.jeesite.common.enumn.RedisKeyConst;
import com.jeesite.common.enumn.TokenConst;
import com.jeesite.common.utils.RedisUtils;
import com.langheng.modules.base.entity.BaseUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class JwtUtils {
    private static Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    public JwtUtils() {
    }

    public static String generateToken(Map<String, Object> claims) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(12, Integer.parseInt(Global.getConfig("jwt.expire")));
        Date expireTime = calendar.getTime();
        String jwt = Jwts.builder().setClaims(claims).setExpiration(expireTime).signWith(SignatureAlgorithm.HS512, Global.getConfig("jwt.secret")).compact();
        return jwt;
    }

    public static TokenInfo validateTokenAndGetTokenInfo(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(TokenConst.HEADER_TOKEN_KEY);
        return validateTokenAndGetTokenInfo(token);
    }

    public static TokenInfo validateTokenAndGetTokenInfo(String token) {
        try {
            Map<String, Object> body = Jwts.parser()
                    .setSigningKey(Global.getConfig("jwt.secret"))
                    .parseClaimsJws(token).getBody();
            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setUserId((String)body.get("userId"));
            tokenInfo.setLoginCode((String)body.get("loginCode"));
            tokenInfo.setUserName((String)body.get("userName"));
            tokenInfo.setUserType((String)body.get("userType"));
            tokenInfo.setToken(token);
            long exp = 1000L * (long)(Integer)body.get("exp");
            tokenInfo.setExpireTime(new Date(exp));
            return tokenInfo;
        } catch (Exception var7) {
            return null;
        }
    }

    public static TokenInfo checkIsHadLogin(BaseUser baseUser) {
        String tokenRedisKey = RedisKeyConst.USER_TOKEN_KEY_PREFIX + baseUser.getUserCode();
        String token = (String) RedisUtils.get(tokenRedisKey);
        if (StringUtils.isNotBlank(token)) {
            return JwtUtils.validateTokenAndGetTokenInfo(token);
        }

        return null;
    }

    public static TokenInfo generateTokenAndCache(BaseUser baseUser) {
        Map<String, Object> claims = new HashMap();
        claims.put("userId", baseUser.getUserCode());
        claims.put("loginCode", baseUser.getLoginCode());
        claims.put("userName", baseUser.getUserName());
        claims.put("userType", baseUser.getUserType());
        String token = generateToken(claims);
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setToken(token);
        tokenInfo.setUserId(baseUser.getUserCode());
        tokenInfo.setLoginCode(baseUser.getLoginCode());
        tokenInfo.setUserName(baseUser.getUserName());
        tokenInfo.setUserType(baseUser.getUserType());
        tokenInfo.setLoginTime(new Date());
        tokenInfo.setExpireTime(DateUtils.addMinutes(new Date(), (new Long(Global.getConfig("jwt.expire"))).intValue()));
        String tokenRedisKey = RedisKeyConst.USER_TOKEN_KEY_PREFIX + baseUser.getUserCode();
        RedisUtils.set(tokenRedisKey, token);
        // 重置token有效时间。
        refreshTokenCache(baseUser.getUserCode());
        return tokenInfo;
    }

    public static void removeLockAccount(TokenInfo tokenInfo) {
        String redisLockAccountKey = RedisKeyConst.USER_LOCK_ACCOUNT_KEY_PREFIX + tokenInfo.getUserId();
        RedisUtils.del(new String[]{redisLockAccountKey});
    }

    public static void removeTokenInfo(TokenInfo tokenInfo){
        String cacheToken = (String) RedisUtils.get(RedisKeyConst.USER_TOKEN_KEY_PREFIX + tokenInfo.getUserId());
        if (tokenInfo.getToken().equals(cacheToken)) {
            removeTokenInfo(tokenInfo.getUserId());
        }
    }

    public static void removeTokenInfo(String userId){
        if (StringUtils.isNotBlank(userId)){
            String tokenRedisKey = RedisKeyConst.USER_TOKEN_KEY_PREFIX + userId;
            RedisUtils.del(new String[]{tokenRedisKey});
        }
    }

    /**
     * 重置token有效时间。
     * @param userId
     */
    public static void refreshTokenCache(String userId){
        RedisUtils.expire(RedisKeyConst.USER_TOKEN_KEY_PREFIX + userId,
                new Long(Global.getConfig("jwt.tokenCacheExpire")), TimeUnit.MINUTES);
    }
}
