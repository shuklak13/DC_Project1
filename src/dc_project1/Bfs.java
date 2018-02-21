
package dc_project1;

import java.util.HashMap;
import java.lang.*;
import java.util.Iterator;


public class Bfs {
  int parent;
  boolean marked = false;
  boolean isLeader = false;
  HashMap<Integer, Boolean> nbrIsChild;
  HashMap<Integer, Boolean> haveAcked;
  HashMap<Integer, Boolean> haveSearchedUs;
  int myUid;
  
  public Bfs(boolean isLeader, int[] neighbors, int myUid)
  {
    this.myUid = myUid;
    this.isLeader = isLeader;
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
  
  public String genMsg(int nbr){
    BfsMessage send;
    if(nbr==parent && allNbrsAcked())
      send = new BfsMessage("pos-ack", myUid);
    else if(haveSearchedUs.get(nbr) && nbr!=parent)
      send = new BfsMessage("neg-ack", myUid);
    else if(marked)
      send = new BfsMessage("search", myUid);
    else
      send = new BfsMessage("", myUid);
    return send.toString();
  }
  
  public boolean allNbrsAcked(){
    Iterator it = haveAcked.entrySet().iterator();
    int ctr = 0;
    while (it.hasNext())
        if (((HashMap.Entry)it.next()).getValue().equals(true))
          ctr += 1;
    return ctr == haveAcked.size();
  }
  
}
    
   
