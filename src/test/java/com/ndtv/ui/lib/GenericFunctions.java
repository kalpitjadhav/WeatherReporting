package com.ndtv.ui.lib;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import com.apiEngine.endpoints.Endpoints;
import com.ndtv.ui.basetest.BaseTest;
import com.ndtv.ui.locators.Locators;
import io.restassured.response.Response;


public class GenericFunctions extends BaseTest {
	private static Logger logger=Logger.getLogger("GenericFunctions");
	private static boolean bStatus;
	private static int iWait=30;
	private static List<WebElement> ldetails;
	private static String temp="";
	private static String[] arrValues;
	private static HashMap<String,String> mapDetails= new HashMap<String, String>();


	/*******************************************************************************
	Function Name 					: navigateToWeatherMenu
	Description						: Navigate to submenu from the header
	Parameters						: module name
	Usage							: bStatus=navigateToWeatherMenu("Weather")
	Created By						: Kalpit
	Created On						: 

	 ******************************************************************************/
	public static boolean navigateToWeatherMenu(String sText) {


		try {

			String sAcceptNotification="No Thanks";
			bStatus=Wait.waitForElementVisibility(By.xpath(Locators.HomeNDTV.SetNotificationWindow(sAcceptNotification)),iWait);
			if(bStatus) {
				bStatus=userActions.click(By.xpath(Locators.HomeNDTV.SetNotificationWindow(sAcceptNotification)));
				if(!bStatus) return false;
			}
			bStatus=Wait.waitForElementToBeClickable(By.xpath(Locators.HomeNDTV.buttonHeaderMore()), iWait);
			if(!bStatus) return false;
			bStatus=userActions.click(By.xpath(Locators.HomeNDTV.buttonHeaderMore()));
			if(!bStatus) return false;

			bStatus=Elements.clickElement(By.xpath(Locators.HomeNDTV.subMenu(sText)));
			if(!bStatus) return false;
			logger.info("Successfully navigated to Module"+ sText);
			return bStatus;
		}
		catch(Exception e) {
			logger.warn("Not able to navigate to Module "+ sText+ " due to ..... "+ e.getMessage());
			Messages.errorMsg=e.getMessage();
			return false;
		}

	}

	/*******************************************************************************
	Function Name 					: getCityOnMap
	Description						: open panel to capture weather details and verify if temp is visible
	Parameters						: city
	Usage							: bStatus=getCityOnMap("pune")
	Created By						: Kalpit
	Created On						: 

	 ******************************************************************************/

	public static boolean getCityOnMap(String sText) throws InterruptedException {

		bStatus=Elements.enterText(By.xpath(Locators.weatherNDTV.textSearchBox()), sText);
		if(!bStatus) return bStatus;
		bStatus=Elements.selectCheckbox(By.xpath(Locators.weatherNDTV.checkBox(sText)));
		if(!bStatus) return bStatus;
		ldetails=Elements.getWebElements(By.xpath(Locators.weatherNDTV.getTempOnMap(sText)));
		if(!(ldetails.isEmpty())) {
			for(WebElement c:ldetails) {
				temp=c.getText();
				if(temp.isBlank()) {
					logger.warn("Temperature is not visible on the UI panel");
					return false;
				}
			}
		}

		else {
			logger.warn("Not able to see city on map");
			return false;	
		}
		logger.info("Successfully opened panel to capture weather details");
		return bStatus;

	}

	/*******************************************************************************
	Function Name 					: getWeatherDetails
	Description						: capture weather details from weather panel on the map
	Parameters						: city
	return type						: hashmap with all weather details
	Usage							: hashmap=getWeatherDetails("pune")
	Created By						: Kalpit
	Created On						: 

	 ******************************************************************************/

	public static HashMap<String,String> getWeatherDetails(String sCity) throws InterruptedException {
		// go to popup panel to get weather details of the city
		bStatus=Elements.clickElement(By.xpath(Locators.weatherNDTV.getCityOnMap(sCity)));
		if(bStatus) {
			if(bStatus) {
				bStatus=Verify.verifyElementVisible(By.xpath(Locators.weatherNDTV.verifyCityOnPanel(sCity)));

				ldetails=Elements.getWebElements(By.xpath(Locators.weatherNDTV.readCityWeatherDetails(sCity)));
				if(!(ldetails.isEmpty())) {
					for(WebElement c:ldetails) {
						temp="";
						temp=c.getText();
						arrValues=temp.split(":");
						if(!(arrValues[1].trim().isEmpty())){
							mapDetails.put(arrValues[0].trim(), arrValues[1].trim());
						}
						else {
							logger.warn("Value for field : "+ arrValues[0].trim() +" is empty");
						}

					}

				}

			}
		}
		logger.info("Captured weather details on the UI are :"+ mapDetails);
		return mapDetails;
	}


	/*******************************************************************************
	Function Name 					: verifyApiVsUi
	Description						: Verify that the Temperature received from UI is matching with Temperature received from API
	Parameters						: ModuleName, city,Unit,Variance
	return type						: Boolean
	Usage							: Boolean=verifyApiVsUi("Weather","Pune","metric","2")
	Created By						: Kalpit
	Created On						: 

	 ******************************************************************************/
	public static boolean verifyApiVsUi(String sModule, String city,String unit, int variance) throws InterruptedException{
		HashMap<String,String> Sweather=new HashMap<String, String>();

		// go to weather sub menu
		bStatus=navigateToWeatherMenu(sModule);
		if(!bStatus) return bStatus;

		//check if city is visible on the map
		bStatus=getCityOnMap(city);
		if(!bStatus) return bStatus;

		//capture weather details from the panel and save it in the hash map
		Sweather=getWeatherDetails(city);
		if(Sweather.isEmpty()) return false;

		// call API and get response
		Response response=Endpoints.getWeatherFromAPI(city, unit);
		if(response.statusCode()!=200) return false;

		//get temp from response
		float tempAPI=response.body().jsonPath().get("main.temp");

		// get Temp from UI retured hashmap and assign temp to variable depending on the unit
		float tempUI;
		if(unit.equalsIgnoreCase("metric")) {
			tempUI=Integer.parseInt(Sweather.get("Temp in Degrees"));
		}
		else 
			tempUI=Integer.parseInt(Sweather.get("Temp in Fahrenheit"));

		logger.info("Temperature from API is : " +tempAPI+ " Temperature from UI is : "+tempUI+ " , variance is : "+variance+ " and Unit of measurement is : "+unit);

		// check if temp returned from api and UI is within tolerance level
		if(compareWeatherValues(variance, tempAPI, tempUI)) {
			return true;
		}
		else
			return false;

	}


	/*******************************************************************************
	Function Name 					: compareWeatherValues
	Description						: Compare API temp and UI temp and if it is within variance retirn true
	Parameters						: variance, Temp from api, temp from UI
	return type						: Boolean
	Usage							: Boolean=compareWeatherValues(variance, tempAPI, tempUI)
	Created By						: Kalpit
	Created On						: 

	 ******************************************************************************/
	public static boolean compareWeatherValues(int variance,float tempAPI,float tempUI) {
		float Svariance=tempAPI-tempUI;
		float upprLimit=variance;
		float lowerLimit=variance*(-1);

		if(Svariance>=lowerLimit & Svariance<=upprLimit) {
			return true;
		}
		else
			logger.warn("Weather values are not matching while comparing it");
		return false;

	}

}
