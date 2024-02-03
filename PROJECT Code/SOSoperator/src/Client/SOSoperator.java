package Client;

import java.io.IOException;
import controllers.EnterGuiController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SOSoperator extends Application {
	private Stage Stage;
	@Override
	public void start(Stage Stage)
	{
		AnchorPane root;
		this.Stage=Stage;
		EnterGuiController controller = new EnterGuiController();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/EnterGUI.fxml"));
			root =  loader.load();
			controller = loader.getController();
			controller.StageSeter(Stage);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Scene s = new Scene(root);
		Stage.setScene(s);
		Stage.setTitle("Home");
		Stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	
	}

}
