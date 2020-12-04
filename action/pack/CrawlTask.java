package action.pack;

public class CrawlTask implements Runnable{

    private String urlToCrawl;
    private Crawl webCrawler;

    public CrawlTask(String urlToCrawl, Crawl webCrawler) {
        this.urlToCrawl = urlToCrawl;
        this.webCrawler = webCrawler;
    }

    @Override
    public void run() {
        try{
            crawl();
        }catch (Exception exception){

        }
    }

    private void crawl(){

    }
}
