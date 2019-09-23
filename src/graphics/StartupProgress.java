package graphics;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class StartupProgress {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ProgressBar progress;
    
    @FXML
    private Label data;
    
    @FXML
    void initialize() {
    	
    }
    
    public void setProgress(long value) {
    	progress.setProgress(value);
    }
    

    public void setLabel(String info) {
    	data.setText("Loading modules: "+info);
    }
}