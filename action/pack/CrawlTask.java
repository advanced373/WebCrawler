package action.pack;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Implement the class that downloads the page from a URL
 * @author Rosca Stefan
 */


public class CrawlTask implements Runnable{

    /**
     * Member decription
    */

    private String urlToCrawl;
    private Crawl webCrawler;
    private Integer delay;

    /** CrawlTask constructor
     * @param urlToCrawl The URL where the page is downloaded
     * @param webCrawler Represents an instance of the main class of the download process
     * @param delay After each downloaded page it will wait a period depending on the value of this parameter
     */

    public CrawlTask(String urlToCrawl, Crawl webCrawler, Integer delay) {
        this.urlToCrawl = urlToCrawl;
        this.webCrawler = webCrawler;
        this.delay = delay;
    }

    /**
     * This method call crawl function
     */

    @Override
    public void run() {
        try{
            crawl();
        }catch (Exception exception){

        }
    }

    /**
     * This method downloads the page from the URL
     * @throws IOException when there is a connection problem
     */

    private void crawl() throws IOException {
        URL url=new URL(this.urlToCrawl);

        try{
            URLConnection urlConnection=null;
            urlConnection=url.openConnection();

            InputStream inputStream=urlConnection.getInputStream();

            //extract URL din pagina
            //add in queue

            Thread.sleep(this.delay);

        }catch (IOException  exception){
            throw new RuntimeException("Error connecting to URL",exception);
        }catch (InterruptedException exception){
            throw new RuntimeException("Error runtime",exception);
        }
    }
}
