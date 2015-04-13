import java.util.*;
import java.net.*;
import java.io.*;

public class Master implements Protocol{
    public static final int SEED = 22266015;
    public static void main (String[] args) {
        TreeSet<Integer> universe = new TreeSet<Integer>();
        LinkedList<Integer> readyToDownload = new LinkedList<Integer>();
        Socket socket = null;
        ServerSocket serversocket = null;
        try{
            serversocket = new ServerSocket(PORT);
        }catch(IOException e){
            System.out.println(e);
        }

        while(true){
            try {
                socket = serversocket.accept();
                DataOutputStream toSlave = new DataOutputStream(socket.getOutputStream());
                for (int i=0; i<MAX_THREADS;i++ ) {
                    toSlave.writeInt(SEED);
                }
                toSlave.flush();
                new Thread(new ExtractedIdAcceptService(universe,readyToDownload, socket)).start();
                System.out.println("Connected");

            } catch(IOException e) {
                System.out.println("Disconnected");

            }
        }
    }

}
