package notePad;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class FileModel {
	private ArrayList<ChangeListener> listeners=new ArrayList<ChangeListener>();
	private LinkedList<String> history=new LinkedList<String>();
	private File recent=new File(".recentFiles.txt");
	
	/**
	 * this method makes the file model object ready for use. it updates it's state
	 * by reading a list of previously opened files
	 */
	public void init() {
		if(recent.exists()) {
			Scanner reader=null;
			try {
				reader = new Scanner(recent);
				while(reader.hasNext()) {
					String file=reader.nextLine();
					addRecent(file);
				}
			} catch (FileNotFoundException e) {
				System.err.println("could not read list of recently used files");
			}finally {
				if(reader!=null) {
					reader.close();
				}
			}
		}
	}
	/**
	 * call this method before closing program to keep "recent files" updated
	 */
	public void close() {
		PrintWriter out=null;
		try {
			out = new PrintWriter(recent);
			for(String s:history) {
				out.println(s);
			}
			out.flush();
		} catch (FileNotFoundException e) {
			System.err.println("Cannot save recent files list");
		}finally {
			if(out!=null) {
				out.close();
			}
		}
	}
	
	public void AddChangeListener(ChangeListener cl) {
		listeners.add(cl);
	}
	
	private void addRecent(String fileName) {
		if(history.contains(fileName))
			history.remove(fileName);
		history.addFirst(fileName);
		ChangeEvent e =new ChangeEvent(this);
		for(ChangeListener cl:listeners) {
			cl.stateChanged(e);
		}
	
	}
	
	/**
	 * saves text to a file, and updates recently used list
	 * @param file name of the file
	 * @param text the text to be saved to the file
	 */
	public void saveFile(File file,String text) {
		PrintWriter out=null;
		try {
			out=new PrintWriter(file);
			out.print(text);
			out.flush();
			addRecent(file.getPath());
		} catch (FileNotFoundException e) {
			System.err.println("could not save file");
		}finally {
			if(out!=null) {
				out.close();
			}
		}
	}
	/**
	 * opens file, reads it to a string and returns
	 * it closes file after reading (don't want to lock unneeded resources)
	 * @param file name of the file
	 * @return text in file
	 */
	public String openFile(File file){
		String buffer="";
		Scanner in=null;
		try {
			in = new Scanner(file);
			while(in.hasNext()) {
				buffer+=in.nextLine()+"\n";
			}
			addRecent(file.getPath());
			
		} catch (FileNotFoundException e) {
			System.err.println("could not read files");
		}finally {
			if(in!=null) {
				in.close();
			}
		}
		return buffer;
	}
	/**
	 * returns a list of recently opened files in the form of strings
	 * @return list of filenames
	 */
	public List<String> getRecent() {
		return (List<String>) history.clone();
	}

}
