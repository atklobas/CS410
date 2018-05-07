package notePad;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
public class NotePadWindow extends JFrame implements ChangeListener{
	
	public static final int NUMBER_OF_RECENT_FILES=5;
	private JMenuBar menuBar = new JMenuBar();
	private JMenu fileMenu = new JMenu("File");
	private JMenu editMenu = new JMenu("Edit");
	private JMenu recentMenu=new JMenu("Recent");
	private JTextPane textPane = new JTextPane();
    private JMenuItem newFile = new JMenuItem("New File");
    private JMenuItem saveFile = new JMenuItem("Save File");
    private JMenuItem openFile = new JMenuItem("Open File");
    private JMenuItem printFile = new JMenuItem("Print File");
    private JMenuItem copy = new JMenuItem("Copy");
    private JMenuItem paste = new JMenuItem("Paste");
    private JMenuItem replace = new JMenuItem("Replace");
    
    private NotePadController controller;
    
    public NotePadWindow(NotePadController controller) {
    	this.controller=controller;

        setTitle("A Simple Notepad Tool");
        
        makeMenu();
        setJMenuBar(menuBar);
        
        add(new JScrollPane(textPane));
        setPreferredSize(new Dimension(600,600));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        pack();
    }
    private void makeMenu() {
    	fileMenu.add(newFile);
        fileMenu.addSeparator();
        fileMenu.add(saveFile);
        fileMenu.addSeparator();
        fileMenu.add(openFile);
        fileMenu.addSeparator();
        fileMenu.add(recentMenu);
        fileMenu.addSeparator();
        fileMenu.add(printFile);
        editMenu.add(copy);
        editMenu.add(paste);
        editMenu.add(replace);
        
        newFile.addActionListener(e->newFile());
        saveFile.addActionListener(e->controller.save(textPane.getText()));
        openFile.addActionListener(e->controller.openFile());
        printFile.addActionListener(e->controller.print(textPane.getText()));
        copy.addActionListener(e->copy());
        paste.addActionListener(e->paste());
        replace.addActionListener(e -> controller.findAndReplace());
        
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
    }
    
    @Override
	public void stateChanged(ChangeEvent e) {
    	//if file model is updated, update the recently opened files menu
		if(e.getSource()instanceof FileModel) {
			updateRecentMenu(((FileModel)(e.getSource())).getRecent());
		}
	}
    private void updateRecentMenu(List<String> recentList) {
    	recentMenu.removeAll();
    	for(String s: recentList) {
    		JMenuItem recent=new JMenuItem(s);
    		recent.addActionListener(e->controller.openFile(s));
    		
    		recentMenu.add(recent);
    		if(recentMenu.getItemCount()>=NUMBER_OF_RECENT_FILES)
    			break;
    	}
    }
    
    public void newFile() {
    	textPane.setText("");
    }
    public void copy() {
    	textPane.copy();
    }
    public void paste() {
         textPane.paste();
    }
    /**
     * 
     * @return content in the textpane
     */
    public String getTextContent() {
    	return textPane.getText();
    }
    /**
     * 
     * @param content sets textpane to have content as it's text
     */
    public void setTextContent(String content) {
    	textPane.setText(content);
    }
	//intercept closing pane to properly clean up other resources before exit
	public void dispose() {
		controller.close();
		super.dispose();
	}
	/**
	 * highlights text starting at index for the given length
	 * @param index
	 * @param length
	 */
	public void highlight(int index, int length) {
		textPane.select(index, index+length);
	}
	/**
	 * gets position of carrot
	 * @return the index of the character after the carrow
	 */
	public int position() {
		return textPane.getCaretPosition();
	}
}