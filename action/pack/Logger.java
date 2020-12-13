/*
 * Logger
 *
 * Version 1.0
 *
 * All rights reserved.
 */

package action.pack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;


public class Logger {
    private IAction actionObj;

    /**
     * Function that test if a given path is valid/exists
     *
     * @param path the path to be tested
     * @return true if is valid or false if not
     */

    private static boolean isValidPath(String path) {
        try {

            Paths.get(path);
        } catch (InvalidPathException e) {
            return false;
        }
        return true;
    }

    /**
     * Function that creates and starts a new action
     *
     * @param option    specify the type of action
     * @param atributes is an string array that contain
     *                  the arguments given in command line
     */

    public void newAction(String option, String[] atributes) throws FileNotFoundException, CrawlerException {
        if (option.equals("Help")) {
            this.actionObj = new HelpAction("man/man.txt");
        }
        if (option.equals("Crawl")) {
            ArrayList<String> param = new ArrayList<>();

            if (atributes.length > 1) {
                if (atributes[1].equals("help")) {
                    this.actionObj = new HelpAction("man/crawlman.txt");
                    return;
                } else {
                    param.add(atributes[1]);
                }
            } else {
                param.add("no");
            }
            this.actionObj = new Crawl("", "file.conf", "seed.txt", param);
        }
        if (option.equals("Search")) {
            if (atributes.length > 1) {
                if (atributes[1].equals("help")) {
                    this.actionObj = new HelpAction("man/searchman.txt");
                    return;
                }
                if (atributes.length == 2)
                    throw new CrawlerException("210", "Missing arguments.\nPlease check man page with 'Search help' command");
                if (atributes.length == 3) {
                    File f = new File("C:\\root\\" + atributes[1]);
                    if (!f.exists()) {
                        throw new CrawlerException("201", "Invalid path.\nPlease enter a valid relative file path or check man page with 'Search help' command");
                    } else {
                        this.actionObj = new SearchAction("C:\\root", "C:\\root\\" + atributes[1], atributes[2]);
                    }
                }
            }
            throw new CrawlerException("210", "Missing arguments.\nPlease check man page with 'Search help' command");
        }
        if (option.equals("Sitemap")) {
            if (atributes.length > 1) {
                if (atributes[1].equals("help")) {
                    this.actionObj = new HelpAction("man/sitemapman.txt");
                    return;
                }
                if (!new File(atributes[1]).isAbsolute()) {
                    throw new CrawlerException("200", "Invalid path.\nPlease enter an absolute path");
                }
                this.actionObj = new SitemapAction(atributes[1]);
            } else {
                throw new CrawlerException("210", "Missing argument.\nPlease check man page with 'Sitemap help' command");
            }
        }
        if (option.equals("Filter")) {
            if (atributes.length > 1) {
                if (atributes[1].equals("help")) {
                    this.actionObj = new HelpAction("man/filterman.txt");
                    return;
                }
                if (atributes.length == 2)
                    throw new CrawlerException("210", "Missing arguments.\nPlease check man page with 'Filter help' command");
                if (atributes.length == 3) {
                    File f = new File("C:\\root\\" + atributes[1]);
                    if (!f.exists()) {
                        throw new CrawlerException("201", "Invalid path.\nPlease enter a valid relative file path or check man page with 'Filter help' command");
                    } else {
                        this.actionObj = new FilterAction("C:\\root", "C:\\root\\" + atributes[1], atributes[2]);
                    }
                } else if (Util.isNumeric(atributes[3])) {
                    this.actionObj = new FilterAction("C:\\root", "C:\\root\\" + atributes[1], atributes[2], Integer.parseInt(atributes[3]));
                } else
                    throw new CrawlerException("202", "Invalid size.\nPlease enter a valid resource size");
            } else {
                throw new CrawlerException("210", "Missing arguments.\nPlease check man page with 'Filter help' command");
            }

        }
    }


    public boolean runAction() throws IOException {
        return actionObj.runAction();
    }
}
