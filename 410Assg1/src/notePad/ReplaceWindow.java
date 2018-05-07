package notePad;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ReplaceWindow extends JFrame{
	private JTextField findText=new JTextField();
	private JTextField replaceText=new JTextField();
	private JButton findButton=new JButton("Find");
	private JButton replaceButton=new JButton("Replace");
	
	public ReplaceWindow(NotePadController controller) {
    	this.setLayout(new GridLayout(3,2));
    	
        add(new JLabel("Find:"));
        add(findText);
        add(new JLabel("Replace:"));
        add(replaceText);
        add(findButton);
        add(replaceButton);
        
        findButton.addActionListener(e->controller.find(findText.getText()));
        replaceButton.addActionListener(e->controller.replace(findText.getText(),replaceText.getText()));
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        pack();
    }
}
