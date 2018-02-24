package dc_project1;

class PelegMessage{
    int leader;
    int round;
    int maxUID;
    int dist;
    int senderUID;

    public PelegMessage(int maxUid, int d, int l, int rnd, int uid){
        maxUID = maxUid;
        dist = d;
        round = rnd;
        leader = l;  
        senderUID = uid;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Peleg ");
        sb.append(maxUID).append(" ");
        sb.append(dist).append(" ");
        sb.append(leader).append(" ");
        sb.append(round).append(" ");
        sb.append(senderUID).append(" ");
        return sb.toString();
    }

    public static PelegMessage toPelegMsg(String rcvd_msg){
        String[] parsed_msg = rcvd_msg.split("\\s+");
//        System.out.println();
//        for(String m: parsed_msg)
//          System.out.print(m);
//        System.out.println();
        return new PelegMessage(Integer.parseInt(parsed_msg[1]),
                                Integer.parseInt(parsed_msg[2]),
                                Integer.parseInt(parsed_msg[3]),
                                Integer.parseInt(parsed_msg[4]),
                                Integer.parseInt(parsed_msg[5]));
    }
}