/*
 * LogManager
 *
 * Version 1.0
 *
 * All rights reserved.
 */

package crawler_log;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Class for logging crawler's action in a log file
 *
 * @author CorinaTanase
 */
public class FileLogFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {
        return record.getThreadID()+"::"+record.getSourceClassName()+"::"
                +record.getSourceMethodName()+"::"
                +new Date(record.getMillis())+"::"
                +record.getMessage()+"\n";
    }
}
