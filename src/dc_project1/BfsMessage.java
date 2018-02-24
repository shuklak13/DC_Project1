package dc_project1;

class BfsMessage{
  
    String type;
    int senderUID;
    int leader; //used for broadcasting leader after termination of Peleg

    public BfsMessage(String t, int s, int l){
      type = t;
      senderUID = s;
      leader = l;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("BFS ");
        sb.append(type).append(" ");
        sb.append(senderUID).append(" ");
        sb.append(leader).append(" ");
        return sb.toString();
    }

    public static BfsMessage toBfsMsg(String rcvd_msg){
      String[] parsed_msg = rcvd_msg.split("\\s+");
      return new BfsMessage(parsed_msg[1],
                            Integer.parseInt(parsed_msg[2]),
                            Integer.parseInt(parsed_msg[3]));
    }
}