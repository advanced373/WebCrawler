import action.pack.*;
import action.pack.IAction;
import crawler_log.LogManager;
import crawler_log.LoggerType;
import file_handlers.FileWorker;
import action.pack.Crawl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;

class Main {
    static enum Options {
        List("-list"),
        Crawl("-crawl"),
        Search("-search"),
        Filter("-filter"),
        Help("-help"),
        Sitemap("-sitemap");

        Options(String opt) {
        }
    }

    public static <E extends Enum<E>> boolean contains(Class<E> _enumClass,
                                                       String value) {
        try {
            return EnumSet.allOf(_enumClass)
                    .contains(Enum.valueOf(_enumClass, value));
        } catch (Exception e) {
            return false;
        }
    }


    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                throw new CrawlerException("101", "Missing parameters");
                //de adaugat log
            }
            if (!contains(Options.class, args[0])) {
                throw new CrawlerException("102", "Invalid option");
                //de adaugat log
            }
            Logger loggerObj = new Logger();
            loggerObj.newAction(args[0], args);
            if (!loggerObj.runAction()) {
                throw new CrawlerException("103", "Unssucceseful action");
                //de adaugat log
            }
        } catch (CrawlerException | IOException e) {
            /*System.out.println("An error occurred.");
            e.printStackTrace();*/
            System.out.println(e.getMessage());
            //de adaugat log
        }

//        ArrayList<String> param = new ArrayList<>();
//        param.add("no");
//
//
//        IAction c = new SitemapAction("D:\\Download\\dzone.com");
//        c.runAction();


        /*
        IAction action=new SearchAction("D:\\anul 4\\semestrul 1\\ingineria programarii\\tema1\\root","D:\\anul 4\\semestrul 1\\ingineria programarii\\tema1\\root\\ajax.googleapis.com", "Foundation");
        action.runAction();
         */
/*
        action=new FilterAction("D:\\anul 4\\semestrul 1\\ingineria programarii\\tema1\\root","D:\\anul 4\\semestrul 1\\ingineria programarii\\tema1\\root\\ajax.googleapis.com", "html");
        action.runAction();*/

        /*
        Logger logger = LogManager.getLogger(LoggerType.ConsoleLogger);
        logger.log(Level.FINE,"Helllo!!");
        logger.log(Level.FINE,"Helllo!!");
        logger.log(Level.FINE,"Helllo!!");
        */

        ArrayList<String> param=new ArrayList<>();
        param.add("no");
        
        //IAction c= new SitemapAction("D:\\Download\\dzone.com","sitemap.txt");
        //SitemapAction.displayDirectoryContents(new File("D:\\Download\\dzone.com"));

        // for(int i=0;i<100;i++) {
           /* long startTime = System.currentTimeMillis();
            try {
                IAction action = new Crawl( "", "file.conf", "seed.txt", param );
                action.runAction();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;

            System.out.println( "URL's crawled : " + " in " + totalTime + " i: " );*/
        //}


    }
}
