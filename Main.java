import action.pack.Crawl;
import action.pack.IAction;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.EnumSet;

class Main {
    static enum Options
    {
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



    public static void main(String[] args)
    {

        /*//de adaugat try - catch dupa ce sunt facute clasele de tratare a exceptiilor
        if(args.length < 1)
        {
            //nr gresit de parametrii
        }
        if(!contains(Options.class, args[0]))
        {
            //optiune inexistenta.
        }

        Logger loggerObj = new Logger();
        loggerObj.newAction(args[0], args);
        if(!loggerObj.runAction())
        {
            //rularea actiunii a esuat
        }*/

        ArrayList<String> param=new ArrayList<>();
        param.add("no");



            long startTime = System.currentTimeMillis();
            try {
                IAction action = new Crawl( "", "file.conf", "seed.txt", param );
                action.runAction();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;

            System.out.println( "URL's crawled : " + " in " + totalTime + " i: " );


    }
}
