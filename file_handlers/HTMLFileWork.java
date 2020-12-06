package file_handlers;
import action.pack.Util;

import java.io.*;
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
public class HTMLFileWork extends FileWork{
    /**
     * Function responsible for reading URLs from file, using REGEX.
     *
     * @param fileName is absolute path to file
     * @return list of URLs
     */
    @Override
    protected  ArrayList<String> read(String fileName) throws FileNotFoundException, MalformedURLException {
        File inFile = new File(fileName);
        Pattern pattern;
        Matcher matcher;
        pattern=Pattern.compile("([^\\\\s]+(\\\\.(?i)(html|php))$)");
        matcher = pattern.matcher(fileName);
        if(!matcher.matches()){
            return null;
        }
        Scanner myReader = new Scanner(inFile);
        ArrayList <String> URLs = new ArrayList<>();
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            pattern= Pattern.compile("href=\"(.*?)\"");
            matcher= pattern.matcher(data);
            int i=0;
            String helperNormalization;
            while (matcher.find()) {
                    helperNormalization = Util.normalize(matcher.group(1));
                    URLs.add(helperNormalization);
                    i++;
                }
        }
        return URLs;
    }

    @Override
    protected void write(String fileName, String data) throws IOException {
        URL myURL = new URL(data);
        if(myURL.getPath() != null) {
            String mySiteName = myURL.getHost();
            String fileNameCopy=fileName;
            String pathToSite = "";
            String parts[] = fileName.split("\\\\");
            String nameOfMyFile = parts[parts.length-1];
            String newFileContent="";
            for (String part : parts) {
                if (!part.equals(mySiteName)) {
                    pathToSite += part;
                    pathToSite += "\\";
                } else
                {
                    pathToSite += part;
                    pathToSite += "\\";
                    break;
                }

            }
            File f = new File(pathToSite);
            File[] matchingFiles = f.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.endsWith("html") || name.endsWith("php");
                }
            });
            Matcher matcher;
            Pattern pattern;
            Scanner myReader = new Scanner(matchingFiles[0]);
            String regex = "href=\".*"+ nameOfMyFile+"\"";
            while (myReader.hasNextLine()) {
                String row = myReader.nextLine();
                pattern= Pattern.compile(regex);
                matcher= pattern.matcher(row);
                if(matcher.find())
                {
                    row = row.replace(matcher.group(0),"href=\""+fileNameCopy+"\"");
                    System.out.println(row);
                }
                newFileContent = newFileContent + row + System.lineSeparator();
            }
            myReader.close();
            FileWriter writer = new FileWriter(matchingFiles[0]);
            writer.write(newFileContent);
            writer.close();
        }
    }

    @Override
    protected ArrayList<String> search(String pathToFile, String word) {
        return null;
    }

    @Override
    protected ArrayList<String> filter(String pathToFile, String filter) {
        return null;
    }
}
