import java.io.*;
import java.net.*;
import java.util.*;

public class Server
{
    private int max;

    public Server(int max)
    {
        this.max = max;
        doService();
    }

    public void doService()
    {
        Master master = new Master();
        Thread server = new Thread(master);
        server.start();
    }

    public static void main(String[] args)
    {
        int max_num = 2;
        boolean flag = false;

        if(args.length == 0)
            flag = true;

        int i = 0;
        while(!flag && i < args.length)
        {
            if(args[i].equals("-num"))
            {
                i++;
                if(args.length == 1)
                {
                    System.out.println("Missing the argument of thread's number");
                    flag = true;
                }
                else
                    max_num = Integer.parseInt(args[i]);
            }
            i++;
        }
        new Server(max_num);
    }
}
