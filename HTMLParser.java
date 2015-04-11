import java.util.*;
import java.io.*;

public class HTMLParser {
    private String html;

    public HTMLParser(String fileName){
        html = "";
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
    }

    public static void main (String[] args) {
        HTMLParser parser = new HTMLParser("index.html");
        //parser.print();
        parser.getQuestionsIDs();
    }
    
    public int[] getQuestionsIDs(){
        TreeSet<Integer> idSet = new TreeSet<Integer>();
        int foundIndex;
        int startIndex;
        int endIndex = 0;
        Pattern p = Pattern.compile("href=\"/question/");
        String endMark = "";

        while((endIndex)!=-1){
             startIndex = html.indexOf(target, endIndex);
             endIndex = html.indexOf(endMark, startIndex);

             if (startIndex != -1 && endIndex != -1) {
                String number =  html.substring(startIndex+target.length(), endIndex);
                System.out.println(number);
             
             }

        }


        return null;
        
    
    }




    public void print(){
        System.out.println(html);
    
    }

    @Override
    public String toString(){
        return html;
    }

}
