package action.pack;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class providing a wide range of utilities
 *
 * @author CorinaTanase
 */


public class Util {


    private static String getFileExtension(String file) throws MalformedURLException {
        URL url = new URL( file );
        String path = url.getPath();
        if (path.lastIndexOf( "." ) != -1 && path.lastIndexOf( "." ) != 0)
            return path.substring( path.lastIndexOf( "." ) + 1 );
        else return "";
    }

    public static boolean checkUrlExtension(ArrayList<String> allExtension, String url) {

        String extension = null;
        try {
            extension = getFileExtension( url );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if ("".equals( extension )) {
            for (String ext : allExtension) {
                if (ext.equals( extension ))
                    return true;
            }
        }else {
            for (String ext : allExtension) {
                if (ext.equals( extension ))
                    return true;
            }
        }
        return false;
    }

    public static boolean isValidDomain(String str)
    {

        String regex = "^((?!-)[A-Za-z0-9-]"
                + "{1,63}(?<!-)\\.)"
                + "+[A-Za-z]{2,6}";


        Pattern p = Pattern.compile(regex);
        if (str == null) {
            return false;
        }

        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static String trimUrl(String stringValue){
        if(stringValue.startsWith( "http://" )) {
            String str=stringValue.substring( 7 );
            str=str.replaceAll( "/+","/" );
            stringValue="http://"+str;
        }
        if(stringValue.startsWith( "https://" )) {
            String str=stringValue.substring( 8 );
            str=str.replaceAll( "/+","/" );
            stringValue="https://"+str;
        }
        else{
            String str=stringValue.replaceAll( "/+","/" );
            stringValue=str;
        }
        return stringValue;
    }
}
