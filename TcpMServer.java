import java.io.*;
import java.net.*;
public class TcpMServer {

	public void go() {
	
		try	{
		
			//Create a server socket at port 5000
			ServerSocket serverSock = new ServerSocket(5000);
			
			//Server goes into a permanent loop accepting connections from clients			
			while(true)
			{
				//Listens for a connection to be made to this socket and accepts it
				//The method blocks until a connection is made
				ClientManager w;
				try {
					w = new ClientManager(serverSock.accept());
					Thread t = new Thread(w);
					t.start();
				} catch(IOException e) {
					System.out.println("accept failed");
					System.exit(100);
				}				
			}

		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		TcpMServer SampleServerObj = new TcpMServer();
		SampleServerObj.go();
	}

}