package graphics;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HomeController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView chat;

    @FXML
    private ImageView home;

    @FXML
    private ImageView outbox;

    @FXML
    private ImageView inbox;

    @FXML
    private ImageView settings;

    @FXML
    private StackPane mainWindow;

    @FXML
    private ImageView outbox_home;

    @FXML
    private ImageView inbox_home;

    @FXML
    private ImageView settings_home;
    
    @FXML
    private VBox leftBox;

    private GridPane inBox;
    private GridPane outBox;
    private Stage settingStage;
    private Stage chatStage=null;
    private ChatWindowController chatControl;
    
    @FXML
    void initialize() {
    	try {
			outBox = FXMLLoader.load(getClass().getResource("/graphics/InBox.fxml"));
			inBox = FXMLLoader.load(getClass().getResource("/graphics/OutBox.fxml"));
		} catch (IOException e) {
		}
    	prepareSettingView();
    }
    
    private void prepareSettingView() {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/Settings.fxml"));
			StackPane root = loader.load();
			settingStage = new Stage();
			settingStage.setScene(new Scene(root));
			settingStage.initModality(Modality.APPLICATION_MODAL);
			settingStage.setResizable(false);
			settingStage.setTitle("Settings");
			leftBox.setVisible(false);
			((SettingController)loader.getController()).setStage=settingStage;
		} catch (IOException e) {
		}
    }

    @FXML
    private void showFocus(MouseEvent event) {
    	if(event.getEventType().equals(MouseEvent.MOUSE_ENTERED)) {
    		DropShadow shadow = new DropShadow();
    		shadow.colorProperty().set(Color.DARKSEAGREEN);
    		shadow.setRadius(10);
    		shadow.setSpread(0.8);
    		((ImageView)event.getSource()).setEffect(shadow);
    	}else if(event.getEventType().equals(MouseEvent.MOUSE_EXITED)){
    		((ImageView)event.getSource()).setEffect(null);
    	}
    }
    
    @FXML
    private void showInbox(MouseEvent event) {
    	if(event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
    		if(mainWindow.getChildren().contains(outBox))
    			mainWindow.getChildren().remove(outBox);
    		if(!mainWindow.getChildren().contains(inBox))
    			mainWindow.getChildren().add(inBox);
    		leftBox.setVisible(true);
    	}
    }
    
    @FXML
    private void showOutbox(MouseEvent event) {
    	if(event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
    		if(mainWindow.getChildren().contains(inBox))
    			mainWindow.getChildren().remove(inBox);
    		if(!mainWindow.getChildren().contains(outBox))
    			mainWindow.getChildren().add(outBox);
    		leftBox.setVisible(true);
    	}
    }
    
    @FXML
    private void showHome(MouseEvent event) {
    	if(event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
    		if(mainWindow.getChildren().contains(inBox))
    			mainWindow.getChildren().remove(inBox);
    		else if(mainWindow.getChildren().contains(outBox))
    			mainWindow.getChildren().remove(outBox);
    		leftBox.setVisible(false);
    	}
    }
    
    @FXML
    private void showSetting(MouseEvent event) {
    	if(event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
    		settingStage.show();
    	}
    }
    
    @FXML
    private void showChatWindow() {
    	if(chatStage!=null) {
    		if(chatStage.isShowing()) {
    			chatControl.refreshList();
    			chatStage.toFront();
    		}else {
    			chatControl.refreshList();
    			chatStage.show();
    		}
    	}else {
    		try {
    			FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/ChatWindow.fxml"));
				BorderPane root =loader.load();
				chatControl = loader.getController();
				chatStage = new Stage();
				chatStage.setTitle("CHAT");
				chatStage.setScene(new Scene(root));
				chatStage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
}