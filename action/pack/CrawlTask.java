package action.pack;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class CrawlTask implements Runnable{

    private String urlToCrawl;
    private Crawl webCrawler;
    private Integer delay;



    public CrawlTask(String urlToCrawl, Crawl webCrawler, Integer delay) {
        this.urlToCrawl = urlToCrawl;
        this.webCrawler = webCrawler;
        this.delay = delay;
    }

    @Override
    public void run() {
        try{
            crawl();
        }catch (Exception exception){

        }
    }

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
