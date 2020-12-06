/*
 * SearchAction
 *
 * Version 1.0
 *
 * All rights reserved.
 */

package action.pack;

import file_handlers.FileWorker;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

/**
 * Class implementing keyword based search operation for the web crawler
 *
 * @author CorinaTanase
 */

public class SearchAction extends InternAction {

    /**
     * keyword used for search action
     */
    private String keyWord;

    /**
     * SearchAction constructor
     *
     * @param filePath absolute path of file
     * @param keyWord  search keyword
     */
    public SearchAction(String filePath, String keyWord)  {
        super(filePath);
        this.keyWord = keyWord;
    }

    @Override
    public boolean runAction() {
        return false;
    }
}