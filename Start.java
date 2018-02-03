import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
import java.net.*;

public class Start {
    public static void main(String[] args){
        ArrayList<Node> nodes;
        try(Scanner sc=new Scanner(new File("config.txt"))){
            nodes = parseLines(sc);
        }
        catch(IOException e){
            System.out.println("File " + args[1] + " not found");
            System.exit(0);
        }
        Node leader = peleg(nodes);
        BFSTree tree = bfsTree(leader);
    }
    public static Node peleg(Nodes nodes){
        // TO-DO
        return nodes.get(0);
    }
    public static BFSTree bfsTree(Node root){
        // TO-DO
        return BFSTree(root);
    }
    public static Node[] parseLines(Scanner sc){
        int numNodes = 0;
        ArrayList<Node> nodes = new ArrayList<>();
        while(sc.hasNext()){
            String[] params = sc.nextLine().trim().split("\\s+");
            
            if(!(params[0].equals("#") || params.length<1)){
                if(numNodes==0)
                    numNodes = Integer.parseInt(params[0]);
                else
                    nodes.add(parseLine(params));
            }
        }
        nodes.forEach(node -> node.print());
        return nodes;
    }
    public static Node parseLine(String[] nodeParams){
        String uid = nodeParams[0];
        String hostname = nodeParams[1];
        String port = nodeParams[2];
        String[] neighbors = new String[nodeParams.length-3];
        System.arraycopy(nodeParams, 3, neighbors, 0, nodeParams.length-3);
        return new Node(uid, hostname, port, neighbors);
    }
}

class Node{
    String uid, port;
    String hostname;
    String[] neighbors;
    int round;
    ArrayList<Message> buffer = new ArrayList<Message>();

    public Node(String u, String hn, String p, String[] nghbrs){
        uid = u;
        hostname = hn;
        port = p;
        neighbors = nghbrs;
        try	{
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
		}
        // TO-DO: CREATE SOCKET FOR EACH NEIGHBOR
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
}

class Message{
    String text;
    int round;
    String sender, receiver;

    public Message(String s, String r, string t, int rnd){
        sender = s;
        receiver = r;
        round = rnd;
        text = t;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(sender+" ");
        sb.append(receiver+" ");
        sb.append(round+";");
        sb.append(text);
        return sb.toString();
    }
}

// TO-DO
class BFSTree{
    public BFSTree(Node root){

    }
}