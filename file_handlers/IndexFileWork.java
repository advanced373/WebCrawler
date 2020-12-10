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
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                lines.add(data);
            }
            //System.out.println("Successfully read from file " + fileName );
            myReader.close();
        } catch (FileNotFoundException e) {
            throw e;
        }

        return lines;
    }

    /**
     * Function responsible for writing data to index.json file
     *
     * @param fileName is absolute path to file
     * @param data is the data string to be written
     * @throws IOException in case of write errors
     */

    @Override
    protected void write(String fileName, String data) throws IOException {
        try {
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write(data);
            myWriter.close();
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * Function responsible for searching a given keyword in index.json file
     *
     * @param  pathToFile is absolute path to index.json file
     * @param  word is the data string to be searched
     * @return an ArrayList that contain the site URLs that contain the given keyword
     *         or null in case that index.json file doesn't exist
     * @throws FileNotFoundException if index.json file doesn't exist
     */

    @Override
    protected ArrayList<String> search(String pathToFile, String word) {
        try {
            ArrayList<String> indexContent = read(pathToFile);
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
                    if(m.find()) {
                        retData.add(m.group(1));
                    }

                    i = currentPoz;
                }
            }

            return retData;

        } catch (FileNotFoundException e) {
            return null;
        }
    }

    /**
     * Function responsible for filtering the index.json file
     * by an filtering criteria
     *
     * @param  pathToFile is absolute path to file
     * @param  filter is an file extension on the basis of which the
     *                filtering is done
     * @return an ArrayList that contain the site URLs that contain the given filter
     *         or null in case that index.json file doesn't exist
     * @throws FileNotFoundException if index.json file doesn't exist
     */

    @Override
    protected ArrayList<String> filter(String pathToFile, String filter) {
        try {
            ArrayList<String> indexContent = read(pathToFile);
            ArrayList<String> retData = new ArrayList<>();
            int currentPoz;

            int i;

            Pattern p = Pattern.compile("\"([^\"]*)\"");
            for(i=0; i<indexContent.size(); i++)
            {
                if(indexContent.get(i).contains(filter)
                        && !indexContent.get(i).contains("{"))
                {
                    while(!indexContent.get(i).contains("]"))
                    {
                        i++;
                    }

                    currentPoz = i;

                    while(!indexContent.get(i).contains("{"))
                    {
                        i--;
                    }

                    Matcher m = p.matcher(indexContent.get(i));
                    if(m.find()) {
                        retData.add(m.group(1));
                    }

                    i = currentPoz;
                }
            }

            return retData;

        } catch (FileNotFoundException e) {
            return null;
        }
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
                    strToWrite = strToWrite + "\"" + keyWord + "\",\n";
                }
            }

            this.write(indexFilePath, strToWrite);

        } catch(FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
