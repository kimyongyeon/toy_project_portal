package com.simple.portal.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 정규식 유틸
 */
public class RegularExpressionUtil {

    public static String phoneFixedRegEx = "(010|011|016|017|018|019)(.+)(.{4})";
    public static String phoneRegEx = "(\\d{3})(\\d{3,4})(\\d{4})";
    public static String emailRegEx = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
    public static String nameRegEx = "(?=.*[@#$%&\\s])";
    public static String userIdRegEx = "^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$";

    private static boolean isRegularExpress(String email, String regex) {
        boolean err = false;
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if(m.matches()) {
            err = true;
        }
        return err;
    }

    /**
     * 전화번호/휴대폰 검증
     * @param phoneNoStr
     * @return
     */
    public static boolean isValidPhoneNumber(String phoneNoStr) {
        return isRegularExpress(phoneNoStr, phoneRegEx);
    }

    /**
     * 이메일 검증
     * @param email
     * @return
     */
    public static boolean isValidEmail(String email) {
        return isRegularExpress(email, emailRegEx);
    }

    /**
     * 이름 검증
     * @param str
     * @return
     */
    public static boolean isValidName(String str) {
        return isRegularExpress(str, nameRegEx);

    }

    /**
     * 아이디 검증
     * @param userId
     * @return
     */
    public static boolean isValidUserId(String userId) {
        return isRegularExpress(userId, userIdRegEx);
    }
}
