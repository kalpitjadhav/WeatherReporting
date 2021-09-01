package com.ndtv.reporting;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.ndtv.ui.basetest.BaseTest;
import com.ndtv.ui.lib.Messages;

public class Reporting
{

	public static String Functionality = "NoName";
	public static String Testcasename = "NoName";
	public static int Iterator = 0;
	public static String sScreenshotFilePath = "";


	private static String sPathSeparatorChar = java.io.File.separator;
	private static String sTestResultFolderPath = "logs";
	private static Document doc;
	private static String sResultXMLFilePath = "";
	private static String sXMLCurrentScriptName;
	private static int sXMLCurrentIterator;
	private static Element rootElement;
	private static Element IteratorElement;
	private static Element tsElement;
	private static Element FuncElement;
	private static boolean bCreateFile = false;
	private static String sXMLCurrentFunctionality;
	private static int testCasesCount = 0;
	private static int testcasesFailCount = 0;
	private static int testcasesPassCount = 0;
	private static DocumentBuilderFactory docFactory;
	private static DocumentBuilder docBuilder;
	private static FileInputStream file;
	
	private static Logger logger=Logger.getLogger("Reporting");


	/*******************************************************************************
	Function Name 					: logResults
	Description						: Logs the result step based on the given status, desc, result under the assigned functionality and testscript
	Parameters						: sStatus, sStepdescription, sStepResult
	Usage							: logResults(sStatus, sStepdescription, sStepResult)
	Created By						: Kalpit
	Created On						: 
	 * @throws IOException 
	 ******************************************************************************/
	public static void logResults(String sStatus, String sStepdescription,String sStepResult) throws HeadlessException, IOException, AWTException
	{
		sXMLCurrentFunctionality = Functionality;
		sXMLCurrentScriptName = Testcasename;
		sXMLCurrentIterator = Iterator;

		// Check log folder was created
		createResultfolder();

		// Check XML file was created and open the XMl file
		openXMLFile();

		// update functionality node
		addOrUpdateFunctionalityNode();

		// update testscript node
		addOrUpdateTestScriptNode();

		// update iterator node
		addOrUpdateIteratorNode();

		// update step node
		addOrUpdateStepNode(sStatus, sStepdescription, sStepResult);
		
		// log the info the console		
		if (sStatus.toLowerCase().contains("pass")) {
			logger.info(sStatus + ", " + sStepdescription + ", " + sStepResult);
		}
		else if (sStatus.toLowerCase().contains("fail")) {
			logger.error(sStatus + ", " + sStepdescription + ", " + sStepResult);
			updateFailStatus();
		}
		else if (sStatus.toLowerCase().contains("warn")) {
			logger.warn(sStatus + ", " + sStepdescription + ", " + sStepResult);
		}
		else {
			logger.warn(sStatus + ", " + sStepdescription + ", " + sStepResult);
		}

		testCasesCount = getTotalTestCaseCount();
		testcasesFailCount = getFailTestCaseCount();
		testcasesPassCount = getPassTestCaseCount();

		rootElement.setAttribute("TP", "" + testcasesPassCount);
		rootElement.setAttribute("TF", "" + testcasesFailCount);
		rootElement.setAttribute("TotalTestCases", "" + testCasesCount);

		// write the content into xml file
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();

			Transformer transformer = transformerFactory.newTransformer();

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(sResultXMLFilePath));

			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	/*******************************************************************************
	Function Name 					: createResultfolder
	Description						: Creates the 'logs' folder under the project root node if the logs folder is not there already
	Parameters						: 
	Usage							: createResultfolder()
	Created By						: Kalpit
	Created On						: 
	 ******************************************************************************/
	private static void createResultfolder() {
		if ((new File(sTestResultFolderPath)).exists()) {
			return;
		}
		(new File(sTestResultFolderPath)).mkdir();
	}

	/*******************************************************************************
	Function Name 					: openXMLFile
	Description						: creates xml file(with unique name) with basic root node(test suite node) and other info
	Parameters						: 
	Usage							: openXMLFile()
	Created By						: Kalpit
	Created On						: 
	 ******************************************************************************/
	private static void openXMLFile() {
		if (!bCreateFile) {
			bCreateFile = true;
			String sResultFileName = "ResultFile" + now() + ".xml";
			sResultFileName = sResultFileName.replace(":", "");
			sResultFileName = sResultFileName.replace(" ", "");
			sResultXMLFilePath = sTestResultFolderPath + sPathSeparatorChar;
			sResultXMLFilePath = sResultXMLFilePath + sResultFileName;
			createXMLFile();
		}
	}

	/*******************************************************************************
	Function Name 					:	createXMLFile
	Description						: creates xml file(with unique name) with basic root node(test suite node) and other info
	Parameters						: 
	Usage							: createXMLFile()
	Created By						: Kalpit
	Created On						: 
	 ******************************************************************************/
	private static void createXMLFile() {
		try {
//			System.setProperty("javax.xml.transform.TransformerFactory","com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");

			docFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docFactory.newDocumentBuilder();

			Document xmlDocument = docBuilder.newDocument();
			xmlDocument.insertBefore(xmlDocument.createProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"Result.xsl\""),
					xmlDocument.getDocumentElement());


			rootElement = xmlDocument.createElement("TestSuite");
			xmlDocument.appendChild(rootElement);

			// setting attributes
			rootElement.setAttribute("StartTime", now());
			rootElement.setAttribute("EndTime", now());
			rootElement.setAttribute("TotalTestCases", "" + testCasesCount);

			rootElement.setAttribute("TF", "" + testcasesFailCount);
			rootElement.setAttribute("TP", "" + testcasesPassCount);

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(xmlDocument);
			StreamResult result = new StreamResult(new File(sResultXMLFilePath));
			transformer.transform(source, result);
			changeToEditMode();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/*******************************************************************************
	Function Name 					:	now
	Description						: returns the current date and time with MM-dd-yyyy HH:mm:ss format
	Parameters						: 
	Usage							: String sTime = now()
	Created By						: Kalpit
	Created On						: 
	 ******************************************************************************/
	private static String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		return sdf.format(cal.getTime());
	}

	/*******************************************************************************
	Function Name 					:	changeToEditMode
	Description						: changes the xml file to edit mode
	Parameters						: 
	Usage							: changeToEditMode
	Created By						: Kalpit
	Created On						: 
	 ******************************************************************************/
	private static void changeToEditMode() {
		try {
			file = new FileInputStream(new File(sResultXMLFilePath));
			doc = docBuilder.parse(file);
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = "//TestSuite";
			NodeList nodeList_ele = (NodeList) xPath.compile(expression)
					.evaluate(doc, XPathConstants.NODE);
			rootElement = (Element) nodeList_ele;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*******************************************************************************
	Function Name 					:	addOrUpdateFunctionalityNode
	Description						: If the functionality node is not available, it adds the functionality node.
	Parameters						: 
	Usage							: changeToEditMode
	Created By						: Kalpit
	Created On						: 
	 ******************************************************************************/
	private static void addOrUpdateFunctionalityNode() {
		if (!checkFunctionalNodeAvailable()) {
			testcasesFailCount = 0;
			testcasesPassCount = 0;
			Element ele1 = doc.createElement("Functionality");
			rootElement.appendChild(ele1);
			ele1.setAttribute("name", sXMLCurrentFunctionality);
			FuncElement = ele1;
		}

	}

	/*******************************************************************************
	Function Name 					:	addOrUpdateTestScriptNode
	Description						: If the testscript node is not available, it adds the testscript node.
	Parameters						: 
	Usage							: addOrUpdateTestScriptNode()
	Created By						: Kalpit
	Created On						: 
	 ******************************************************************************/
	private static void addOrUpdateTestScriptNode() {
		if (!checkTestScriptNodeAvailable()) {
			Element ele = doc.createElement("TestScript");
			FuncElement.appendChild(ele);
			ele.setAttribute("name", sXMLCurrentScriptName);
			ele.setAttribute("StartTime", now());
			ele.setAttribute("EndTime", now());
			ele.setAttribute("TC_Status", "Pass");
			tsElement = ele;
		}
	}

	private static void addOrUpdateIteratorNode() {
		if (!checkIteratorNodeAvailable()) {
			Element ele = doc.createElement("Iterator");
			tsElement.appendChild(ele);
			ele.setAttribute("no", Integer.toString(Iterator));
			IteratorElement = ele;
		}
	}

	private static void addOrUpdateStepNode(String sStatus,
			String sStepdescription, String sStepResult) throws HeadlessException, IOException, AWTException {
		NodeList nl = IteratorElement.getChildNodes();
		int iStepNo = nl.getLength();

		Element el = doc.createElement("step");
		IteratorElement.appendChild(el);
		el.setAttribute("no", Integer.toString((iStepNo + 1)));
		Node eStep = IteratorElement.getLastChild();

		el = doc.createElement("status");
		Text txt = doc.createTextNode(sStatus);
		el.appendChild(txt);
		eStep.appendChild(el);

		el = doc.createElement("stepname");
		txt = doc.createTextNode(sStepdescription);
		el.appendChild(txt);
		eStep.appendChild(el);

		el = doc.createElement("Description");
		txt = doc.createTextNode("" + sStepResult);
		el.appendChild(txt);
		eStep.appendChild(el);

		//capture screenshot

		//if there is no screenshot taken already, then capture it now
		if(sStatus.equalsIgnoreCase("fail"))
		{
			if(sScreenshotFilePath.length()==0)
				CaptureScreenshot();
		}

		el = doc.createElement("screenshot");
		txt = doc.createTextNode(sScreenshotFilePath);
		el.appendChild(txt);
		eStep.appendChild(el);
		sScreenshotFilePath = "";

		el = doc.createElement("timestamp");
		txt = doc.createTextNode(now());
		el.appendChild(txt);
		eStep.appendChild(el);

		tsElement.setAttribute("EndTime", now());
		rootElement.setAttribute("EndTime", now());

		try {
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();

			Transformer transformer = transformerFactory.newTransformer();

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(sResultXMLFilePath));

			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*******************************************************************************
	Function Name 					:	checkFunctionalNodeAvailable
	Description						: Checkes whether the functionality node exist or not
	Parameters						: 
	Usage							: bStatus = checkFunctionalNodeAvailable()
	Created By						: Kalpit
	Created On						: 
	 ******************************************************************************/
	private static boolean checkFunctionalNodeAvailable() {
		try {
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = "//TestSuite/Functionality[@name='"+ sXMLCurrentFunctionality + "']";
			NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(
					doc, XPathConstants.NODESET);
			NodeList nodeListElement = (NodeList) xPath.compile(expression)
					.evaluate(doc, XPathConstants.NODE);
			if (nodeList.getLength() > 0) {
				FuncElement = (Element) nodeListElement;
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/*******************************************************************************
	Function Name 					:	checkTestScriptNodeAvailable
	Description						: Checks whether the testscript node exists or not
	Parameters						: 
	Usage							: bStatus = checkTestScriptNodeAvailable()
	Created By						: Kalpit
	Created On						: 
	 ******************************************************************************/
	private static boolean checkTestScriptNodeAvailable() {
		try {
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = "//Functionality[@name='"
					+ sXMLCurrentFunctionality + "']/TestScript[@name='"
					+ sXMLCurrentScriptName + "']";
			NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(
					doc, XPathConstants.NODESET);
			NodeList nodeListElement = (NodeList) xPath.compile(expression)
					.evaluate(doc, XPathConstants.NODE);
			if (nodeList.getLength() > 0) {
				tsElement = (Element) nodeListElement;
				return true;
			}
			return false;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	/*******************************************************************************
	Function Name 					:	checkIteratorNodeAvailable
	Description						: Checks whether the iterator node exists or not
	Parameters						: 
	Usage							: bStatus = checkIteratorNodeAvailable()
	Created By						: Kalpit
	Created On						: 
	 ******************************************************************************/
	private static boolean checkIteratorNodeAvailable() {
		try {
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = "//Functionality[@name='"
					+ sXMLCurrentFunctionality + "']/TestScript[@name='"
					+ sXMLCurrentScriptName + "']/Iterator[@no='"
					+ sXMLCurrentIterator + "']";
			NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(
					doc, XPathConstants.NODESET);
			NodeList nodeListElement = (NodeList) xPath.compile(expression)
					.evaluate(doc, XPathConstants.NODE);
			if (nodeList.getLength() > 0) {
				IteratorElement = (Element) nodeListElement;
				return true;
			}
			return false;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	/*******************************************************************************
	Function Name 					:	getFailTestCaseCount
	Description						: Determines the total fail test cases count
	Parameters						: 
	Usage							: iFailCount = getFailTestCaseCount()
	Created By						: Kalpit
	Created On						: 
	 ******************************************************************************/
	private static int getFailTestCaseCount() {
		NodeList nodeList = null;
		int iSum = 0;
		try {
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = "//TestSuite/Functionality";
			nodeList = (NodeList) xPath.compile(expression).evaluate(doc,
					XPathConstants.NODESET);
			for (int iCount = 1; iCount <= nodeList.getLength(); iCount++) {
				String sFailExpression = "//TestSuite/Functionality[" + iCount
						+ "]/TestScript[@TC_Status='Fail']";
				NodeList TCFailnodeList = (NodeList) xPath.compile(
						sFailExpression).evaluate(doc, XPathConstants.NODESET);
				iSum = iSum + TCFailnodeList.getLength();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return iSum;
	}

	/*******************************************************************************
	Function Name 					:	getPassTestCaseCount
	Description						: Determines the total pass test cases count
	Parameters						: 
	Usage							: iPassCount = getPassTestCaseCount()
	Created By						: Kalpit
	Created On						: 
	 ******************************************************************************/
	private static int getPassTestCaseCount() {
		NodeList nodeList = null;
		int iSum = 0;
		try {
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = "//TestSuite/Functionality";
			nodeList = (NodeList) xPath.compile(expression).evaluate(doc,
					XPathConstants.NODESET);
			for (int iCount = 1; iCount <= nodeList.getLength(); iCount++) {
				String sPassExpression = "//TestSuite/Functionality[" + iCount
						+ "]/TestScript[@TC_Status='Pass']";
				NodeList TCPassNodeList = (NodeList) xPath.compile(
						sPassExpression).evaluate(doc, XPathConstants.NODESET);
				iSum = iSum + TCPassNodeList.getLength();
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return iSum;
	}

	/*******************************************************************************
	Function Name 					:	getTotalTestCaseCount
	Description						: Determines the total test cases count
	Parameters						: 
	Usage							: iTotalTCCount = getTotalTestCaseCount()
	Created By						: Kalpit
	Created On						: 
	 ******************************************************************************/
	private static int getTotalTestCaseCount() {
		NodeList nodeList = null;
		int iSum = 0;
		try {
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = "//TestSuite/Functionality";
			nodeList = (NodeList) xPath.compile(expression).evaluate(doc,
					XPathConstants.NODESET);
			for (int iCount = 1; iCount <= nodeList.getLength(); iCount++) {
				String sTotalExpression = "//TestSuite/Functionality[" + iCount
						+ "]/TestScript";
				NodeList TCnodeList = (NodeList) xPath
						.compile(sTotalExpression).evaluate(doc,
								XPathConstants.NODESET);
				iSum = iSum + TCnodeList.getLength();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return iSum;
	}

	/*******************************************************************************
	Function Name 					:	updateFailStatus
	Description						: Updates the rootnode attribute TC_Status as fail if any test step is failed
	Parameters						: 
	Usage							: iTotalTCCount = updateFailStatus()
	Created By						: Kalpit
	Created On						: 
	 ******************************************************************************/
	private static void updateFailStatus() {
		tsElement.setAttribute("TC_Status", "Fail");
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();

			Transformer transformer = transformerFactory.newTransformer();

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(sResultXMLFilePath));

			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Captures a .JPG screen shot image in the \screenshots\Image folder.  This method is usually
	 * invoked when an error is encountered.
	 *  
	 * @return	boolean
	 * @throws IOException
	 */
	public static boolean CaptureScreenshot() throws IOException {
		String methodID = "CaptureScreenshot";
		String sFileName = "screenshots\\Image "+reportnow()+".jpg";

		try {

			//setup image file
			File imageFile = ((TakesScreenshot) BaseTest.driver).getScreenshotAs(OutputType.FILE);
			File appImageFile = new File(sFileName);
			FileUtils.moveFile(imageFile, appImageFile);                
			System.err.println(methodID + ": screen shot of application available - " + sFileName);
		}
		catch (Exception e) {
			Messages.errorMsg = e.toString();
			System.err.println(methodID + ": " + e.toString());
			return false;
		}

		sScreenshotFilePath = "..\\"+sFileName;

		return true;
	}
	
	/**
	 * Returns the current time value in the "MM-dd-yyyy HH-mm-ss" format.
	 * @return String
	 */
	private static String reportnow() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH-mm-ss");
		return sdf.format(cal.getTime());
	}

}
