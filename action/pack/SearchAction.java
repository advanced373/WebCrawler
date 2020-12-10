/*
 * SearchAction
 *
 * Version 1.0
 *
 * All rights reserved.
 */

package action.pack;

import file_handlers.FileWorker;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class implementing keyword based search operation for the web crawler
 *
 * @author CorinaTanase
 */

public class SearchAction extends InternAction {

    /**
     * keyword used for search action
     */
    private String keyWord;
    /** the path to root folder */
    private String rootpath;

    /**
     * SearchAction constructor
     *
     * @param filePath absolute path of site folder
     * @param keyWord  search keyword
     * @param rootPath path to root folder
     */

    public SearchAction(String rootPath, String filePath, String keyWord)  {
        super(filePath);
        this.keyWord = keyWord;
        this.rootpath = rootPath;
    }

    /**
     * This method made the conversion from Stream<T> type to ArrayList<T> type
     *
     * @param stream the stream to be converter to ArrayList
     * @return an list of type ArrayList
     */

    private <T> ArrayList<T>  getArrayListFromStream(Stream<T> stream)
    {

        // Convert the Stream to List
        List<T>
                list = stream.collect(Collectors.toList());

        // Create an ArrayList of the List
        ArrayList<T>
                arrayList = new ArrayList<T>(list);

        // Return the ArrayList
        return arrayList;
    }

    /**
     * This method implements the search action. Firstly is verified the
     * index.json file.
     * If keyword is found, then it is printed the file path and the
     * lines in the file where the keyword appear.
     * If keyword isn't found, then the keyword it is searched in each
     * file of the site. When the keyword is found, it is added in index.json file
     *
     * @return true if search action have success or
     *         false if search action fail
     */

    @Override
    public boolean runAction() {

        FileWorker fileWorkerObj = new FileWorker();
        ArrayList<String> listOfURLs = new ArrayList<String>();

        try{
            listOfURLs = fileWorkerObj.searchInIndexFile(keyWord, filePath);

            if(listOfURLs.isEmpty())
            {
                System.out.println("The keyword doesn't exist in index.json file!\n");
            }
            else
            {
                for(int i = 0; i<listOfURLs.size(); i++)
                {
                    String pathToFile  = listOfURLs.get(i).toString();
                    pathToFile = pathToFile.replaceFirst("(http://|https://)", "");
                    pathToFile = pathToFile.replace("/", "\\");
                    pathToFile = rootpath + "\\" + pathToFile;
                    ArrayList<String> lines = fileWorkerObj.SearchInNormalFile(pathToFile,keyWord);

                    System.out.println("File: "+pathToFile);
                    if(!lines.isEmpty())
                    {
                        System.out.println(keyWord + " apper on lines: "+ lines.toString()+"\n");

                    }
                }

                return true;
            }

            //if searched word doesn't exist in index.json file then we
            //search the word in every regular file from site and
            //the word will be added in index.json file
            Stream<Path> pathArray = Files.walk(Paths.get(filePath))
                    .filter(Files::isRegularFile);
            ArrayList<Path> pathList = new ArrayList<>(getArrayListFromStream(pathArray));

            for(Path path: pathList)
            {
                ArrayList<String> lines = fileWorkerObj.SearchInNormalFile(path.toString(),keyWord);
                System.out.println("File: "+path.toString());
                if(!lines.isEmpty())
                {
                    System.out.println(keyWord + " apper on lines: "+ lines.toString());

                    //making an path to search in index file
                    //this path doesn't contain the root folder
                    String filePathToSeargh  = new String();
                    filePathToSeargh = path.toString().replace(rootpath+"\\", "");
                    filePathToSeargh = filePathToSeargh.replace('\\', '/');

                    if(fileWorkerObj.addKeywordToIndexFile(rootpath+"\\index.json", keyWord, filePathToSeargh))
                    {
                        System.out.println("The new keyword was added to index.json file!\n");
                    }
                    else
                    {
                        System.out.println("Failed to add new keyword in index.json file!\n");
                    }
                }
                else
                {
                    System.out.println(keyWord + " doesn't appear in this file!\n");
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}