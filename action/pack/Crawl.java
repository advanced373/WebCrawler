package action.pack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

public class Crawl extends ExternAction{


    private List<String> seedURL =new ArrayList<>();
    private LinkedBlockingQueue<String> linksQueue=new LinkedBlockingQueue<>();
    private ExecutorService executorService;
    private Integer numThreads;

    public Crawl(List<String> urlToCrawl, Integer numThreads) {

    }

    public Crawl(List<String> urlToCrawl) {

    }

    @Override
    public boolean runAction() {
        return false;
    }
}
