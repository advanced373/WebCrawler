/*
 * SitemapAction
 *
 * Version 1.0
 *
 * All rights reserved.
 */

package action.pack;

/**
 * Class for generating website sitemap
 *
 * @author CorinaTanase
 */

public class SitemapAction extends InternAction{

    /**
     * absolute path of sitemap file
     */
    private String sitemapFile;

    /**
     * SitemapAction constructor
     *
     * @param filePath absolute path of file
     * @param sitemapFile  absolute path of sitemap file
     */
    public SitemapAction(String filePath,String sitemapFile) {
        super(filePath);
        this.sitemapFile=sitemapFile;
    }


    @Override
    public boolean runAction() {
        return false;
    }
}
