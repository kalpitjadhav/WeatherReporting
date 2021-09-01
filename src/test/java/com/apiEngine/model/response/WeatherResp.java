package com.apiEngine.model.response;

public class WeatherResp {
	
	public String base;
	public Clouds clouds;
	public Integer cod;
	public Coord coord;
	public Integer dt;
	public Integer id;
	public Main main;
	public String name;
	public Sys sys;
	public Integer timezone;
	public Integer visibility;
	public List<Weather> weather = null;
	public Wind wind;

}
