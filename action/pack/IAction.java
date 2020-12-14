/*
 * IAction
 *
 * Version 1.0
 *
 * All rights reserved.
 */

package action.pack;

import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;

/**
 * The interface that expose runAction() method.
 * This method will be overwritten by child actions
 *
 * @author Vlijia Stefan
 */

public interface IAction {
    public boolean runAction() throws IOException, BrokenBarrierException, InterruptedException;
}
