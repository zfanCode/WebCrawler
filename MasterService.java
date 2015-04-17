import java.util.*;
import java.io.*;
import java.net.*;

public class MasterService extends GeneralUser implements Protocol, Runnable
{
    private TreeSet<String> universe;
    private TreeSet<String> task;
    private boolean busy;
    private static ArrayList<MasterService> labor = new ArrayList<MasterService>();
    private static ArrayList<MasterService> service = new ArrayList<MasterService>();

    public MasterService(TreeSet<String> set, Socket socket, int n)
    {
        super(socket, n);
        this.universe = set;
        busy = false;
        task = new TreeSet<String> ();
    }

    private static int getSize()
    {
        return service.size();
    }
    
    public void brokenPipe()
    {
        int size = getSize();
        report("the size is " + size);
        int index = 0;
        if(size > 1)
        {
            for(; index < size; index++)
            {
                if(service.get(index).getNum() == getNum())
                    break;
            }
        }
        if(size > 0)
            service.remove(index);
        report("remove broken pipe... done");
    }

    public void boardcast(String command, int type)
    {
        int size = getSize();
	report("the thread size is " + size);
        for(int i = 0; i < size; i++)
        {
            service.get(i).sentData(type);
            service.get(i).sentMsg(command);
        }
    }

    private void receiveData()
    {
        int num = getData();
        for(int i = 0; i < num; i++)
        {
            String data = getMsg();
            if(!universe.contains(data))
            {
                universe.add(data);
                task.add(data);
            }
        }
    }

    private int findThread(int index)
    {
        int size = getSize();
        for(int i = 0; i < size; i++)
        {
            if(service.get(i).getNum() == index)
                return i;
        }
        return -1;
    }

    private void transfer(int targetId)
    {
        service.get(targetId).sentData(DONE);
        if(getData() == SLAVE)
        {
            int num = getData();  
            service.get(targetId).sentData(num);
            for(int i = 0; i < num; i++)
            {
                String data = getMsg();
                service.get(targetId).sentMsg(data);
            }
            if(getData() == OK)
                service.get(targetId).sentData(NOJOB);
        }

    }

    public void run()
    {
        boolean done = false;
        while(!done)
        {
            int command = getData();
            if(command == CONN)
            {
                report("new slave arrived...");
                service.add(this);
                int i = getSize() - 1;
                setNum(i);
                report("new slave" + getNum() + " add in the list");
            }

            else if(command == RANDOM)
            {
                boardcast("", RANDOM);
                receiveData();
            }

            else if(command == USER)
            {
                String uid = getMsg();
                report("uid is : " + uid);
                //we assume that the uid is exist already
                if(!new File("./download/" + uid).exists())
                {
                    if(new File("./download/" + uid).mkdirs())
                        report(uid + "'s home directory created...");
                    else
                        report("create directory failed");
                }
                boardcast(uid, USER);
            }

            else if(command == DONE)
            {
                report("receive command DONE");
                int index = getData();
                if(index != getNum())
                {
                    int threadid = findThread(index);
                    transfer(threadid);
                }
                else
                {
                    if(getData() == SLAVE)
                        receiveData();
                }
            }

            else if(command == CATEGORY)
            {

            }

            else if(command == SLAVE)
            {
                report("receive command slave");
                receiveData();
            }

            else if(command == OK)
            {
                report("receive command OK");
                if(!task.isEmpty())
                {
                    sentData(CLIENT);
                    sentMsg(task.pollFirst());
                }
            }

            else if(command == NOJOB)
            {
                report("receive command NOJOB");
                if(!task.isEmpty())
                {
                    sentData(JOB);
                    sentData(getNum());
                    sentMsg(task.pollFirst());
                }
            }

            else if(command == QUIT)
            {
                endProgram();   
            }

            else if(command == BROKEN)
            {
                brokenPipe();
                done = true;
            }
        }
    }
}
