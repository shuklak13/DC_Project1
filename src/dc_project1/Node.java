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
      log.append(Long.toString(System.currentTimeMillis()) + "\t" + s);
      log.newLine();
    }

    public boolean connectToNeighbors(HashMap<Integer, Integer> uids2ports, HashMap<Integer, String> uids2hosts){
        for(int neigbhor: neighbors)
            startSender(uids2ports.get(neigbhor), uids2hosts.get(neigbhor));
            p1 = new Peleg(neighbors, uid);
//            b1 = new Bfs(neighbors);
        return true;
    }

    public void startSender(int port, String hostname) {
        (new Thread() {
            @Override
            public void run() {
                try {
                    Socket s = new Socket(hostname, port);
                    BufferedWriter out = new BufferedWriter(
                            new OutputStreamWriter(s.getOutputStream()));
                    while (true) {
                        out.write(genMsg().toString());
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
      
        (new Thread() {
            @Override
            public void run() {
                ServerSocket ss;
                try {
                    ss = new ServerSocket(port);
                    Socket s = ss.accept();
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(s.getInputStream()));
                    String line = null;
                    while ((line = in.readLine()) != null) {
                        writeToLog(handleMsg(line));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        
      
		/*try	{
			ServerSocket ss = new ServerSocket(port);
			while(true)
			{
				ClientManager w;
				try {
                    Socket s = ss.accept();
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(s.getInputStream()));
                    ClientManager w = new ClientManager(in);
                    String line = null;
                    while ((line = in.readLine()) != null) {
                        writeToLog(handleMsg(line));
                    }
					Thread t = new Thread(w);
					t.start();
				} catch(IOException e) {
					System.out.println("accept failed");
					System.exit(100);
				}				
			}

		} catch(IOException ex) {
			ex.printStackTrace();
		}
        */
        
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

    public String genMsg()
    {
        if (algo.equals("peleg"))
            return p1.genMsg();
        //else if (algo.equals("bfs"))
            //return b1.getMsg();
        return null;
    }

    public String handleMsg(String m)
    {
        if (algo.equals("peleg"))
            return p1.handleMsg(m);
        //else if (algo.equals("bfs"))
            //return b1.handleMsg(m);
        else
          return "";
    }
   
}