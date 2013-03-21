package com.danveloper.log4j.jms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: ddcdwoods
 * Date: 3/19/13
 */
public class TestJMSLogger {
    private static final Log log = LogFactory.getLog(TestJMSLogger.class);

    public static void main(String[] args) {
        new Thread() {
            public void run() {
                while(true) {
                    log.error("error message");
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        log.error("There was an error sleeping.", e);
                    }
                }
            }
        }.run();
    }
}
