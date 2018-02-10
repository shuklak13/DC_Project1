package dc_project1;

class Message{
    String text;
    int round;
    String sender, receiver;

    public Message(String s, String r, String t, int rnd){
        sender = s;
        receiver = r;
        round = rnd;
        text = t;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(sender+" ");
        sb.append(receiver+" ");
        sb.append(round+";");
        sb.append(text);
        return sb.toString();
    }

    public static String fromString(String rcvd_msg){
        String[] parsed_msg = rcvd_msg.split("\\s+");
        
        return new Message(Integer.parseInt(parsed_msg[0]), Integer.parseInt(parsed_msg[1]), parsed_msg[2], Integer.parseInt(parsed_msg[3]));
    }
}