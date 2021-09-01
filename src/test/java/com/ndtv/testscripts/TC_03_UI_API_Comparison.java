package com.ndtv.testscripts;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.io.IOException;
import java.util.Map;

import org.testng.annotations.Test;

import com.ndtv.reporting.Reporting;
import com.ndtv.ui.basetest.BaseTest;
import com.ndtv.ui.lib.GenericFunctions;
import com.ndtv.ui.lib.Utilities;




public class TC_03_UI_API_Comparison extends BaseTest {
	private static boolean bStatus;
	private static String TestCaseName="TC_03_UI_API_Comparison";
	private static String sFilePath="TestData\\WeatherReport.xls";
	private static String sSheetName="Weather";
	private static Map<String,String> objTestData;
	
	@Test
	public static void compareUiWithApi() throws InterruptedException, HeadlessException, IOException, AWTException {
		Reporting.Functionality = "Weather Report";
		Reporting.Testcasename = TestCaseName;
		
		//read test data from excel sheet
		objTestData=Utilities.readTestData(sFilePath, sSheetName, TestCaseName);
		
		//Verify that the Temperature received from UI is matching with Temperature received from API
		bStatus=GenericFunctions.verifyApiVsUi(objTestData.get("Module"), objTestData.get("City"), objTestData.get("MeasurementUnit"),Integer.parseInt(objTestData.get("variance")));
		if(!bStatus) {
			Reporting.logResults("Fail", "Validate API response for temperature against UI weather details", "Temperature from UI and API is not matching");
			return;
		}
		else {
			Reporting.logResults("Pass", "Validate API response for temperature against UI weather details" , "Temperature from UI and API is matching and is within specified variance limit");
		}
	}

}
