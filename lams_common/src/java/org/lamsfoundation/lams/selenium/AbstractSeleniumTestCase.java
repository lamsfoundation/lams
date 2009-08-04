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
package org.lamsfoundation.lams.selenium;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.ActivityDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningDesignDAO;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dao.hibernate.LessonDAO;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.dao.hibernate.ToolDAO;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.Workspace;
import org.lamsfoundation.lams.usermanagement.service.UserManagementService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.thoughtworks.selenium.HttpCommandProcessor;
import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;

public abstract class AbstractSeleniumTestCase extends SeleneseTestCase {

    protected static Selenium selenium;
    
    protected static ToolDAO toolDAO;
    protected static ActivityDAO activityDAO;
    protected static UserManagementService userManagementService;
    protected static LessonDAO lessonDAO;
    protected static LearningDesignDAO learningDesignDAO;

    private static final String[] contextConfigLocation = new String[] {
	    "org/lamsfoundation/lams/localCommonContext.xml",
	    "org/lamsfoundation/lams/lesson/lessonApplicationContext.xml",
	    "org/lamsfoundation/lams/toolApplicationContext.xml", };

    // name of the learning design. we receive it from the subclass initially.
    // but then it might be changed if LD with the same name already exists
    private static String learningDesignName;
    
    //contains contentFolderID and toolContentID received during setUpAuthoring()
    private HashMap<String, String> authoringSettings;

    @Override
    public void setUp() throws Exception {
	if (selenium == null) {
	    ApplicationContext context = new ClassPathXmlApplicationContext(contextConfigLocation);
	    toolDAO = (ToolDAO) context.getBean("toolDAO");
	    activityDAO = (ActivityDAO) context.getBean("activityDAO");
	    userManagementService = (UserManagementService) context.getBean("userManagementService");
	    lessonDAO = (LessonDAO) context.getBean("lessonDAO");
	    learningDesignDAO = (LearningDesignDAO) context.getBean("learningDesignDAO");
	    learningDesignName = getLearningDesignName();

	    HttpCommandProcessor proc = new HttpCommandProcessor(SeleniumConstants.SERVER_HOST,
		    SeleniumConstants.SERVER_PORT, SeleniumConstants.BROWSER,
		    SeleniumConstants.WEB_APP_HOST + SeleniumConstants.WEB_APP_DIR);
	    selenium = new DefaultSeleniumFlex(proc);
	    selenium.start();
	}
	// do not reduce this parameter as Selenium might start working wrong
	selenium.setSpeed("400");
    }
    
    @Override
    public void tearDown() throws Exception {
	checkForVerificationErrors();
	
	//leaves only Lams main window opened
	String[] windowNames = selenium.getAllWindowNames();
	for (String windowName : windowNames) {
	    if (!(windowName.equals(SeleniumConstants.LAMS_MAIN_WINDOW_NAME) || windowName.equals("null"))) {
		selenium.selectWindow(windowName);
		selenium.close();
	    }
	}
	selenium.selectWindow(null);
    }
    
    /**
     * Tears down Selenium. Also provides output for all Selenium verify commands. 
     */
    public static void tearDownSelenium() {
	if (selenium != null) {
	    selenium.stop();
	    selenium = null;
	}
    }

    /**
     * Returns tool's signature. This method should be overridden by all
     * subclasses.
     * 
     * @return tool's signature
     */
    protected abstract String getToolSignature();

    /**
     * Returns the name of learning design as it should be saved. Should be
     * overridden by all subclasses.
     * 
     * @return name of learning design
     */
    protected abstract String getLearningDesignName();
    
    /** */
    protected void loginToLams() throws Exception {
	//prevent introductionary window from appearing (for users logged in for the first time)
	User user = userManagementService.getUserByLogin(SeleniumConstants.USER_LOGIN);
	if (user.isFirstLogin()) {
	    user.setFirstLogin(false);
	    Workspace workspace = (Workspace) activityDAO.find(Workspace.class, user.getWorkspace().getWorkspaceId());
	    user.setWorkspace(workspace);
	    userManagementService.save(user);
	}
	
	selenium.open(SeleniumConstants.WEB_APP_DIR);
	selenium.waitForPageToLoad("20000");
	
	// if it's IE try to logout
	if (SeleniumConstants.BROWSER.equals(SeleniumConstants.BROWSER_INTERNET_EXPLORER)
		&& selenium.isElementPresent("link=Logout")) {
	    selenium.click("link=Logout");
	    selenium.waitForPageToLoad("20000");
	}

	assertTrue("Unable to access LAMS login page.", selenium.isElementPresent("link=Login"));
	selenium.type("j_username", SeleniumConstants.USER_LOGIN);
	selenium.type("j_password", SeleniumConstants.USER_PASSWORD);
	selenium.click("link=Login");
	selenium.waitForPageToLoad("20000");
	Thread.sleep(3000);

    }

    /**
     * Opens up all authoring windows. Should be done before testing tool's
     * authoring.
     * 
     * @return Map containing contentFolderID and toolContentID
     * @throws Exception
     */
    protected void setUpAuthoring() throws Exception {
	// open authoring canvas
	selenium.click("//div[@id='header-my-courses']//div[@class='tab-middle-highlight']/a");
	selenium.waitForPopUp("aWindow", "10000");

	Integer userID = userManagementService.getUserByLogin(SeleniumConstants.USER_LOGIN).getUserId();
	String createUniqueContentFolderUrl = SeleniumConstants.WEB_APP_DIR
		+ "authoring/author.do?method=createUniqueContentFolder&userID=" + userID;
	final String createUniqueContentFolderId = "createUniqueContentFolderId";
	selenium.openWindow(createUniqueContentFolderUrl, createUniqueContentFolderId);
	selenium.waitForPopUp(createUniqueContentFolderId, "10000");
	selenium.selectWindow(createUniqueContentFolderId);
	String wddxPacket = selenium.getEval("this.browserbot.getDocument().getElementsByTagName('body')[0].innerHTML");
	final String contentFolderID = this.extractFolderIDFromWDDXPacket(wddxPacket);
	selenium.close();
	selenium.selectWindow(null);

	Tool tool = toolDAO.getToolBySignature(getToolSignature());
	String getToolContentUrl = SeleniumConstants.WEB_APP_DIR
		+ "authoring/author.do?method=getToolContentID&toolID=" + tool.getToolId();
	final String getToolContentId = "getToolContentId";
	selenium.openWindow(getToolContentUrl, getToolContentId);
	selenium.waitForPopUp(getToolContentId, "10000");
	selenium.selectWindow(getToolContentId);
	wddxPacket = selenium.getEval("this.browserbot.getDocument().getElementsByTagName('body')[0].innerHTML");
	final String toolContentID = this.extractToolContentIDFromWDDXPacket(wddxPacket);
	selenium.close();
	selenium.selectWindow(null);

	String openToolUrl = SeleniumConstants.WEB_APP_DIR + tool.getAuthorUrl() + "?mode=author"
		+ "&toolContentID=" + toolContentID + "&contentFolderID=" + contentFolderID;
	final String openToolId = "openToolId";
	selenium.openWindow(openToolUrl.toString(), openToolId);
	selenium.waitForPopUp(openToolId, "10000");
	selenium.selectWindow(openToolId);

	authoringSettings = new HashMap<String, String>() {
	    {
		put("contentFolderID", contentFolderID);
		put("toolContentID", toolContentID);
	    }
	};
    }

    /**
     * Stores learning design. Uses getLearningDesignName() as a new design
     * name.
     */
    protected void storeLearningDesign() {
	//closes tool authoring screen
	selenium.click("//span[@class='okIcon']");
	selenium.waitForPageToLoad("10000");
	selenium.selectWindow(null);

	final String storeLearningDesignUrl = SeleniumConstants.WEB_APP_DIR
		+ "servlet/authoring/storeLearningDesignDetails";
	String designDetails = constructWddxDesign(authoringSettings);
	//callback function is aimed to let Selenium wait till StoreLDServlet finishes its work
	selenium.runScript("$.post(\"" + storeLearningDesignUrl + "\", " +
			"\"" + designDetails + "\", " +
			"function(data){ return data; }" +
			");");
    }

    /**
     * Beware this method might work wrong (due to the CloudWizard's
     * hidden/visible root element)
     * 
     * @throws Exception
     */
    public void createNewLesson() throws Exception {
	Long lastCreatedLessonId = getLastCreatedLessonId(true);
	DefaultSeleniumFlex flexSelenium = (DefaultSeleniumFlex) selenium;
	
	flexSelenium.click("link=Add Lesson");
	Thread.sleep(6000);
	// flexObjId is now set to "cloudWizard" by default, change it if
	// you want to use another one.
	// flexSelenium.flexSetFlexObjID("cloudWizard");
	waitForFlexExists("workspaceTree", 20, flexSelenium);
	assertTrue(flexSelenium.getFlexEnabled("startButton").equals("true"));

	User user = userManagementService.getUserByLogin(SeleniumConstants.USER_LOGIN);
	Workspace workspace = (Workspace) activityDAO.find(Workspace.class, user.getWorkspace().getWorkspaceId());
	Integer workspaceFolderID = workspace.getDefaultFolder().getWorkspaceFolderId();
	List<String> titles = learningDesignDAO.getLearningDesignTitlesByWorkspaceFolder(workspaceFolderID);
	assertTrue("There is no stored learning design", titles.size() > 0);
	//TODO receive sorted titles list from the server because if LD was imported it's not sorted alphabetically
	Collections.sort(titles, String.CASE_INSENSITIVE_ORDER);
	int count = 1;
	for (String title : titles) {
	    if (title.equals(learningDesignName)) {
		break;
	    }
	    count++;
	}
	assertTrue("There isn't learning design with name " + learningDesignName, count <= titles.size());

	flexSelenium.flexSelectIndex("workspaceTree", String.valueOf(count));
	flexSelenium.flexClick("startButton");
	Thread.sleep(5000);
	
	// TODO fix CloudWizard or define offset checking for lesson's LD name
	final String cloudWizardError = "Tests aborted due to the problem with CloudWizard's root element problem. Please, restart tests";
	assertTrue(cloudWizardError, lastCreatedLessonId < getLastCreatedLessonId(true));

	Long lessonId = getLastCreatedLessonId(false);
	String lessonTitle = lessonDAO.getLesson(lessonId).getLessonName();
	assertTrue(cloudWizardError, learningDesignName.equals(lessonTitle));
    }

    /**
     * Opens up learning. It should be used in the beginning of each learner test.
     * 
     * @throws InterruptedException
     */
    protected void setUpLearning() throws InterruptedException {
	selenium.runScript("openLearner(" + getLastCreatedLessonId(false) + ")");
	waitForLearning();
    }
    
    /**
     * Waits for learning. It can be used after learning refresh.
     * 
     * @throws InterruptedException
     */
    protected void waitForLearning() throws InterruptedException {
	selenium.waitForPopUp("lWindow", "30000");
	selenium.selectWindow("lWindow");
	waitForElementPresent("contentFrame");
	selenium.selectFrame("contentFrame");
	waitForElementPresent("//html//div[@id='content']");
    }

    /**
     * Closes Lams learning. Then selects back Lams main window.
     */
    protected void tearDownLearning() {
	selenium.close();
	selenium.selectWindow(null);
    }

    /**
     * Launches Lams monitor, followed by tool's monitor popup.
     */
    protected void setUpMonitoring() {
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
	String monitorUrl = SeleniumConstants.WEB_APP_DIR + tool.getMonitorUrl() + "?toolContentID="
		+ toolActivity.getToolContentId() + "&contentFolderID=" + contentFolderID;
	final String monitorId = "monitorId";
	selenium.openWindow(monitorUrl, monitorId);
	selenium.waitForPopUp(monitorId, "50000");
	selenium.selectWindow(monitorId);
    }

    /**
     * Closes Lams monitor. Then selects back Lams main window.
     */
    protected void tearDownMonitoring() {
	selenium.close();
	selenium.selectWindow(null);
    }

    // *****************************************************************************
    // methods for testing Flex
    // *****************************************************************************

    /**
     * Waits till element will be presented on a page.
     * 
     * @param locator
     *            - an element locator
     * @throws InterruptedException
     */
    protected void waitForElementPresent(String locator) throws InterruptedException {
	for (int second = 0;; second++) {
	    if (second >= 30) {
		fail("Timeout while waiting for element with locator " + locator);
	    }
	    try {
		if (selenium.isElementPresent(locator)) {
		    break;
		}
	    } catch (Exception e) {
	    }
	    Thread.sleep(1000);
	}
    }

    @Override
    public String getText() {
	return selenium.getEval("this.page().bodyText()");
    }

    protected void waitForFlexExists(String objectID, int timeout, DefaultSeleniumFlex selenium) throws Exception {
	while (timeout > 0 && !selenium.getFlexExists(objectID).contains("true")) {
	    Thread.sleep(1000);
	    timeout--;
	}
	if (timeout == 0) {
	    throw new Exception("waitForFlexExists flex object:" + objectID + " Timed Out");
	}
    }

    protected void waitForFlexVisible(String objectID, int timeout, DefaultSeleniumFlex selenium) throws Exception {
	while (timeout > 0 && !selenium.getFlexVisible(objectID).equals("true")) {
	    Thread.sleep(1000);
	    timeout--;
	}
	if (timeout == 0) {
	    throw new Exception("waitForFlexVisible flex object:" + objectID + " Timed Out");
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
	Integer userID = userManagementService.getUserByLogin(SeleniumConstants.USER_LOGIN).getUserId();
	List<Lesson> lessonsCreatedByUser = lessonDAO.getLessonsCreatedByUser(userID);
	List<Lesson> lessonsWithLDName = new ArrayList<Lesson>();
	for (Lesson lesson : lessonsCreatedByUser) {
	    if (lesson.getLessonName().equals(learningDesignName)) {
		lessonsWithLDName.add(lesson);
	    }
	}
	if (lessonsWithLDName.size() == 0) {
	    if (returnNegative) {
		return new Long(-1);
	    } else {
		fail("Lesson creation failed.");
	    }
	}

	Lesson lastCreatedLesson = lessonsCreatedByUser.iterator().next();
	for (Lesson lesson : lessonsWithLDName) {
	    if (lesson.getCreateDateTime().after(lastCreatedLesson.getCreateDateTime())) {
		lastCreatedLesson = lesson;
	    }
	}
	return lastCreatedLesson.getLessonId();
    }

    /**
     * Given a WDDX packet in our normal format, gets the content folder from
     * within the &lt;var
     * name='messageValue'&gt;&lt;string&gt;num&lt;/string&gt;&lt;/var&gt;
     * 
     * @param wddxPacket
     * @return id
     */
    private String extractFolderIDFromWDDXPacket(String wddxPacket) {
	wddxPacket = wddxPacket.toLowerCase();
	int indexMessageValue = wddxPacket.indexOf("<var name=\"messagevalue\"><string>");
	assertTrue("<var name='messageValue'><string> string not found", indexMessageValue > 0);
	int endIndexMessageValue = wddxPacket.indexOf("</string></var>", indexMessageValue);
	return wddxPacket.substring(indexMessageValue + "<var name='messagevalue'><string>".length(),
		endIndexMessageValue);
    }

    /**
     * Given a WDDX packet in our normal format, gets the tool content id from
     * within the &lt;var
     * name='messageValue'&gt;&lt;number&gt;num&lt;/number&gt;&lt;/var&gt;
     * 
     * @param wddxPacket
     * @return id
     */
    private String extractToolContentIDFromWDDXPacket(String wddxPacket) {
	wddxPacket = wddxPacket.toLowerCase();
	int indexMessageValue = wddxPacket.indexOf("<var name=\"messagevalue\"><number>");
	assertTrue("<var name='messageValue'><number> string not found", indexMessageValue > 0);
	int endIndexMessageValue = wddxPacket.indexOf(".0</number></var>", indexMessageValue);
	String idString = wddxPacket.substring(indexMessageValue + "<var name='messagevalue'><string>".length(),
		endIndexMessageValue);
	try {
	    Long.parseLong(idString);
	    return idString;
	} catch (NumberFormatException e) {
	    fail("Unable to get toolContentID from WDDX packet. Format exception. String was " + idString);
	}
	return null;
    }

    /**
     * Constructs wddx packet(the same as Flash creates) which contains LD
     * details.
     * 
     * @return
     */
    private String constructWddxDesign(Map<String, String> contentDetails) {
	User user = userManagementService.getUserByLogin(SeleniumConstants.USER_LOGIN);
	Tool tool = toolDAO.getToolBySignature(getToolSignature());

	//checks if another LD with the same name exists. If so then trying to create unique one.
	Workspace workspace = (Workspace) activityDAO.find(Workspace.class, user.getWorkspace().getWorkspaceId());
	Integer workspaceFolderID = workspace.getDefaultFolder().getWorkspaceFolderId();
	List<String> existingTitles = learningDesignDAO.getLearningDesignTitlesByWorkspaceFolder(workspaceFolderID);
	if (existingTitles.contains(learningDesignName)) {
	    int i = 2;
	    String tempLearningDesignName;
	    do {
		tempLearningDesignName = learningDesignName + "(" + i++ + ")";
	    } while (existingTitles.contains(tempLearningDesignName));
	    learningDesignName = tempLearningDesignName;
	}

	// searching for the template activity
	ToolActivity templateActivity = null;
	for (Object activityObject : activityDAO.getAllActivities()) {
	    if (activityObject instanceof ToolActivity) {
		ToolActivity activity = (ToolActivity) activityObject;
		if ((activity.getLearningDesign() == null) && activity.getTool().getToolId().equals(tool.getToolId())) {
		    templateActivity = activity;
		    break;
		}
	    }
	}

	String design = 
	    "<wddxPacket version='1.0'>" + "<header />" + "<data>" + "<struct>" + 
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
        				"<var name='toolContentID'><number>" + contentDetails.get("toolContentID") + "</number></var>" +
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
        				//works with activityUIID=1 but may be we should generate unique one.
        				"<var name='activityUIID'><number>1</number></var>" +
        				"<var name='activityCategoryID'><number>" + templateActivity.getActivityCategoryID() + "</number></var>" +
        				"<var name='activityID'><number>" + templateActivity.getActivityId() + "</number></var>" +
        				"<var name='activityTypeID'><number>" + templateActivity.getActivityTypeId() + "</number></var>" +
        			"</struct>" +
        		"</array>" +
        	"</var>" +
        	"<var name='contentFolderID'><string>" + contentDetails.get("contentFolderID") + "</string></var>" +
        	"<var name='createDateTime'><dateTime>2009-6-24T22:45:24+3:0</dateTime></var>" +
        	"<var name='workspaceFolderID'><number>" + workspaceFolderID + "</number></var>" +
        	"<var name='maxID'><number>1</number></var>" +
        	"<var name='saveMode'><number>0</number></var>" +
        	"<var name='validDesign'><boolean value='true' /></var>" +
        	"<var name='readOnly'><boolean value='false' /></var>" +
        	"<var name='userID'><string>" + user.getUserId() + "</string></var>" +
        	"<var name='title'><string>" + learningDesignName + "</string></var>" +
        	"<var name='learningDesignID'><number>-111111</number></var>" +
        	"<var name='copyTypeID'><number>1</number></var>" +
            "</struct>" + "</data>" + "</wddxPacket>";
	return design;
    }
}
