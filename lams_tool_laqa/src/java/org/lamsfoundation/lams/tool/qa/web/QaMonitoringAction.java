/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */

 /**
 * @author Ozgur Demirtas
 */

package org.lamsfoundation.lams.tool.qa.web;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaApplicationException;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.QaStringComparator;
import org.lamsfoundation.lams.tool.qa.QaUtils;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;


public class QaMonitoringAction extends LamsDispatchAction implements QaAppConstants
{
	static Logger logger = Logger.getLogger(QaMonitoringAction.class.getName());

	public static String SELECTBOX_SELECTED_TOOL_SESSION ="selectBoxSelectedToolSession";
	public static Integer READABLE_TOOL_SESSION_COUNT = new Integer(1);
 
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
        throws IOException, ServletException, ToolException{
        
        QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
        
        IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
        
        /*
         * persist time zone information to session scope. 
         */
        QaUtils.persistTimeZone(request);
        
        /*
         * mark the http session as an authoring activity 
         */
        request.getSession().setAttribute(TARGET_MODE,TARGET_MODE_MONITORING);
        
        /*
         * obtain and setup the current user's data 
         */
        String userId = "";
        /* get session from shared session. */
        HttpSession ss = SessionManager.getSession();
        /* get back login user DTO */
        UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
        if ((user == null) || (user.getUserID() == null))
        {
            logger.debug("error: The tool expects userId");
            persistError(request,"error.authoringUser.notAvailable");
            request.setAttribute(USER_EXCEPTION_USERID_NOTAVAILABLE, new Boolean(true));
            return (mapping.findForward(LOAD_QUESTIONS));
        }else
            userId = user.getUserID().toString();
        
        logger.debug("TOOL_USER is:" + user);
        
        Long toolContentId = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_CONTENT_ID));
        logger.debug("TOOL_CONTENT_ID: " + toolContentId);
        qaMonitoringForm.setToolContentID(toolContentId);
        
        try
        {
            if ((toolContentId != null) && (toolContentId.longValue() != 0)) 
            {
                if (!QaUtils.existsContent(toolContentId.longValue(), qaService))
                {
                    persistError(request,"error.content.doesNotExist");
                    request.setAttribute(USER_EXCEPTION_CONTENT_DOESNOTEXIST, new Boolean(true));
                    logger.debug("forwarding to: " + MONITORING_ERROR);
                    return (mapping.findForward(MONITORING_REPORT));
                }
            }
        }
        catch(NumberFormatException e)
        {
            persistError(request,"error.numberFormatException");
            request.setAttribute(USER_EXCEPTION_NUMBERFORMAT, new Boolean(true));
            logger.debug("forwarding to: " + MONITORING_ERROR);
            return (mapping.findForward(MONITORING_REPORT));
        }
        
        
        /*
         * find out if only content id but no tool sessions has been passed
         * since with the updated tool contract only userId+toolContentId is passed
         * this will always return true 
         */
        
        boolean isOnlyContentIdAvailable = isOnlyContentIdAvailable(request);
        logger.debug("final isOnlyContentIdAvailable: " + isOnlyContentIdAvailable);
        
        request.getSession().setAttribute(NO_TOOL_SESSIONS_AVAILABLE, new Boolean(false));
        if (isOnlyContentIdAvailable == false)
        {
            /*
             * this block of code will normally never run!
             */
            logger.debug("Warning! We are not supposed to reach here!");
        }
        else
        {
            logger.debug("isOnlyContentIdAvailable: " + isOnlyContentIdAvailable);
            logger.debug("no tool sessions passed and they will be populated from toolContentId.");
            qaMonitoringForm.resetUserAction();
            logger.debug("getting qaContent for toolContentId: " + toolContentId);
            QaContent qaContent=qaService.loadQa(toolContentId.longValue());
            logger.debug("retrieved qaContent: " + qaContent);
            List listToolSessionIds=qaService.getToolSessionsForContent(qaContent);
            logger.debug("retrieved listToolSessionIds: " + listToolSessionIds);
            
            Map originalSessionList= new TreeMap(new QaStringComparator());
            Iterator sessionIdsIterator=listToolSessionIds.iterator();
            int sessionIdCounter=1;
            while (sessionIdsIterator.hasNext())
            {
                Long derivedToolSessionId=(Long) sessionIdsIterator.next();
                logger.debug("derivedToolSessionId: " + derivedToolSessionId);
                originalSessionList.put(new Integer(sessionIdCounter).toString(), derivedToolSessionId.toString());
                sessionIdCounter++;
            }
            logger.debug("constructed originalSessionList: " + originalSessionList);
            
            if (originalSessionList.size() == 0)
                request.getSession().setAttribute(NO_TOOL_SESSIONS_AVAILABLE, new Boolean(true));
            else
                request.getSession().setAttribute(ORIGINAL_TOOL_SESSIONS,originalSessionList);          
            
            qaMonitoringForm.setSummary("summary");
        }
        
        String strFromToolContentId="";
        String strToToolContentId="";
            
            /*
             * simulate Monitoring Service bean by calling the interface methods here
             */
            if (qaMonitoringForm.getStartLesson() != null)
            {
                qaMonitoringForm.resetUserAction();
                /*
                 * In deployment, we won't be passing FROM_TOOL_CONTENT_ID, TO_TOOL_CONTENT_ID and TOOL_SESSION_ID from url
                 * the Monitoring Service bean calls:
                 * copyToolContent(Long fromContentId, Long toContentId)  
                 */
                strFromToolContentId=request.getParameter(FROM_TOOL_CONTENT_ID);
                logger.debug("FROM_TOOL_CONTENT_ID: " + strFromToolContentId);
                
                strToToolContentId=request.getParameter(TO_TOOL_CONTENT_ID);
                logger.debug("TO_TOOL_CONTENT_ID: " + strToToolContentId);
                try
                {
                    qaService.copyToolContent(new Long(strFromToolContentId), new Long(strToToolContentId));    
                }
                catch(ToolException e)
                {
                    logger.debug("exception copying content.");
                    throw e;
                }
                logger.debug("test successfull: copyToolContent.");
                
                /*
                 *  calls to these two methods will be made from Monitoring Service bean optionally depending on
                 *  the the tool is setup for DefineLater and (or) RunOffline 
                 */
                
                /*
                 * TESTED to work
                 * qaService.setAsDefineLater(new Long(strToToolContentId));
                   qaService.setAsRunOffline(new Long(strToToolContentId));
                 * 
                 */
            }
            else if (qaMonitoringForm.getDeleteLesson() != null)
            {
                qaMonitoringForm.resetUserAction();
                /*
                 * TESTED to work
                 */
                strToToolContentId=request.getParameter(TO_TOOL_CONTENT_ID);
                logger.debug("TO_TOOL_CONTENT_ID: " + strToToolContentId);
                if (strToToolContentId == null)
                {
                    throw new QaApplicationException("Exception occured: " +
                            "Tool expects a legitimate TO_TOOL_CONTENT_ID from the container. Can't continue!");
                }
                qaService.removeToolContent(new Long(strToToolContentId));
            }
            /*
             *forceComplete is an API call to service bean from monitoring environment with userId as the parameter
             */
            else if (qaMonitoringForm.getForceComplete() != null)
            {
                /*
                 * Parameter: userId
                 */
                qaMonitoringForm.resetUserAction();
                logger.debug("request for forceComplete");
                userId=request.getParameter(MONITOR_USER_ID);
                logger.debug("MONITOR_USER_ID: " + userId);
                qaService.setAsForceComplete(new Long(userId));
                logger.debug("end of setAsForceComplete with userId: " + userId);
            }
            /*
             * summary tab is one of the main tabs in monitoring screen, summary is the default tab
             */
            else if (qaMonitoringForm.getSummary() != null)
            {
                qaMonitoringForm.resetUserAction();
                logger.debug("do generateToolSessionDataMap");
//                QaMonitoringAction  qaMonitoringAction= new QaMonitoringAction();
                return showSummary(mapping, form, request, response);
            }
            else if (qaMonitoringForm.getInstructions() != null)
            {
                qaMonitoringForm.resetUserAction();
                logger.debug("request for instructions");
            }
            else if (qaMonitoringForm.getEditActivity() != null)
            {
                qaMonitoringForm.resetUserAction();
                logger.debug("request for editActivity");
            }
            else if (qaMonitoringForm.getStats() != null)
            {
                qaMonitoringForm.resetUserAction();
                logger.debug("request for stats");
            }
        return null;
    }
    
    public ActionForward showStats(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {        
        QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
        Boolean noToolSessionsAvailable=(Boolean)request.getSession().getAttribute(NO_TOOL_SESSIONS_AVAILABLE);
        if ((noToolSessionsAvailable !=null) && (noToolSessionsAvailable.booleanValue()))
        {
            qaMonitoringForm.resetUserAction();
            logger.debug("detected noToolSessionsAvailable:" + noToolSessionsAvailable);
            persistError(request,"error.content.noToolSessions");
            request.setAttribute(USER_EXCEPTION_ONLYCONTENT_ANDNOSESSIONS, new Boolean(true));
            logger.debug("forwarding to: " + MONITORING_REPORT);
            return (mapping.findForward(MONITORING_REPORT));    
        }
        
        IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
        logger.debug("retrieving qaService: " + qaService);
        
        Map mapStats= new TreeMap(new QaStringComparator());
        request.getSession().setAttribute(MAP_STATS,mapStats);
        
        Map sessionList=(Map)request.getSession().getAttribute(SUMMARY_TOOL_SESSIONS);
        for (int toolSessionIdCounter=1; toolSessionIdCounter < READABLE_TOOL_SESSION_COUNT.intValue(); toolSessionIdCounter++)
        {
            String strToolSessionId=(String)sessionList.get("Group" + toolSessionIdCounter);
            logger.debug("strToolSessionId from http session: " + strToolSessionId);
            if ((strToolSessionId != null) && (strToolSessionId.length() > 0))
            {
                QaSession qaSession=qaService.retrieveQaSessionOrNullById(new Long(strToolSessionId).longValue());
                logger.debug("retrieving qaSession: " + qaSession);
                if (qaSession != null)
                {
                    logger.debug("retrieving qaSession: " + qaSession);
                    int countSessionUser=qaService.countSessionUser(qaSession);
                    logger.debug("countSessionUser: " + countSessionUser);
                    mapStats.put(strToolSessionId, new Integer(countSessionUser).toString());
                    request.getSession().setAttribute(MAP_STATS,mapStats);
                }
                
            }
        }
        mapStats=(Map)request.getSession().getAttribute(MAP_STATS);
        logger.debug("final MAP_STATS: " + mapStats);
        qaMonitoringForm.resetUserAction();
        
        request.getSession().setAttribute(CHOICE_MONITORING, CHOICE_TYPE_MONITORING_STATS);
        return (mapping.findForward(MONITORING_REPORT));
    }
    
    public ActionForward showEditActivity(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {       
        QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
        IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
        logger.debug("retrieving qaService: " + qaService);

        Long toolContentId = qaMonitoringForm.getToolContentID();
        logger.debug("toolContentId: " + toolContentId);        
        
        request.getSession().setAttribute(CHOICE_MONITORING, CHOICE_TYPE_MONITORING_EDITACTIVITY);
        request.getSession().setAttribute(CONTENT_LOCKED, new Boolean(false));
        
        if (toolContentId == null)
        {
            /*
            *  toolContentId is not available from the toolSessions passed to the monitoring url.
            *  in this case,  toolContentId must have been passed separetely 
            */
            Long monitoredContentId=(Long)request.getSession().getAttribute(MONITORED_CONTENT_ID);
            logger.debug("will generateEditActivityScreen: " + monitoredContentId);
                
                if (monitoredContentId == null)
                {
                    throw new QaApplicationException("Exception occured: " +
                            "Tool expects a legitimate TOOL_CONTENT_ID from the container since it is also not available from the toolSession(s) passed. Can't continue!");
                }
                toolContentId=monitoredContentId;
        }
        request.getSession().setAttribute(MONITORED_CONTENT_ID, toolContentId);
        
        logger.debug("will use monitoredContentId: " + toolContentId);
        QaContent qaContent=qaService.loadQa(toolContentId.longValue());
        logger.debug("will use qaContent: " + qaContent);
        
        request.getSession().setAttribute(IS_MONITORING_DEFINE_LATER, new Boolean(qaContent.isDefineLater()));
        logger.debug("IS_MONITORING_DEFINE_LATER: " + request.getSession().getAttribute(IS_MONITORING_DEFINE_LATER));
        
        logger.debug("calling studentActivityOccurredGlobal with: " + qaContent);
        boolean studentActivity=qaService.studentActivityOccurredGlobal(qaContent);
        logger.debug("studentActivity on content: " + studentActivity);
        
        qaMonitoringForm.resetUserAction();
        if (studentActivity == false)
        {
            /*
             * forward to Authoring Basic tab
             */
            QaStarterAction qaStarterAction = new QaStarterAction();
            QaAuthoringForm qaAuthoringForm = new QaAuthoringForm();
            logger.debug("forward to Authoring Basic tab ");
            ActionForward actionForward=qaStarterAction.startMonitoringSummary(mapping, qaAuthoringForm, request, response);
            logger.debug("actionForward: " + actionForward);
            return (actionForward);
        }
        else
        {
            logger.debug("forward to warning screen as the content is not allowed to be modified.");
            ActionMessages errors= new ActionMessages();
            errors.add(Globals.ERROR_KEY, new ActionMessage("error.content.inUse"));
            saveErrors(request,errors);
            request.getSession().setAttribute(CONTENT_LOCKED, new Boolean(true));
            request.setAttribute(START_MONITORING_SUMMARY_REQUEST, new Boolean(true));
            logger.debug("forwarding to:" + LOAD);
            return (mapping.findForward(LOAD));
        }
    }
    
    public ActionForward showInstructions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {        
        logger.debug(logger + " " + this.getClass().getName() +  "will generateInstructionsScreen.");
        QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
        
//        Long initialMonitoringContentId=(Long)request.getSession().getAttribute(INITIAL_MONITORING_TOOL_CONTENT_ID);
//        if (initialMonitoringContentId == null) 
//        {
//            logger.debug("missing content id:");
//            persistError(request,"error.tab.contentId.required");
//            request.setAttribute(USER_EXCEPTION_MONITORINGTAB_CONTENTID_REQUIRED, new Boolean(true));
//            logger.debug("forwarding to: " + MONITORING_REPORT);
//            return (mapping.findForward(MONITORING_REPORT));        
//        }
        
        qaMonitoringForm.resetUserAction();
        
        request.getSession().setAttribute(CHOICE_MONITORING, CHOICE_TYPE_MONITORING_INSTRUCTIONS);
        return (mapping.findForward(MONITORING_REPORT));
    }
    
    public ActionForward showSummary(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {
        QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
        IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
        
        
        MonitoringUtil monitoringUtil = new MonitoringUtil();
        qaMonitoringForm.setUpdatedResponse("");
        logger.debug("request for summary");
        
        String isToolSessionChanged=request.getParameter(IS_TOOL_SESSION_CHANGED);
        logger.debug("IS_TOOL_SESSION_CHANGED - initial: " + isToolSessionChanged);
        
        /*
         * sessionList holds all the toolSessionIds passed to summary page to be presented in a drop-down box.
         */
        Map sessionList = new TreeMap();
        int SELECTION_CASE=0;
        
        logger.debug("isNonDefaultScreensVisited: " + monitoringUtil.isNonDefaultScreensVisited(request));
        
        /*
         * Following conditions test which entry has been selected in the monitoring mode-summary screen drop box.
         * is Default page 
         */
        String selectionCase=(String)request.getSession().getAttribute("selectionCase");
        logger.debug("current selectionCase: " + selectionCase);
        
        boolean sessionListReadable=false;
        if ((isToolSessionChanged == null) && !monitoringUtil.isNonDefaultScreensVisited(request))
        {
            logger.debug("First case based on null. Gets rendered only once in http session life time");
            logger.debug("summary to use MAX_TOOL_SESSION_COUNT");
            /*
             * initialize sessionList with "All", which is the default option.
             */
            sessionList.put("All", "All");
            READABLE_TOOL_SESSION_COUNT=MAX_TOOL_SESSION_COUNT;
            SELECTION_CASE=1;
        }
        else if ((isToolSessionChanged == null) && monitoringUtil.isNonDefaultScreensVisited(request))
        {
            logger.debug("Other tabs visited. Gets rendered for all tool sessions");
            sessionList.put("All", "All");
            READABLE_TOOL_SESSION_COUNT=MAX_TOOL_SESSION_COUNT;
            SELECTION_CASE=2;
            sessionListReadable=true;
        }
        else if ((isToolSessionChanged != null) && isToolSessionChanged.equalsIgnoreCase("1"))
        {
            String selectedToolSessionId=(String)request.getParameter("toolSessionId1");
            logger.debug("selectedToolSessionId" + selectedToolSessionId);
            /*
             * is "All" selected 
             */
            if (selectedToolSessionId.equalsIgnoreCase("All"))
            {
                logger.debug("Second case");
                logger.debug("summary to use MAX_TOOL_SESSION_COUNT");
                /*
                 * initialize sessionList with "All", which is the default option.
                 */
                sessionList.put("All", "All");
                READABLE_TOOL_SESSION_COUNT=MAX_TOOL_SESSION_COUNT;
                SELECTION_CASE=2;
                sessionListReadable=true;
            }
            else
            {
                /*
                 * is a single session id selected 
                 */
                logger.debug("Third case");
                logger.debug("selectedToolSessionId" + selectedToolSessionId);
                READABLE_TOOL_SESSION_COUNT=new Integer(2);
                SELECTION_CASE=3;
                request.getSession().setAttribute(CURRENT_MONITORED_TOOL_SESSION,selectedToolSessionId); 
                logger.debug("CURRENT_MONITORED_TOOL_SESSION " + selectedToolSessionId);
            }
        }
        else if (isToolSessionChanged.equals("")  && (selectionCase.equals("3"))) 
        {
            /*
             * is a single session id selected 
             */
            logger.debug("case with single session and edit or update selected");
            READABLE_TOOL_SESSION_COUNT=new Integer(2);
            SELECTION_CASE=3;
        }
        else if (isToolSessionChanged.equals("")) 
        {
            logger.debug("All is selected");
            sessionList.put("All", "All");
            READABLE_TOOL_SESSION_COUNT=MAX_TOOL_SESSION_COUNT;
            SELECTION_CASE=2;
            sessionListReadable=true;
        }
        logger.debug("final SELECTION_CASE: " + SELECTION_CASE);
        
        boolean useSelectedToolSessionId=false;
        String selectedToolSessionId = (String) request.getSession().getAttribute(CURRENT_MONITORED_TOOL_SESSION);
        if ((selectedToolSessionId != null) && (SELECTION_CASE == 3) && (READABLE_TOOL_SESSION_COUNT.intValue()  == 2))
        {
            useSelectedToolSessionId=true;
        }
        
        if ((qaMonitoringForm.getEditReport() == null) && (qaMonitoringForm.getUpdateReport() == null))
        {
            logger.debug("no editReport or updateReport selected");
            request.getSession().setAttribute(DATAMAP_EDITABLE_RESPONSE_ID, "");
            request.getSession().setAttribute("selectionCase",new Long(SELECTION_CASE).toString());
        }
        qaMonitoringForm.resetUserAction();
        
        /*
         * holds all the toolSessionIds passed in the form toolSessionId1, toolSessionId2 etc.
         */
        Map mapToolSessions= new TreeMap(new QaStringComparator());
        request.getSession().setAttribute(MAP_TOOL_SESSIONS,mapToolSessions);
        logger.debug("MAP_TOOL_SESSIONS placed into session");
        
        Map mapUserResponses= new TreeMap(new QaStringComparator());
        request.getSession().setAttribute(MAP_USER_RESPONSES,mapUserResponses);
        logger.debug("MAP_USER_RESPONSES placed into session");
        logger.debug("request for contributeLesson");
        /*
         * monitoring reads all the toolSessionsIds appended one after other until it finds a null one. The cap was limited to 500.
         * This is the case all the tool sessions are using the same content.
         * 
         * The final Map mapToolSessions holds the Map mapQuestions which in turn holds the Map mapUserResponses.
         * The key of  mapToolSessions:  incremental numbers
         * The key of  mapQuestions:     questions themselves
         * The key of  mapUserResponses: incremental numbers
         * 
         */
            
        request.getSession().setAttribute(NO_AVAILABLE_SESSIONS,new Boolean(false));
        logger.debug("NO_AVAILABLE_SESSIONS: " + false);
        
        logger.debug("retrieving ORIGINAL_TOOL_SESSIONS");
        Map originalSessionList=(Map)request.getSession().getAttribute(ORIGINAL_TOOL_SESSIONS);
        logger.debug("retrieved ORIGINAL_TOOL_SESSIONS : " + originalSessionList);
        /*
         *  monitoredToolSessionsCounter holds the total number of valid toolSessionIds passed to the monitoring url
         */
        logger.debug("READABLE_TOOL_SESSION_COUNT: " + READABLE_TOOL_SESSION_COUNT);
        int monitoredToolSessionsCounter=0;
        for (int toolSessionIdCounter=1; toolSessionIdCounter < READABLE_TOOL_SESSION_COUNT.intValue(); toolSessionIdCounter++)
        {
            logger.debug("toolSessionIdCounter: " + toolSessionIdCounter);
            String strToolSessionId="";
            if (useSelectedToolSessionId)
            {
                strToolSessionId=(String) request.getSession().getAttribute(CURRENT_MONITORED_TOOL_SESSION);
                logger.debug("using strToolSessionId: " +strToolSessionId);
            }
            else
            {
                strToolSessionId=(String) originalSessionList.get(""+toolSessionIdCounter);
                logger.debug("using strToolSessionId from the session: ");
            }
            logger.debug("original strToolSessionId: " + strToolSessionId);
                        
            String strRetrievableToolSessionId="";
            /*
             * catering for un-formatted monitoring url
             * Watch for case where the "All" is selected in the drop-down.
             */
            logger.debug("SELECTION_CASE: " + SELECTION_CASE);
            if ((strToolSessionId == null) && (SELECTION_CASE == 1))
            {
                logger.debug("un-formatted monitoring url, exiting...");
                break;
            }
            else if  ((!sessionListReadable) &&  ((strToolSessionId == null) || (strToolSessionId.length() == 0)))
            {
                logger.debug("un-formatted monitoring url, exiting...");
                break;
            }
            else
            {
                if (sessionListReadable)
                {
                    logger.debug("sessionListReadable is true.");
                    logger.debug("strToolSessionId is All: " + strToolSessionId);
                    sessionList=(Map)request.getSession().getAttribute(SUMMARY_TOOL_SESSIONS);
                    logger.debug("toolSessionIdCounter: " + toolSessionIdCounter);
                    logger.debug(logger + " " + this.getClass().getName() +  "sessionList size: " + sessionList.size());
                    
                    if (toolSessionIdCounter==sessionList.size())
                    {
                        logger.debug("sessionList size equals toolSessionIdCounter, exiting...");
                        break;
                    }
                        
                    logger.debug("sessionList: " + sessionList);
                    strToolSessionId=(String)sessionList.get("Group" + toolSessionIdCounter);
                    logger.debug("strToolSessionId from sessionList: " + strToolSessionId);
                }
                
                strRetrievableToolSessionId=strToolSessionId;
                logger.debug("retrievableStrToolSessionId: " + strRetrievableToolSessionId);
                
                QaSession qaSession=qaService.retrieveQaSessionOrNullById(new Long(strRetrievableToolSessionId).longValue());
                logger.debug("retrieving qaSession: " + qaSession);
                
                if (qaSession !=null)
                {
                    monitoredToolSessionsCounter++;
                    request.getSession().setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, new Long(strToolSessionId));
                    logger.debug("TOOL_SESSION_ID in session");
                    
                    if (READABLE_TOOL_SESSION_COUNT.equals(MAX_TOOL_SESSION_COUNT))
                    {
                        logger.debug("default screen - READABLE_TOOL_SESSION_COUNT equals MAX_TOOL_SESSION_COUNT");
                        /*
                         * add the current toolSessionId to the arraylist for the drop-down box
                         */
                        sessionList.put("Group" + monitoredToolSessionsCounter, strToolSessionId);
                        logger.debug("sessionList Map new entry, strToolSessionId added to the list: " + toolSessionIdCounter + "->" + strToolSessionId );  
                    }
                    
                    /*
                     * get to content from the tool session
                     */
                    QaContent qaContent=qaSession.getQaContent();
                    logger.debug("using qaContent: " + qaContent);
                    logger.debug("Monitor - contribute will be using TOOL_CONTENT_ID: " + qaContent.getQaContentId());
                    
                    /*
                     * editActivity-defineLater screen depends on MONITORED_CONTENT_ID
                     */
                    request.getSession().setAttribute(MONITORED_CONTENT_ID,qaContent.getQaContentId());
                    
                    /*
                     * place it into TOOL_CONTENT_ID session attribute since learningUtil.buidLearnerReport(request) depends on it
                     * to generate a report
                     */
                    request.getSession().setAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID,qaContent.getQaContentId());
                    
                    /*
                     * this is to convince jsp although usernameVisible applies only to learning mode
                     */
                    request.getSession().setAttribute(IS_USERNAME_VISIBLE, new Boolean(true));
                    
                    logger.debug("REPORT_TITLE_MONITOR: " + qaContent.getMonitoringReportTitle());
                    request.getSession().setAttribute(REPORT_TITLE_MONITOR,qaContent.getMonitoringReportTitle());
                    
                    
                    /*
                     * check if the author requires that the all tool sessions must be COMPLETED before the report in Monitoring
                     */
                    boolean isAllSessionsCompleted=monitoringUtil.isSessionsSync(qaContent);
                    logger.debug("Monitor - contribute will be using isAllSessionsCompleted: " + isAllSessionsCompleted);
                    logger.debug("Monitor - contribute will be using isSynchInMonitor: " + qaContent.isSynchInMonitor());
                    /*
                     * if the author requires syncronization but not all the sessions are COMPLETED give an error
                     */
                    if (qaContent.isSynchInMonitor() && (!isAllSessionsCompleted))
                    {
                        request.getSession().setAttribute(CHECK_ALL_SESSIONS_COMPLETED, new Boolean(true));
                        request.getSession().setAttribute(IS_ALL_SESSIONS_COMPLETED, new Boolean(isAllSessionsCompleted));
    
                        ActionMessages errors= new ActionMessages();
                        errors.add(Globals.ERROR_KEY, new ActionMessage("error.synchInMonitor"));
                        logger.debug("add synchInMonitor to ActionMessages");
                        saveErrors(request,errors);                 
                    }
                    else
                    {
                        request.getSession().setAttribute(CHECK_ALL_SESSIONS_COMPLETED, new Boolean(false));
                    }
                    logger.debug("IS_ALL_SESSIONS_COMPLETED:" + request.getSession().getAttribute(IS_ALL_SESSIONS_COMPLETED));
                    logger.debug("CHECK_ALL_SESSIONS_COMPLETED" + request.getSession().getAttribute(CHECK_ALL_SESSIONS_COMPLETED));
                            
                    LearningUtil learningUtil= new LearningUtil();
                    /*
                     * generate a report for the Author/Teacher
                     */
                    logger.debug("calling buidMonitoringReport with toolSessionIdCounter:" + toolSessionIdCounter);
                    learningUtil.buidLearnerReport(request, toolSessionIdCounter, qaService);
                }
            }
        }
        
        /*
         * store the arrayList in the session
         */
        if (READABLE_TOOL_SESSION_COUNT.equals(MAX_TOOL_SESSION_COUNT))
        {
            logger.debug("sessionList storable");
            request.getSession().setAttribute(SUMMARY_TOOL_SESSIONS,sessionList);
            logger.debug("SUMMARY_TOOL_SESSIONS stored into the session:" + request.getSession().getAttribute(SUMMARY_TOOL_SESSIONS));
        }
        
        mapToolSessions=(Map)request.getSession().getAttribute(MAP_TOOL_SESSIONS);
        logger.debug("before forwarding MAP_TOOL_SESSIONS:" + mapToolSessions);
        
        if (mapToolSessions.size() == 0)
        {
            request.getSession().setAttribute(NO_AVAILABLE_SESSIONS,new Boolean(true));
            logger.debug("NO_AVAILABLE_SESSIONS: " +true);
            ActionMessages errors= new ActionMessages();
            errors.add(Globals.ERROR_KEY, new ActionMessage("error.noStudentActivity"));
            logger.debug("add error.noStudentActivity to ActionMessages");
            saveErrors(request,errors);
        }
        
        Boolean noAvailableSessions=(Boolean) request.getSession().getAttribute(NO_AVAILABLE_SESSIONS);
        logger.debug("before forwarding NO_AVAILABLE_SESSIONS:" + noAvailableSessions);
        
        Map mapMonitoringQuestions=(Map)request.getSession().getAttribute(MAP_MONITORING_QUESTIONS);
        logger.debug("before forwarding MAP_MONITORING_QUESTIONS:" + mapMonitoringQuestions);
        
        String targetMode=(String )request.getSession().getAttribute(TARGET_MODE);
        logger.debug("TARGET_MODE: " + targetMode);
        request.getSession().setAttribute(CHOICE_MONITORING, CHOICE_TYPE_MONITORING_SUMMARY);
        return (mapping.findForward(MONITORING_REPORT));
    }
    
    
    public ActionForward editResponse(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {
        QaMonitoringForm qaMonitoringForm = (QaMonitoringForm)form;
        String responseId=qaMonitoringForm.getResponseId();
        request.getSession().setAttribute(DATAMAP_EDITABLE_RESPONSE_ID, responseId);
        
        return showSummary(mapping, form, request, response);
    }
    
    
    public ActionForward updateReport(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {
        QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
        IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
        MonitoringUtil monitoringUtil = new MonitoringUtil();
        String responseId=qaMonitoringForm.getResponseId();
        
        responseId=(String)request.getSession().getAttribute(DATAMAP_EDITABLE_RESPONSE_ID);
        String updatedResponse=qaMonitoringForm.getUpdatedResponse();
        monitoringUtil.updateResponse(qaService, responseId, updatedResponse);
        
        request.getSession().setAttribute(DATAMAP_EDITABLE_RESPONSE_ID, "");
        return showSummary(mapping, form, request, response);
    }
    
    public ActionForward selectToolSession(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {        
        return showSummary(mapping, form, request, response);
    }
    
    public ActionForward hideResponse(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {        
        QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
        IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
        String hiddenResponseId=qaMonitoringForm.getHiddenResponseId();
        MonitoringUtil monitoringUtil = new MonitoringUtil();
     
        request.getSession().setAttribute(DATAMAP_HIDDEN_RESPONSE_ID, hiddenResponseId);
        monitoringUtil.hideResponse(qaService, hiddenResponseId);
        
        return showSummary(mapping, form, request, response);
    }
    
    public ActionForward unhideResponse(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {
        QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
        IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
        String unHiddenResponseId=qaMonitoringForm.getUnHiddenResponseId();
        MonitoringUtil monitoringUtil = new MonitoringUtil();
        
        request.getSession().setAttribute(DATAMAP_HIDDEN_RESPONSE_ID, "");
        monitoringUtil.unHideResponse(qaService, unHiddenResponseId);
        
        return showSummary(mapping, form, request, response);
    }

	
	/**
     * persists error messages to request scope
     * persistError(HttpServletRequest request, String message)
     * @param request
     * @param message
     */
	public void persistError(HttpServletRequest request, String message)
	{
		ActionMessages errors= new ActionMessages();
		errors.add(Globals.ERROR_KEY, new ActionMessage(message));
		logger.debug("add " + message +"  to ActionMessages:");
		saveErrors(request,errors);	    	    
	}
    
    
    /**
     * boolean isOnlyContentIdAvailable(HttpServletRequest request)
     * @param request
     * @return boolean
     */
    public boolean isOnlyContentIdAvailable(HttpServletRequest request)
    {
        boolean existsContentId=false;
        String strToolContentId=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
        if ((strToolContentId != null) && (strToolContentId.length() > 0))
            existsContentId=true;
        
        boolean existsToolSession=false;
        for (int toolSessionIdCounter=1; toolSessionIdCounter < MAX_TOOL_SESSION_COUNT.intValue(); toolSessionIdCounter++)
        {
            String strToolSessionId=request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID + toolSessionIdCounter);
            if ((strToolSessionId != null) && (strToolSessionId.length() > 0))
            {
                existsToolSession=true;
            }
        }
        
        if (existsContentId && (!existsToolSession))
        {
            logger.debug("OnlyContentIdAvailable");
            return true;
        }
        else
        {
            logger.debug("Not OnlyContentIdAvailable");
            return false;
        }
    }

}