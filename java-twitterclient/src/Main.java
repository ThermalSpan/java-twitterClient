/* Main.java
 * Authored by Russell Bentley
 * 
 * 
 * 
 * 
 */
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;

import java.util.ArrayList;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.StatsReporter.StatsTracker;
import com.twitter.hbc.core.endpoint.Location;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import com.twitter.hbc.core.event.Event;


public class Main implements WindowListener{
	//GUI components
	private SetupPanel setupPanel;
	private StreamingPanel streamingPanel;
	private CardLayout cardLayout;
	private JPanel mainPane;
	private JFrame frame;
	//These are the parameters that the setup panel needs to get and finalize
	private ArrayList<Long> userList;
	private ArrayList<String> languageList;
	private ArrayList<Location> locationList;
	private ArrayList<String> phraseList;
	private String outputFile;
	//This object handles loading/getting the user secrets and tokens and such
	private APITokens api;
	//These allow the hbc client and other components to communicate
	private BlockingQueue<String> messageQueue;
	private BlockingQueue<Event> eventQueue;
	//The two ends of the application
	private Client hbcClient;
	private FileWriter fileWriter;
	private StatsTracker statsTracker;
	private java.util.Timer timer;
	

	public Main() {
		api = new APITokens();
		
		messageQueue = new LinkedBlockingQueue<String>(10000);
    	eventQueue = new LinkedBlockingQueue<Event>(1000);
    	fileWriter = new AsyncFilePrinter(messageQueue);
    	
    	userList = new ArrayList<Long>();
    	languageList = new ArrayList<String>();
    	locationList  = new ArrayList<Location>();
    	phraseList = new ArrayList<String>();
    	outputFile = "defualt.txt";
    	
    	setupPanel = new SetupPanel(this, languageList, locationList, userList, languageList);
		streamingPanel = new StreamingPanel(this, eventQueue);
		cardLayout = new CardLayout();
		mainPane = new JPanel(cardLayout);
		mainPane.add("setup", setupPanel);
		mainPane.add("streaming", streamingPanel);
		
    
    	JFrame frame = new JFrame("Twitter Client");
    	frame.addWindowListener(this);
    	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	
    	frame.setLayout(cardLayout);
    	frame.add(mainPane);
    	cardLayout.show(mainPane, "setup");
    	frame.pack();
    	frame.setVisible(true);
    	
	}
	
	public void buildStreamClient(){
		/*We need the host to connect to, the endpoint and the authentication*/
		//Hosts hbcHosts = new HttpHosts(Constants.STREAM_HOST);
		StatusesFilterEndpoint hbcEndpoint = new StatusesFilterEndpoint();
		Authentication hbcAuth = new OAuth1(api.getConsumerKey(), api.getConsumerSecret(), api.getToken(), api.getSecret());

		/*We add our stream parameters to the endpoint object*/
		if(!userList.isEmpty()) hbcEndpoint.followings(userList);
		if(!languageList.isEmpty()) hbcEndpoint.languages(languageList);
		if(!locationList.isEmpty()) hbcEndpoint.locations(locationList);
		if(!phraseList.isEmpty()) hbcEndpoint.trackTerms(phraseList);
		
		System.out.println(hbcEndpoint.getURI());
		System.out.println(hbcEndpoint.getPostParamString());

		/*We now setup the client builder and return the result */ 
		ClientBuilder builder = new ClientBuilder()
	  		.hosts(Constants.STREAM_HOST)
	  		.authentication(hbcAuth)
	 		.endpoint(hbcEndpoint)
	  		.processor(new StringDelimitedProcessor(messageQueue))
	  		.eventMessageQueue(eventQueue);      

	  	hbcClient = builder.build();
	  	statsTracker = hbcClient.getStatsTracker();
	  	
	  	System.out.println(hbcClient.getName());
	}	
	
	public void startStream() {
		try{
			//Get finalized parameters from setup Panel
			setupPanel.finalize();
			for(Long l: userList) System.out.println(l);
			//Build the client
			buildStreamClient();
			//Switch the GUI to the streaming mode
			cardLayout.show(mainPane, "streaming");
			//Start the streaming event thread
			streamingPanel.start();
			hbcClient.connect();
			
			//new Thread(new messagePrinter(messageQueue, statsTracker)).start();
			
			//System.out.println("About to make file writer" + outputFile);
			//fileWriter.open(new File(outputFile));
			//new Thread(fileWriter).start();
		} catch(Exception e) {
			System.out.println("Problem starting stream" + e.toString());
		}		
	}
	
	public void closeStream(){
		if(hbcClient != null) hbcClient.stop();
		if(fileWriter != null) fileWriter.close();
		streamingPanel.stop();
		if(timer != null) timer.cancel();
		timer = null;
		cardLayout.show(mainPane, "setup");
	}
	
	public long getMessageCount(){
		return statsTracker.getNumMessages();
	}
	
	//Methods for the WindowListener interface
	public void windowActivated(WindowEvent arg0) {}
	public void windowDeactivated(WindowEvent arg0) {}
	public void windowDeiconified(WindowEvent arg0) {}
	public void windowIconified(WindowEvent arg0) {}
	public void windowOpened(WindowEvent arg0) {}
	public void windowClosing(WindowEvent arg0) {closeStream();}	
	public void windowClosed(WindowEvent arg0) {}

	public void setTimer(long delay) {
		//Run as deamon thread, will not hold the shutdown of application
		timer = new java.util.Timer(true);
		final Main main = this;
		timer.schedule(new TimerTask(){
			public void run() {
				main.closeStream();
			}
		}, delay);
	}
	
	public void setoutputFile(String outputFile){
		this.outputFile = outputFile;
	}

	public static void main(String[] args) {
		new Main();
	}

}
