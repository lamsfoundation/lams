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



package org.lamsfoundation.lams.tool.forum.web.actions;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.learningdesign.TextSearchConditionComparator;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.forum.dto.MessageDTO;
import org.lamsfoundation.lams.tool.forum.persistence.Attachment;
import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.ForumCondition;
import org.lamsfoundation.lams.tool.forum.persistence.ForumToolSession;
import org.lamsfoundation.lams.tool.forum.persistence.ForumUser;
import org.lamsfoundation.lams.tool.forum.persistence.Message;
import org.lamsfoundation.lams.tool.forum.persistence.PersistenceException;
import org.lamsfoundation.lams.tool.forum.service.IForumService;
import org.lamsfoundation.lams.tool.forum.util.AttachmentComparator;
import org.lamsfoundation.lams.tool.forum.util.ForumConstants;
import org.lamsfoundation.lams.tool.forum.util.ForumWebUtils;
import org.lamsfoundation.lams.tool.forum.util.MessageComparator;
import org.lamsfoundation.lams.tool.forum.util.MessageDtoComparator;
import org.lamsfoundation.lams.tool.forum.web.forms.ForumForm;
import org.lamsfoundation.lams.tool.forum.web.forms.ForumPedagogicalPlannerForm;
import org.lamsfoundation.lams.tool.forum.web.forms.MessageForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Steve.Ni
 * @version $Revision$
 */
public class AuthoringAction extends Action {
    private static Logger log = Logger.getLogger(AuthoringAction.class);
    private IForumService forumService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, PersistenceException,
	    IllegalAccessException, NoSuchMethodException, InvocationTargetException {

	String param = mapping.getParameter();
	// -----------------------Forum Author function ---------------------------
	if (param.equals("initPage")) {
	    request.setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.AUTHOR);
	    return initPage(mapping, form, request, response);
	}

	// ***************** Monitoring define later screen ********************
	if (param.equals("defineLater")) {
	    // update define later flag to true
	    request.setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER);
	    forumService = getForumManager();
	    Long contentId = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
	    Forum forum = forumService.getForumByContentId(contentId);

	    boolean isForumEditable = ForumWebUtils.isForumEditable(forum);
	    if (!isForumEditable) {
		request.setAttribute(ForumConstants.PAGE_EDITABLE, new Boolean(isForumEditable));
		return mapping.findForward("forbidden");
	    }

	    if (!forum.isContentInUse()) {
		forum.setDefineLater(true);
		forumService.updateForum(forum);
	    }

	    return initPage(mapping, form, request, response);
	}
	if (param.equals("updateContent")) {
	    return updateContent(mapping, form, request, response);
	}
	// -----------------------Topic function ---------------------------
	if (param.equals("newTopic")) {
	    return newTopic(mapping, form, request, response);
	}
	if (param.equals("createTopic")) {
	    return createTopic(mapping, form, request, response);
	}
	if (param.equals("editTopic")) {
	    return editTopic(mapping, form, request, response);
	}
	if (param.equals("updateTopic")) {
	    return updateTopic(mapping, form, request, response);
	}
	if (param.equals("deleteTopic")) {
	    return deleteTopic(mapping, form, request, response);
	}
	if (param.equals("deleteAttachment")) {
	    return deleteAttachment(mapping, form, request, response);
	}
	if (param.equals("upTopic")) {
	    return upTopic(mapping, form, request, response);
	}
	if (param.equals("downTopic")) {
	    return downTopic(mapping, form, request, response);
	}
	if (param.equals("initPedagogicalPlannerForm")) {
	    return initPedagogicalPlannerForm(mapping, form, request, response);
	}
	if (param.equals("saveOrUpdatePedagogicalPlannerForm")) {
	    return saveOrUpdatePedagogicalPlannerForm(mapping, form, request, response);
	}
	if (param.equals("createPedagogicalPlannerTopic")) {
	    return createPedagogicalPlannerTopic(mapping, form, request, response);
	}

	return mapping.findForward("error");
    }

    // ******************************************************************************************************************
    // Forum Author functions
    // ******************************************************************************************************************

    /**
     * This page will display initial submit tool content. Or just a blank page if the toolContentID does not exist
     * before.
     *
     * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    protected ActionForward initPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// initial Session Map
	SessionMap sessionMap = new SessionMap();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	Long contentId = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	ForumForm forumForm = (ForumForm) form;
	forumForm.setSessionMapID(sessionMap.getSessionID());
	forumForm.setContentFolderID(contentFolderID);
	sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);

	// get back the topic list and display them on page
	forumService = getForumManager();

	List<MessageDTO> topics = null;
	Forum forum = null;
	try {
	    forum = forumService.getForumByContentId(contentId);
	    // if forum does not exist, try to use default content instead.
	    if (forum == null) {
		forum = forumService.getDefaultContent(contentId);
		if (forum.getMessages() != null) {
		    List<Message> list = new ArrayList<Message>();
		    // sorted by create date
		    Iterator iter = forum.getMessages().iterator();
		    while (iter.hasNext()) {
			Message topic = (Message) iter.next();
			// contentFolderID != -1 means it is sysadmin: LDEV-906
			if (topic.getCreatedBy() == null && !StringUtils.equals(contentFolderID, "-1")) {
			    // get login user (author)
			    HttpSession ss = SessionManager.getSession();
			    // get back login user DTO
			    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
			    ForumUser fuser = new ForumUser(user, null);
			    topic.setCreatedBy(fuser);
			}
			list.add(topic);
		    }
		    topics = MessageDTO.getMessageDTO(list);
		} else {
		    topics = null;
		}
	    } else {
		topics = forumService.getAuthoredTopics(forum.getUid());
		// failure tolerance: if current contentID is defaultID, the createBy will be null.
		// contentFolderID != -1 means it is sysadmin: LDEV-906
		if (!StringUtils.equals(contentFolderID, "-1")) {
		    for (MessageDTO messageDTO : topics) {
			if (StringUtils.isBlank(messageDTO.getAuthor())) {
			    // get login user (author)
			    HttpSession ss = SessionManager.getSession();
			    // get back login user DTO
			    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
			    ForumUser fuser = new ForumUser(user, null);
			    messageDTO.setAuthor(fuser.getFirstName() + " " + fuser.getLastName());
			}
		    }
		}
	    }

	    // tear down PO to normal object using clone() method
	    forumForm.setForum((Forum) forum.clone());

	    sessionMap.put(ForumConstants.AUTHORING_FORUM, forum);
	} catch (Exception e) {
	    AuthoringAction.log.error(e);
	    return mapping.findForward("error");
	}

	// set back STRUTS component value
	// init it to avoid null exception in following handling
	if (topics == null) {
	    topics = new ArrayList<MessageDTO>();
	}

	Set topicSet = new TreeSet<MessageDTO>(new MessageDtoComparator());
	topicSet.addAll(topics);
	sessionMap.put(ForumConstants.AUTHORING_TOPICS_LIST, topicSet);

	// init condition set
	SortedSet<ForumCondition> conditionSet = getForumConditionSet(sessionMap);
	conditionSet.clear();
	conditionSet.addAll(forum.getConditions());

	return mapping.findForward("success");
    }

    /**
     * Update all content for forum. These contents contain
     * <ol>
     * <li>Forum authoring infomation, e.g. online/offline instruction, title, advacnce options, etc.</li>
     * <li>Uploaded offline/online instruction files</li>
     * <li>Topics author created</li>
     * <li>Topics' attachment file</li>
     * <li>Author user information</li>
     * </ol>
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public ActionForward updateContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response)
	    throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

	ToolAccessMode mode = getAccessMode(request);
	ForumForm forumForm = (ForumForm) form;
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(forumForm.getSessionMapID());
	// validation
	ActionMessages errors = validate(forumForm, mapping, request);
	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    if (mode.isAuthor()) {
		return mapping.findForward("author");
	    } else {
		return mapping.findForward("monitor");
	    }
	}

	Forum forum = forumForm.getForum();
	// get back tool content ID
	forum.setContentId(forumForm.getToolContentID());

	forumService = getForumManager();

	// *******************************Handle user*******************
	String contentFolderID = (String) sessionMap.get(AttributeNames.PARAM_CONTENT_FOLDER_ID);
	ForumUser forumUser = null;
	// check whether it is sysadmin:LDEV-906
	if (!StringUtils.equals(contentFolderID, "-1")) {
	    // try to get form system session
	    HttpSession ss = SessionManager.getSession();
	    // get back login user DTO
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    forumUser = forumService.getUserByID(new Long(user.getUserID().intValue()));
	    if (forumUser == null) {
		forumUser = new ForumUser(user, null);
		forumService.createUser(forumUser);
	    }
	}
	// **********************************Get Forum PO*********************
	Forum forumPO = forumService.getForumByContentId(forumForm.getToolContentID());
	if (forumPO == null) {
	    // new Forum, create it.
	    forumPO = forum;
	    forumPO.setContentId(forumForm.getToolContentID());
	} else {
	    if (mode.isAuthor()) {
		Long uid = forumPO.getUid();
		PropertyUtils.copyProperties(forumPO, forum);
		// get back UID
		forumPO.setUid(uid);
	    } else {
		// if it is Teacher, then just update basic tab content (definelater)
		forumPO.setInstructions(forum.getInstructions());
		forumPO.setTitle(forum.getTitle());
		// change define later status
		forumPO.setDefineLater(false);
	    }
	}
	forumPO.setCreatedBy(forumUser);

	// copy back
	forum = forumService.updateForum(forumPO);

	// delete message attachment
	List topicDeleteAttachmentList = getTopicDeletedAttachmentList(sessionMap);
	Iterator iter = topicDeleteAttachmentList.iterator();
	while (iter.hasNext()) {
	    Attachment delAtt = (Attachment) iter.next();
	    iter.remove();
	}

	// **************************** Handle topic *********************
	Set<MessageDTO> topics = getTopics(sessionMap);
	iter = topics.iterator();
	while (iter.hasNext()) {
	    MessageDTO dto = (MessageDTO) iter.next();
	    if (dto.getMessage() != null) {
		// This flushes user UID info to message if this user is a new user.
		dto.getMessage().setCreatedBy(forumUser);
		dto.getMessage().setModifiedBy(forumUser);
		forumService.createRootTopic(forum.getUid(), null, dto.getMessage());
	    }
	}
	// delete them from database.
	List delTopics = getDeletedTopicList(sessionMap);
	iter = delTopics.iterator();
	while (iter.hasNext()) {
	    MessageDTO dto = (MessageDTO) iter.next();
	    iter.remove();
	    if (dto.getMessage() != null && dto.getMessage().getUid() != null) {
		forumService.deleteTopic(dto.getMessage().getUid());
	    }
	}

	// ******************************** Handle conditions ****************
	Set<ForumCondition> conditions = getForumConditionSet(sessionMap);
	List delConditions = getDeletedForumConditionList(sessionMap);

	// delete conditions that don't contain any topics
	Iterator<ForumCondition> conditionIter = conditions.iterator();
	while (conditionIter.hasNext()) {
	    ForumCondition condition = conditionIter.next();
	    if (condition.getTopics().isEmpty()) {
		conditionIter.remove();
		delConditions.add(condition);

		// reorder remaining conditions
		for (ForumCondition otherCondition : conditions) {
		    if (otherCondition.getOrderId() > condition.getOrderId()) {
			otherCondition.setOrderId(otherCondition.getOrderId() - 1);
		    }
		}
	    }
	}
	forum.setConditions(conditions);

	iter = delConditions.iterator();
	while (iter.hasNext()) {
	    ForumCondition condition = (ForumCondition) iter.next();
	    iter.remove();
	    forumService.deleteCondition(condition);
	}

	forum = forumService.updateForum(forum);

	request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);
	if (mode.isAuthor()) {
	    return mapping.findForward("author");
	} else {
	    return mapping.findForward("monitor");
	}
    }

    // ******************************************************************************************************************
    // Topic functions
    // ******************************************************************************************************************
    /**
     * Display emtpy topic page for user input topic information. This page will contain all topics list which this
     * author posted before.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward newTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String sessionMapID = WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID);
	((MessageForm) form).setSessionMapID(sessionMapID);

	return mapping.findForward("success");
    }

    /**
     * Create a topic in memory. This topic will be saved when user save entire authoring page.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws PersistenceException
     */
    public ActionForward createTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, PersistenceException {
	MessageForm messageForm = (MessageForm) form;
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(messageForm.getSessionMapID());
	request.setAttribute(ForumConstants.ATTR_SESSION_MAP_ID, messageForm.getSessionMapID());

	SortedSet topics = getTopics(sessionMap);
	// get login user (author)
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	// get message info from web page
	Message message = messageForm.getMessage();
	// init some basic variables for first time create
	message.setIsAuthored(true);
	message.setCreated(new Date());
	message.setUpdated(new Date());
	message.setLastReplyDate(new Date());

	int maxSeq = 1;
	if (topics.size() > 0) {
	    MessageDTO last = (MessageDTO) topics.last();
	    maxSeq = last.getMessage().getSequenceId() + 1;
	}
	message.setSequenceId(maxSeq);

	// check whether this user exist or not
	ForumUser forumUser = forumService.getUserByID(new Long(user.getUserID().intValue()));
	String contentFolderID = (String) sessionMap.get(AttributeNames.PARAM_CONTENT_FOLDER_ID);
	// check whether it is sysadmin:LDEV-906
	if (forumUser == null && !StringUtils.equals(contentFolderID, "-1")) {
	    // if user not exist, create new one in database
	    forumUser = new ForumUser(user, null);
	}
	message.setCreatedBy(forumUser);
	// same person with create at first time
	message.setModifiedBy(forumUser);

	// set attachment of this topic
	Set attSet = null;
	if (messageForm.getAttachmentFile() != null
		&& !StringUtils.isEmpty(messageForm.getAttachmentFile().getFileName())) {
	    forumService = getForumManager();
	    Attachment att = forumService.uploadAttachment(messageForm.getAttachmentFile());
	    // only allow one attachment, so replace whatever
	    attSet = new HashSet();
	    attSet.add(att);
	}
	message.setAttachments(attSet);

	// create clones of this topic (appropriate only for editing in monitoring)
	Forum forum = (Forum) sessionMap.get(ForumConstants.AUTHORING_FORUM);
	if (forum != null) {
	    List<ForumToolSession> toolSessions = forumService.getSessionsByContentId(forum.getContentId());
	    for (ForumToolSession toolSession : toolSessions) {
		Message newMsg = Message.newInstance(message);
		newMsg.setToolSession(toolSession);
		newMsg.setAttachments(new TreeSet<Attachment>(new AttachmentComparator()));
		newMsg.setModifiedBy(null);
		newMsg.setCreatedBy(null);
		message.getSessionClones().add(newMsg);
	    }
	}

	topics.add(MessageDTO.getMessageDTO(message));

	return mapping.findForward("success");
    }

    /**
     * Delete a topic form current topic list. But database record will be deleted only when user save whole authoring
     * page.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws PersistenceException
     */
    public ActionForward deleteTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws PersistenceException {
	// get SessionMAP
	String sessionMapID = WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	request.setAttribute(ForumConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	String topicIndex = request.getParameter(ForumConstants.AUTHORING_TOPICS_INDEX);
	int topicIdx = NumberUtils.stringToInt(topicIndex, -1);

	if (topicIdx != -1) {
	    Set topics = getTopics(sessionMap);
	    List<MessageDTO> rList = new ArrayList<MessageDTO>(topics);
	    MessageDTO item = rList.remove(topicIdx);
	    topics.clear();
	    topics.addAll(rList);
	    // add to delList
	    List delList = getDeletedTopicList(sessionMap);
	    delList.add(item);

	    SortedSet<ForumCondition> list = getForumConditionSet(sessionMap);
	    Iterator<ForumCondition> conditionIter = list.iterator();

	    while (conditionIter.hasNext()) {
		ForumCondition condition = conditionIter.next();
		Set<Message> topicList = condition.getTopics();
		if (topicList.contains(item.getMessage())) {
		    topicList.remove(item.getMessage());
		}
	    }
	}

	return mapping.findForward("success");
    }

    /**
     * Display a HTML FORM which contains subject, body and attachment information from a special topic. This page is
     * ready for user update this topic.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws PersistenceException
     */
    public ActionForward editTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws PersistenceException {

	// get SessionMAP
	String sessionMapID = WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	MessageForm msgForm = (MessageForm) form;
	msgForm.setSessionMapID(sessionMapID);

	String topicIndex = request.getParameter(ForumConstants.AUTHORING_TOPICS_INDEX);
	int topicIdx = NumberUtils.stringToInt(topicIndex, -1);
	if (topicIdx != -1) {
	    Set topics = getTopics(sessionMap);
	    List<MessageDTO> rList = new ArrayList<MessageDTO>(topics);
	    MessageDTO topic = rList.get(topicIdx);
	    if (topic != null) {
		// update message to HTML Form to echo back to web page: for subject, body display
		msgForm.setMessage(topic.getMessage());
	    }
	    // echo back to web page: for attachment display
	    request.setAttribute(ForumConstants.AUTHORING_TOPIC, topic);
	}

	request.setAttribute(ForumConstants.AUTHORING_TOPICS_INDEX, topicIndex);
	return mapping.findForward("success");
    }

    /**
     * Submit user updated inforamion in a topic to memory. This update will be submit to database only when user save
     * whole authoring page.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws PersistenceException
     */
    public ActionForward updateTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws PersistenceException {
	MessageForm messageForm = (MessageForm) form;
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(messageForm.getSessionMapID());
	request.setAttribute(ForumConstants.ATTR_SESSION_MAP_ID, messageForm.getSessionMapID());

	// get param from HttpServletRequest
	String topicIndex = request.getParameter(ForumConstants.AUTHORING_TOPICS_INDEX);
	int topicIdx = NumberUtils.stringToInt(topicIndex, -1);

	if (topicIdx != -1) {
	    Message message = messageForm.getMessage();

	    Set topics = getTopics(sessionMap);
	    List<MessageDTO> rList = new ArrayList<MessageDTO>(topics);
	    MessageDTO newMsg = rList.get(topicIdx);
	    if (newMsg.getMessage() == null) {
		newMsg.setMessage(new Message());
	    }

	    newMsg.getMessage().setSubject(message.getSubject());
	    newMsg.getMessage().setBody(message.getBody());
	    newMsg.getMessage().setUpdated(new Date());
	    // update attachment
	    if (messageForm.getAttachmentFile() != null
		    && !StringUtils.isEmpty(messageForm.getAttachmentFile().getFileName())) {
		forumService = getForumManager();
		Attachment att = forumService.uploadAttachment(messageForm.getAttachmentFile());
		// only allow one attachment, so replace whatever
		Set attSet = new HashSet();
		attSet.add(att);
		newMsg.setHasAttachment(true);
		newMsg.getMessage().setAttachments(attSet);
	    } else if (!messageForm.isHasAttachment()) {
		Set att = newMsg.getMessage().getAttachments();
		if (att != null && att.size() > 0) {
		    List delTopicAtt = getTopicDeletedAttachmentList(sessionMap);
		    delTopicAtt.add(att.iterator().next());
		}
		newMsg.setHasAttachment(false);
		newMsg.getMessage().setAttachments(null);
	    }
	}

	request.setAttribute(ForumConstants.AUTHORING_TOPICS_INDEX, topicIndex);
	return mapping.findForward("success");
    }

    /**
     * Remove message attachment.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward deleteAttachment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("itemAttachment", null);
	return mapping.findForward("success");
    }

    /**
     * Move up current topic.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward upTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return switchTopic(mapping, request, true);
    }

    /**
     * Move down current topic.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward downTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return switchTopic(mapping, request, false);
    }

    private ActionForward switchTopic(ActionMapping mapping, HttpServletRequest request, boolean up) {
	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int itemIdx = NumberUtils.stringToInt(request.getParameter(ForumConstants.AUTHORING_TOPICS_INDEX), -1);
	if (itemIdx != -1) {
	    Set topics = getTopics(sessionMap);
	    List<MessageDTO> rList = new ArrayList<MessageDTO>(topics);
	    MessageDTO newMsg = rList.get(itemIdx);

	    // get current and the target item, and switch their sequence
	    MessageDTO item = rList.get(itemIdx);
	    MessageDTO repItem;
	    if (up) {
		repItem = rList.get(--itemIdx);
	    } else {
		repItem = rList.get(++itemIdx);
	    }
	    int upSeqId = repItem.getMessage().getSequenceId();
	    repItem.getMessage().setSequenceId(item.getMessage().getSequenceId());
	    item.getMessage().setSequenceId(upSeqId);

	    // put back list, it will be sorted again
	    topics.clear();
	    topics.addAll(rList);
	}

	request.setAttribute(ForumConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return mapping.findForward("success");
    }

    // ******************************************************************************************************************
    // Private method for internal functions
    // ******************************************************************************************************************
    private IForumService getForumManager() {
	if (forumService == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    forumService = (IForumService) wac.getBean(ForumConstants.FORUM_SERVICE);
	}
	return forumService;
    }

    /**
     * @param request
     * @return
     */
    private SortedSet<MessageDTO> getTopics(SessionMap sessionMap) {
	SortedSet<MessageDTO> topics = (SortedSet<MessageDTO>) sessionMap.get(ForumConstants.AUTHORING_TOPICS_LIST);
	if (topics == null) {
	    topics = new TreeSet<MessageDTO>(new MessageDtoComparator());
	    sessionMap.put(ForumConstants.AUTHORING_TOPICS_LIST, topics);
	}
	return topics;
    }

    /**
     * @param request
     * @return
     */
    private List getTopicDeletedAttachmentList(SessionMap sessionMap) {
	return getListFromSession(sessionMap, ForumConstants.DELETED_ATTACHMENT_LIST);
    }

    /**
     * @param request
     * @return
     */
    private List getDeletedTopicList(SessionMap sessionMap) {
	return getListFromSession(sessionMap, ForumConstants.DELETED_AUTHORING_TOPICS_LIST);
    }

    /**
     * Get <code>java.util.List</code> from HttpSession by given name.
     *
     * @param request
     * @param name
     * @return
     */
    private List getListFromSession(SessionMap sessionMap, String name) {
	List list = (List) sessionMap.get(name);
	if (list == null) {
	    list = new ArrayList();
	    sessionMap.put(name, list);
	}
	return list;
    }

    /**
     * Get ToolAccessMode from HttpRequest parameters. Default value is AUTHOR mode.
     *
     * @param request
     * @return
     */
    private ToolAccessMode getAccessMode(HttpServletRequest request) {
	ToolAccessMode mode;
	String modeStr = request.getParameter(AttributeNames.ATTR_MODE);
	if (StringUtils.equalsIgnoreCase(modeStr, ToolAccessMode.TEACHER.toString())) {
	    mode = ToolAccessMode.TEACHER;
	} else {
	    mode = ToolAccessMode.AUTHOR;
	}
	return mode;
    }

    /**
     * Forum validation method from STRUCT interface.
     *
     */
    public ActionMessages validate(ForumForm form, ActionMapping mapping,
	    javax.servlet.http.HttpServletRequest request) {
	ActionMessages errors = new ActionMessages();

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(form.getSessionMapID());
	ActionMessage ae;
	try {
	    String modeStr = request.getParameter(AttributeNames.ATTR_MODE);
	    // if (StringUtils.isBlank(form.getForum().getTitle())) {
	    // ActionMessage error = new ActionMessage("error.title.empty");
	    // errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	    // }
	    boolean allowNewTopic = form.getForum().isAllowNewTopic();
	    // define it later mode(TEACHER): need read out AllowNewTopic flag rather than get from HTML form
	    // becuase defineLater does not include this field
	    if (StringUtils.equals(modeStr, ToolAccessMode.TEACHER.toString())) {
		forumService = getForumManager();
		Forum forumPO = forumService.getForumByContentId(form.getToolContentID());
		if (forumPO != null) {
		    allowNewTopic = forumPO.isAllowNewTopic();
		} else {
		    // failure tolerance
		    AuthoringAction.log.error("ERROR: Can not found Forum by toolContentID:" + form.getToolContentID());
		    allowNewTopic = true;
		}
	    }
	    if (!allowNewTopic) {
		Set topics = getTopics(sessionMap);
		if (topics.size() == 0) {
		    ActionMessage error = new ActionMessage("error.must.have.topic");
		    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
		}
	    }
	    // define it later mode(TEACHER) skip below validation.
	    if (StringUtils.equals(modeStr, ToolAccessMode.TEACHER.toString())) {
		return errors;
	    }
	    if (!form.getForum().isAllowRichEditor() && form.getForum().isLimitedMaxCharacters()) {
		if (form.getForum().getMaxCharacters() <= 0) {
		    ActionMessage error = new ActionMessage("error.limit.char.less.zero");
		    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
		}
	    }if (!form.getForum().isAllowRichEditor()) {
		if (form.getForum().getMaxCharacters() != 0
			&& form.getForum().getMaxCharacters() < form.getForum().getMinCharacters()) {
		    ActionMessage error = new ActionMessage("error.min.post.char.less");
		    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
		}
	    }
	    if (form.getForum().isAllowRateMessages()) {
		if (form.getForum().getMaximumRate() <= 0) {
		    ActionMessage error = new ActionMessage("error.limit.char.less.zero");
		    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
		}
	    }if (form.getForum().isAllowRateMessages()) {
		if (form.getForum().getMaximumRate() != 0
			&& form.getForum().getMaximumRate() < form.getForum().getMinimumRate()) {
		    ActionMessage error = new ActionMessage("error.min.rate.less.max");
		    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
		}
	    }
	 
	    if (!form.getForum().isAllowNewTopic()) {
		if (form.getForum().getMaximumReply() != 0
			&& form.getForum().getMaximumReply() < form.getForum().getMinimumReply()) {
		    ActionMessage error = new ActionMessage("error.min.less.max");
		    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
		}
	    }
	    if (!form.getForum().isAllowNewTopic()) {
		if (form.getForum().getMaximumReply() <= 0) {
		    ActionMessage error = new ActionMessage("error.limit.char.less.zero");
		    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
		}
	    }

	} catch (Exception e) {
	    AuthoringAction.log.error(e.toString());
	}
	return errors;
    }

    private float convertToMeg(int numBytes) {
	return numBytes != 0 ? numBytes / 1024 / 1024 : 0;
    }

    /**
     * List containing Forum conditions.
     *
     * @param request
     * @return
     */
    private SortedSet<ForumCondition> getForumConditionSet(SessionMap sessionMap) {
	SortedSet<ForumCondition> list = (SortedSet<ForumCondition>) sessionMap.get(ForumConstants.ATTR_CONDITION_SET);
	if (list == null) {
	    list = new TreeSet<ForumCondition>(new TextSearchConditionComparator());
	    sessionMap.put(ForumConstants.ATTR_CONDITION_SET, list);
	}
	return list;
    }

    /**
     * Get the deleted condition list, which could be persisted or non-persisted items.
     *
     * @param request
     * @return
     */
    private List getDeletedForumConditionList(SessionMap sessionMap) {
	return getListFromSession(sessionMap, ForumConstants.ATTR_DELETED_CONDITION_LIST);
    }

    public ActionForward initPedagogicalPlannerForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ForumPedagogicalPlannerForm plannerForm = (ForumPedagogicalPlannerForm) form;
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Forum forum = getForumManager().getForumByContentId(toolContentID);
	plannerForm.fillForm(forum);
	String contentFolderId = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	plannerForm.setContentFolderID(contentFolderId);
	return mapping.findForward("success");

    }

    public ActionForward saveOrUpdatePedagogicalPlannerForm(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws IOException {
	ForumPedagogicalPlannerForm plannerForm = (ForumPedagogicalPlannerForm) form;
	ActionMessages errors = plannerForm.validate();
	if (errors.isEmpty()) {
	    Forum forum = getForumManager().getForumByContentId(plannerForm.getToolContentID());
	    forum.setInstructions(plannerForm.getInstructions());

	    int topicIndex = 0;
	    int sequenceId = 1;
	    Date currentDate = new Date();
	    String topic = null;
	    String subject = null;
	    Message message = null;
	    List<Message> newTopics = new LinkedList<Message>();
	    Set<Message> forumTopics = new TreeSet<Message>(new MessageComparator());
	    for (Message existingMessage : forum.getMessages()) {
		if (existingMessage.getIsAuthored() && existingMessage.getToolSession() == null) {
		    forumTopics.add(existingMessage);
		}
	    }
	    Iterator<Message> forumTopicIterator = forumTopics.iterator();
	    Pattern regexPattern = Pattern.compile(ForumConstants.WORD_REGEX, ForumConstants.PATTERN_MATCHING_OPTIONS);

	    Matcher matcher = null;
	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    ForumUser forumUser = getForumManager().getUserByID(new Long(user.getUserID().intValue()));

	    do {
		topic = plannerForm.getTopic(topicIndex);
		subject = WebUtil.removeHTMLtags(topic);

		// Getting 3 first words from body and making the subject out of it
		if (StringUtils.isBlank(subject)) {
		    subject = null;
		} else {
		    subject = subject.trim();
		    matcher = regexPattern.matcher(subject);
		    int currentEnd = subject.length();
		    for (short wordIndex = 0; wordIndex < ForumConstants.SUBJECT_WORD_COUNT; wordIndex++) {
			if (matcher.find()) {
			    currentEnd = matcher.end();
			} else {
			    break;
			}
		    }
		    subject = subject.substring(0, currentEnd).concat("...");
		}

		if (StringUtils.isEmpty(subject)) {
		    plannerForm.removeTopic(topicIndex);
		} else if (forumTopicIterator.hasNext()) {
		    message = forumTopicIterator.next();
		    message.setUpdated(currentDate);
		    message.setSubject(subject);
		    message.setBody(topic);
		    message.setSequenceId(sequenceId++);
		} else {
		    message = new Message();
		    message.setIsAuthored(true);
		    message.setCreated(currentDate);
		    message.setUpdated(currentDate);
		    message.setLastReplyDate(currentDate);
		    message.setCreatedBy(forumUser);
		    message.setModifiedBy(forumUser);
		    message.setSubject(subject);
		    message.setBody(topic);
		    message.setSequenceId(sequenceId++);

		    newTopics.add(message);
		    message.setForum(forum);
		    getForumManager().createRootTopic(forum.getUid(), null, message);
		}
		topicIndex++;
	    } while (topic != null);

	    while (forumTopicIterator.hasNext()) {
		message = forumTopicIterator.next();
		forumTopicIterator.remove();
		getForumManager().deleteTopic(message.getUid());
	    }
	    forum.getMessages().addAll(newTopics);
	    getForumManager().updateForum(forum);
	} else {
	    saveErrors(request, errors);
	}
	return mapping.findForward("success");
    }

    public ActionForward createPedagogicalPlannerTopic(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException, PersistenceException {
	ForumPedagogicalPlannerForm plannerForm = (ForumPedagogicalPlannerForm) form;
	plannerForm.setTopic(plannerForm.getTopicCount().intValue(), "");
	return mapping.findForward("success");
    }
}