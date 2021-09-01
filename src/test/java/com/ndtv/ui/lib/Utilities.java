package com.ndtv.ui.lib;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;



import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class Utilities 
{
	private static Map<String, String> objMap = null;


	/*******************************************************************************
	Function Name 					: generateRandString
	Description						: This method will return a time-based, "random" string
	Parameters						: 
	Usage							: sRandomString = generateRandString()
	Created By						: Kalpit
	Created On						: 
	 ******************************************************************************/
	public static String generateRandString() {
		String rString = "";		
		String FORMAT = "MM-dd-yyyy-hh-mm-ss";		
		rString = new SimpleDateFormat(FORMAT).format(new GregorianCalendar().getTime());		
		return rString;				
	}


	/*******************************************************************************
	Function Name 					: readTestData
	Description						: read the test data from the given sheet & row in a file and store in a hash map
	Parameters						: sFilePath, sSheetName, sTestCaseName
	Usage							: bStatus = readTestData(sFilePath, sSheetName, sTestCaseName)
	Created By						: Kalpit
	Created On						: 
	 ********************************************************************************/
	public static Map<String, String> readTestData(String sFilePath,String sSheetName,String sTestCaseName) throws IOException
	{
		String sKey = null;
		String sValue = null;
		try
		{

			objMap = new HashMap<String, String>();	
			Workbook objWorkbook = Workbook.getWorkbook(new File(sFilePath));
			Sheet objSheet = objWorkbook.getSheet(sSheetName);
			int iRowCount = objSheet.getRows();
			int iColCount = objSheet.getColumns();
			for(int iRowCounter = 1;iRowCounter<iRowCount;iRowCounter++)
			{
				String sCurTestCaseName = objSheet.getCell(0,iRowCounter).getContents();
				if ((sCurTestCaseName.equalsIgnoreCase(sTestCaseName)))
				{		
					for(int iColCounter = 1;iColCounter<iColCount;iColCounter++)
					{
						sKey = objSheet.getCell(iColCounter,0).getContents();
						System.out.println(sKey);
						sValue = objSheet.getCell(iColCounter,iRowCounter).getContents();
						System.out.println(sValue);
						sValue = getDate(sValue);
						sValue = getTestDataUniqueValue(sValue);

						if((!sValue.equalsIgnoreCase("Null")) && (sValue.trim().length()!=0))
						{
							objMap.put(sKey,sValue);
						}
					}
					break;
				} 
			}
		}
		catch(BiffException e)
		{
			Messages.errorMsg = "Exception occured.." + e.getMessage();
		}
		return objMap;
	}


	/*******************************************************************************
	Function Name 					: getDate
	Description						: Format the given input to date format and return that value
	Parameters						: sValue
	Usage							: bStatus = readTestData(sValue)
	Created By						: Kalpit
	Created On						: 
	 ******************************************************************************/
	private static String getDate(String sValue) {

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String dt = sValue;

		if (sValue.trim().equalsIgnoreCase("Today"))
		{
			dt = sdf.format(cal.getTime());
		}

		if (sValue.trim().contains("Today_"))
		{
			String [] arrValues = sValue.split("_");
			int iDays = Integer.parseInt(arrValues[1]);
			cal.add(Calendar.DATE, iDays);
			//cal.
			dt = sdf.format(cal.getTime());
		}
		if (sValue.trim().contains("Today#"))
		{
			String [] arrValues = sValue.split("#");
			int iDays = Integer.parseInt(arrValues[1]);
			cal.add(Calendar.DATE, -iDays);
			//cal.
			dt = sdf.format(cal.getTime());
		}
		return dt;
	}
	
	
	/*******************************************************************************
	Function Name 					: getTestDataUniqueValue
	Description						: Format the given input to unique value
	Parameters						: sValue
	Usage							: bStatus = getTestDataUniqueValue(sValue)
	Created By						: Kalpit
	Created On						: 
	 ******************************************************************************/
	private static String getTestDataUniqueValue(String sValue){
		String sTemp;
		if (sValue.toUpperCase().contains("UNIQUE")){
			sTemp = getUniqueName();
			sValue  =  sValue.replaceAll("(?i)unique", sTemp);
			System.out.println(sValue);
		}
		return sValue;
	}
	
	
	/**
	 * Returns a time-based value.
	 *  
	 * @return String
	 */
	private static String getUniqueName() 
	{
		Calendar rightNow = Calendar.getInstance();
		String sNewName =""+rightNow.get(Calendar.YEAR)
		+ (rightNow.get(Calendar.MONTH)+1)
		+ rightNow.get(Calendar.DAY_OF_MONTH)
		+ rightNow.get(Calendar.HOUR)
		+ rightNow.get(Calendar.MINUTE)
		+ rightNow.get(Calendar.SECOND)
		+ rightNow.get(Calendar.MILLISECOND);
		return sNewName;
	}




}
