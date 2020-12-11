package action.pack;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.*;

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
}
