package dc_project1;

import dc_project1.Peleg;
import java.net.*;
import java.io.*;
import java.lang.*;

public class ClientManager implements Runnable {
	
	private Socket client;
    Peleg p1;
    BufferedWriter log;
    String algo = "peleg";

	public ClientManager(Socket client, Peleg p1, BufferedWriter log) {
		this.client = client;
        this.p1 = p1;
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
                writeToLog(handleMsg(line));
            }
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

    public String handleMsg(String m)
    {
        if (algo.equals("peleg"))
            return p1.handleMsg(m);
        //else if (algo.equals("bfs"))
            //return b1.handleMsg(m);
        else
          return "";
    }
    public void writeToLog(String s) throws IOException{
      //log.append(Long.toString(System.currentTimeMillis()) + "\t" + s);
      log.append(s);
      log.newLine();
    }
}