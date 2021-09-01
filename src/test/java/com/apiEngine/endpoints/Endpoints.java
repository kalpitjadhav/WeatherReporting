package com.apiEngine.endpoints;

import org.apache.log4j.Logger;
import com.apiEngine.routes.Route;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Endpoints {

	private static final String BASE_URL="https://api.openweathermap.org"; 
	private static Logger logger=Logger.getLogger("Endpoints");
	
	
	/*******************************************************************************
	Function Name 					: getWeatherFromAPI
	Description						: Get response from the weather api
	Parameters						: city,unit
	Usage							: Response=getWeatherFromAPI("pune","metric")
	Created By						: Kalpit
	Created On						: 

	 ******************************************************************************/
	public static Response getWeatherFromAPI(String city,String unit) {
		RestAssured.baseURI =BASE_URL; 
		RequestSpecification request = RestAssured.given();

		Response response = request.queryParam("q", city) 
				.queryParam("units", unit)
				.queryParam("appid", "7fe67bf08c80ded756e598d6f8fedaea")
				.get(Route.weather());
		
		logger.info("Status code of the response is : "+ response.getStatusCode());
		return response;

	}
	
	
	public static Response getWeather(String city,String unit) {
		RestAssured.baseURI =BASE_URL; 
		RequestSpecification request = RestAssured.given();

		Response response = request.queryParam("q", city) 
				.queryParam("units", unit)
				.queryParam("appid", "7fe67bf08c80ded756e598d6f8fedaea")
				.get(Route.weather());
		
		logger.info("Status code of the response is : "+ response.getStatusCode());
		return response;

	}

}
