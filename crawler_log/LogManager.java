/*
 * LogManager
 *
 * Version 1.0
 *
 * All rights reserved.
 */

package crawler_log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.*;

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

    public static Logger getLogger(LoggerType type, String fileName) {
        if (type == LoggerType.FileLogger) {
            return Logger.getLogger("CrawlerFileLog", fileName);
        } else {
            // to do: throw exception for unsupported logger type
            return null;
        }
    }

    public static Logger getLogger(LoggerType type) throws IOException {
        if (type == LoggerType.FileLogger) {
            Logger logger = Logger.getLogger("CrawlerFileLog");
            Handler fileHandler = new FileHandler("logger.log", 2000, 1,true);
            FileLogFormatter fileLogFormatter = new FileLogFormatter();
            fileHandler.setFormatter(fileLogFormatter);
            fileHandler.setLevel(Level.FINE);
            logger.addHandler(fileHandler);
            return logger;
        } else {
            Logger logger = Logger.getLogger("CrawlerConsoleLog");
            //java.util.logging.LogManager.getLogManager().readConfiguration(new FileInputStream("src/mylogging.properties"));
            Handler consoleHandler = new ConsoleHandler();
            ConsoleLogFormatter consoleLogFormatter = new ConsoleLogFormatter();
            consoleHandler.setFormatter(consoleLogFormatter);
            consoleHandler.setLevel(Level.ALL);
            logger.addHandler(consoleHandler);
            logger.setLevel(Level.FINE);
            return logger;
        }
    }
}
