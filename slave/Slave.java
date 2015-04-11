import java.util.*;
import java.awt.event.*;

public class Slave {
    public static void main (String[] args) {
        //DownloadService service = new DownloadService();
        //Thread t = new Thread(service);
        //service.start();

        TreeSet<Integer> idset = new TreeSet<Integer>();
        idset.add(22266015);
        idset.add(19566985);
        idset.add(21826476);
        idset.add(19561631);
        idset.add(19566980);
        idset.add(19566985);
        idset.add(19801697);
        idset.add(19810462);
        idset.add(19902415);
        idset.add(20014415);
        idset.add(20120168);
        idset.add(20187542);
        idset.add(20200749);
        idset.add(20261859);
        idset.add(20376438);
        idset.add(20727479);


        while(!idset.isEmpty()){
            Thread t = new DownloadThread(idset.pollFirst());
            t.start();
        }
        System.err.println("Done");
        
    }

}
