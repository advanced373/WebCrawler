/*
 * FilterAction
 *
 * Version 1.0
 *
 * All rights reserved.
 */

package action.pack;

/**
 * Class implementing resource type based filtering of crawl results
 *
 * @author CorinaTanase
 */

public class FilterAction extends InternAction {

    /**
     * type of resource used for filtering
     */
    private String resourceType;

    /**
     * FilterAction constructor
     *
     * @param filePath absolute path of file
     * @param resourceType  resource type filter is based on
     */
    public FilterAction(String filePath, String resourceType) {
        super(filePath);
        this.resourceType=resourceType;
    }


    @Override
    public boolean runAction() {
        return false;
    }
}
