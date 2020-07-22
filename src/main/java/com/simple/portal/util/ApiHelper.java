package com.simple.portal.util;

import java.util.Random;

public class ApiHelper {

    // 영어 대소문자, 숫자, 특수문자를 포함한 10자리 랜덤 문자열 생성 ( 비밀번호 찾기 )
    public static String getRandomString( ) {

        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<10; i++) {
            int index = random.nextInt(4);
            switch (index) {
                case 0: // 소문자 ( a ~ z )
                    sb.append((char)(random.nextInt(26) + 97));
                    break;
                case 1: // 대문자 ( A ~ Z )
                    sb.append((char)(random.nextInt(26) + 65));
                    break;
                case 2: // 숫자 ( 0 ~ 1 )
                    sb.append(random.nextInt(10));
                    break;
                case 3: // 특수문자 ( 아스키 코드표 33 ~ 47번까지.. 모든 특수문자는 포함 하지 못함 )
                    char randomChar = (char)(random.nextInt(15) + 33);
                    if(randomChar == '"' ) {
                        i--;
                        continue;
                    }
                    sb.append(randomChar);
            }
        }
        return String.valueOf(sb);
    }
}
