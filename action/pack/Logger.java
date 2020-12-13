/*
 * Logger
 *
 * Version 1.0
 *
 * All rights reserved.
 */

package action.pack;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;


public class Logger {
    private IAction actionObj;

    private static boolean isValidPath(String path)
    {
        try{
            Paths.get(path);
        }
        catch (InvalidPathException e)
        {
            return false;
        }
        return true;
    }

    public void newAction(String option, String[] atributes) throws CrawlerException, FileNotFoundException {
        if (option.equals("-help")) {
            //crate a help action
            this.actionObj = new HelpAction();
        }
        if (option.equals("Crawl")) {
            //boolean robots = atributes[atributes.length-2].equals("-robots");
            ArrayList<String> param = new ArrayList<>();
            param.add(atributes[0]);
            this.actionObj = new Crawl("", "file.conf", "seed.txt", param);

        }
        if (option.equals("Search")) {
            if (!isValidPath(atributes[atributes.length - 1])) {
                //invalid path
               // System.out.println("invalid path");
                throw new CrawlerException("110","Invalid Path");
            }
            this.actionObj = new SearchAction(atributes[1], atributes[2], atributes[3]);
        }
        if (option.equals("Sitemap")) {
            if (!isValidPath(atributes[atributes.length - 1])) {
                //System.out.println("invalid path");
                throw new CrawlerException("110","Invalid Path");
            }
            this.actionObj = new SitemapAction(atributes[1]);
        }
        if (option.equals("Filter")) {
            if (!isValidPath(atributes[atributes.length - 1])) {
                //invalid path
                //System.out.println("invalid path");
                throw new CrawlerException("110","Invalid Path");
            }
            this.actionObj = new FilterAction(atributes[1], atributes[2], atributes[3]);
        }
    }

    public boolean runAction()
    {
        return actionObj.runAction();
    }
}
