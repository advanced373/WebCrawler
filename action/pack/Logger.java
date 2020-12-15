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
import java.util.concurrent.BrokenBarrierException;

/**
 * This class has a management roll. It interprets the
 * parameters given as input in command line and based
 * on them it starts the right action. The
 * class catches all the errors thrown from program
 *
 * @author Vlijia Stefan
 */


public class Logger {
    private IAction actionObj;

    /**
     * Function testing if a given path is valid/exists
     *
     * @param path the path to be tested
     * @return true for valid path or false if not
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
     * @param option    specifies the type of action
     * @param attributes is an string array that contains
     *                  the arguments given in command line
     */

    public void newAction(String option, String[] attributes) throws IOException, CrawlerException {
        if (option.equals("Help")) {
            this.actionObj = new HelpAction("man/man.txt");
        }
        if (option.equals("Crawl")) {
            ArrayList<String> param = new ArrayList<>();

            if (attributes.length > 1) {
                if (attributes[1].equals("help")) {
                    this.actionObj = new HelpAction("man/crawlman.txt");
                    return;
                } else {
                    param.add(attributes[1]);
                }
            } else {
                param.add("no");
            }
            this.actionObj = new Crawl("", "file.conf", "seed.txt", param);
        }
        if (option.equals("Search")) {
            if (attributes.length > 1) {
                if (attributes[1].equals("help")) {
                    this.actionObj = new HelpAction("man/searchman.txt");
                    return;
                }
                if (attributes.length == 2)
                    throw new CrawlerException("210", "Missing arguments.\nPlease check man page with 'Search help' command");
                if (attributes.length == 3) {
                    File f = new File("C:\\root\\" + attributes[1]);
                    if (!f.exists()) {
                        throw new CrawlerException("201", "Invalid path.\nPlease enter a valid relative file path or check man page with 'Search help' command");
                    } else {
                        this.actionObj = new SearchAction("C:\\root", "C:\\root\\" + attributes[1], attributes[2]);
                    }
                }
            }
            throw new CrawlerException("210", "Missing arguments.\nPlease check man page with 'Search help' command");
        }
        if (option.equals("Sitemap")) {
            if (attributes.length > 1) {
                if (attributes[1].equals("help")) {
                    this.actionObj = new HelpAction("man/sitemapman.txt");
                    return;
                }
                if (!new File(attributes[1]).isAbsolute()) {
                    throw new CrawlerException("200", "Invalid path.\nPlease enter an absolute path");
                }
                this.actionObj = new SitemapAction(attributes[1]);
            } else {
                throw new CrawlerException("210", "Missing argument.\nPlease check man page with 'Sitemap help' command");
            }
        }
        if (option.equals("Filter")) {
            if (attributes.length > 1) {
                if (attributes[1].equals("help")) {
                    this.actionObj = new HelpAction("man/filterman.txt");
                    return;
                }
                if (attributes.length == 2)
                    throw new CrawlerException("210", "Missing arguments.\nPlease check man page with 'Filter help' command");
                if (attributes.length == 3) {
                    File f = new File("C:\\root\\" + attributes[1]);
                    if (!f.exists()) {
                        throw new CrawlerException("201", "Invalid path.\nPlease enter a valid relative file path or check man page with 'Filter help' command");
                    } else {
                        this.actionObj = new FilterAction("C:\\root", "C:\\root\\" + attributes[1], attributes[2]);
                    }
                } else if (Util.isNumeric(attributes[3])) {
                    this.actionObj = new FilterAction("C:\\root", "C:\\root\\" + attributes[1], attributes[2], Integer.parseInt(attributes[3]));
                } else
                    throw new CrawlerException("202", "Invalid size.\nPlease enter a valid resource size");
            } else {
                throw new CrawlerException("210", "Missing arguments.\nPlease check man page with 'Filter help' command");
            }

        }
    }


    public boolean runAction() throws IOException, BrokenBarrierException, InterruptedException {
        return actionObj.runAction();
    }
}