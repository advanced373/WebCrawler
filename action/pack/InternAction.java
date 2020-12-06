/*
 * InternAction
 *
 * Version 1.0
 *
 * All rights reserved.
 */

package action.pack;


public abstract class InternAction extends Action {

    public InternAction(String filePath) {
        super(filePath);
    }

    public abstract boolean runAction();
}
