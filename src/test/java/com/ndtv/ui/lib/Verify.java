package com.ndtv.ui.lib;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;

import com.ndtv.ui.basetest.BaseTest;





public class Verify extends BaseTest
{
	
	private static boolean bStatus;
	public static boolean bVisibleCheck = true;
	private static Logger logger=Logger.getLogger("Verify");


	/*******************************************************************************
	Function Name 					: verifyElementVisible
	Description						: verifies whether the given element is visible or not
	Parameters						: objLocator
	Usage							: bStatus = verifyElementVisible(objLocator)
	Created By						: kalpit
	Created On						: 
	 *******************************************************************************/
	public static boolean verifyElementVisible(By objLocator)
	{
		try
		{
			Wait.setQuickElementWait();
			if(bVisibleCheck)
			{
				bStatus = driver.findElement(objLocator).isDisplayed();
				if(!bStatus)
					Messages.errorMsg = "Element "+objLocator+ " is not visibile";

				logger.info("Element "+objLocator+"  visibility is :"+bStatus);
				Wait.resetElementWait();
				return bStatus;
			}
			else
			{
				logger.info("As the Visibility check is False, Just checking the presence of the element in DOM..");
				Wait.resetElementWait();
				return verifyElementPresent(objLocator);
			}
		}
		catch(Exception e)
		{
			Wait.resetElementWait();
			Messages.errorMsg =e.getMessage();
			logger.warn("Element "+objLocator+" is not visible.");
			return false;
		}
	}

	/*******************************************************************************
	Function Name 					: verifyElementPresent
	Description						: verifies the given element existence in DOM
	Parameters						: objLocator
	Usage							: bStatus = verifyElementPresent(objLocator)
	Created By						: Kalpit
	Created On						: 
	 ******************************************************************************/
	public static boolean verifyElementPresent(By objLocator)
	{
		try
		{
			driver.findElement(objLocator);
			logger.info("Element "+objLocator+" is present in DOM");
			return true;
		}
		catch(Exception e)
		{
			Messages.errorMsg =e.getMessage() ;
			logger.warn("Element "+objLocator+" is not present in DOM because "+Messages.errorMsg);
			return false;
		}
	}
	
	
}
