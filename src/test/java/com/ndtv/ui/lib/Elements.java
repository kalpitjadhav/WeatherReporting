package com.ndtv.ui.lib;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.ndtv.ui.basetest.BaseTest;




public class Elements extends BaseTest  
{

	private static boolean bStatus;
	private static int iWait=20;
	private static Logger logger=Logger.getLogger("Elements");

	/*******************************************************************************
	Function Name 					: enterText
	Description						: Enters the given text in the specified textbox
	Parameters						: objLocator, sValue
	Usage							: bStatus = enterText(objLocator, sValue)
	Created By						: Kalpit
	Created On						: 
	 * @throws InterruptedException 
	 ******************************************************************************/
	public static boolean enterText(By objLocator,String sValue) throws InterruptedException
	{
		bStatus = Wait.waitForElementVisibility(objLocator, iWait);
		if(bStatus)
		{
			scrollToViewElement(objLocator);
			driver.findElement(objLocator).click();
			driver.findElement(objLocator).clear();
			driver.findElement(objLocator).sendKeys(sValue);
			logger.info("The text "+sValue+" has been inputted successfully.");
			return true;
		}
		logger.warn("The text "+sValue+" could not be entered successfully");
		return false;	
	}



	/*******************************************************************************
	Function Name 					: clickElement
	Description						: Clicks the element identified by `objLocator`
	Parameters						: objLocator
	Usage							: bStatus = clickButton(objLocator)
	Created By						: kalpit
	Created On						: 
	 ******************************************************************************/
	public static boolean clickElement(By objLocator)
	{
		//bStatus = Verify.verifyElementVisible(objLocator);
		bStatus=Wait.waitForElementVisibility(objLocator, iWait);
		if(bStatus)
		{
			driver.findElement(objLocator).click();
			logger.info("The element "+objLocator+" has been clicked successfully");
			return true;
		}
		logger.warn("The element "+objLocator+" cannot be clicked due to: "+Messages.errorMsg);
		return false;
	}



	/*******************************************************************************
	Function Name 					: selectCheckbox
	Description						: Selects the required checkbox
	Parameters						: objLocator
	Usage							: bStatus = selectCheckbox(objLocator)
	Created By						: Kalpit
	Created On						: 
	 ******************************************************************************/
	public static boolean selectCheckbox(By objLocator) throws InterruptedException
	{
		bStatus = Verify.verifyElementVisible(objLocator);
		if(bStatus)
		{
			bStatus = driver.findElement(objLocator).isSelected();
			if(!bStatus)
			{
				driver.findElement(objLocator).click();
				logger.info("The check box has been selected successfully");
			}
			logger.info("The check box has been already selected");	
			Wait.waitForGivenTimeOut(10);
			return true;
		}
		logger.warn("Cannot check the CheckBox because "+Messages.errorMsg);
		return false;
	}


	/*******************************************************************************
	Function Name 					: scrollToViewElement
	Description						: scroll into view of the element
	Parameters						: objLocator
	Usage							: bStatus = scrollToViewElement(objLocator)
	Created By						: Kalpit
	Created On						: 
	 ******************************************************************************/
	public static boolean scrollToViewElement(By objLocator) throws InterruptedException
	{
		try
		{
			WebElement element = BaseTest.driver.findElement(objLocator);
			((JavascriptExecutor) BaseTest.driver).executeScript("arguments[0].scrollIntoView(true);", element);
			return true;
		}

		catch(Exception e)
		{
			Messages.errorMsg= e.getMessage();
			return false;	
		}

	}

	/*******************************************************************************
	Function Name 					: getWebElements
	Description						: Returns the list of web elements of the given element locator
	Parameters						: (By objLocator)
	Usage							: Webelement ele = getWebElements(objLocator)
	Created By						: kalpit
	Created On						: 
	 ******************************************************************************/
	public static List<WebElement> getWebElements(By objLocator)
	{
		bStatus = Verify.verifyElementVisible(objLocator);
		if(bStatus)
		{
			logger.info("The Elements with locator "+objLocator+" is visible and can be used");
			return driver.findElements(objLocator);
		}
		logger.warn("The Element with locator "+objLocator+" is not visible and cannot be used");
		return null;
	}
	
	/*******************************************************************************
	Function Name 					: getWebElement
	Description						: Returns the web element of the given element locator
	Parameters						: (By objLocator)
	Usage							: Webelement ele = getWebElement(objLocator)
	Created By						: Kalpit
	Created On						: 
 ********************************************************************************/
	public static WebElement getWebElement(By objLocator)
	{
		bStatus = Verify.verifyElementVisible(objLocator);
		if(bStatus)
		{
			logger.info("The Element "+objLocator+" is visible and can be used");
			return driver.findElement(objLocator);
		}
		logger.warn("The Element "+objLocator+" is not visible and cannot be used");
		return null;
	}



}
