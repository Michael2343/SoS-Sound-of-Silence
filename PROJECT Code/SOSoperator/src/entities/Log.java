package entities;

import java.io.Serializable;

public class Log implements Serializable{

	private String INCIDENT_NUMBER;
	private String ID;
	private String START_TIME;
	private String STATUS;
	
	public Log(String INCIDENT_NUMBER,String ID,String START_TIME,String STATUS) {
		this.INCIDENT_NUMBER=INCIDENT_NUMBER;
		this.ID=ID;
		this.START_TIME=START_TIME;
		this.STATUS=STATUS;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getINCIDENT_NUMBER() {
		return INCIDENT_NUMBER;
	}
	public void setINCIDENT_NUMBER(String iNCIDENT_NUMBER) {
		INCIDENT_NUMBER = iNCIDENT_NUMBER;
	}
	public String getSTART_TIME() {
		return START_TIME;
	}
	public void setSTART_TIME(String sTART_TIME) {
		START_TIME = sTART_TIME;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	
	@Override
	public String toString() {
		return INCIDENT_NUMBER + " " + ID + " " + START_TIME + " " + STATUS  + " ";
	}
}
