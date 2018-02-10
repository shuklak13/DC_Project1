
package dc_project1;

import java.util.ArrayList;
import java.util.HashMap;

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
    
    public void handleMsg(String m)
    {
        PelegMessage pmsg = PelegMessage.toPelegMsg(m);
        if(myUid==5){
          System.out.println("\nNEW NODE: "+myUid);
          System.out.println("Message Round: " + pmsg.round);
          System.out.println("Neighbors: ");
          rcvdFromNbr.keySet().forEach(node -> System.out.print(node + " "));
          System.out.println("\nSender: " + pmsg.senderUID);
          System.out.println("Received from nodes: " + rcvdFromNbr.toString());
          System.out.println("Size, Ctr: " + rcvdFromNbr.size() + " " + countRecMsgs);
        }
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
            {
                maxDist=Math.max(maxDist,pmsg.dist);
            }
            
            if (countRecMsgs == rcvdFromNbr.size())
            {
                countRecMsgs=0;
                for(int neighbor: rcvdFromNbr.keySet())
                {
                    rcvdFromNbr.put(neighbor, false);
                }
                roundNo++;
                if(myUid==maxUid && maxDist==d2 && maxDist==d3){
                    terminate();
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
    }
    
    public String genMsg()
    {
        PelegMessage pmsg = new PelegMessage(maxUid, maxDist, "NA", roundNo, myUid);
        return pmsg.toString();
    }    
    
    public void terminate()
    {
        System.out.println("Terminate");
    }
}
