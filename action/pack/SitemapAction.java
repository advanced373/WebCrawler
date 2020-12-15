/*
 * SitemapAction
 *
 * Version 1.0
 *
 * All rights reserved.
 */

package action.pack;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

/**
 * Class for generating website sitemap
 *
 * @author CorinaTanase
 */

public class SitemapAction extends InternAction {

    /**
     * absolute path of sitemap file
     */
    private final String sitemapFile;
    /**
     * File object for current website
     */
    private final File currentFile;

    /**
     * SitemapAction constructor
     *
     * @param filePath absolute path of website's file
     */
    public SitemapAction(String filePath) {
        super(filePath);
        this.currentFile = new File(filePath);
        this.sitemapFile = "D:\\Sitemaps\\" + currentFile.getName() + ".txt";
    }

    /**
     * This method implements sitemap making action.
     *
     * @return true if the action was successful
     * false if the action failed
     */
    @Override
    public boolean runAction() throws IOException {
        File siteMap = new File(this.sitemapFile);
        if (siteMap.createNewFile()) {
            //System.out.println("File created: " + myObj.getName());
        } else {
            // System.out.println("File already exists.");
        }
        FileWriter myWriter = new FileWriter(siteMap);
        myWriter.write(currentFile.getName() + "\n");
        generateSitemap(currentFile, myWriter);
        myWriter.close();
        //de apelat logger
        return true;
    }

    /**
     * Method for recursively list files from a directory
     *
     * @param dir           file to generate sitemap for
     * @param sitemapWriter FileWriter object for writing in the sitemap file
     */
    public static void generateSitemap(File dir, FileWriter sitemapWriter) throws IOException {

        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                sitemapWriter.write("\t" + file.getName() + "/\n");
                generateSitemap(file, sitemapWriter);
            } else {
                sitemapWriter.write("\t\t" + file.getName() + "\n");
            }
        }
    }

}
