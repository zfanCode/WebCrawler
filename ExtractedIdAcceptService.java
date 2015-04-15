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

    private DataInputStream fromUniverseBk;
    private DataInputStream fromDownloadQueueBk;
    private DataOutputStream toUniverseBk;
    private DataOutputStream toDownloadQueueBk;
    private static final int BACKUP_FREQUENCY = 30;

    public ExtractedIdAcceptService(TreeSet<Integer> universe, LinkedList<Integer> readyToDownload, Socket socket, DataInputStream fromUniverseBk,DataInputStream fromDownloadQueueBk, DataOutputStream toUniverseBk, DataOutputStream toDownloadQueueBk ){
        this.socket  = socket;
        this.universe = universe;
        this.readyToDownload = readyToDownload;

        this.fromUniverseBk = fromUniverseBk;
        this.fromDownloadQueueBk = fromDownloadQueueBk;
        this.toUniverseBk = toUniverseBk;
        this.toDownloadQueueBk =  toDownloadQueueBk;

        count = getAmountDiff(universe, readyToDownload);

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

                    if (count % BACKUP_FREQUENCY == 0) {
                        System.out.println("backup ids");
                        backup();
                    }
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

    private static int getAmountDiff(Set<Integer> universe, LinkedList<Integer> downloadList){
        Set<Integer> result = new TreeSet<Integer>();
        result.addAll(universe);
        result.removeAll(downloadList);
        return result.size();
    }


    private void backup(){

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
