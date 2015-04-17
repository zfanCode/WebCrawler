
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.io.*;
 
import javax.net.ssl.HttpsURLConnection;
 
public class DownloadThread implements Runnable, Protocol
{
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String parentURL = "http://www.zhihu.com/";
    private DataOutputStream toMaster;
    private String doc;

    public DownloadThread(String doc, DataOutputStream toMaster)
    {
        this.toMaster = toMaster;
        this.doc = doc;
        
        //make dir
        String dir = doc.substring(0, doc.lastIndexOf("/"));
        System.out.println("dir is " + dir);
        if(new File("./download/" + dir).mkdirs())
            System.out.println("dir ./download/" + dir + " established");
    }

    @Override
    public void run()
    {
        downloadByGet();
    }

    // HTTP GET request
    private void downloadByGet()
    {
        PrintWriter out = null;

        System.out.println("./download/"+ doc +".html");
        try 
        {
            out =  new PrintWriter(new File("./download/"+ doc +".html"));

            String url = parentURL+doc;
            System.out.println(url);
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) 
            {
                response.append(inputLine);
            }
            in.close();

            //print result
            out.print(response.toString());

        }
        catch(FileNotFoundException e) 
        {
            System.err.println("File not found");
        }
        catch(IOException e) 
        {
            System.err.println(e.getMessage());
        }
        finally
        {
            out.close();
        }
        AnalysisProgress(doc);
    }

    private void AnalysisProgress(String link)
    {
        TreeSet<String> linkset = HTMLParser.getQuestionsIDs(link);
        try 
        {
            toMaster.writeInt(SLAVE);
            toMaster.writeInt(linkset.size());
            for (String links : linkset) 
                toMaster.writeUTF(links);
            toMaster.writeInt(OK);
            toMaster.flush();
        }
        catch(IOException e) 
        {
        }
    }
}
