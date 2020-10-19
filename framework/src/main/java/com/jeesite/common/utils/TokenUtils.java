package com.jeesite.common.utils;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.TokenInfo;
import com.jeesite.common.enumn.RedisKeyConst;
import io.jsonwebtoken.Jwts;

import java.util.Date;
import java.util.Map;

/**
 * @ClassName TokenUtils
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-03-30 17:19
 * @Version 1.0
 */
public class TokenUtils {

    public static TokenInfo validateTokenAndGetTokenInfo(String token) {
        try {
            Map<String, Object> body = Jwts.parser()
                    .setSigningKey(Global.getConfig("jwt.secret"))
                    .parseClaimsJws(token).getBody();
            String userId = (String)body.get("userId");
            String redisToken = (String) RedisUtils.get(RedisKeyConst.USER_TOKEN_KEY_PREFIX + userId);
            if (!token.equals(redisToken)){
                return null;
            }
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

}