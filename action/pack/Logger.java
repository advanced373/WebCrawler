/*
 * IndexFileWork
 *
 * Version 1.0
 *
 * All rights reserved.
 */

package action.pack;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

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

    /**
     * Function that create and start a new action
     *
     * @param option specify the type of action
     * @param atributes is an string array that contain
     *                  the arguments given in command line
     */

    public void newAction(String option, String[] atributes)
    {
        if(option.equals("-help"))
        {
            //crate a help action
        }
        if(option.equals("-crawl"))
        {
            boolean robots = atributes[atributes.length-2].equals("-robots");
            if(robots)
            {
                //create a crawl action with robots option
            }
            else
            {
                //create a crawl action without robots option
            }
        }
        if(option.equals("-list"))
        {
            if(!isValidPath(atributes[atributes.length-1]))
            {
                //invalid path
            }
            //create a list action
        }
        if(option.equals("-search"))
        {
            if(!isValidPath(atributes[atributes.length-1]))
            {
                //invalid path
            }
            //create a search action
        }
        if(option.equals("-sitemap"))
        {
            if(!isValidPath(atributes[atributes.length-1]))
            {
                //invalid path
            }
            //create a sitemap action
        }
        if(option.equals("-filter"))
        {
            if(!isValidPath(atributes[atributes.length-1]))
            {
                //invalid path
            }
            //create a filter action
        }
    }

    public boolean runAction()
    {
        return actionObj.runAction();
    }
}
