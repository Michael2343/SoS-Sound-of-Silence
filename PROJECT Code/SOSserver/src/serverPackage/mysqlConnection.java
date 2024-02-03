package serverPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import entities.Log;

/**
 * MySQL Connection class. Using a single connector to the DB.
 *
 * @author Eden Ben Abu
 * @version February 2022 (1.0)
 */
public class mysqlConnection {

	/**
	 * Default arguments for connection received from configuration file.
	 */
	private static Connection conn;

	/**
	 * Set connection
	 * 
	 * @param args
	 */

	public static void setConnection(String mySQLpassword) {

		 try 
			{
		        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		        System.out.println("Driver definition succeed");
		    } catch (Exception ex) {
		    	/* handle the error*/
		    	 System.out.println("Driver definition failed");
		    	 }
		    
		    try 
		    {
		        conn = DriverManager.getConnection("jdbc:mysql://localhost/sos?useLegacyDatetimeCode=false&serverTimezone=Israel","root",mySQLpassword);
		        System.out.println("SQL connection succeed");
		 	} catch (SQLException ex) 
		 	    {/* handle any errors*/
		        System.out.println("SQLException: " + ex.getMessage());
		        System.out.println("SQLState: " + ex.getSQLState());
		        System.out.println("VendorError: " + ex.getErrorCode());
		        }
	}
	
	//creates a new incident on the DB log.
	public static void startSOS(String userId)
	{
		PreparedStatement stmt;
		try {
			String query = "INSERT INTO sos.incident_log (Id,StartTime,Status) values (?,CURRENT_TIMESTAMP(),'Active')";
			stmt = conn.prepareStatement(query);
			stmt.setString(1,userId);
			stmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
		}
	}
	

	// checks if theres an open SOS from the last 5 minutes, returns TRUE if yes
	public static boolean checkOpenSOS(String userId)
	{
		PreparedStatement stmt;
		try {
			String query = "SELECT * FROM sos.incident_log WHERE Id = ? AND status = 'Active'"
					+ "AND TIME_TO_SEC(TIMEDIFF(NOW(),StartTime))<360"; // check for Active incident from last 5min
			stmt = conn.prepareStatement(query);
			stmt.setString(1,userId);
			ResultSet rSet = stmt.executeQuery();
			if(rSet.next())
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
		}
		return false;
	}
	
	public static boolean checkOpenIncident(String userId, String type)
	{
		PreparedStatement stmt;
		try {
			String query = "SELECT * FROM sos.incident_log WHERE Id = ? "
					+ "AND TIME_TO_SEC(TIMEDIFF(NOW(),StartTime))<360 AND "; // check for Active incident from last 5min
			stmt = conn.prepareStatement(query);
			stmt.setString(1,userId);
			ResultSet rSet = stmt.executeQuery();
			if(rSet.next())
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
		}
		return false;
	}

	public static void abortRecentSOS(String userId) {
		PreparedStatement stmt;
		String incidentId="";
		try {
			String query = "SELECT * FROM sos.incident_log WHERE Id = ? AND "
					+  "TIME_TO_SEC(TIMEDIFF(NOW(),StartTime))<360"; // check for incidents from last 5min
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, Integer.parseInt(userId));;
			ResultSet rSet = stmt.executeQuery();
			if(rSet.next())
				incidentId=rSet.getString(2);// get incident id
			else// no open incident to abort.
				return;
			query = "UPDATE sos.incident_log SET Status='Abort'  WHERE Id = ? AND "
					+ " IncidentId=?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, userId);
			stmt.setString(2, incidentId);
			stmt.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void createMadaIncident(String userId) {
		checkOpenIncident(userId, "mada");
		
	}

	public static String getIncidents() {
		PreparedStatement stmt;
		Log log;
		String logList= "LOG ";
		try {
			String query = "SELECT * FROM sos.incident_log "
					+ "WHERE Status='Active' OR Status='SOS' OR Status='Processed'"; 
			stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				log = new Log(String.valueOf(rs.getInt(1)), 
						String.valueOf(rs.getInt(2)), 
						rs.getString(3), rs.getString(4));
				logList = logList.concat(log.toString());
				logList = logList.concat("|");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return logList;
	}

	public static String getUserData(String substring) {
			PreparedStatement stmt;
			String dataList= "USER ";
			try {
				String query = "SELECT * FROM sos.user WHERE Id=?"; 
				stmt = conn.prepareStatement(query);
				stmt.setInt(1, Integer.parseInt(substring));
				ResultSet rs = stmt.executeQuery();
				while(rs.next()) {
					dataList=dataList.concat(String.valueOf(rs.getInt(1)));
					dataList=dataList.concat(" ");
					dataList=dataList.concat(rs.getString(2));
					dataList=dataList.concat(" ");
					dataList=dataList.concat(rs.getString(3));
					dataList=dataList.concat(" ");
					dataList=dataList.concat(rs.getString(4));
					dataList=dataList.concat(" ");
					dataList=dataList.concat(rs.getString(5));
					dataList=dataList.concat(" ");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return dataList;
		}
	
	public static String updateUserData(String substring) {
		PreparedStatement stmt;
		String dataList= "USER ";
		String id, status, incNumber;
		String[] lst = substring.split(" ");
		id = lst[0];
		status = lst[1];
		incNumber = lst[2];
		try {
			String query = "UPDATE sos.incident_log SET Status = ? "
					+ "WHERE IncidentId = ?"; 
			stmt = conn.prepareStatement(query);
			stmt.setString(1,status );
			stmt.setInt(2, Integer.parseInt(incNumber));
			int rs = stmt.executeUpdate();
//			while(rs.next()) {
//				log = new Log(String.valueOf(rs.getInt(1)), 
//						String.valueOf(rs.getInt(2)), 
//						rs.getString(3), rs.getString(4));
//				logList = logList.concat(log.toString());
//				logList = logList.concat("|");
//			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id + " Status Updated Successfully";
	}
}
	
