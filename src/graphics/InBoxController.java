package graphics;

import java.net.URL;
import java.util.ResourceBundle;

import application.PeerManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class InBoxController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<String> history;

    @FXML
    private Button clear;

    @FXML
    void initialize() {
    	history.setItems(PeerManager.fileHistory);
    }
    
    @FXML
    public void clearList(ActionEvent event) {
    	Platform.runLater(new Runnable() {
    		@Override
    		public void run() {
    			PeerManager.fileHistory.clear();
    		}
    	});
    }
}