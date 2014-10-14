package org.lamsfoundation.lams.admin;

import static org.testng.Assert.assertEquals;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.codec.binary.Hex;
import org.lamsfoundation.lams.LamsConstants;
import org.lamsfoundation.lams.admin.util.AdminUtil;
import org.lamsfoundation.lams.util.LamsUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.xml.sax.InputSource;

public class MaintainIntegrations {

	private static WebDriver driver = null;

	// Test data
	private static final String randomInt = LamsUtil.randInt(0, 9999);
	private String serverId = "tester-" + randomInt;
	private String serverPrefix = "t" + randomInt;
	private static String serverKey = "qBw5QPy4i58cMI50t12F";
	private static String serverName = "Testing integration";
	private static String serverDescription  = "Tests the integration via JUnit";
	private static String userInfoURL = "http://someurl.com/";
	private static String externalServerURL = "http://someurl.com/external/";
	private static String timeoutURL = "http://someurl.com/";
	private static String lessonFinishCallbackURL = "http://someurl.com/callback/url";
	private static String country = "AU";
	private static String language = "en";

	// user testing data
	private String username = "bart" + randomInt;
	private String firstName = "Bart";
	private String lastName = "Simpson";
	private String email = "bart.simpson@movies.com";
	private String datetime = randomInt;
	private String courseId = "CF" + randomInt;

	// Lesson data
	private String ldId; // this value is assigned dynamically when importing a sequence
	private String lessonId; // this value is assigned dynamically when creating the lesson


	/**
	 * Creates an integration server 
	 */
	@Test (description = "Create an integration server")
	public void createIntegrationServer() {

		// Get to Sysadmin menu
		driver.get(LamsConstants.ADMIN_MENU_URL);

		// Click on Maintain integrated servers link
		driver.findElement(By.linkText("Maintain integrated servers")).click();
		// Assert that we are in the correct page
		assertEquals("Maintain integrated servers", driver.getTitle());

		// Click to add a integration server
		driver.findElement(By.name("addnewserver")).click();
		// Assert that we are in the correct page
		assertEquals("Edit integrated server", driver.getTitle());

		// Populate add server form
		driver.findElement(By.name("serverid")).sendKeys(serverId);
		driver.findElement(By.name("serverkey")).sendKeys(serverKey);
		driver.findElement(By.name("servername")).sendKeys(serverName);
		driver.findElement(By.name("serverdesc")).sendKeys(serverDescription);
		driver.findElement(By.name("prefix")).sendKeys(serverPrefix);
		driver.findElement(By.name("userinfoUrl")).sendKeys(userInfoURL);
		driver.findElement(By.name("serverUrl")).sendKeys(externalServerURL);
		driver.findElement(By.name("timeoutUrl")).sendKeys(timeoutURL);
		driver.findElement(By.name("lessonFinishUrl")).sendKeys(lessonFinishCallbackURL);
		// Submit form
		driver.findElement(By.name("submitbutton")).click();

		// Check that the new integration is listed 
		String editIdTag = "edit_" + serverId;
		Boolean hasNewServerIntegration = driver.findElements(By.id(editIdTag)).size() > 0;

		Assert.assertTrue(hasNewServerIntegration);

	}

	/**
	 * Disables and re-enables the integration server 
	 * 
	 */
	@Test (description = "Disables and re-enables the integration server ", dependsOnMethods={"createIntegrationServer"})
	public void toogleDisableEnableServer() { 

		// Check if the server we've just created has a disable link
		Boolean canBeDisabled = (driver.findElements(By.id("disable_"+serverId)).size() > 0);

		System.out.println("canBeDisabled: " + canBeDisabled);

		if (canBeDisabled) {
			// Click on the disable link
			driver.findElement(By.id("disable_"+serverId)).click();

		}

		// Check now if the server is disabled 
		Boolean isDisabledNow = (driver.findElements(By.id("enable_"+serverId)).size() > 0);

		System.out.println("isDisabledNow: " + isDisabledNow);


		Assert.assertTrue(isDisabledNow, "There's a problem as the integration server still shows as enabled");

		// Enable the integration server again
		driver.findElement(By.id("enable_"+serverId)).click();

		// Assert new enable state
		Boolean isEnabledNow = (driver.findElements(By.id("disable_"+serverId)).size() > 0);

		Assert.assertTrue(isEnabledNow, "There's a problem as the integration server still shows as disabled");


	}

	/**
	 * Checks if the integration servlet validates a call and creates a course, user, roles and returns 
	 * the appropriate blank learning designs
	 */
	@Test (description = "Checks the LearningDesignRepository call", dependsOnMethods={"toogleDisableEnableServer"})
	public void getLearningDesigns() { 

		// First let's logout
		LamsUtil.logout(driver);

		// Create the authentication hash
		String hash = createHash(datetime, username);
		String queryString = "?username=" + username 
				+ "&datetime=" + datetime 
				+ "&serverId=" + serverId 
				+ "&hashValue=" + hash
				+ "&courseId=" + courseId 
				+ "&mode=2" 
				+ "&firstName=" + firstName 
				+ "&lastName=" + lastName 
				+ "&email=" + email
				+ "&country=" + country
				+ "&lang=" + language;


		// Now let's make the call: 
		driver.get(LamsConstants.INTEGRATION_LEARNING_DESIGN_REPO_URL + queryString);

		// If we get <Folder> then we are good
		Boolean hasFolders = driver.findElements(By.tagName("Folder")).size() > 0;

		Assert.assertTrue(hasFolders);


	}  


	/**
	 * Imports a learning design and gets the learning design Id (ldId) 
	 * 
	 */
	@Test (description = "Imports a learning design and gets the learning design Id (ldId)", dependsOnMethods={"getLearningDesigns"})
	public void importLearningDesign() { 

		// test data
		// path to file in LAMS server 
		String filePath = "/tmp/Example1.zip";
		// First let's logout
		LamsUtil.logout(driver);

		// Create the authentication hash
		String hash = createHash(datetime, username);
		String queryString = "?method=import" 
				+ "&serverId=" + serverId 
				+ "&datetime=" + datetime
				+ "&hashValue=" + hash 
				+ "&username=" + username 
				+ "&courseId=" + courseId 
				+ "&country=" + country
				+ "&lang=" + language
				+ "&filePath=" + filePath;

		// Now let's make the call: 
		driver.get(LamsConstants.INTEGRATION_LESSON_MANAGER_URL + queryString);

		// Get the xml file into a string 
		String output = driver.getPageSource();

		// Now we parse the string to get the X from within it ldId="X" 
		ldId = org.apache.commons.lang3.StringUtils.substringBetween(output, "ldId=\"", "\"");

		// Test is successful if ldId is not null
		Assert.assertNotNull((ldId), "The learning design returned was null. Must be something wrong with the import.");

	}  

	/**
	 *    We implement a data provider so we reuse the same test for start a lesson
	 *    as well as to preview it.
	 */
	@DataProvider(name = "LessonStarts")
	public static Object[][] lessonStartsAs() {
		return new Object[][] {
				new Object[] { "preview" }, { "start"} 
		};
	}

	/**
	 * Start a lesson with the learning design that we just imported.  
	 * @throws UnsupportedEncodingException 
	 * 
	 */
	@Test (description = "Starts a lesson with the learning design that we just imported.", dataProvider = "LessonStarts", dependsOnMethods={"importLearningDesign"})
	public void startLesson(String test) throws UnsupportedEncodingException { 

		// test data
		String lessonName = URLEncoder.encode("Lesson " + test + " " + randomInt , "UTF-8");
		String lessonDescription = URLEncoder.encode("A lesson", "UTF-8");

		// First let's logout
		LamsUtil.logout(driver);

		// Create the authentication hash
		String hash = createHash(datetime, username);
		String queryString = "?method=" + test 
				+ "&username=" + username 
				+ "&serverId=" + serverId 
				+ "&datetime=" + datetime
				+ "&hashValue=" + hash 
				+ "&courseId=" + courseId 
				+ "&country=" + country
				+ "&lang=" + language
				+ "&ldId=" + ldId
				+ "&title=" + lessonName
				+ "&desc=" + lessonDescription;

		System.out.println("Create lesson URL: " + (LamsConstants.INTEGRATION_LESSON_MANAGER_URL + queryString));

		// Now let's make the call: 
		driver.get(LamsConstants.INTEGRATION_LESSON_MANAGER_URL + queryString);

		// Get XML
		String output = driver.getPageSource();

		// Now we parse the string to get the X from within it lessonId="X" 
		lessonId = org.apache.commons.lang3.StringUtils.substringBetween(output, "lessonId=\"", "\"");

		System.out.println("lessonId for " + test + ": " + lessonId);

		// Test is successful if lessonId is not null
		Assert.assertNotNull((lessonId), "The lesson returned was null");

	}  

	/**
	 * Creates two monitor users for the lesson and verify their roles are setup appropriately 
	 * @throws UnsupportedEncodingException 
	 * 
	 */
	@Test (description = "Create monitors for a lesson and verify their roles", dependsOnMethods={"startLesson"})
	public void createMonitors() throws UnsupportedEncodingException { 

		// test data
		String monitorIds = "stefan,patryk";
		String firstNames = URLEncoder.encode("Stefan Ólafur,Patryk", "UTF-8");
		String lastNames  = URLEncoder.encode("Władysław,Jónas Jędrzej", "UTF-8");
		String emails     = "s@com.com,p@com.com";

		// Create the authentication hash
		String hash = createHash(datetime, username);
		String queryString = "?username=" + username 
				+ "&serverId=" + serverId 
				+ "&datetime=" + datetime
				+ "&hashValue=" + hash 
				+ "&courseId=" + courseId 
				+ "&country=" + country
				+ "&lsId=" + lessonId
				+ "&method=" + "join" 
				+ "&monitorIds=" + monitorIds
				+ "&firstNames=" + firstNames
				+ "&lastNames=" + lastNames
				+ "&emails=" + emails;

		System.out.println(LamsConstants.INTEGRATION_LESSON_MANAGER_URL + queryString);

		// Now let's make the call: 
		driver.get(LamsConstants.INTEGRATION_LESSON_MANAGER_URL + queryString);

		// As the integration returns and xml again with the lessonId again,
		// we compare if that's what we get

		System.out.println("Page: " + driver.getPageSource());

		// Get XML
		String output = driver.getPageSource();

		// Now we parse the string to get the X from within it lessonId="X" 
		lessonId = org.apache.commons.lang3.StringUtils.substringBetween(output, "lessonId=\"", "\"");

		// If it didn't return it, fail the test
		Assert.assertNotNull(lessonId, "There was a problem adding monitors to lesson/course");

		// Now, let's check for real that the user has the appropriate roles in the course (given the lesson)

		// We add the prefixes accordingly to username and courseId
		String monitorUsername = serverPrefix + "_" + "stefan";
		String courseName = serverPrefix + "_" + courseId;

		// Login again -as this has to be done by sysadmin
		AdminUtil.loginAsSysadmin(driver);

		// Get the roles    	
		String monitorRoles = AdminUtil.rolesInCourse(driver, monitorUsername, courseName);

		// Logout
		LamsUtil.logout(driver);

		Assert.assertEquals(monitorRoles, "Author Learner Monitor", "The roles for the user" 
				+ monitorUsername + " in course " + courseName + "are incorrect");


	}  

	/**
	 * Creates four learner users for the lesson and verify their roles are setup appropriately 
	 * @throws UnsupportedEncodingException 
	 * 
	 */
	@Test (description = "Create monitors for a lesson and verify their roles", dependsOnMethods={"createMonitors"})
	public void createLearners() throws UnsupportedEncodingException { 

		// test data
		String learnerIds = "shiho,toshiro,ang,omar";
		String firstNames = URLEncoder.encode("志保,敏郎,李,عمر", "UTF-8");
		String lastNames  = URLEncoder.encode("藤村,三船,安,الشريف", "UTF-8");
		String emails     = "shiho@com.jp,tochiro@com.jp,ang@com.cn,omar@com.eg";

		// Create the authentication hash
		String hash = createHash(datetime, username);
		String queryString = "?username=" + username 
				+ "&serverId=" + serverId 
				+ "&datetime=" + datetime
				+ "&hashValue=" + hash 
				+ "&courseId=" + courseId 
				+ "&country=" + country
				+ "&lsId=" + lessonId
				+ "&method=" + "join" 
				+ "&learnerIds=" + learnerIds
				+ "&firstNames=" + firstNames
				+ "&lastNames=" + lastNames
				+ "&emails=" + emails;

		System.out.println(LamsConstants.INTEGRATION_LESSON_MANAGER_URL + queryString);

		// Now let's make the call: 
		driver.get(LamsConstants.INTEGRATION_LESSON_MANAGER_URL + queryString);

		// As the integration returns and xml again with the lessonId again,
		// we compare if that's what we get

		System.out.println("Page: " + driver.getPageSource());

		// Get XML
		String output = driver.getPageSource();

		// Now we parse the string to get the X from within it lessonId="X" 
		lessonId = org.apache.commons.lang3.StringUtils.substringBetween(output, "lessonId=\"", "\"");

		// If it didn't return it, fail the test
		Assert.assertNotNull(lessonId, "There was a problem adding monitors to lesson/course");

		// Now, let's check for real that the user has the appropriate roles in the course (given the lesson)

		// We add the prefixes accordingly to username and courseId
		String learnerUsername = serverPrefix + "_" + "shiho";
		String courseName = serverPrefix + "_" + courseId;

		// Login again -as this has to be done by sysadmin
		AdminUtil.loginAsSysadmin(driver);

		// Get the roles    	
		String learnerRole = AdminUtil.rolesInCourse(driver, learnerUsername, courseName);

		// Logout
		LamsUtil.logout(driver);

		Assert.assertEquals(learnerRole, "Learner", "The role for the learner " 
				+ learnerUsername + " in course " + courseName + "is incorrect");

	}      

	/**
	 * Jump in lesson as learner
	 * @throws UnsupportedEncodingException 
	 * 
	 */
	@Test (description = "Jump in lesson as learner", dependsOnMethods={"createLearners"})
	public void jumpAsLearner() throws UnsupportedEncodingException { 

		// test data
		String learnerUsername = "shiho";
		String method = "learnerStrictAuth";

		// Create the authentication hash
		String hash = createHashWithMethod(datetime, learnerUsername, method, lessonId);
		String queryString = "?uid=" + learnerUsername 
				+ "&ts=" + datetime 
				+ "&sid=" + serverId
				+ "&method=" + method
				+ "&hash=" + hash
				+ "&lsid=" + lessonId
				+ "&couseid=" + courseId;

		// Get the learner page
		driver.get(LamsConstants.INTEGRATION_LOGINREQUEST_URL + queryString);

		System.out.println("Jump into learner URL: " + LamsConstants.INTEGRATION_LOGINREQUEST_URL + queryString);

		// Find in the learner page the lessonTitleRow
		String lessonTitle = URLDecoder.decode(driver.findElement(By.id("lessonTitleRow")).getText().trim(), "UTF-8");
		String expectedLessonTitle = "Lesson start " + randomInt;

		System.out.println("lessonTitle: " + lessonTitle);

		//Assert.assertEquals(lessonTitle, expectedLessonTitle, "The lesson title is not correct");
		
		// Complete the first Assessment
		// Switch to contentFrame iframe
		driver.switchTo().frame("contentFrame");
		
		// Randomly select thru the answers
		for (int i = 0; i < 5; i++) {
			String xpathAnswer = "//*[@id=\"question-area-" + i + "\"]/table/tbody/tr[" + LamsUtil.randInt(1, 3) + "]/td[1]/input";
			driver.findElement(By.xpath(xpathAnswer)).click();
		}
		// Submit all responses
		driver.findElement(By.name("submitAll")).click();
		// Click on next activity
		driver.findElement(By.xpath("//*[@id=\"content\"]/div[4]/a/span")).click();
		
		driver.switchTo().defaultContent();
		// driver.navigate().refresh();
		
		Assert.assertEquals(lessonTitle, expectedLessonTitle, "The lesson title is not correct");
		
	}
	
	
	/**
	 *    We implement a data provider so we reuse the same test for displaying SVG and PNG
	 *    files for designs. Image format (svgFormat can be either 1 for SVG or 2 for PNG)
	 *    @see http://wiki.lamsfoundation.org/x/_Qg
	 */
	@DataProvider(name = "SvgAndPng")
	public static Object[][] svgAndPng() {
		return new Object[][] {
				new Object[] { "1" }, { "2"} 
		};
	}

	/**
	 * Get learning design image as SVG and PNG
	 * 
	 */
	@Test (description = "Get learning design image as SVG and PNG", dataProvider="SvgAndPng", dependsOnMethods={"jumpAsLearner"})
	public void getLdImage(String fileFormat) { 

		// test data

		// Create the authentication hash
		String hash = createHash(datetime, username);
		String queryString = "?username=" + username 
				+ "&serverId=" + serverId 
				+ "&datetime=" + datetime
				+ "&hashValue=" + hash 
				+ "&ldId=" + ldId
				+ "&svgFormat=" + fileFormat;
		

		// Get the image 
		driver.get(LamsConstants.INTEGRATION_LEARNING_DESIGN_SVG + queryString);

		System.out.println("LD image as SVG: " + LamsConstants.INTEGRATION_LEARNING_DESIGN_SVG + queryString);		
		
		if (fileFormat.equals("1")) {
			// SVG image
			Boolean hasSVGNode = (driver.findElements(By.tagName("svg")).size() > 0);
			Assert.assertTrue(hasSVGNode, "Image file isn't an SVG file.");
		} else {
			// PNG image
			Boolean isPNG = (driver.findElements(By.tagName("img")).size() > 0);
			System.out.println("isPNG: " + isPNG.toString());
			Assert.assertTrue(isPNG);
		}
		
	}

	
	
	/**
	 *    We implement a data provider so we reuse the same test for get student progress
	 *     
	 */
	@DataProvider(name = "Methods")
	public static Object[][] userMethod() {
		return new Object[][] {
				new Object[] { "singleStudentProgress" }, { "studentProgress"} 
		};
	}

	
	/**
	 * Get learner Progress
	 * Gets the XML from LAMS showing a learner's progress within a lesson.
	 * @throws Exception 
	 * @see http://wiki.lamsfoundation.org/x/_Qg
	 */
	@Test (description = "Get learner Progress", dataProvider="Methods", dependsOnMethods={"jumpAsLearner"})
	public void getLearnerProgress(String method) throws Exception { 

		// test data
		String learnerUsername = "shiho";
		int expectedCompletedActivities = 1; 
		
		// Create the authentication hash
		String hash = createHash(datetime, username);
		String queryString = "?username=" + username 
				+ "&serverId=" + serverId 
				+ "&datetime=" + datetime
				+ "&hashValue=" + hash 
				+ "&lsId=" + lessonId
				+ "&method=" + method
				+ "&courseId=" + courseId
				+ "&progressUser=" + learnerUsername;
		

		// Get the learner page
		driver.get(LamsConstants.INTEGRATION_LESSON_MANAGER_URL + queryString);

		System.out.println("Learner progress (" + method +"): " + LamsConstants.INTEGRATION_LESSON_MANAGER_URL + queryString);		

		String xmlOutput = driver.getPageSource();
		
		//System.out.println("xmlOutput " + method + ": " + xmlOutput);
				
		String activitiesCompleted = "";
		
		Document doc = loadXMLFromString(xmlOutput);

		Node node = doc.getDocumentElement().getFirstChild();
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element elem = (Element) node;
			String lUsername = elem.getAttribute("username");
			activitiesCompleted = elem.getAttribute("activitiesCompleted");
			
			System.out.println("Progress for: " + lUsername + " activities completed: "+ activitiesCompleted);
			
		}
		// Turn the activities completed into an integer
		int totalActivitiesCompleted = Integer.parseInt(activitiesCompleted);
		
		Assert.assertEquals(totalActivitiesCompleted, expectedCompletedActivities, "The completed activities for " + learnerUsername + " is incorrect.");
	
	}
	
	/**
	 * Get learner marks
	 * Gets the XML from LAMS showing a learner's marks.
	 * @throws Exception 
	 * 
	 * @see http://wiki.lamsfoundation.org/x/_Qg
	 */
	@Test (description = "Get learner marks", dependsOnMethods={"getLearnerProgress"})
	public void getLearnerMarks() throws Exception { 
	
		// test data
		String learnerUsername = "shiho";
		// int expectedCompletedActivities = 1; 
		String method = "toolOutputsUser";
		String score = "";
		
		// Create the authentication hash
		String hash = createHash(datetime, username);
		String queryString = "?username=" + username 
				+ "&serverId=" + serverId 
				+ "&datetime=" + datetime
				+ "&hashValue=" + hash 
				+ "&lsId=" + lessonId
				+ "&method=" + method
				+ "&courseId=" + courseId
				+ "&outputsUser=" + learnerUsername;
		

		// Get the learner page
		driver.get(LamsConstants.INTEGRATION_LESSON_MANAGER_URL + queryString);

		System.out.println("Learner output (" + method +"): " + LamsConstants.INTEGRATION_LESSON_MANAGER_URL + queryString);		

		String xmlOutput = driver.getPageSource();
		
		System.out.println("xmlOutput " + method + ": " + xmlOutput);
		
		Document doc = loadXMLFromString(xmlOutput);
	
		Node node = doc.getDocumentElement().getFirstChild();
		
		if (node.getNodeType() == Node.ELEMENT_NODE) {
		
			NodeList toolOutputs = doc.getElementsByTagName("ToolOutput");
			int totalToolOutputs = toolOutputs.getLength();
			
			for (int i = 0; i < totalToolOutputs; i++) {
				
				Element output = (Element) toolOutputs.item(i);
				if (output.getAttribute("name").equals("learner.total.score")) {
					score = output.getAttribute("output");
					System.out.println("Score:" + score);
				}
			}
		}

		System.out.println("Username: " +learnerUsername + " score:" + score);
		Assert.assertTrue((score != ""), "Score is empty!");
		
	}
	
	
	
	
	
	
	
	
	/**
	 *	XML parser from a string
	 */

	public static org.w3c.dom.Document loadXMLFromString(String xml) throws Exception
	{
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    InputSource is = new InputSource(new StringReader(xml));
	    return builder.parse(is);
	}
	
	
	/**
	 *	Creates the authentication hash
	 */
	private String createHash(String datetime, String username) {

		// Create the hash string to hash
		String hashValue = datetime.toLowerCase().trim() + username.toLowerCase().trim() 
				+ serverId.toLowerCase().trim() + serverKey.toLowerCase().trim();

		// Return sha1 hash
		String hash = sha1(hashValue);
		return hash;
	}

	/**
	 *	Creates the authentication hash with method
	 */
	private String createHashWithMethod(String datetime, String username, String method, String lessonId) {

		// Create the hash string to hash
		String hashValue = datetime.toLowerCase().trim() 
				+ username.toLowerCase().trim() 
				+ method.toLowerCase().trim() 
				+ lessonId
				+ serverId.toLowerCase().trim() 
				+ serverKey.toLowerCase().trim();

		// Return sha1 hash
		String hash = sha1(hashValue);
		return hash;
	}  

	private static String sha1(String plaintext){
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			return new String(Hex.encodeHex(md.digest(plaintext.getBytes())));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	@BeforeClass
	public static void openBrowser(){
		driver = new FirefoxDriver();
		//driver = new PhantomJSDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// Login
		AdminUtil.loginAsSysadmin(driver);

	} 

	@AfterClass
	public static void closeBrowser(){
		// Logout before quitting 
		LamsUtil.logout(driver);
		driver.quit();
	}


}
