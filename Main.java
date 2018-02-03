import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

import javafx.scene.shape.HLineTo;

public class Main {
    public static void main(String[] args){
        try(Scanner sc=new Scanner(new FileInputStream(args[1]))){
            int numNodes = 0;
            ArrayList<Node> nodes = new ArrayList<Node>();
            while(sc.hasNext()){
                String line = sc.nextLine();
                if(!line.startsWith("#")){
                    if(numNodes==0)
                        numNodes = Integer.parseInt(line);
                    else
                        nodes.add(parseLine(line));
                }
            }
            nodes.forEach(node -> node.print());
        }
        catch(IOException e){
            System.out.println("File " + args[1] + " not found");
        }
    }
    public static Node parseLine(String line){
        String[] nodeParams = line.split(" ");
        int uid = Integer.parseInt(nodeParams[0]);
        String hostname = nodeParams[1];
        int port = Integer.parseInt(nodeParams[2]);
        int[] neighbors = new int[nodeParams.length-3];
        System.arraycopy(nodeParams, 3, neighbors, 0, nodeParams.length-3);
        return new Node(uid, hostname, port, neighbors);
    }
}

class Node{
    int uid, port;
    String hostname;
    int[] neighbors;

    public Node(int u, String hn, int p, int[] nghbrs){
        uid = u;
        hostname = hn;
        port = p;
        neighbors = nghbrs;
    }

    public void print(){
        StringBuffer sb = new StringBuffer();
        sb.append(uid);
        sb.append(hostname);
        sb.append(port);
        for(int neighbor: neighbors)
            sb.append(neighbor);
        System.out.println(sb.toString());
    }
}