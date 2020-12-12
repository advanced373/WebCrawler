package action.pack;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Semaphore;

public class CrawlTaskRobots extends CrawlTask{
    /**
     * CrawlTask constructor
     *
     * @param urlToCrawl The URL where the page is downloaded
     * @param webCrawler Represents an instance of the main class of the download process
     * @param delay      After each downloaded page it will wait a period depending on the value of this parameter
     * @param rootDir    the directory where the download pages are stored
     */
    public CrawlTaskRobots(String urlToCrawl, Crawl webCrawler, Integer delay, String rootDir, File indexFile) {
        super(urlToCrawl, webCrawler, delay, rootDir, indexFile);
    }

    @Override
    public void run() {
        try {
            URL url=new URL(this.urlToCrawl);
            if(Robots.robotParser(url))
                this.crawl();
            else {
                System.out.println(this.urlToCrawl);
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
