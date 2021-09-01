package com.apiEngine.routes;

public class Route {

	private static final String data="/data";
	private static final String version="/2.5";

	
	public static String weather() {
		return data + version + "/weather";
		
	}
}
