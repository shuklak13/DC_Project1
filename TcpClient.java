import java.io.*;
import java.net.*;

public class TcpClient {

	public void go() {
	
		String message;
		BufferedReader reader = null;
		PrintWriter writer = null;

		try	{
		
			// Create a client socket and connect to server at 127.0.0.1 port 5000
			Socket clientSocket = new Socket("localhost",5000);
			
			/* Create BufferedReader to read messages from server. Input stream is in bytes. 
				They are converted to characters by InputStreamReader.
				Characters from the InputStreamReader are converted to buffered characters by BufferedReader.
				This is done for efficiency purpose.
			*/
			reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			// PrintWriter is a bridge between character data and the socket's low-level output stream
			writer = new PrintWriter(clientSocket.getOutputStream(), true);
			
		} catch(IOException ex) {
			ex.printStackTrace();
		}

		try {
			writer.println("Hello from Client");
			message = reader.readLine();
			System.out.println(message);
		} catch(IOException e) {
			System.out.println("Read failed");
			System.exit(100);
		}
	}

	public static void main(String args[]) {
		TcpClient SampleClientObj = new TcpClient();
		SampleClientObj.go();
	}
}