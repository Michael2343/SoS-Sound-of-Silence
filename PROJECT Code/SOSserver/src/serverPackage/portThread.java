package serverPackage;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import entities.*;
import javafx.collections.ObservableList;

// extends Thread gives an ability to be a thread
public class portThread extends Thread
{
	private Socket ss; 
	
	//Constructor
	public portThread(Socket s){
		this.ss = s;
	}
	
	@Override
	public void run(){
	    try {
			System.out.println("ClientConnected");
	        PrintWriter out = new PrintWriter(ss.getOutputStream(), true);
	        BufferedReader in = new BufferedReader(new InputStreamReader(ss.getInputStream()));
	        String inputLine;
	        
	        while ((inputLine = in.readLine()) != null) {
    			System.out.println("Received CMD: " + inputLine);
	        	if(inputLine.contains("0AA"))// watch commands
	        	{
	        		String userId = inputLine.substring(3,6);
	        		if(inputLine.contains("SOS")){
	        			if(!mysqlConnection.checkOpenSOS(userId))// if no active SOS opened on the last 5 minutes:
	        				mysqlConnection.startSOS(inputLine.substring(3,6)); // get user id
	        		}
	        		else if(inputLine.contains("ABORT")){
	        			mysqlConnection.abortRecentSOS(userId);
	        			
	        		}
	        		else if(inputLine.contains("MADA")){
	        			mysqlConnection.createMadaIncident(userId);
	        		}
	        		else if(inputLine.contains("POLICE")){
	        			
	        		}
	        		else if(inputLine.contains("ESH")){
	        			
	        		}
	        		else if(inputLine.contains("Q1")){
	        			
	        		}
	        		else if(inputLine.contains("Q2")){
	        			
	        		}
	        		else if(inputLine.contains("Q3")){
	        			
	        		}
	        		else if(inputLine.contains("Q4")){
	        			
	        		}
	        	}
	        	else if(inputLine.contains("0BB"))// client commands
	        	{
	        		if(inputLine.contains("LOG")){
	        			String logList = mysqlConnection.getIncidents();
	        			out.println(logList);
	        		}
	        		else if (inputLine.contains("USER"))
	        		{
	        			
	        			String logList = mysqlConnection.getUserData(inputLine.substring(7));
	        			out.println(logList);
	        		}
	        		else if (inputLine.contains("UPDAT"))
	        		{
	        			
	        			String logList = mysqlConnection.updateUserData(inputLine.substring(9));
	        			out.println(logList);
	        		}
	        	}
	        	else if ("0".equals(inputLine)) {
	                out.println("good bye");
	                System.out.println("Client request to close connection is successful.");
	                break;
	             }
	        }
	           
	    } catch (Exception e) {
		// TODO Auto-generated catch block
	    	//if()
	    	//{
	    		System.out.println("Connection Closed");
	    	//}
		//e.printStackTrace();
		e.getClass();
		}
    }
}