package com.ndtv.testscripts;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.ndtv.reporting.Reporting;
import com.ndtv.ui.basetest.BaseTest;
import com.ndtv.ui.lib.GenericFunctions;
import com.ndtv.ui.lib.Messages;
import com.ndtv.ui.lib.Utilities;;

public class TC_01_UI_validateWeatherDetails extends BaseTest{

	private static boolean bStatus;
	public static HashMap<String,String> objWeatherList;
	private static String TestCaseName="TC_01_UI_validateWeatherDetails";
	private static String sFilePath="TestData\\WeatherReport.xls";
	private static String sSheetName="Weather";
	private static Map<String,String> objTestData;

	@Test
	public static void validateCityWeather() throws HeadlessException, IOException, AWTException, InterruptedException {
		// initialize variables for reporting
		Reporting.Functionality = "Weather Report";
		Reporting.Testcasename = TestCaseName;

		//read test data from excel sheet
		objTestData=Utilities.readTestData(sFilePath, sSheetName, TestCaseName);

		//Step 1 navigate to Weather menu
		bStatus=GenericFunctions.navigateToWeatherMenu(objTestData.get("Module"));
		if(!bStatus) {
			Reporting.logResults("Fail", "Navigate to "+ objTestData.get("Module") , "Unable to Navigate to "+ objTestData.get("Module") + " Due to.... "+Messages.errorMsg);
			return;
		}
		else {
			Reporting.logResults("Pass", "Navigate to "+ objTestData.get("Module") +" page" , "successfully navigated to "+ objTestData.get("Module") + " page");
		}



		//Step 2 Validate that the corresponding city is available on the map with temperature information
		bStatus=GenericFunctions.getCityOnMap(objTestData.get("City"));
		if(!bStatus) {
			Reporting.logResults("Fail", "Validate that the corresponding city is available on the map with temperature details", "Unable to validate city and temperature for "+ objTestData.get("Module") + " Due to.... "+Messages.errorMsg);
			return;
		}
		else {
			Reporting.logResults("Pass", "Validate that the corresponding city is available on the map with temperature details" , "successfully validated City and temperature on the map");
		}

		// Step 3  Validate that selecting any city on the map reveals the weather details
		objWeatherList=GenericFunctions.getWeatherDetails(objTestData.get("City"));
		if((objWeatherList.isEmpty())) {
			Reporting.logResults("Fail", "Validate all weather details on the panel for "+ objTestData.get("City"), "Weather details are not visible for "+ objTestData.get("City")+ " Due to.... "+Messages.errorMsg );
		}
		else {
			Reporting.logResults("Pass", "Validate all weather details on the panel for "+ objTestData.get("City"), "Weather details are visible for "+ objTestData.get("City") );
		}
		return;
	}

}
