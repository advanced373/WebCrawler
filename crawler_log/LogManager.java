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
 * @author CorinaTanase and Stoica Mihai
 */

public class LogManager {

    private static LogManager singleInstance;
    private static Logger consoleLogger;
    private static Logger fileLogger;
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

    /**
     * Method for getting the right logger (console or file)
     * @param type must be one of LoggerType values
     * @return
     * @throws IOException
     */
    public static Logger getLogger(LoggerType type) throws IOException {
        if (type == LoggerType.FileLogger && fileLogger == null) {
            fileLogger = Logger.getLogger("CrawlerFileLog");
            fileLogger.setUseParentHandlers(false);
            Handler fileHandler = new FileHandler("logger.log", 2000, 1,true);
            FileLogFormatter fileLogFormatter = new FileLogFormatter();
            fileHandler.setFormatter(fileLogFormatter);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setLevel(Level.ALL);
            fileLogger.addHandler(fileHandler);
            return fileLogger;
        }
        if ( type == LoggerType.ConsoleLogger && consoleLogger == null)
        {
            consoleLogger = Logger.getLogger("CrawlerConsoleLog");
            //java.util.logging.LogManager.getLogManager().readConfiguration(new FileInputStream("src/mylogging.properties"));
            Handler consoleHandler = new ConsoleHandler();
            ConsoleLogFormatter consoleLogFormatter = new ConsoleLogFormatter();
            consoleHandler.setFormatter(consoleLogFormatter);
            consoleHandler.setLevel(Level.ALL);
            consoleLogger.setLevel(Level.ALL);
            consoleLogger.addHandler(consoleHandler);
            return consoleLogger;
        }
        if(type == LoggerType.FileLogger && fileLogger != null)
        {
            return fileLogger;
        }
        if(type == LoggerType.ConsoleLogger && consoleLogger != null)
        {
            return consoleLogger;
        }
        return null;
    }
}