import java.util.*;
import java.net.*;
import java.io.*;

public class Master implements Protocol{
    public static final int SEED = 22266015;
    public static void main (String[] args) {
        TreeSet<Integer> universe = new TreeSet<Integer>();
        LinkedList<Integer> readyToDownload = new LinkedList<Integer>();
        Socket socket = null;
        try{
            ServerSocket serversocket = new ServerSocket(PORT);
            socket = serversocket.accept();
        }catch(IOException e){
            System.out.println(e);
        }

        try {
            DataOutputStream toSlave = new DataOutputStream(socket.getOutputStream());
            for (int i=0; i<MAX_THREADS;i++ ) {
                toSlave.writeInt(SEED);
            }
            toSlave.flush();
        } catch(Exception e) {
        }

        new Thread(new ExtractedIdAcceptService(universe,readyToDownload, socket)).start();

        System.out.println("Connected");
    }

}
