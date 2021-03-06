/*
 * Robots
 *
 * Version 1.0
 *
 * All rights reserved.
 */

package action.pack;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class implementing work with robots.txt file
 *
 * @author Vlijia Stefan
 */

public class Robots {

    /**
     * Member description
     */

    private static final String DISALLOW = "Disallow";

    /**
     * Function responsible for verify if an given URL is allowed to be
     * downloaded by the crawler or not
     *
     * @param url the URL to be verified
     * @return true if is allowed or false if is disallowed
     * to download the page. Also return false if the
     * url address is malformed or if errors occur
     * while reading from the robots.txt file
     */

    public static boolean robotParser(URL url) throws IOException {
        String baseAddress = url.getHost();
        String strRobotURL = null;

        if (url.getProtocol().equals("http")) {
            strRobotURL = "http://" + baseAddress + "/robots.txt";
        } else if (url.getProtocol().equals("https")) {
            strRobotURL = "https://" + baseAddress + "/robots.txt";
        }

        URL robotTxtURL;
        robotTxtURL = new URL(strRobotURL);


        String strCommands;

        HttpURLConnection connection = (HttpURLConnection) robotTxtURL.openConnection();
        connection.setConnectTimeout(2000);

        if (connection.getResponseCode() < 200 || 226 < connection.getResponseCode()) {
            return true;
        }
        InputStream urlRobotStream = robotTxtURL.openStream();
        byte b[] = new byte[1000];
        int readNr = urlRobotStream.read(b);
        strCommands = new String(b, 0, readNr);

        while (readNr != -1) {
            readNr = urlRobotStream.read(b);
            if (readNr != -1) {
                String newCommands = new String(b, 0, readNr);
                strCommands += newCommands;
            }
        }
        urlRobotStream.close();

        if (strCommands.contains(DISALLOW)) {
            String[] split = strCommands.split("\n");

            String userAgent = null;
            for (int i = 0; i < split.length; i++) {
                String line = split[i].trim();
                if (line.toLowerCase().startsWith("user-agent")) {
                    int start = line.indexOf(":") + 1;
                    int end = line.length();
                    userAgent = line.substring(start, end).trim();
                } else if (line.startsWith(DISALLOW)) {
                    if (userAgent != null && userAgent.equals("*")) {
                        int start = line.indexOf(":") + 1;
                        int end = line.length();
                        String rule = line.substring(start, end).trim();

                        String regex = "(http|https):\\/\\/" + baseAddress;

                        for (int j = 0; j < rule.length(); j++) {
                            if (rule.charAt(j) == '/') {
                                regex += "\\/";
                            } else if (rule.charAt(j) == '*') {
                                regex += ".*";
                            } else {
                                regex += rule.charAt(j);
                            }
                        }

                        String strURL = url.toString();


                        Pattern pat = Pattern.compile(regex);
                        Matcher matcher = pat.matcher(strURL);

                        if (matcher.matches()) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
