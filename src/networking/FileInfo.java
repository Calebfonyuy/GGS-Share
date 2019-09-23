package networking;


public class FileInfo{
    private String name;
    private long length;
    private String pseudo;
    private String ip;
    private int port;

    public FileInfo(String pseudo, String ip){
        this.pseudo=pseudo;
        this.ip=ip;
    }
    public void setName(String name){
    	if(name!=null)
    		this.name = name;
    	else
    		this.name="New Received File";
    }
    
    public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getName(){
        return name;
    }

    public void setLength(long length){
        this.length= length;
    }

    public long getLength(){
        return length;
    }

    public String getPseudo(){
        return pseudo;
    }

    public String getIp(){
        return ip;
    }
}
