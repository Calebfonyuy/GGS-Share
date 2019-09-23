package graphics;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.PeerManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import networking.Peer;

public class OutBoxController{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<String> peers;

    @FXML
    private Label send;

    @FXML
    private ListView<String> history;
    
    private FileChooser chooser;
    private String peerName="Test Peer";
    private Peer active;
    private ObservableList<Peer> conPeers;
    public static ObservableList<String> fileHistory;
    private ObservableList<String> peerList;
    
    @FXML
    public void initialize() {
    	chooser = new FileChooser();
    	chooser.setTitle("Choose File");
    	
    	fileHistory = FXCollections.observableArrayList();
    	history.setItems(fileHistory);
    	
    	peerList = PeerManager.peerNames;
    	peers.setItems(peerList);
    	
    	conPeers = PeerManager.peers;
    }
    
    @FXML
    private void showFocus(MouseEvent event) {
    	if(event.getEventType().equals(MouseEvent.MOUSE_ENTERED)) {
    		DropShadow shadow = new DropShadow();
    		shadow.colorProperty().set(Color.BURLYWOOD);
    		shadow.setRadius(10);
    		shadow.setSpread(1);
    		((Label)event.getSource()).setEffect(shadow);
    	}else if(event.getEventType().equals(MouseEvent.MOUSE_EXITED)){
    		((Label)event.getSource()).setEffect(null);
    	}
    }
    
    @FXML
    private void selectPeer(MouseEvent event) {
    	if(peers.getSelectionModel().getSelectedIndex()!=-1) {
    		active = conPeers.get(peers.getSelectionModel().getSelectedIndex());
    	}
    }
    
    @FXML
    private void sendFile(MouseEvent event) {
    	if(peers.getSelectionModel().getSelectedIndex()==-1)
    		return;
    	//Initialization of peer coordinates should be found here
    	
    	List<File> files = chooser.showOpenMultipleDialog(null);
    	if(files==null)
    		return;
    	else if(files.size()>5) {
    		Alert al = new Alert(AlertType.INFORMATION,"Cannot send more than five files simultaneously!");
    		al.setTitle("Too many files");
    		al.show();
    		return;
    	}
    	FXMLLoader loader;
    	TransferController controller=null;
    	GridPane pane = new GridPane();
    	ListView<GridPane> transferList = new ListView<>();
    	boolean flag = false;
    	
    	for(File file: files) {
    		try {
    			loader = new FXMLLoader(getClass().getResource("/graphics/Transfer.fxml"));
    			pane = loader.load();
    			controller = (TransferController)loader.getController();
    			controller.setLabels(file.getName());
    			controller.setName(peerName);
    			controller.getProgressBar().setProgress(ProgressBar.INDETERMINATE_PROGRESS);
    			
    			if(active==null)
    				System.out.println("active");
    			boolean status = active.sendFile(file,controller.getProgressBar(),controller.getProgressLabel());
    			  if(!status){
    				  Platform.runLater(new Runnable() {
    					  @Override
    					  public void run() {
    	    				fileHistory.add("Failed to send: "+file.getName());
    					  }
    				  });
    				  continue;
    			  }
    			 
    			final GridPane p = pane;
    			Platform.runLater(new Runnable() {
    				@Override
    				public void run() {
    					transferList.getItems().add(p);
    				}
    			});
    			flag=true;
    		}catch(IOException ex) {
    			ex.printStackTrace();
    		}
    	}
    	
    	if(!flag)
    		return;
		Stage stage = new Stage();
		transferList.setPrefHeight((files.size()*pane.getPrefHeight())+50);
		transferList.setPrefWidth(pane.getPrefWidth()+50);
		stage.setScene(new Scene(transferList));
		stage.setWidth(pane.getPrefWidth()+20);
		stage.setTitle("Sending file(s) to "+peerName);
		final TransferController control = controller;
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				if(control.getProgressBar().getProgress()<1) {
					event.consume();
				}
			}
			
		});
		stage.show();
    }
}