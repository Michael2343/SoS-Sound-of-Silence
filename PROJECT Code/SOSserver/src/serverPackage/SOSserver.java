package serverPackage;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.lang.*;
class SOSserver
{
	
	public static void main(String[] args) throws IOException
	{  
		//Server require its own socket
		ServerSocket s1 = new ServerSocket(5555);
    	mysqlConnection.setConnection("88888888");
		while(true){
			//normal socket is required to accept the incoming request to socket s1
			Socket ss = s1.accept();
			portThread p = new portThread(ss); 
			p.start();
		}
	}	
}
	
