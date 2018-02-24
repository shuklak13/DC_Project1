
package dc_project1;

import java.util.HashMap;
import java.lang.*;
import java.util.ArrayList;
import java.util.StringJoiner;

public class Bfs {
  int parent;
  ArrayList<String> children = new ArrayList<String>();
  HashMap<Integer, Boolean> haveSearchedUs = new HashMap<Integer, Boolean>();
  HashMap<Integer, Boolean> haveAcked = new HashMap<Integer, Boolean>();
  int ackCtr = 0;
  Node owner;
  int degree = Integer.MAX_VALUE;
  int maxDegree = 0;
  
  public Bfs(boolean isLeader, int[] neighbors, Node owner)
  {
    this.owner = owner;
    System.out.println(owner.uid + " started; Leadership=" + isLeader);
    if(isLeader)
      degree = 0;
    for (int i=0; i<neighbors.length; i++){
        this.haveAcked.put(neighbors[i], false);
        this.haveSearchedUs.put(neighbors[i], false);
    }
  }
  
  public void handleMsg(String msg){
    BfsMessage m = BfsMessage.toBfsMsg(msg);
    if(!m.type.equals("dummy"))  
      synchronized(this){
        if(m.maxDegree > maxDegree)
          maxDegree = m.maxDegree;
        if(m.type.equals("terminate")){
          StringJoiner finalOutput = new StringJoiner("\t");
            finalOutput.add(String.valueOf(owner.uid));
            finalOutput.add("My Parent: " + parent);
            finalOutput.add("My Children: " + String.join(",", children));
            finalOutput.add("My Degree: " + degree);
          if(owner.isLeader()){
            finalOutput.add("Max Degree: " + maxDegree);}
          System.out.println(finalOutput.toString());
        }
        else if(m.type.equals("search")){
          if(m.degree<degree){
            parent = m.senderUID;
            haveAcked.put(m.senderUID, true);
            ackCtr++;
            degree = m.degree+1;
          }
        }
        else if(m.type.endsWith("ack")){
          if(m.type.startsWith("pos"))
            children.add(String.valueOf(m.senderUID));
          haveAcked.put(m.senderUID, true);
          ackCtr++;
        }
      }
  }
  
  public BfsMessage genMsg(int nbr){
    String type;
    if(owner.isLeader() && allNbrsAcked())
      type = "terminate";
    else if(nbr==parent && allNbrsAcked())
      type = "pos-ack";
    else if(haveSearchedUs.get(nbr) && nbr!=parent)
      type = "neg-ack";
    else if(degree < Integer.MAX_VALUE)
      type = "search";
    else
      type = "dummy";
    return new BfsMessage(type, owner.uid, owner.leader, degree, maxDegree);
  }
  
  public boolean allNbrsAcked(){
    return ackCtr == haveAcked.size();
  }
  
    public String constructLogMsg_Receive(BfsMessage bmsg){
      StringJoiner sj = new StringJoiner("\t");
        sj.add("(BFS) I receive: ");
        sj.add("\nSender: " + bmsg.senderUID);
        sj.add("Message Type: " + bmsg.type);
      return sj.toString();
    }
    
    public String constructLogMsg_Send(BfsMessage bmsg, String hostname, int port){
      StringJoiner sj = new StringJoiner("\t");
        sj.add("(BFS) I send the following to " + hostname + ":"+port);
        sj.add("\nSender: " + bmsg.senderUID);
        sj.add("Message Type: " + bmsg.type);
      return sj.toString();
    }
}
    
   
