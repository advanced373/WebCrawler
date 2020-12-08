package action.pack;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;


import file_handlers.*;

/**
 * This class implements the download process starting from a file with URLs
 * @author Rosca Stefan
 */

public class Crawl extends ExternAction{

    /** seed URLs */
    private List<String> urlsToCrawl =new ArrayList<>();
    /** manages the thread pool*/
    private ExecutorService executorService;
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
    /** used when checking the contents of the file robots.txt */
    private Integer flagRobots;
    /** used to download specific resources depending on the extension*/
    private ArrayList<String> extension=new ArrayList<>();
    /** used to check if the page from a url has already been downloaded*/
    private Set<String> visitedLinks=new HashSet();


    /** processing queue */
    public LinkedBlockingQueue<String> linksQueue=new LinkedBlockingQueue<>();
    /** used for synchronization when linksQueue is empty and a task is running*/
    public CyclicBarrier cyclicBarrier;
    /** the name of the configuration file */
    public String fileNameConf;
    /** the name of the file with the seed urls */
    public String fileNameUrlList;


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

        FileWorker fileWorker= new FileWorker();
        ArrayList<String> confParam=new ArrayList<>(5);
        try {
            this.urlsToCrawl=fileWorker.ReadFromURLsFile(this.fileNameUrlList);
            confParam=fileWorker.readFromConfigureFile(this.fileNameConf);
            this.parseParam(confParam,this.numThreads,this.delay,this.rootDir,this.logLevel,this.depth);
        }catch (FileNotFoundException | MalformedURLException exception) {
            exception.printStackTrace();
        }catch (NumberFormatException  numberFormatException){
            numberFormatException.printStackTrace();
        }catch (CrawlerException crawlException){
            crawlException.print();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(parameters.get(0).equals("yes"))
            setFlagRobots(1);
        else
            setFlagRobots(0);

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

        this.executorService = Executors.newFixedThreadPool(this.numThreads);

        int countPagesDownload=0;

        while (!this.linksQueue.isEmpty() && countPagesDownload<this.depth){
            try {
                String currentURL=linksQueue.take();

                if(!this.checkURL(currentURL)) {
                    System.out.println("Nu merge");
                    continue;
                }
                this.visitedLinks.add(currentURL);
                System.out.println(currentURL);

                CrawlTask crawlTask=new CrawlTaskNormal(currentURL,this,this.delay,this.rootDir);
                this.executorService.submit(crawlTask);

                synchronized (this){
                    countPagesDownload++;
                }

                if(this.linksQueue.isEmpty()){
                    cyclicBarrier.await();
                }

            }catch (InterruptedException | BrokenBarrierException exception){
                exception.printStackTrace();
            }
        }

        this.executorService.shutdown();

        try {
            this.executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            return true;
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
        for(int i=1;i<extension.size();i++)
            this.extension.add((extension.get(i)));
    }

    public void setFlagRobots(Integer flagRobots) {
        this.flagRobots = flagRobots;
    }

    /**
     * This method parses the configuration parameters and initializes them
     * @param param
     * @param numThreads
     * @param delay
     * @param rootDir
     * @param logLevel
     * @param depth
     * @throws CrawlerException when size of param differ from 5
     */

    private void parseParam( ArrayList<String> param,Integer numThreads,Integer delay,String rootDir,Integer logLevel,Integer depth) throws CrawlerException {

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
     * This method check if url already been visited or extension is ok
     * @param url
     * @return true if the url has not been visited or the extension is allowed else false
     */

    private boolean checkURL(String url){

        if(this.visitedLinks.contains(url))
            return false;
        if(Util.checkUrlExtension(this.extension,url)) {
            System.out.println("Extdsfhdsjk");
            return true;
        }
        return true;
    }

    /**
     * Check that the URL format is valid
     * @param url contains value of url
     * @return true if url is valid else false
     */

    private boolean isValidUrl(String url){

        String regex = "((http|https)://)(www.)?"
                    + "[a-zA-Z0-9@:%._\\+~#?&//=]"
                    + "{2,256}\\.[a-z]"
                    + "{2,6}\\b([-a-zA-Z0-9@:%"
                    + "._\\+~#?&//=]*)";

        Pattern p = Pattern.compile(regex);
        if (url == null) {
            return false;
        }

        Matcher m = p.matcher(url);

        return m.matches();
    }

}
