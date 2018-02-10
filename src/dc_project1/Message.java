package dc_project1;

class PelegMessage{
    String text;
    int round;
    int maxUID;
    int dist;

    public PelegMessage(int maxUid, int d, String t, int rnd){
        maxUID = maxUid;
        dist = d;
        round = rnd;
        text = t;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(maxUID+" ");
        sb.append(dist+" ");
        sb.append(round+";");
        sb.append(text);
        return sb.toString();
    }

    public static String toPelegMsg(String rcvd_msg){
        String[] parsed_msg = rcvd_msg.split("\\s+");
        
        return new PelegMsg(Integer.parseInt(parsed_msg[0]), Integer.parseInt(parsed_msg[1]), parsed_msg[2], Integer.parseInt(parsed_msg[3]));
    }
}