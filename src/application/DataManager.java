package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public final class DataManager {
	private static final ArrayList<String> DOCUMENTS = new ArrayList<String>() {/**
		 * 
		 */
		private static final long serialVersionUID = -7353905702068158457L;

	{
		add("pdf");
		add("xls");
		add("xlsx");
		add("doc");
		add("docx");
		add("accb");
		add("rtf");
		add("odf");
		add("txt");
		add("ppt");
		add("pptx");}};

	private static final ArrayList<String> PICTURES = new ArrayList<String>() {/**
		 * 
		 */
		private static final long serialVersionUID = -708025651560508957L;

	{
		add("jpeg");
		add("jpg");
		add("gif");
		add("bmp");
		add("svg");
		add("tiff");
		add("raw");}};
	private static final ArrayList<String> VIDEOS = new ArrayList<String>() {/**
		 * 
		 */
		private static final long serialVersionUID = 3329861548431174283L;

	{
		add("avi");
		add("mp4");
		add("mpeg");
		add("webm");
		add("mkv");
		add("flv");
		add("vob");
		add("gifv");
		add("wmv");
		add("asf");
		add("mpv");
		add("mpg");
		add("mp2");
		add("m4v");
		add("3gp");}};

	private static final ArrayList<String> MUSIC = new ArrayList<String>() {/**
		 * 
		 */
		private static final long serialVersionUID = -4862886351521615770L;

	{
		add("mp3");
		add("aac");
		add("wav");
		add("aiff");
		add("vof");
		add("wma");
		add("caf");
		add("flac");
		add("alac");}};
		
	private static String path;
	private static String pseudo=null;
	
	public static String getPath() {
		if(checkPath())
			return path+"/";
		return null;
	}
	
	public static String getSavePath(String extension) {
		if(PICTURES.contains(extension)){
			return path+"/Pictures/";
		}else if(DOCUMENTS.contains(extension)){
			return path+"/Documents/";
		}else if(MUSIC.contains(extension)){
			return path+"/Music/";
		}else if(VIDEOS.contains(extension)){
			return path+"/Videos/";
		}else return path+"/Others/";
	}
	
	public static boolean checkPath() {
		return readPath();
	}
	
	private static void createPath() {
		File file = new File(path+"/Documents");
		file.mkdirs();
		file = new File(path+"/Music");
		file.mkdirs();
		file = new File(path+"/Videos");
		file.mkdirs();
		file = new File(path+"/Pictures");
		file.mkdirs();
		file = new File(path+"/Others");
		file.mkdirs();
	}
	
	public static boolean saveSettings(String name,String path) {
		DataManager.path = path+"/GGS share files";
		createPath();
		return savePath(path)&&savePseudo(name);
	}
	
	private static boolean savePath(String path) {
		DataManager.path = path+"/GGS share files";
		File f = new File(System.getProperty("user.home"));
		f.mkdirs();
		f = new File(System.getProperty("user.home")+"/ggs_share_path.txt");
		FileWriter w;
		try {
			w = new FileWriter(f);
			w.write(DataManager.path);
			w.close();
		}catch(IOException ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	private static boolean savePseudo(String name) {
		pseudo = name;
		File f = new File(path+"/pseudo.txt");
		FileWriter w;
		try {
			w = new FileWriter(f);
			w.write(name);
			w.close();
		}catch(IOException ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static String readPseudo() {
		if(pseudo!=null)
			return pseudo;
		File f = new File(path+"/pseudo.txt");
		BufferedReader r;
		try {
			r= new BufferedReader(new FileReader(f));
			pseudo = r.readLine();
			r.close();
		}catch(IOException ex) {
			ex.printStackTrace();
			pseudo = null;
		}
		return pseudo;
	}
	
	private static boolean readPath() {
		File f = new File(System.getProperty("user.home")+"/ggs_share_path.txt");
		if(!f.exists()) {
			return false;
		}
		BufferedReader r;
		try {
			r= new BufferedReader(new FileReader(f));
			path = r.readLine();
			r.close();
			return (new File(path)).exists();
		}catch(IOException ex) {
			ex.printStackTrace();
			path  = System.getProperty("user.home")+"/Documents/GGS_share files";
			return false;
		}
	}
}