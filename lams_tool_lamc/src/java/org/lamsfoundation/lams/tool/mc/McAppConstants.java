/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.mc;

/**
 * @author Ozgur Demirtas
 *
 * TODO: Check back the constants with "Attention"
 * Holds constants used throughout the application
 * 
 */
public interface McAppConstants {
	/**
	 * Currently we are hardcoding the default content id.
     * This will be replaced when the deploy logic automatically assigns a default content id in the deploy script.
     */
	
	public static final String MY_SIGNATURE 							="lamc11";
	public static final long DEFAULT_CONTENT_ID 						=0;
    public static final long DEFAULT_QUE_CONTENT_ID 					=1;

	/**
	 * temporarily refers to an existing content id for an incoming tool session id, won't need it in deployment environment 
	 */
	public static final long THE_MOCKED_CONTENT_ID						=1803739427456570536L;  
	public static final String TOOL_SERVICE								="tool_service";
	/**
	 * indicates which mode the tool runs under between Authoring, Learning, Monitoring
	 */
	public static final String TARGET_MODE								="targetMode";
	public static final String TARGET_MODE_AUTHORING					="Authoring";
	public static final String TARGET_MODE_LEARNING						="Learning";
	public static final String TARGET_MODE_MONITORING					="Monitoring";
	public static final String TARGET_MODE_EXPORT_PORTFOLIO				="ExportPortfolio";
	
	public static final String AUTHORING_STARTER						="starter";
	public static final String LEARNING_STARTER							="learningStarter";
	public static final String MONITORING_STARTER						="monitoringStarter";
	
	
	/** these constants are from org.lamsfoundation.lams.web.util.AttributeNames  */
	public static final String USER= "user";
	public static final String TOOL_CONTENT_ID = "toolContentID";
	public static final String TOOL_SESSION_ID = "toolSessionID"; 
	public static final String MODE = "mode";
	public static final String USER_ID = "userID";
    public static final String ACTIVITY_ID = "activityID";
	
	
	/**
	 * Mock constants below are temporary until the tool gets a User object from the container.
	 */
	public static final Integer	MOCK_USER_ID							= new Integer(111);
	public static final String  MOCK_USER_NAME							="Ozgur";
	public static final String  MOCK_USER_LASTNAME						="Demirtas";
	public static final String  MOCK_LOGIN_NAME							="ozgur";
	
    /**
     * refers to number of questions presented initially, we have a single record for default content
     */
    public static final Long INITIAL_QUESTION_COUNT		=new Long(1);
    public static final long MAX_QUESTION_COUNT 		=20;
    public static final long MAX_OPTION_COUNT 			=10;
    
    
    /**
     * Struts level constants
     */
    public static final String LOAD										="load";
    public static final String LOAD_QUESTIONS							="load";
    public static final String LOAD_STARTER								="starter";
    public static final String EDIT_OPTS_CONTENT						="editOptsContent";
    public static final String ADD_OPTS_CONTENT							="addOptsContent";
    public static final String REMOVE_OPTS_CONTENT						="removeOptsContent";
    public static final String USER_ACTION								="userAction";
    
    
    public static final String AUTHORING_BASIC							="authoringBasic";
    public static final boolean MODE_OPTIONAL 							= false;
    public static final String DISABLE_TOOL								="disabled";
    public static final String IS_DEFINE_LATER							="isDefineLater";
    
    /**
     * authoring mode controllers
     */ 
    public static final String IS_ADD_QUESTION							="isAddQuestion";
    public static final String IS_REMOVE_QUESTION						="isRemoveQuestion";
    public static final String IS_REMOVE_CONTENT						="isRemoveContent";
    
    public static final String QUESTIONS_SEQUENCED						="questionsSequenced";
        
    /**
     * tab controllers, constants for authoring page html tabs, used within jsp
     */
    public static final String CHOICE									="choice";
    public static final String CHOICE_TYPE_BASIC						="choiceTypeBasic";
    public static final String CHOICE_TYPE_ADVANCED						="choiceTypeAdvanced";
    public static final String CHOICE_TYPE_INSTRUCTIONS					="choiceTypeInstructions";

    
    /**
     * authoring mode constants
     */
    public static final String MAP_QUESTIONS_CONTENT					="mapQuestionsContent";
    public static final String MAP_OPTIONS_CONTENT						="mapOptionsContent";
    public static final String MAP_DEFAULTOPTIONS_CONTENT				="mapDefaultOptionsContent";
    public static final String MAP_DISABLED_QUESTIONS					="mapDisabledQuestions";
    
    public static final String SELECTED_QUESTION_CONTENT_UID			="selectedQuestionContentUid";
    public static final String SELECTED_QUESTION						="selectedQuestion";
    public static final String SELECTED_QUESTION_INDEX					="selectedQuestionIndex";
    
    
    public static final String MAP_QUESTION_CONTENT						="mapQuestionContent";
    public static final String DEFAULT_QUESTION_CONTENT					="defaultQuestionContent";
    public static final String TITLE									="title";
    public static final String INSTRUCTIONS								="instructions";
    public static final String CREATED_BY								="createdBy";
    public static final String CREATION_DATE							="creationDate";
    public static final String USERNAME_VISIBLE							="usernameVisible";
    public static final String RUN_OFFLINE								="runOffline";
    public static final String DEFINE_LATER								="defineLater";
    public static final String SYNCH_IN_MONITOR							="synchInMonitor";
    public static final String RETRIES									="retries";
    public static final String PASSMARK									="passMark";
    public static final String SHOW_FEEDBACK							="showFeedback";
    
    
    public static final String ONLINE_INSTRUCTIONS						="onlineInstructions";
    public static final String OFFLINE_INSTRUCTIONS						="offlineInstructions";
    public static final String END_LEARNING_MESSSAGE					="endLearningMessage";
    public static final String CONTENT_LOCKED							="contentLocked";
    public static final String CONTENT_IN_USE							="contentInUse";
    public static final String ON										="ON";
    public static final String OFF										="OFF";
    public static final String IS_USERNAME_VISIBLE_MONITORING			="isUsernameVisibleMonitoring";
    public static final String IS_SYNCH_INMONITOR_MONITORING			="isSynchInMonitorMonitoring";
    public static final String IS_QUESTIONS_SEQUENCED_MONITORING		="isQuestionsSequencedMonitoring";
    
    
    
    public static final String RICHTEXT_OFFLINEINSTRUCTIONS				="richTextOfflineInstructions";
    public static final String RICHTEXT_ONLINEINSTRUCTIONS				="richTextOnlineInstructions";
    public static final String RICHTEXT_TITLE							="richTextTitle";
    public static final String RICHTEXT_INSTRUCTIONS					="richTextInstructions";
    public static final String RICHTEXT_BLANK							="<p>&nbsp;</p>";
    public static final String THE_OFFLINE_FILE							="theOfflineFile";
    public static final String THE_ONLINE_FILE							="theOnlineFile";
    public static final String THE_OFFLINE_FILENAME						="theOfflineFileName";
    public static final String THE_ONLINE_FILENAME						="theOnlineFileName";
    public static final String SUBMIT_OFFLINE_FILE						="submitOfflineFile";
    public static final String SUBMIT_ONLINE_FILE						="submitOnlineFile";
    public static final String LIST_UPLOADED_OFFLINE_FILES				="listUploadedOfflineFiles";
    public static final String LIST_UPLOADED_ONLINE_FILES				="listUploadedOnlineFiles";
    public static final String LIST_UPLOADED_OFFLINE_FILENAMES			="listUploadedOfflineFileNames";
    public static final String LIST_UPLOADED_ONLINE_FILENAMES			="listUploadedOnlineFileNames";
    public static final String LIST_UPLOADED_OFFLINE_FILES_UUID			="listUploadedOfflineFilesUuid";
    public static final String LIST_UPLOADED_OFFLINE_FILES_NAME			="listUploadedOfflineFilesName";
    public static final String LIST_UPLOADED_ONLINE_FILES_UUID			="listUploadedOnlineFilesUuid";
    public static final String LIST_UPLOADED_ONLINE_FILES_NAME			="listUploadedOnlineFilesName";
    public static final String POPULATED_UPLOADED_FILESDATA				="populateUploadedFilesData";
    
    
    /**
     * the author's current content id
     */
    //public static final String TOOL_CONTENT_ID							="toolContentId";
    
    /**
     * the learner or monitoring environment provides toolSessionId
     */
    //public static final String TOOL_SESSION_ID							="toolSessionId";
    public final long 	ONE_DAY 										=60 * 60 * 1000 * 24;
    public final static String NOT_ATTEMPTED 							="NOT_ATTEMPTED";
    public final static String INCOMPLETE 								="INCOMPLETE";
    public static final String COMPLETED 								="COMPLETED";
    
    public static final String MAP_TOOL_SESSIONS						="mapToolSessions";
    public static final String RESPONSE_INDEX							="responseIndex";
    public static final Integer MAX_TOOL_SESSION_COUNT					=new Integer(500);
    public static final String IS_TOOL_SESSION_CHANGED					="isToolSessionChanged";
    public static final String USER_EXCEPTION_WRONG_FORMAT				="userExceptionWrongFormat";
    public static final String USER_EXCEPTION_UNCOMPATIBLE_IDS      	="userExceptionUncompatibleIds";
    public static final String USER_EXCEPTION_NUMBERFORMAT  	    	="userExceptionNumberFormat";
    public static final String USER_EXCEPTION_CONTENT_DOESNOTEXIST		="userExceptionContentDoesNotExist";
    public static final String USER_EXCEPTION_TOOLSESSION_DOESNOTEXIST	="userExceptionToolSessionDoesNotExist";
    public static final String USER_EXCEPTION_CONTENTID_REQUIRED		="userExceptionContentIdRequired";
    public static final String USER_EXCEPTION_TOOLSESSIONID_REQUIRED	="userExceptionToolSessionIdRequired";
    public static final String USER_EXCEPTION_DEFAULTCONTENT_NOT_AVAILABLE 			="userExceptionDefaultContentNotAvailable";
    public static final String USER_EXCEPTION_DEFAULTQUESTIONCONTENT_NOT_AVAILABLE 	="userExceptionDefaultQuestionContentNotAvailable";
    public static final String USER_EXCEPTION_DEFAULTOPTIONSCONTENT_NOT_AVAILABLE 	="userExceptionDefaultOptionsContentNotAvailable";
    
    public static final String USER_EXCEPTION_USERID_NOTAVAILABLE		="userExceptionUserIdNotAvailable";
    public static final String USER_EXCEPTION_USERID_NOTNUMERIC			="userExceptionUserIdNotNumeric";
    public static final String USER_EXCEPTION_ONLYCONTENT_ANDNOSESSIONS ="userExceptionOnlyContentAndNoSessions";
    public static final String USER_EXCEPTION_USERID_EXISTING			="userExceptionUserIdExisting";
    public static final String USER_EXCEPTION_USER_DOESNOTEXIST			="userExceptionUserDoesNotExist";
    public static final String USER_EXCEPTION_MONITORINGTAB_CONTENTID_REQUIRED	="userExceptionMonitoringTabContentIdRequired";
    public static final String USER_EXCEPTION_DEFAULTCONTENT_NOTSETUP	="userExceptionDefaultContentNotSetup";
    public static final String USER_EXCEPTION_NO_TOOL_SESSIONS			="userExceptionNoToolSessions";
    
    /**
     * export portfolio constants
     */
    public static final String LEARNER									="learner"; 
    public static final String TEACHER									="teacher";
    public static final String USER_EXCEPTION_MODE_REQUIRED				="userExceptionModeRequired";					
    public static final String PORTFOLIO_REPORT							="portfolioReport";
    public static final String PORTFOLIO_REQUEST						="portfolioRequest";
    public static final int    MAX_GROUPS_COUNT							=250;						
        
    /**
     * user actions
     */
    public static final String ADD_NEW_QUESTION							="addNewQuestion";
    public static final String REMOVE_QUESTION							="removeQuestion";
    public static final String REMOVE_ALL_CONTENT						="removeAllContent";
    public static final String SUBMIT_ALL_CONTENT						="submitAllContent";
    public static final String SUBMIT_TAB_DONE							="submitTabDone";
    
    public static final String OPTION_OFF								="false";
    
    //LEARNER mode contants
    public static final String MAP_QUESTION_CONTENT_LEARNER				="mapQuestionContentLearner";
    public static final String CURRENT_QUESTION_INDEX					="currentQuestionIndex";
    public static final String TOTAL_QUESTION_COUNT						="totalQuestionCount";
    public static final String MAP_ANSWERS								="mapAnswers";
    public static final String CURRENT_ANSWER							="currentAnswer";
    public static final String USER_FEEDBACK							="userFeedback";
    public static final String REPORT_TITLE								="reportTitle"; 
    public static final String MONITORING_REPORT_TITLE					="monitoringReportTitle"; 
    public static final String REPORT_TITLE_LEARNER						="reportTitleLearner"; 
    public static final String END_LEARNING_MESSAGE						="endLearningMessage";
    
    public static final String IS_TOOL_ACTIVITY_OFFLINE					="isToolActivityOffline";
    public static final String IS_USERNAME_VISIBLE						="isUsernameVisible";
    public static final String IS_ALL_SESSIONS_COMPLETED				="isAllSessionsCompleted";
    public static final String CHECK_ALL_SESSIONS_COMPLETED				="checkAllSessionsCompleted";
    public static final String FROM_TOOL_CONTENT_ID						="fromToolContentId";
    public static final String TO_TOOL_CONTENT_ID						="toToolContentId";
    public static final String LEARNER_REPORT							="learnerReport";
    public static final String MAP_USER_RESPONSES						="mapUserResponses";
    public static final String MAP_MAIN_REPORT							="mapMainReport";
    public static final String MAP_STATS								="mapStats";
    public static final String ACTIVITY_TITLE							="activityTitle";
    public static final String ACTIVITY_INSTRUCTIONS					="activityInstructions";
        
    /**
     * Monitoring Mode constants
     */
    public static final String REPORT_TITLE_MONITOR						="reportTitleMonitor"; 
    public static final String MONITOR_USER_ID							="userId";
    public static final String MONITORING_REPORT						="monitoringReport";
    public static final String MONITORING_ERROR							="monitoringError";
    public static final String MAP_MONITORING_QUESTIONS					="mapMonitoringQuestions";
    public static final String SUMMARY_TOOL_SESSIONS					="summaryToolSessions";
    public static final String CURRENT_MONITORED_TOOL_SESSION 			="currentMonitoredToolSession";
    public static final String CHOICE_MONITORING			  			="choiceMonitoring";
    public static final String CHOICE_TYPE_MONITORING_SUMMARY 			="choiceTypeMonitoringSummary";
    public static final String CHOICE_TYPE_MONITORING_INSTRUCTIONS 		="choiceTypeMonitoringInstructions";
    public static final String CHOICE_TYPE_MONITORING_EDITACTIVITY 		="choiceTypeMonitoringEditActivity";
    public static final String CHOICE_TYPE_MONITORING_STATS 	   		="choiceTypeMonitoringStats";
    public static final String MONITORING_INSTRUCTIONS_VISITED 	   		="monitoringInstructionsVisited";
    public static final String MONITORING_EDITACTIVITY_VISITED 	   		="monitoringEditActivityVisited";
    public static final String MONITORING_STATS_VISITED 	   	   		="monitoringStatsVisited";
    public static final String DATAMAP_EDITABLE					   		="dataMapEditable";
    public static final String DATAMAP_EDITABLE_RESPONSE_ID		   		="dataMapEditableResponseId";
    public static final String DATAMAP_HIDDEN_RESPONSE_ID		   		="dataMapHiddenResponseId";
    public static final String MONITORED_CONTENT_ID				   		="monitoredContentId";
    public static final String START_MONITORING_SUMMARY_REQUEST	   		="startMonitoringSummaryRequest";
    public static final String STOP_RENDERING_QUESTIONS			   		="stopRenderingQuestions";
    public static final String EDITACTIVITY_EDITMODE	   		   		="editActivityEditMode";
    public static final String FORM_INDEX	   		   			   		="formIndex";
    public static final String RENDER_MONITORING_EDITACTIVITY	   		="renderMonitoringEditActivity";
    public static final String NO_AVAILABLE_SESSIONS	   		   		="noAvailableSessions";
    public static final String INITIAL_MONITORING_TOOL_CONTENT_ID  		="initialMonitoringToolContentId";
    public static final String IS_MONITORING_DEFINE_LATER  		   		="isMonitoringDefineLater";
    public static final String NO_TOOL_SESSIONS_AVAILABLE				="noToolSessionAvailable";
    public static final String ORIGINAL_TOOL_SESSIONS					="originalToolSessions";
    
    public static final String MONITORED_OFFLINE_INSTRUCTIONS			="monitoredOfflineInstructions";
    public static final String MONITORED_ONLINE_INSTRUCTIONS			="monitoredOnlineInstructions";
    public static final String MONITORING_INSTRUCTIONS_UPDATE_MESSAGE	="monitoringInstructionsUpdateMessage";
    
    /**
     * Monitor and Learning common constants - used in jsp reporting
     */
    public static final String FULLNAME									="fullName";
    public static final String ANSWER									="answer";
    public static final String ATIME									="aTime";
    public static final String FORMATTED_ATIME							="formattedAtime";
    public static final String RESPONSE_ID								="responseId";
    public static final String RESPONSE_HIDDEN							="responseHidden";
    public static final String CURRENTLEARNER_FULLNAME					="currentLearnerFullname";
    public static final String ATTR_USERDATA 							="qa_user";
    public static final String TIMEZONE									="timeZone";
    public static final String TIMEZONE_ID								="timeZoneId";
    
    /**
     * following tell whether author prefers to  have the questions listed all in one page or listed sequentially. The default is all in one page.
     */
    public static final String QUESTION_LISTING_MODE					="questionListingMode";
    public static final String QUESTION_LISTING_MODE_SEQUENTIAL			="questionListingModeSequential";
    public static final String QUESTION_LISTING_MODE_COMBINED			="questionListingModeCombined";
    
    public static final String FEEDBACK_TYPE_SEQUENTIAL					="You will be presented a total of : ";
    public static final String FEEDBACK_TYPE_COMBINED					="You are being presented a total of : ";
    public static final String QUESTIONS								=" questions.";

    /**
     * constants redundant for the moment
     */
    public static final String DISPLAY_QUESTIONS 						="display";
    public static final String SUBMIT_QUESTIONS 						="submit";
    public static final String VIEW_INDIVIDUAL_SUMMARY 					="view";
}
