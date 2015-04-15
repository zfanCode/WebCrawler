import java.util.*;
import java.net.*;
import java.io.*;

public class Master implements Protocol{
    public static final int SEED = 22266015;
    public static void main (String[] args)  throws FileNotFoundException{
        TreeSet<Integer> universe = new TreeSet<Integer>();
        LinkedList<Integer> readyToDownload = new LinkedList<Integer>();
        Socket socket = null;
        ServerSocket serversocket = null;
        String UNIVERSE_BACKUP = "./data/IdUniverse.dat";
        String READY_TO_DOWNLOAD_BACKUP = "./data/IdToDownload.dat";

        DataInputStream fromUniverseBk = new DataInputStream(new FileInputStream(UNIVERSE_BACKUP));
        DataInputStream fromDownloadQueueBk = new DataInputStream(new FileInputStream(READY_TO_DOWNLOAD_BACKUP));

        init(universe, readyToDownload, fromUniverseBk,fromDownloadQueueBk);

        DataOutputStream toUniverseBk = new DataOutputStream(new FileOutputStream(UNIVERSE_BACKUP));
        DataOutputStream toDownloadQueueBk = new DataOutputStream(new FileOutputStream(READY_TO_DOWNLOAD_BACKUP));

        backup(universe, readyToDownload, toUniverseBk,toDownloadQueueBk);

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
                new Thread(new ExtractedIdAcceptService(universe,readyToDownload, socket, fromUniverseBk, fromDownloadQueueBk, toUniverseBk, toDownloadQueueBk)).start();
                System.out.println("Connected");

            } catch(IOException e) {
                System.out.println("Disconnected");

            }
        }
    }

    private static void init(TreeSet<Integer> universe, LinkedList<Integer> readyToDownload,DataInputStream fromUniverseBk, DataInputStream fromDownloadQueueBk){
        int tmp;
        try {
            while(true){
                universe.add(fromUniverseBk.readInt());
            
            }
        }catch(Exception e){
            System.err.println(e);
        }
        try {
            while(true){
                readyToDownload.add(fromDownloadQueueBk.readInt());
            }
        } catch(Exception e) {
            System.err.println(e);
        }

        System.out.println("Finish initialization");

    
    }

    private static void backup(TreeSet<Integer> universe, LinkedList<Integer> readyToDownload, DataOutputStream toUniverseBk, DataOutputStream toDownloadQueueBk){

        TreeSet<Integer> uni = new TreeSet<Integer>(universe);
        LinkedList<Integer> download =new LinkedList<Integer>(readyToDownload);
        try{
            int tmp;
            for (Integer  x:uni ) {
                toUniverseBk.writeInt(x);
            }
            toUniverseBk.flush();


            for (Integer  x:download ) {
                toDownloadQueueBk.writeInt(x);
            }
            toDownloadQueueBk.flush();    
        }catch(IOException e){
            System.out.println("Cannot backup");
            System.out.println(e.getMessage());
        }
 
    }


}
