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

package org.lamsfoundation.lams.tool.forum.web.controller;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.TextSearchConditionComparator;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.forum.ForumConstants;
import org.lamsfoundation.lams.tool.forum.dto.MessageDTO;
import org.lamsfoundation.lams.tool.forum.model.Attachment;
import org.lamsfoundation.lams.tool.forum.model.Forum;
import org.lamsfoundation.lams.tool.forum.model.ForumCondition;
import org.lamsfoundation.lams.tool.forum.model.ForumToolSession;
import org.lamsfoundation.lams.tool.forum.model.ForumUser;
import org.lamsfoundation.lams.tool.forum.model.Message;
import org.lamsfoundation.lams.tool.forum.service.IForumService;
import org.lamsfoundation.lams.tool.forum.util.MessageDtoComparator;
import org.lamsfoundation.lams.tool.forum.util.PersistenceException;
import org.lamsfoundation.lams.tool.forum.web.forms.ForumForm;
import org.lamsfoundation.lams.tool.forum.web.forms.MessageForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Steve.Ni
 */
@Controller
@RequestMapping("/authoring")
public class AuthoringController {
    private static Logger log = Logger.getLogger(AuthoringController.class);

    @Autowired
    private IForumService forumService;

    @Autowired
    @Qualifier("forumMessageService")
    private MessageService messageService;

    // ******************************************************************************************************************
    // Forum Author functions
    // ******************************************************************************************************************

    /**
     * This page will display initial submit tool content. Or just a blank page if the toolContentID does not exist
     * before.
     */
    @RequestMapping("/authoring")
    public String initPage(@ModelAttribute ForumForm forumForm, HttpServletRequest request) {
	request.setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.AUTHOR);
	return readDatabaseData(forumForm, request);
    }

    @RequestMapping(path = "/definelater", method = RequestMethod.POST)
    public String defineLater(@ModelAttribute ForumForm forumForm, HttpServletRequest request) {

	// update define later flag to true
	request.setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER);

	Long contentId = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
	Forum forum = forumService.getForumByContentId(contentId);

	if (!forum.isContentInUse()) {
	    forum.setDefineLater(true);
	    forumService.updateForum(forum);

	    // audit log the teacher has started editing activity in monitor
	    forumService.auditLogStartEditingActivityInMonitor(contentId);
	}

	return readDatabaseData(forumForm, request);
    }

    /**
     * Common method for "start" and "defineLater"
     */
    private String readDatabaseData(ForumForm forumForm, HttpServletRequest request) {
	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	forumForm.setSessionMapID(sessionMap.getSessionID());
	forumForm.setContentFolderID(contentFolderID);
	sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);

	// get back the topic list and display them on page
	List<MessageDTO> topics = null;
	Forum forum = null;
	try {
	    forum = forumService.getForumByContentId(contentId);
	    // if forum does not exist, try to use default content instead.
	    if (forum == null) {
		forum = forumService.getDefaultContent(contentId);
		if (forum.getMessages() != null) {
		    List<Message> list = new ArrayList<>();
		    // sorted by create date
		    Iterator<Message> iter = forum.getMessages().iterator();
		    while (iter.hasNext()) {
			Message topic = iter.next();
			// contentFolderID != -1 means it is appadmin: LDEV-906
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
		// contentFolderID != -1 means it is appadmin: LDEV-906
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
	    AuthoringController.log.error(e);
	    return "error";
	}

	// init it to avoid null exception in following handling
	if (topics == null) {
	    topics = new ArrayList<>();
	}

	Set<MessageDTO> topicSet = new TreeSet<>(new MessageDtoComparator());
	topicSet.addAll(topics);
	sessionMap.put(ForumConstants.AUTHORING_TOPICS_LIST, topicSet);

	// init condition set
	SortedSet<ForumCondition> conditionSet = getForumConditionSet(sessionMap);
	conditionSet.clear();
	conditionSet.addAll(forum.getConditions());

	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());

	return "jsps/authoring/authoring";
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
     */
    @RequestMapping(path = "/update", method = RequestMethod.POST)
    public String updateContent(@ModelAttribute ForumForm forumForm, HttpServletRequest request)
	    throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(forumForm.getSessionMapID());
	// validation
	MultiValueMap<String, String> errorMap = validate(forumForm, request);
	if (!errorMap.isEmpty()) {
	    request.setAttribute("errorMap", errorMap);
	    return "jsps/authoring/authoring";
	}

	Forum forum = forumForm.getForum();
	// get back tool content ID
	forum.setContentId(forumForm.getToolContentID());

	// *******************************Handle user*******************
	String contentFolderID = (String) sessionMap.get(AttributeNames.PARAM_CONTENT_FOLDER_ID);
	ForumUser forumUser = null;
	// check whether it is appadmin:LDEV-906
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
	    Long uid = forumPO.getUid();
	    PropertyUtils.copyProperties(forumPO, forum);
	    // get back UID
	    forumPO.setUid(uid);

	    // if it's a teacher - change define later status
	    if (mode.isTeacher()) {
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

	request.setAttribute(CommonConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);
	return "jsps/authoring/authoring";
    }

    // ******************************************************************************************************************
    // Topic functions
    // ******************************************************************************************************************
    /**
     * Display emtpy topic page for user input topic information. This page will contain all topics list which this
     * author posted before.
     */
    @RequestMapping("/newTopic")
    public String newTopic(@ModelAttribute("topicFormId") MessageForm topicFormId, HttpServletRequest request) {
	String sessionMapID = WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID);
	topicFormId.setSessionMapID(sessionMapID);
	topicFormId.setTmpFileUploadId(FileUtil.generateTmpFileUploadId());

	return "jsps/authoring/message/create";
    }

    /**
     * Create a topic in memory. This topic will be saved when user save entire authoring page.
     */
    @RequestMapping(path = "/createTopic", method = RequestMethod.POST)
    public String createTopic(@ModelAttribute("topicFormId") MessageForm messageForm, HttpServletRequest request)
	    throws IOException, ServletException, PersistenceException {
	//validate form
	MultiValueMap<String, String> errorMap = messageForm.validate(request, messageService);
	if (!errorMap.isEmpty()) {
	    messageForm.setTmpFileUploadId(FileUtil.generateTmpFileUploadId());
	    request.setAttribute("errorMap", errorMap);
	    return "jsps/authoring/message/create";
	}

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(messageForm.getSessionMapID());
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
	// check whether it is appadmin:LDEV-906
	if (forumUser == null && !StringUtils.equals(contentFolderID, "-1")) {
	    // if user not exist, create new one in database
	    forumUser = new ForumUser(user, null);
	}
	message.setCreatedBy(forumUser);
	// same person with create at first time
	message.setModifiedBy(forumUser);

	// set attachment of this topic
	Set<Attachment> attSet = setupAttachmentSet(messageForm, message);
	message.setAttachments(attSet);

	// LDEV-4696 no longer needed - cannot edit in monitoring once a session is created.
	// create clones of this topic (appropriate only for editing in monitoring)
	Forum forum = (Forum) sessionMap.get(ForumConstants.AUTHORING_FORUM);
	if (forum != null) {
	    List<ForumToolSession> toolSessions = forumService.getSessionsByContentId(forum.getContentId());
	    for (ForumToolSession toolSession : toolSessions) {
		Message newMsg = Message.newInstance(message);
		newMsg.setToolSession(toolSession);
		newMsg.setAttachments(new TreeSet<Attachment>());
		newMsg.setModifiedBy(null);
		newMsg.setCreatedBy(null);
		message.getSessionClones().add(newMsg);
	    }
	}

	topics.add(MessageDTO.getMessageDTO(message));

	return "jsps/authoring/message/topiclist";
    }

    /**
     * Delete a topic form current topic list. But database record will be deleted only when user save whole authoring
     * page.
     */
    @RequestMapping(path = "/deleteTopic", method = RequestMethod.POST)
    public String deleteTopic(HttpServletRequest request) throws PersistenceException {

	// get SessionMAP
	String sessionMapID = WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
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

	return "jsps/authoring/message/topiclist";
    }

    /**
     * Display a HTML FORM which contains subject, body and attachment information from a special topic. This page is
     * ready for user update this topic.
     */
    @RequestMapping("/editTopic")
    public String editTopic(@ModelAttribute("topicFormId") MessageForm topicFormId, HttpServletRequest request)
	    throws PersistenceException {

	// get SessionMAP
	String sessionMapID = WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	topicFormId.setSessionMapID(sessionMapID);

	String topicIndex = request.getParameter(ForumConstants.AUTHORING_TOPICS_INDEX);
	int topicIdx = NumberUtils.stringToInt(topicIndex, -1);
	if (topicIdx != -1) {
	    Set topics = getTopics(sessionMap);
	    List<MessageDTO> rList = new ArrayList<MessageDTO>(topics);
	    MessageDTO topic = rList.get(topicIdx);
	    if (topic != null) {
		// update message to HTML Form to echo back to web page: for subject, body display
		topicFormId.setMessage(topic.getMessage());
	    }
	    // echo back to web page: for attachment display
	    request.setAttribute(ForumConstants.AUTHORING_TOPIC, topic);
	}

	topicFormId.setTmpFileUploadId(FileUtil.generateTmpFileUploadId());

	request.setAttribute(ForumConstants.AUTHORING_TOPICS_INDEX, topicIndex);
	return "jsps/authoring/message/edit";
    }

    /**
     * Submit user updated inforamion in a topic to memory. This update will be submit to database only when user save
     * whole authoring page.
     *
     * @throws ServletException
     */
    @RequestMapping(path = "/updateTopic", method = RequestMethod.POST)
    public String updateTopic(@ModelAttribute("topicFormId") MessageForm messageForm, HttpServletRequest request)
	    throws PersistenceException, ServletException {
	//validate form
	MultiValueMap<String, String> errorMap = messageForm.validate(request, messageService);
	if (!errorMap.isEmpty()) {
	    messageForm.setTmpFileUploadId(FileUtil.generateTmpFileUploadId());
	    request.setAttribute("errorMap", errorMap);
	    return "jsps/authoring/message/edit";
	}

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(messageForm.getSessionMapID());
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
	    if (!messageForm.isHasAttachment()) {
		Set oldAttachments = newMsg.getMessage().getAttachments();
		if (oldAttachments != null && oldAttachments.size() > 0) {
		    List delTopicAtt = getTopicDeletedAttachmentList(sessionMap);
		    delTopicAtt.add(oldAttachments.iterator().next());
		}
		Set attSet = setupAttachmentSet(messageForm, newMsg.getMessage());
		newMsg.getMessage().setAttachments(attSet);
		newMsg.setHasAttachment(!attSet.isEmpty());
	    }
	}

	request.setAttribute(ForumConstants.AUTHORING_TOPICS_INDEX, topicIndex);
	return "jsps/authoring/message/topiclist";
    }

    /* only allow one attachment, so replace whatever */
    private Set<Attachment> setupAttachmentSet(MessageForm messageForm, Message msg) throws ServletException {
	Set<Attachment> attSet = new HashSet<>();

	File uploadDir = FileUtil.getTmpFileUploadDir(messageForm.getTmpFileUploadId());
	if (uploadDir.canRead()) {
	    File[] files = uploadDir.listFiles();
	    if (files.length > 1) {
		throw new ServletException("Uploaded more than 1 file");
	    }

	    if (files.length == 1) {

		File file = files[0];
		Attachment att = forumService.uploadAttachment(file);
		attSet.add(att);
		att.setMessage(msg);

		FileUtil.deleteTmpFileUploadDir(messageForm.getTmpFileUploadId());
	    }
	}

	return attSet;
    }

    /**
     * Remove message attachment.
     */
    @RequestMapping(path = "/deleteAttachment", method = RequestMethod.POST)
    public String deleteAttachment(HttpServletRequest request) {
	request.setAttribute("itemAttachment", null);
	request.setAttribute("tmpFileUploadId", FileUtil.generateTmpFileUploadId());
	return "jsps/authoring/parts/msgattachment";
    }

    /**
     * Move up current topic.
     */
    @RequestMapping("/upTopic")
    public String upTopic(HttpServletRequest request) {
	return switchTopic(request, true);
    }

    /**
     * Move down current topic.
     */
    @RequestMapping("/downTopic")
    public String downTopic(HttpServletRequest request) {
	return switchTopic(request, false);
    }

    private String switchTopic(HttpServletRequest request, boolean up) {
	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

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
	return "jsps/authoring/message/topiclist";
    }

    // ******************************************************************************************************************
    // Private method for internal functions
    // ******************************************************************************************************************

    private SortedSet<MessageDTO> getTopics(SessionMap<String, Object> sessionMap) {
	SortedSet<MessageDTO> topics = (SortedSet<MessageDTO>) sessionMap.get(ForumConstants.AUTHORING_TOPICS_LIST);
	if (topics == null) {
	    topics = new TreeSet<>(new MessageDtoComparator());
	    sessionMap.put(ForumConstants.AUTHORING_TOPICS_LIST, topics);
	}
	return topics;
    }

    private List getTopicDeletedAttachmentList(SessionMap<String, Object> sessionMap) {
	return getListFromSession(sessionMap, ForumConstants.DELETED_ATTACHMENT_LIST);
    }

    private List getDeletedTopicList(SessionMap<String, Object> sessionMap) {
	return getListFromSession(sessionMap, ForumConstants.DELETED_AUTHORING_TOPICS_LIST);
    }

    /**
     * Get <code>java.util.List</code> from HttpSession by given name.
     */
    private List getListFromSession(SessionMap<String, Object> sessionMap, String name) {
	List list = (List) sessionMap.get(name);
	if (list == null) {
	    list = new ArrayList();
	    sessionMap.put(name, list);
	}
	return list;
    }

    /**
     * Forum validation method from STRUCT interface.
     *
     */
    public MultiValueMap<String, String> validate(ForumForm form, HttpServletRequest request) {

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(form.getSessionMapID());
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
		Forum forumPO = forumService.getForumByContentId(form.getToolContentID());
		if (forumPO != null) {
		    allowNewTopic = forumPO.isAllowNewTopic();
		} else {
		    // failure tolerance
		    AuthoringController.log
			    .error("ERROR: Can not found Forum by toolContentID:" + form.getToolContentID());
		    allowNewTopic = true;
		}
	    }
	    if (!allowNewTopic) {
		Set topics = getTopics(sessionMap);
		if (topics.size() == 0) {
		    errorMap.add("GLOBAL", messageService.getMessage("error.must.have.topic"));
		}
	    }
	    // define it later mode(TEACHER) skip below validation.
	    if (StringUtils.equals(modeStr, ToolAccessMode.TEACHER.toString())) {
		return errorMap;
	    }
	    if (!form.getForum().isAllowRichEditor() && form.getForum().isLimitedMaxCharacters()) {
		if (form.getForum().getMaxCharacters() <= 0) {
		    errorMap.add("GLOBAL", messageService.getMessage("error.limit.char.less.zero"));
		}
	    }
	    if (!form.getForum().isAllowRichEditor()) {
		if (form.getForum().getMaxCharacters() != 0
			&& form.getForum().getMaxCharacters() < form.getForum().getMinCharacters()) {
		    errorMap.add("GLOBAL", messageService.getMessage("error.min.post.char.less"));
		}
	    }
	    if (form.getForum().isAllowRateMessages()) {
		if (form.getForum().getMaximumRate() != 0
			&& form.getForum().getMaximumRate() < form.getForum().getMinimumRate()) {
		    errorMap.add("GLOBAL", messageService.getMessage("error.min.rate.less.max"));
		}
	    }

	    if (!form.getForum().isAllowNewTopic()) {
		if (form.getForum().getMaximumReply() != 0
			&& form.getForum().getMaximumReply() < form.getForum().getMinimumReply()) {
		    errorMap.add("GLOBAL", messageService.getMessage("error.min.less.max"));
		}
	    }

	} catch (Exception e) {
	    AuthoringController.log.error(e.toString());
	}
	return errorMap;
    }

    /**
     * List containing Forum conditions.
     */
    private SortedSet<ForumCondition> getForumConditionSet(SessionMap<String, Object> sessionMap) {
	SortedSet<ForumCondition> list = (SortedSet<ForumCondition>) sessionMap.get(ForumConstants.ATTR_CONDITION_SET);
	if (list == null) {
	    list = new TreeSet<>(new TextSearchConditionComparator());
	    sessionMap.put(ForumConstants.ATTR_CONDITION_SET, list);
	}
	return list;
    }

    /**
     * Get the deleted condition list, which could be persisted or non-persisted items.
     */
    private List getDeletedForumConditionList(SessionMap<String, Object> sessionMap) {
	return getListFromSession(sessionMap, ForumConstants.ATTR_DELETED_CONDITION_LIST);
    }
}