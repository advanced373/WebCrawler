/*
 * FileLogFormatter
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
 * Class for formatting records from file logger
 *
 * @author Stoica Mihai
 */
public class FileLogFormatter extends Formatter {
    /**
     * Method that change default format for Logger.
     * @param record entry in the file
     * @return
     */
    @Override
    public String format(LogRecord record) {
        return record.getThreadID()+"::"+record.getSourceClassName()+"::"
                +record.getSourceMethodName()+"::"
                +new Date(record.getMillis())+"::"
                +record.getMessage()+"\n";
    }
}
