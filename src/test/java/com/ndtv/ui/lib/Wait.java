package com.ndtv.ui.lib;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.ndtv.ui.basetest.BaseTest;



public class Wait extends BaseTest 
{

	private static WebDriverWait wait;
	private static Logger logger=Logger.getLogger("Wait");

	
	
	/********************************************************************************
	Function Name 					: waitForGivenTimeOut
	Description						: Wiats for the tive time out
	Parameters						: iTimeOut (milliseconds)
	Usage							: waitForGivenTimeOut(iTimeOut)
	Created By						: Kalpit
	Created On						: 
	 ******************************************************************************/
	public static void waitForGivenTimeOut(long iTimeOut) throws InterruptedException
	{
		Thread.sleep(iTimeOut);
		logger.info("Waited :"+iTimeOut + " milli seconds");
	}
	
	
	
	/*******************************************************************************
	Function Name 					: waitForElementToBeClickable
	Description						: Wait for the given element to be clickable
	Parameters						: objLocator, iTimeout(seconds)
	Usage							: bStatus = waitForElementVisibility(objLocator, iTimeout)
	Created By						: Kalpit
	Created On						: 
	 ******************************************************************************/

	public static boolean waitForElementToBeClickable(By objLocator,long iTimeout)
	{
		try{
			WebDriverWait wait2 = new WebDriverWait(driver, 10);
			wait2.until(ExpectedConditions.elementToBeClickable(objLocator));
			logger.info("element "+objLocator+" is clickable after waiting.");	
			return true;
		}
		catch(Exception e)
		{	
			Messages.errorMsg = e.getMessage();
			logger.warn("element "+objLocator+" is not clickable after waiting "+iTimeout+" secs.");
			return false;
		}
	}
	
	
	/*******************************************************************************
	Function Name 					: waitForElementVisibility
	Description						: Wait for the given element to be visible with a maximum time out of 'iTimOut" seconds.
	Parameters						: objLocator, iTimeout(seconds)
	Usage							: bStatus = waitForElementVisibility(objLocator, iTimeout)
	Created By						: Kalpit
	Created On						: 
	 ******************************************************************************/

	public static boolean waitForElementVisibility(By objLocator,long iTimeout)
	{
		try{
			wait = new WebDriverWait(driver,iTimeout);
			wait.until(ExpectedConditions.visibilityOfElementLocated(objLocator));
			logger.info("element "+objLocator+" is visible after waiting.");
			return true;
		}
		catch(Exception e)
		{	
			Messages.errorMsg = e.getMessage();
			logger.warn("element "+objLocator+" is not present after waiting "+iTimeout+" secs.");
			return false;
		}
	}
	
	public static boolean setQuickElementWait() {
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		return true;
	}
	
	
	public static boolean resetElementWait() {
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return true;
	}

}
