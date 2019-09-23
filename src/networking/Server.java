package networking;

import javafx.application.Platform;

import java.net.*;

import application.Main;
import application.PeerManager;

import java.io.*;

public class Server extends Thread{
    private int port = 9800;
    private ServerSocket serverSoc;
    private Socket soc;

    public boolean creatServer(){
        int k=0;
        ServerSocket tmp = null;
        while(tmp==null&&k<2){
            try{
                tmp = new ServerSocket(port+k);
            }catch(Exception ex){
            	k++;
                tmp =null;
                ex.printStackTrace();
            }
        }
        if(tmp==null)
            return false;
        else{
           serverSoc = tmp;
           port +=k;
           return true;
        }
    }

    public int getPort(){
        return port;
    }

    public void run(){
        while(true){
            try{
                soc = serverSoc.accept();
                String address = soc.getInetAddress().getHostAddress();
                if(PeerManager.conAddress.contains(address)) {
                	soc.close();
                	continue;
                }
                
                PrintWriter printer = new PrintWriter(soc.getOutputStream(),true);
                printer.println(Main.hostName);
                BufferedReader reader = new BufferedReader(new InputStreamReader(soc.getInputStream()));
                String name = reader.readLine();
                if(name==null)
                	throw new IOException();
                Peer peer = new Peer(name,soc);
                
                Platform.runLater(new Runnable() {
                	@Override
                	public void run() {
                		PeerManager.conAddress.add(address);
                		PeerManager.peerNames.add(name);
                		PeerManager.peers.add(peer);
                	}
                });
                
                peer.startReception();
            }catch(IOException ex){
                ex.printStackTrace();
                try {
                	if(soc!=null&&!soc.isClosed())
                		soc.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        }
    }
}
