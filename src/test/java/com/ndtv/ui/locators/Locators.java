package com.ndtv.ui.locators;


public class Locators 
{
	public static String sXpath = "";

	public static class HomeNDTV {

		public static String SetNotificationWindow(String sText) {
			sXpath="//div[@class='noti_wrap']//*[contains(text(),'"+sText+"')]";
			return sXpath;
		}

		public static String buttonHeaderMore() {
			sXpath="//a[@id='h_sub_menu']";
			return sXpath;
		}

		public static String subMenu(String sText) {
			sXpath="//div[@id='subnav']//*[contains(text(),'"+sText+"')]";
			return sXpath;
		}

	}
	public static class weatherNDTV{

		public static String textSearchBox() {
			sXpath="//*[@id='searchBox']";
			return sXpath;
		}
		
		public static String checkBox(String sText) {
			sXpath="//label//input[@id='"+sText+"']";
			return sXpath;
		}
		
		public static String getCityOnMap(String sText) {
			sXpath="//div[@class='cityText' and text()='"+sText+"']";
			return sXpath;
		}
		
		public static String getTempOnMap(String sText) {
			sXpath="//div[@class='cityText' and text()='"+sText+"']"+"//preceding-sibling::div//span";
			return sXpath;
		}
		
		public static String readCityWeatherDetails(String sText) {
			sXpath="//span[contains(text(),'"+sText+"')]//parent::div//following-sibling::span//b";
			return sXpath;
		}
		public static String verifyCityOnPanel(String sText) {
			sXpath="//span[contains(text(),'"+sText+"')]";
			return sXpath;
		}
		
	}





}
