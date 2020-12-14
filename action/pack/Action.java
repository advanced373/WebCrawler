/*
 * IndexFileWork
 *
 * Version 1.0
 *
 * All rights reserved.
 */

package action.pack;

import java.io.IOException;

/**
 * Abstract class that implements IAction interface

 * @author Vlijia Stefan
 */

public abstract class Action implements IAction {

    /** the path to site directory */
    protected String filePath;

    /**
     * Class constructor
     *
     * @param  filePath is the path to site directory
     * */
    public Action(String filePath) {
        this.filePath = filePath;
    }

    /**
     *Method that will be overrided by an concret
     * action class.
     * */

    @Override
    public abstract boolean runAction() throws IOException;
}
