package com.stock.process.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author Nabeel Ahmed
 */
@Component
public class BarcoUtil {

    public static Logger logger = LogManager.getLogger(BarcoUtil.class);

    public static String SIMPLE_DATE_PATTERN = "yyyy-MM-dd";
    public static String CONTENT_DISPOSITION ="Content-Disposition";
    public static String ERROR = "ERROR";
    public static String SUCCESS = "SUCCESS";

    public BarcoUtil() {}

    public static boolean isNull(Object payload) {
        return payload == null || payload == "";
    }

    public static boolean isNotBlank(String s) {
        return !isBlank(s);
    }

    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static boolean isNull(Long log) {
        return log == null;
    }

    public static boolean isNull(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNull(Boolean bool) {
        return bool == null;
    }

    public static boolean isNull(Double dou) {
        return dou == null;
    }

    public static boolean isNull(Date dt) {
        if (dt == null) {
            return true;
        } else if (String.valueOf(dt) == null) {
            return true;
        } else if (String.valueOf(dt).trim().length() <= 0) {
            return true;
        }
        return false;
    }

    /**
     * Method use to get the current month
     * */
    public static String currentMonth() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }

}