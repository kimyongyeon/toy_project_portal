package com.simple.portal.util;

/**
 * 마스킹 유틸
 */
public class MaskingUtil {

    private static final String EMAIL_PATTERN = "([\\w.])(?:[\\w.]*)(@.*)";
    private static final String LASTNAME_PATTERN = "(?<=.{0}).";
    private static final String LAST_6_CHAR_PATTERN = "(.{6}$)";

    /**
     * 이름
     * String firstName = "Steve";
     * String lastName = "Jobs";
     * Steve ****
     * @param firstName
     * @param lastName
     * @return
     */
    public static String getNameMasking(String firstName, String lastName) {
        return firstName + " " + lastName.replaceAll(LASTNAME_PATTERN , "*");
    }

    /**
     * 이메일
     * String email = "test@test.com";
     * t****@test.com
     * @param email
     * @return
     */
    public static String getEmailMasking(String email) {
        return email.replaceAll(EMAIL_PATTERN, "$1****$2");
    }

    /**
     * 주민번호
     * String certNum = "123456-1234567";
     * 123456-1******
     * @param certNum
     * @return
     */
    public static String getCertNumMasking(String certNum) {
        return certNum.replaceAll(LAST_6_CHAR_PATTERN, "******");
    }

    /**
     * 여권번호
     * String passPortNum = "M12345678";
     * M12******
     * @param passPortNum
     * @return
     */
    public static String getPassPortNumMasking(String passPortNum) {
        return passPortNum.replaceAll(LAST_6_CHAR_PATTERN, "******");
    }

    /**
     * 전화번호
     * 휴대폰 번호 마스킹(010****1234 / 011***1234)
     * @param phoneNumber
     * @return
     */
    public static String getPhoneNumberMasking(String phoneNumber){

        String maskedPhoneNum = phoneNumber;
        // 공백제거
        maskedPhoneNum = maskedPhoneNum.replaceAll(" ", "");

        // '-'가 포함되어있으면 모두 삭제
        if(maskedPhoneNum.contains("-")){
            maskedPhoneNum = maskedPhoneNum.replaceAll("[^0-9]", "");
        }

        // 11자리 또는 10자리가 되지 않으면 공백 ""
        if(maskedPhoneNum.length() == 11 || maskedPhoneNum.length() < 10){
            maskedPhoneNum = "";
        }else{
            // 11자리 휴대폰 번호 마스킹 처리
            if(maskedPhoneNum.length() == 11){
                String num1 = maskedPhoneNum.substring(0, 3);
                String num3 = maskedPhoneNum.substring(7);

                maskedPhoneNum = num1 + "****" + num3;
                // 10자리 휴대폰 번호 마스킹 처리
            }else if(maskedPhoneNum.length() == 10){
                String num1 = maskedPhoneNum.substring(0, 3);
                String num3 = maskedPhoneNum.substring(6);
                maskedPhoneNum = num1 + "***" + num3;
            }
        }

        return maskedPhoneNum;
    }


}
