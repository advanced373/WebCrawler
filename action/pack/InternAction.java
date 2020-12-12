/*
 * InternAction
 *
 * Version 1.0
 *
 * All rights reserved.
 */

package action.pack;

/**
 * Abstract class that extends Action abstract class
 *
 * @author Vlijia Stefan
 */

public abstract class InternAction extends Action {

    /**
     * Class constructor
     *
     * @param  filePath is the path to site directory
     * */

    public InternAction(String filePath) {
        super(filePath);
    }

    /**
     *Method that will be overrided by an concret
     * action class.
     * */

    public abstract boolean runAction();
}
