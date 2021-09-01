package com.ndtv.testscripts;

import java.util.ArrayList;

import org.testng.annotations.Test;

import com.apiEngine.routes.Route;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TestApi {
	
	
	@Test
	
	public static void checkUrKnowldege() {
		
		
		RestAssured.baseURI="https://api.openweathermap.org/data/2.5";
		
		RequestSpecification request= RestAssured.given();
		


		Response a= request.queryParam("q", "pune")
				.queryParam("appid", "7fe67bf08c80ded756e598d6f8fedaea")
				.queryParam("units", "metric")
				.get("/weather");
		
		
	
		System.out.println(a.statusCode());
		System.out.println(a.asString());
		//ArrayList<String> s = a.body().jsonPath().get("weather.id");
		//System.out.println(a.body().jsonPath().get("weather.id"));
		
	}

}
