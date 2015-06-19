/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
/* $$Id$$ */

package org.lamsfoundation.lams.tool.chat.service;

import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.rest.RestTags;
import org.lamsfoundation.lams.rest.ToolRestManager;
import org.lamsfoundation.lams.tool.ToolContentImport102Manager;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.chat.dao.IChatDAO;
import org.lamsfoundation.lams.tool.chat.dao.IChatMessageDAO;
import org.lamsfoundation.lams.tool.chat.dao.IChatSessionDAO;
import org.lamsfoundation.lams.tool.chat.dao.IChatUserDAO;
import org.lamsfoundation.lams.tool.chat.model.Chat;
import org.lamsfoundation.lams.tool.chat.model.ChatCondition;
import org.lamsfoundation.lams.tool.chat.model.ChatMessage;
import org.lamsfoundation.lams.tool.chat.model.ChatSession;
import org.lamsfoundation.lams.tool.chat.model.ChatUser;
import org.lamsfoundation.lams.tool.chat.util.ChatConstants;
import org.lamsfoundation.lams.tool.chat.util.ChatException;
import org.lamsfoundation.lams.tool.chat.util.ChatMessageFilter;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.util.wddx.WDDXProcessorConversionException;

/**
 * An implementation of the IChatService interface.
 * 
 * As a requirement, all LAMS tool's service bean must implement ToolContentManager and ToolSessionManager.
 */

public class ChatService implements ToolSessionManager, ToolContentManager, ToolContentImport102Manager, IChatService, ToolRestManager {

    private static Logger logger = Logger.getLogger(ChatService.class.getName());

    private IChatDAO chatDAO = null;

    private IChatSessionDAO chatSessionDAO = null;

    private IChatUserDAO chatUserDAO = null;

    private IChatMessageDAO chatMessageDAO = null;

    private ILearnerService learnerService;

    private ILamsToolService toolService;

    private IToolContentHandler chatToolContentHandler = null;

    private IAuditService auditService = null;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;

    private ChatOutputFactory chatOutputFactory;

    private Random generator = new Random();

    /* Methods from ToolSessionManager */
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	if (ChatService.logger.isDebugEnabled()) {
	    ChatService.logger.debug("entering method createToolSession:" + " toolSessionId = " + toolSessionId
		    + " toolSessionName = " + toolSessionName + " toolContentId = " + toolContentId);
	}

	ChatSession session = new ChatSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);

	Chat chat = chatDAO.getByContentId(toolContentId);
	session.setChat(chat);
	chatSessionDAO.saveOrUpdate(session);
    }

    @Override
    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {

	// TODO issues with session status/start date/ end date. Need to
	// reimplement method.

	// if (logger.isDebugEnabled()) {
	// logger.debug("entering method leaveToolSession:"
	// + " toolSessionId=" + toolSessionId + " learnerId="
	// + learnerId);
	// }
	//
	// if (toolSessionId == null) {
	// logger
	// .error("Fail to leave tool Session based on null tool session id.");
	// throw new ToolException(
	// "Fail to remove tool Session based on null tool session id.");
	// }
	// if (learnerId == null) {
	// logger.error("Fail to leave tool Session based on null learner.");
	// throw new ToolException(
	// "Fail to remove tool Session based on null learner.");
	// }
	//
	// ChatSession session = chatSessionDAO.getBySessionId(toolSessionId);
	// if (session != null) {
	// session.setStatus(ChatConstants.SESSION_COMPLETED);
	// chatSessionDAO.saveOrUpdate(session);
	// } else {
	// logger
	// .error("Fail to leave tool Session.Could not find submit file "
	// + "session by given session id: " + toolSessionId);
	// throw new DataMissingException(
	// "Fail to leave tool Session."
	// + "Could not find submit file session by given session id: "
	// + toolSessionId);
	// }
	return learnerService.completeToolSession(toolSessionId, learnerId);
    }

    @Override
    public ToolSessionExportOutputData exportToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public ToolSessionExportOutputData exportToolSession(List toolSessionIds) throws DataMissingException,
	    ToolException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	chatSessionDAO.deleteBySessionID(toolSessionId);
	// TODO check if cascade worked
    }

    @Override
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return getChatOutputFactory().getToolOutput(names, this, toolSessionId, learnerId);
    }

    @Override
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return getChatOutputFactory().getToolOutput(name, this, toolSessionId, learnerId);
    }
    
    @Override
    public void forceCompleteUser(Long toolSessionId, User user) {
	//no actions required
    }

    /* Methods from ToolContentManager */

    @Override
    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {

	if (ChatService.logger.isDebugEnabled()) {
	    ChatService.logger.debug("entering method copyToolContent:" + " fromContentId=" + fromContentId
		    + " toContentId=" + toContentId);
	}

	if (toContentId == null) {
	    String error = "Failed to copy tool content: toContentID is null";
	    throw new ToolException(error);
	}

	Chat fromContent = null;
	if (fromContentId != null) {
	    fromContent = chatDAO.getByContentId(fromContentId);
	}
	if (fromContent == null) {
	    // create the fromContent using the default tool content
	    fromContent = getDefaultContent();
	}
	Chat toContent = Chat.newInstance(fromContent, toContentId);
	chatDAO.saveOrUpdate(toContent);
    }
    
    @Override
    public void resetDefineLater(Long toolContentId) throws DataMissingException, ToolException {
	Chat chat = chatDAO.getByContentId(toolContentId);
	if (chat == null) {
	    throw new ToolException("Could not find tool with toolContentID: " + toolContentId);
	}
	chat.setDefineLater(false);
	chatDAO.saveOrUpdate(chat);
    }

    public void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException,
	    ToolException {
	// TODO Auto-generated method stub
    }

    @SuppressWarnings("unchecked")
    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (logger.isDebugEnabled()) {
	    logger.debug("Removing Chat messages for user ID " + userId + " and toolContentId " + toolContentId);
	}
	Chat chat = chatDAO.getByContentId(toolContentId);
	if (chat == null) {
	    logger.warn("Did not find activity with toolContentId: " + toolContentId + " to remove learner content");
	    return;
	}

	for (ChatSession session : (Set<ChatSession>) chat.getChatSessions()) {
	    ChatUser user = chatUserDAO.getByUserIdAndSessionId(userId.longValue(), session.getSessionId());
	    if (user != null) {
		List<ChatMessage> messages = chatMessageDAO.getSentByUser(user.getUid());
		if (!messages.isEmpty()) {
		    for (ChatMessage message : messages) {
			chatMessageDAO.delete(message);
			session.getChatMessages().remove(message);
		    }
		}

		NotebookEntry entry = getEntry(session.getSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL,
			ChatConstants.TOOL_SIGNATURE, userId);
		if (entry != null) {
		    chatDAO.delete(entry);
		}

		user.setFinishedActivity(false);
		user.setLastPresence(null);
		chatUserDAO.update(user);
	    }

	}
    }
    
    /**
     * Export the XML fragment for the tool's content, along with any files needed for the content.
     * 
     * @throws DataMissingException
     *             if no tool content matches the toolSessionId
     * @throws ToolException
     *             if any other error occurs
     */

    public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
	Chat chat = chatDAO.getByContentId(toolContentId);
	if (chat == null) {
	    chat = getDefaultContent();
	}
	if (chat == null) {
	    throw new DataMissingException("Unable to find default content for the chat tool");
	}

	// set ResourceToolContentHandler as null to avoid copy file node in
	// repository again.
	chat = Chat.newInstance(chat, toolContentId);
	chat.setChatSessions(null);
	try {
	    exportContentService.exportToolContent(toolContentId, chat, chatToolContentHandler, rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    /**
     * Import the XML fragment for the tool's content, along with any files needed for the content.
     * 
     * @throws ToolException
     *             if any other error occurs
     */
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {
	try {
	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(ChatImportContentVersionFilter.class);
	
	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, chatToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof Chat)) {
		throw new ImportToolContentException("Import Chat tool content failed. Deserialized object is "
			+ toolPOJO);
	    }
	    Chat chat = (Chat) toolPOJO;

	    // reset it to new toolContentId
	    chat.setToolContentId(toolContentId);
	    chat.setCreateBy(new Long(newUserUid.longValue()));

	    chatDAO.saveOrUpdate(chat);
	} catch (ImportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    /**
     * Get the definitions for possible output for an activity, based on the toolContentId. These may be definitions
     * that are always available for the tool (e.g. number of marks for Multiple Choice) or a custom definition created
     * for a particular activity such as the answer to the third question contains the word Koala and hence the need for
     * the toolContentId
     * 
     * @return SortedMap of ToolOutputDefinitions with the key being the name of each definition
     */
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	Chat chat = getChatDAO().getByContentId(toolContentId);

	if (chat == null) {
	    chat = getDefaultContent();
	}

	return getChatOutputFactory().getToolOutputDefinitions(chat, definitionType);
    }

    public String getToolContentTitle(Long toolContentId) {
	return getChatByContentId(toolContentId).getTitle();
    }
    
    public boolean isContentEdited(Long toolContentId) {
	return getChatByContentId(toolContentId).isDefineLater();
    }

    /* IChatService Methods */
    public Long getDefaultContentIdBySignature(String toolSignature) {
	Long toolContentId = null;
	toolContentId = new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));
	if (toolContentId == null) {
	    String error = "Could not retrieve default content id for this tool";
	    ChatService.logger.error(error);
	    throw new ChatException(error);
	}
	return toolContentId;
    }

    public Chat getDefaultContent() {
	Long defaultContentID = getDefaultContentIdBySignature(ChatConstants.TOOL_SIGNATURE);
	Chat defaultContent = getChatByContentId(defaultContentID);
	if (defaultContent == null) {
	    String error = "Could not retrieve default content record for this tool";
	    ChatService.logger.error(error);
	    throw new ChatException(error);
	}
	if (defaultContent.getConditions().isEmpty()) {
	    defaultContent.getConditions().add(
		    getChatOutputFactory().createDefaultUserMessagesCondition(defaultContent));
	}
	return defaultContent;
    }

    public Chat copyDefaultContent(Long newContentID) {

	if (newContentID == null) {
	    String error = "Cannot copy the Chat tools default content: + " + "newContentID is null";
	    ChatService.logger.error(error);
	    throw new ChatException(error);
	}

	Chat defaultContent = getDefaultContent();
	// create new chat using the newContentID
	Chat newContent = new Chat();
	newContent = Chat.newInstance(defaultContent, newContentID);
	chatDAO.saveOrUpdate(newContent);
	return newContent;
    }

    public Chat getChatByContentId(Long toolContentID) {
	Chat chat = chatDAO.getByContentId(toolContentID);
	if (chat == null) {
	    ChatService.logger.debug("Could not find the content with toolContentID:" + toolContentID);
	}
	return chat;
    }

    public ChatSession getSessionBySessionId(Long toolSessionId) {
	ChatSession chatSession = chatSessionDAO.getBySessionId(toolSessionId);
	if (chatSession == null) {
	    ChatService.logger.debug("Could not find the chat session with toolSessionID:" + toolSessionId);
	}
	return chatSession;
    }

    public List<ChatUser> getUsersActiveBySessionId(Long toolSessionId) {
	Date oldestLastPresence = new Date(System.currentTimeMillis() - ChatConstants.PRESENCE_IDLE_TIMEOUT);
	return chatUserDAO.getBySessionIdAndLastPresence(toolSessionId, oldestLastPresence);
    }

    public ChatUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId) {
	return chatUserDAO.getByUserIdAndSessionId(userId, toolSessionId);
    }

    public ChatUser getUserByLoginNameAndSessionId(String loginName, Long toolSessionId) {
	return chatUserDAO.getByLoginNameAndSessionId(loginName, toolSessionId);
    }

    public ChatUser getUserByUID(Long uid) {
	return chatUserDAO.getByUID(uid);
    }

    public ChatUser getUserByNicknameAndSessionID(String nickname, Long sessionID) {
	return chatUserDAO.getByNicknameAndSessionID(nickname, sessionID);
    }

    /**
     * Stores information when users with given UIDs were last seen in their Chat session.
     */
    public void updateUserPresence(Map<Long, Date> presence) {
	for (Long userUid : presence.keySet()) {
	    ChatUser chatUser = chatUserDAO.getByUID(userUid);
	    chatUser.setLastPresence(presence.get(userUid));
	    saveOrUpdateChatUser(chatUser);
	}
    }

    public List<ChatMessage> getMessagesForUser(ChatUser chatUser) {
	return chatMessageDAO.getForUser(chatUser);
    }

    /**
     * {@inheritDoc}
     */
    public List<ChatMessage> getMessagesSentByUser(Long userUid) {
	return chatMessageDAO.getSentByUser(userUid);
    }

    public void saveOrUpdateChat(Chat chat) {
	updateMessageFilters(chat);
	chatDAO.saveOrUpdate(chat);
    }

    public void saveOrUpdateChatSession(ChatSession chatSession) {
	chatSessionDAO.saveOrUpdate(chatSession);
    }

    public void saveOrUpdateChatUser(ChatUser chatUser) {
	chatUserDAO.saveOrUpdate(chatUser);
    }

    public void saveOrUpdateChatMessage(ChatMessage chatMessage) {
	chatMessageDAO.saveOrUpdate(chatMessage);
    }

    public synchronized ChatUser createChatUser(UserDTO user, ChatSession chatSession) {
	ChatUser chatUser = new ChatUser(user, chatSession);

	chatUser.setNickname(createNickname(chatUser));
	saveOrUpdateChatUser(chatUser);
	return chatUser;
    }

    private String createNickname(ChatUser chatUser) {
	String desiredNickname = chatUser.getFirstName() + " " + chatUser.getLastName();
	String nickname = desiredNickname;

	boolean valid = false;
	int count = 1;

	// TODO may need max tries to prevent possibly entering infinite loop.
	while (!valid) {
	    if (getUserByNicknameAndSessionID(nickname, chatUser.getChatSession().getSessionId()) == null) {
		// nickname is available
		valid = true;
	    } else {
		nickname = desiredNickname + " " + count;
		count++;
	    }
	}

	return nickname;
    }

    public String filterMessage(String message, Chat chat) {
	Pattern pattern = getFilterPattern(chat);

	if (pattern == null) {
	    return message;
	}

	Matcher matcher = pattern.matcher(message);
	return matcher.replaceAll(ChatConstants.FILTER_REPLACE_TEXT);
    }

    private Pattern getFilterPattern(Chat chat) {
	if (!chat.isFilteringEnabled()) {
	    return null;
	}

	// get the filter
	ChatMessageFilter filter = messageFilters.get(chat.getToolContentId());
	if (filter == null) {
	    // this is the first message we have see for this toolContentId
	    // update the available filters.
	    filter = updateMessageFilters(chat);
	}

	// get the pattern
	Pattern pattern = filter.getPattern();
	if (pattern == null) {
	    // no pattern available. This occurs when filtering is enabled but
	    // no valid keywords have been defined.
	    return null;
	}
	return pattern;
    }

    public ChatMessageFilter updateMessageFilters(Chat chat) {
	ChatMessageFilter filter = new ChatMessageFilter(chat);
	messageFilters.put(chat.getToolContentId(), filter);
	return filter;
    }

    public ChatMessage getMessageByUID(Long messageUID) {
	return chatMessageDAO.getByUID(messageUID);
    }

    public List getLastestMessages(ChatSession chatSession, int max) {
	return chatMessageDAO.getLatest(chatSession, max);
    }

    public IAuditService getAuditService() {
	return auditService;
    }

    public void setAuditService(IAuditService auditService) {
	this.auditService = auditService;
    }

    public void auditEditMessage(ChatMessage chatMessage, String messageBody) {
	auditService.logChange(ChatConstants.TOOL_SIGNATURE, chatMessage.getFromUser().getUserId(), chatMessage
		.getFromUser().getLoginName(), chatMessage.getBody(), messageBody);
    }

    public void auditHideShowMessage(ChatMessage chatMessage, boolean messageHidden) {
	if (messageHidden) {
	    auditService.logHideEntry(ChatConstants.TOOL_SIGNATURE, chatMessage.getFromUser().getUserId(), chatMessage
		    .getFromUser().getLoginName(), chatMessage.toString());
	} else {
	    auditService.logShowEntry(ChatConstants.TOOL_SIGNATURE, chatMessage.getFromUser().getUserId(), chatMessage
		    .getFromUser().getLoginName(), chatMessage.toString());
	}
    }

    public boolean isGroupedActivity(long toolContentID) {
	return toolService.isGroupedActivity(toolContentID);
    }

    /* Private methods */
    private Map<Long, ChatMessageFilter> messageFilters = new ConcurrentHashMap<Long, ChatMessageFilter>();

    public IChatDAO getChatDAO() {
	return chatDAO;
    }

    public void setChatDAO(IChatDAO chatDAO) {
	this.chatDAO = chatDAO;
    }

    public IToolContentHandler getChatToolContentHandler() {
	return chatToolContentHandler;
    }

    public void setChatToolContentHandler(IToolContentHandler chatToolContentHandler) {
	this.chatToolContentHandler = chatToolContentHandler;
    }

    public IChatSessionDAO getChatSessionDAO() {
	return chatSessionDAO;
    }

    public void setChatSessionDAO(IChatSessionDAO sessionDAO) {
	chatSessionDAO = sessionDAO;
    }

    public ILamsToolService getToolService() {
	return toolService;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    public IChatUserDAO getChatUserDAO() {
	return chatUserDAO;
    }

    public void setChatUserDAO(IChatUserDAO userDAO) {
	chatUserDAO = userDAO;
    }

    public IChatMessageDAO getChatMessageDAO() {
	return chatMessageDAO;
    }

    public void setChatMessageDAO(IChatMessageDAO messageDAO) {
	chatMessageDAO = messageDAO;
    }

    public ILearnerService getLearnerService() {
	return learnerService;
    }

    public void setLearnerService(ILearnerService learnerService) {
	this.learnerService = learnerService;
    }

    public IExportToolContentService getExportContentService() {
	return exportContentService;
    }

    public void setExportContentService(IExportToolContentService exportContentService) {
	this.exportContentService = exportContentService;
    }

    public Map<Long, Integer> getMessageCountBySession(Long chatUID) {
	return chatMessageDAO.getCountBySession(chatUID);
    }

    public Map<Long, Integer> getMessageCountByFromUser(Long sessionUID) {
	return chatMessageDAO.getCountByFromUser(sessionUID);
    }

    public ICoreNotebookService getCoreNotebookService() {
	return coreNotebookService;
    }

    public void setCoreNotebookService(ICoreNotebookService coreNotebookService) {
	this.coreNotebookService = coreNotebookService;
    }

    public Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry) {
	return coreNotebookService.createNotebookEntry(id, idType, signature, userID, "", entry);
    }

    public NotebookEntry getEntry(Long id, Integer idType, String signature, Integer userID) {

	List<NotebookEntry> list = coreNotebookService.getEntry(id, idType, signature, userID);
	if ((list == null) || list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    /**
     * @param notebookEntry
     */
    public void updateEntry(NotebookEntry notebookEntry) {
	coreNotebookService.updateEntry(notebookEntry);
    }

    /*
     * ===============Methods implemented from ToolContentImport102Manager ===============
     */

    /**
     * Import the data for a 1.0.2 Chat
     */
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues) {
	Date now = new Date();
	Chat chat = new Chat();
	chat.setContentInUse(Boolean.FALSE);
	chat.setCreateBy(new Long(user.getUserID().longValue()));
	chat.setCreateDate(now);
	chat.setDefineLater(Boolean.FALSE);
	chat.setFilterKeywords(null);
	chat.setFilteringEnabled(Boolean.FALSE);
	chat.setInstructions(WebUtil.convertNewlines((String) importValues
		.get(ToolContentImport102Manager.CONTENT_BODY)));
	chat.setLockOnFinished(Boolean.FALSE);
	chat.setReflectInstructions(null);
	chat.setReflectOnActivity(Boolean.FALSE);
	chat.setTitle((String) importValues.get(ToolContentImport102Manager.CONTENT_TITLE));
	chat.setToolContentId(toolContentId);
	chat.setUpdateDate(now);

	try {
	    Boolean isReusable = WDDXProcessor.convertToBoolean(importValues,
		    ToolContentImport102Manager.CONTENT_REUSABLE);
	    chat.setLockOnFinished(isReusable != null ? !isReusable.booleanValue() : true);
	} catch (WDDXProcessorConversionException e) {
	    ChatService.logger.error("Unable to content for activity " + chat.getTitle()
		    + "properly due to a WDDXProcessorConversionException.", e);
	    throw new ToolException(
		    "Invalid import data format for activity "
			    + chat.getTitle()
			    + "- WDDX caused an exception. Some data from the design will have been lost. See log for more details.");
	}

	chatDAO.saveOrUpdate(chat);
    }

    /**
     * Set the description, throws away the title value as this is not supported in 2.0
     */
    public void setReflectiveData(Long toolContentId, String title, String description) throws ToolException,
	    DataMissingException {

	Chat chat = getChatByContentId(toolContentId);
	if (chat == null) {
	    throw new DataMissingException("Unable to set reflective data titled " + title
		    + " on activity toolContentId " + toolContentId + " as the tool content does not exist.");
	}

	chat.setReflectOnActivity(Boolean.TRUE);
	chat.setReflectInstructions(description);
    }

    public ChatOutputFactory getChatOutputFactory() {
	return chatOutputFactory;
    }

    public void setChatOutputFactory(ChatOutputFactory notebookOutputFactory) {
	chatOutputFactory = notebookOutputFactory;
    }

    public String createConditionName(Collection<ChatCondition> existingConditions) {
	String uniqueNumber = null;
	do {
	    uniqueNumber = String.valueOf(Math.abs(generator.nextInt()));
	    for (ChatCondition condition : existingConditions) {
		String[] splitedName = getChatOutputFactory().splitConditionName(condition.getName());
		if (uniqueNumber.equals(splitedName[1])) {
		    uniqueNumber = null;
		}
	    }
	} while (uniqueNumber == null);
	return getChatOutputFactory().buildUserMessagesConditionName(uniqueNumber);
    }

    public void deleteCondition(ChatCondition condition) {
	if ((condition != null) && (condition.getConditionId() != null)) {
	    chatDAO.delete(condition);
	}
    }

    public void releaseConditionsFromCache(Chat chat) {
	if (chat.getConditions() != null) {
	    for (ChatCondition condition : chat.getConditions()) {
		getChatDAO().releaseFromCache(condition);
	    }
	}
    }

    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getChatOutputFactory().getSupportedDefinitionClasses(definitionType);
    }
    // =========================================================================================
    
    // ****************** REST methods *************************

    /** Used by the Rest calls to create content. 
     * Mandatory fields in toolContentJSON: title, instructions
     * Optional fields reflectInstructions, lockWhenFinished, filterKeywords
     */
    @Override
    public void createRestToolContent(Integer userID, Long toolContentID, JSONObject toolContentJSON) throws JSONException {

	Chat content = new Chat();
	Date updateDate = new Date();
	content.setCreateDate(updateDate);
	content.setUpdateDate(updateDate);
	content.setToolContentId(toolContentID);
	content.setTitle(toolContentJSON.getString(RestTags.TITLE));
	content.setInstructions(toolContentJSON.getString(RestTags.INSTRUCTIONS));
	content.setCreateBy(userID.longValue());
	
	content.setContentInUse(false);
	content.setDefineLater(false);
	content.setReflectInstructions((String) JsonUtil.opt(toolContentJSON, RestTags.REFLECT_INSTRUCTIONS, null));
	content.setReflectOnActivity(JsonUtil.opt(toolContentJSON, RestTags.REFLECT_ON_ACTIVITY, Boolean.FALSE));
	content.setLockOnFinished(JsonUtil.opt(toolContentJSON, RestTags.LOCK_WHEN_FINISHED, Boolean.FALSE));
	
	String filterKeywords = JsonUtil.opt(toolContentJSON, "filterKeywords", null);
	content.setFilteringEnabled(filterKeywords != null && filterKeywords.length()>0);
	content.setFilterKeywords(filterKeywords);
	// submissionDeadline is set in monitoring

	saveOrUpdateChat(content);

    }
}
