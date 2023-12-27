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

package org.lamsfoundation.lams.tool.chat.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.confidencelevel.ConfidenceLevelDTO;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.rest.RestTags;
import org.lamsfoundation.lams.rest.ToolRestManager;
import org.lamsfoundation.lams.tool.ToolCompletionStatus;
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
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.JsonUtil;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * An implementation of the IChatService interface.
 *
 * As a requirement, all LAMS tool's service bean must implement ToolContentManager and ToolSessionManager.
 */

public class ChatService implements ToolSessionManager, ToolContentManager, IChatService, ToolRestManager {

    private static Logger logger = Logger.getLogger(ChatService.class.getName());

    private IChatDAO chatDAO = null;

    private IChatSessionDAO chatSessionDAO = null;

    private IChatUserDAO chatUserDAO = null;

    private IChatMessageDAO chatMessageDAO = null;

    private ILamsToolService toolService;

    private IToolContentHandler chatToolContentHandler = null;

    private ILogEventService logEventService = null;

    private IExportToolContentService exportContentService;

    private ChatOutputFactory chatOutputFactory;

    private Random generator = new Random();

    /* Methods from ToolSessionManager */
    @Override
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
	return toolService.completeToolSession(toolSessionId, learnerId);
    }
    
    @Override
    public String finishToolSession(Long userUid) {

	// set the finished flag
	ChatUser chatUser = getUserByUID(userUid);
	if (chatUser != null) {
	    chatUser.setFinishedActivity(true);
	    saveOrUpdateChatUser(chatUser);
	}

	return leaveToolSession(chatUser.getChatSession().getSessionId(), chatUser.getUserId());
    }

    @Override
    public ToolSessionExportOutputData exportToolSession(Long toolSessionId)
	    throws DataMissingException, ToolException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public int getTopicsNum(Long userID, Long sessionId) {
	return chatMessageDAO.getTopicsNum(userID, sessionId);
    }

    @Override
    public ToolSessionExportOutputData exportToolSession(List toolSessionIds)
	    throws DataMissingException, ToolException {
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
    public List<ToolOutput> getToolOutputs(String name, Long toolContentId) {
	return new ArrayList<>();
    }

    @Override
    public List<ConfidenceLevelDTO> getConfidenceLevels(Long toolSessionId) {
	return null;
    }
    
    @Override
    public boolean isUserGroupLeader(Long userId, Long toolSessionId) {
	return false;
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

    @SuppressWarnings("unchecked")
    @Override
    public void removeToolContent(Long toolContentId) throws ToolException {
	Chat chat = chatDAO.getByContentId(toolContentId);
	if (chat == null) {
	    ChatService.logger.warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}

	chatDAO.delete(chat);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (ChatService.logger.isDebugEnabled()) {
	    ChatService.logger
		    .debug("Removing Chat messages for user ID " + userId + " and toolContentId " + toolContentId);
	}
	Chat chat = chatDAO.getByContentId(toolContentId);
	if (chat == null) {
	    ChatService.logger
		    .warn("Did not find activity with toolContentId: " + toolContentId + " to remove learner content");
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

    @Override
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
    @Override
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {
	try {
	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(ChatImportContentVersionFilter.class);

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, chatToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof Chat)) {
		throw new ImportToolContentException(
			"Import Chat tool content failed. Deserialized object is " + toolPOJO);
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
    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	Chat chat = getChatDAO().getByContentId(toolContentId);

	if (chat == null) {
	    chat = getDefaultContent();
	}

	return getChatOutputFactory().getToolOutputDefinitions(chat, definitionType);
    }

    @Override
    public String getToolContentTitle(Long toolContentId) {
	return getChatByContentId(toolContentId).getTitle();
    }

    @Override
    public boolean isContentEdited(Long toolContentId) {
	return getChatByContentId(toolContentId).isDefineLater();
    }

    @Override
    public boolean isReadOnly(Long toolContentId) {
	Chat chat = chatDAO.getByContentId(toolContentId);
	for (ChatSession session : (Set<ChatSession>) chat.getChatSessions()) {
	    if (!session.getChatMessages().isEmpty()) {
		// we don't remove users in removeLearnerContent(), just messages
		return true;
	    }
	}

	return false;
    }

    /* IChatService Methods */
    private Long getDefaultContentIdBySignature(String toolSignature) {
	Long toolContentId = toolService.getToolDefaultContentIdBySignature(toolSignature);
	if (toolContentId == null) {
	    String error = "Could not retrieve default content id for this tool";
	    ChatService.logger.error(error);
	    throw new ChatException(error);
	}
	return toolContentId;
    }

    @Override
    public Chat getDefaultContent() {
	Long defaultContentID = getDefaultContentIdBySignature(ChatConstants.TOOL_SIGNATURE);
	Chat defaultContent = getChatByContentId(defaultContentID);
	if (defaultContent == null) {
	    String error = "Could not retrieve default content record for this tool";
	    ChatService.logger.error(error);
	    throw new ChatException(error);
	}
	return defaultContent;
    }

    @Override
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

    @Override
    public Chat getChatByContentId(Long toolContentID) {
	Chat chat = chatDAO.getByContentId(toolContentID);
	if (chat == null) {
	    ChatService.logger.debug("Could not find the content with toolContentID:" + toolContentID);
	}
	return chat;
    }

    @Override
    public ChatSession getSessionBySessionId(Long toolSessionId) {
	ChatSession chatSession = chatSessionDAO.getBySessionId(toolSessionId);
	if (chatSession == null) {
	    ChatService.logger.debug("Could not find the chat session with toolSessionID:" + toolSessionId);
	}
	return chatSession;
    }

    @Override
    public List<ChatUser> getUsersActiveBySessionId(Long toolSessionId) {
	Date oldestLastPresence = new Date(System.currentTimeMillis() - ChatConstants.PRESENCE_IDLE_TIMEOUT);
	return chatUserDAO.getBySessionIdAndLastPresence(toolSessionId, oldestLastPresence);
    }

    @Override
    public ChatUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId) {
	return chatUserDAO.getByUserIdAndSessionId(userId, toolSessionId);
    }

    @Override
    public ChatUser getUserByLoginNameAndSessionId(String loginName, Long toolSessionId) {
	return chatUserDAO.getByLoginNameAndSessionId(loginName, toolSessionId);
    }

    @Override
    public ChatUser getUserByUID(Long uid) {
	return chatUserDAO.getByUID(uid);
    }

    @Override
    public ChatUser getUserByNicknameAndSessionID(String nickname, Long sessionID) {
	return chatUserDAO.getByNicknameAndSessionID(nickname, sessionID);
    }

    /**
     * Stores information when users with given UIDs were last seen in their Chat session.
     */
    @Override
    public void updateUserPresence(Long toolSessionId, Set<String> activeUsers) {
	Date currentTime = new Date();
	for (String userName : activeUsers) {
	    ChatUser chatUser = getUserByNicknameAndSessionID(userName, toolSessionId);
	    chatUser.setLastPresence(currentTime);
	    saveOrUpdateChatUser(chatUser);
	}
    }

    @Override
    public List<ChatMessage> getMessagesForUser(ChatUser chatUser) {
	return chatMessageDAO.getForUser(chatUser);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ChatMessage> getMessagesSentByUser(Long userUid) {
	return chatMessageDAO.getSentByUser(userUid);
    }

    @Override
    public void saveOrUpdateChat(Chat chat) {
	updateMessageFilters(chat);
	chatDAO.saveOrUpdate(chat);
    }

    @Override
    public void saveOrUpdateChatSession(ChatSession chatSession) {
	chatSessionDAO.saveOrUpdate(chatSession);
    }

    @Override
    public void saveOrUpdateChatUser(ChatUser chatUser) {
	chatUserDAO.saveOrUpdate(chatUser);
    }

    @Override
    public void saveOrUpdateChatMessage(ChatMessage chatMessage) {
	chatMessageDAO.saveOrUpdate(chatMessage);
    }

    @Override
    public ChatUser createChatUser(UserDTO user, ChatSession chatSession) {
	ChatUser chatUser = new ChatUser(user, chatSession);

	chatUser.setNickname(createNickname(chatUser));
	saveOrUpdateChatUser(chatUser);
	return chatUser;
    }

    private synchronized String createNickname(ChatUser chatUser) {
	String desiredNickname = chatUser.getFirstName() + " " + chatUser.getLastName() + " ";
	String nickname = desiredNickname;

	boolean valid = false;
	int count = 1;
	Long sessionId = chatUser.getChatSession().getSessionId();

	// TODO may need max tries to prevent possibly entering infinite loop.
	while (!valid) {
	    if (getUserByNicknameAndSessionID(nickname, sessionId) == null) {
		// nickname is available
		valid = true;
	    } else {
		nickname = desiredNickname + count;
		count++;
	    }
	}

	return nickname;
    }

    @Override
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

    @Override
    public ChatMessageFilter updateMessageFilters(Chat chat) {
	ChatMessageFilter filter = new ChatMessageFilter(chat);
	messageFilters.put(chat.getToolContentId(), filter);
	return filter;
    }

    @Override
    public ChatMessage getMessageByUID(Long messageUID) {
	return chatMessageDAO.getByUID(messageUID);
    }

    @Override
    public List getLastestMessages(ChatSession chatSession, Integer max, boolean orderAsc) {
	return chatMessageDAO.getLatest(chatSession, max, orderAsc);
    }

    public ILogEventService getLogEventService() {
	return logEventService;
    }

    public void setLogEventService(ILogEventService logEventService) {
	this.logEventService = logEventService;
    }

    @Override
    public void auditEditMessage(ChatMessage chatMessage, String messageBody) {
	Long toolContentId = null;
	if (chatMessage.getChatSession() != null && chatMessage.getChatSession().getChat() != null) {
	    toolContentId = chatMessage.getChatSession().getChat().getToolContentId();
	}

	logEventService.logChangeLearnerContent(chatMessage.getFromUser().getUserId(),
		chatMessage.getFromUser().getLoginName(), toolContentId, chatMessage.getBody(), messageBody);
    }

    @Override
    public void auditHideShowMessage(ChatMessage chatMessage, boolean messageHidden) {
	Long toolContentId = null;
	if (chatMessage.getChatSession() != null && chatMessage.getChatSession().getChat() != null) {
	    toolContentId = chatMessage.getChatSession().getChat().getToolContentId();
	}
	if (messageHidden) {
	    logEventService.logHideLearnerContent(chatMessage.getFromUser().getUserId(),
		    chatMessage.getFromUser().getLoginName(), toolContentId, chatMessage.toString());
	} else {
	    logEventService.logShowLearnerContent(chatMessage.getFromUser().getUserId(),
		    chatMessage.getFromUser().getLoginName(), toolContentId, chatMessage.toString());
	}
    }

    @Override
    public boolean isGroupedActivity(long toolContentID) {
	return toolService.isGroupedActivity(toolContentID);
    }

    @Override
    public void auditLogStartEditingActivityInMonitor(long toolContentID) {
	toolService.auditLogStartEditingActivityInMonitor(toolContentID);
    }
    
    @Override
    public boolean isLastActivity(Long toolSessionId) {
	return toolService.isLastActivity(toolSessionId);
    }

    /* Private methods */
    private Map<Long, ChatMessageFilter> messageFilters = new ConcurrentHashMap<>();

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

    public IExportToolContentService getExportContentService() {
	return exportContentService;
    }

    public void setExportContentService(IExportToolContentService exportContentService) {
	this.exportContentService = exportContentService;
    }

    @Override
    public Map<Long, Integer> getMessageCountBySession(Long chatUID) {
	return chatMessageDAO.getCountBySession(chatUID);
    }

    @Override
    public Map<Long, Integer> getMessageCountByFromUser(Long sessionUID) {
	return chatMessageDAO.getCountByFromUser(sessionUID);
    }

    public ChatOutputFactory getChatOutputFactory() {
	return chatOutputFactory;
    }

    public void setChatOutputFactory(ChatOutputFactory chatOutputFactory) {
	this.chatOutputFactory = chatOutputFactory;
    }

    @Override
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

    @Override
    public void deleteCondition(ChatCondition condition) {
	if ((condition != null) && (condition.getConditionId() != null)) {
	    chatDAO.delete(condition);
	}
    }

    @Override
    public void releaseConditionsFromCache(Chat chat) {
	if (chat.getConditions() != null) {
	    for (ChatCondition condition : chat.getConditions()) {
		getChatDAO().releaseFromCache(condition);
	    }
	}
    }

    @Override
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getChatOutputFactory().getSupportedDefinitionClasses(definitionType);
    }

    @Override
    public ToolCompletionStatus getCompletionStatus(Long learnerId, Long toolSessionId) {
	ChatUser learner = getUserByUserIdAndSessionId(learnerId, toolSessionId);
	if (learner == null) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_NOT_ATTEMPTED, null, null);
	}

	Date startDate = null;
	Date endDate = learner.getLastPresence();
	List<ChatMessage> messages = getMessagesForUser(learner);
	for (ChatMessage message : messages) {
	    Date sendDate = message.getSendDate();
	    if (sendDate != null) {
		if (startDate == null || sendDate.before(startDate)) {
		    startDate = sendDate;
		}
		if (endDate == null || sendDate.after(endDate)) {
		    endDate = sendDate;
		}
	    }
	}

	if (learner.isFinishedActivity()) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_COMPLETED, startDate, endDate);
	} else {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_ATTEMPTED, startDate, null);
	}
    }
    // =========================================================================================

    // ****************** REST methods *************************

    /**
     * Used by the Rest calls to create content. Mandatory fields in toolContentJSON: title, instructions Optional
     * fields reflectInstructions, lockWhenFinished, filterKeywords
     */
    @Override
    public void createRestToolContent(Integer userID, Long toolContentID, ObjectNode toolContentJSON) {

	Chat content = new Chat();
	Date updateDate = new Date();
	content.setCreateDate(updateDate);
	content.setUpdateDate(updateDate);
	content.setToolContentId(toolContentID);
	content.setTitle(JsonUtil.optString(toolContentJSON, RestTags.TITLE));
	content.setInstructions(JsonUtil.optString(toolContentJSON, RestTags.INSTRUCTIONS));
	content.setCreateBy(userID.longValue());

	content.setContentInUse(false);
	content.setDefineLater(false);
	content.setLockOnFinished(JsonUtil.optBoolean(toolContentJSON, RestTags.LOCK_WHEN_FINISHED, Boolean.FALSE));

	String filterKeywords = JsonUtil.optString(toolContentJSON, "filterKeywords");
	content.setFilteringEnabled((filterKeywords != null) && (filterKeywords.length() > 0));
	content.setFilterKeywords(filterKeywords);
	// submissionDeadline is set in monitoring

	saveOrUpdateChat(content);

    }
}
