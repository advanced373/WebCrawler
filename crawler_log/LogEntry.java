/*
 * LogEntry
 *
 * Version 1.0
 *
 * All rights reserved.
 */

package crawler_log;

import java.time.LocalDateTime;

/**
 * Class implementing log entity
 * @author CorinaTanase
 */

public class LogEntry {
    /** text message of current logged entry */
    private String messageText;
    /** date and time for current logged entry */
    private LocalDateTime timeStamp;
    /** type of current logged entry */
    private String type;

}
