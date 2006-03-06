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
package org.lamsfoundation.lams.tool.qa;

/**
 * @author Ozgur Demirtas
 */
public interface QaAppConstants {

	public static final String MY_SIGNATURE 							="laqa11";
	public static final long DEFAULT_CONTENT_ID 						=0;
    public static final long DEFAULT_QUE_CONTENT_ID 					=1;
	public static final String TOOL_SERVICE								="tool_service";
	public static final String ERROR_MCAPPLICATION 						= "error.exception.QaApplication";
	public static final String TOOL_CONTENT_ID 							= "toolContentID";

	public static final String TARGET_MODE								="targetMode";
	public static final String TARGET_MODE_AUTHORING					="Authoring";
	public static final String TARGET_MODE_LEARNING						="Learning";
	public static final String TARGET_MODE_MONITORING					="Monitoring";
	public static final String TARGET_MODE_EXPORT_PORTFOLIO				="ExportPortfolio";
	
	public static final String AUTHORING_STARTER						="starter";
	public static final String LOAD_LEARNER								="loadLearner";
	public static final String LEARNING_STARTER							="learningStarter";
	public static final String MONITORING_STARTER						="monitoringStarter";
	public static final String LOAD_MONITORING							="loadMonitoring";
	public static final String LOAD_VIEW_ONLY						="loadViewOnly";
	public static final String EDIT_RESPONSE							="editResponse";
	public static final String EDITABLE_RESPONSE_ID						="editableResponseId";
	public static final String COPY_TOOL_CONTENT 						="copyToolContent";
	public static final String ERROR_LIST								="errorList";
	public static final String ERROR_LIST_LEARNER						="errorListLearner";
	public static final String DEFAULT_CONTENT_ID_STR					="defaultContentIdStr";
	public static final String TOOL_SESSION_ID 							= "toolSessionID";
	
	public static final String ACTIVE_MODULE							="activeModule";
	public static final String AUTHORING								="authoring";
	public static final String MONITORING								="monitoring";
	public static final String DEFINE_LATER_IN_EDIT_MODE				="defineLaterInEditMode";
	public static final String SHOW_AUTHORING_TABS						="showAuthoringTabs";
	public static final String MONITORING_ORIGINATED_DEFINELATER		="monitoringOriginatedDefineLater";
	public static final String DEFINE_LATER								="defineLater";
	public static final String REQUESTED_MODULE							="requestedModule";
	
	public static final String SOURCE_MC_STARTER						="sourceMcStarter";	
	
	public static final String IS_MONITORED_CONTENT_IN_USE				="isMonitoredContentInUse";
	public static final String LOAD_MONITORING_CONTENT_EDITACTIVITY		="loadMonitoringEditActivity";
			
	/*
     * refers to number of questions presented initially, we have a single record for default content
     */
    public static final Long INITIAL_QUESTION_COUNT		=new Long(1);
    
    /*
     * Struts level constants
     */
    public static final String LOAD										="load";
    public static final String LOAD_QUESTIONS							="load";
    public static final String LOAD_STARTER								="starter";
    public static final String IS_DEFINE_LATER							="isDefineLater";
    public static final String LEARNING_MODE							="learningMode";
    
    /*
     * authoring mode controllers
     */ 
    public static final String IS_ADD_QUESTION							="isAddQuestion";
    public static final String IS_REMOVE_QUESTION						="isRemoveQuestion";
    public static final String IS_REMOVE_CONTENT						="isRemoveContent";
    public static final String SELECTION_CASE							="selectionCase";
    
    /*
     * authoring mode constants
     */
    public static final String MAP_QUESTION_CONTENT						="mapQuestionContent";
    public static final String DEFAULT_QUESTION_CONTENT					="defaultQuestionContent";
    public static final String ONLINE_INSTRUCTIONS						="onlineInstructions";
    public static final String OFFLINE_INSTRUCTIONS						="offlineInstructions";
    public static final String END_LEARNING_MESSSAGE					="endLearningMessage";
    public static final String ON										="ON";
    public static final String OFF										="OFF";
    public static final String RICHTEXT_OFFLINEINSTRUCTIONS				="richTextOfflineInstructions";
    public static final String RICHTEXT_ONLINEINSTRUCTIONS				="richTextOnlineInstructions";
    public static final String RICHTEXT_TITLE							="richTextTitle";
    public static final String RICHTEXT_INSTRUCTIONS					="richTextInstructions";
    public static final String RICHTEXT_BLANK							="<p>&nbsp;</p>";

    public static final String SUBMIT_OFFLINE_FILE						="submitOfflineFile";
    public static final String SUBMIT_ONLINE_FILE						="submitOnlineFile";
    public static final String POPULATED_UPLOADED_FILESDATA				="populateUploadedFilesData";
    public static final String USER_ID 									= "userID";
    
    public final long 	ONE_DAY 										=60 * 60 * 1000 * 24;
    public final static String NOT_ATTEMPTED 							="NOT_ATTEMPTED";
    public final static String INCOMPLETE 								="INCOMPLETE";
    public static final String COMPLETED 								="COMPLETED";
    
    public static final String MAP_TOOL_SESSIONS						="mapToolSessions";
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
    public static final String USER_EXCEPTION_USERID_NOTAVAILABLE		="userExceptionUserIdNotAvailable";
    public static final String USER_EXCEPTION_USERID_NOTNUMERIC			="userExceptionUserIdNotNumeric";
    public static final String USER_EXCEPTION_ONLYCONTENT_ANDNOSESSIONS ="userExceptionOnlyContentAndNoSessions";
    public static final String USER_EXCEPTION_USERID_EXISTING			="userExceptionUserIdExisting";
    public static final String USER_EXCEPTION_USER_DOESNOTEXIST			="userExceptionUserDoesNotExist";
    public static final String USER_EXCEPTION_MONITORINGTAB_CONTENTID_REQUIRED	="userExceptionMonitoringTabContentIdRequired";
    public static final String USER_EXCEPTION_DEFAULTCONTENT_NOTSETUP	="userExceptionDefaultContentNotSetup";
    public static final String USER_EXCEPTION_NO_TOOL_SESSIONS			="userExceptionNoToolSessions";
    public static final String USER_EXCEPTION_NO_STUDENT_ACTIVITY		="userExceptionNoStudentActivity";
    public static final String USER_EXCEPTION_CONTENT_IN_USE			="userExceptionContentInUse";
    public static final String USER_EXCEPTION_CONTENT_DEFINE_LATER		="userExceptionContentDefineLater";
    public static final String USER_EXCEPTION_RUN_OFFLINE				="userExceptionRunOffline";    
    public static final String USER_EXCEPTION_MODE_REQUIRED				="userExceptionModeRequired";    
    public static final String USER_EXCEPTION_MODE_INVALID				="userExceptionModeInvalid";    
    public static final String COUNT_SESSION_COMPLETE					="countSessionComplete";
    public static final String CURRENT_MONITORED_TOOL_SESSION 			="currentMonitoredToolSession";
    
    
    public static final String COUNT_ALL_USERS							="countAllUsers";
    public static final String CURRENT_MONITORING_TAB					="currentMonitoringTab";
    public static final String LIST_MONITORED_ANSWERS_CONTAINER_DTO		="listMonitoredAnswersContainerDto";
    public static final String SUMMARY_TOOL_SESSIONS					="summaryToolSessions";
    public static final String SUMMARY_TOOL_SESSIONS_ID					="summaryToolSessionsId";
    
    public static final String ACTIVITY_TITLE							="activityTitle";
    public static final String ACTIVITY_INSTRUCTIONS					="activityInstructions";
    public static final String IS_USERNAME_VISIBLE						="isUsernameVisible";
    public static final String CURRENT_ANSWER							="currentAnswer";

        
    /*
     * export portfolio constants
     */
    public static final String MODE										="mode";  
    public static final String LEARNER									="learner"; 
    public static final String TEACHER									="teacher";
    					
    public static final String PORTFOLIO_REPORT							="portfolioReport";
    public static final String PORTFOLIO_REQUEST						="portfolioRequest";
    public static final int    MAX_GROUPS_COUNT							=250;						
        
    /*
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
    public static final String USER_FEEDBACK							="userFeedback";
    public static final String REPORT_TITLE								="reportTitle"; 
    public static final String MONITORING_REPORT_TITLE					="monitoringReportTitle"; 
    public static final String REPORT_TITLE_LEARNER						="reportTitleLearner"; 
    public static final String END_LEARNING_MESSAGE						="endLearningMessage";
    public static final String IS_TOOL_ACTIVITY_OFFLINE					="isToolActivityOffline";
    public static final String CHECK_ALL_SESSIONS_COMPLETED				="checkAllSessionsCompleted";
    public static final String FROM_TOOL_CONTENT_ID						="fromToolContentId";
    public static final String TO_TOOL_CONTENT_ID						="toToolContentId";
    public static final String LEARNER_REPORT							="learnerReport";
    
            
    /*
     * Monitoring Mode constants
     */
    public static final String EDITACTIVITY_EDITMODE	   		   		="editActivityEditMode";
    public static final String RENDER_MONITORING_EDITACTIVITY	   		="renderMonitoringEditActivity";
    public static final String NO_AVAILABLE_SESSIONS	   		   		="noAvailableSessions";
    public static final String NO_TOOL_SESSIONS_AVAILABLE				="noToolSessionAvailable";
    
    public static final String TIMEZONE									="timeZone";
    public static final String TIMEZONE_ID								="timeZoneId";
    /*
     * Monitor and Learning common constants - used in jsp reporting
     */
    
    public static final String QUESTION_LISTING_MODE					="questionListingMode";
    public static final String QUESTION_LISTING_MODE_SEQUENTIAL			="questionListingModeSequential";
    public static final String QUESTION_LISTING_MODE_COMBINED			="questionListingModeCombined";
    
    public static final String FEEDBACK_TYPE_SEQUENTIAL					="You will be presented a total of : ";
    public static final String FEEDBACK_TYPE_COMBINED					="You are being presented a total of : ";
    public static final String QUESTIONS								=" questions.";

    public static final String ATTACHMENT_LIST                          ="attachmentList";
    public static final String SUBMIT_SUCCESS							="sbmtSuccess";
    public static final String DELETED_ATTACHMENT_LIST                  ="deletedAttachmentList";
    public static final String UUID                                     ="uuid";
}
