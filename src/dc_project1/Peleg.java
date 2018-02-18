
package dc_project1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringJoiner;

public class Peleg {
    int maxUid = 0;
    int maxDist=0;
    int d1=0;
    int d2=0; 
    int d3=0;
    int roundNo=0;
    int countRecMsgs = 0;
    int myUid = 0;
    ArrayList<String> buffer;
    HashMap<Integer, Boolean> rcvdFromNbr;
    
    public Peleg(int[] neighbors, int uid)
    {
        this.rcvdFromNbr = new HashMap<>();
        buffer  = new ArrayList<String>();
        this.maxUid = uid;
        this.myUid = uid;
        this.countRecMsgs =0;
        for (int i=0; i<neighbors.length; i++)
        {
            this.rcvdFromNbr.put(neighbors[i], false);
        }
    }
    
    public String handleMsg(String m)
    {
        PelegMessage pmsg = PelegMessage.toPelegMsg(m);
        StringJoiner sj = new StringJoiner("\t");
        sj.add("My Round: " + roundNo);
        sj.add("Sender's Round: " + pmsg.round);
        /*sj.add("Neighbors: ");
        for(int id: rcvdFromNbr.keySet())
          sj.add(Integer.toString(id)+" ");*/
        sj.add("\nSender: " + pmsg.senderUID);
        sj.add("Received from nodes: " + rcvdFromNbr.toString());
        sj.add("Ctr: " + countRecMsgs);
        //sj.add("Size, Ctr: " + rcvdFromNbr.size() + " " + countRecMsgs);
        if ((roundNo <= pmsg.round) && (!rcvdFromNbr.get(pmsg.senderUID)))
        { 
            countRecMsgs+=1;
            rcvdFromNbr.put(pmsg.senderUID,true);
            if (pmsg.maxUID > maxUid)
            {
                maxDist=pmsg.dist+1;
                maxUid =pmsg.maxUID;
            }
            else if (pmsg.maxUID == maxUid)
                maxDist=Math.max(maxDist,pmsg.dist);
            
            if (countRecMsgs == rcvdFromNbr.size())
            {
                countRecMsgs=0;
                for(int neighbor: rcvdFromNbr.keySet())
                {
                    rcvdFromNbr.put(neighbor, false);
                }
                roundNo++;
                if(myUid==maxUid && maxDist==d2 && maxDist==d3){
                    sj.add(terminate());
                }
                d3=d2;
                d2=maxDist;
                                
                while(buffer.size()>0)
                {
                    String buffMsg = buffer.remove(0);
                    handleMsg(buffMsg);
                }
            }
        }
        else if (roundNo < pmsg.round)
        {
            buffer.add(m);
        }
      return sj.toString();
    }
    
    public String genMsg()
    {
        PelegMessage pmsg = new PelegMessage(maxUid, maxDist, "NA", roundNo, myUid);
        return pmsg.toString();
    }    
    
    public String terminate()
    {
        return "Terminate";
    }
}