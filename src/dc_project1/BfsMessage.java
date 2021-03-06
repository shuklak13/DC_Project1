package dc_project1;

import java.util.StringJoiner;

class BfsMessage{
  
    String type;
    int senderUID;
    int leader; //used for broadcasting leader after termination of Peleg
    int height, maxHeight, maxDegree;
    int round;

    public BfsMessage(String t, int s, int l, int h, int mh, int rnd, int md){
      type = t;
      senderUID = s;
      leader = l;
      height = h;
      maxHeight = mh;
      maxDegree = md;
      round = rnd;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("BFS ");
        sb.append(type).append(" ");
        sb.append(senderUID).append(" ");
        sb.append(leader).append(" ");
        sb.append(height).append(" ");
        sb.append(maxHeight).append(" ");
        sb.append(round).append(" ");
        sb.append(maxDegree).append(" ");
        return sb.toString();
    }
    
    public String toReadableString(){
      StringJoiner sj = new StringJoiner("\t");
        sj.add("Sender: " + senderUID);
        sj.add("Message Type: " + type);
        sj.add("Height: " + height);
        sj.add("Max Height: " + maxHeight);
        sj.add("Max Degree: " + maxDegree);
        sj.add("Round: " + round);
      return sj.toString();
    }

    public static BfsMessage toBfsMsg(String rcvd_msg){
      String[] parsed_msg = rcvd_msg.split("\\s+");
//        System.out.println();
//        for(String m: parsed_msg)
//          System.out.print(m+" ");
//        System.out.println();
      return new BfsMessage(parsed_msg[1],
                            Integer.parseInt(parsed_msg[2]),
                            Integer.parseInt(parsed_msg[3]),
                            Integer.parseInt(parsed_msg[4]),
                            Integer.parseInt(parsed_msg[5]),
                            Integer.parseInt(parsed_msg[6]),
                            Integer.parseInt(parsed_msg[7]));
    }
}