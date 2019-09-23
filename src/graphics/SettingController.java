package graphics;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import application.DataManager;
import application.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class SettingController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField username;

    @FXML
    private TextField saveText;

    @FXML
    private Button saveBtn;
    
    private DirectoryChooser chooser;
    
    public Stage setStage =null;
    public Stage mainStage = null;
    public Main mainClass=null;

    @FXML
    void initialize() {
    	chooser = new DirectoryChooser();
    	chooser.setInitialDirectory(new File(System.getProperty("user.home")));
    	chooser.setTitle("Default save location");
    }
    
    @FXML
    private void chooseDirectory(ActionEvent event) {
    	final File dir = chooser.showDialog(null);
    	Platform.runLater(new Runnable() {
    		@Override
    		public void run() {
    	    	saveText.setText(dir.getAbsolutePath());
    		}
    	});
    }
    
    @FXML
    private void saveActions(ActionEvent event){
    	String name = username.getText();
    	String path = saveText.getText();
    	if(name==null||name.isEmpty()||path==null||path.isEmpty())
    		return;
    	DataManager.saveSettings(name,path);
    	setStage.close();
    	if(!(mainStage==null)) {
    		mainClass.loadModules();
    	}
    }
}
