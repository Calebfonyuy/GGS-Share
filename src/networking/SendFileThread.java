package networking;

import java.io.*;

import javafx.application.Platform;
import javafx.scene.control.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import application.PortManager;
import graphics.OutBoxController;

public class SendFileThread extends Thread {
	private File file;
	private ProgressBar bar;
	private Label label;
	private int port;
	private ServerSocketChannel soc=null;
	private int bufSize;
	
	public SendFileThread(File file,ProgressBar bar,String pseudo,Label label) {
		this.file = file;
		this.bar = bar;
		this.label = label;
		bufSize = getBufSize();
		try {
			soc = ServerSocketChannel.open();
		} catch (IOException e) {
			try {
				soc = ServerSocketChannel.open();
			}catch(IOException ex) {
				soc = null;
			}
		}
	}
	
	public int createServerSocket() {
		port = PortManager.getPort();
		if(port==0)
			return 0;
		int k=0;
		if(soc==null) {
			try {
				soc = ServerSocketChannel.open();
			} catch (IOException e) {
				try {
					soc = ServerSocketChannel.open();
				}catch(IOException ex) {
					soc = null;
					return 0;
				}
			}
		}
		while(!soc.socket().isBound()&&k<5) {
			try {
				soc.socket().bind(new InetSocketAddress(port+k));
			}catch(IOException ex) {
				port++;
				soc=null;
				ex.printStackTrace();
			}
		}
		if(soc!=null) {
			port +=k;
			return port;
		}else {
			return 0;
		}
	}
	
	private int getBufSize() {
		if(file.length()<=16475600) return 10240;
		else if(file.length()<=8228800) return 164576;
		else return 1645760;
	}
	
	@Override
	public void run() {
		try {
			SocketChannel socket = soc.accept();
			FileInputStream fileIs = new FileInputStream(file);
			
			FileChannel fos = fileIs.getChannel();
			ByteBuffer bytes = ByteBuffer.allocate(bufSize);
			
			int read,total=0;
			double size=file.length();
			
			while((read=fos.read(bytes))!=-1) {
				bytes.flip();
				socket.write(bytes);
				total +=read;
				final int tot = total;
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						bar.setProgress((double)tot/size);
						label.setText(String.format("%.2f", 100*(double)tot/size)+"%");
					}
				});
				bytes.clear();
			}
			
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					bar.setProgress(1);
					OutBoxController.fileHistory.add(file.getName());
				}
			});
			
			fileIs.close();
			socket.close();
			soc.close();
		} catch (IOException e) {

		}
	}
}
