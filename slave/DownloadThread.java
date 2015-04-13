
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.io.*;
 
import javax.net.ssl.HttpsURLConnection;
 
public class DownloadThread implements Runnable{
	private static final String USER_AGENT = "Mozilla/5.0";
        private static final String parentURL = "http://www.zhihu.com/question/";
        private DataOutputStream toMaster;
        private int num;

        public DownloadThread(int num, DataOutputStream toMaster){
            this.toMaster = toMaster;
            this.num = num;
        }

        @Override
        public void run(){
            downloadByGet();
        }

	// HTTP GET request
	private void downloadByGet(){
                PrintWriter out = null;

                try {
                    out =  new PrintWriter(new File("./download/"+num+".html"));
 
		    String url = parentURL+num;
 
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

                
                TreeSet<Integer> idset = HTMLParser.getQuestionsIDs(num);
                try {
                    
                    for (int x :idset ) {
                        toMaster.writeInt(x);
                    }
                    toMaster.flush();
                } catch(IOException e) {
                }
 
	}
}
