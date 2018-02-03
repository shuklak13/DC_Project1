import java.net.*;
import java.io.*;
import java.lang.*;

public class ClientManager implements Runnable {
	
	private Socket client;

	public ClientManager(Socket client) {
		this.client = client;
	}

	public void run() {
		String line;
		BufferedReader in = null;
		PrintWriter out = null;

		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);
			line = in.readLine();

			while(line != null) {
			
					System.out.println(line);
					// send data back to the client
					String line2 = "Hello From Server";
					out.println(line2);
					System.out.println("Message sent to client");
					line = in.readLine();
			}


		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}