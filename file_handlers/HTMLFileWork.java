package file_handlers;
import action.pack.URLNormalization;
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
     * @param bothValues is a string that contains both siteURL (which is  and path
     * @return list of URLs
     */
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
                URLs.add(helperNormalization);
                i++;
            }
            pattern = Pattern.compile("src=\"(?://(?:(?:[A-Za-z0-9\\-._~!$&'()*+,;=:]|%[0-9A-Fa-f]{2})*@)?(?:\\[(?:(?:(?:(?:[0-9A-Fa-f]{1,4}:){6}|::(?:[0-9A-Fa-f]{1,4}:){5}|(?:[0-9A-Fa-f]{1,4})?::(?:[0-9A-Fa-f]{1,4}:){4}|(?:(?:[0-9A-Fa-f]{1,4}:){0,1}[0-9A-Fa-f]{1,4})?::(?:[0-9A-Fa-f]{1,4}:){3}|(?:(?:[0-9A-Fa-f]{1,4}:){0,2}[0-9A-Fa-f]{1,4})?::(?:[0-9A-Fa-f]{1,4}:){2}|(?:(?:[0-9A-Fa-f]{1,4}:){0,3}[0-9A-Fa-f]{1,4})?::[0-9A-Fa-f]{1,4}:|(?:(?:[0-9A-Fa-f]{1,4}:){0,4}[0-9A-Fa-f]{1,4})?::)(?:[0-9A-Fa-f]{1,4}:[0-9A-Fa-f]{1,4}|(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))|(?:(?:[0-9A-Fa-f]{1,4}:){0,5}[0-9A-Fa-f]{1,4})?::[0-9A-Fa-f]{1,4}|(?:(?:[0-9A-Fa-f]{1,4}:){0,6}[0-9A-Fa-f]{1,4})?::)|[Vv][0-9A-Fa-f]+\\.[A-Za-z0-9\\-._~!$&'()*+,;=:]+)\\]|(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)|(?:[A-Za-z0-9\\-._~!$&'()*+,;=]|%[0-9A-Fa-f]{2})*)(?::[0-9]*)?(?:/(?:[A-Za-z0-9\\-._~!$&'()*+,;=:@]|%[0-9A-Fa-f]{2})*)*|/(?:(?:[A-Za-z0-9\\-._~!$&'()*+,;=:@]|%[0-9A-Fa-f]{2})+(?:/(?:[A-Za-z0-9\\-._~!$&'()*+,;=:@]|%[0-9A-Fa-f]{2})*)*)?|(?:[A-Za-z0-9\\-._~!$&'()*+,;=@]|%[0-9A-Fa-f]{2})+(?:/(?:[A-Za-z0-9\\-._~!$&'()*+,;=:@]|%[0-9A-Fa-f]{2})*)*|)(?:\\?(?:[A-Za-z0-9\\-._~!$&'()*+,;=:@/?]|%[0-9A-Fa-f]{2})*)?(?:\\#(?:[A-Za-z0-9\\-._~!$&'()*+,;=:@/?]|%[0-9A-Fa-f]{2})*)?\"|src=\"\\b(([\\w-]+://?|www[.])[^\\s()<>]+(?:\\([\\w\\d]+\\)|([^[:punct:]\\s]|/)))\"");
            matcher= pattern.matcher(data);
            while (matcher.find()) {
                String dataToSend = matcher.group(0).replace("src=\"","");
                dataToSend = dataToSend.substring(0,dataToSend.length()-1);
                helperNormalization = URLNormalization.URLProcessing(helpURL,dataToSend);
                URLs.add(helperNormalization);
                i++;
            }
        }
        myReader.close();
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