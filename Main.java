import action.pack.*;
import action.pack.IAction;
import crawler_log.LogManager;
import crawler_log.LoggerType;
import file_handlers.FileWorker;
import action.pack.Crawl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.concurrent.BrokenBarrierException;
import java.util.logging.Level;

class Main {
    static enum Options {
        List("-list"),
        Crawl("-crawl"),
        Search("-search"),
        Filter("-filter"),
        Help("-help"),
        Sitemap("-sitemap");

        Options(String opt) {
        }
    }

    public static <E extends Enum<E>> boolean contains(Class<E> _enumClass,
                                                       String value) {
        try {
            return EnumSet.allOf(_enumClass)
                    .contains(Enum.valueOf(_enumClass, value));
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) throws IOException {
        try {
            if (args.length < 1) {
                throw new CrawlerException("101", "Missing parameters");
            }
            if (!contains(Options.class, args[0])) {
                throw new CrawlerException("102", "Invalid option");
            }
            Logger loggerObj = new Logger();
            loggerObj.newAction(args[0], args);
            if (!loggerObj.runAction()) {
                throw new CrawlerException("103", "Bad action");
            }
        } catch (CrawlerException | IOException | BrokenBarrierException | InterruptedException e) {
            LogManager.getMyLogger(LoggerType.FileLogger).log(Level.SEVERE,e.getMessage());
        }
    }
}
