/**************************************************************** 
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org) 
 * ============================================================= 
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/ 
 * 
 * This program is free software; you can redistribute it and/or modify 
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation. 
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU General Public License for more details. 
 * 
 * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA 
 * 
 * http://www.gnu.org/licenses/gpl.txt 
 * **************************************************************** 
 */

/* $Id$ */
package org.lamsfoundation.lams.tool.notebook.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.ActivityDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningDesignDAO;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dao.hibernate.LessonDAO;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.dao.hibernate.ToolDAO;
import org.lamsfoundation.lams.usermanagement.service.UserManagementService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.thoughtworks.selenium.HttpCommandProcessor;
import com.thoughtworks.selenium.SeleneseTestCase;

public abstract class AbstractSeleniumTestCase extends SeleneseTestCase {

	/**
	 * Used as and activityUIID property while storing Learning Design 
	 */
	protected static int defaultActivityUIId = 1;

	protected ToolDAO toolDAO;
	protected ActivityDAO activityDAO;
	protected UserManagementService userManagementService;
	protected LessonDAO lessonDAO;
	protected LearningDesignDAO learningDesignDAO;

	protected ApplicationContext context;
	private static final String[] contextConfigLocation = new String[] {
			"org/lamsfoundation/lams/localCommonContext.xml",
			"org/lamsfoundation/lams/lesson/lessonApplicationContext.xml",
			"org/lamsfoundation/lams/toolApplicationContext.xml",
	};

	/** the host name on which the Selenium Server resides */
	private static final String SERVER_HOST = "localhost";
	/** the port on which the Selenium Server is listening */
	private static final int SERVER_PORT = 5555;
	/**
	 * the command string used to launch the browser, e.g. "*firefox" or
	 * "c:\\program files\\internet explorer\\iexplore.exe"
	 */
	private static final String BROWSER_START_COMMAND = "*firefox";
	/**
	 * the starting URL including just a domain name. We'll start the browser
	 * pointing at the Selenium resources on this URL,
	 */
	private static final String BROWSER_URL = "http://127.0.0.1:8080/lams/";

	protected static final String SERVER_URL = "/lams/";
	protected static final String USER_LOGIN = "mmm";
	protected static final String USER_PASSWORD = "mmm";

	public void setUp() throws Exception {
		//TODO use super.setUp();
		context = new ClassPathXmlApplicationContext(contextConfigLocation);
		toolDAO = (ToolDAO) this.context.getBean("toolDAO");
		activityDAO = (ActivityDAO) this.context.getBean("activityDAO");
		userManagementService = (UserManagementService) this.context.getBean("userManagementService");
		lessonDAO = (LessonDAO) this.context.getBean("lessonDAO");
		learningDesignDAO = (LearningDesignDAO) this.context.getBean("learningDesignDAO");

		// selenium = new DefaultSelenium(SERVER_HOST, SERVER_PORT, BROWSER_START_COMMAND, BROWSER_URL);
		HttpCommandProcessor proc = new HttpCommandProcessor(SERVER_HOST,
				SERVER_PORT, BROWSER_START_COMMAND, BROWSER_URL);
		selenium = new DefaultSeleniumFlex(proc);
		selenium.start();
		selenium.setSpeed("1000");
	}

	public void tearDown() throws Exception {
		super.tearDown();
		selenium.stop();
	}
	
	/**
	 * Our main test class. Currently it tests authoring, learning and
	 * monitoring sequentially one-by-one. Also it relies on the fact that you
	 * store new LD in authoring (otherwise you should reimplement this method).
	 * 
	 * @throws Exception
	 */
	public void testEntireTool() throws Exception {
		loginToLams();
		
		//Authoring part
		openAuthoringWindow();
		authoringTest();
		storeLearningDesign();
		closeAuthoringWindow();
		
		createNewLesson();
		
		//Learning part
		learningTest();
		
		//Monitoring part		
		openToolMonitor();
		monitoringTest();
		closeToolMonitor();
	}
	
	/**
	 * Returns tool's signature. This method should be overridden by all subclasses.
	 * 
	 * @return tool's signature
	 */
	protected abstract String getToolSignature();
	
	/**
	 * Returns the name of learning design that user entered saving it in authoring.  
	 * Should be overridden by all subclasses.
	 * 
	 * @return name of learning design
	 */
	protected abstract String getLearningDesignName();
	
	/**
	 * Tests tool authoring.  
	 * Should be overridden by all subclasses.
	 */
	protected abstract void authoringTest();
	
	/**
	 * Tests tool learning.  
	 * Should be overridden by all subclasses.
	 */
	protected abstract void learningTest();
	
	/**
	 * Tests tool monitoring.  
	 * Should be overridden by all subclasses.
	 */
	protected abstract void monitoringTest();

	// TODO change this behavior (after main workflow will be setted up)
	private String toolContentID;
	private String contentFolderID;
	/**
	 * Opens up all authoring windows. Should be done before testing tool's
	 * authoring.
	 */
	protected void openAuthoringWindow() throws Exception {
		// open authoring canvas
		selenium.click("//div[@id='header-my-courses']//div[@class='tab-middle-highlight']/a");
		selenium.waitForPopUp("aWindow", "10000");

		Integer userID = userManagementService.getUserByLogin(USER_LOGIN).getUserId();
		String createUniqueContentFolderUrl = SERVER_URL
				+ "authoring/author.do?method=createUniqueContentFolder&userID="
				+ userID;
		final String createUniqueContentFolderId = "createUniqueContentFolderId";
		selenium.openWindow(createUniqueContentFolderUrl, createUniqueContentFolderId);
		selenium.waitForPopUp(createUniqueContentFolderId, "10000");
		selenium.selectWindow(createUniqueContentFolderId);
		String wddxPacket = selenium.getEval("this.browserbot.getDocument().getElementsByTagName('body')[0].innerHTML");
		contentFolderID = this.extractFolderIDFromWDDXPacket(wddxPacket);
		selenium.close();
		selenium.selectWindow(null);

		Tool tool = toolDAO.getToolBySignature(getToolSignature());
		String getToolContentUrl = SERVER_URL
				+ "authoring/author.do?method=getToolContentID&toolID="
				+ tool.getToolId();
		final String getToolContentId = "getToolContentId";
		selenium.openWindow(getToolContentUrl, getToolContentId);
		selenium.waitForPopUp(getToolContentId, "10000");
		selenium.selectWindow(getToolContentId);
		wddxPacket = selenium.getEval("this.browserbot.getDocument().getElementsByTagName('body')[0].innerHTML");
		toolContentID = this.extractToolContentIDFromWDDXPacket(wddxPacket);
		selenium.close();
		selenium.selectWindow(null);

		String openToolUrl = SERVER_URL + tool.getAuthorUrl() + "?mode=author"
				+ "&toolContentID=" + toolContentID + "&contentFolderID="
				+ contentFolderID;
		final String openToolId = "openToolId";
		selenium.openWindow(openToolUrl.toString(), openToolId);
		selenium.waitForPopUp(openToolId, "10000");
		selenium.selectWindow(openToolId);
	}

	/** checks for verification errors and stops the browser */
	protected void closeAuthoringWindow() {

	}

	/**
	 * Stores learning design. Using getLearningDesignName() as a new design name.
	 */
	protected void storeLearningDesign() {
		selenium.click("//span[@class='okIcon']");
		selenium.waitForPageToLoad("10000");
		//closes ok/reedit confirmation screen
		selenium.click("//span[@class='close']");

		//TODO remove next Paragraph
		selenium.selectWindow(null);
		Tool tool = toolDAO.getToolBySignature(getToolSignature());
		String openToolUrl = SERVER_URL + tool.getAuthorUrl() + "?mode=author"
				+ "&toolContentID=" + toolContentID + "&contentFolderID="
				+ contentFolderID;
		final String openToolId = "openToolId2";
		selenium.openWindow(openToolUrl.toString(), openToolId);
		selenium.waitForPopUp(openToolId, "10000");
		selenium.selectWindow(openToolId);
		
		final String storeLearningDesignUrl = SERVER_URL + "servlet/authoring/storeLearningDesignDetails";
		String designDetails = constructWddxDesign(getToolSignature(), getLearningDesignName());
		selenium.runScript("var options = { " + "method:\"post\", "
											  + "postBody:\"" + designDetails + "\" " 
											  + "};"
				+ "new Ajax.Request(\"" + storeLearningDesignUrl + "\",options);");
		
		//TODO remove next Paragraph
		selenium.close();
		
		//closes Flash authoring screen
		selenium.selectWindow("aWindow");
		selenium.close();
		selenium.selectWindow(null);
	}
	
	/**
	 * Beware this method works wrong sometimes (due to the appearance of root element) 
	 * 
	 * @throws Exception
	 */
	protected void createNewLesson() throws Exception {
		Long lastCreatedLessonId = getLastCreatedLessonId(true);
		DefaultSeleniumFlex flexSelenium = (DefaultSeleniumFlex) selenium;
		
		
		//TODO change the way workspaceFolderID setted
		List<String> titles = learningDesignDAO.getLearningDesignTitlesByWorkspaceFolder(new Integer(4));
		assertTrue("There is no stored learning design", titles.size() > 0);
		Collections.sort(titles, String.CASE_INSENSITIVE_ORDER);
		int count = 1;
		for (String title : titles) {
			if (title.equals(getLearningDesignName())) {
				break;
			}
			count++;
		}
		assertTrue("There isn't learning design with name" + getLearningDesignName(),count <= titles.size());
		
		flexSelenium.click("link=Add Lesson");
		Thread.sleep(6000);
		waitForFlexExists("workspaceTree", 20, flexSelenium);

		// flexObjId is now setted to "cloudWizard" by default, change this if
		// you want to use another one
		// flexSelenium.flexSetFlexObjID("cloudWizard");
		 
		assertTrue(flexSelenium.getFlexEnabled("startButton").equals("true"));
		flexSelenium.flexSelectIndex("workspaceTree", String.valueOf(count));
//		flexSelenium.flexType("resourceName_txi", "bueno");
		flexSelenium.flexClick("startButton");
		Thread.sleep(25000);
		assertTrue("Lesson creation failed", lastCreatedLessonId < getLastCreatedLessonId(true));
	}
	
	/**
	 * Prepares learning environment. Should be invoked each time learning is going to be tested.
	 */
	protected void setUpLearning() {
		selenium.runScript("openLearner(" + getLastCreatedLessonId(false) + ")");
//		selenium.click("link=" + getLearningDesignName());
		selenium.waitForPopUp("lWindow", "30000");
		String previousSpeed = selenium.getSpeed();
		selenium.setSpeed("3000");
		selenium.selectWindow("lWindow");
		selenium.setSpeed(previousSpeed);
	}
	
	/**
	 * Shuts up learning environment. Should be invoked each time learning testing is done.
	 */
	protected void tearDownLearning() {
		selenium.close();
		selenium.selectWindow(null);
	}

	/**
	 * Launches Lams monitor, followed by tool's monitor popup.
	 */
	protected void openToolMonitor() {
		Long lastCreatedLessonId = getLastCreatedLessonId(false);
		LearningDesign learningDesign = lessonDAO.getLesson(lastCreatedLessonId).getLearningDesign();
		String contentFolderID = learningDesign.getContentFolderID();
		Activity activity = learningDesign.getFirstActivity();
		// doing this to prevent CGILib initialization exception
		ToolActivity toolActivity = (ToolActivity) activityDAO.getActivityByActivityId(activity.getActivityId());
		
		selenium.runScript("openMonitorLesson(" + lastCreatedLessonId + ")");
		selenium.waitForPopUp("mWindow", "15000");

		// openning tool monitor popup
		Tool tool = toolDAO.getToolBySignature(getToolSignature());
		String monitorUrl = SERVER_URL + tool.getMonitorUrl()
				+ "?toolContentID=" + toolActivity.getToolContentId()
				+ "&contentFolderID=" + contentFolderID;
		final String monitorId = "monitorId";
		selenium.openWindow(monitorUrl, monitorId);
		selenium.waitForPopUp(monitorId, "50000");
		selenium.selectWindow(monitorId);
	}

	/**
	 * Closes Lams monitor. (Optional activity)
	 */
	protected void closeToolMonitor() {
		selenium.close();
		selenium.selectWindow(null);
	}
	
	
	/** */
	protected void loginToLams() throws Exception {
		selenium.open(SERVER_URL);
		selenium.type("j_username", USER_LOGIN);
		selenium.type("j_password", USER_PASSWORD);
		selenium.click("//p[@class='login-button']/a");
		selenium.waitForPageToLoad("10000");
		Thread.sleep(3000);
	}

	// *****************************************************************************
	// methods for testing Flex
	// *****************************************************************************

	protected void waitForFlexExists(String objectID, int timeout,	DefaultSeleniumFlex selenium) throws Exception {
		while (timeout > 0	&& !selenium.getFlexExists(objectID).contains("true")) {
			Thread.sleep(1000);
			timeout--;
		}
		if (timeout == 0) {
			throw new Exception("waitForFlexExists flex object:" + objectID	+ " Timed Out");
		}
	}

	protected void waitForFlexVisible(String objectID, int timeout,
			DefaultSeleniumFlex selenium) throws Exception {
		while (timeout > 0 && !selenium.getFlexVisible(objectID).equals("true")) {
			Thread.sleep(1000);
			timeout--;
		}
		if (timeout == 0) {
			throw new Exception("waitForFlexVisible flex object:" + objectID
					+ " Timed Out");
		}
	}

	

	// *****************************************************************************
	// auxiliary methods
	// *****************************************************************************
	
	/**
	 * Returns lessonId of the last created lesson.
	 *  
	 * @return id
	 */
	private Long getLastCreatedLessonId(boolean returnNegative) {
		Integer userID = userManagementService.getUserByLogin(USER_LOGIN).getUserId();
		List<Lesson> lessonsCreatedByUser = lessonDAO.getLessonsCreatedByUser(userID);
		List<Lesson> lessonsByLDName = new ArrayList<Lesson>();
		for (Lesson lesson : lessonsCreatedByUser) {
			if (lesson.getLessonName().equals(getLearningDesignName())){
				lessonsByLDName.add(lesson);
			}
		}
		if (lessonsByLDName.size() == 0) {
			if (returnNegative) {
				return new Long(-1);
			} else {
				fail("Lesson creation failed.");
			}
		}
		
		Lesson lastCreatedLesson = lessonsCreatedByUser.iterator().next();
		for (Lesson lesson : lessonsByLDName) {
			if (lesson.getCreateDateTime().after(lastCreatedLesson.getCreateDateTime()) ){
				lastCreatedLesson = lesson;
			}
		}
		return lastCreatedLesson.getLessonId();
	}

	/**
	 * Given a WDDX packet in our normal format, gets the id number from within
	 * the &lt;var
	 * name='messageValue'&gt;&lt;number&gt;num&lt;/number&gt;&lt;/var&gt;
	 * 
	 * @param wddxPacket
	 * @return id
	 */
	private String extractFolderIDFromWDDXPacket(String wddxPacket) {
		int indexMessageValue = wddxPacket.indexOf("<var name=\"messageValue\"><string>");
		assertTrue("<var name='messageValue'><string> string not found", indexMessageValue > 0);
		int endIndexMessageValue = wddxPacket.indexOf("</string></var>", indexMessageValue);
		return wddxPacket.substring(indexMessageValue
				+ "<var name='messageValue'><string>".length(),	endIndexMessageValue);
	}

	/**
	 * Given a WDDX packet in our normal format, gets the id number from within
	 * the &lt;var
	 * name='messageValue'&gt;&lt;number&gt;num&lt;/number&gt;&lt;/var&gt;
	 * 
	 * @param wddxPacket
	 * @return id
	 */
	private String extractToolContentIDFromWDDXPacket(String wddxPacket) {
		int indexMessageValue = wddxPacket.indexOf("<var name=\"messageValue\"><number>");
		assertTrue(wddxPacket + "!!<var name='messageValue'><number> string not found",	indexMessageValue > 0);
		int endIndexMessageValue = wddxPacket.indexOf(".0</number></var>", indexMessageValue);
		String idString = wddxPacket.substring(indexMessageValue
				+ "<var name='messageValue'><string>".length(),
				endIndexMessageValue);
		try {
			Long.parseLong(idString);
			return idString;
		} catch (NumberFormatException e) {
			fail("Unable to get id number from WDDX packet. Format exception. String was "
					+ idString);
		}
		return null;
	}

	private String constructWddxDesign(String toolSignature,String newLearningDesignName) {
		Integer userID = userManagementService.getUserByLogin(USER_LOGIN).getUserId();
		Tool tool = toolDAO.getToolBySignature(toolSignature);
		
		// TemplateActivityByLibraryID(libraryID);
		ToolActivity templateActivity = null;
		for (Object activityObject : activityDAO.getAllActivities()) {
			if (activityObject instanceof ToolActivity) {
				ToolActivity activity = (ToolActivity) activityObject;
				if ((activity.getLearningDesign() == null)
						&& activity.getTool().getToolId().equals(tool.getToolId())) {
					templateActivity = activity;
				}
			}
		}
		
		String design = 
		    "<wddxPacket version='1.0'>" + 
	        	"<header />" + 
	        	"<data>" + 
	        		"<struct>" + 
	        			"<var name='competences'><array length='0' /></var>" + 
	        			"<var name='branchMappings'><array length='0' /></var>" + 
	        			"<var name='groupings'><array length='0' /></var>" + 
	        			"<var name='transitions'><array length='0' /></var>" + 
	        			"<var name='activities'>" + 
	        				"<array length='1'>" +
	        					"<struct>" +
	        						"<var name='competenceMappings'><array length='0' /></var>" +
	        						"<var name='gradebookToolOutputDefinitionName'><string>string_null_value</string></var>" +
	        						"<var name='extLmsId'><string>string_null_value</string></var>" +
	        						"<var name='toolID'><number>" + tool.getToolId() + "</number></var>" +
	        						"<var name='toolContentID'><number>" + toolContentID + "</number></var>" +
	        						"<var name='toolSignature'><string>" + tool.getToolSignature() + "</string></var>" +
	        						"<var name='toolDisplayName'><string>" + tool.getToolDisplayName() + "</string></var>" +
	        						"<var name='helpURL'><string>" + tool.getHelpUrl() + "</string></var>" +
	        						"<var name='authoringURL'><string>" + tool.getAuthorUrl() + "</string></var>" +
	        						"<var name='stopAfterActivity'><boolean value='false' /></var>" +
	        						"<var name='groupingSupportType'><number>" + templateActivity.getGroupingSupportType() + "</number></var>" +
	        						"<var name='createDateTime'><dateTime>2009-6-12T1:24:50+3:0</dateTime></var>" +
	        						"<var name='defineLater'><boolean value='false' /></var>" +
	        						"<var name='runOffline'><boolean value='false' /></var>" +
	        						"<var name='applyGrouping'><boolean value='false' /></var>" +
	        						"<var name='parentActivityID'><number>-111111</number></var>" +
	        						"<var name='parentUIID'><number>-111111</number></var>" +
	        						"<var name='libraryActivityUIImage'><string>" + templateActivity.getLibraryActivityUiImage() + "</string></var>" +
	        						"<var name='xCoord'><number>124</number></var>" +
	        						"<var name='yCoord'><number>132</number></var>" +
	        						"<var name='helpText'><string>" + templateActivity.getHelpText() + "</string></var>" +
	        						"<var name='description'><string>" + templateActivity.getDescription() + "</string></var>" +
	        						"<var name='activityTitle'><string>" + templateActivity.getTitle() + "</string></var>" +
	        						"<var name='learningLibraryID'><number>" + templateActivity.getLearningLibrary().getLearningLibraryId() + "</number></var>" +
	        						"<var name='activityUIID'><number>"+ (defaultActivityUIId++) +"</number></var>" +
	        						"<var name='activityCategoryID'><number>" + templateActivity.getActivityCategoryID() + "</number></var>" +
	        						"<var name='activityID'><number>" + templateActivity.getActivityId() + "</number></var>" +
	        						"<var name='activityTypeID'><number>" + templateActivity.getActivityTypeId() + "</number></var>" +
	        					"</struct>" +
	        				"</array>" +
	        			"</var>" +
	        			"<var name='contentFolderID'><string>" + contentFolderID + "</string></var>" +
	        			"<var name='createDateTime'><dateTime>2009-6-24T22:45:24+3:0</dateTime></var>" +
	        			//TODO may be we need provide real workspaceFolderID      	
//	        			WorkspaceFolderContentDAO dd;
//	        			dd.getWorkspaceFolderContentByID(new Long(2)).getWorkspaceFolder().getWorkspaceFolderId();
	        			"<var name='workspaceFolderID'><number>4</number></var>" +
	        			"<var name='maxID'><number>1</number></var>" +
	        			"<var name='saveMode'><number>0</number></var>" +
	        			"<var name='validDesign'><boolean value='true' /></var>" +
	        			"<var name='readOnly'><boolean value='false' /></var>" +
	        			"<var name='userID'><string>" + userID + "</string></var>" +
	        			"<var name='title'><string>" + newLearningDesignName + "</string></var>" +
	        			"<var name='learningDesignID'><number>-111111</number></var>" +
	        			"<var name='copyTypeID'><number>1</number></var>" +
	        		"</struct>" +
	        	"</data>" +
	            "</wddxPacket>";
		return design;
	}

	// /**
	// * Given a WDDX packet in our normal format, return the map object in the
	// messageValue parameter. This should
	// * contain any returned ids.
	// *
	// * @param wddxPacket
	// * @return Map
	// */
	// protected String extractIdMapFromWDDXPacket(String wddxPacket) {
	//
	// Object obj = null;
	// try {
	// obj = WDDXProcessor.deserialize(wddxPacket);
	// } catch (WddxDeserializationException e1) {
	// fail("WddxDeserializationException " + e1.getMessage());
	// e1.printStackTrace();
	// }
	// // WDDXProcessor.convertToBoolean(table, key)
	//
	// Map map = (Map) obj;
	// Object uniqueContentFolderObj = map.get("createUniqueContentFolder");
	// assertNotNull("createUniqueContentFolder object found",
	// uniqueContentFolderObj);
	// if (!String.class.isInstance(uniqueContentFolderObj)) {
	// fail("createUniqueContentFolder is not a String - try extractIdFromWDDXPacket(packet)");
	// }
	//
	// return (String) uniqueContentFolderObj;
	// }

}
