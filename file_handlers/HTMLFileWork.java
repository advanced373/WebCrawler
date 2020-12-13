/*
 * HTMLFileWork
 *
 * Version 1.0
 *
 * All rights reserved.
 */
package file_handlers;
import action.pack.URLNormalization;
import action.pack.Util;
import crawler_log.LogManager;
import crawler_log.LoggerType;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
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
     * @param bothValues is a string that contains both siteURL (which is  and path
     * @return list of URLs
     */
    private final String fileName="help.txt";
    @Override
    protected  ArrayList<String> read(String bothValues) throws IOException {
        String parts[];
        parts = bothValues.split("!");
        String fileName = parts[1];
        String siteURL  = parts[0];
        File inFile = new File(fileName);
        if(inFile.isDirectory()){
            return null;
        }

        Pattern pattern;
        Matcher matcher;
        FileWriter valuesForWrite = new FileWriter(this.fileName,true);
        pattern=Pattern.compile("([^\\s]+(\\.(?i)(html|php))$)");
        matcher = pattern.matcher(fileName);
        if(!matcher.matches()){
            CheckFileType checkFileType = new CheckFileType();
            if(checkFileType.getType(fileName) == null)
            {
                return null;
            }
        }
        Scanner myReader = new Scanner(inFile);
        ArrayList <String> URLs = new ArrayList<>();
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            pattern = Pattern.compile("href=\"(?://(?:(?:[A-Za-z0-9\\-._~!$&'()*+,;=:]|%[0-9A-Fa-f]{2})*@)?(?:\\[(?:(?:(?:(?:[0-9A-Fa-f]{1,4}:){6}|::(?:[0-9A-Fa-f]{1,4}:){5}|(?:[0-9A-Fa-f]{1,4})?::(?:[0-9A-Fa-f]{1,4}:){4}|(?:(?:[0-9A-Fa-f]{1,4}:){0,1}[0-9A-Fa-f]{1,4})?::(?:[0-9A-Fa-f]{1,4}:){3}|(?:(?:[0-9A-Fa-f]{1,4}:){0,2}[0-9A-Fa-f]{1,4})?::(?:[0-9A-Fa-f]{1,4}:){2}|(?:(?:[0-9A-Fa-f]{1,4}:){0,3}[0-9A-Fa-f]{1,4})?::[0-9A-Fa-f]{1,4}:|(?:(?:[0-9A-Fa-f]{1,4}:){0,4}[0-9A-Fa-f]{1,4})?::)(?:[0-9A-Fa-f]{1,4}:[0-9A-Fa-f]{1,4}|(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))|(?:(?:[0-9A-Fa-f]{1,4}:){0,5}[0-9A-Fa-f]{1,4})?::[0-9A-Fa-f]{1,4}|(?:(?:[0-9A-Fa-f]{1,4}:){0,6}[0-9A-Fa-f]{1,4})?::)|[Vv][0-9A-Fa-f]+\\.[A-Za-z0-9\\-._~!$&'()*+,;=:]+)\\]|(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)|(?:[A-Za-z0-9\\-._~!$&'()*+,;=]|%[0-9A-Fa-f]{2})*)(?::[0-9]*)?(?:/(?:[A-Za-z0-9\\-._~!$&'()*+,;=:@]|%[0-9A-Fa-f]{2})*)*|/(?:(?:[A-Za-z0-9\\-._~!$&'()*+,;=:@]|%[0-9A-Fa-f]{2})+(?:/(?:[A-Za-z0-9\\-._~!$&'()*+,;=:@]|%[0-9A-Fa-f]{2})*)*)?|(?:[A-Za-z0-9\\-._~!$&'()*+,;=@]|%[0-9A-Fa-f]{2})+(?:/(?:[A-Za-z0-9\\-._~!$&'()*+,;=:@]|%[0-9A-Fa-f]{2})*)*|)(?:\\?(?:[A-Za-z0-9\\-._~!$&'()*+,;=:@/?]|%[0-9A-Fa-f]{2})*)?(?:\\#(?:[A-Za-z0-9\\-._~!$&'()*+,;=:@/?]|%[0-9A-Fa-f]{2})*)?\"|href=\"\\b(([\\w-]+://?|www[.])[^\\s()<>]+(?:\\([\\w\\d]+\\)|([^[:punct:]\\s]|/)))\"");
            matcher= pattern.matcher(data);
            int i=0;
            String helperNormalization;
            String helpURL;
            URL helpCheckURL = new URL(siteURL);
            if(helpCheckURL.getPath() != "" && helpCheckURL.getPath() != "/")
                helpURL = siteURL.substring(0,siteURL.lastIndexOf("/"));
            else
                helpURL = siteURL;
            while (matcher.find()) {
                String dataToSend = matcher.group(0).replace("href=\"","");
                dataToSend = dataToSend.substring(0,dataToSend.length()-1);
                helperNormalization = URLNormalization.URLProcessing(helpURL,dataToSend);
                valuesForWrite.write(fileName + " " +helperNormalization+" "+dataToSend+"\n");
                URLs.add(helperNormalization);
                i++;
            }
            pattern = Pattern.compile("src=\"(?://(?:(?:[A-Za-z0-9\\-._~!$&'()*+,;=:]|%[0-9A-Fa-f]{2})*@)?(?:\\[(?:(?:(?:(?:[0-9A-Fa-f]{1,4}:){6}|::(?:[0-9A-Fa-f]{1,4}:){5}|(?:[0-9A-Fa-f]{1,4})?::(?:[0-9A-Fa-f]{1,4}:){4}|(?:(?:[0-9A-Fa-f]{1,4}:){0,1}[0-9A-Fa-f]{1,4})?::(?:[0-9A-Fa-f]{1,4}:){3}|(?:(?:[0-9A-Fa-f]{1,4}:){0,2}[0-9A-Fa-f]{1,4})?::(?:[0-9A-Fa-f]{1,4}:){2}|(?:(?:[0-9A-Fa-f]{1,4}:){0,3}[0-9A-Fa-f]{1,4})?::[0-9A-Fa-f]{1,4}:|(?:(?:[0-9A-Fa-f]{1,4}:){0,4}[0-9A-Fa-f]{1,4})?::)(?:[0-9A-Fa-f]{1,4}:[0-9A-Fa-f]{1,4}|(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))|(?:(?:[0-9A-Fa-f]{1,4}:){0,5}[0-9A-Fa-f]{1,4})?::[0-9A-Fa-f]{1,4}|(?:(?:[0-9A-Fa-f]{1,4}:){0,6}[0-9A-Fa-f]{1,4})?::)|[Vv][0-9A-Fa-f]+\\.[A-Za-z0-9\\-._~!$&'()*+,;=:]+)\\]|(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)|(?:[A-Za-z0-9\\-._~!$&'()*+,;=]|%[0-9A-Fa-f]{2})*)(?::[0-9]*)?(?:/(?:[A-Za-z0-9\\-._~!$&'()*+,;=:@]|%[0-9A-Fa-f]{2})*)*|/(?:(?:[A-Za-z0-9\\-._~!$&'()*+,;=:@]|%[0-9A-Fa-f]{2})+(?:/(?:[A-Za-z0-9\\-._~!$&'()*+,;=:@]|%[0-9A-Fa-f]{2})*)*)?|(?:[A-Za-z0-9\\-._~!$&'()*+,;=@]|%[0-9A-Fa-f]{2})+(?:/(?:[A-Za-z0-9\\-._~!$&'()*+,;=:@]|%[0-9A-Fa-f]{2})*)*|)(?:\\?(?:[A-Za-z0-9\\-._~!$&'()*+,;=:@/?]|%[0-9A-Fa-f]{2})*)?(?:\\#(?:[A-Za-z0-9\\-._~!$&'()*+,;=:@/?]|%[0-9A-Fa-f]{2})*)?\"|src=\"\\b(([\\w-]+://?|www[.])[^\\s()<>]+(?:\\([\\w\\d]+\\)|([^[:punct:]\\s]|/)))\"");
            matcher= pattern.matcher(data);
            while (matcher.find()) {
                String dataToSend = matcher.group(0).replace("src=\"","");
                dataToSend = dataToSend.substring(0,dataToSend.length()-1);
                helperNormalization = URLNormalization.URLProcessing(helpURL,dataToSend);
                valuesForWrite.write(fileName + " " +helperNormalization+" "+dataToSend+"\n");
                URLs.add(helperNormalization);
                i++;
            }
        }
        valuesForWrite.close();
        myReader.close();
        return URLs;
    }
    /**
     * Function responsible for writing URLs to html file from where they were read.
     *
     * @param fileName is a string that contains path to file
     * @param data is a string that contains siteURL
     * @return list of URLs
     */
    @Override
    protected void write(String fileName, String data) throws IOException {
        URL myURL = new URL(data);
        if(myURL.getPath() != null) {
            String parts[];
            String filesForHelp[];
            String pathToSite = "";
            String fileNameCopy=fileName;
            parts = fileName.split("/");
            String nameOfMyFile = parts[parts.length-1];
            String newFileContent = "";
            String oldValue="";
            File inFile = new File(this.fileName);
            Scanner myReaderValues = new Scanner(inFile);
            while (myReaderValues.hasNextLine()) {
                String row = myReaderValues.nextLine();
                filesForHelp = row.split(" ");
                if (data.equals(filesForHelp[1]) == true) {
                    pathToSite = filesForHelp[0];
                    oldValue = filesForHelp[2];
                }
            }
            if (pathToSite != "") {
                File f = new File(pathToSite);
                Matcher matcher;
                Pattern pattern;
                Scanner myReader = new Scanner(f);
                System.out.println(oldValue+"\n");
                while (myReader.hasNextLine()) {
                    String row = myReader.nextLine();
                    pattern = Pattern.compile("href=\"(?://(?:(?:[A-Za-z0-9\\-._~!$&'()*+,;=:]|%[0-9A-Fa-f]{2})*@)?(?:\\[(?:(?:(?:(?:[0-9A-Fa-f]{1,4}:){6}|::(?:[0-9A-Fa-f]{1,4}:){5}|(?:[0-9A-Fa-f]{1,4})?::(?:[0-9A-Fa-f]{1,4}:){4}|(?:(?:[0-9A-Fa-f]{1,4}:){0,1}[0-9A-Fa-f]{1,4})?::(?:[0-9A-Fa-f]{1,4}:){3}|(?:(?:[0-9A-Fa-f]{1,4}:){0,2}[0-9A-Fa-f]{1,4})?::(?:[0-9A-Fa-f]{1,4}:){2}|(?:(?:[0-9A-Fa-f]{1,4}:){0,3}[0-9A-Fa-f]{1,4})?::[0-9A-Fa-f]{1,4}:|(?:(?:[0-9A-Fa-f]{1,4}:){0,4}[0-9A-Fa-f]{1,4})?::)(?:[0-9A-Fa-f]{1,4}:[0-9A-Fa-f]{1,4}|(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))|(?:(?:[0-9A-Fa-f]{1,4}:){0,5}[0-9A-Fa-f]{1,4})?::[0-9A-Fa-f]{1,4}|(?:(?:[0-9A-Fa-f]{1,4}:){0,6}[0-9A-Fa-f]{1,4})?::)|[Vv][0-9A-Fa-f]+\\.[A-Za-z0-9\\-._~!$&'()*+,;=:]+)\\]|(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)|(?:[A-Za-z0-9\\-._~!$&'()*+,;=]|%[0-9A-Fa-f]{2})*)(?::[0-9]*)?(?:/(?:[A-Za-z0-9\\-._~!$&'()*+,;=:@]|%[0-9A-Fa-f]{2})*)*|/(?:(?:[A-Za-z0-9\\-._~!$&'()*+,;=:@]|%[0-9A-Fa-f]{2})+(?:/(?:[A-Za-z0-9\\-._~!$&'()*+,;=:@]|%[0-9A-Fa-f]{2})*)*)?|(?:[A-Za-z0-9\\-._~!$&'()*+,;=@]|%[0-9A-Fa-f]{2})+(?:/(?:[A-Za-z0-9\\-._~!$&'()*+,;=:@]|%[0-9A-Fa-f]{2})*)*|)(?:\\?(?:[A-Za-z0-9\\-._~!$&'()*+,;=:@/?]|%[0-9A-Fa-f]{2})*)?(?:\\#(?:[A-Za-z0-9\\-._~!$&'()*+,;=:@/?]|%[0-9A-Fa-f]{2})*)?\"|href=\"\\b(([\\w-]+://?|www[.])[^\\s()<>]+(?:\\([\\w\\d]+\\)|([^[:punct:]\\s]|/)))\"|src=\"\\b(([\\w-]+://?|www[.])[^\\s()<>]+(?:\\([\\w\\d]+\\)|([^[:punct:]\\s]|/)))\"|src=\"(?://(?:(?:[A-Za-z0-9\\-._~!$&'()*+,;=:]|%[0-9A-Fa-f]{2})*@)?(?:\\[(?:(?:(?:(?:[0-9A-Fa-f]{1,4}:){6}|::(?:[0-9A-Fa-f]{1,4}:){5}|(?:[0-9A-Fa-f]{1,4})?::(?:[0-9A-Fa-f]{1,4}:){4}|(?:(?:[0-9A-Fa-f]{1,4}:){0,1}[0-9A-Fa-f]{1,4})?::(?:[0-9A-Fa-f]{1,4}:){3}|(?:(?:[0-9A-Fa-f]{1,4}:){0,2}[0-9A-Fa-f]{1,4})?::(?:[0-9A-Fa-f]{1,4}:){2}|(?:(?:[0-9A-Fa-f]{1,4}:){0,3}[0-9A-Fa-f]{1,4})?::[0-9A-Fa-f]{1,4}:|(?:(?:[0-9A-Fa-f]{1,4}:){0,4}[0-9A-Fa-f]{1,4})?::)(?:[0-9A-Fa-f]{1,4}:[0-9A-Fa-f]{1,4}|(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))|(?:(?:[0-9A-Fa-f]{1,4}:){0,5}[0-9A-Fa-f]{1,4})?::[0-9A-Fa-f]{1,4}|(?:(?:[0-9A-Fa-f]{1,4}:){0,6}[0-9A-Fa-f]{1,4})?::)|[Vv][0-9A-Fa-f]+\\.[A-Za-z0-9\\-._~!$&'()*+,;=:]+)\\]|(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)|(?:[A-Za-z0-9\\-._~!$&'()*+,;=]|%[0-9A-Fa-f]{2})*)(?::[0-9]*)?(?:/(?:[A-Za-z0-9\\-._~!$&'()*+,;=:@]|%[0-9A-Fa-f]{2})*)*|/(?:(?:[A-Za-z0-9\\-._~!$&'()*+,;=:@]|%[0-9A-Fa-f]{2})+(?:/(?:[A-Za-z0-9\\-._~!$&'()*+,;=:@]|%[0-9A-Fa-f]{2})*)*)?|(?:[A-Za-z0-9\\-._~!$&'()*+,;=@]|%[0-9A-Fa-f]{2})+(?:/(?:[A-Za-z0-9\\-._~!$&'()*+,;=:@]|%[0-9A-Fa-f]{2})*)*|)(?:\\?(?:[A-Za-z0-9\\-._~!$&'()*+,;=:@/?]|%[0-9A-Fa-f]{2})*)?(?:\\#(?:[A-Za-z0-9\\-._~!$&'()*+,;=:@/?]|%[0-9A-Fa-f]{2})*)?\"");
                    matcher = pattern.matcher(row);
                    if (matcher.find()) {
                        if(matcher.group(0).equals("href=\""+oldValue+"\""))
                        {
                            row = row.replace(matcher.group(0), "href=\"" + fileNameCopy + "\"");
                            System.out.println("Aici e ok!\n");
                            synchronized (this)
                            {
                                LogManager.getLogger(LoggerType.FileLogger).log(Level.INFO,row + "was replaced!\n");
                            }
                        }
                    }
                    newFileContent = newFileContent + row + System.lineSeparator();
                }
                myReader.close();
                FileWriter writer = new FileWriter(pathToSite);
                writer.write(newFileContent);
                writer.close();
            }
        }
    }
    /**
     * Has no purpose in this class, is used only for override
     * @param pathToFile
     * @param word is a keyword
     * @return
     */
    @Override
    protected ArrayList<String> search(String pathToFile, String word) {
        return null;
    }

    /**
     * Has no purpose in this class, is used only for override
     * @param pathToFile
     * @param filter is a word by which we are filtering data from file
     * @return
     */
    @Override
    protected ArrayList<String> filter(String pathToFile, String filter) {
        return null;
    }

}