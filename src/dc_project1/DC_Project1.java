package dc_project1;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
import java.util.HashMap;

public class DC_Project1 {
    static boolean test = false;
    static boolean distributed = false;
    static int nodeId;
    public static void main(String[] args){
        if(args.length>=2){
          if(args[1].equalsIgnoreCase("test")){
            test = true;
            System.out.println("Running in test (host will be localhost)");
          }
          if(args[1].matches("^\\d+$")){
            distributed = true;
            nodeId = Integer.parseInt(args[1]);
          }
        }
        try{
            Scanner sc=new Scanner(new File(args[0]));
            ArrayList<Node> nodes = parseLines(sc);
            HashMap<Integer, Integer> uids2ports = new HashMap<Integer, Integer>();
            HashMap<Integer, String> uids2hosts = new HashMap<Integer, String>();
            for(Node node: nodes){
              uids2ports.put(node.uid, node.port);
              uids2hosts.put(node.uid, node.hostname);
            }
            System.out.println("\n Mapping from UIDs to Ports:");
            nodes.forEach(node -> System.out.println(node.uid + " -> " + uids2hosts.get(node.uid) + ":" + uids2ports.get(node.uid)));
            System.out.println();
            
            for(Node node: nodes)
              if(!distributed || node.uid == nodeId)
                node.connectToNeighbors(uids2ports, uids2hosts);
        }
        catch(IOException e){
            System.out.println("File " + args[0] + " not found");
            System.exit(0);
        }
    }
    public static ArrayList<Node> parseLines(Scanner sc){
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        while(sc.hasNext()){
          String line = sc.nextLine();
          String[] params = line.trim().split("\\s+");
          System.out.println(line);
          if(params.length>1 && !params[0].equals("#")){
            int uid = Integer.valueOf(params[0]);
            if (nodes.containsKey(uid))
              nodes.get(uid).addNeighbors(parseLine_Neighbors(params));
            else
              nodes.put(uid, parseLine_Node(params));
          }
        }
        nodes.forEach((nodeID, node) -> System.out.println(node.toString()));
        return new ArrayList<Node>(nodes.values());
    }
    public static Node parseLine_Node(String[] nodeParams){
        String uid = nodeParams[0];
        String hostname = nodeParams[1];
        String port = nodeParams[2];
        return new Node(Integer.parseInt(uid), hostname, Integer.parseInt(port), test);
    }
    public static int[] parseLine_Neighbors(String[] params){
        int[] neighbors = new int[params.length-1];
        for(int i=1; i<params.length; i++)
            neighbors[i-1]=Integer.parseInt(params[i]);
        return neighbors;
    }

}
