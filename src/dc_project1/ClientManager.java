package dc_project1;

import java.net.*;
import java.io.*;
import java.lang.*;

public class ClientManager implements Runnable {
	
	private Socket client;
    BufferedWriter log;
    Node owner;

	public ClientManager(Socket client, Node owner, BufferedWriter log) {
		this.client = client;
        this.owner = owner;
        this.log = log;
	}

	public ClientManager(Socket client) {
		this.client = client;
	}

	public void run() {
		try {
          String line;
          BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
          while ((line = in.readLine()) != null)
            handleMsg(line);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
    
    public void handleMsg(String m){
      if (owner.leader == -1)
          owner.leader = owner.p1.handleMsg(m);
      else{
          if(owner.b1==null)
            owner.initiateBfs();
          owner.b1.handleMsg(m);
      }
    }
}