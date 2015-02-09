

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.JOptionPane;

/**
 * APITokens object.
 * 
 * <P> This handles loading the API tokens from the configuration file,
 *  or making a new configuration file if necessary. 
 *  
 * @author thermalspan
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
		//First We try to read an existing config file. If found the user has the choice to use it. 
		try {
			FileReader fr = new FileReader(configFileName);
			BufferedReader br = new BufferedReader(fr);
			consumerKey = br.readLine();
			consumerSecret = br.readLine();
			token = br.readLine();
			secret = br.readLine();
			br.close();
			
			int choice = JOptionPane.showConfirmDialog(null, "An existing configuration file was found. Do you want to use it?");
			switch(choice) {
			case 0 : null;
				break;
			//User does not want to use existing configuration,
			case 1 : buildConfigFile();
				break;
			
			case 2 :
				break;
				
			}
		} catch (Exception e) {
			buildConfigFile();
			e.printStackTrace();
		}
		if(consumerKey == null | consumerSecret == null | token == null | secret == null) {
			buildConfigFile();
		}
	}
	
	//This method will take user input to make a new config file
	public void buildConfigFile() {
		System.out.println("There does not appear to be an existing config file:");

		try {
			//First we use the buffered read to get the information.
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Please enter Consumer Key:");
			consumerKey = br.readLine();
			System.out.println("Please enter Consumer Secret:");
			consumerSecret = br.readLine();
			System.out.println("Please enter Token:");
			token = br.readLine();
			System.out.println("Please enter Secret:");
			secret = br.readLine();
			br.close();
			
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
