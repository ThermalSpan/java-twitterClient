/* StreamingPanel.java
 * Authored by Russell Bentley
 * 
 * This is what is displayed while there is a stream ongoing.
 * Notably it has a Jlist that displays all events, which is updated by a seperate thread. 
 */


import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.twitter.hbc.core.endpoint.Location;
import com.twitter.hbc.core.event.Event;

public class StreamingPanel extends JPanel implements Runnable{
	private boolean streaming;
	private Main main;
	private DefaultListModel<String> dataModel;
	private BlockingQueue<Event> eventQueue;
	private JButton stop;
	private JLabel messageCount;

	public StreamingPanel(final Main main, BlockingQueue<Event> eventQueue) {
		super();
		this.eventQueue = eventQueue;
		this.main = main;
		streaming = false;
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		JLabel title = new JLabel("Events:");
		this.add(title);
		
		dataModel = new DefaultListModel<String>();
		JList<String> optionList = new JList<String>(dataModel);
		JScrollPane scrollPane = new JScrollPane(optionList);
		scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(scrollPane);
		
		stop = new JButton("End Stream");
		messageCount = new JLabel("N.A");
		this.add(messageCount);
		this.add(stop);
		
		stop.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				main.closeStream();
			}
		});	
	}
	
	public void run(){
		while(streaming){
			try{
				Event e = eventQueue.poll(100, TimeUnit.MICROSECONDS);
				if(e != null) dataModel.addElement(e.getMessage());
			} catch(Exception e){}
			
			messageCount.setText(Long.toString(main.getMessageCount()));
		}
	}
	
	public void start(){
		streaming = true;
		new Thread(this).start();
	}
	
	public void stop(){
		streaming = false;
		dataModel.clear();
	}

}
