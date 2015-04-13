import java.net.*;
import java.io.*;
import java.util.*;

public class ExtractedIdAcceptService implements Runnable{
    private TreeSet<Integer> universe;
    private Socket socket;
    private DataInputStream fromSlave;
    private DataOutputStream toSlave;

    public ExtractedIdAcceptService(TreeSet<Integer> universe, Socket socket ){
        this.socket  = socket;
        this.universe = universe;
        try {
            fromSlave = new DataInputStream(socket.getInputStream());
            toSlave = new DataOutputStream(socket.getOutputStream());
        } catch(IOException e) {
        }
    
    }
    @Override
    public void run(){
        int num;
        try {
            while(true){
                num = fromSlave.readInt();
                if (!universe.contains(num)) {
                   universe.add(num);
                   toSlave.writeInt(num);
                   toSlave.flush();
                }
            }
        } catch(IOException e) {
            System.out.println(e);
        }finally{
            try {
                socket.close();
            } catch(Exception e) {
            }
        }
    
    }
}
