import java.io.*;
import java.util.concurrent.*;
import java.util.*;
import java.net.*;
public class Slave implements Runnable, Protocol {
    private ExecutorService pool;
    private DataInputStream fromMaster;
    private DataOutputStream toMaster;
    private static final int MAX_THREADS = 10;

    public Slave(Socket socket){
        pool = Executors.newFixedThreadPool(MAX_THREADS);
        try {
            fromMaster = new DataInputStream(socket.getInputStream());
            toMaster = new DataOutputStream(socket.getOutputStream());
        } catch(IOException e) {
        }
    }

    public static void main (String[] args) {
        try {
            Socket  socket = new Socket(IP, PORT);
            new Thread(new Slave(socket)).start();
        } catch(Exception e) {
        }
    }


    @Override
    public void run(){
        int next;
        try {
            while(true){
                try {
                    next = fromMaster.readInt();
                    System.out.println("Id accepted: " + next);
                    pool.execute(new DownloadThread(next,toMaster));
                } catch(InputMismatchException e) {
                    System.out.println("Wrong input");
                }
            }
        } catch(IOException e) {
        }
    }
}
