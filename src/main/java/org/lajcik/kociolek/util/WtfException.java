package org.lajcik.kociolek.util;

/**
 * @author lajcik
 */
public class WtfException extends RuntimeException {

    public WtfException(String message) {
        super(message);
    }

    public WtfException(String message, Throwable ex) {
        super(message, ex);
    }
}
