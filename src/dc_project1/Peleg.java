
package dc_project1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringJoiner;

public class Peleg {
    int maxUid = 0;
    int maxDist=0;
    int d1=0;
    int d2=-1; 
    int d3=-1;
    int roundNo=0;
    int countRecMsgs = 0;
    int myUid = 0;
    ArrayList<String> buffer;
    HashMap<Integer, Boolean> rcvdFromNbr;
    int leader = -1;  // -1 indicates ongoing; else, Peleg has terminated
    Node owner;
    
    public Peleg(int[] neighbors, Node owner)
    {
        this.rcvdFromNbr = new HashMap<>();
        buffer  = new ArrayList<String>();
        this.owner = owner;
        this.maxUid = owner.uid;
        this.myUid = owner.uid;
        this.countRecMsgs =0;
        for (int i=0; i<neighbors.length; i++)
            this.rcvdFromNbr.put(neighbors[i], false);
    }
    
    private void handleLeaderElection(int newLeader, int sender){
      if(leader!=-1 && leader!=newLeader)
        System.out.println("ERROR: node " + myUid + " thinks that leader is " + leader
        + ", but it received a message from node " + sender + " that node " + newLeader + " is leader");
      if(leader==-1)
        owner.writeToLog("I HAVE SELECTED " + newLeader + " TO BE THE NEW LEADER");
      leader = newLeader;
    }
    
    /**
     * @return if algo ongoing, -1; if algo terminated, leader's uid
     */
    public int handleMsg(String m){
        PelegMessage pmsg = PelegMessage.toPelegMsg(m);
        
        if(pmsg.leader!=-1)
          handleLeaderElection(pmsg.leader, pmsg.senderUID);
        if(leader!=-1)
          return leader;
        
        owner.writeToLog(constructLogMsg_Receive(pmsg));
        
      synchronized(this){
        if ((roundNo <= pmsg.round) && (!rcvdFromNbr.get(pmsg.senderUID))){ 
            countRecMsgs+=1;
            rcvdFromNbr.put(pmsg.senderUID,true);
            if (pmsg.maxUID > maxUid){
                maxDist=pmsg.dist+1;
                maxUid =pmsg.maxUID;
            }
            else if (pmsg.maxUID == maxUid)
                maxDist=Math.max(maxDist,pmsg.dist);
            
            if (countRecMsgs == rcvdFromNbr.size()){
                countRecMsgs=0;
                for (int neighbor: rcvdFromNbr.keySet())
                    rcvdFromNbr.put(neighbor, false);
                roundNo++;
                if(myUid==maxUid && maxDist==d2 && maxDist==d3){
                  System.out.println("Leader is: " + String.valueOf(myUid));
                  leader = maxUid;
                  return leader;
                }
                d3=d2;
                d2=maxDist;
                while(buffer.size()>0)
                    handleMsg(buffer.remove(0));
            }
        }
        else if (roundNo < pmsg.round)
            buffer.add(m);
      }
      return -1;
    }
    
    public String constructLogMsg_Receive(PelegMessage pmsg){
      StringJoiner sj = new StringJoiner("\t");
        sj.add("(Peleg) I receive: ");
        sj.add("My Round: " + roundNo);
        sj.add("Sender's Round: " + pmsg.round);
        sj.add("\nSender: " + pmsg.senderUID);
        sj.add("Received from nodes: " + rcvdFromNbr.toString());
        sj.add("Ctr: " + countRecMsgs);
        //sj.add("Size, Ctr: " + rcvdFromNbr.size() + " " + countRecMsgs);
      return sj.toString();
    }
    
    public String constructLogMsg_Send(PelegMessage pmsg, String hostname, int port){
      StringJoiner sj = new StringJoiner("\t");
        sj.add("(Peleg) I send the following to " + hostname + ":"+port);
        sj.add("MaxUID: " + pmsg.maxUID);
        sj.add("Dist: " + pmsg.dist);
        sj.add("Leader: " + pmsg.leader);
        sj.add("Round: " + pmsg.round);
        sj.add("Sender: " + pmsg.senderUID);
      return sj.toString();
    }
    
    public PelegMessage genMsg(){
        return new PelegMessage(maxUid, maxDist, leader, roundNo, myUid);
    }    
}
