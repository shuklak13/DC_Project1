import java.io.*;
import java.net.*;

public class TcpServer {

	public void go() {
	
		String message="Hello from server";
		
		try	{
			// Create a server socket at port 5000
			ServerSocket serverSock = new ServerSocket(5000);
			
			// Server goes into a permanent loop accepting connections from clients			
			while(true)
			{
				// Listens for a connection to be made to this socket and accepts it
				// The method blocks until a connection is made
				Socket sock = serverSock.accept();

				/* Create BufferedReader to read messages from server. Input stream is in bytes. 
					They are converted to characters by InputStreamReader
					This is done for efficiency purposes.
					Characters from the InputStreamReader are converted to buffered characters by BufferedReader.
					This is done for efficiency purpose.
				*/
				BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				
				// PrintWriter is a bridge between character data and the socket's low-level output stream
				PrintWriter writer = new PrintWriter(sock.getOutputStream(), true);

				//The method readLine is blocked until a message is received 
				String cmessage = reader.readLine();
				System.out.println(cmessage);

				//PrintWriter is a bridge between character data and the socket's low-level output stream
				writer.println(message);
				//writer.close();
				System.out.println("Msg sent to client");
			}
			
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String args[]) {
		
		TcpServer SampleServerObj = new TcpServer();
		SampleServerObj.go();
	}

}