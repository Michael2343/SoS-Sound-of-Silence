package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;



public class SOSclient 
{
	private String serverName;//192.168.74.63
	
	BufferedReader in=null;
	PrintWriter out=null;
	public SOSclient(String serverName ) {
		this.serverName=serverName;
		int serverPortNumber = 5555;
		
	
		try {
			@SuppressWarnings("resource")
			Socket clientToServer = new Socket(serverName,serverPortNumber);
			in = new BufferedReader(new InputStreamReader(clientToServer.getInputStream()));
			out = new PrintWriter(clientToServer.getOutputStream(), true);
		} catch (IOException e) {e.printStackTrace();}
		

	
	}
	public BufferedReader getIn ()
	{
		return in;
	}
	public PrintWriter getOut()
	{
		return out;
	}

}