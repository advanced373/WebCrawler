package action.pack;


import file_handlers.FileWorker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class implements the download process starting from a file with URLs
 * @author Rosca Stefan
 */

public class Crawl extends ExternAction{

    /** seed URLs */
    private List<String> urlsToCrawl =new ArrayList<>();
    /** manages the thread pool*/
    private ThreadPoolExecutor threadPoolExecutor;
    /** the number of threads in the pool*/
    private Integer numThreads;
    /** root directory where save downloaded pages */
    private String rootDir;
    /** After each downloaded page it will wait a period depending on the value of this parameter */
    private Integer delay;
    /** It's a level */
    private Integer logLevel;
    /** the maximum number of pages that can be downloaded*/
    private Integer depth;
    /** used when checking the contents of the file robots.txt (if the value is 1 it must be checked otherwise not) */
    private Integer flagRobots=0;
    /** used if certain types of files can be downloaded (if the value is 1 it must be checked otherwise not) */
    private Integer flagExtension=0;
    /** used to check if the page from a url has already been downloaded*/
    private final Set<String> visitedLinks=new HashSet<>();
    /** */
    private Integer countPagesDownload;



    /** processing queue */
    public LinkedBlockingQueue<String> linksQueue=new LinkedBlockingQueue<>();
    /** used for synchronization when linksQueue is empty and a task is running*/
    public CyclicBarrier cyclicBarrier;
    /** the name of the configuration file */
    public String fileNameConf;
    /** the name of the file with the seed urls */
    public String fileNameUrlList;
    /** used to download specific resources depending on the extension*/
    public final ArrayList<String> extension=new ArrayList<>();


    /** CrawlTask constructor
     * @param filePath
     * @param fileNameConf the name of the configuration file
     * @param fileNameUrlList the name of the file with the seed urls
     * @param parameters list of extensions and  flag value flagRobots
     * @throws FileNotFoundException  when there is a problem opening a file
     */

    public Crawl(String filePath,String fileNameConf, String fileNameUrlList,ArrayList<String> parameters) throws FileNotFoundException {
        super(filePath);
        this.fileNameConf = fileNameConf;
        this.fileNameUrlList = fileNameUrlList;
        this.cyclicBarrier=new CyclicBarrier(2);
        this.threadPoolExecutor=null;
        this.countPagesDownload=0;

        FileWorker fileWorker= new FileWorker();
        ArrayList<String> confParam;
        try {
            this.urlsToCrawl=fileWorker.ReadFromURLsFile(this.fileNameUrlList);
            confParam=fileWorker.readFromConfigureFile(this.fileNameConf);
            this.parseParam(confParam);
        }catch (NumberFormatException  | IOException exception) {
            exception.printStackTrace();
        }catch (CrawlerException crawlException){
            crawlException.print();
        }

        if(parameters.get(0).equals("yes"))
            setFlagRobots(1);

        setExtension(parameters);
        initProcessQueue();

    }


    /**
     * This method call execute function
     */
    @Override
    public boolean runAction() {
        return this.execute();
    }

    /**
     * This method initializes a pool of threads and allows
     * each thread to download one page at a time.
     */

    public boolean  execute() {

        this.threadPoolExecutor =(ThreadPoolExecutor) Executors.newFixedThreadPool( this.numThreads );

        while (!this.linksQueue.isEmpty() && this.countPagesDownload<this.depth){
            try {

                String currentURL=linksQueue.take();

                if(!this.checkURL(currentURL,this.flagExtension)) {
                   if(this.linksQueue.isEmpty()  && this.threadPoolExecutor.getActiveCount()>0){
                       this.cyclicBarrier.await();
                   }
                   continue;
                }
                this.visitedLinks.add(currentURL);


                CrawlTask crawlTask=TaskFactory.createTask( currentURL,this,this.delay,this.rootDir,this.flagRobots );
                this.threadPoolExecutor.submit(crawlTask);

                if(this.linksQueue.isEmpty() && this.threadPoolExecutor.getActiveCount()>0){
                    this.cyclicBarrier.await();
                }
                System.out.println(this.countPagesDownload);

            }catch (InterruptedException  exception){
                exception.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }

        try {
            this.cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println("Barrier: "+this.cyclicBarrier.getNumberWaiting());
        System.out.println("Count final "+this.countPagesDownload);
        System.out.println("Task count "+this.threadPoolExecutor.getTaskCount());
        this.threadPoolExecutor.shutdown();

        try {
            return this.threadPoolExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        }catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
        return false;
    }


    public void setNumThreads(Integer numThreads) {
        this.numThreads = numThreads;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public void setLogLevel(Integer logLevel) {
        this.logLevel = logLevel;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public void setExtension(ArrayList<String> extension) {
        if(extension.size()==1){
            this.flagExtension=0;
            return;
        }
        for(int i=1;i<extension.size();i++)
            this.extension.add((extension.get(i)));
        this.flagExtension=1;
    }

    public void setFlagRobots(Integer flagRobots) {
        this.flagRobots = flagRobots;
    }

    /**
     * This method parses the configuration parameters and initializes them
     * @throws CrawlerException when size of param differ from 5
     */

    private void parseParam(ArrayList<String> param) throws CrawlerException {

        if(param.size()!=5){
            throw new CrawlerException("100","The number of configuration parameters is different from 5.");
        }

        setNumThreads(Integer.parseInt(param.get(0).substring(10)));
        setDelay(Integer.parseInt(param.get(1).substring(6)));
        setRootDir(param.get(2).substring(9));
        setLogLevel(Integer.parseInt(param.get(3).substring(10)));
        setDepth(Integer.parseInt(param.get(4).substring(6)));

    }

    /**
     * This method initializes the processing queue
     */

    private void initProcessQueue(){
        for (String s: this.urlsToCrawl){
            if(this.isValidUrl(s))
                linksQueue.add(s);
        }
    }

    /**
     * This method check if url already been visited or extension is ok if is case
     * @param url value of url
     * @return true if the url has not been visited or the extension is allowed else false
     */

    private boolean checkURL(String url,Integer flagExtension){

        if(this.visitedLinks.contains(url))
            return false;
        return true;
    }

    public List<String> getUrlsToCrawl() {
        return urlsToCrawl;
    }

    public Integer getFlagExtension() {
        return flagExtension;
    }

    /**
     * Check that the URL format is valid
     * @param url contains value of url
     * @return true if url is valid else false
     */

    private boolean isValidUrl(String url){


        //OWASP url regex
        String regex =  "^((((https?|ftps?|gopher|telnet|nntp)://)|(mailto:|news:))" +
                "(%[0-9A-Fa-f]{2}|[-()_.!~*';/?:@&=+$,A-Za-z0-9])+)" +
                "([).!';/?:,][[:blank:]])?$";

        Pattern p = Pattern.compile(regex);
        if (url == null) {
            return false;
        }

        Matcher m = p.matcher(url);

        return m.matches();
    }

    public Integer getActiveCount() {
        return this.threadPoolExecutor.getActiveCount();
    }

    public void addCountDownloadedPage(){
        synchronized (this) {
            this.countPagesDownload++;
        }
    }

}
