/*
 * HelpAction
 *
 * Version 1.0
 *
 * All rights reserved.
 */

package action.pack;

import file_handlers.FileWork;
import file_handlers.FileWorker;
import file_handlers.NormalFileWork;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class for presenting the manual page of the web crawler operations
 *
 * @author CorinaTanase
 */

public class HelpAction extends InternAction{

    /**
     * Class constructor
     *
     * @param filePath is the path to the manual page
     */
    public HelpAction(String filePath) {
        super(filePath);
    }

    /**
     * Method to display man page
     */
    @Override
    public boolean runAction() throws IOException {
        FileWorker fw= new FileWorker();
        ArrayList <String> lines= fw.readFromManFile(filePath);

        for(String l : lines)
            System.out.println(l);
        return true;
    }
}
