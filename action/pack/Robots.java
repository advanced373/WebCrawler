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
     * Member decription
     */

    private static final String DISALLOW = "Disallow";

    /**
     * Function responsible for verify if an given URL is allowed to be
     * download by crawler or not
     *
     * @param url is the URL to be verified
     * @return true if is allowed or false if is disallowed
     *         to download the page. Also return false if
     *         url address is malformed or if errors occur
     *         while reading from the robots.txt file
     */

    public static boolean robotParser(URL url)
    {
        String baseAddress = url.getHost();
        String strRobotURL = null;

        if(url.getProtocol().equals("http"))
        {
            strRobotURL = "http://" + baseAddress + "/robots.txt";
        }
        else if (url.getProtocol().equals("https"))
        {
            strRobotURL = "https://" + baseAddress + "/robots.txt";
        }

        URL robotTxtURL;
        try
        {
            robotTxtURL = new URL(strRobotURL);
        }
        catch (MalformedURLException e)
        {
            return false;
        }

        String strCommands;
        try
        {
            InputStream urlRobotStream = robotTxtURL.openStream();
            byte b[] = new byte[1000];
            int readNr = urlRobotStream.read(b);
            strCommands = new String(b, 0, readNr);

            while (readNr != -1)
            {
                readNr = urlRobotStream.read(b);
                if (readNr != -1)
                {
                    String newCommands = new String(b, 0, readNr);
                    strCommands += newCommands;
                }
            }
            urlRobotStream.close();
        }
        catch (IOException e)
        {
            return true;
        }

        if (strCommands.contains(DISALLOW))
        {
            String[] split = strCommands.split("\n");

            String userAgent = null;
            for (int i = 0; i < split.length; i++)
            {
                String line = split[i].trim();
                if (line.toLowerCase().startsWith("user-agent"))
                {
                    int start = line.indexOf(":") + 1;
                    int end   = line.length();
                    userAgent = line.substring(start, end).trim();
                }
                else if (line.startsWith(DISALLOW))
                {
                    if (userAgent != null && userAgent.equals("*"))
                    {
                        int start = line.indexOf(":") + 1;
                        int end   = line.length();
                        String rule = line.substring(start, end).trim();

                        String regex = "(http|https):\\/\\/" + baseAddress;

                        for(int j = 0; j < rule.length(); j++)
                        {
                            if(rule.charAt(j) == '/')
                            {
                                regex += "\\/";
                            }
                            else if (rule.charAt(j) == '*')
                            {
                                regex += ".*";
                            }
                            else
                            {
                                regex += rule.charAt(j);
                            }
                        }

                        String strURL = url.toString();
                        System.out.println(strURL);

                        Pattern pat = Pattern.compile(regex);
                        Matcher matcher = pat.matcher(strURL);

                        if(matcher.matches())
                        {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
