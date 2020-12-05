package action.pack;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


import file_handlers.*;

/**
 * This class implements the download process starting from a file with URLs
 * @author Rosca Stefan
 */

public class Crawl extends ExternAction{

    /** seed URLs */
    private List<String> urlsToCrawl =new ArrayList<>();
    /** processing queue */
    private LinkedBlockingQueue<String> linksQueue=new LinkedBlockingQueue<>();
    private ExecutorService executorService;
    /** the number of threads in the pool*/
    private Integer numThreads;
    /** root directory where save downloaded pages */
    private String rootDir;
    /** After each downloaded page it will wait a period depending on the value of this parameter */
    private Integer delay;
    /** */
    private Integer logLevel;
    /** the maximum number of pages that can be downloaded*/
    private Integer depth;

    private CyclicBarrier cyclicBarrier;


    /** the name of the configuration file */
    public String fileNameConf;
    /** the name of the file with the seed urls */
    public String fileNameUrlList;


    /** CrawlTask constructor
     * @param fileNameConf the name of the configuration file
     * @param fileNameUrlList the name of the file with the seed urls
     * @throws FileNotFoundException  when there is a problem opening a file
     */

    public Crawl(String fileNameConf, String fileNameUrlList) throws FileNotFoundException {
        this.fileNameConf = fileNameConf;
        this.fileNameUrlList = fileNameUrlList;
        this.cyclicBarrier=new CyclicBarrier(2);

        FileWorker fileWorker= new FileWorker();

        ArrayList<String> confParam=new ArrayList<>(5);

        try {
            this.urlsToCrawl=fileWorker.ReadFromURLsFile(this.fileNameUrlList);
            confParam=fileWorker.readFromConfigureFile(this.fileNameConf);
            this.parseParam(confParam,this.numThreads,this.delay,this.rootDir,this.logLevel,this.depth);
        }catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }catch (NumberFormatException numberFormatException){
            numberFormatException.printStackTrace();
        }

    }


    /**
     * This method call execute function
     */
    @Override
    public boolean runAction() {
        this.execute();
        return false;
    }

    /**
     * This method initializes a pool of threads and allows
     * each thread to download one page at a time.
     */

    public void execute() {

        this.executorService = Executors.newFixedThreadPool(this.numThreads);
        initProcessQueue();

        int countPagesDownload=0;

        while (!this.linksQueue.isEmpty()||countPagesDownload<this.depth){
            try {
                String currentURL=linksQueue.take();

                CrawlTask crawlTask=new CrawlTask(currentURL,this,this.delay);
                this.executorService.submit(crawlTask);

                synchronized (this){
                    countPagesDownload++;
                }

                if(this.linksQueue.isEmpty()){
                    this.cyclicBarrier.await();
                }

            }catch (InterruptedException interruptedException){
                interruptedException.printStackTrace();
            }catch (BrokenBarrierException brokenBarrierException){
                brokenBarrierException.printStackTrace();
            }

            this.executorService.shutdown();

            try {
                this.executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            }catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
    }

    /**
     * This method parses the configuration parameters and initializes them
     * @param param
     * @param numThreads
     * @param delay
     * @param rootDir
     * @param logLevel
     * @param depth
     */

    private void parseParam( ArrayList<String> param,int numThreads,int delay,String rootDir,int logLevel,int depth){

        if(param.size()!=5){
            //arunca exceptie
            return;
        }

        numThreads=Integer.parseInt(param.get(0));
        delay=Integer.parseInt(param.get(1));
        rootDir=param.get(2);
        logLevel=Integer.parseInt(param.get(3));
        depth=Integer.parseInt(param.get(4));

    }

    /**
     * This method initializes the processing queue
     */

    private void initProcessQueue(){
        for (String s: this.urlsToCrawl){
            linksQueue.add(s);
        }
    }
}
