package graphics;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class TransferBox extends Pane {
	private TransferController control;
	
	public TransferBox(String fileName,String peerName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/Transfer.fxml"));
			GridPane pane = loader.load();
			control = (TransferController)loader.getController();
			
			this.getChildren().add(pane);
			control.setLabels(fileName);
			control.setName(peerName);
			System.out.println("New tranfer box created!");
		}catch(IOException ex) {}
	}
}
