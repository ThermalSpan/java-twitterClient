/* DirectoryPanel.java
 * Authored by Russell Bentley
 * 
 * This is the panel where the user selects the directory in which they want their data saved. 
 * If there is no directory entered, then an exception is thrown when the parameters are finalized. 
 */

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DirectoryPanel extends JPanel implements ActionListener{
	Main main;
	File directory;
	JTextField name;
	JButton pick;
	JLabel current;

	public DirectoryPanel(Main main) {
		super();
		this.main = main;
		directory = null;
		
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		pick = new JButton("Choose Directory");
		current = new JLabel("Current Directory: ");
		JLabel nameLabel = new JLabel("File Name: ");
		name = new JTextField("data.txt");
		
		this.add(current);
		this.add(Box.createRigidArea(new Dimension(5,0)));
		this.add(pick);
		this.add(Box.createRigidArea(new Dimension(5,0)));
		this.add(nameLabel);
		this.add(name);
		
		pick.addActionListener(this);
	}
	
	public void finalize() throws Exception{
		if(directory == null) throw(new Exception("No directory chosen."));
		if(name.getText().equals("")) throw(new Exception("No file name chosen."));
		main.setoutputFile((new File(directory, name.getText())).toString());
	}

	public void actionPerformed(ActionEvent arg0) {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(null);
		chooser.setDialogTitle("Choose a Directory");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
			directory = chooser.getSelectedFile();
			current.setText("Current Directory: " + directory.toString());
		}
	}
}
