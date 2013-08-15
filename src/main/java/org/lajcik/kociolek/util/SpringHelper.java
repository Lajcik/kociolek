package org.lajcik.kociolek.util;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: sienkom
 * Date: 15.08.13
 * Time: 15:03
 * To change this template use File | Settings | File Templates.
 */
public class SpringHelper {
    private static AbstractApplicationContext context;

    private static void checkInitialized() {
        if(context == null || !context.isActive()) {
            throw new WtfException("Spring not initialized");
        }
    }

    public static <T> T getBean(Class<T> clazz) {
        checkInitialized();
        return context.getBean(clazz);
    }

    public static void init() {
        context = new ClassPathXmlApplicationContext("META-INF/spring/applicationContext.xml");
        context.registerShutdownHook();

    }
}
