
package dc_project1;

import java.util.ArrayList;
import java.util.HashMap;

public class peleg {
        
    int maxUid = 0;
    int d=0;
    int d1=0;
    int d2=0; 
    int d3=0;
    int roundNo=0;
    int countRecMsgs = 0;
    String buffer[];
    HashMap<Integer, Boolean> rcvdFromNbr;
    
    public peleg(int[] neighbors)
    {
        this.rcvdFromNbr = new HashMap<>();
        buffer  = new String[neighbors.length];
    }
    
    public void handleMsg(PelegMessage msg)
    {
      
    }
    
    public String genMsg()
    {
        
    }
    
    
    
}
