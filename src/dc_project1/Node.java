package dc_project1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileWriter;
import java.io.File;

class Node{
    int uid;
    int port;
    String hostname;
    int[] neighbors;
    int round;
    ArrayList<PelegMessage> buffer = new ArrayList<PelegMessage>();
    int leader = -1;  // if -1, algo=peleg; else, algo=bfs
    Peleg p1;
    Bfs b1;
    BufferedWriter log;

    public Node(int u, String hn, int p, int[] nghbrs, boolean test) {
      this(u, hn, p, test);
      neighbors = nghbrs;
    }

    public Node(int u, String hn, int p, boolean test) {
        uid = u;
        if(test)
          hostname = "localhost";
        else
          hostname = hn;
        port = p;
        try{
          log = new BufferedWriter(new FileWriter(new File("logs", Integer.toString(u)+".txt")));
          log.write("Timestamp\tMessage");
        }
        catch(IOException e){
          System.out.println("FAIL: Attempted to write to " + Integer.toString(u)+".txt");
          e.printStackTrace();
        }
        startServer();
    }
    
    public void addNeighbors(int[] neighbors){
      this.neighbors = neighbors;
    }
    
    public void writeToLog(String s){
      try{
        log.append(s);
        log.newLine();
      }
      catch(IOException e){
        System.out.println("IOException: " + e);
      }
    }

    public void connectToNeighbors(HashMap<Integer, Integer> uids2ports, HashMap<Integer, String> uids2hosts){
      if(neighbors!=null){
        for(int neigbhor: neighbors)
            startSender(uids2ports.get(neigbhor), uids2hosts.get(neigbhor), neigbhor);
        p1 = new Peleg(neighbors, this);
      }
      else
        System.out.println("ERROR: Node " + uid + " attempted to connect to neighbors, but has none");
    }

    public void startSender(int port, String hostname, int neighborUID) {
        (new Thread() {
            boolean terminated = false;
            @Override
            public void run() {
                try {
                    Socket s = new Socket(hostname, port);
                    BufferedWriter out = new BufferedWriter(
                            new OutputStreamWriter(s.getOutputStream()));
                    while (!terminated) {
                        if (leader==-1){
                          PelegMessage msg = p1.genMsg();
                          writeToLog(p1.constructLogMsg_Send(msg, hostname, port));
                          out.write(msg.toString());
                        }
                        else{
                          if(b1==null)
                            initiateBfs();
                          BfsMessage msg = b1.genMsg(neighborUID);
                          if(msg.type.equalsIgnoreCase("terminate"))
                            terminated = true; 
                          writeToLog(b1.constructLogMsg_Send(msg, hostname, port));
                          out.write(msg.toString());
                        }
                        out.newLine();
                        out.flush();
                        Thread.sleep(200);
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void startServer() {
        Node t = this;
        (new Thread() {
            @Override
            public void run() {
              try	{
                  ServerSocket ss = new ServerSocket(port);
                  while(true)
                    try {
                        Socket s = ss.accept();
                        ClientManager w = new ClientManager(s, t, log);
                        Thread t = new Thread(w);
                        t.start();
                    } catch(IOException e) {
                        System.out.println("accept failed");
                        System.exit(100);
                    }		
              } catch(IOException ex) {
                  ex.printStackTrace();
              }
            }
        }).start();
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(uid+" ");
        sb.append(hostname+" ");
        sb.append(port+" ");
        for(int neighbor: neighbors)
            sb.append(neighbor+"    ");
        return sb.toString();
    }
    
    public void initiateBfs(){
      synchronized(this){
        if(b1==null)
          b1 = new Bfs(isLeader(), neighbors, this);
      }
    }
    
    public boolean isLeader(){
      return uid==leader;
    }
   
}