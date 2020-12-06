package action.pack;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.BrokenBarrierException;

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
    private String rootDir;

    /** CrawlTask constructor
     * @param urlToCrawl The URL where the page is downloaded
     * @param webCrawler Represents an instance of the main class of the download process
     * @param delay After each downloaded page it will wait a period depending on the value of this parameter
     */

    public CrawlTask(String urlToCrawl, Crawl webCrawler, Integer delay,String rootDir) {
        this.urlToCrawl = urlToCrawl;
        this.webCrawler = webCrawler;
        this.delay = delay;
        this.rootDir=rootDir;
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
            InputStream inputStream=url.openStream();

            String path;
            if(url.getPath().isEmpty()){
                path = rootDir + '/' + url.getHost() + '/'+url.getHost();
            }else {
                path = rootDir + '/' + url.getHost() +'/' +url.getPath();
            }

            this.writePage(path,inputStream);

            //adaugare exragere link

           inputStream.close();

            if(this.webCrawler.cyclicBarrier.getNumberWaiting()==1)
                this.webCrawler.cyclicBarrier.await();

           Thread.sleep(this.delay);

        }catch (IOException  exception){
            throw new RuntimeException("Error connecting to URL",exception);
        }catch (InterruptedException exception){
            throw new RuntimeException("Error runtime",exception);
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is responsible with write data in file
     * @param path the absolute path where the downloaded page will be stored
     * @param inputStream page data
     * @throws IOException
     */

    private void writePage(String path,InputStream inputStream) throws IOException {
        File file = new File(path);
        if(!file.exists()){

            file.getParentFile().mkdirs();
            file.createNewFile();


            OutputStream outputStream=new FileOutputStream(file);
            int read;
            byte[] bytes=new byte[1024];
            while ((read=inputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,read);
            }
            outputStream.close();

        }
    }
}
