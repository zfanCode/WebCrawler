
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.io.*;
 
import javax.net.ssl.HttpsURLConnection;
 
public class Downloader {
	private static final String USER_AGENT = "Mozilla/5.0";
        private static final String parentURL = "http://www.zhihu.com/question/";
	// HTTP GET request
	public static void downloadByGet(int id){
                PrintWriter out = null;

                try {
                    out =  new PrintWriter(new File("./download/"+id+".html"));
 
		    String url = parentURL+id;
 
		    URL obj = new URL(url);
		    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		    // optional default is GET
		    con.setRequestMethod("GET");
 
		    //add request header
		    con.setRequestProperty("User-Agent", USER_AGENT);
 
		    int responseCode = con.getResponseCode();
		    System.out.println("\nSending 'GET' request to URL : " + url);
 
		    BufferedReader in = new BufferedReader(
		            new InputStreamReader(con.getInputStream()));
		    String inputLine;
		    StringBuffer response = new StringBuffer();
 
		    while ((inputLine = in.readLine()) != null) {
		    	response.append(inputLine);
		    }
		    in.close();
 
		    //print result
		    out.print(response.toString());

                }catch(FileNotFoundException e) {
                    System.err.println("File not found");
                }catch(IOException e) {
                    System.err.println(e.getMessage());
                }finally{
                    out.close();
                }
 
	}
}
