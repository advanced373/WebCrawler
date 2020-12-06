/*
 * ExternAction
 *
 * Version 1.0
 *
 * All rights reserved.
 */

package action.pack;



public abstract class ExternAction extends Action {

    public ExternAction(String filePath) {
        super(filePath);
    }

    public abstract boolean runAction();
}
