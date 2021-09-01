package com.ndtv.ui.basetest;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import com.ndtv.ui.lib.Browser;
import com.ndtv.ui.lib.Messages;
import com.ndtv.ui.lib.Utilities;

public class BaseTest
{
	public static String sBrowser = "";		
	public static String sTestSiteURL = "";
	public static String sChromeDriverPath = "";
	public static Map<String, String> objProperties = null;
	public static WebDriver driver;

	/**
	 * Launches the test webdriver browser based on the given values in the app.properties 

	 */
	@BeforeClass

	public static void launchBrowser() throws IOException {
		try {
			//read properties file and set local variable values
			readPropertiesFileNSetLocalVars();

			//open the browser session
			if(sBrowser.equalsIgnoreCase("chrome") || sBrowser.equalsIgnoreCase("cr"))
				driver = Browser.openBrowser(sBrowser,sTestSiteURL);
			else
			{
				Messages.errorMsg="No browser drivers found";
				return;
			}
			
			//set the default wait and timeout values 
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

	}


	/**
	 * Reads the app.properties file from the project's working directory and assigns the values to local variables.

	 * @author Kalpit
	 */
	public static void readPropertiesFileNSetLocalVars()
	{
		String sKey = "";
		String sValue = "";
		Properties p = new Properties();
		FileReader reader;
		try
		{
			objProperties = new HashMap<String, String>();

			reader = new FileReader("app.properties");
			p.load(reader);
			reader.close();

			//get all the keys defined in properties file
			Set<Object> keys = p.keySet();

			//get each key and value. Put them in a hash map.
			for(Object k:keys)
			{
				sKey = (String)k;
				sValue =  p.getProperty(sKey);
				objProperties.put(sKey, sValue);
			}

			sTestSiteURL = objProperties.get("test_url");
			sBrowser = objProperties.get("browser");
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	/**
	 * Closes the WebDriver browser after the test suite is complete.
	 * 
	 * @author Kalpit

	 */
	@AfterClass(alwaysRun = true)
	public static void closeBrowser() {
		String methodID = "closeBrowser";
		Browser.closeCurrentBrowser(driver);
		System.out.println(methodID + ": the WebDriver '" + sBrowser + "' browser was closed.");
		System.out.println("Test End Date & Time: " + Utilities.generateRandString());
		System.out.println("");
	}

}
