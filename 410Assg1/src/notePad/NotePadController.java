package notePad;

import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class NotePadController{
	
    public static void main(String[] args) {
    	new NotePadController();
    }
    private NotePadWindow app;
    private FileModel fileModel;
    public NotePadController() {
    	app = new NotePadWindow(this);
    	fileModel=new FileModel();
    	fileModel.AddChangeListener(app);
    	fileModel.init();
    	
    }
    /**
     * prompts for the file to save to then saves text to a file
     * @param text text to save to file
     */
    public void save(String text) {
    	File fileToWrite = null;
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION)
            fileToWrite = fc.getSelectedFile();
        fileModel.saveFile(fileToWrite, text);
    }
    /**
     * prints text
     * @param text
     */
    public void print(String text) {
    	 try{
             PrinterJob pjob = PrinterJob.getPrinterJob();
             pjob.setJobName("Sample Command Pattern");
             pjob.setCopies(1);
             pjob.setPrintable(new Printable() {
                 public int print(Graphics pg, PageFormat pf, int pageNum) {
                     if (pageNum>0)
                         return Printable.NO_SUCH_PAGE;
                     pg.drawString(text, 500, 500);
                     app.paint(pg);
                     return Printable.PAGE_EXISTS;
                 }
             });

             if (pjob.printDialog() == false)
                 return;
             pjob.print();
         } catch (PrinterException pe) {
             JOptionPane.showMessageDialog(null,
                     "Printer error" + pe, "Printing error",
                     JOptionPane.ERROR_MESSAGE);
         }
    }
    /**
     * prompts user for file selection then opens said file
     */
	public void openFile() {
		File fileToRead = null;
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION)
        	fileToRead = fc.getSelectedFile();
		
		app.setTextContent(fileModel.openFile(fileToRead));
	}
	/**
	 * opens file with specified name
	 * @param fileName the file
	 */
	public void openFile(String fileName) {
		app.setTextContent(fileModel.openFile(new File(fileName)));
	}
	/**
	 * gets the position of a string inside another string after some index
	 * if it doesn't exist after index, it tries from start of document
	 * if that doesn't work, it presents and error to the user
	 * @param text text being searched through
	 * @param search text being searched for
	 * @param carrot the start position
	 * @return the start index of found text (-1 if not found)
	 */
	private int getStringPosition(String text,String search,int carrot) {
		int index=text.indexOf(search, carrot);
		if(index<0) 
			index=text.indexOf(search);
		if(index<0){
			JOptionPane.showMessageDialog(null,
                    "Could not find text \""+search+"\"","Not Found",
                    JOptionPane.WARNING_MESSAGE);
		}
		return index;
	}
	/**
	 * removes length of text from larger string and replaces with other text
	 * @param text
	 * @param replacement
	 * @param offset
	 * @param length
	 * @return
	 */
	private String replace(String text,String replacement,int offset,int length) {
		return text.substring(0, offset)+replacement+text.substring(offset+length);
	}
	
	/**
	 * finds and highlights next indstance of text in string
	 * @param text
	 */
	public void find(String find) {
		int index=getStringPosition(app.getTextContent(),find,app.position());
		if(index>=0)
			app.highlight(index,find.length());
	}
	
	/**
	 * finds and replaces next instance of text.
	 * if cursor is touching text,it's also considered to be next
	 * (IE if cursor is at end of a word, it will still take that word instead of next instance)
	 * @param find
	 * @param replace
	 */
	public void replace(String find, String replace) {
		int index=getStringPosition(app.getTextContent(),find,app.position()-replace.length());
		if(index>=0)
			app.setTextContent(replace(app.getTextContent(),replace,index,find.length()));
		
	}
	/**
	 * opens find and replace window
	 */
	public void findAndReplace() {
		new ReplaceWindow(this);
	}
	/**
	 * cleans up resources before closing application
	 */
	public void close() {
		fileModel.close();
		System.exit(0);
		
	}
}