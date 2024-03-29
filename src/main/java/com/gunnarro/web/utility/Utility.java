package com.gunnarro.web.utility;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

    public static final String DATE_EEE_PATTERN = "EEEE, dd. MMM yyyy";
    public static final String DATE_PATTERN = "dd.MM.yyyy";
    public static final String DATE_TIME_PATTERN = "dd.MM.yyyy HH:mm";
    public static final String DATE_DEFAULT_PATTERN = DATE_TIME_PATTERN;
    public static final String MYSQL_DATE_PATTERN = "yyyy-MM-dd";
    public static final String REGEXP_DATE = "";
    public static final String REGEXP_TIME_24 = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
    public static final String REGEXP_DATE_TIME = REGEXP_DATE + " " + REGEXP_TIME_24;
    public static final String TIME_PATTERN = "HH:mm";
    public static final String TIME24HOURS_REGEXP_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
    public static final String VALIDATOR_PATTERN_EMAIL = "[\\w-]+@([\\w-]+\\.)+[\\w-]+";
    public static final String VALIDATOR_PATTERN_STRING = "[\\wæøå\\WÆØÅ0-9-\\_ \\]*";
    private static final Logger LOG = LoggerFactory.getLogger(Utility.class);

    public static String capitalizationWord(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String w : s.toLowerCase().split(" ")) {
            sb.append(w.substring(0, 1).toUpperCase()).append(w.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    public static boolean filter(String name, String[] filters) {
        if (filters == null || filters.length == 0) {
            return true;
        }

        if ("all".equalsIgnoreCase(filters[0])) {
            return true;
        }
        for (String filter : filters) {
            if (name.toLowerCase().contains(filter.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static String formatTime(long time, String pattern) {
        String p = pattern;
        if (p == null) {
            p = DATE_DEFAULT_PATTERN;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(p, Locale.UK);
        try {
            return sdf.format(new Date(time));
        } catch (Exception e) {
            LOG.error(null, e);
            return "";
        }
    }

    public static String getWeekInfo(int weekNumber) {
        DateTimeZone timeZone = DateTimeZone.forID("Europe/Paris");
        DateTime weekStartDate = new DateTime(timeZone).withWeekOfWeekyear(weekNumber);
        DateTime weekStart = weekStartDate.withDayOfWeek(DateTimeConstants.MONDAY).withTimeAtStartOfDay();
        DateTime weekEnd = weekStartDate.withDayOfWeek(DateTimeConstants.SUNDAY).withTimeAtStartOfDay();
        return "Week " + weekStartDate.getWeekOfWeekyear() + ", " + Utility.formatTime(weekStart.getMillis(), "EEE d") + " - "
                + Utility.formatTime(weekEnd.getMillis(), "EEE d MMM yyyy");
    }

    public static String getWeekInfo(Date date) {
        DateTimeZone timeZone = DateTimeZone.forID("Europe/Paris");
        int weekNumber = new DateTime(date).getWeekOfWeekyear();
        DateTime weekStartDate = new DateTime(date, timeZone).withWeekOfWeekyear(weekNumber);
        DateTime weekStart = weekStartDate.withDayOfWeek(DateTimeConstants.MONDAY).withTimeAtStartOfDay();
        DateTime weekEnd = weekStartDate.withDayOfWeek(DateTimeConstants.SUNDAY).withTimeAtStartOfDay();
        return "Week " + weekStartDate.getWeekOfWeekyear() + ", " + Utility.formatTime(weekStart.getMillis(), "EEE d") + " - "
                + Utility.formatTime(weekEnd.getMillis(), "EEE d MMM yyyy");
    }

    public static Date resetTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar.getTime();
    }

    public static Date timeToDate(String time, String pattern) {
        try {
            if (ObjectUtils.isEmpty(time)) {
                return null;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_DEFAULT_PATTERN, Locale.UK);
            sdf.setTimeZone(TimeZone.getTimeZone("Europe/Oslo"));
            if (!ObjectUtils.isEmpty(pattern)) {
                sdf = new SimpleDateFormat(pattern, Locale.UK);
            }
            return sdf.parse(time);
        } catch (Exception e) {
            LOG.warn("time: {}, pattern: {}, error: {}", time, pattern, e.getMessage());
            return null;
        }
    }

    public static boolean validateValue(final String value, final String regexpPattern) {
        if (ObjectUtils.isEmpty(value) || ObjectUtils.isEmpty(regexpPattern)) {
            return false;
        }
        Pattern pattern = Pattern.compile(regexpPattern);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public String getAPIVersion() {
        String path = "/version.prop";
        InputStream stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            return "UNKNOWN";
        }
        Properties props = new Properties();
        try {
            props.load(stream);
            stream.close();
            return (String) props.get("version");
        } catch (IOException e) {
            LOG.error(null, e);
            return "UNKNOWN";
        }
    }

    public static String calculateDiffBetweenDates(LocalDate fromDate, LocalDate toDate) {
        Period diff = Period.between(fromDate, toDate);
        if (diff.getYears() > 0 && diff.getMonths() > 0) {
            return String.format("%s år %s måneder", diff.getYears(), diff.getMonths());
        } else if (diff.getYears() > 0 && diff.getMonths() == 0) {
            return diff.getYears() + " år";
        } else if (diff.getYears() == 0 && diff.getMonths() > 0) {
            return diff.getMonths() + " måneder";
        } else {
            return diff.getDays() + " dager";
        }
    }
}
