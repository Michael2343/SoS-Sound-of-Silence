package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.ResourceBundle;

import Client.SOSclient;
import entities.Log;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class IncidentLogController implements Initializable {
    @FXML
    private TableView<Log> approvalTable;

    @FXML
    private Text headLabel;

    @FXML
    private ImageView icon;

    @FXML
    private TableColumn<Log, String> id;

    @FXML
    private TableColumn<Log,String> incident_Number;

    @FXML
    private Text instructions;

    @FXML
    private TableColumn<Log, String> startTime;

    @FXML
    private TableColumn<Log, String> status;

	private String ID;
	private String INCIDENT_NUMBER;
	private String START_TIME;
	private String STATUS;
	private String ip;
	private Stage Stage;
	private SOSclient client;
	private ArrayList<Log> list =new ArrayList<>();
	private ObservableList<Log>obList= FXCollections.observableArrayList();
	
	public void runIt()
	{
		Thread t1=new Thread(new Runnable() 
		{
			BufferedReader in=client.getIn();
			PrintWriter out=client.getOut();
			String str;
			String substring;
			@Override
			public void run()
			{
				while (true)
				{
					out.println("0BBLOG");
				try {
					str=in.readLine();
					ArrayList<Log > list = new ArrayList<Log>();

					if (str.contains("LOG ")) {
						str=str.substring(4);
						while(str.indexOf("|")!=-1)
						{
							substring=str.substring(0,str.indexOf("|"));
							str=str.substring(str.indexOf("|")+1);
							
							INCIDENT_NUMBER=substring.substring(0,substring.indexOf(" "));
							substring=substring.substring(substring.indexOf(" ")+1);
							ID=substring.substring(0,substring.indexOf(" "));
							substring=substring.substring(substring.indexOf(" ")+1);
							START_TIME=substring.substring(0,substring.indexOf(" "));
							substring=substring.substring(substring.indexOf(" ")+1);
							START_TIME+=substring.substring(0,substring.indexOf(" "));
							substring=substring.substring(substring.indexOf(" ")+1);
							STATUS=substring.substring(0,substring.indexOf(" "));
							substring=substring.substring(substring.indexOf(" ")+1);
							list.add(new Log(INCIDENT_NUMBER, ID, START_TIME, STATUS));
						}
					}
					getLog(list);
					approvalTable.getItems().clear();
					setTable();
					Thread.sleep(1200);
					
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}});
		t1.start();
		
		
	}
	public void StageAndIpSeter(String ip,Stage Stage) 
	{
		client =new SOSclient(ip);
		this.ip=ip;
		this.Stage=Stage;
		runIt();
	}

    @FXML
    void copyTableData(MouseEvent event) {
    	
    	if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
    		String idFromTable = String.valueOf(approvalTable.getSelectionModel().getSelectedItem().getID());
    		String incidentNumberFromTable = String.valueOf(approvalTable.getSelectionModel().getSelectedItem().getINCIDENT_NUMBER());
    		//client.getOut().println("0BBUSER"+incidentNumberFromTable);
    		client.getOut().println("0BBUSER"+idFromTable);
    		
    		AnchorPane root;
    		IncidentDataController controller = new IncidentDataController();
    		try {
    			FXMLLoader loader = new FXMLLoader();
    			loader.setLocation(getClass().getResource("/gui/IncidentData.fxml"));
    			root =  loader.load();
    			controller = loader.getController();
    			controller.StageSeter(Stage,client,ip);
        		controller.initController(client.getIn().readLine(),incidentNumberFromTable);
    			
    		} catch (IOException e) {
    			e.printStackTrace();
    			return;
    		}
    		
    		Scene s = new Scene(root);
    		Stage.setScene(s);
    		Stage.setTitle("Data");
    		Stage.show();
    		}
    }
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	private void getLog(ArrayList<Log> tempList)
	{
		obList= FXCollections.observableArrayList();
		for (Log tempLog: tempList) {
			Log log=new Log(tempLog.getINCIDENT_NUMBER(),tempLog.getID(),tempLog.getSTART_TIME(),tempLog.getSTATUS());
			obList.add(log);
		}
	}
	private void setTable() 
	{
		incident_Number.setCellValueFactory(new PropertyValueFactory<>("INCIDENT_NUMBER"));
		id.setCellValueFactory(new PropertyValueFactory<>("ID"));
		startTime.setCellValueFactory(new PropertyValueFactory<>("START_TIME"));
		status.setCellValueFactory(new PropertyValueFactory<>("STATUS"));
		approvalTable.setItems(obList);
		approvalTable.setVisible(true);
		approvalTable.setEditable(true);
		
	}
	
}
