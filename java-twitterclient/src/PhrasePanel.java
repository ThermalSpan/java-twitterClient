/* PhrasesPanel.java
 * Authored by Russell Bentley
 * 
 * This is the parameter panel for adding terms or phrases.
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

public class PhrasePanel extends JPanel{
	private ArrayList<String> phraseList;
	final private DefaultListModel<String> dataModel;
	
	public PhrasePanel(ArrayList<String> phraseList){
		super();
		this.phraseList = phraseList;
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		final JLabel title = new JLabel("Phrases");
		this.add(title);
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		dataModel = new DefaultListModel<String>();
		final JList<String> optionList = new JList<String>(dataModel);
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
		final JButton addButton = new JButton("Add Phrase");
		final JButton removeButton = new JButton("Del. Phrase");
		buttonPanel.add(addButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(5,0)));
		buttonPanel.add(removeButton);
		this.add(buttonPanel);
		buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = textBox.getText();
				textBox.setText("");
				if(!(text.equals(""))) dataModel.addElement(text);
			}
		});


		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(String s: optionList.getSelectedValuesList()){
					dataModel.removeElement(s);
				}
			}
		});
		
	}
	
	public void finalizeList() {
		phraseList.clear();
		//Note that this is Oracle's suggested use case as of java 7
		for(Enumeration<String> e = dataModel.elements(); e.hasMoreElements();){
			String s = e.nextElement();
			phraseList.add(s);
			System.out.println(s);
		}
	}
}
