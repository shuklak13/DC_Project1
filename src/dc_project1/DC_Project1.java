package dc_project1;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
import java.net.*;

public class DC_Project1 {
    public static void main(String[] args){
        ArrayList<Node> nodes;
        try(Scanner sc=new Scanner(new File("config.txt"))){
            nodes = parseLines(sc);
            for(Node node: nodes)
                node.connectToNeighbors();
            Node leader = peleg(nodes);
            BFSTree tree = bfsTree(leader);
        }
        catch(IOException e){
            System.out.println("File " + args[1] + " not found");
            System.exit(0);
        }
    }
    public static ArrayList<Node> parseLines(Scanner sc){
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
    public static Node peleg(ArrayList<Node> nodes){
        // TO-DO
        return nodes.get(0);
    }
    public static BFSTree bfsTree(Node root){
        // TO-DO
        return new BFSTree(root);
    }
}