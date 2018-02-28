package dc_project1;

import java.net.*;
import java.io.*;
import java.lang.*;

public class ClientManager implements Runnable {
	
	private Socket client;
    Node owner;

	public ClientManager(Socket client, Node owner) {
		this.client = client;
        this.owner = owner;
	}

	public ClientManager(Socket client) {
		this.client = client;
	}

	public void run() {
		try {
          String line;
          BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
          while (!owner.terminated && (line = in.readLine()) != null){
            handleMsg(line);
            if(owner.b1!=null && owner.isLeader() && owner.b1.allNbrsAcked()){
              owner.terminated = true; 
              System.out.println(owner.b1.terminateString());
//              System.exit(0);
            }
          }
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
    
    public void handleMsg(String m){
      if (owner.leader == -1)
          owner.leader = owner.p1.handleMsg(m);
      if (owner.leader != -1){
          if(owner.b1==null)
            owner.initiateBfs();
          owner.b1.handleMsg(m);
      }
    }
}