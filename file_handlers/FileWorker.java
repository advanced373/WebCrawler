package file_handlers;
import java.io.FileNotFoundException;
import java.util.ArrayList;


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
    public ArrayList<String> ReadFromURLsFile(String pathToFile) throws FileNotFoundException {
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

        //write the updated data to index.json file
        fileWork = new IndexFileWorker();
        fileWork.write(indexFilePath, strToWrite);
    }

    public void readFromIndexFile(String siteURL, String argument) {
    }
    public String searchInIndexFile(String argument) {
        return null;
    }

    /**
     * Function responsible for reading configure file.
     * @param path is absolute path where file was saved
     * @return list of configure file rows
     */
    public ArrayList<String> readFromConfigureFile(String path) throws FileNotFoundException {
        fileWork = new NormalFileWork();
        ArrayList<String> rows =  fileWork.read(path);
        fileWork = null;
        return rows;
    }
    public void writeToConfigureFile(String argument, String value) {
    }
    public ArrayList<String> filterInIndexFile(String type) {
        return null;
    }
    /**
     * Function responsible for reading URLs from the file given.
     * @param path is absolute path where file was saved
     * @param siteURL is Seed URL
     * @return list of URLs extracted from file
     */
    public ArrayList<String> readFromHTMLFile(String siteURL, String path) throws FileNotFoundException {
        fileWork = new HTMLFileWork();
        ArrayList<String> URLs=fileWork.read(path);
        fileWork = null;
        return URLs;
    }
}
