/*
 * CrawlerException
 *
 * Version 1.0
 *
 * All rights reserved.
 */

package action.pack;

import java.io.FileNotFoundException;

/**
 * Class handling user-defined exceptions
 *
 * @author CorinaTanase
 */

public class CrawlerException extends Exception{

    /**
     *  Application universal exception code
     */
    private String code;

    /**
     * CrawlerException constructor
     *
     * @param code the exception's numerical associated value
     * @param message  the exception's text message
     * @param cause to pass the root cause of the exception
     */
    public CrawlerException(String code, String message, Throwable cause) {
        super(message, cause);
        this.setCode(code);
    }

    /**
     * CrawlerException constructor
     *
     * @param code the exception's numerical associated value
     * @param message  the exception's text message
     */
    public CrawlerException(String code, String message) {
        super(message);
        this.setCode(code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Simple print method for showing the exception's code, message and cause
     */
    public void print(){
        System.out.println("Code: "+this.getCode()+" Exception Message : " +this.getMessage()+"\n\t in "+this.getCause());
    }

}
