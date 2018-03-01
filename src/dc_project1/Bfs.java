
package dc_project1;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.StringJoiner;

public class Bfs {
  int parent;
  ArrayList<String> children = new ArrayList<String>();
  HashMap<Integer, Boolean> haveSearchedUs = new HashMap<Integer, Boolean>(); // to send a neg-ack
  HashMap<Integer, Boolean> haveRcvdAck = new HashMap<Integer, Boolean>();    // to ensure we've received from everyone before terminating )posack)
  HashMap<Integer, Boolean> haveSentAck = new HashMap<Integer, Boolean>();    // to ensure we've sent to everyone before terminating )posack)
  HashMap<Integer, Boolean> rcvdFromNbr = new HashMap<Integer, Boolean>();    // to increment rounds
  int ackCtr, sendCtr;
  Node owner;
  int height = Integer.MAX_VALUE;
  int maxHeight = 0;
  int maxDegree = 0;
  int[] neighbors;
  int round = 0;
  int initRound = -1; //round at which node rcvs first search msg
  int countRecMsgs = 0;
  
  public Bfs(boolean isLeader, int[] neighbors, Node owner)
  {
    this.neighbors = neighbors;
    this.owner = owner;
    if(owner.test)
      System.out.println(owner.uid + " started; Leadership=" + isLeader);
    if(isLeader){
      height = 0;
      parent = owner.uid;
    }
    ackCtr = 0;
    sendCtr = 0;
    for (int i=0; i<neighbors.length; i++){
        haveRcvdAck.put(neighbors[i], false);
        haveSentAck.put(neighbors[i], false);
        haveSearchedUs.put(neighbors[i], false);
        rcvdFromNbr.put(neighbors[i], false);
    }
  }
  
  public void updateDegreeAndHeight(BfsMessage m){
    if(m.maxHeight > maxHeight)
      maxHeight = m.maxHeight;
    if(m.maxDegree > maxDegree)
      maxDegree = m.maxDegree;
    if(degree() > maxDegree)
      maxDegree = degree();
  }
  
  public void handleMsg(String msg){
    if(msg.split("\\s")[0].equalsIgnoreCase("BFS")){
      BfsMessage m = BfsMessage.toBfsMsg(msg);
      if(!m.type.equals("dummy"))
        owner.writeToLog(constructLogMsg_Receive(m));
      synchronized(this){
        if(owner.isLeader())
          System.out.println("Leader rcvd: " + m.toReadableString());
        if(round <= m.round){
          if(m.type.equals("search"))
            handleSearchMsg(m);
          else if(m.type.endsWith("ack"))
            handleAckMsg(m);
          countRecMsgs++;
          rcvdFromNbr.put(m.senderUID, true);
          if(countRecMsgs==rcvdFromNbr.size())
            nextRound();
          updateDegreeAndHeight(m);
        }
      }
    }
  }
  
  private boolean marked(){
    assert (height < Integer.MAX_VALUE) == (initRound!=-1);
    return initRound!=-1;
  }
  
  private void updateRcvAck(int senderUid){
    if(owner.isLeader())
      System.out.println("Leader ack status (before): " + ackCtr + ", " + haveRcvdAck.toString());
    if(!haveRcvdAck.get(senderUid))
      ackCtr++;
    haveRcvdAck.put(senderUid, true);
    if(owner.isLeader())
      System.out.println("Leader ack status (after): " + ackCtr + ", " + haveRcvdAck.toString());
  }
  
  private void updateSendAck(int nbr){
    if(!haveSentAck.get(nbr))
      sendCtr++;
    haveSentAck.put(nbr, true);
  }
  
  private void handleSearchMsg(BfsMessage m){
    haveSearchedUs.put(m.senderUID, true);
    updateRcvAck(m.senderUID);
    if(!marked()){
      initRound = round;
      parent = m.senderUID;
      height = m.height+1;
      if(height>maxHeight)
        maxHeight = height;
    }
  }
  
  private void handleAckMsg(BfsMessage m){
    if(m.type.startsWith("pos")){
      children.add(String.valueOf(m.senderUID));
      if(owner.isLeader())
        System.out.println("LEader rcvd posack from " + m.senderUID);
    }
    updateRcvAck(m.senderUID);
  }
  
  private void nextRound(){
    round++;
    countRecMsgs=0;
    for (int i=0; i<neighbors.length; i++)
        this.rcvdFromNbr.put(neighbors[i], false);
  }
  
  public int degree(){
    if(owner.isLeader())
      return children.size();
    else
      return 1+children.size();
  }
  
  public BfsMessage genMsg(int nbr){
    String type;
    if(nbr==parent){
      if(allNbrsAckedButParent()){
        type = "pos-ack";
        updateSendAck(nbr);
      }
      else
        type = "dummy";
    }
    else if(haveSearchedUs.get(nbr)){
      type = "neg-ack";
      updateSendAck(nbr);
      }
    else if(owner.isLeader() || (marked() && round>initRound)){  // nodes should not send search in the same round they received search
      type = "search";
      updateSendAck(nbr);
    }
    else
      type = "dummy";
    if(owner.test)
      System.out.println(owner.uid + " sends to " + nbr + ": " + type + 
            " (PARENT IS " + parent + ", Round="+round + ")   " +
            "RcvAcks: " + ackCtr+"/"+haveRcvdAck.size() + " " + haveRcvdAck.toString() +
            " SndAcks: " + sendCtr+"/"+haveSentAck.size() + " " + haveSentAck.toString());
    return new BfsMessage(type, owner.uid, owner.leader, height, maxHeight, round, degree());
  }
  
  public String terminateString(){
    StringJoiner finalOutput = new StringJoiner("\t");
      finalOutput.add(String.valueOf(owner.uid) + " has terminated.");
      finalOutput.add("My Parent: " + parent);
      finalOutput.add("My Children: " + String.join(",", children));
      finalOutput.add("My Degree: " + degree());
      finalOutput.add("My Height: " + height);
    if(owner.isLeader()){
      finalOutput.add("\nMax Degree: " + maxDegree);
      finalOutput.add("Max Height: " + maxHeight);
    }
    return finalOutput.toString();
  }
  
  public boolean allNbrsAckedButParent(){
    return ackCtr == haveRcvdAck.size() && sendCtr==haveSentAck.size()-1; //-1 because won't send to parent until termination
  }
  
  public boolean allNbrsAcked(){
    return ackCtr == haveRcvdAck.size() && sendCtr==haveSentAck.size(); //-1 because won't send to parent until termination
  }
  
  public String constructLogMsg_Receive(BfsMessage bmsg){
    return "(BFS) I receive: " + bmsg.toReadableString() + "\tReceived ACKs from nodes: " + haveRcvdAck.toString();
  }
  public String constructLogMsg_Send(BfsMessage bmsg, String hostname, int port){
    return "(BFS) I send the following to " + hostname+":"+port + " " + bmsg.toReadableString();
  }
}
    
   
