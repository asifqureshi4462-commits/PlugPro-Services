package com.plugpro.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtil {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    private static final SimpleDateFormat CHAT_TIME_FORMAT = new SimpleDateFormat("hh:mm a", Locale.getDefault());

    public static String formatDate(Date date) {
        if (date == null) return "";
        return DATE_FORMAT.format(date);
    }

    public static String formatTime(Date date) {
        if (date == null) return "";
        return TIME_FORMAT.format(date);
    }

    public static String formatChatTime(Date date) {
        if (date == null) return "";
        return CHAT_TIME_FORMAT.format(date);
    }

    public static String getTodayDateFormatted() {
        return DATE_FORMAT.format(new Date());
    }
}
