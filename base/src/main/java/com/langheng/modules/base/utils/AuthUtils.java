package com.langheng.modules.base.utils;

import com.jeesite.common.config.Global;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * @ClassName JwtUtils
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-03-23 10:57
 * @Version 1.0
 */
public class AuthUtils {

    private static final PathMatcher pathMatcher = new AntPathMatcher();

    public static boolean isMatchAuthPath(String servletPath){
        String urlStr = Global.getConfig("jwt.authorised-urls");
        String[] urls = urlStr.split(",");
        for (String url : urls) {
            if (pathMatcher.match(url, servletPath)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isMatchVerifyPath(String servletPath) {
        String urlStr = Global.getConfig("jwt.verify-urls");
        String[] urls = urlStr.split(",");
        for (String url : urls) {
            if (pathMatcher.match(url, servletPath)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isMatchPath(String  matchPath,String servletPath) {
        return pathMatcher.match(matchPath, servletPath);
    }
}
