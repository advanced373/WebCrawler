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
     * Construct a LogRecord with the given level and message values.
     * <p>
     * The sequence property will be initialized with a new unique value.
     * These sequence values are allocated in increasing order within a VM.
     * <p>
     * Since JDK 9, the event time is represented by an {@link}.
     * The instant property will be initialized to the {@linkplain
     * Instant#now() current instant}, using the best available
     * {@linkplain Clock#systemUTC() clock} on the system.
     * <p>
     * The thread ID property will be initialized with a unique ID for
     * the current thread.
     * <p>
     * All other properties will be initialized to "null".
     *
     * @param level a logging level value
     * @param msg   the raw non-localized logging message (may be null)
     * @see Clock#systemUTC()
     */
    public LogEntry(Level level, String msg) {
        super(level, msg);
    }
}
