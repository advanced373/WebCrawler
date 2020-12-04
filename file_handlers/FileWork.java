package file_handlers;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Class implementing work with different file types
 * @author Stoica Mihai
 */
abstract public class FileWork {
    /** type of file */
    private int type;
    /** local file path */
    private String path;
    /**
     * Function responsoble for reading different type of data (it depends on file type)
     * @param fileName is absolute path to file
     * @return list of data (it depends on file type)
     */
    protected abstract ArrayList<String> read(String fileName) throws FileNotFoundException;
    /**
     * Function responsible for writing data to file (it depends on file type)
     * @param value is absolute path to file
     */
    protected abstract void write(String value);
    /**
     * Function responsible for searching a word in file
     * @param word is a keyword
     * @return list of data (it depends of file type)
     */
    protected abstract ArrayList<String> search(String word);
    /**
     * Function responsoble for filtering values from file (it depends on file type)
     * @param filter is a word by which we are filtering data from file
     * @return list of filtered data (it depends on file type)
     */
    protected abstract ArrayList<String> filter(String filter);
}
