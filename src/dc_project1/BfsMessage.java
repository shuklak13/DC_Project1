package dc_project1;

class BfsMessage{
  
    String type;
    int senderUID;

    public BfsMessage(String t, int s){
      type = t;
      senderUID = s;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(type).append(" ");
        sb.append(senderUID).append(" ");
        return sb.toString();
    }

    public static BfsMessage toBfsMsg(String rcvd_msg){
      String[] parsed_msg = rcvd_msg.split("\\s+");
      return new BfsMessage(parsed_msg[0], Integer.parseInt(parsed_msg[1]));
      }
}