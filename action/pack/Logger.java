package action.pack;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;


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
