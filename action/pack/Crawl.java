package action.pack;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import  file_handlers.FileWork;

public class Crawl extends ExternAction{


    private List<String> urlsToCrawl =new ArrayList<>();
    private LinkedBlockingQueue<String> linksQueue=new LinkedBlockingQueue<>();
    private ExecutorService executorService;
    private Integer numThreads;
    private String rootDir;
    private Integer delay;
    private Integer logLevel;

    public String fileNameConf;
    public String fileNameUrlList;


    public Crawl(String fileNameConf, String fileNameUrlList) {
        this.fileNameConf = fileNameConf;
        this.fileNameUrlList = fileNameUrlList;
    }

    @Override
    public boolean runAction() {
        this.execute();
        return false;
    }

    public void execute() {

        this.executorService = Executors.newFixedThreadPool(this.numThreads);
    }
}
