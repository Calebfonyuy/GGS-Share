package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import networking.Peer;

public class PeerManager {
	public static final ObservableList<String> peerNames = FXCollections.observableArrayList();
	public static final ObservableList<Peer> peers = FXCollections.observableArrayList();
	public static final ObservableList<String> conAddress = FXCollections.observableArrayList();
	public static final ObservableList<String> fileHistory = FXCollections.observableArrayList();
	public static String serverAddress=IPManager.hostAddress;
	
	private static int PORT=9800;

	public static void addPeer(Peer p) {
		peers.add(p);
		final String name = p.getPseudo();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				peerNames.add(name);
			}
		});
	}
	
	public static boolean startPeerSearch() {
		serverAddress=IPManager.findHostAddress();
		int n = serverAddress.lastIndexOf(".");
        String base = serverAddress.substring(0, n + 1);
        int ref = Integer.parseInt(serverAddress.substring(n + 1));
        
        ExecutorService executor = Executors.newCachedThreadPool();
        
        executor.execute(new Connection(base,1,10,ref));
        executor.execute(new Connection(base,10,20,ref));
        executor.execute(new Connection(base,20,30,ref));
        executor.execute(new Connection(base,30,40,ref));
        executor.execute(new Connection(base,40,50,ref));
        executor.execute(new Connection(base,50,60,ref));
        executor.execute(new Connection(base,60,70,ref));
        executor.execute(new Connection(base,70,80,ref));
        executor.execute(new Connection(base,80,90,ref));
        executor.execute(new Connection(base,90,100,ref));
        executor.execute(new Connection(base,100,110,ref));
        executor.execute(new Connection(base,110,120,ref));
        executor.execute(new Connection(base,120,130,ref));
        executor.execute(new Connection(base,130,140,ref));
        executor.execute(new Connection(base,140,150,ref));
        executor.execute(new Connection(base,150,160,ref));
        executor.execute(new Connection(base,160,170,ref));
        executor.execute(new Connection(base,170,180,ref));
        executor.execute(new Connection(base,180,190,ref));
        executor.execute(new Connection(base,190,200,ref));
        executor.execute(new Connection(base,200,220,ref));
        executor.execute(new Connection(base,220,225,ref));
        executor.shutdown();
		return true;
	}
	
static class Connection extends Thread{
		private int min, max,ref;
		private String base;
		private Socket soc;
		
		public Connection(String base,int min, int max, int ref) {
			this.base = base;
			this.min = min;
			this.max = max;
			this.ref = ref;
		}
		
		public void run() {
			String address;
			while(!Thread.interrupted()) {
				int i = min;
				while(i<max) {
					if(i==ref)
						i++;
					if(i==max){
						i++;
						continue;
					}
					address = base.concat(""+i);
					if(conAddress.contains(address)) {
						i++;
	    				continue;
					}
	    			else {
	    				int k=0;
	    		    	boolean flag = false;
	    		    	while(!flag&&k<2) {
	    		    		try {
	    		    			soc = new Socket(address,PORT+k);
	    		    			flag = true;
	    		    		}catch(IOException ex) {
	    		    			k++;
	    		    		}
	    		    	}
	    		    	
	    		    	if(flag) {
	    		    		final String add = address;
	    		    		String pseudo;
							try {
								pseudo = (new BufferedReader(new InputStreamReader(soc.getInputStream()))).readLine();
								(new PrintWriter(soc.getOutputStream(),true)).println(Main.hostName);
								System.gc();
								
		    		    		Peer nPeer = new Peer(pseudo,soc);
		    		    		System.out.println(Main.hostName+" connected to "+pseudo);
		    		    		
		    		            Platform.runLater(new Runnable() {
		    		            	@Override
		    		            	public void run() {
		    		            		peerNames.add(pseudo);
				    		            conAddress.add(add);
				    		            peers.add(nPeer);
		    		            	}
		    		            });
		    		            nPeer.startReception();
							} catch (IOException e) {
								try {
									soc.close();
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								i--;
							}
	    		    	}
	    			}
					i++;
				}
			}
		}
	}
}
