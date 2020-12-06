package action.pack;

public abstract class Action implements IAction {

    private String filePath;

    public Action(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public abstract boolean runAction();
}
