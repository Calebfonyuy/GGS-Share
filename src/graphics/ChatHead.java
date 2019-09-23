package graphics;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

public class ChatHead extends Pane{
	private ChatHeadController controller;
	
	public ChatHead(String name) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/ChatHead.fxml"));
		Pane pane;
		try {
			pane = loader.load();
			controller =loader.getController();
			controller.setName(name);
			this.getChildren().add(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
