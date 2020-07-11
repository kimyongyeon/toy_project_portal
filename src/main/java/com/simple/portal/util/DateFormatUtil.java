package com.simple.portal.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 날짜 포맷팅 유틸
 */
public class DateFormatUtil {

    private static class TIME_MAXIMUM {
        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int HOUR = 24;
        public static final int DAY = 30;
        public static final int MONTH = 12;
    }


    // todo: Date to 날짜문자열 : new Date() -> 2020-06-10
    public static String getDateFormat(Date today, String format) {
        SimpleDateFormat format1, format2, format3, format4, format5, format6, format7;
        format1 = new SimpleDateFormat("yyyy-MM-dd");
        format3 = new SimpleDateFormat("yy/MM/dd");
        format2 = new SimpleDateFormat("yyyy년 MM월 dd일 E요일");
        format4 = new SimpleDateFormat("HH:mm:ss");
        format5 = new SimpleDateFormat("hh:mm:ss a");
        format6 = new SimpleDateFormat("오늘은 yyyy년의 w주차이며 D번째 날입니다.");
        format7 = new SimpleDateFormat("오늘은 M월의 w번째 주, d번째 날이며, F번째 E요일입니다.");
//        System.out.println(format1.format(today));
//        System.out.println(format2.format(today));
//        System.out.println(format3.format(today));
//        System.out.println(format4.format(today));
//        System.out.println(format5.format(today));
//        System.out.println(format6.format(today));
//        System.out.println(format7.format(today));
        return format1.format(today);
    }

    // todo: Date to 날짜시간문자열 : new Date() -> 2020-06-10 00:00:00
    public static String getDateTimeFormat(Date today) {

        // Calendar클래스의 getInstance()메서드를 활용하는 방법
//        SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
//        SimpleDateFormat format2 = new SimpleDateFormat ( "yyyy년 MM월dd일 HH시mm분ss초");
//
//        Calendar time = Calendar.getInstance();
//
//        String format_time1 = format1.format(time.getTime());
//        String format_time2 = format2.format(time.getTime());
//
//        System.out.println(format_time1);
//        System.out.println(format_time2);

        // System클래스의 currentTimeMillis()메서드를 활용하는 방법
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy년 MM월dd일 HH시mm분ss초");

        String format_time1 = format1.format(System.currentTimeMillis());
        String format_time2 = format2.format(System.currentTimeMillis());

//        System.out.println(format_time1);
//        System.out.println(format_time2);

        return format_time1;
    }

    // todo: 시간계산 = 현재시간 - 과거시간
    public static String getTimeBefore(Date tempDate) {
        long curTime = System.currentTimeMillis();
        long regTime = tempDate.getTime();
        long diffTime = (curTime - regTime) / 1000;


        String msg = null;
        if (diffTime < TIME_MAXIMUM.SEC) {
            // sec
            msg = "방금 전";

        } else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
            // min
            msg = diffTime + "분 전";

        } else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
            // hour
            msg = (diffTime) + "시간 전";

        } else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
            // day
            msg = (diffTime) + "일 전";

        } else if ((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH) {
            // day
            msg = (diffTime) + "달 전";

        } else {
            msg = (diffTime) + "년 전";

        }

        return msg;
    }

    // 출력 시간 format
    private static SimpleDateFormat time_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //현재시간 Date -> String
    public static String makeNowTimeStamp( ) {
        Date date = new Date( );
        String now = time_format.format(date);
        return now;
    }
}
