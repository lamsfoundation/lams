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

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.ActivityDAO;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dao.hibernate.LessonDAO;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.dao.hibernate.ToolDAO;
import org.lamsfoundation.lams.usermanagement.service.UserManagementService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.thoughtworks.selenium.HttpCommandProcessor;
import com.thoughtworks.selenium.SeleneseTestCase;

public class SeleniumBaseTestCase extends SeleneseTestCase {
    
//     protected FlexUISelenium flexUITester;
//     protected FlashSelenium flashApp;
//    protected DefaultSeleniumFlex defaultSeleniumFlex;
    
    protected static int defaultActivityUIId = 1;
    
    protected ApplicationContext context;
    
    protected ToolDAO toolDAO;
    protected ActivityDAO activityDAO;
    protected UserManagementService userManagementService;
    protected LessonDAO lessonDAO;

    
    private static final String[] contextConfigLocation = new String[] {
		// "org/lamsfoundation/lams/localApplicationContext.xml"};//,
		// "org/lamsfoundation/lams/lesson/lessonApplicationContext.xml",
		// "org/lamsfoundation/lams/toolApplicationContext.xml",
		// "org/lamsfoundation/lams/learning/learningApplicationContext.xml",
		// "org/lamsfoundation/lams/tool/notebook/testApplicationContext.xml"};
		"org/lamsfoundation/lams/localCommonContext.xml",
		"org/lamsfoundation/lams/lesson/lessonApplicationContext.xml",
		"org/lamsfoundation/lams/toolApplicationContext.xml",
		//"org/lamsfoundation/lams/contentrepository/applicationContext.xml",
		//TODO we might gonna need this one 
		//"org/lamsfoundation/lams/learning/learningApplicationContext.xml",
		//"org/lamsfoundation/lams/workspace/workspaceapplicationcontext.xml",
//		"org/lamsfoundation/lams/authoring/testAuthoringApplicationContext.xml",
		//"org/lamsfoundation/lams/monitoring/monitoringapplicationcontext.xml",
//		"org/lamsfoundation/lams/tool/notebook/testApplicationContext.xml"
		 };
    
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5555;
    private static final String BROWSER_START_COMMAND = "*firefox";
    private static final String BROWSER_URL = "http://127.0.0.1:8080/lams/";
    
    protected static final String SERVER_URL = "/lams/";
    protected static final String USER_LOGIN = "mmm";
    private static final String USER_PASSWORD = "mmm";
    
    public void setUp() throws Exception {
        context = new ClassPathXmlApplicationContext(contextConfigLocation);
//        initializeHibernateSession();
        
	toolDAO = (ToolDAO) this.context.getBean("toolDAO");
	activityDAO = (ActivityDAO) this.context.getBean("activityDAO");
	userManagementService = (UserManagementService) this.context.getBean("userManagementService");
	lessonDAO = (LessonDAO) this.context.getBean("lessonDAO");

//	selenium = new DefaultSelenium(SERVER_HOST, SERVER_PORT, BROWSER_START_COMMAND, BROWSER_URL);
//	selenium.start();

	
//	flashApp = new FlashSelenium(selenium, "clickcolors");
	HttpCommandProcessor proc = new HttpCommandProcessor(SERVER_HOST, SERVER_PORT, BROWSER_START_COMMAND, BROWSER_URL);
	selenium = new DefaultSeleniumFlex(proc);	
	selenium.start();
	selenium.setSpeed("1000");
	
//      assertEquals(100, flashApp.PercentLoaded());	
//	setUp("http://www.google.com/", "*firefox");
//	flexUITester = new FlexUISelenium(selenium, "compareSumFlexObjId");
    }
    
    /** checks for verification errors and stops the browser */
    public void tearDown() throws Exception {
        super.tearDown();
//        finalizeHibernateSession();
	selenium.stop();
    }
    
    /** checks for verification errors and stops the browser */
    public void loginToLams() throws Exception {
        super.tearDown();
	selenium.open(SERVER_URL);
	selenium.type("j_username", USER_LOGIN);
	selenium.type("j_password", USER_PASSWORD);
	selenium.click("//p[@class='login-button']/a");
	selenium.waitForPageToLoad("10000");
	Thread.sleep(2000);
    }
    
    /** checks for verification errors and stops the browser */
    public void openAuthoringCanvas() throws Exception {
	selenium.click("//div[@id='header-my-courses']//div[@class='tab-middle-highlight']/a");
	selenium.waitForPopUp("aWindow", "5000");
//	Thread.sleep(6000);
    }
    
    
    //TODO change this behaviour
    private String toolContentID;
    private String contentFolderID;
    
    /** checks for verification errors and stops the browser */
    public void openToolAuthoringWindow(String toolSignature) throws Exception {
	loginToLams();
	openAuthoringCanvas();
	
	Integer userID = userManagementService.getUserByLogin(USER_LOGIN).getUserId();
	String createUniqueContentFolderUrl = SERVER_URL + "authoring/author.do?method=createUniqueContentFolder&userID=" + userID;
	final String createUniqueContentFolderId = "createUniqueContentFolderId";
	selenium.openWindow(createUniqueContentFolderUrl, createUniqueContentFolderId);
	selenium.waitForPopUp(createUniqueContentFolderId, "5000");
	selenium.selectWindow(createUniqueContentFolderId);
	String wddxPacket = selenium.getEval("this.browserbot.getDocument().getElementsByTagName('body')[0].innerHTML");
	contentFolderID = this.extractFolderIDFromWDDXPacket(wddxPacket);
	selenium.close();
	selenium.selectWindow(null);
	
	Tool tool = toolDAO.getToolBySignature(toolSignature);
	String getToolContentUrl = SERVER_URL + "authoring/author.do?method=getToolContentID&toolID=" + tool.getToolId();
	final String getToolContentId = "getToolContentId";
	selenium.openWindow(getToolContentUrl, getToolContentId);
	selenium.waitForPopUp(getToolContentId, "5000");
	selenium.selectWindow(getToolContentId);
	wddxPacket = selenium.getEval("this.browserbot.getDocument().getElementsByTagName('body')[0].innerHTML");
	toolContentID = this.extractToolContentIDFromWDDXPacket(wddxPacket);
	selenium.close();
	selenium.selectWindow(null);
	
	String openToolUrl = SERVER_URL + tool.getAuthorUrl() + 
		"?mode=author" 
	    	+ "&toolContentID=" + toolContentID
		+ "&contentFolderID=" + contentFolderID;
	final String openToolId = "openToolId";
	selenium.openWindow(openToolUrl.toString(), openToolId);
	selenium.waitForPopUp(openToolId, "5000");
	selenium.selectWindow(openToolId);
    }
    
    /** checks for verification errors and stops the browser */
    public void closeToolAuthoringWindow() throws Exception {
	selenium.click("//span[@class='okIcon']");
	selenium.waitForPageToLoad("30000");
	selenium.click("//span[@class='close']");
	selenium.selectWindow("aWindow");
    }
    
    /** checks for verification errors and stops the browser */
    public void storeLearningDesign(String toolSignature, String newLearningDesignName) throws Exception {
	closeToolAuthoringWindow();
	
	//  TemplateActivityByLibraryID(libraryID);
	final String storeLearningDesignUrl = SERVER_URL + "servlet/authoring/storeLearningDesignDetails";
	String designDetails = constructWddxDesign(toolSignature, newLearningDesignName);
	selenium.runScript("var options = { " +
				"method:\"post\", " +
				"postBody:\"" + designDetails + "\" " +
			   "};" + 
			   "new Ajax.Request(\"" + storeLearningDesignUrl + "\",options);");
	selenium.close();
	selenium.selectWindow(null);
    }
    
    /** checks for verification errors and stops the browser */
    public void openToolMonitor(String toolSignature, String learningDesignName) throws Exception {
	loginToLams();
	
	String lessonIdStr = selenium.getEval("window.document.evaluate(\"//a[text()=' " + learningDesignName + "']/parent::*\", window.document, null, XPathResult.ANY_TYPE, null).iterateNext().id");
	Long lessonId = Long.parseLong(lessonIdStr);
	Lesson lesson = lessonDAO.getLesson(lessonId);
	String contentFolderID = lesson.getLearningDesign().getContentFolderID();
	Activity activity = lesson.getLearningDesign().getFirstActivity();
	;
	ToolActivity toolActivity = (ToolActivity) activityDAO.getActivityByActivityId(activity.getActivityId());
//	userManagementService.get
	
	selenium.click("//a[text()=' " + learningDesignName + "']/following-sibling::*[2]");
	selenium.waitForPopUp("mWindow", "15000");
	
	Tool tool = toolDAO.getToolBySignature(toolSignature);
	String monitorUrl = SERVER_URL + tool.getMonitorUrl()
		+ "?toolContentID=" + toolActivity.getToolContentId() + 
		"&contentFolderID=" + contentFolderID;
	final String monitorId = "monitorId";
	selenium.openWindow(monitorUrl, monitorId);
	selenium.waitForPopUp(monitorId, "5000");
	selenium.selectWindow(monitorId);
    }
    
    /** checks for verification errors and stops the browser */
    public void closeToolMonitor() throws Exception {
	selenium.close();
	selenium.selectWindow(null);
    }
    
	/**
     * @throws HibernateException
     */
    protected void initializeHibernateSession() throws HibernateException
    {
        //hold the hibernate session
		SessionFactory sessionFactory = (SessionFactory) this.context.getBean("notebookSessionFactory");
		Session s = sessionFactory.openSession();
		TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(s));
    }  
    /**
     * @throws HibernateException
     */
    protected void finalizeHibernateSession() throws HibernateException
    {
        //clean the hibernate session
	    SessionFactory sessionFactory = (SessionFactory)this.context.getBean("notebookSessionFactory");
	    SessionHolder holder = (SessionHolder)TransactionSynchronizationManager.getResource(sessionFactory);
	    if (holder != null) {
	    	Session s = holder.getSession(); 
		    s.flush();
		    TransactionSynchronizationManager.unbindResource(sessionFactory);
		    SessionFactoryUtils.releaseSession(s, sessionFactory);
	    }
    }




    /**
     * Given a WDDX packet in our normal format, gets the id number from within the &lt;var
     * name='messageValue'&gt;&lt;number&gt;num&lt;/number&gt;&lt;/var&gt;
     * 
     * @param wddxPacket
     * @return id
     */
    private String extractFolderIDFromWDDXPacket(String wddxPacket) {
	int indexMessageValue = wddxPacket.indexOf("<var name=\"messageValue\"><string>");
	assertTrue("<var name='messageValue'><string> string not found", indexMessageValue > 0);
	int endIndexMessageValue = wddxPacket.indexOf("</string></var>", indexMessageValue);
	return wddxPacket.substring(indexMessageValue + "<var name='messageValue'><string>".length(), endIndexMessageValue);
    }
    
    /**
     * Given a WDDX packet in our normal format, gets the id number from within the &lt;var
     * name='messageValue'&gt;&lt;number&gt;num&lt;/number&gt;&lt;/var&gt;
     * 
     * @param wddxPacket
     * @return id
     */
    private String extractToolContentIDFromWDDXPacket(String wddxPacket) {
//	Log.info("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa2" + wddxPacket);
	int indexMessageValue = wddxPacket.indexOf("<var name=\"messageValue\"><number>");
	assertTrue(wddxPacket + "!!<var name='messageValue'><number> string not found", indexMessageValue > 0);
	int endIndexMessageValue = wddxPacket.indexOf(".0</number></var>", indexMessageValue);
	String idString = wddxPacket.substring(indexMessageValue + "<var name='messageValue'><string>".length(), endIndexMessageValue);
	try {
	    Long.parseLong(idString);
	    return idString;
	} catch (NumberFormatException e) {
	    fail("Unable to get id number from WDDX packet. Format exception. String was " + idString);
	}
	return null;
    }
    
    private String constructWddxDesign(String toolSignature, String newLearningDesignName) throws Exception {
	Integer userID = userManagementService.getUserByLogin(USER_LOGIN).getUserId();
	Tool tool = toolDAO.getToolBySignature(toolSignature);
	
	ToolActivity templateActivity = null;
	for (Object activityObject : activityDAO.getAllActivities()) {
	    if (activityObject instanceof ToolActivity) {
		ToolActivity activity = (ToolActivity) activityObject;
		if ((activity.getLearningDesign() == null) && activity.getTool().getToolId().equals(tool.getToolId())) {
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
        			"<var name='workspaceFolderID'><number>4</number></var>" +
        			"<var name='maxID'><number>1</number></var>" +
        			"<var name='saveMode'><number>1</number></var>" +
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
    
    
    
    
//  /**
//   * Given a WDDX packet in our normal format, return the map object in the messageValue parameter. This should
//   * contain any returned ids.
//   * 
//   * @param wddxPacket
//   * @return Map
//   */
//  protected String extractIdMapFromWDDXPacket(String wddxPacket) {
//
//	Object obj = null;
//	try {
//	    obj = WDDXProcessor.deserialize(wddxPacket);
//	} catch (WddxDeserializationException e1) {
//	    fail("WddxDeserializationException " + e1.getMessage());
//	    e1.printStackTrace();
//	}
////	WDDXProcessor.convertToBoolean(table, key)
//
//	Map map = (Map) obj;
//	Object uniqueContentFolderObj = map.get("createUniqueContentFolder");
//	assertNotNull("createUniqueContentFolder object found", uniqueContentFolderObj);
//	if (!String.class.isInstance(uniqueContentFolderObj)) {
//	    fail("createUniqueContentFolder is not a String - try extractIdFromWDDXPacket(packet)");
//	}
//
//	return (String) uniqueContentFolderObj;
//  }

}
