package org.lajcik.kociolek.util;

import junit.framework.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * User: sienkom
 */

public class TimeUtilsTest {

    @Test
    public void testUtil() {
        Assert.assertEquals(2, get(1377528599500L, 1377528767748L));
        Assert.assertEquals(2, get(1377528618617L, 1377528764624L));
        Assert.assertEquals(2, get(1377528647052L, 1377528775486L));
    }

    private int get(long l1, long l2) {
        Date d1 = new Date();
        d1.setTime(l1);
        Date d2 = new Date();
        d2.setTime(l2);
        return TimeUtils.minuteDiff(d1, d2);
    }
}
