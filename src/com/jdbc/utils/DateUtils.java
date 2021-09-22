package com.jdbc.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//日期的工具类,简化日期格式话操作
public class DateUtils {
    /**
     *
     * @param dateFormat 日期格式
     * @param date 需要格式化的日期
     * @return 返回一个Date类型的日期
     * @throws ParseException
     */
    public static Date getDate(String dateFormat,String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date1 = sdf.parse(date);
        return new Date(date1.getTime());
    }
}
