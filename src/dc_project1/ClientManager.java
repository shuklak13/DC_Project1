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
		String line = null;
		BufferedReader in = null;

		try {
			in=new BufferedReader(new InputStreamReader(client.getInputStream()));
            while ((line = in.readLine()) != null) {
                owner.writeToLog(owner.handleMsg(line));
            }
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}