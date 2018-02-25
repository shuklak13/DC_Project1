
package dc_project1;

import java.util.HashMap;
import java.lang.*;
import java.util.ArrayList;
import java.util.StringJoiner;

public class Bfs {
  int parent;
  ArrayList<String> children = new ArrayList<String>();
  HashMap<Integer, Boolean> haveSearchedUs;
  HashMap<Integer, Boolean> haveAcked;
  int ackCtr;
  Node owner;
  int degree = Integer.MAX_VALUE;
  int maxDegree = 0;
  int[] neighbors;
  
  public Bfs(boolean isLeader, int[] neighbors, Node owner)
  {
    this.neighbors = neighbors;
    this.owner = owner;
    System.out.println(owner.uid + " started; Leadership=" + isLeader);
    if(isLeader)
      degree = 0;
    initializeState();
  }
  
  public void initializeState(){
    haveAcked = new HashMap<Integer, Boolean>();
    haveSearchedUs = new HashMap<Integer, Boolean>();
    ackCtr = 0;
    for (int i=0; i<neighbors.length; i++){
        haveAcked.put(neighbors[i], false);
        haveSearchedUs.put(neighbors[i], false);
    }
  }
  
  public void handleMsg(String msg){
    BfsMessage m = BfsMessage.toBfsMsg(msg);
    if(!m.type.equals("dummy"))  
      synchronized(this){
        if(m.maxDegree > maxDegree)
          maxDegree = m.maxDegree;
        if(m.type.equals("search") && (m.degree < degree)){
            parent = m.senderUID;
            initializeState();
            haveAcked.put(m.senderUID, true);
            haveSearchedUs.put(m.senderUID, true);
            ackCtr++;
            degree = m.degree+1;
            if(degree>maxDegree)
              maxDegree = degree;
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
  
  public String terminateString(){
    StringJoiner finalOutput = new StringJoiner("\t");
      finalOutput.add(String.valueOf(owner.uid));
      finalOutput.add("My Parent: " + parent);
      finalOutput.add("My Children: " + String.join(",", children));
      finalOutput.add("My Degree: " + degree);
    if(owner.isLeader()){
      finalOutput.add("Max Degree: " + maxDegree);}
    return finalOutput.toString();
  }
  
  public boolean allNbrsAcked(){
    return ackCtr == haveAcked.size();
  }
  
  public String constructLogMsg_Receive(BfsMessage bmsg){
    return "(BFS) I receive: " + bmsg.toReadableString();
  }
  public String constructLogMsg_Send(BfsMessage bmsg, String hostname, int port){
    return "(BFS) I send the following to " + hostname+":"+port + " " + bmsg.toReadableString();
  }
}
    
   
