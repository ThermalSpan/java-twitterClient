/* UserPanel.java
 * Authored by Russell Bentley
 * 
 * This is the parameter panel for adding users.
 * This is where incorrect input is handled. See the addbutton's listener. 
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
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class UserPanel extends JPanel{
	private ArrayList<Long> userList;
	final private DefaultListModel<Long> dataModel;
	
	public UserPanel(ArrayList<Long> userList){
		super();
		this.userList = userList;
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		final JLabel title = new JLabel("Users");
		this.add(title);
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		dataModel = new DefaultListModel<Long>();
		final JList<Long> optionList = new JList<Long>(dataModel);
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
		final JButton addButton = new JButton("Add User");
		final JButton removeButton = new JButton("Del. User");
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
						Long user = Long.parseLong(text, 10);
						dataModel.addElement(user);
					} catch(Exception error){
						JOptionPane.showMessageDialog(null,
								"Your input did not have the correct form:\n"
								+ "\"<Long>\" \n"
								+ "You should enter a user ID number.\n"
								+ "Your particular error was:\n"
								+ error.toString(), "Format Error",
							    JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});

		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(Long l: optionList.getSelectedValuesList()){
					dataModel.removeElement(l);
				}
			}
		});

	}
	
	public void finalizeList() {
		userList.clear();
		//Note that this is Oracle's suggested use case as of java 7
		for(Enumeration<Long> e = dataModel.elements(); e.hasMoreElements();){
			userList.add(e.nextElement());
		}
	}
}
