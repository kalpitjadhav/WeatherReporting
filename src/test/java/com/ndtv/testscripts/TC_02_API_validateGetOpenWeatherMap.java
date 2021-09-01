package com.ndtv.testscripts;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.io.IOException;
import java.util.Map;

import org.testng.annotations.Test;
import com.apiEngine.endpoints.Endpoints;
import com.ndtv.reporting.Reporting;
import com.ndtv.ui.lib.Messages;
import com.ndtv.ui.lib.Utilities;

import io.restassured.response.Response;


public class TC_02_API_validateGetOpenWeatherMap {


	private static Map<String,String> objTestData;
	private static String TestCaseName="TC_02_API_validateGetOpenWeatherMap";
	private static String sFilePath="TestData\\WeatherReport.xls";
	private static String sSheetName="Weather";

	@Test
	public static void validateApiWeatherDetails() throws HeadlessException, IOException, AWTException {
		Reporting.Functionality = "Weather Report";
		Reporting.Testcasename = TestCaseName;

		//read test data from excel sheet
		objTestData=Utilities.readTestData(sFilePath, sSheetName, TestCaseName);

		//Call API for given city and measurement unit for weather info
		Response weatherResponse=Endpoints.getWeatherFromAPI(objTestData.get("City"), objTestData.get("MeasurementUnit"));
		float temp=weatherResponse.body().jsonPath().get("main.temp");

		//verify status code
		if(weatherResponse.statusCode()==200) {
			Reporting.logResults("Pass", "Get weather details for "+objTestData.get("City") , "Weather details are retrived successfully and temperatur of "+objTestData.get("City")+" is "+temp );	
		}
		else {
			Reporting.logResults("Fail", "Get weather details for "+objTestData.get("City"), "Unable to get Weather detail  Due to.... "+Messages.errorMsg);
		}
		return;
	}


}
