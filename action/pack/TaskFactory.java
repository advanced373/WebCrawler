/*
 * TaskFactory
 *
 * Version 1.0
 *
 * All rights reserved.
 */

package action.pack;

import java.io.File;
import java.util.concurrent.Semaphore;

/**
 * This class is responsible for creating the task depending on whether or not the robots.txt file needs to be checked
 * @author Rosca Stefan
 */

public class TaskFactory {

    /**
     * if the Robots flag flag is 1 a CrawlTaskRobots object is created otherwise a CrawlTaskNormal object is created
     * @param urlToCrawl The URL where the page is downloaded
     * @param webCrawler Represents an instance of the main class of the download process
     * @param delay After each downloaded page it will wait a period depending on the value of this parameter
     * @param rootDir the directory where the download pages are stored
     * @param flagRobots  used to know what kind of object to create
     * @return a CrawlTask object
     */
    public static CrawlTask createTask(String urlToCrawl, Crawl webCrawler, Integer delay, String rootDir, Integer flagRobots, File indexFile){
        if(flagRobots==1)
            return new CrawlTaskRobots( urlToCrawl,webCrawler,delay,rootDir, indexFile);
        else
            return new CrawlTaskNormal( urlToCrawl,webCrawler,delay,rootDir, indexFile);
    }
}
