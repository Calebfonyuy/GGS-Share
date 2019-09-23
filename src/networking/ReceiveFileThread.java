package networking;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

import application.DataManager;
import application.PeerManager;
import graphics.TransferController;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ReceiveFileThread extends Thread{
	private FileInfo info;
	private SocketChannel soc;
	private ProgressBar bar;
	private Label label;
	private String path;
	private File file;
	private int bufSize;
	
	public ReceiveFileThread(FileInfo info) {
		this.info = info;
		path = DataManager.getSavePath(info.getName().substring(info.getName().lastIndexOf(".")+1));
		setupGui();
		bufSize = getBufSize();
		try {
			soc = SocketChannel.open();
		} catch (IOException e) {
			try {
				soc = SocketChannel.open();
			} catch (IOException ex) {
				soc = null;
			}
		}
	}
	
	private void setupGui() {
		file = new File(path+info.getName());
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/Transfer.fxml"));
    	TransferController controller;
    	GridPane pane;
    	
		try {
			pane = loader.load();
			controller = (TransferController)loader.getController();
			controller.setLabels(info.getName());
			controller.setName(info.getPseudo());
			this.bar=controller.getProgressBar();
			this.label = controller.getProgressLabel();
			bar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
			final TransferController control = controller;
			
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Stage stage = new Stage();
					stage.setScene(new Scene(pane));
					stage.setResizable(false);
					stage.setTitle("Receiving file(s) from "+info.getPseudo());
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
			});
		}catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private int getBufSize() {
		if(info.getLength()<=16475600) return 10240;
		else if(info.getLength()<=8228800) return 164576;
		else return 1645760;
	}
	
	@Override
	public void run() {
		try {
			if(soc==null) {
				soc = SocketChannel.open();
			}
			
			soc.socket().connect(new InetSocketAddress(info.getIp(),info.getPort()));
			
			FileOutputStream os = new FileOutputStream(file,true);
			FileChannel channel = os.getChannel();
			ByteBuffer bytes = ByteBuffer.allocate(bufSize);

			long total=0,size=info.getLength();
			int read;
			
			while((read=soc.read(bytes))!=-1) {
				bytes.flip();
				channel.write(bytes);
				bytes.clear();
				total +=read;
				final long l=total;
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						bar.setProgress((double)l/size);
						label.setText(String.format("%.2f",100*(double)l/size)+"%");
					}
				});
			}
			os.close();
            Platform.runLater(new Runnable(){
                @Override
                public void run(){
        			bar.setProgress(1);
             	    PeerManager.fileHistory.add(file.getName());
                }
             });
			soc.close();
		}catch(IOException ex) {
			ex.printStackTrace();
			this.interrupt();
		}
	}
}
