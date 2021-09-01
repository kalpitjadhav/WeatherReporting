package com.ndtv.ui.lib;

import java.util.Set;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.ndtv.ui.basetest.BaseTest;

import io.github.bonigarcia.wdm.WebDriverManager;


public class Browser extends BaseTest
{
	private static Logger logger=Logger.getLogger("Browser");
	private static WebDriver wDriver;


	/**
	 * Opens a new browser instance to given URL.

	 * @author Kalpit
	 */
	public static WebDriver openBrowser(String sBrowserName,String sURL)
	{
		if(sBrowserName.equalsIgnoreCase("chrome")){
			logger.info("The application has been invoked successfully in Google Chrome with URL:"+sURL);
			return openChromeBrowser(sURL);
		}

		else {
			Messages.errorMsg="No browser drivers found";
			logger.warn("The application could not be invoked due to "+Messages.errorMsg);
			return wDriver;
		}	
	}



	/**
	 * Opens a new chrome browser instance to given URL.

	 * @author Kalpit
	 */
	public static WebDriver openChromeBrowser(String sURL )
	{

		try{
			WebDriverManager.chromedriver().setup();
			wDriver = new ChromeDriver();	
			logger.info("Chrome browser is opened successfully");
			System.out.println("Chrome browser is opened successfully");
			wDriver.get(sURL);
			wDriver.manage().window().maximize();	
		}
		catch(Exception e)
		{
			Messages.errorMsg = e.getMessage();
			logger.warn(Messages.errorMsg);
		}

		return wDriver;
	}



	/**
	 * Close the current browser related to given driver instance.
	 * 
	 * @param wDriver	WebDriver object
	 * @return boolean
	 * @author kalpit
	 */
	public static boolean closeCurrentBrowser(WebDriver wDriver)
	{
		try{
			if(wDriver != null)
			{
				String sFocusedWindow = wDriver.getWindowHandle();
				Set<String> windows = wDriver.getWindowHandles();
				int iSize = windows.size();
				if(iSize > 1)
				{
					wDriver.close();
					for (String handle : windows) 
					{
						if(!sFocusedWindow.equalsIgnoreCase(handle))
						{
							wDriver.switchTo().window(handle);
						}
					}
				}	
				wDriver.close();
				logger.info("The current browser has been closed successfully");
				Runtime.getRuntime().exec("taskkill /T /F /IM chromedriver.exe");
				return true;
			}
			logger.warn("The current browser could not be closed");
			return false;

		}
		catch(Exception e){
			Messages.errorMsg = e.getMessage();
			logger.warn("The current browser could not be closed due to "+Messages.errorMsg);
			return false;
		}
	}


}
