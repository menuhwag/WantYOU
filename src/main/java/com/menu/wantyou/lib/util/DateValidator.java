package com.menu.wantyou.lib.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateValidator {

    public static void valid(String birthYear, String birthDay) throws IllegalArgumentException{
        if (!existBirthYear(birthYear)) return;
        if (!existBirthDay(birthDay)) return;
        parseDate(toStringDate(birthYear, birthDay));
    }

    private static String toStringDate(String birthYear, String birthDay) {
        String month = parseMonth(birthDay);
        String day = parseDay(birthDay);
        return birthYear + "-" + month + "-" + day;
    }

    private static String parseMonth(String birthDay) {
        return birthDay.substring(0,2);
    }

    private static String parseDay(String birthDay) {
        return birthDay.substring(2);
    }

    private static boolean existBirthYear(String birthYear) {
        return birthYear != null;
    }

    private static boolean existBirthDay(String birthDay) {
        return birthDay != null;
    }

    private static void parseDate(String date) throws IllegalArgumentException {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            format.setLenient(false);
            format.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException("유효하지 않은 생일입니다.");
        }
    }
}
