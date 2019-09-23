package graphics;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class TransferController{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ProgressBar progressbar;

    @FXML
    private Label fileNameLbl;

    @FXML
    private Label peerNameLbl;

    @FXML
    private Label progressLbl;

    @FXML
    void initialize() {

    }
  

    public void setLabels(String fileName) {
    	fileNameLbl.setText(fileName);
    }
    

    public void setName(String peerName) {
    	peerNameLbl.setText(peerName);
    }
    
    @FXML
    public ProgressBar getProgressBar() {
    	return progressbar;
    }
    
    @FXML
    public Label getProgressLabel() {
    	return progressLbl;
    }
}
