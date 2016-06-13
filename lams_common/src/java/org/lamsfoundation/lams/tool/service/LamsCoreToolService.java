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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.service;

import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ActivityEvaluation;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.tool.SystemTool;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolAdapterContentManager;
import org.lamsfoundation.lams.tool.ToolContent;
import org.lamsfoundation.lams.tool.ToolContentIDGenerator;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.dao.ISystemToolDAO;
import org.lamsfoundation.lams.tool.dao.IToolContentDAO;
import org.lamsfoundation.lams.tool.dao.IToolSessionDAO;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.tool.exception.RequiredGroupMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.dao.DataIntegrityViolationException;

/**
 *
 * @author Jacky Fang
 * @version 1.1
 * @since 2005-2-23
 */
public class LamsCoreToolService implements ILamsCoreToolService, ApplicationContextAware {
    private static final Logger log = Logger.getLogger(LamsCoreToolService.class);

    // ---------------------------------------------------------------------
    // Instance variables
    // ---------------------------------------------------------------------
    private ApplicationContext context;
    private IToolSessionDAO toolSessionDAO;
    private ISystemToolDAO systemToolDAO;
    private ToolContentIDGenerator contentIDGenerator;
    protected IToolContentDAO toolContentDAO;
    private MessageService messageService;

    // ---------------------------------------------------------------------
    // Inversion of Control Methods - Method injection
    // ---------------------------------------------------------------------
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
	this.context = context;
    }

    /**
     * @param toolSessionDAO
     *            The toolSessionDAO to set.
     */
    public void setToolSessionDAO(IToolSessionDAO toolSessionDAO) {
	this.toolSessionDAO = toolSessionDAO;
    }

    public ISystemToolDAO getSystemToolDAO() {
	return systemToolDAO;
    }

    public void setSystemToolDAO(ISystemToolDAO systemToolDAO) {
	this.systemToolDAO = systemToolDAO;
    }

    /**
     * @param contentIDGenerator
     *            The contentIDGenerator to set.
     */
    public void setContentIDGenerator(ToolContentIDGenerator contentIDGenerator) {
	this.contentIDGenerator = contentIDGenerator;
    }

    public void setToolContentDAO(IToolContentDAO toolContentDAO) {
	this.toolContentDAO = toolContentDAO;
    }

    /**
     * Set i18n MessageService
     */
    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    // ---------------------------------------------------------------------
    // Service Methods
    // ---------------------------------------------------------------------

    @Override
    public synchronized ToolSession createToolSession(User learner, ToolActivity activity, Lesson lesson)
	    throws RequiredGroupMissingException, DataIntegrityViolationException {
	// look for an existing applicable tool session
	// could be either a grouped (class group or standard group) or an individual.
	// more likely to be grouped (more tools work that way!)
	ToolSession toolSession = toolSessionDAO.getToolSessionByLearner(learner, activity);

	// if haven't found an existing tool session then create one
	if (toolSession == null) {

	    if (LamsCoreToolService.log.isDebugEnabled()) {
		LamsCoreToolService.log.debug("Creating tool session for [" + activity.getActivityId() + ","
			+ activity.getTitle() + "] for learner [" + learner.getLogin() + "] lesson ["
			+ lesson.getLessonId() + "," + lesson.getLessonName() + "].");
	    }

	    toolSession = activity.createToolSessionForActivity(messageService, learner, lesson);
	    toolSessionDAO.saveToolSession(toolSession);

	    return toolSession;
	}

	// indicate that we found an existing tool session by returning null
	return null;
    }

    @Override
    public ToolSession getToolSessionByLearner(User learner, Activity activity) throws LamsToolServiceException {
	return toolSessionDAO.getToolSessionByLearner(learner, activity);
    }

    @Override
    public ToolSession getToolSessionById(Long toolSessionId) {
	return toolSessionDAO.getToolSession(toolSessionId);
    }

    @Override
    public ToolSession getToolSessionByActivity(User learner, ToolActivity toolActivity)
	    throws LamsToolServiceException {
	return toolSessionDAO.getToolSessionByLearner(learner, toolActivity);
    }

    @Override
    public void notifyToolsToCreateSession(ToolSession toolSession, ToolActivity activity) throws ToolException {
	try {
	    ToolSessionManager sessionManager = (ToolSessionManager) findToolService(activity.getTool());

	    sessionManager.createToolSession(toolSession.getToolSessionId(), toolSession.getToolSessionName(),
		    activity.getToolContentId());
	} catch (NoSuchBeanDefinitionException e) {
	    String message = "A tool which is defined in the database appears to missing from the classpath. Unable to create tool session. ToolActivity "
		    + activity;
	    LamsCoreToolService.log.error(message, e);
	    throw new ToolException(message, e);
	}
    }

    @Override
    public Long notifyToolToCopyContent(ToolActivity toolActivity, String customCSV)
	    throws DataMissingException, ToolException {
	Long toolcontentID = toolActivity.getToolContentId();
	try {
	    ToolContentManager contentManager = (ToolContentManager) findToolService(toolActivity.getTool());

	    toolcontentID = contentIDGenerator.getNextToolContentIDFor(toolActivity.getTool());

	    // If it is a tool adapter tool, call the special copyToolContent which takes the customCSV param
	    if (contentManager instanceof ToolAdapterContentManager) {
		ToolAdapterContentManager adapterContentManager = (ToolAdapterContentManager) contentManager;
		adapterContentManager.copyToolContent(toolActivity.getToolContentId(), toolcontentID, customCSV);
	    } else {
		contentManager.copyToolContent(toolActivity.getToolContentId(), toolcontentID);
	    }

	} catch (NoSuchBeanDefinitionException e) {
	    String message = "A tool which is defined in the database appears to missing from the classpath. Unable to copy/update the tool content. ToolActivity "
		    + toolActivity;
	    LamsCoreToolService.log.error(message, e);
	    throw new ToolException(message, e);
	}

	return toolcontentID;
    }

    @Override
    public Long notifyToolToCopyContent(Long toolContentId, String customCSV)
	    throws DataMissingException, ToolException {
	ToolContent toolContent = (ToolContent) toolContentDAO.find(ToolContent.class, toolContentId);
	if (toolContent == null) {
	    String error = "The toolContentID " + toolContentId
		    + " is not valid. No such record exists on the database.";
	    LamsCoreToolService.log.error(error);
	    throw new DataMissingException(error);
	}

	Tool tool = toolContent.getTool();
	if (tool == null) {
	    String error = "The tool for toolContentId " + toolContentId + " is missing.";
	    LamsCoreToolService.log.error(error);
	    throw new DataMissingException(error);
	}

	Long newToolcontentID = contentIDGenerator.getNextToolContentIDFor(tool);
	try {
	    ToolContentManager contentManager = (ToolContentManager) findToolService(tool);
	    if (contentManager instanceof ToolAdapterContentManager) {
		ToolAdapterContentManager toolAdapterContentManager = (ToolAdapterContentManager) contentManager;
		toolAdapterContentManager.copyToolContent(toolContentId, newToolcontentID, customCSV);
	    } else {
		contentManager.copyToolContent(toolContentId, newToolcontentID);
	    }
	} catch (NoSuchBeanDefinitionException e) {
	    String message = "A tool which is defined in the database appears to missing from the classpath. Unable to copy the tool content. ToolContentId "
		    + toolContentId;
	    LamsCoreToolService.log.error(message, e);
	    throw new ToolException(message, e);
	}

	return newToolcontentID;
    }

    @Override
    public void notifyToolToDeleteContent(ToolActivity toolActivity) throws ToolException {
	try {
	    ToolContentManager contentManager = (ToolContentManager) findToolService(toolActivity.getTool());
	    contentManager.removeToolContent(toolActivity.getToolContentId());
	} catch (NoSuchBeanDefinitionException e) {
	    String message = "A tool which is defined in the database appears to missing from the classpath. Unable to delete the tool content. ToolActivity "
		    + toolActivity;
	    LamsCoreToolService.log.error(message, e);
	    throw new ToolException(message, e);
	}
    }

    @Override
    public void notifyToolToDeleteLearnerContent(ToolActivity toolActivity, Integer userId) throws ToolException {
	try {
	    ToolContentManager contentManager = (ToolContentManager) findToolService(toolActivity.getTool());
	    contentManager.removeLearnerContent(toolActivity.getToolContentId(), userId);
	} catch (NoSuchBeanDefinitionException e) {
	    String message = "A tool which is defined in the database appears to missing from the classpath. Unable to delete learner content. ToolActivity "
		    + toolActivity;
	    LamsCoreToolService.log.error(message, e);
	    throw new ToolException(message, e);
	}
    }

    @Override
    public SortedMap<String, ToolOutputDefinition> getOutputDefinitionsFromTool(Long toolContentId, int definitionType)
	    throws ToolException {

	ToolContent toolContent = (ToolContent) toolContentDAO.find(ToolContent.class, toolContentId);
	if (toolContent == null) {
	    String error = "The toolContentID " + toolContentId
		    + " is not valid. No such record exists on the database.";
	    LamsCoreToolService.log.error(error);
	    throw new DataMissingException(error);
	}

	Tool tool = toolContent.getTool();
	if (tool == null) {
	    String error = "The tool for toolContentId " + toolContentId + " is missing.";
	    LamsCoreToolService.log.error(error);
	    throw new DataMissingException(error);
	}

	try {
	    ToolContentManager contentManager = (ToolContentManager) findToolService(tool);
	    return contentManager.getToolOutputDefinitions(toolContentId, definitionType);
	} catch (NoSuchBeanDefinitionException e) {
	    String message = "A tool which is defined in the database appears to missing from the classpath. Unable to get the tool output definitions. ToolContentId "
		    + toolContentId;
	    LamsCoreToolService.log.error(message, e);
	    throw new ToolException(message, e);
	} catch (java.lang.AbstractMethodError e) {
	    String message = "Tool " + tool.getToolDisplayName()
		    + " doesn't support the getToolOutputDefinitions(toolContentId) method so no output definitions can be accessed.";
	    LamsCoreToolService.log.error(message, e);
	    throw new ToolException(message, e);
	}

    }

    @Override
    public SortedMap<String, ToolOutputDefinition> getOutputDefinitionsFromToolFiltered(Long outputToolContentId,
	    int definitionType, Long inputToolContentId) throws ToolException {
	SortedMap<String, ToolOutputDefinition> definitions = getOutputDefinitionsFromTool(outputToolContentId,
		definitionType);
	ToolContent toolContent = (ToolContent) toolContentDAO.find(ToolContent.class, inputToolContentId);
	if (toolContent == null) {
	    String error = "The toolContentID " + inputToolContentId
		    + " is not valid. No such record exists on the database.";
	    LamsCoreToolService.log.error(error);
	    throw new DataMissingException(error);
	}

	Tool tool = toolContent.getTool();
	if (tool == null) {
	    String error = "The tool for toolContentId " + inputToolContentId + " is missing.";
	    LamsCoreToolService.log.error(error);
	    throw new DataMissingException(error);
	}

	try {
	    ToolContentManager contentManager = (ToolContentManager) findToolService(tool);

	    Class[] supportedClasses = contentManager.getSupportedToolOutputDefinitionClasses(definitionType);
	    if (supportedClasses != null) {
		Set<String> keysToRemove = new TreeSet<String>();
		for (String key : definitions.keySet()) {
		    ToolOutputDefinition value = definitions.get(key);
		    Class valueClass = value.getValueClass();
		    boolean matchFound = false;
		    if (valueClass != null) {
			for (Class supportedClass : supportedClasses) {
			    // we take into account also superclasses
			    if (supportedClass.isAssignableFrom(valueClass)) {
				matchFound = true;
				break;
			    }
			}
		    }
		    if (!matchFound) {
			keysToRemove.add(key);
		    }
		}

		for (String key : keysToRemove) {
		    definitions.remove(key);
		}
	    }
	    return definitions;
	} catch (NoSuchBeanDefinitionException e) {
	    String message = "A tool which is defined in the database appears to missing from the classpath. Unable to get the tool output definitions. ToolContentId "
		    + inputToolContentId;
	    LamsCoreToolService.log.error(message, e);
	    throw new ToolException(message, e);
	} catch (java.lang.AbstractMethodError e) {
	    String message = "Tool " + tool.getToolDisplayName()
		    + " doesn't support the getSupportedToolOutputDefinitionClasses(definitionType) method so no output definitions can be accessed.";
	    LamsCoreToolService.log.error(message, e);
	    throw new ToolException(message, e);
	}

    }

    @Override
    public ToolOutput getOutputFromTool(String conditionName, Long toolSessionId, Integer learnerId)
	    throws ToolException {

	ToolSession session = toolSessionDAO.getToolSession(toolSessionId);
	return getOutputFromTool(conditionName, session, learnerId);

    }

    @Override
    public ToolOutput getOutputFromTool(String conditionName, ToolSession toolSession, Integer learnerId)
	    throws ToolException {

	if (toolSession == null) {
	    String error = "The toolSession is not valid. Unable to get the tool output";
	    LamsCoreToolService.log.error(error);
	    throw new DataMissingException(error);
	}

	Tool tool = toolSession.getToolActivity().getTool();
	if (tool == null) {
	    String error = "The tool for toolSession " + toolSession.getToolSessionId() + " is missing.";
	    LamsCoreToolService.log.error(error);
	    throw new DataMissingException(error);
	}

	try {
	    ToolSessionManager sessionManager = (ToolSessionManager) findToolService(tool);
	    Long longLearnerId = learnerId != null ? new Long(learnerId.longValue()) : null;
	    return sessionManager.getToolOutput(conditionName, toolSession.getToolSessionId(), longLearnerId);
	} catch (NoSuchBeanDefinitionException e) {
	    String message = "A tool which is defined in the database appears to missing from the classpath. Unable to gt the tol output. toolSession "
		    + toolSession.getToolSessionId();
	    LamsCoreToolService.log.error(message, e);
	    throw new ToolException(message, e);
	} catch (java.lang.AbstractMethodError e) {
	    String message = "Tool " + tool.getToolDisplayName()
		    + " doesn't support the getToolOutput(name, toolSessionId, learnerId) method so no output definitions can be accessed.";
	    LamsCoreToolService.log.error(message, e);
	    throw new ToolException(message, e);
	}
    }
    
    @Override
    public List<ToolOutput> getOutputsFromTool(String conditionName, ToolActivity toolActivity)
	    throws ToolException {

	if (toolActivity == null) {
	    String error = "The toolActivity is null. Unable to get tool outputs";
	    LamsCoreToolService.log.error(error);
	    throw new DataMissingException(error);
	}

	Tool tool = toolActivity.getTool();
	if (tool == null) {
	    String error = "The tool for toolActivity " + toolActivity.getActivityId() + " is missing.";
	    LamsCoreToolService.log.error(error);
	    throw new DataMissingException(error);
	}

	try {
	    ToolSessionManager sessionManager = (ToolSessionManager) findToolService(tool);
	    return sessionManager.getToolOutputs(conditionName, toolActivity.getToolContentId());
	} catch (NoSuchBeanDefinitionException e) {
	    String message = "A tool which is defined in the database appears to missing from the classpath. Unable to grt the tol output. toolActivity "
		    + toolActivity.getActivityId();
	    LamsCoreToolService.log.error(message, e);
	    throw new ToolException(message, e);
	} catch (java.lang.AbstractMethodError e) {
	    String message = "Tool " + tool.getToolDisplayName()
		    + " doesn't support the getToolOutput(name, toolSessionId, learnerId) method so no output definitions can be accessed.";
	    LamsCoreToolService.log.error(message, e);
	    throw new ToolException(message, e);
	}
    }

    @Override
    public void forceCompleteActivity(ToolSession toolSession, User learner) throws ToolException {

	if (toolSession == null) {
	    String error = "The toolSession is not valid. Unable to force complete activity.";
	    LamsCoreToolService.log.error(error);
	    throw new DataMissingException(error);
	}

	Tool tool = toolSession.getToolActivity().getTool();
	if (tool == null) {
	    String error = "The tool for toolSession " + toolSession.getToolSessionId() + " is missing.";
	    LamsCoreToolService.log.error(error);
	    throw new DataMissingException(error);
	}

	try {
	    ToolSessionManager sessionManager = (ToolSessionManager) findToolService(tool);
	    sessionManager.forceCompleteUser(toolSession.getToolSessionId(), learner);
	} catch (NoSuchBeanDefinitionException e) {
	    String message = "A tool which is defined in the database appears to missing from the classpath. Unable to force complete activity. toolSession "
		    + toolSession.getToolSessionId();
	    LamsCoreToolService.log.error(message, e);
	    throw new ToolException(message, e);
	} catch (java.lang.AbstractMethodError e) {
	    String message = "Tool " + tool.getToolDisplayName()
		    + " doesn't support the forceCompleteUser(ToolSession toolSession, User learner) method so can't force complete learner.";
	    LamsCoreToolService.log.error(message, e);
	    throw new ToolException(message, e);
	}
    }

    @Override
    public SortedMap<String, ToolOutput> getOutputFromTool(List<String> names, Long toolSessionId, Integer learnerId)
	    throws ToolException {
	ToolSession session = toolSessionDAO.getToolSession(toolSessionId);
	return getOutputFromTool(names, session, learnerId);
    }

    @Override
    public SortedMap<String, ToolOutput> getOutputFromTool(List<String> names, ToolSession toolSession,
	    Integer learnerId) throws ToolException {

	if (toolSession == null) {
	    String error = "The toolSession is not valid. Unable to get the tool output";
	    LamsCoreToolService.log.error(error);
	    throw new DataMissingException(error);
	}

	Tool tool = toolSession.getToolActivity().getTool();
	if (tool == null) {
	    String error = "The tool for toolSession " + toolSession.getToolSessionId() + " is missing.";
	    LamsCoreToolService.log.error(error);
	    throw new DataMissingException(error);
	}

	try {
	    ToolSessionManager sessionManager = (ToolSessionManager) findToolService(tool);
	    Long longLearnerId = learnerId != null ? new Long(learnerId.longValue()) : null;
	    return sessionManager.getToolOutput(names, toolSession.getToolSessionId(), longLearnerId);
	} catch (NoSuchBeanDefinitionException e) {
	    String message = "A tool which is defined in the database appears to missing from the classpath. Unable to gt the tol output. toolSession "
		    + toolSession.getToolSessionId();
	    LamsCoreToolService.log.error(message, e);
	    throw new ToolException(message, e);
	} catch (java.lang.AbstractMethodError e) {
	    String message = "Tool " + tool.getToolDisplayName()
		    + " doesn't support the getToolOutput(name, toolSessionId, learnerId) method so no output definitions can be accessed.";
	    LamsCoreToolService.log.error(message, e);
	    throw new ToolException(message, e);
	}
    }

    @Override
    public Long getActivityMaxPossibleMark(ToolActivity activity) {
	SortedMap<String, ToolOutputDefinition> map = getOutputDefinitionsFromTool(activity.getToolContentId(),
		ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_CONDITION);

	Set<ActivityEvaluation> actEvals = activity.getActivityEvaluations();

	if (map != null) {
	    for (String key : map.keySet()) {
		ToolOutputDefinition definition = map.get(key);
		if (actEvals != null && actEvals.size() > 0) {

		    // get first evaluation
		    ActivityEvaluation actEval = actEvals.iterator().next();

		    if (actEval.getToolOutputDefinition().equals(key)) {

			Object upperLimit = definition.getEndValue();
			if (upperLimit != null && upperLimit instanceof Long) {
			    return (Long) upperLimit;
			}
			break;
		    }
		} else {
		    if (definition.isDefaultGradebookMark() != null && definition.isDefaultGradebookMark()) {
			Object upperLimit = definition.getEndValue();
			if (upperLimit != null && upperLimit instanceof Long) {
			    return (Long) upperLimit;
			}
			break;
		    }
		}
	    }
	}
	return null;
    }

    @Override
    public void updateToolSession(ToolSession toolSession) {
	toolSessionDAO.updateToolSession(toolSession);
    }

    @Override
    public List getToolSessionsByLesson(Lesson lesson) {
	return toolSessionDAO.getToolSessionsByLesson(lesson);
    }

    @Override
    public void deleteToolSession(ToolSession toolSession) {
	if (toolSession == null) {
	    LamsCoreToolService.log.error("deleteToolSession: unable to delete tool session as tool session is null.");
	    return;
	}

	// call the tool to remove the session details
	ToolSessionManager sessionManager = (ToolSessionManager) findToolService(
		toolSession.getToolActivity().getTool());

	try {
	    sessionManager.removeToolSession(toolSession.getToolSessionId());
	} catch (DataMissingException e) {
	    LamsCoreToolService.log.error("Unable to delete tool data for tool session "
		    + toolSession.getToolSessionId() + " as toolSession does not exist", e);
	} catch (ToolException e) {
	    LamsCoreToolService.log.error("Unable to delete tool data for tool session "
		    + toolSession.getToolSessionId() + " as tool threw an exception", e);
	}

	// now remove the tool session from the core tables.
	toolSessionDAO.removeToolSession(toolSession);

    }

    @Override
    public String getToolLearnerURL(Long lessonID, Activity activity, User learner) throws LamsToolServiceException {
	if (activity.isToolActivity()) {
	    ToolActivity toolActivity = (ToolActivity) activity;
	    String toolURL = toolActivity.getTool().getLearnerUrl();
	    return setupToolURLWithToolSession(toolActivity, learner, toolURL);
	} else if (activity.isSystemToolActivity()) {
	    SystemTool sysTool = systemToolDAO.getSystemToolByActivityTypeId(activity.getActivityTypeId());
	    if (sysTool != null) {
		return setupURLWithActivityLessonID(activity, lessonID, sysTool.getLearnerUrl());
	    }
	}
	return null;
    }

    @Override
    public String getToolLearnerPreviewURL(Long lessonID, Activity activity, User authorLearner)
	    throws LamsToolServiceException {
	if (activity.isToolActivity()) {
	    ToolActivity toolActivity = (ToolActivity) activity;
	    String toolURL = toolActivity.getTool().getLearnerPreviewUrl();
	    return setupToolURLWithToolSession(toolActivity, authorLearner, toolURL);
	} else if (activity.isSystemToolActivity()) {
	    SystemTool sysTool = systemToolDAO.getSystemToolByActivityTypeId(activity.getActivityTypeId());
	    if (sysTool != null) {
		return setupURLWithActivityLessonID(activity, lessonID, sysTool.getLearnerPreviewUrl());
	    }
	}
	return null;
    }

    @Override
    public String getToolLearnerProgressURL(Long lessonID, Activity activity, User learner)
	    throws LamsToolServiceException {
	if (activity.isToolActivity()) {
	    ToolActivity toolActivity = (ToolActivity) activity;
	    String toolURL = toolActivity.getTool().getLearnerProgressUrl();
	    toolURL = appendUserIDToURL(learner, toolURL);
	    return setupToolURLWithToolSession(toolActivity, learner, toolURL);
	} else if (activity.isSystemToolActivity()) {
	    SystemTool sysTool = systemToolDAO.getSystemToolByActivityTypeId(activity.getActivityTypeId());
	    if (sysTool != null) {
		return setupURLWithActivityLessonUserID(activity, lessonID, learner.getUserId(),
			sysTool.getLearnerProgressUrl());
	    }
	}
	return null;
    }

    @Override
    public String getToolMonitoringURL(Long lessonID, Activity activity) throws LamsToolServiceException {

	if (activity.isToolActivity()) {
	    ToolActivity toolActivity = (ToolActivity) activity;
	    String url = toolActivity.getTool().getMonitorUrl();
	    if (url != null) {
		return setupToolURLWithToolContent(toolActivity, url);
	    }
	} else if (activity.isSystemToolActivity()) {
	    SystemTool sysTool = systemToolDAO.getSystemToolByActivityTypeId(activity.getActivityTypeId());
	    if (sysTool != null) {
		return setupURLWithActivityLessonID(activity, lessonID, sysTool.getMonitorUrl());
	    }
	}
	return null;
    }

    @Override
    public String getToolContributionURL(Long lessonID, Activity activity) throws LamsToolServiceException {
	if (activity.isSystemToolActivity()) {
	    SystemTool sysTool = systemToolDAO.getSystemToolByActivityTypeId(activity.getActivityTypeId());
	    if (sysTool != null) {
		return setupURLWithActivityLessonID(activity, lessonID, sysTool.getContributeUrl());
	    }
	}
	return null;
    }

    @Override
    public String getToolAuthorURL(Long lessonID, ToolActivity activity, ToolAccessMode mode) {
	String url = activity.getTool().getAuthorUrl();
	url = WebUtil.appendParameterToURL(url, AttributeNames.PARAM_TOOL_CONTENT_ID,
		activity.getToolContentId().toString());
	// should have used LessonService, but reusing existing tools is just easier
	Lesson lesson = (Lesson) toolContentDAO.find(Lesson.class, lessonID);
	url = WebUtil.appendParameterToURL(url, AttributeNames.PARAM_CONTENT_FOLDER_ID,
		lesson.getLearningDesign().getContentFolderID());

	url = WebUtil.appendParameterToURL(url, AttributeNames.PARAM_MODE, mode.toString());
	return url;
    }

    /**
     * Add the user id to the url
     */
    private String appendUserIDToURL(User user, String toolURL) {
	return WebUtil.appendParameterToURL(toolURL, AttributeNames.PARAM_USER_ID, user.getUserId().toString());
    }

    @Override
    public String setupToolURLWithToolSession(ToolActivity activity, User learner, String toolURL)
	    throws LamsToolServiceException {
	ToolSession toolSession = this.getToolSessionByActivity(learner, activity);

	if (toolSession == null) {
	    String error = "Unable to set up url as session does not exist. Activity "
		    + (activity != null ? activity.getActivityId() + ":" + activity.getTitle() : "null") + " learner "
		    + (learner != null ? learner.getUserId() + ":" + learner.getLogin() : "null");
	    LamsCoreToolService.log.error(error);
	    throw new LamsToolServiceException(error);
	}

	return WebUtil.appendParameterToURL(toolURL, AttributeNames.PARAM_TOOL_SESSION_ID,
		toolSession.getToolSessionId().toString());
    }

    private String setupURLWithActivityLessonUserID(Activity activity, Long lessonID, Integer userID,
	    String learnerURL) {
	String url = setupURLWithActivityLessonID(activity, lessonID, learnerURL);
	if (url != null && userID != null) {
	    url = WebUtil.appendParameterToURL(url, AttributeNames.PARAM_USER_ID, userID.toString());
	}
	return url;
    }

    private String setupURLWithActivityLessonID(Activity activity, Long lessonID, String learnerURL) {
	String url = learnerURL;
	if (url != null && activity != null) {
	    url = WebUtil.appendParameterToURL(url, AttributeNames.PARAM_ACTIVITY_ID,
		    activity.getActivityId().toString());
	}
	if (url != null && lessonID != null) {
	    url = WebUtil.appendParameterToURL(url, AttributeNames.PARAM_LESSON_ID, lessonID.toString());
	}
	return url;
    }

    @Override
    public String setupToolURLWithToolContent(ToolActivity activity, String toolURL) {
	return WebUtil.appendParameterToURL(toolURL, AttributeNames.PARAM_TOOL_CONTENT_ID,
		activity.getToolContentId().toString());
    }

    @Override
    public Object findToolService(Tool tool) throws NoSuchBeanDefinitionException {
	return context.getBean(tool.getServiceName());
    }

    @Override
    public boolean isContentEdited(Activity activity) {
	if (activity.isToolActivity()) {
	    ToolActivity toolActivity = (ToolActivity) activity;
	    ToolContentManager toolService = (ToolContentManager) findToolService(toolActivity.getTool());
	    return toolService.isContentEdited(toolActivity.getToolContentId());
	}
	return false;
    }
}
