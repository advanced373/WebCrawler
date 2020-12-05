package file_handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class implementing file basic operations
 * @author CorinaTanase
 */

public class NormalFileWork extends FileWork{
    /**
     * Function responsible for reading URLs from file.
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
     * Function responsible for writing data to a normal file
     *
     * @param fileName is absolute path to file
     * @param data is the data string to be written
     */
    @Override
    protected void write(String fileName, String data) throws IOException{

        try {
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write(data);
            myWriter.close();
            //System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
           throw e;
        }
    }

    /**
     * Function responsible for searching a word in file
     *
     * @param word is a keyword
     * @return list of data (it depends of file type)
     */
    @Override
    protected ArrayList<String> search(String word) {
        return null;
    }

    /**
     * Function responsible for filtering values from file (it depends on file type)
     *
     * @param filter is a word by which we are filtering data from file
     * @return list of filtered data (it depends on file type)
     */
    @Override
    protected ArrayList<String> filter(String filter) {
        return null;
    }
}
