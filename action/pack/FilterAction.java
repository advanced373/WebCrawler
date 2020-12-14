/*
 * FilterAction
 *
 * Version 1.0
 *
 * All rights reserved.
 */

package action.pack;

import file_handlers.FileWorker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

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
     * the path to root folder
     */
    private String rootpath;
    /**
     *
     */
    private long maxDimensions;

    /**
     * FilterAction constructor
     *
     * @param filePath     absolute path of file
     * @param resourceType resource type filter is based on
     */
    public FilterAction(String rootPath, String filePath, String resourceType) {
        super(filePath);
        this.resourceType = resourceType;
        this.rootpath = rootPath;
        this.maxDimensions = 0;
    }

    /**
     * FilterAction constructor
     *
     * @param filePath      absolute path of file
     * @param resourceType  resource type filter is based on
     * @param maxDimensions max size of file
     */
    public FilterAction(String rootPath, String filePath, String resourceType, long maxDimensions) {
        super(filePath);
        this.resourceType = resourceType;
        this.rootpath = rootPath;
        this.maxDimensions = maxDimensions;
    }

    /**
     * This method implements filter by a specified type the
     * content from a downloaded site using the index.json file
     *
     * @return true if filter action have success or
     * false if filter action fail
     */

    @Override
    public boolean runAction() throws FileNotFoundException {

        FileWorker fileWorkerObj = new FileWorker();
        ArrayList<String> listOfURLs = new ArrayList<String>();

        listOfURLs = fileWorkerObj.filterInIndexFile(resourceType, filePath);

        if (listOfURLs.isEmpty()) {
            System.out.println("Doesn't exists " + resourceType + " resources on this site!");
            return false;
        } else {
            System.out.println("\n" + resourceType + " resources are:" + "\n");
            for (int i = 0; i < listOfURLs.size(); i++) {
                String pathToFile = listOfURLs.get(i).toString();
                pathToFile = pathToFile.replaceFirst("(http://|https://)", "");
                pathToFile = pathToFile.replace("/", "\\");
                //pathToFile = rootpath + "\\" + pathToFile;

                if (this.maxDimensions > 0) {
                    File file = new File(pathToFile);
                    if (file.length() < this.maxDimensions)
                        System.out.println(pathToFile + "\n");
                } else {
                    System.out.println(pathToFile + "\n");
                }

            }
        }

        return true;
    }
}