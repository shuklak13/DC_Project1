package dc_project1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

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

    public Node(int u, String hn, int p, int[] nghbrs, boolean test){
        uid = u;
        if(test)
          hostname = "localhost";
        else
          hostname = hn;
        port = p;
        neighbors = nghbrs;
        startServer();
    }

    public boolean connectToNeighbors(){
        // TO-DO
        for(int neigbhor: neighbors)
            startSender(neigbhor);
            p1 = new Peleg(neighbors, uid);
//            b1 = new Bfs(neighbors);

        return true;
    }

    public void startSender(int neighbor) {
        (new Thread() {
            @Override
            public void run() {
                try {
                    Socket s = new Socket(hostname, port);
                    BufferedWriter out = new BufferedWriter(
                            new OutputStreamWriter(s.getOutputStream()));
                    while (true) {
                        //out.write(new Message(uid, neighbor, "", 0).toString());
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
                        handleMsg(line);
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void print(){
        StringBuilder sb = new StringBuilder();
        sb.append(uid+" ");
        sb.append(hostname+" ");
        sb.append(port+" ");
        for(int neighbor: neighbors)
            sb.append(neighbor+"    ");
        System.out.println(sb.toString());
    }

    public String genMsg()
    {
        if (algo.equals("peleg"))
            return p1.genMsg();
        //else if (algo.equals("bfs"))
            //return b1.getMsg();
        return null;
    }

    public void handleMsg(String m)
    {
        if (algo.equals("peleg"))
            p1.handleMsg(m);
        //else if (algo.equals("bfs"))
            //b1.handleMsg(m);

    }
   
}