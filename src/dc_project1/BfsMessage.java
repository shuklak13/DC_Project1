package dc_project1;

class BfsMessage{

    public BfsMessage(){
    }

    @Override
    public String toString(){
      return "";
    }

    public static BfsMessage toBfsMsg(String rcvd_msg){
      return new BfsMessage();
    }
}