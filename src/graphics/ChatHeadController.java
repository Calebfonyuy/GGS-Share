package graphics;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ChatHeadController {

    @FXML
    private Label peerName;
    
    public void setName(String name) {
    	peerName.setText(name);
    }
}