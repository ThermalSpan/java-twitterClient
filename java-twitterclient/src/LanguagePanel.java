/*
 * LanguagePanel.java
 * Authored by Russell Bentley
 * 
 * 
 */

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class LanguagePanel extends JPanel{
	private String Languages[][];
	private ArrayList<String> languageList;
	final private DefaultListModel<Language> dataModel;
	

	public LanguagePanel(ArrayList<String> languageList) {
		super();
		this.languageList = languageList;
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		final JLabel title = new JLabel("Languages");
		this.add(title);
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		dataModel = new DefaultListModel<Language>();
		final JList<Language> optionList = new JList<Language>(dataModel);
		final JScrollPane scrollPane = new JScrollPane(optionList);
		this.add(scrollPane);
		scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		//This information can be retrieved though a REST Api, this should be added as a feature in the future. 
		String Languages[][] = {{"fr","French"},{"en","English"},{"ar","Arabic"},{"ja","Japanese"},{"es","Spanish"},{"de","German"},{"it","Italian"},{"id","Indonesian"},{"pt","Portuguese"},{"ko","Korean"},{"tr","Turkish"},{"ru","Russian"},{"nl","Dutch"},{"fil","Filipino"},{"msa","Malay"},{"zh-tw","Traditional Chinese"},{"zh-cn","Simplified Chinese"},{"hi","Hindi"},{"no","Norwegian"},{"sv","Swedish"},{"fi","Finnish"},{"da","Danish"},{"pl","Polish"},{"hu","Hungarian"},{"fa","Farsi"},{"he","Hebrew"},{"ur","Urdu"},{"th","Thai"},{"en-gb","English UK"}};
		final JComboBox<Language> comboBox = new JComboBox<Language>();
		for(int i = 0; i<29; i++){
			comboBox.addItem(new Language(Languages[i][1],Languages[i][0]));
		}
		
		this.add(comboBox);
		
		final JButton removeButton = new JButton("Del. Language");
		this.add(removeButton);
		removeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		

		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(Language l: optionList.getSelectedValuesList()){
					dataModel.removeElement(l);
				}
			}
		});
		
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dataModel.addElement(comboBox.getItemAt(comboBox.getSelectedIndex()));
			}
		});
	}

	public void finalizeList() {
		languageList.clear();
		//Note that this is Oracle's suggested use case as of java 7
		for(Enumeration<Language> e = dataModel.elements(); e.hasMoreElements();){
			languageList.add(e.nextElement().identifier);
		}
	}
	
	public class Language{
		String name;
		String identifier;
		public Language(String name, String identifier){
			this.name = name;
			this.identifier = identifier;
		}

		public String toString(){
			return name; 
		}
		
	}

}
