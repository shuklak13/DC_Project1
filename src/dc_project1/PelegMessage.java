package dc_project1;

class PelegMessage{
    String text;
    int round;
    int maxUID;
    int dist;
    int senderUID;

    public PelegMessage(int maxUid, int d, String t, int rnd, int uid){
        maxUID = maxUid;
        dist = d;
        round = rnd;
        text = t;  
        senderUID = uid;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(maxUID).append(" ");
        sb.append(dist).append(" ");
        sb.append(text).append(" ");
        sb.append(round).append(" ");
        sb.append(senderUID).append(" ");
        return sb.toString();
    }

    public static PelegMessage toPelegMsg(String rcvd_msg){
        String[] parsed_msg = rcvd_msg.split("\\s+");
        
        return new PelegMessage(Integer.parseInt(parsed_msg[0]),
                                Integer.parseInt(parsed_msg[1]),
                                parsed_msg[2],
                                Integer.parseInt(parsed_msg[3]),
                                Integer.parseInt(parsed_msg[4]));
    }
}