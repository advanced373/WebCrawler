/*
 * LogEntry
 *
 * Version 1.0
 *
 * All rights reserved.
 */

package crawler_log;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Class implementing log entity
 * @author CorinaTanase
 */

public class LogEntry extends LogRecord {
    /**
     *
     * @param level
     * @param msg
     */
    public LogEntry(Level level, String msg) {
        super(level, msg);
    }
}
