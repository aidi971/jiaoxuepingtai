package com.langheng.modules.base.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName ValidCodeUtils
 * @Description TODO
 * @Author xiaoxie
 * @Date 2019-12-18 18:29
 * @Version 1.0
 */
public class ValidCodeUtils {
    public static final String VALID_CODE = "validCode";

    public ValidCodeUtils() {
    }

    public static boolean validate(HttpServletRequest request, String validCode) {
        return validate(request, "validCode", validCode);
    }

    public static boolean validate(HttpServletRequest request, String attributeName, String validCode, boolean isClear) {
        String attributeValue = (String)request.getSession().getAttribute(attributeName);
        boolean isValid = validCode != null && validCode.equalsIgnoreCase(attributeValue);
        if (isValid && isClear) {
            request.getSession().removeAttribute(attributeName);
        }

        return isValid;
    }

    public static boolean validate(HttpServletRequest request, String attributeName, String validCode) {
        return validate(request, attributeName, validCode, true);
    }
}
