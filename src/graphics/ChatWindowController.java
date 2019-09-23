package graphics;

import application.PeerManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class ChatWindowController {

    @FXML
    private ListView<ChatHead> chatHeadList;

    @FXML
    private ListView<Text> activeChat;

    @FXML
    private TextArea message;
    
    public static ObservableList<ChatHead> chatHeads = FXCollections.observableArrayList();
    
    @FXML
    public void initialize() {
    	refreshList();
    	chatHeadList.setItems(chatHeads);
    }
    
    public void refreshList() {
    	Platform.runLater(new Runnable() {
    		@Override
    		public void run() {
    			chatHeads.clear();
    			activeChat.getItems().clear();
    			for(String name:PeerManager.peerNames) {
    	    		chatHeads.add(new ChatHead(name));
    	    	}
    		}
    	});
    }
    
    @FXML
    public void selectChat(MouseEvent event) {
    	int i=chatHeadList.getSelectionModel().getSelectedIndex();
    	if(i!=-1) {
    		Platform.runLater(new Runnable() {
    			@Override
    			public void run() {
    				try {
    					activeChat.setItems(PeerManager.peers.get(i).getMessages());
    				}catch(ArrayIndexOutOfBoundsException ex) {
    					refreshList();
    				}
    			}
    		});
    	}
    }
    
    @FXML
    public void sendMessage() {
    	String msg = message.getText();
    	if(msg.isEmpty())
    		return;
    	
    	int i=chatHeadList.getSelectionModel().getSelectedIndex();
		if(i!=-1) {
	    	Platform.runLater(new Runnable() {
	    		@Override
	    		public void run() {
		    		PeerManager.peers.get(i).sendMessage(msg);
	    		}
	    	});
	    	message.clear();
		}
    }
}