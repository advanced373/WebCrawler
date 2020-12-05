package file_handlers;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
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
    public ArrayList<String> ReadFromURLsFile(String pathToFile) throws FileNotFoundException, MalformedURLException {
        fileWork = new NormalFileWork();
        ArrayList<String> SeedURLs =  fileWork.read(pathToFile);
        fileWork = null;
        return SeedURLs;
    }
    public void writeSiteMap(String pathToFile) {
    }
    public void writeToIndexFile(String pathToSiteFolder) {
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
    public ArrayList<String> readFromConfigureFile(String path) throws FileNotFoundException, MalformedURLException {
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
    public ArrayList<String> readFromHTMLFile(String siteURL, String path) throws IOException {
        fileWork = new HTMLFileWork();
        ArrayList<String> URLs=fileWork.read(path);
        fileWork.write(path,siteURL);
        fileWork = null;
        return URLs;
    }
}
