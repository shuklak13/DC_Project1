
package dc_project1;

import java.util.HashMap;
import java.lang.*;
import java.util.Iterator;
import java.util.StringJoiner;


public class Bfs {
  int parent;
  boolean marked = false;
  HashMap<Integer, Boolean> nbrIsChild = new HashMap<Integer, Boolean>();
  HashMap<Integer, Boolean> haveAcked = new HashMap<Integer, Boolean>();
  HashMap<Integer, Boolean> haveSearchedUs = new HashMap<Integer, Boolean>();
  Node owner;
  
  public Bfs(boolean isLeader, int[] neighbors, Node owner)
  {
    this.owner = owner;
    System.out.println(owner.uid + " started; Leadership=" + isLeader);
    if(isLeader)
      marked = true;
    for (int i=0; i<neighbors.length; i++){
        this.nbrIsChild.put(neighbors[i], false);
        this.haveAcked.put(neighbors[i], false);
        this.haveSearchedUs.put(neighbors[i], false);
    }
  }
  
  public String handleMsg(String msg){
    BfsMessage m = BfsMessage.toBfsMsg(msg);
    if(m.type.equals(""))  
      return "";
    else if(m.type.equals("search")){
      if(!marked){
        marked = true;
        parent = m.senderUID;
        haveAcked.put(m.senderUID, true);
        return "pos-ack";
      }
      else
        return "neg-ack";
    }
    else if(m.type.endsWith("ack")){
      if(m.type.startsWith("pos"))
        nbrIsChild.put(m.senderUID, true);
      haveAcked.put(m.senderUID, true);
    }
    return "";
  }
  
  public BfsMessage genMsg(int nbr){
    if(nbr==parent && allNbrsAcked())
      return new BfsMessage("pos-ack", owner.uid, owner.leader);
    else if(haveSearchedUs.get(nbr) && nbr!=parent)
      return new BfsMessage("neg-ack", owner.uid, owner.leader);
    else if(marked)
      return new BfsMessage("search", owner.uid, owner.leader);
    else
      return new BfsMessage("", owner.uid, owner.leader);
  }
  
  public boolean allNbrsAcked(){
    Iterator it = haveAcked.entrySet().iterator();
    int ctr = 0;
    while (it.hasNext())
        if (((HashMap.Entry)it.next()).getValue().equals(true))
          ctr += 1;
    return ctr == haveAcked.size();
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
    
   
