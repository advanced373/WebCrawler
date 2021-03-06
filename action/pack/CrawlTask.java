package action.pack;

import crawler_log.LogManager;
import crawler_log.LoggerType;
import file_handlers.CheckFileType;
import file_handlers.FileWorker;


import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.logging.Level;


import javax.imageio.ImageIO;

/**
 * Implement the class that downloads the page from a URL
 * @author Rosca Stefan
 */


public abstract class CrawlTask implements Runnable{

    /**
     * Member description
    */

    protected String urlToCrawl;
    protected Crawl webCrawler;
    private Integer delay;
    private String rootDir;
    protected File indexFile;

    /** CrawlTask constructor
     * @param urlToCrawl The URL where the page is downloaded
     * @param webCrawler Represents an instance of the main class of the download process
     * @param delay After each downloaded page it will wait a period depending on the value of this parameter
     * @param rootDir the directory where the download pages are stored
     */

    public CrawlTask(String urlToCrawl, Crawl webCrawler, Integer delay, String rootDir, File indexFile) {
        this.urlToCrawl = urlToCrawl;
        this.webCrawler = webCrawler;
        this.delay = delay;
        this.rootDir=rootDir;
        this.indexFile = indexFile;
    }

    /**
     * This method call crawl function
     */

    public abstract void run() throws RuntimeException;

    /**
     * This method downloads the page from the URL
     * @throws IOException when there is a connection problem
     * @throws BrokenBarrierException if the barrier action failed due to an exception
     * @throws InterruptedException if the current thread was interrupted while waiting
     */

    protected void crawl() throws IOException, BrokenBarrierException, InterruptedException {

            this.urlToCrawl=URLNormalization.URLProcessing( this.urlToCrawl ,"");
            URL url=new URL(this.urlToCrawl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            if(connection.getResponseCode()<200 || 226 < connection.getResponseCode()) {
                //this.webCrawler.decrementCountDownloadedPage();
                LogManager.getMyLogger(LoggerType.FileLogger).log(Level.WARNING,"Code : "+connection.getResponseCode()+" "+ connection.getResponseMessage());
                return;
            }
            InputStream inputStream=connection.getInputStream();
            String path=this.getPath(url);
            String strURL = this.getURL(url);
            this.writePage(strURL, path,inputStream);
            LogManager.getMyLogger(LoggerType.FileLogger).log(Level.INFO,"Code : "+connection.getResponseCode()+" "+ connection.getResponseMessage()+" "+this.urlToCrawl);
            inputStream.close();
            connection.disconnect();
            ArrayList<String> URLs;
            synchronized (this.webCrawler.fileWorker)
            {
                FileWorker fileWorker = this.webCrawler.fileWorker;
                URLs= fileWorker.readFromHTMLFile(this.urlToCrawl,path);
            }
            this.addUrlLinkedQueue( URLs );
            CheckFileType checkFileType = new CheckFileType();
            if(this.webCrawler.cyclicBarrier.getNumberWaiting()==1 &&

                  (checkFileType.getType(path) !=null || this.webCrawler.linksQueue.isEmpty())) {

                this.webCrawler.cyclicBarrier.await();
            }

            if(this.webCrawler.getFlagExtension()==1 && !Util.checkUrlExtension(this.webCrawler.extension, this.urlToCrawl )){
                File file=new File( path );
                if (!file.delete()) {
                }
            }
            else if(this.webCrawler.getFlagExtension()==1 && Util.checkUrlExtension(this.webCrawler.extension, this.urlToCrawl ))
            {
                this.webCrawler.addCountDownloadedPage();
            }
            Thread.sleep(this.delay);

    }

    /**
     * This method is responsible with write data in file and
     * to registry the new downloaded data to index.json file
     *
     * @param strURL is the site url
     * @param path the absolute path where the downloaded page will be stored
     * @param inputStream page data
     * @throws IOException
     */

    private void writePage(String strURL, String path,InputStream inputStream) throws IOException {

        String auxPath=path.substring( 0,path.lastIndexOf( "/" ) );
        File file=null;
        if(auxPath!=null){
            File auxFile=new File( auxPath );
            if(auxFile.exists() && !auxFile.isDirectory()){
                auxFile.delete();
            }
        }
        file = new File(path);

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

            FileWorker fileWorkerObj = new FileWorker();

            ArrayList<String> dataArray = createDataArrayForIndexFile("", path);
            if(!dataArray.isEmpty())
            {
                fileWorkerObj.writeToIndexFile(indexFile, rootDir, dataArray, 0);
            }

            dataArray = createDataArrayForIndexFile(strURL, path);
            if(!dataArray.isEmpty())
            {
                fileWorkerObj.writeToIndexFile(indexFile, rootDir, dataArray, 1);
            }

        }
    }

    private String getURL(URL url){
        if(url.getPath().isEmpty()){
            return url.toString() + "/" + url.getHost();
        }else {
            return url.toString().replaceAll("(?<!(http:|https:))//", "/");
        }
    }

    private ArrayList createDataArrayForIndexFile(String strUrl, String filePath) throws IOException {

        ArrayList dataArray = new ArrayList<>();
        String type = new String();

        Image image = ImageIO.read(new File(filePath));
        if (image != null) {
            type = "images";
        }

        if(strUrl.isEmpty())
        {

            String dirPath = new String();
            filePath = filePath.replace("//", "/");
            String[] path = filePath.split("/");

            for(int i = 0; i<path.length - 1; i++)
            {
                dirPath += path[i];
                if(i != path.length - 2)
                {
                    dirPath +="\\";
                }
            }

            String fileName = path[path.length - 1];

            dataArray.add(dirPath);
            dataArray.add("keywords");
            dataArray.add("0");
            if(type.equals("images"))
            {
                dataArray.add("images");
                dataArray.add("1");
                dataArray.add(fileName);
                dataArray.add("documents");
                dataArray.add("0");
            }
            else
            {
                dataArray.add("images");
                dataArray.add("0");
                dataArray.add("documents");
                dataArray.add("1");
                dataArray.add(fileName);
            }
            dataArray.add("sitemap");
            dataArray.add("yes");

        }
        else {
            if(!type.equals("image"))
            {

                dataArray.add(strUrl);
                dataArray.add("keywords");
                dataArray.add("0");
                dataArray.add("images");
                dataArray.add("0");
                dataArray.add("documents");
                dataArray.add("0");
                dataArray.add("sitemap");
                dataArray.add("yes");
            }
        }

        return dataArray;
    }

    /**
     * Add new URL to processing queue
     * @param URLs URLs extracted from the downloaded page
     */

    private void addUrlLinkedQueue(ArrayList<String> URLs) throws IOException {
        if (URLs==null)
            return;
        for (String url:URLs)
        {
            this.webCrawler.linksQueue.add( url );
            synchronized(LogManager.mutex)
            {
                LogManager.getMyLogger(LoggerType.FileLogger).log(Level.INFO,url);
            }

        }

    }

    /**
     * based on a url and the root directory returns an absolute path
     * @param url url of the page that was downloaded and based on which the absolute path is created
     * @return absolute path
     */
    private String getPath(URL url){

        if(url.getPath().isEmpty()){
            return Util.trimUrl( rootDir + '/' + url.getHost() + '/'+url.getHost());
        }else {

            return Util.trimUrl( rootDir + '/' + url.getHost() +'/' +url.getPath());
        }
    }

}
