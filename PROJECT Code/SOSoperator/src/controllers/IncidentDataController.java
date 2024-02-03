package controllers;

import java.io.IOException;
import java.util.Random;

import Client.SOSclient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class IncidentDataController {
	private Stage Stage;
    private SOSclient client;
    private String ip;
    public void StageSeter(Stage Stage,SOSclient client,String ip) {
    	this.ip=ip;
    	this.client =client;
    	this.Stage=Stage;	
    	BPMchange();
	}
    @FXML
    private Text AnswerLabel;

    @FXML
    private Text HeadLabel;

    @FXML
    private Text QuesttionLabel;

    @FXML
    private Text StatusLabel;

    @FXML
    private Text a1;

    @FXML
    private Text a2;

    @FXML
    private Text a3;

    @FXML
    private Text a4;

    @FXML
    private Text a5;

    @FXML
    private RadioButton activeRadioBtn;

    @FXML
    private Label addressLabel;

    @FXML
    private RadioButton finishactiveRadioBtn;

    @FXML
    private Label idLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private RadioButton processedc;

    @FXML
    private Text q1;

    @FXML
    private Text q2;

    @FXML
    private Text q3;

    @FXML
    private Text q4;

    @FXML
    private Text q5;

    @FXML
    private ToggleGroup radio1;
    
    @FXML
    private Text BPMText;
    
    @FXML
    private Button BuckBT;
    
    private String IncidentNumber;
    
    @FXML
    void buckndl(ActionEvent event) 
    {
		AnchorPane root;
		IncidentLogController controller = new IncidentLogController();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/IncidentLog.fxml"));
			root =  loader.load();
			controller = loader.getController();
			controller.StageAndIpSeter(ip, Stage);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Scene s = new Scene(root);
		Stage.setScene(s);
		Stage.setTitle("Log");
		Stage.show();
	
    }

    @FXML
    void setActiveStatus(ActionEvent event) {
    	client.getOut().println("0BBUPDATE"+idLabel.getText()+" Active " + IncidentNumber);
    	StatusLabel.setText("Status : Active");
    
    }

    @FXML
    void setFinishedStatus(ActionEvent event) {
    	client.getOut().println("0BBUPDATE"+idLabel.getText()+" Finish " + IncidentNumber);
    	StatusLabel.setText("Status :Finished");

    }

    @FXML
    void setProcessedStatus(ActionEvent event) {
    	client.getOut().println("0BBUPDATE"+idLabel.getText()+" Processed " + IncidentNumber);
    	StatusLabel.setText("Status :Processed");
    
    }
    
   public void initController(String str,String ICIDENT_NUMBER) {
    	System.out.println(str);
    	System.out.println(ICIDENT_NUMBER);
    	IncidentNumber = ICIDENT_NUMBER;
    	if(str.contains("USER "))
    	{
    	String substring=str.substring(5);
    	idLabel.setText(substring.substring(0,substring.indexOf(" ")));
		substring=substring.substring(substring.indexOf(" ")+1);
		String name=substring.substring(0,substring.indexOf(" "));
		substring=substring.substring(substring.indexOf(" ")+1);
		name=name+" " +substring.substring(0,substring.indexOf(" "));
		substring=substring.substring(substring.indexOf(" ")+1);
		nameLabel.setText(name);
		addressLabel.setText(substring.substring(0,substring.indexOf(" ")));
		substring=substring.substring(substring.indexOf(" ")+1);
		phoneLabel.setText(substring.substring(0,substring.indexOf(" ")));
		
		/*Thread t1 =new Thread(new Runnable() {
			
			@Override
			public void run(){
			String incidentNumber=ICIDENT_NUMBER;	
			String questionData;
			String type = "";
			String question="";
			/*	
			while(true)
			{
				try {
					Thread.sleep(1200);
					client.getOut().println("0BBINFO"+ICIDENT_NUMBER);
					questionData=client.getIn().readLine();
					incidentNumber=questionData.substring(0,substring.indexOf(" "));
					questionData=questionData.substring(substring.indexOf(" ")+1);
					type=questionData.substring(0,substring.indexOf(" "));
					questionData=questionData.substring(substring.indexOf(" ")+1);
					question=questionData.substring(0,substring.indexOf(" "));
					questionData=questionData.substring(substring.indexOf(" ")+1);
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
				switch (type)
				{
					case "1": 
					{
						
						q1.setText("q1");
						q1.setVisible(true);
						q2.setText("q2");
						q2.setVisible(true);
						q3.setText("q3");
						q3.setVisible(true);
						break;
					}
				case "2":
					{
						q1.setText("q1");
						q1.setVisible(true);
						q2.setText("q2");
						q2.setVisible(true);
						q3.setText("q3");
						q3.setVisible(true);
						break;
					}
				case "3":
					{
						q1.setText("q1");
						q1.setVisible(true);
						q2.setText("q2");
						q2.setVisible(true);
						q3.setText("q3");
						q3.setVisible(true);
						break;
					}
				}
			}
		}
		
	});*/
	//t1.start();
    }
   }
   public void BPMchange () 
   {
	   Thread t1=new Thread(new Runnable() {
		
		@Override
		public void run() {
			while(true)
			{
				try {
					Thread.sleep(1000);
					Random random = new Random();
					BPMText.setText(String.valueOf(90+(random.nextInt()%2)));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
		}
	});
	   t1.start();
   }
}
