package action.pack;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.*;

/**
 * Class providing a wide range of utilities
 * @author CorinaTanase
 */


public class Util {

    /**
     * Function implementing user-defined processing of URLs
     * - Concatenate relative resource to main URL
     */

    public static String URLProcessing(String url, String resource) throws MalformedURLException {

        String finalUrl;
        System.out.println( url + " " + resource + "\n" );
        finalUrl = Util.normalize( url );

        if (resource.startsWith( "./" ))
            if (finalUrl.endsWith( "/" ))
                finalUrl = finalUrl + resource.substring( 2 );
            else
                finalUrl = finalUrl + resource.substring( 1 );


        if (resource.startsWith( "../" ))
            if (finalUrl.endsWith( "/" )) {
                finalUrl = finalUrl.substring( 0, finalUrl.length() - 1 );
                finalUrl = finalUrl.substring( 0, finalUrl.lastIndexOf( "/" ) ) + resource.substring( 2 );
            } else {
                finalUrl = finalUrl.substring( 0, finalUrl.lastIndexOf( "/" ) ) + resource.substring( 2 );
            }

        return finalUrl;
    }

    /**
     * Function implementing normalization of URLs
     * - Covert the scheme and host to lowercase (done by java.net.URL)
     * - Normalize the path (done by java.net.URI)
     * - Remove the fragment (the part after the #)
     * - Remove some query string params like "utm_*" and "*session*"
     */
    public static String normalize(final String taintedURL) throws MalformedURLException {

        final URL url;
        try {
            url = new URI( taintedURL ).normalize().toURL();
        } catch (URISyntaxException e) {
            throw new MalformedURLException( e.getMessage() );
        }

        final String path = url.getPath().replace( "/$", "" );
        final SortedMap<String, String> params = createParameterMap( url.getQuery() );
        final int port = url.getPort();
        final String queryString;

        if (params != null) {
            // Some params are only relevant for user tracking, so remove the most commons ones.
            for (Iterator<String> i = params.keySet().iterator(); i.hasNext(); ) {
                final String key = i.next();
                if (key.startsWith( "utm_" ) || key.contains( "session" )) {
                    i.remove();
                }
            }
            queryString = "?" + canonicalize( params );
        } else {
            queryString = "";
        }

        return url.getProtocol() + "://" + url.getHost()
                + (port != -1 && port != 80 ? ":" + port : "")
                + path + queryString;
    }


    private static SortedMap<String, String> createParameterMap(final String queryString) {
        if (queryString == null || queryString.isEmpty()) {
            return null;
        }

        final String[] pairs = queryString.split( "&" );
        final Map<String, String> params = new HashMap<String, String>( pairs.length );

        for (final String pair : pairs) {
            if (pair.length() < 1) {
                continue;
            }

            String[] tokens = pair.split( "=", 2 );
            for (int j = 0; j < tokens.length; j++) {
                try {
                    tokens[j] = URLDecoder.decode( tokens[j], "UTF-8" );
                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                }
            }
            switch (tokens.length) {
                case 1: {
                    if (pair.charAt( 0 ) == '=') {
                        params.put( "", tokens[0] );
                    } else {
                        params.put( tokens[0], "" );
                    }
                    break;
                }
                case 2: {
                    params.put( tokens[0], tokens[1] );
                    break;
                }
            }
        }

        return new TreeMap<String, String>( params );
    }

    private static String canonicalize(final SortedMap<String, String> sortedParamMap) {
        if (sortedParamMap == null || sortedParamMap.isEmpty()) {
            return "";
        }

        final StringBuffer sb = new StringBuffer( 350 );
        final Iterator<Map.Entry<String, String>> iter = sortedParamMap.entrySet().iterator();

        while (iter.hasNext()) {
            final Map.Entry<String, String> pair = iter.next();
            sb.append( percentEncodeRfc3986( pair.getKey() ) );
            sb.append( '=' );
            sb.append( percentEncodeRfc3986( pair.getValue() ) );
            if (iter.hasNext()) {
                sb.append( '&' );
            }
        }

        return sb.toString();
    }

    private static String percentEncodeRfc3986(final String string) {
        try {
            return URLEncoder.encode( string, "UTF-8" ).replace( "+", "%20" ).replace( "*", "%2A" ).replace( "%7E", "~" );
        } catch (UnsupportedEncodingException e) {
            return string;
        }
    }

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
        if ("".equals( extension ))
            for (String ext : allExtension) {
                if (ext.equals( extension ))
                    return true;
            }
        return false;
    }
}
