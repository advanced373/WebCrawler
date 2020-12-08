/*
 * LogManager
 *
 * Version 1.0
 *
 * All rights reserved.
 */

package crawler_log;

import java.util.logging.Logger;

/**
 * Singleton class LogManager used for logging web crawler's actions
 *
 * @author CorinaTanase
 */

public class LogManager {

    private static LogManager singleInstance;

    private LogManager() {
    }

    /**
     * Method for returning the single instance of Singleton class LogManager
     * providing double-checked locking implementation for thread-safety
     * but avoiding extra overhead everytime for performance reasons
     *
     * @return the single instance of LogManager
     */
    public static LogManager getInstance() {
        if (singleInstance == null) {
            synchronized (LogManager.class) {
                if (singleInstance == null) {
                    singleInstance = new LogManager();
                }
            }
        }
        return singleInstance;
    }

    public Logger GetLogger(LoggerType type, String fileName) {
        if (type == LoggerType.FileLogger) {
            return Logger.getLogger("CrawlerFileLog", fileName);
        } else {
            // to do: throw exception for unsupported logger type
            return null;
        }
    }

    public Logger GetLogger(LoggerType type) {
        if (type == LoggerType.ConsoleLogger) {
            return Logger.getLogger("CrawlerConsoleLog");
        } else {
            // to do: throw exception for unsupported logger type
            return null;
        }
    }
}
