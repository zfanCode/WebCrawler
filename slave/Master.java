import java.util.*;
import java.net.*;
import java.io.*;

public class Master implements Protocol{
    public static void main (String[] args) {
        TreeSet<Integer> universe = new TreeSet<Integer>();
        Socket socket = null;
        try{
            ServerSocket serversocket = new ServerSocket(PORT);
            socket = serversocket.accept();
        }catch(IOException e){
            System.out.println(e);
        }

        try {
            DataOutputStream toSlave = new DataOutputStream(socket.getOutputStream());
            toSlave.writeInt(22266015);
            toSlave.flush();
        } catch(Exception e) {
        }

        new Thread(new ExtractedIdAcceptService(universe, socket)).start();

        System.out.println("Connected");
    }


    public static void main2 (String[] args) {
        Scanner in = new Scanner(System.in);
        Socket socket = null;
        try{
        
        ServerSocket serversocket = new ServerSocket(PORT);
        socket = serversocket.accept();

        DataOutputStream toSlave = new DataOutputStream(socket.getOutputStream());

        System.out.println("Connected");

        while(true){
            try {
                
                System.out.println("What's the next number?");
                int num = in.nextInt();
                toSlave.writeInt(num);
                toSlave.flush();
            } catch(InputMismatchException e) {
                System.out.println(e.getMessage());
            }
        }
        }catch(IOException e){
        
        }finally{
            System.out.println("Done");
            try {
                socket.close();
            } catch(Exception e) {
            }
            in.close();
        
        }

    }


}
