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
     * @param pathToFile is the location in which we are searching
     * @return list of lines index where word was found
     */

    @Override
    protected ArrayList<String> search(String pathToFile, String word) throws FileNotFoundException {
        File inFile = new File(pathToFile);
        Scanner myReader = new Scanner(inFile);
        ArrayList <String> lines = new ArrayList<>();
        int i=0;
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            if(data.indexOf(word) != -1)
            {
                lines.add(Integer.toString(i));
            }
            i++;
        }
        return lines;
    }

    /**
     * Function responsible for filtering values from file (it depends on file type)
     *
     * @param filter is a word by which we are filtering data from file
     * @return list of filtered data (it depends on file type)
     */

    @Override
    protected ArrayList<String> filter(String pathToFile, String filter) {
        return null;
    }
}
