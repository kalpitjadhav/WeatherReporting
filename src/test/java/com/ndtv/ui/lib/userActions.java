package com.ndtv.ui.lib;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import com.ndtv.ui.basetest.BaseTest;

public class userActions extends BaseTest{
	private static Logger logger=Logger.getLogger("userActions");
	private static Actions act;
	
	/*********************************************************************************
	Function Name 					: click
	Description						: clicks given element 'objLocator'
	Parameters						: objLocator
	Usage							: bStatus = click(objLocator)
	Created By						: Kalpit
	Created On						: 
	 ******************************************************************************/
	public static boolean click(By objLocator)
	{
		WebElement wbElement = Elements.getWebElement(objLocator);
		if(wbElement == null)
		{
			logger.warn("The object "+objLocator+" cannot be clicked due to "+Messages.errorMsg);
			return false;
		}
		act = new Actions(driver);
		act.click(wbElement);
		act.perform();
		logger.info("The object "+objLocator+" has been clicked succesfully.");
		return true;
	}
}
