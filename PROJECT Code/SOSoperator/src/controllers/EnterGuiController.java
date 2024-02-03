package controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class EnterGuiController {

		private String ip;
		private  Stage Stage;
      
	    @FXML
	    private Label enterBtn;

	    @FXML
	    private Label errorMsg;

	    @FXML
	    private Label exitBtn;

	    @FXML
	    private TextField ipTxt;

	    @FXML
		void closeApplication(MouseEvent event) {
			System.exit(0);
		}

	    @FXML
	    void enterApplication(MouseEvent event) 
	    {
	    	if(!ipTxt.getText().equals("192.168.1.24"))
	    	{

	    		Alert popAlert = new Alert(AlertType.INFORMATION);
	    		popAlert.setTitle("");
	    		popAlert.setHeaderText("");
	    		popAlert.setContentText("Please enter a valid IP ...");
	    		popAlert.showAndWait();
	    		ipTxt.setText("");
	    	}
	    	else
	    	{
	    		ip=ipTxt.getText();
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
	    	
	    }
	    
	    public void StageSeter(Stage Stage) {
        	this.Stage=Stage;
		}

	
	

}
