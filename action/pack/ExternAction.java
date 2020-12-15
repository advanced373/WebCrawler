/*
 * ExternAction
 *
 * Version 1.0
 *
 * All rights reserved.
 */

package action.pack;

import java.util.concurrent.BrokenBarrierException;

/**
 * Abstract class that extends Action abstract class
 *
 * @author Vlijia Stefan
 */

public abstract class ExternAction extends Action {

    /**
     * Class constructor
     *
     * @param  filePath is the path to site directory
     * */

    public ExternAction(String filePath) {
        super(filePath);
    }

    /**
     *Method that will be overrided by an concret
     * action class.
     * */

    public abstract boolean runAction() throws BrokenBarrierException, InterruptedException;
}
