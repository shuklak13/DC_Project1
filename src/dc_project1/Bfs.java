
package dc_project1;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.StringJoiner;

public class Bfs {
  int parent;
  ArrayList<String> children = new ArrayList<String>();
  HashMap<Integer, Boolean> haveSearchedUs = new HashMap<Integer, Boolean>();
  HashMap<Integer, Boolean> haveAcked = new HashMap<Integer, Boolean>();
  HashMap<Integer, Boolean> haveSent = new HashMap<Integer, Boolean>();
  HashMap<Integer, Boolean> rcvdFromNbr = new HashMap<Integer, Boolean>();
  int ackCtr, sendCtr;
  Node owner;
  int degree = Integer.MAX_VALUE;
  int maxDegree = 0;
  int[] neighbors;
  int round = 0;
  int initRound = -1; //round at which node rcvs first search msg
  int countRecMsgs = 0;
  
  public Bfs(boolean isLeader, int[] neighbors, Node owner)
  {
    this.neighbors = neighbors;
    this.owner = owner;
    System.out.println(owner.uid + " started; Leadership=" + isLeader);
    if(isLeader){
      degree = 0;
      parent = owner.uid;
    }
    ackCtr = 0;
    sendCtr = 0;
    for (int i=0; i<neighbors.length; i++){
        haveAcked.put(neighbors[i], false);
        haveSent.put(neighbors[i], false);
        haveSearchedUs.put(neighbors[i], false);
        rcvdFromNbr.put(neighbors[i], false);
    }
  }
  
  public void handleMsg(String msg){
    if(msg.split("\\s")[0].equalsIgnoreCase("BFS")){
      BfsMessage m = BfsMessage.toBfsMsg(msg);
      if(!m.type.equals("dummy"))
        owner.writeToLog(constructLogMsg_Receive(m));
      synchronized(this){
        if(round <= m.round){
          if(m.maxDegree > maxDegree)
            maxDegree = m.maxDegree;
          if(m.type.equals("search"))
            handleSearchMsg(m);
          else if(m.type.endsWith("ack"))
            handleAckMsg(m);
          countRecMsgs++;
          rcvdFromNbr.put(m.senderUID, true);
          if(countRecMsgs==rcvdFromNbr.size())
            nextRound();
        }
      }
    }
  }
  
  private boolean marked(){
    assert (degree < Integer.MAX_VALUE) == (initRound!=-1);
    return initRound!=-1;
  }
  
  private void handleSearchMsg(BfsMessage m){
    haveSearchedUs.put(m.senderUID, true);
    if(!haveAcked.get(m.senderUID))
      ackCtr++;
    haveAcked.put(m.senderUID, true);
    if(!marked()){
      initRound = round;
      parent = m.senderUID;
      degree = m.degree+1;
      if(degree>maxDegree)
        maxDegree = degree;
    }
  }
  
  private void handleAckMsg(BfsMessage m){
    if(m.type.startsWith("pos"))
      children.add(String.valueOf(m.senderUID));
    else if(owner.isLeader())
      System.out.println(m.senderUID + " send " + m.type);
    if(!haveAcked.get(m.senderUID))
      ackCtr++;
    haveAcked.put(m.senderUID, true);
  }
  
  private void nextRound(){
    round++;
    countRecMsgs=0;
    for (int i=0; i<neighbors.length; i++)
        this.rcvdFromNbr.put(neighbors[i], false);
  }
  
  public BfsMessage genMsg(int nbr){
    String type;
    if(nbr==parent){
      if(allNbrsAcked())
        type = "pos-ack";
      else
        type = "dummy";
    }
    else if(haveSearchedUs.get(nbr))
      type = "neg-ack";
    else if(owner.isLeader() || (marked() && round>initRound))  // nodes should not send search in the same round they received search
      type = "search";
    else
      type = "dummy";
//    System.out.println(owner.uid + " sends to " + nbr + ": " + type + 
//            " (PARENT IS " + parent + ", Round="+round + ")   " +
//            "Acks: " + ackCtr+"/"+haveAcked.size() + " " + haveAcked.toString());
    haveSent.put(nbr, true);
    sendCtr++;
    if(sendCtr==haveSent.size()){
      sendCtr = 0;
      for (int i=0; i<neighbors.length; i++)
          haveSent.put(neighbors[i], false);
    }
    return new BfsMessage(type, owner.uid, owner.leader, degree, maxDegree, round);
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
  public boolean allNbrsSent(){
    return sendCtr == 0;
  }
  
  public String constructLogMsg_Receive(BfsMessage bmsg){
    return "(BFS) I receive: " + bmsg.toReadableString() + "\tReceived ACKs from nodes: " + haveAcked.toString();
  }
  public String constructLogMsg_Send(BfsMessage bmsg, String hostname, int port){
    return "(BFS) I send the following to " + hostname+":"+port + " " + bmsg.toReadableString();
  }
}
    
   
