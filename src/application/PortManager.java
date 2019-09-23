package application;

public class PortManager {
	private static final int[] PORTS = {9802,9803,9804,9805,9806};
	private static final boolean[] STATUS = {false,false,false,false,false};
	
	public static int getPort() {
		for(int i=0; i<STATUS.length;i++) {
			if(!STATUS[i]) {
				STATUS[i]=true;
				return PORTS[i];
			}
		}
		return 0;
	}
	
	public static void freePort(int port) {
		for(int i=0;i<STATUS.length;i++) {
			if(PORTS[i]==port) {
				STATUS[i]=false;
				return;
			}
		}
	}
}