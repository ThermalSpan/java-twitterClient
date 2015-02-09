/* SetupPanel.java
 * Authored by Russell Bentley
 * 
 * This is a JPanel that contains all the GUI components for setting up and starting a stream
 * 
 */

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.twitter.hbc.core.endpoint.Location;

public class SetupPanel extends JPanel{
	private Main main;
	private LanguagePanel languagePanel;
	private LocationPanel locationPanel;
	private UserPanel userPanel;
	private PhrasePanel phrasePanel;
	private DirectoryPanel directoryPanel;
	private JRadioButton hoursEnd;
	private JTextField timeField;
	
	public SetupPanel(final Main main, ArrayList<String> languageList, ArrayList<Location> locationList, 
			ArrayList<Long> userList, ArrayList<String> phraseList) {
		super();
		this.main = main;
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		JPanel paramPanel = new JPanel();
    	paramPanel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
    	paramPanel.setLayout(new GridLayout(2,2));
    	userPanel = new UserPanel(userList);
    	phrasePanel = new PhrasePanel(phraseList);
    	locationPanel = new LocationPanel(locationList);
    	languagePanel = new LanguagePanel(languageList);
    	userPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    	phrasePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    	locationPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    	languagePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    	paramPanel.add(userPanel);
    	paramPanel.add(phrasePanel);
    	paramPanel.add(locationPanel);
    	paramPanel.add(languagePanel);
    	this.add(paramPanel);
    	
    	directoryPanel = new DirectoryPanel(main);
    	directoryPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    	this.add(directoryPanel);
    	
    	JPanel radioPanel = new JPanel();
    	radioPanel.setLayout(new GridLayout(0,1));
    	JRadioButton userEnd = new JRadioButton("End by User Request");
    	userEnd.setSelected(true);
    	hoursEnd = new JRadioButton("End After #hours");
    	timeField = new JTextField();
    	radioPanel.add(new JLabel("End Condition:"));
    	radioPanel.add(userEnd);
    	radioPanel.add(hoursEnd);
    	radioPanel.add(timeField);
    	ButtonGroup group = new ButtonGroup();
    	group.add(userEnd);
    	group.add(hoursEnd);
    	this.add(radioPanel);
    	
    	JButton start = new JButton("Start Stream");
    	this.add(start);
    	
    	start.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
    			main.startStream();
    		}
    	});
	}
	
	public void finalize() throws Exception{
		try{
			languagePanel.finalizeList();
			locationPanel.finalizeList();
			userPanel.finalizeList();
			phrasePanel.finalizeList();
			directoryPanel.finalize();
			if(hoursEnd.isSelected()) main.setTimer(60*60*1000*Long.parseLong(timeField.getText()));
		} catch(Exception error){
			JOptionPane.showMessageDialog(null,
				    "Setup not complete.\n"
				    + "Your particular error was:\n"
				    + error.toString(), "Setup Incomplete",
				    JOptionPane.WARNING_MESSAGE);
			throw(new Exception());
		}
	}

}
