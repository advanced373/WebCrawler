package action.pack;


import java.io.IOException;

/**
 * Abstract class that implements IAction interface
 *
 * @author Vlijia Stefan
 */

public abstract class Action implements IAction {

    protected String filePath;

    public Action(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public abstract boolean runAction() throws IOException;
}
