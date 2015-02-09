

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

/**
 * APITokens object.
 * 
 * <P> This handles loading the API tokens from the configuration file,
 *  or making a new configuration file if necessary. 
 *  
 * @author Russell Bentley
 *
 */
public class APITokens {
	String configFileName;
	
	private String consumerKey;
	private String consumerSecret; 
	private String token;
	private String secret;
	
	public APITokens() {
		configFileName = System.getProperty("user.home")+System.getProperty("file.separator")+".twitterclient";
		
		try {
			//First We try to read an existing config file. If found the user has the choice to use it. 
			FileReader fr = new FileReader(configFileName);
			BufferedReader br = new BufferedReader(fr);
			consumerKey = br.readLine();
			consumerSecret = br.readLine();
			token = br.readLine();
			secret = br.readLine();
			br.close();
			
			int choice = JOptionPane.showConfirmDialog(null, "An existing configuration file was found. Do you want to use it?");
		
			//User does not want to use existing configuration,
			if(choice == 1) buildConfigFile();
			//User hits cancel, exit?
			if(choice == 2) System.exit(0);
			
		} catch (Exception e) {
			buildConfigFile();
			e.printStackTrace();
		}
		if(consumerKey == null | consumerSecret == null | token == null | secret == null) {
			JOptionPane.showInternalMessageDialog(null, "Invalid configuration file found. Time for a new one!");
			buildConfigFile();
		}
	}
	
	//This method will take user input to make a new config file
	public void buildConfigFile() {
		System.out.println("There does not appear to be an existing config file:");

		try {
			//We use JOptionPanes to get the information.
			consumerKey = JOptionPane.showInputDialog("Please enter Consumer Key:");
			consumerSecret = JOptionPane.showInputDialog("Please enter Consumer Secret:");
			token = JOptionPane.showInputDialog("Please enter Token:");
			secret = JOptionPane.showInputDialog("Please enter Secret:");
			
			//Then we use a Print Writer to make the config file. 
			PrintWriter writer = new PrintWriter(configFileName, "UTF-8");
			writer.println(consumerKey);
			writer.println(consumerSecret);
			writer.println(token);
			writer.println(secret);
			writer.close();
		} catch (Exception e) {
			System.out.println("There was an error while getting user input:");
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(1);
		} 
	}
	
	public String getConsumerKey() {
		return consumerKey;
	}
	
	public String getConsumerSecret() {
		return consumerSecret;
	}
	
	public String getToken() {
		return token;
	}
	
	public String getSecret() {
		return secret;
	}
}
