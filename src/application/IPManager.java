package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class IPManager {
	
	public static String hostAddress;

	public static String findHostAddress() {
		hostAddress =findWindowsIp();
        if(hostAddress == null){
            hostAddress=findUbuntuIp();
        }
        return hostAddress;
    }
	
	private static String findUbuntuIp(){
        Process p;
		try {
			p = Runtime.getRuntime().exec("ifconfig");
		} catch (IOException e) {
			return null;
		}
        String line2=null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        try {
			while((line=reader.readLine())!=null){
				if(line.contains("inet ")&&(!line.contains("127.0.0.1"))){
				    String[] tmp = line.split("inet ");
				    line2= tmp[1];
				    tmp = line2.split(" ");
				    line2 = tmp[0];
				    line2 = line2.trim();
				}
			}
		} catch (IOException e) {
			return null;
		}
        return line2;
	}
	
    private static String findWindowsIp() {
        String address=null;
        Process process;
		try {
			process = Runtime.getRuntime().exec("ipconfig");
		} catch (IOException e) {
			return null;
		}
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        try {
			while ((line = reader.readLine()) != null) {
			     if (line.contains("IPv4 Address")) {
			         String[] words = line.split(" : ");
			          address = words[words.length - 1];
			         return address;
			     }
			 }
		} catch (IOException e) {
			return null;
		}
        return address;
    }
}
