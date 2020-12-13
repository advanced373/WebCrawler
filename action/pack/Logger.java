/*
 * Logger
 *
 * Version 1.0
 *
 * All rights reserved.
 */

package action.pack;

import java.io.FileNotFoundException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * This class has a management roll. It interprets the
 * parameters given as input in command line and based
 * on this parameters it start the right action. The
 * class catch all the errors thrown from program
 *
 * @author Vlijia Stefan
 */


public class Logger {
    private IAction actionObj;

    /**
     * Function that test if a given path is valid/exists
     *
     * @param path the path to be tested
     * @return true if is valid or false if not
     */

    private static boolean isValidPath(String path) {
        try {
            Paths.get( path );
        } catch (InvalidPathException e) {
            return false;
        }
        return true;
    }

    /**
     * Function that create and start a new action
     *
     * @param option    specify the type of action
     * @param atributes is an string array that contain
     * @return
     */

    public boolean newAction(String option, String[] atributes) throws CrawlerException, FileNotFoundException
    {
        if (option.equals( "-help" )) {
            //this.actionObj = new HelpAction();
        }
        if (option.equals( "Crawl" )) {
            //boolean robots = atributes[atributes.length-2].equals("-robots");
            ArrayList<String> param = new ArrayList<>();
            param.add( atributes[0] );
            this.actionObj = new Crawl( "", "file.conf", "seed.txt", param );

        }
        if (option.equals( "Search" )) {
            if (!isValidPath( atributes[atributes.length - 1] )) {
                //invalid path
                // System.out.println("invalid path");
                throw new CrawlerException( "110", "Invalid Path" );
            }
            this.actionObj = new SearchAction( atributes[1], atributes[2], atributes[3] );
        }

        if (option.equals( "Sitemap" )) {
            if (!isValidPath( atributes[atributes.length - 1] )) {
                //System.out.println("invalid path");
                throw new CrawlerException( "110", "Invalid Path" );
            }
            this.actionObj = new SitemapAction( atributes[1] );
        }

        if (option.equals( "Filter" )) {
            if (!isValidPath( atributes[atributes.length - 1] )) {
                //invalid path
                //System.out.println("invalid path");
                throw new CrawlerException( "110", "Invalid Path" );
            }
            this.actionObj = new FilterAction( atributes[1], atributes[2], atributes[3] );
        }
        return true;
    }

    public boolean runAction ()
    {
        return actionObj.runAction();
    }
}
