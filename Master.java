import java.util.*;
import java.io.*;
import java.net.*;

public class Master implements Protocol, Runnable
{
    private Socket socket;
    private ServerSocket master;
    TreeSet<String> universe = new TreeSet<String>();

    public Master()
    {
        master = null;
    }

    private void report(String msg)
    {
        System.out.println(msg);
    }

    private void init()
    {
        try
        {
            socket = master.accept();
            report("[" + new Date() + "]" + " new SLAVE connect in ...");
        }
        catch(IOException e)
        {
            report(e.toString());
            e.printStackTrace(System.err);
        }
    }

    public void run()
    {
        try
        {
            master = new ServerSocket(PORT);
            report("[" + new Date() + "]" + " Master start ...");
            int n = 0;
            while(true)
            {
                n++;
                init();
                MasterService user = new MasterService(universe, socket, n);
                Thread thread = new Thread(user);
                thread.start();
            }
        }
        catch(IOException e)
        {
            report("connection error");
        }
    }
}
