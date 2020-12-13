/*
 * InternAction
 *
 * Version 1.0
 *
 * All rights reserved.
 */

package action.pack;

import java.io.IOException;

/**
 * Abstract class that extends Action abstract class
 *
 * @author Vlijia Stefan
 */

public abstract class InternAction extends Action {

    public InternAction(String filePath) {
        super(filePath);
    }

    /**
     *Method that will be overrided by an concret
     * action class.
     * */

    public abstract boolean runAction() throws IOException;

}
