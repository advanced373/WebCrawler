package file_handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Class offers public functions to work with files and uses FileWork for
 * different type of files.
 * @author Stoica Mihai
 */

public class FileWorker {
    FileWork fileWork;
    /**
     * Function responsible for reading Seed URLs ( we call seed because we'll also read URLs from
     * this site).
     * @param pathToFile is absolute path where file was saved
     * @return list of URLs
     */
    public ArrayList<String> ReadFromURLsFile(String pathToFile) throws FileNotFoundException, MalformedURLException {
        fileWork = new NormalFileWork();
        ArrayList<String> SeedURLs =  fileWork.read(pathToFile);
        fileWork = null;
        return SeedURLs;
    }
    public void writeSiteMap(String pathToFile) {
    }

    /**
     * Function responsible for write new entry in index.json file
     *
     * @author Vlijia Stefan
     * @param  pathToRootFolder is absolute path to the root folder
     * @param  dataArray is an array with data for the new entry
     * @throws IOException in case of read/write error and
     *         FileNotFoundException if the file doesn't exist
     */

    public static void writeToIndexFile(String pathToRootFolder, ArrayList<String> dataArray) throws IOException, FileNotFoundException {

        String indexFilePath = pathToRootFolder + "/index.json";

        if(dataArray.isEmpty())
        {
            return;
        }

        String entry = new String();

        //create an entry for the given URL
        for(int i = 0; i< dataArray.size(); i++) {
            String field = dataArray.get(i);

            if (i == 0) {
                entry = entry + "\t\""+ dataArray.get(i) +"\": {\n";

            }

            if (field.equals("keywords")
                    || field.equals("images")
                    || field.equals("documents")) {

                if (field.equals("keywords")) {
                    entry += "\t\t\t\"keywords\": [\n";
                }
                if (field.equals("images")) {
                    entry += "\t\t\t\"images\": [\n";
                }
                if (field.equals("documents")) {
                    entry += "\t\t\t\"documents\": [\n";
                }

                i++;
                int nrOfElements = Integer.parseInt(dataArray.get(i));
                i++;
                for (int j = i; j < i + nrOfElements; j++) {
                    entry += "\t\t\t\t";
                    if (j == i + nrOfElements - 1) {
                        entry = entry + "\"" + dataArray.get(j) + "\"\n\t\t\t],\n";
                    } else {
                        entry = entry + "\"" + dataArray.get(j) + "\",\n";
                    }
                }
                i = i + nrOfElements - 1;
            }

            if (field.equals("sitemap")) {
                entry += "\t\t\t\"sitemap\": ";
                entry = entry + "\"" + dataArray.get(i + 1) + "\"\n\t\t}";
            }
        }

        String strURL = dataArray.get(0);
        URL siteURL = new URL(strURL);
        String siteDomainName = siteURL.getHost();

        File indexFile = new File(indexFilePath);
        String strToWrite = new String();

        int exists = 0;
        Scanner scanner = null;
        try {
            scanner = new Scanner(indexFile);

            //firstly we will verify if the given URL belongs to an site that already exist in index.json file
            int lineNum = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineNum++;
                if(line.contains(siteDomainName)) {
                    exists = 1;
                    break;
                }
            }
            scanner.close();

            lineNum = 0;
            scanner = new Scanner(indexFile);

            //if the site already exist we will append the new entry for given URL inside the site entry
            //else we will create a new entry for the site that contain the new entry for the given URL
            if(exists == 1)
            {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    lineNum++;
                    strToWrite += line;
                    strToWrite += "\n";
                    if(line.contains("\"" + siteDomainName + "\"")) {
                        strToWrite = strToWrite + "\t" + entry + ",\n";
                    }
                }
            }
            else
            {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    lineNum++;
                    strToWrite += line;
                    strToWrite += "\n";
                    if(lineNum == 1) {
                        strToWrite = strToWrite + "\t" + "\"" + siteDomainName + "\": {\n";
                        strToWrite = strToWrite + "\t" + entry + "\n";
                        strToWrite = strToWrite + "\t},\n";
                    }
                }
            }

        } catch(FileNotFoundException e) {
            throw e;
        }

        fileWork = new IndexFileWork();
        fileWork.write(indexFilePath, strToWrite);
    }

    /**
     * Function responsible for read an entry corresponding to a given URL
     *
     * @author Vlijia Stefan
     * @param  siteURL the URL for which we want to get the corresponding
     *                 entry from index.json file
     * @param  pathToRootFolder is absolute path to the root folder
     * @return a ArrayList with the data corresponding to the entry or null
     *         if no entry exist
     * @throws FileNotFoundException if the index file isn't found and
     *         MalformedURLException if the given URL is wrong
     */

    public ArrayList<String> readFromIndexFile(String siteURL, String pathToRootFolder) throws FileNotFoundException, MalformedURLException {
        String indexFilePath = pathToRootFolder + "/index.json";
        ArrayList<String> returnEntry = new ArrayList<>();

        //read entire content of index.json
        fileWork = new IndexFileWork();
        ArrayList<String> content = fileWork.read(indexFilePath);

        //firstly we search the index for the given URL
        int index = -1;
        for(int i = 0; i<content.size(); i++)
        {
            if(content.get(i).contains(siteURL))
            {
                index = i;
                break;
            }
        }

        //if exist we will extract entire entry and put the data in an ArrayList
        if(index != -1)
        {
            String line = new String();
            Pattern p = Pattern.compile("\"([^\"]*)\"");

            for(int i = index; i<content.size(); i++)
            {
                line = content.get(i);
                if(line.contains(siteURL))
                {
                    returnEntry.add(siteURL);
                }

                if(line.contains("keywords") || line.contains("images") || line.contains("documents"))
                {
                    i++;
                    if(line.contains("keywords"))
                    {
                        returnEntry.add("keywords");
                    }

                    if(line.contains("images"))
                    {
                        returnEntry.add("images");
                    }

                    if(line.contains("documents"))
                    {
                        returnEntry.add("documents");
                    }

                    int keywordsNr = i;
                    line = content.get(keywordsNr);
                    while(!line.contains("]")){
                        line = content.get(keywordsNr);
                        keywordsNr++;
                    }

                    returnEntry.add(Integer.toString(keywordsNr - i - 1));

                    for(int j = i; j<keywordsNr - 1; j++)
                    {
                        line = content.get(j);
                        Matcher m = p.matcher(line);
                        if(m.find()) {
                            returnEntry.add(m.group(1));
                        }
                    }
                    i = keywordsNr - 1;
                }

                if(line.contains("sitemap"))
                {
                    returnEntry.add("sitemap");
                    Matcher m = p.matcher(line);
                    m.find();
                    if(m.find()) {
                        returnEntry.add(m.group(1));
                    }
                    break;
                }

            }
        }
        else
        {
            return null;
        }

        return returnEntry;
    }
  
    /**
     * Function responsible searching an keyword in index.json file
     *
     * @author Vlijia Stefan
     * @param argument is the keyword to search
     * @param pathToRootFolder is the absolute path to the root folder
     * @return a list of site URLs that contain the given keyword
     *         or null in case that keyword doesn't exist
     */

    public ArrayList<String> searchInIndexFile(String argument, String pathToRootFolder) {

        String indexFilePath = pathToRootFolder + "/index.json";

        fileWork = new IndexFileWork();
        return fileWork.search(indexFilePath, argument);
    }

    /**
     * Function responsible for reading configure file.
     * @param path is absolute path where file was saved
     * @return list of configure file rows
     */

    public ArrayList<String> readFromConfigureFile(String path) throws FileNotFoundException, MalformedURLException {
        fileWork = new NormalFileWork();
        ArrayList<String> rows =  fileWork.read(path);
        fileWork = null;
        return rows;
    }

    public void writeToConfigureFile(String argument, String value) {
    }

    /**
     * Function responsible for filtering the index.json file
     * by an filtering criteria
     *
     * @author Vlijia Stefan
     * @param  pathToRootFolder is absolute path to the root folder
     * @param  argument is an file extension on the basis of which the
     *                  filtering is done
     * @return an ArrayList that contain the site URLs that contain the given filter
     *         or null in case that index.json file doesn't exist
     */

    public ArrayList<String> filterInIndexFile(String argument, String pathToRootFolder) {
        String indexFilePath = pathToRootFolder + "/index.json";

        fileWork = new IndexFileWork();
        return fileWork.filter(indexFilePath, argument);
    }
    /**
     * Function responsible for reading URLs from the file given.
     * @param path is absolute path where file was saved
     * @param siteURL is Seed URL
     * @return list of URLs extracted from file
     */
    public ArrayList<String> readFromHTMLFile(String siteURL, String path) throws IOException {
        fileWork = new HTMLFileWork();
        ArrayList<String> URLs=fileWork.read(path);
        fileWork.write(path,siteURL);
        fileWork = null;
        return URLs;
    }
}
