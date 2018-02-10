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
    String uid;
    int port;
    String hostname;
    String[] neighbors;
    int round;
    ArrayList<Message> buffer = new ArrayList<Message>();
    Node leader;

    public Node(String u, String hn, int p, String[] nghbrs){
    }
    public Node(String u, String hn, int p, String[] nghbrs, boolean test){
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
        for(String neigbhor: neighbors)
            startSender(neigbhor);

        /*try	{
			ServerSocket serverSock = new ServerSocket(port);			
			while(true)
			{
				ClientManager clientmanager;
				try {
					clientmanager = new ClientManager(serverSock.accept());
					Thread t = new Thread(clientmanager);
					t.start();
				} catch(IOException e) {
					System.out.println("accept failed");
					System.exit(100);
				}				
			}
		} catch(IOException ex) {
			ex.printStackTrace();
		}*/
        // TO-DO: CREATE SOCKET FOR EACH NEIGHBOR
        
        return true;
    }

    public void startSender(String neighbor) {
        (new Thread() {
            @Override
            public void run() {
                try {
                    Socket s = new Socket(hostname, port);
                    BufferedWriter out = new BufferedWriter(
                            new OutputStreamWriter(s.getOutputStream()));
                    while (true) {
                        out.write(new Message(uid, neighbor, "", 0).toString());
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
        for(String neighbor: neighbors)
            sb.append(neighbor+"    ");
        System.out.println(sb.toString());
    }

    public void send(Message msg){
        // TO-DO
        String sendMsg = msg.toString();
        String message;
		BufferedReader reader = null;
		PrintWriter writer = null;

		try	{
			// Create a client socket and connect to server at 127.0.0.1 port 5000
			Socket clientSocket = new Socket("localhost", 5000);
			
			/* Create BufferedReader to read messages from server. Input stream is in bytes. 
				They are converted to characters by InputStreamReader.
				Characters from the InputStreamReader are converted to buffered characters by BufferedReader.
				This is done for efficiency purpose.
			*/
			reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			// PrintWriter is a bridge between character data and the socket's low-level output stream
			writer = new PrintWriter(clientSocket.getOutputStream(), true);
			
		} catch(IOException ex) {
			ex.printStackTrace();
		}

		try {
			writer.println("Hello from Client");
			message = reader.readLine();
			System.out.println(message);
		} catch(IOException e) {
			System.out.println("Read failed");
			System.exit(100);
		}
    }

    public void receive(){
        // TO-DO
        
    }
    
    public Node peleg(){
      // TO-DO
      
      return leader;
    }
}