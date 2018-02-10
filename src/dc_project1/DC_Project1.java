package dc_project1;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
import java.util.HashMap;

public class DC_Project1 {
    static boolean test = false;
    public static void main(String[] args){
        ArrayList<Node> nodes;
        if(args.length>=2 && args[1].equalsIgnoreCase("test")){
          test = true;
          System.out.println("Running in test (host will be localhost)");
        }
        try(Scanner sc=new Scanner(new File(args[0]))){
            nodes = parseLines(sc);
            HashMap<Integer, Integer> uids2ports = new HashMap<Integer, Integer>();
            for(Node node: nodes)
              uids2ports.put(node.uid, node.port);
            for(Node node: nodes)
                node.connectToNeighbors(uids2ports);
            //Node leader = peleg(nodes);
            //BFSTree tree = bfsTree(leader);
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
        int[] neighbors = new int[nodeParams.length-3];
        for(int i=3; i<nodeParams.length; i++)
            neighbors[i-3]=Integer.parseInt(nodeParams[i]);
        return new Node(Integer.parseInt(uid), hostname, Integer.parseInt(port), neighbors, test);
    }

}