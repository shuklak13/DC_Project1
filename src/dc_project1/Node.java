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

class Node{
    int uid;
    int port;
    String hostname;
    int[] neighbors;
    int round;
    ArrayList<PelegMessage> buffer = new ArrayList<PelegMessage>();
    Node leader;
    String algo = "peleg";
    Peleg p1;
    Bfs b1;
    BufferedWriter log;

    public Node(int u, String hn, int p, int[] nghbrs, boolean test) {
        uid = u;
        if(test)
          hostname = "localhost";
        else
          hostname = hn;
        port = p;
        neighbors = nghbrs;
        try{
          log = new BufferedWriter(new FileWriter(Integer.toString(u)+".txt"));
          log.write("Timestamp\tMessage");
        }
        catch(IOException e){
          System.out.println("FAIL: Attempted to write to " + Integer.toString(u)+".txt");
          e.printStackTrace();
        }
        startServer();
    }
    
    public void writeToLog(String s) throws IOException{
      //log.append(Long.toString(System.currentTimeMillis()) + "\t" + s);
      log.append(s);
      log.newLine();
    }

    public boolean connectToNeighbors(HashMap<Integer, Integer> uids2ports, HashMap<Integer, String> uids2hosts){
        for(int neigbhor: neighbors)
            startSender(uids2ports.get(neigbhor), uids2hosts.get(neigbhor), neighbor);
            p1 = new Peleg(neighbors, uid);
        return true;
    }

    public void startSender(int port, String hostname, int neighborUID) {
        (new Thread() {
            @Override
            public void run() {
                try {
                    Socket s = new Socket(hostname, port);
                    BufferedWriter out = new BufferedWriter(
                            new OutputStreamWriter(s.getOutputStream()));
                    while (true) {
                        String msg = genMsg(neighborUID);
                        writeToLog("\nI send: " + readablePelegMsg(msg) + " to " + hostname+":"+port+"\n");
                        out.write(msg);
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
    
    public String readablePelegMsg(String s){
      String[] s2 = s.split(" ");
        StringBuilder sb = new StringBuilder();
        sb.append("MaxUID: " + s2[0]).append(" ");
        sb.append("Dist: " + s2[1]).append(" ");
        sb.append("Text: " + s2[2]).append(" ");
        sb.append("Round: " + s2[3]).append(" ");
        sb.append("Sender: " + s2[4]).append(" ");
        return sb.toString();
    }

    public void startServer() {
        System.out.println("Creating Server");
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
        System.out.println("Created Server");
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

    public String genMsg(int neighborUID)
    {
        if (algo.equals("peleg"))
            return p1.genMsg();
        else if (algo.equals("bfs"))
            return b1.genMsg(neighborUID);
        return null;
    }
    
    
    public String handleMsg(String m)
    {
        if (algo.equals("peleg"))
          if(!m.equals("terminate"))
            return p1.handleMsg(m);
          else{
            algo = "bfs";
            b1 = new Bfs(neighbors, uid);
            return "";
          }
        else if (algo.equals("bfs"))
            return b1.handleMsg(m);
        else
          return "";
    }
   
}