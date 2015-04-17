import java.util.*;
import java.util.regex.*;
import java.io.*;

public class HTMLParser 
{
    //private static final String REGEX = "question_link\" href=\"/([^\"]*)\"";
    private static final String REGEX = "href=\"/(question/[^\"]*)\"";
    private static final String PAGE = "page=(\\d+)";
    private static final Pattern p = Pattern.compile(REGEX);
    private static final Pattern page = Pattern.compile(PAGE);
    private static int num = 0;
    private static String log = "";

    public static TreeSet<String> getQuestionsIDs(String link)
    {
        TreeSet<String> linkSet = new TreeSet<String>();
        try
        {
        
            PrintWriter out = new PrintWriter(new File("./download/log.txt"));
            String fileName = "./download/"+link + ".html";
            String html = readFile(fileName);

            if(link.contains("people"))
            {
                if(link.indexOf("/") == link.lastIndexOf("/"))
                    linkSet = extractPeopleHomePage(link, linkSet);
                else 
                {
                    matchPages(html);
                    report("****** the linke is " + link);
                    linkSet = addPages(link, linkSet);
                    linkSet = extractQuestion(html, linkSet);
                }
            }
            else if(!link.contains("question"))
            {
                matchPages(html);
                linkSet = extractQuestion(html, linkSet);
            }
            out.print(log);
            out.close();
        }
        catch(Exception e)
        {
            e.printStackTrace(System.err);
        }
        return linkSet;
    }

    private static void report(String msg)
    {
        log += msg + "\n";
        System.out.println(msg);
    }

    private static TreeSet<String> extractQuestion(String html, TreeSet<String> linkSet)
    {
        //extract numbers
        Matcher m = p.matcher(html);

        while(m.find())
        {
            report("match the link " + m.group(1));
            linkSet.add(m.group(1));
        }
        return linkSet;
    }

    private static void matchPages(String html)
    {
        Matcher n = page.matcher(html);
        while(n.find())
        {
            int pages = Integer.parseInt(n.group(1));
            if(pages > num)
                num = pages;
            report("the page num is " + num);
        }
    }

    private static TreeSet<String> addPages(String link, TreeSet<String> linkSet)
    {
        report("link is " + link);
        for(int i = 2; i <= num; i++)
        {
            if(link.contains("?page="))
            {
                link = link.substring(0, link.indexOf("?page="));
            }
            linkSet.add(link + "?page=" + i);
            report("the address is " + link + "?page=" + i);
        }
        return linkSet;
    }

    private static TreeSet<String> extractPeopleHomePage(String link, TreeSet<String> linkSet)
    {
        String name = "";
        name = link.substring(link.indexOf("/") + 1);
        report("the name is " + name);
        if(!new File("./download/people/" + name + "/asks").exists())
        {
            if(new File("./download/people/" + name + "/asks").mkdirs())
            {
                report(name + "'s home directory asks created...");
                if(new File("./download/people/" + name + "/answers").mkdirs())
                {
                    report(name + "'s home directory answers created...");
                    if(new File("./download/people/" + name + "/posts").mkdirs())
                        report(name + "'s home directory posts created...");
                }
            }
        }
        else
            report("create directory failed");
        //linkSet.add("people/" + name + "/asks");
        linkSet.add("people/" + name + "/answers");
        return linkSet;
    }

    private static String readFile(String file)
    {
        String html = "";
        num = 0;

        //get text
        Scanner in = null;
        try 
        {
            in = new Scanner(new File(file));
        } 
        catch(FileNotFoundException e) 
        {
            System.out.println(e.getMessage());
        }
        if (in!=null) 
        {
            while(in.hasNextLine())
            {
                html += in.nextLine();
            }
            in.close();

        }
        return html;
    }

}
