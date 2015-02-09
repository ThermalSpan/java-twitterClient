/* LocationPanel.java
 * Authored by Russell Bentley
 * 
 * This is the parameter panel for adding locations.
 * This is where incorrect input is handled. See the addbutton's listener. 
 * 
 */

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.twitter.hbc.core.endpoint.Location;
import com.twitter.hbc.core.endpoint.Location.Coordinate;

public class LocationPanel extends JPanel{
	private ArrayList<Location> locationList;
	final private DefaultListModel<Location> dataModel;
	
	public LocationPanel(ArrayList<Location> locationList){
		super();
		this.locationList = locationList;
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		final JLabel title = new JLabel("Locations");
		this.add(title);
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		dataModel = new DefaultListModel<Location>();
		final JList<Location> optionList = new JList<Location>(dataModel);
		final JScrollPane scrollPane = new JScrollPane(optionList);
		this.add(scrollPane);
		scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

		final JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.LINE_AXIS));
		final JLabel inputLabel = new JLabel("input:");
		final JTextField textBox = new JTextField();
		textBox.setMaximumSize(new Dimension(100,20));
		inputPanel.add(inputLabel);
		inputPanel.add(Box.createRigidArea(new Dimension(5,0)));
		inputPanel.add(textBox);
		this.add(inputPanel);
		inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		final JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		final JButton addButton = new JButton("Add Location");
		final JButton removeButton = new JButton("Del. Location");
		buttonPanel.add(addButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(5,0)));
		buttonPanel.add(removeButton);
		this.add(buttonPanel);
		buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = textBox.getText();
				textBox.setText("");
				if(!(text.equals(""))) {
					try{
						String[] input = text.split(",", 6);
						if(input.length > 4) throw new Exception("More than Four Tokens");
						if(input.length < 4) throw new Exception("Less than Four Tokens");
						double lon1 = Double.parseDouble(input[0]);
						double lat1 = Double.parseDouble(input[1]);
						double lon2 = Double.parseDouble(input[0]);
						double lat2 = Double.parseDouble(input[1]);
						Coordinate c1 = new Coordinate(lon1, lat1);
						Coordinate c2 = new Coordinate(lon2, lat2);
						dataModel.addElement(new Location(c1, c2));
					} catch(Exception error){
						JOptionPane.showMessageDialog(null,
							    "Your input did not have the correct form:\n"
							    + "\"<double>, <double>, <double>, <double> \" \n"
							    + "You should enter the two points that defined a bounding box.\n"
							    + "Your particular error was:\n"
							    + error.toString(), "Format Error",
							    JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});

		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(Location l: optionList.getSelectedValuesList()){
					dataModel.removeElement(l);
				}
			}
		});

	}
	
	public void finalizeList() {
		locationList.clear();
		//Note that this is Oracle's suggested use case as of java 7
		for(Enumeration<Location> e = dataModel.elements(); e.hasMoreElements();){
			locationList.add(e.nextElement());
		}
	}
}
