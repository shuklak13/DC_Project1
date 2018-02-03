import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
//import javafx.scene.Node;


public class Start {
    public static void main(String[] args){
        try(Scanner sc=new Scanner(new File("config.txt"))){
            int numNodes = 0;
            ArrayList<Node> nodes = new ArrayList<>();
            while(sc.hasNext()){
                String[] params = sc.nextLine().trim().split("\\s+");
                
                System.out.println("\n\nNEWLINE");
                StringBuilder sb = new StringBuilder();
                for(String param: params)
                    sb.append(param+"    ");
                System.out.println(sb.toString());
                System.out.println(params.length);
                if(!(params[0].equals("#") || params.length<1)){
                    if(numNodes==0)
                        numNodes = Integer.parseInt(params[0]);
                    else
                        nodes.add(parseLine(params));
                }
            }
            //nodes.forEach(node -> node.print());
            
            StringBuilder sb = new StringBuilder();
            for(Node node: nodes)
                sb.append(node+"    ");
            System.out.println(sb.toString());
        }
        catch(IOException e){
            System.out.println("File " + args[1] + " not found");
        }
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

    public Node(String u, String hn, String p, String[] nghbrs){
        uid = u;
        hostname = hn;
        port = p;
        neighbors = nghbrs;
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
}
