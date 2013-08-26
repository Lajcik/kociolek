package org.lajcik.kociolek.util;

import java.util.Date;

/**
 * User: sienkom
 */
public class TimeUtils {

    public static int minuteDiff(Date date1, Date date2) {
        long millisDiff = date1.getTime() - date2.getTime();
        return (int) (millisDiff / (1000 * 60));
    }
}
