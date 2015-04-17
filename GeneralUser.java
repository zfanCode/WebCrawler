import java.io.*;
import java.net.*;

class GeneralUser implements Protocol
{
    private Socket socket;
    private DataInputStream get;
    private DataOutputStream sent;
    private int num;

    public GeneralUser(Socket s, int number)
    {
        this.socket = s;
        this.num = number;
        init();
    }

    private void init()
    {
        try
        {
            get = new DataInputStream(this.socket.getInputStream());
            sent = new DataOutputStream(this.socket.getOutputStream());
        }
        catch(IOException e)
        {
            e.printStackTrace(System.err);
        }
    }

    public static void report(String msg)
    {
        System.out.println(msg);
    }

    public DataOutputStream outport()
    {
        return this.sent;
    }

    public int getNum()
    {
        return this.num;
    }

    public void setNum(int n)
    {
        this.num = n;
    }

    public void sentData(int data)
    {
        try
        {
            sent.writeInt(data);
            sent.flush();
        }
        catch(IOException e)
        {
            e.printStackTrace(System.err);
        }
    }

    public int getData()
    {
        int result;
        try
        {
            result = get.readInt();   
        }
        catch(IOException e)
        {
            e.printStackTrace(System.err);
            report("catch broken pipe");
            result = BROKEN;
        }
        return result;
    }

    public String getMsg()
    {
        String result = "";
        try
        {
            result = get.readUTF();
        }
        catch(IOException e)
        {
            e.printStackTrace(System.err);
        }
        return result;
    }

    public void sentMsg(String msg)
    {
        try
        {
            sent.writeUTF(msg);
            sent.flush();
        }
        catch(IOException e)
        {
            e.printStackTrace(System.err);
        }
    }

    public void endProgram()
    {
        try
        {
            this.get.close();
            this.sent.close();
            this.socket.close();
        }
        catch(IOException e)
        {
            report(e.toString() + num);
            e.printStackTrace(System.err);
        }
    }
}
