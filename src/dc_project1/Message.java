package dc_project1;

class Message{
    String text;
    int round;
    String sender, receiver;

    public Message(String s, String r, string t, int rnd){
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
}