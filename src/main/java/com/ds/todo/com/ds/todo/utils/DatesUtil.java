package com.ds.todo.com.ds.todo.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by dsutedja on 6/21/16.
 */
public class DatesUtil {
    public static String toSQLTimestamp(Date date) {
        String str = "";
        if (date == null) {
            date = new Date(System.currentTimeMillis());
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        str = sdf.format(date);

        return str;
    }

    public static String nowToSQLTimestamp() {
        return toSQLTimestamp(Calendar.getInstance().getTime());
    }
}
