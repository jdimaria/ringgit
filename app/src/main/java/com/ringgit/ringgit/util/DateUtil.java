package com.ringgit.ringgit.util;

import android.text.format.DateUtils;

import java.util.Calendar;

/**
 * Created by Joe DiMaria on 3/9/2017.
 * Helper for manipulating date and time data
 */

public class DateUtil {
    public static final long S_TO_MS_MULTIPLE = 1000;

    public static CharSequence getRelativeTimeSinceLong(long time) {
        return DateUtils.getRelativeTimeSpanString(
                time,
                Calendar.getInstance().getTimeInMillis(),
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_RELATIVE
        );
    }
}
