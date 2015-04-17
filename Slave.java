import java.io.*;
import java.util.concurrent.*;
import java.util.*;
import java.net.*;


public class Slave extends GeneralUser implements Protocol, Runnable
{
    private ExecutorService pool;
    private static final int MAX_THREAD = 2;
    private String commandline;

    public Slave(Socket socket)
    {
        super(socket, 0);
        pool = Executors.newFixedThreadPool(MAX_THREAD);
        commandline = "people/maverickz";
        sentData(CONN);
        sentData(USER);
        sentMsg(commandline);
    }

    public static void main(String[] args)
    {
        try
        {
            Socket socket = new Socket("localhost", PORT);
            new Thread(new Slave(socket)).start();
        }
        catch(IOException e)
        {
            System.out.println(e.toString());
        }
    }

    private void goScanner(String line, DataOutputStream out)
    {
        pool.execute(new DownloadThread(line, out));
    }

    public void run()
    {
        try
        {
            boolean done = false;
            while(!done)
            {
                int command = getData();
                if(command == USER)
                {
                    report("receive the user command");
                    String name = getMsg();
                
                    report("searching information about " + name);
                    goScanner(name, outport());
                }

                else if(command == CLIENT)
                {
                    report("receive command CLIENT");
                    String data = getMsg();
                    goScanner(data, outport());
                }

                else if(command == JOB)
                {
                    report("receive command JOB");
                    int threadid = getData();
                    String data = getMsg();
                    sentData(DONE);
                    sentData(threadid);
                    goScanner(data, outport());
                }
                else if(command == BROKEN)
                {
                    done = true;
                }
            }
        }
        catch(Exception e)
        {
            
        }
    }
}
