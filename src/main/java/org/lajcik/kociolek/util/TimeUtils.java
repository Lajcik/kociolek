package org.lajcik.kociolek.util;

import java.util.Date;

/**
 * User: sienkom
 */
public class TimeUtils {

    public static int minuteDiff(Date date1, Date date2) {
        long millisDiff = date2.getTime() - date1.getTime();
        return (int) (millisDiff / (1000 * 60));
    }
}
