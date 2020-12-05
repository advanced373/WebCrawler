package action.pack;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import  file_handlers.FileWork;
import file_handlers.FileWorker;
import file_handlers.NormalFileWork;

public class Crawl extends ExternAction{


    private List<String> urlsToCrawl =new ArrayList<>();
    private LinkedBlockingQueue<String> linksQueue=new LinkedBlockingQueue<>();
    private ExecutorService executorService;
    private Integer numThreads;
    private String rootDir;
    private Integer delay;
    private Integer logLevel;
    private Integer depth;

    public String fileNameConf;
    public String fileNameUrlList;


    public Crawl(String fileNameConf, String fileNameUrlList) throws FileNotFoundException {
        this.fileNameConf = fileNameConf;
        this.fileNameUrlList = fileNameUrlList;

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

    @Override
    public boolean runAction() {
        this.execute();
        return false;
    }

    public void execute() {

        this.executorService = Executors.newFixedThreadPool(this.numThreads);
        initProcessQueue();

        int countPagesDownload=0;

        while (!this.linksQueue.isEmpty()){
            try {
                String currentURL=linksQueue.take();

                CrawlTask crawlTask=new CrawlTask(currentURL,this);
                this.executorService.submit(crawlTask);

            }catch (InterruptedException interruptedException){
                interruptedException.printStackTrace();
            }
        }
    }

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

    private void initProcessQueue(){
        for (String s: this.urlsToCrawl){
            linksQueue.add(s);
        }
    }
}
