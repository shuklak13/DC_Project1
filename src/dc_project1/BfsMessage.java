package dc_project1;

import java.util.StringJoiner;

class BfsMessage{
  
    String type;
    int senderUID;
    int leader; //used for broadcasting leader after termination of Peleg
    int degree, maxDegree;
    int round;

    public BfsMessage(String t, int s, int l, int d, int md, int rnd){
      type = t;
      senderUID = s;
      leader = l;
      degree = d;
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
        sb.append(degree).append(" ");
        sb.append(maxDegree).append(" ");
        sb.append(round).append(" ");
        return sb.toString();
    }
    
    public String toReadableString(){
      StringJoiner sj = new StringJoiner("\t");
        sj.add("\nSender: " + senderUID);
        sj.add("Message Type: " + type);
        sj.add("Degree: " + degree);
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
                            Integer.parseInt(parsed_msg[6]));
    }
}