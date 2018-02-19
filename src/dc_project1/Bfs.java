
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
  
  public Bfs(boolean isLeader, int[] neighbors, int self)
  {
    this.isLeader = isLeader;
    if(isLeader)
      marked = true;
    for (int i=0; i<neighbors.length; i++)
        this.nbrIsChild.put(neighbors[i], false);
    for (int i=0; i<neighbors.length; i++)
        this.haveAcked.put(neighbors[i], false);
    for (int i=0; i<neighbors.length; i++)
        this.haveSearchedUs.put(neighbors[i], false);
  }
  
  public String handleMsg(String s){
    String[] m = s.split(" ");
    int sender = Integer.parseInt(m[1]);
    if(m[0].equals(""))  
      return "";
    else if(m[0].equals("search")){
      if(!marked){
        marked = true;
        parent = sender;
        haveAcked.put(sender, true);
        return "pos-ack";
      }
      else
        return "neg-ack";
    }
    else if(m[0].endsWith("ack")){
      if(m[0].startsWith("pos"))
        nbrIsChild.put(sender, true);
      haveAcked.put(sender, true);
    }
    return "";
  }
  
  public String genMsg(int nbr){
    if(nbr==parent && allNbrsAcked())
      return "pos-ack";
    else if(haveSearchedUs.get(nbr) && nbr!=parent)
      return "neg-ack";
    else if(marked)
      return "search";
    else
      return "";
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
    
   
