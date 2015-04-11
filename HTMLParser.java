import java.util.*;
import java.util.regex.*;
import java.io.*;

public class HTMLParser {
    private static final String REGEX = "question/\\d+";
    private static final Pattern p = Pattern.compile(REGEX);

    public static TreeSet<Integer> getQuestionsIDs(int id){
        String fileName = id + ".html";

        String html = "";

        //get text
        Scanner in = null;
        try {
            in = new Scanner(new File(fileName));
        } catch(FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        if (in!=null) {
            while(in.hasNextLine()){
                html += in.nextLine();
            
            }
            in.close();
        }


 
        //extract numbers
        TreeSet<Integer> idSet = new TreeSet<Integer>();

        String matchedMsg = "";
        int number;
        Matcher m = p.matcher(html);

        while (m.find()){
            matchedMsg = m.group(0);
            number = Integer.parseInt(matchedMsg.substring("question/".length(),matchedMsg.length()));
            idSet.add(number);
        } 

        return idSet;
    }

}
