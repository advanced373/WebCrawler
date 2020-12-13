package action.pack;

import file_handlers.CheckFileType;
import file_handlers.FileWorker;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;

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

    /** CrawlTask constructor
     * @param urlToCrawl The URL where the page is downloaded
     * @param webCrawler Represents an instance of the main class of the download process
     * @param delay After each downloaded page it will wait a period depending on the value of this parameter
     * @param rootDir the directory where the download pages are stored
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

    public abstract void run();

    /**
     * This method downloads the page from the URL
     * @throws IOException when there is a connection problem
     */

    protected void crawl() throws IOException {

        try{
            this.urlToCrawl=URLNormalization.URLProcessing( this.urlToCrawl ,"");
            URL url=new URL(this.urlToCrawl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            if(connection.getResponseCode()<200 || 226 < connection.getResponseCode()) {
                System.out.println(connection.getResponseMessage());
                return;
            }

            InputStream inputStream=connection.getInputStream();
            String path=this.getPath( url );
            this.writePage(path,inputStream);
            inputStream.close();
            connection.disconnect();


            //System.out.println(this.urlToCrawl);
            FileWorker fileWorker = new FileWorker();
            ArrayList<String> URLs = fileWorker.readFromHTMLFile(this.urlToCrawl,path);
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
            }else {
                this.webCrawler.addCountDownloadedPage();
            }

            Thread.sleep(this.delay);


        }catch (IOException  exception){
            exception.printStackTrace();
        }catch (InterruptedException exception){
            exception.printStackTrace();
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

        String auxPath=path.substring( 0,path.lastIndexOf( "/" ) );
        File file=null;
        if(auxPath!=null){
            File auxFile=new File( auxPath );
            if(auxFile.exists() && !auxFile.isDirectory()){
                //plus salvare fisier vechi
                //auxPath=auxPath+"/index.html";
               // InputStream inputStream1=new FileInputStream( auxFile );
               // writePage( auxPath,inputStream1 );
                file = new File(path);
            }else {
                file = new File(path);
            }
        }else{
            file = new File(path);
        }
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

    /**
     * Add new URL to processing queue
     * @param URLs URLs extracted from the downloaded page
     */

    private void addUrlLinkedQueue(ArrayList<String> URLs)  {
        if (URLs==null)
            return;
        for (String url:URLs)
            this.webCrawler.linksQueue.add( url );
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
