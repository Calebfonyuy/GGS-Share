package application;
	
import java.io.IOException;

import graphics.SettingController;
import graphics.StartupProgress;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import networking.Server;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;


public class Main extends Application {
	private Stage mainStage;
	public static String hostName;
	private String hostAddress;
	private Server server;
	
	@Override
	public void start(Stage primaryStage) {
		mainStage = primaryStage;
		try {
			StackPane root = FXMLLoader.load(getClass().getResource("/graphics/HomeView.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setMinHeight(root.getHeight());
			primaryStage.setMinWidth(root.getWidth());
			
			primaryStage.getIcons().add(new Image(this.getClass().getResource("/application/ggs_share_icon.png").toString()));
			primaryStage.setOnCloseRequest((WindowEvent e)->{
				e.consume();
				System.gc();
				System.exit(0);
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(!DataManager.checkPath())
			runSetup();
		else {
			hostAddress = IPManager.findHostAddress();
			loadModules();
		}
	}
	
	private void checkAddress() {
		if(hostAddress==null) {
			Alert alert = new Alert(AlertType.ERROR,"CONNECT TO A NETWORK IN ORDER TO SHARE");
			alert.setTitle("GGSshare\tCONNECTION ERROR");
			alert.setHeaderText("CONNECTION ERROR");
			alert.showAndWait();
			System.exit(0);
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void loadModules(){
		checkAddress();
		GridPane pane = null;
		Stage stage = new Stage();
		FXMLLoader loader;
		StartupProgress controller=null;
		try {
			loader = new FXMLLoader(getClass().getResource("/graphics/Startup.fxml"));
			pane = loader.load();
			controller = loader.getController();
			stage.setScene(new Scene(pane));
			stage.setResizable(false);
			stage.setIconified(false);
		}catch(IOException ex) {
			ex.printStackTrace();
			pane=null;
		}
		
		if(pane==null) {
			hostName = DataManager.readPseudo();
			server = new Server();
			server.creatServer();
			server.start();
			PeerManager.startPeerSearch();
		}else {
			stage.show();
			controller.setLabel("addresses");
			controller.setProgress((long) 0.2);
			controller.setLabel("host data");
			hostName = DataManager.readPseudo();
			controller.setProgress((long) 0.4);
			controller.setLabel("network services");
			server = new Server();
			if(!server.creatServer()) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
				}
				System.exit(1);
			}
			controller.setProgress((long) 0.6);
			controller.setLabel("network services");
			server.start();
			controller.setProgress((long) 0.8);
			controller.setLabel("search services");
			PeerManager.startPeerSearch();
			controller.setProgress((long) 1);
			controller.setLabel("Loading completed!");
			stage.close();
		}
		System.gc();
		mainStage.setTitle(hostName);
		mainStage.show();
	}
	
	private void runSetup() {
		try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/Settings.fxml"));
			StackPane root = loader.load();
			Stage settingStage = new Stage();
			settingStage.setScene(new Scene(root));
			settingStage.initModality(Modality.APPLICATION_MODAL);
			settingStage.setResizable(false);
			settingStage.setTitle("Welcome to GGS_share: Initial setup");
			((SettingController)loader.getController()).setStage=settingStage;
			((SettingController)loader.getController()).mainStage=mainStage;
			((SettingController)loader.getController()).mainClass=this;
			settingStage.show();
		} catch (IOException e) {
		}
	}
}
