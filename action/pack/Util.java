package action.pack;

import javax.sound.midi.SysexMessage;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.*;

/**
 * Class providing a wide range of utilities
 *
 * @author CorinaTanase
 */


public class Util {
    
    private static String getFileExtension(String url) {
        if (url.lastIndexOf(".") != -1 && url.lastIndexOf(".") != 0)
            return url.substring(url.lastIndexOf(".") + 1);
        else return "";
    }

    public static boolean checkUrlExtension(ArrayList<String> allExtension, String url) {
        String extension = getFileExtension(url);
        if (extension != "") {
            for (String ext : allExtension)
                if (ext.equals(extension))
                    return true;
        }
        return false;
    }

}
