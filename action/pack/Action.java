package action.pack;

public abstract class Action implements IAction {

    private String filePath;

    @Override
    public abstract boolean runAction();
}
