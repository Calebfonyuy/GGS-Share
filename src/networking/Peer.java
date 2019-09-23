package networking;


import java.net.*;

import application.PeerManager;

import java.io.*;

import javafx.application.Platform;
import javafx.collections.*;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Peer{
    private String pseudo;
    private Socket socket;
    private String ip;
    private ObservableList<Text> messages;
    private Messaging message;
    private PrintWriter printer;
    private Peer ref;

    public Peer(String pseudo, Socket soc){
        this.pseudo = pseudo;
        this.socket = soc;
        this.messages = FXCollections.observableArrayList();
        try {
			this.printer = new PrintWriter(socket.getOutputStream(),true);
		} catch (IOException e) {
			e.printStackTrace();
		}
        this.ip = socket.getInetAddress().getHostAddress();
        ref = this;
    }

    public final String getPseudo() {
		return pseudo;
	}


	public final String getIp() {
		return ip;
	}


	public final ObservableList<Text> getMessages() {
		return messages;
	}


	public void sendMessage(String mess){
		Text text = new Text(mess);
		text.setWrappingWidth(230);
		text.setTextAlignment(TextAlignment.RIGHT);

        /*Include a process here to remove all end of line characters so that the message should be sent as one*/
    	mess=mess.replaceAll("\n", " ");
		messages.add(text);
        printer.println(mess);
    }

    public boolean sendFile(File file,ProgressBar bar,Label label){
        SendFileThread thread = new SendFileThread(file,bar,pseudo,label);
        int soc = thread.createServerSocket();
        if(soc==0)
        	return false;
        
        sendMessage("FILE");
        sendMessage(file.getName());
        sendMessage(""+file.length());
        sendMessage(""+soc);
        
        
        Platform.runLater(new Runnable(){
           @Override
           public void run(){
                thread.start();
           }
        });
        return true;
    }
    
    public void startReception() {
    	message = new Messaging(socket,messages);
    	message.start();
    }
    
    @Override
    public String toString() {
    	return pseudo;
    }

class Messaging extends Thread{
    private Socket socket;
    private BufferedReader reader;
    private final ObservableList<Text> messageList;

    public Messaging(Socket sock, ObservableList<Text> messageList){
        this.socket = sock;
        try {
			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
        this.messageList = messageList;
    }

    public void run(){
        String message;
        boolean flag = true;
        while(flag){
            message = checkMessage();
            if(message==null) {
            	try {
					socket.close();
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							PeerManager.conAddress.remove(ip);
							PeerManager.peerNames.remove(pseudo);
							PeerManager.peers.remove(ref);
						}
					});
				} catch (IOException e) {
				}
            	
            	flag = false;
            	continue;
            }
            	
            if(message.equals("FILE")){
                ReceiveFileThread tmp = new ReceiveFileThread(receiveFileDetails());
                
                Platform.runLater(new Runnable(){
                   @Override
                   public void run(){
                        tmp.start();
                   }
                });
                
            }else{
                final String tmp = message;
                Platform.runLater(new Runnable(){
                   @Override
                   public void run(){
                	    Text text = new Text(pseudo+": "+tmp);
                	    text.setTextAlignment(TextAlignment.LEFT);
                	    text.setWrappingWidth(240);
                	    
                        messageList.add(text);
                   }
                });
            }
        }
    }

    private FileInfo receiveFileDetails(){
        FileInfo tmp = new FileInfo(pseudo, ip);
        tmp.setName(checkMessage());
        long tmp2 = Long.parseLong(checkMessage());
        tmp.setLength(tmp2);
        int port = Integer.parseInt(checkMessage());
        tmp.setPort(port);
        return tmp;
    }

    public String checkMessage(){
        String message=null;
		try {
			message = reader.readLine();
		} catch (IOException e) {
		}
        return message;
    }
}
}
