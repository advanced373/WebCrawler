/*
 * IndexFileWork
 *
 * Version 1.0
 *
 * All rights reserved.
 */

package file_handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class implementing work with index.json file
 *
 * @author Vlijia Stefan
 */

public class IndexFileWork extends FileWork {

    /**
     * Function responsible for reading all data from index.json file.
     *
     * @param fileName is absolute path to file
     * @return list( type ArrayList ) of data
     */

    @Override
    protected ArrayList<String> read(String fileName) throws FileNotFoundException {
        ArrayList<String> lines = new ArrayList<>();
        File myObj = new File(fileName);
        Scanner myReader = new Scanner(myObj);

        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            lines.add(data);
        }
        myReader.close();

        return lines;
    }

    /**
     * Function responsible for writing data
     * to index.json file
     *
     * @param fileName is absolute path to file
     * @param data is the data string to be written
     * @throws IOException in case of write errors
     */

    @Override
    protected void write(String fileName, String data) throws IOException {
        File indexFile = new File(fileName);
        FileWriter myWriter = new FileWriter(indexFile);
        myWriter.write(data);
        myWriter.close();
    }

    /**
     * Function responsible for writing data syncronized
     * to index.json file
     *
     * @param indexFile is the File object for the indexFile
     * @param data is the data string to be written
     * @throws IOException in case of write errors
     */

    protected void writeSyncronized(File indexFile, String data) throws IOException {
        FileWriter myWriter = new FileWriter(indexFile);
        myWriter.write(data);
        myWriter.close();
    }

    /**
     * Function responsible for searching a given keyword in index.json file
     *
     * @param  pathToSiteFolder is absolute path to folder where the site is downloaded
     * @param  word is the data string to be searched
     * @return an ArrayList that contain the site URLs that contain the given keyword
     *         or null in case that index.json file doesn't exist
     * @throws FileNotFoundException if index.json file doesn't exist
     */

    @Override
    protected ArrayList<String> search(String pathToSiteFolder, String word) throws FileNotFoundException {

        String rootPath = new String();
        String[] path = pathToSiteFolder.split("\\\\");

        for(int i = 0; i<path.length - 1; i++)
        {
            rootPath += path[i];
            rootPath +="\\";
        }

        String siteDomainName = path[path.length - 1];
        String pathToIndexFile = rootPath + "index.json";

        ArrayList<String> indexContent = read(pathToIndexFile);
        ArrayList<String> retData = new ArrayList<>();
        int currentPoz;
        int i;

        Pattern p = Pattern.compile("\"([^\"]*)\"");
        for(i=0; i<indexContent.size(); i++)
        {
            Matcher m = p.matcher(indexContent.get(i));
            if(m.find() && m.group(1).equals(word))
            {
                currentPoz = i;

                while(!indexContent.get(i).contains("{"))
                {
                    i--;
                }

                m = p.matcher(indexContent.get(i));
                if(m.find() && m.group(1).contains(siteDomainName)) {
                        retData.add(m.group(1));
                }
                i = currentPoz;
            }
        }
        return retData;
    }

    /**
     * Function responsible for filtering the index.json file
     * by an filtering criteria
     *
     * @param  pathToSiteFolder is absolute path to the folder where
     *                          the site is downloaded
     * @param  filter is an file extension on the basis of which the
     *                filtering is done
     * @return an ArrayList that contain the site URLs that contain the given filter
     *         or null in case that index.json file doesn't exist. 0 for dir 1 for file.
     * @throws FileNotFoundException if index.json file doesn't exist
     */

    @Override
    protected ArrayList<String> filter(String pathToSiteFolder, String filter) throws FileNotFoundException {

        String rootPath = new String();
        String[] path = pathToSiteFolder.split("\\\\");

        for(int i = 0; i<path.length - 1; i++)
        {
            rootPath += path[i];
            rootPath +="\\";
        }

        String siteDomainName = path[path.length - 1];
        String pathToIndexFile = rootPath + "index.json";

        ArrayList<String> indexContent = read(pathToIndexFile);
        ArrayList<String> retData = new ArrayList<>();
        int currentPoz;

        int i;

        Pattern p = Pattern.compile("\"([^\"]*)\"");
        for(i=0; i<indexContent.size(); i++)
        {
            if(indexContent.get(i).contains(filter)
                    && !indexContent.get(i).contains("{"))
            {
                currentPoz = i;

                while(!indexContent.get(i).contains("{"))
                {
                    i--;
                }

                Matcher m = p.matcher(indexContent.get(i));
                if(m.find() && m.group(1).contains(siteDomainName)) {
                    Matcher m2 = p.matcher(indexContent.get(currentPoz));
                    m2.find();
                    retData.add(m.group(1)+"/"+ m2.group(1));
                }

                i = currentPoz;
            }
        }

        return retData;
    }

    /**
     * Function responsible to add a new keyword to an existing entry in
     * index.json file
     *
     * @param  indexFilePath is absolute path to the index.json file
     * @param  keyWord is the word to be added
     * @param  filePath is the pat to the file without the path to the root folder
     * @return an ArrayList that contain the site URLs that contain the given filter
     *         or null in case that index.json file doesn't exist
     */

    protected boolean addKeyWord(String indexFilePath, String keyWord, String filePath)
    {
        String strToWrite = new String();

        try {

            File indexFile = new File(indexFilePath);

            int lineNum = 0;

            Scanner scanner = new Scanner(indexFile);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineNum++;
                strToWrite += line;
                strToWrite += "\n";
                if(line.contains(filePath)) {
                    while(!line.contains("keyword") && !line.contains("["))
                    {
                        line = scanner.nextLine();
                        lineNum++;
                        strToWrite += line;
                        strToWrite += "\n";
                    }
                    strToWrite += "\t\t\t\t";
                    strToWrite = strToWrite + "\"" + keyWord;
                    line = scanner.nextLine();
                    lineNum++;
                    if(line.contains("]"))
                    {
                        strToWrite += "\"\n";
                    }
                    else
                    {
                        strToWrite += "\",\n";
                    }
                    strToWrite += line;
                    strToWrite += "\n";
                }
            }

            this.write(indexFilePath, strToWrite);

        } catch(FileNotFoundException e) {
            return false;
        } catch (MalformedURLException e) {
            return false;
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    /**
     * Function responsible to add a new keyword to an existing entry in
     * index.json file
     *
     * @param  indexFilePath is absolute path to the index.json file
     * @param  dirPath is the word to be added
     * @param  fileName is the pat to the file without the path to the root folder
     * @param fileType is the type of file (images or documents)
     * @return true if the adding operation was successful or
     *         false if the operation failed
     */

    protected boolean addFileToDir(String indexFilePath,  String dirPath, String fileName, String fileType)
    {
        String strToWrite = new String();

        try {

            File indexFile = new File(indexFilePath);

            int lineNum = 0;

            Scanner scanner = new Scanner(indexFile);

            Pattern p = Pattern.compile("\"([^\"]*)\"");

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineNum++;
                strToWrite += line;
                strToWrite += "\n";

                Matcher m = p.matcher(line);
                if(m.find()  && m.group(1).equals(dirPath)) {
                    while(true)
                    {
                        if(line.contains(fileType) && line.contains("["))
                        {
                            break;
                        }
                        line = scanner.nextLine();
                        lineNum++;
                        strToWrite += line;
                        strToWrite += "\n";
                    }
                    strToWrite += "\t\t\t\t";
                    strToWrite = strToWrite + "\"" + fileName + "\",\n";
                }
            }

            this.write(indexFilePath, strToWrite);

        } catch(FileNotFoundException e) {
            return false;
        } catch (MalformedURLException e) {
            return false;
        } catch (IOException e) {
            return false;
        }

        return true;
    }
}
