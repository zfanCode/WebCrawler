import java.net.*;
import java.io.*;
import java.util.*;

public class ExtractedIdAcceptService implements Runnable,Protocol{
    private TreeSet<Integer> universe;
    private LinkedList<Integer> readyToDownload;
    private Socket socket;
    private DataInputStream fromSlave;
    private DataOutputStream toSlave;
    private static int count = 0;


    public ExtractedIdAcceptService(TreeSet<Integer> universe, LinkedList<Integer> readyToDownload, Socket socket ){
        this.socket  = socket;
        this.universe = universe;
        this.readyToDownload = readyToDownload;
        try {
            fromSlave = new DataInputStream(socket.getInputStream());
            toSlave = new DataOutputStream(socket.getOutputStream());
        } catch(IOException e) {
        }
    
    }
    @Override
    public void run(){
        int num;
        int numOfIds;
        int cmd;
        try {
            while(true){
                cmd = fromSlave.readInt();
                if (cmd == ID_REQUEST) {
                    //get ids from client 
                    numOfIds = fromSlave.readInt();
                    for (int i= 0; i< numOfIds; i++) {
                        num = fromSlave.readInt();
                        if (!universe.contains(num)) {
                           universe.add(num);
                           readyToDownload.add(num);
                        }
                    }

                    //gives client one id
                    toSlave.writeInt(readyToDownload.pollFirst());
                    toSlave.flush();

                    count++;
                    System.err.println("Total downloaded: "+count);
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
