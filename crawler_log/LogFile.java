/*
 * LogFile
 *
 * Version 1.0
 *
 * All rights reserved.
 */

package crawler_log;

public abstract class LogFile {

    private LoggerType type;

    /**
     * Function for initializing the logger entity
     * @param type type of logger ( ConsoleLogFile and FileLogFile extend abstract LogFile class)
     * @param fileName name of file
     */
    public void initialize(LoggerType type, String fileName) {
        this.type = type;
    }
    /**
     * Abstract method for log actions
     * @param entry the event that will be logged
     * @param level position on a scale of quality/type of logged event
     */
    public abstract void log(LogEntry entry, LogLevel level);

}
