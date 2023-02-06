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

package org.lamsfoundation.lams.tool.forum.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.confidencelevel.ConfidenceLevelDTO;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.contentrepository.exception.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.exception.RepositoryCheckedException;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.rest.RestTags;
import org.lamsfoundation.lams.rest.ToolRestManager;
import org.lamsfoundation.lams.tool.ToolCompletionStatus;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.forum.ForumConstants;
import org.lamsfoundation.lams.tool.forum.dao.IAttachmentDAO;
import org.lamsfoundation.lams.tool.forum.dao.IForumDAO;
import org.lamsfoundation.lams.tool.forum.dao.IForumToolSessionDAO;
import org.lamsfoundation.lams.tool.forum.dao.IForumUserDAO;
import org.lamsfoundation.lams.tool.forum.dao.IMessageDAO;
import org.lamsfoundation.lams.tool.forum.dao.IMessageRatingDAO;
import org.lamsfoundation.lams.tool.forum.dao.IMessageSeqDAO;
import org.lamsfoundation.lams.tool.forum.dao.ITimestampDAO;
import org.lamsfoundation.lams.tool.forum.dto.AverageRatingDTO;
import org.lamsfoundation.lams.tool.forum.dto.MessageDTO;
import org.lamsfoundation.lams.tool.forum.model.Attachment;
import org.lamsfoundation.lams.tool.forum.model.Forum;
import org.lamsfoundation.lams.tool.forum.model.ForumCondition;
import org.lamsfoundation.lams.tool.forum.model.ForumConfigItem;
import org.lamsfoundation.lams.tool.forum.model.ForumReport;
import org.lamsfoundation.lams.tool.forum.model.ForumToolSession;
import org.lamsfoundation.lams.tool.forum.model.ForumUser;
import org.lamsfoundation.lams.tool.forum.model.Message;
import org.lamsfoundation.lams.tool.forum.model.MessageRating;
import org.lamsfoundation.lams.tool.forum.model.MessageSeq;
import org.lamsfoundation.lams.tool.forum.model.Timestamp;
import org.lamsfoundation.lams.tool.forum.util.DateComparator;
import org.lamsfoundation.lams.tool.forum.util.ForumException;
import org.lamsfoundation.lams.tool.forum.util.MessageDtoComparator;
import org.lamsfoundation.lams.tool.forum.util.PersistenceException;
import org.lamsfoundation.lams.tool.forum.util.TopicComparator;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 *
 * @author Steve.Ni
 *
 * @version $Revision$
 */
public class ForumService implements IForumService, ToolContentManager, ToolSessionManager, ToolRestManager {
    private static final Logger log = Logger.getLogger(ForumService.class);

    // DAO variables
    private IForumDAO forumDao;

    private IAttachmentDAO attachmentDao;

    private IMessageDAO messageDao;

    private ITimestampDAO timestampDao;

    private IMessageSeqDAO messageSeqDao;

    private IMessageRatingDAO messageRatingDao;

    private IForumUserDAO forumUserDao;

    private IForumToolSessionDAO forumToolSessionDao;

    // system level handler and service
    private ILamsToolService toolService;

    private IToolContentHandler forumToolContentHandler;

    private ILogEventService logEventService;

    private MessageService messageService;

    private IExportToolContentService exportContentService;

    private IUserManagementService userManagementService;

    private ICoreNotebookService coreNotebookService;

    private ForumOutputFactory forumOutputFactory;

    private IEventNotificationService eventNotificationService;

    private ILessonService lessonService;

    private IActivityDAO activityDAO;

    private Random generator = new Random();

    // ---------------------------------------------------------------------
    // Inversion of Control Methods - Method injection
    // ---------------------------------------------------------------------
    public void setLogEventService(ILogEventService logEventService) {
	this.logEventService = logEventService;
    }

    @Override
    public ILogEventService getLogEventService() {
	return logEventService;
    }

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    public ForumOutputFactory getForumOutputFactory() {
	return forumOutputFactory;
    }

    public void setForumOutputFactory(ForumOutputFactory forumOutputFactory) {
	this.forumOutputFactory = forumOutputFactory;
    }

    @Override
    public Forum updateForum(Forum forum) throws PersistenceException {
	forumDao.saveOrUpdate(forum);
	return forum;
    }

    @Override
    public Forum getForum(Long forumUid) throws PersistenceException {
	return forumDao.getById(forumUid);
    }

    @Override
    public Forum getForumByContentId(Long contentID) throws PersistenceException {
	return forumDao.getByContentId(contentID);
    }

    public void deleteForumAttachment(Long attachmentId) throws PersistenceException {
	Attachment attachment = attachmentDao.getById(attachmentId);
	attachmentDao.delete(attachment);
    }

    @Override
    public Message createRootTopic(Long forumId, Long sessionId, Message message) throws PersistenceException {
	return createRootTopic(forumId, getSessionBySessionId(sessionId), message);
    }

    public Message createRootTopic(Long forumId, ForumToolSession session, Message message)
	    throws PersistenceException {
	// get Forum and ForumToolSesion
	if (message.getForum() == null) {
	    Forum forum = forumDao.getById(forumId);
	    message.setForum(forum);
	}
	// if topic created by author, session will be null.
	if (session != null) {
	    message.setToolSession(session);
	}

	if (message.getUid() == null) {
	    // update message sequence
	    MessageSeq msgSeq = new MessageSeq();
	    msgSeq.setMessage(message);
	    msgSeq.setMessageLevel((short) 0);
	    // set itself as root
	    msgSeq.setRootMessage(message);
	    messageSeqDao.save(msgSeq);
	}

	// if this message had any cloned objects, they also need to be changed.
	// this will only happen if an authored topic is changed via monitoring
	if (message.getSessionClones().size() > 0) {
	    Iterator iter = message.getSessionClones().iterator();
	    while (iter.hasNext()) {
		Message clone = (Message) iter.next();
		message.updateClone(clone);
		clone.updateModificationData();
		messageDao.saveOrUpdate(clone);
	    }
	}

	// create message in database
	message.updateModificationData();
	messageDao.saveOrUpdate(message);

	return message;
    }

    @Override
    public Message updateTopic(Message message) throws PersistenceException {

	// update message
	message.updateModificationData();
	messageDao.saveOrUpdate(message);

	// udate root message's lastReplyDate
	MessageSeq msgSeq = messageSeqDao.getByTopicId(message.getUid());
	Message root = msgSeq.getRootMessage();
	root.setLastReplyDate(new Date());
	messageDao.saveOrUpdate(root);

	return message;
    }

    @Override
    public List<MessageDTO> getMessageAsDTO(Long messageUid) throws PersistenceException {

	MessageSeq msgSeq = messageSeqDao.getByMessageId(messageUid);
	List<MessageDTO> msgDtoList = new ArrayList<>();
	msgDtoList.add(makeDTOSetRating(msgSeq, msgSeq.getMessage()));
	return msgDtoList;
    }

    @Override
    public void updateMark(Message message) {
	messageDao.saveOrUpdate(message);

	// send marks to gradebook, if marks are released for that session
	ForumUser user = message.getCreatedBy();
	ForumToolSession session = message.getToolSession();
	if (session.isMarkReleased()) {
	    sendMarksToGradebook(user, session.getSessionId());
	}
    }

    @Override
    public Message updateMessageHideFlag(Long messageId, boolean hideFlag) {

	Message message = getMessage(messageId);
	if (message != null) {
	    Long userId = 0L;
	    String loginName = "Default";
	    Long toolContentId = null;
	    if (message.getCreatedBy() != null) {
		userId = message.getCreatedBy().getUserId();
		loginName = message.getCreatedBy().getLoginName();
	    }
	    if (message.getToolSession() != null && message.getToolSession().getForum() != null) {
		toolContentId = message.getToolSession().getForum().getContentId();
	    }
	    if (hideFlag) {
		logEventService.logHideLearnerContent(userId, loginName, toolContentId, message.toString());
	    } else {
		logEventService.logShowLearnerContent(userId, loginName, toolContentId, message.toString());
	    }

	    message.setHideFlag(hideFlag);

	    // update message
	    messageDao.update(message);
	}
	return message;
    }

    private Message getMessageForUpdate(Long messageUid) throws PersistenceException {
	return messageDao.getByIdForUpdate(messageUid);
    }

    @Override
    public Message getMessage(Long messageUid) throws PersistenceException {
	Message message = messageDao.getById(messageUid);
	for (Attachment attachment : message.getAttachments()) {
	    attachment.setFileDisplayUuid(forumToolContentHandler.getFileUuid(attachment.getFileUuid()));
	}
	return message;
    }

    @Override
    public void deleteTopic(Long topicUid) throws PersistenceException {
	Message topic = messageDao.getById(topicUid);

	// cascade delete children topic by recursive
	List children = messageDao.getChildrenTopics(topicUid);
	if (children != null) {
	    Iterator iter = children.iterator();
	    while (iter.hasNext()) {
		Message msg = (Message) iter.next();
		this.deleteTopic(msg.getUid());
	    }
	}

	// recursively delete clones
	for (Message clone : topic.getSessionClones()) {
	    this.deleteTopic(clone.getUid());
	}

	messageSeqDao.deleteByTopicId(topicUid);
	messageDao.delete(topicUid);
    }

    @Override
    public void deleteCondition(ForumCondition condition) throws PersistenceException {
	forumDao.deleteCondition(condition);
    }

    @Override
    public MessageSeq replyTopic(Long parentId, Long sessionId, Message replyMessage) throws PersistenceException {
	// set parent
	Message parent = this.getMessageForUpdate(parentId);
	replyMessage.setParent(parent);
	replyMessage.setForum(parent.getForum());
	// parent sessionID maybe empty if created by author role. So given sessionId is exactly value.
	ForumToolSession session = getSessionBySessionId(sessionId);
	replyMessage.setToolSession(session);
	replyMessage.updateModificationData();
	messageDao.saveOrUpdate(replyMessage);

	// get root topic and create record in MessageSeq table
	MessageSeq parentSeq = messageSeqDao.getByTopicId(parent.getUid());
	if (parentSeq == null) {
	    ForumService.log
		    .error("Message Sequence table is broken becuase topic " + parent + " can not get Sequence Record");
	}
	Message root = parentSeq.getRootMessage();
	MessageSeq msgSeq = new MessageSeq();
	msgSeq.setMessage(replyMessage);
	msgSeq.setMessageLevel((short) (parentSeq.getMessageLevel() + 1));
	msgSeq.setRootMessage(root);
	// look back up through the parents to find the thread top - will be level 1
	if (msgSeq.getMessageLevel() == 1) {
	    msgSeq.setThreadMessage(replyMessage);
	} else {
	    MessageSeq threadSeq = parentSeq;
	    while (threadSeq.getMessageLevel() > 1) {
		threadSeq = messageSeqDao.getByTopicId(threadSeq.getMessage().getParent().getUid());
	    }
	    msgSeq.setThreadMessage(threadSeq.getMessage());
	}
	messageSeqDao.save(msgSeq);

	// update last reply date for root message
	root.setLastReplyDate(new Date());
	// update reply message number for root
	root.setReplyNumber(root.getReplyNumber() + 1);
	messageDao.saveOrUpdate(root);

	return msgSeq;
    }

    @Override
    public Attachment uploadAttachment(File uploadFile) throws PersistenceException {
	if ((uploadFile == null) || StringUtils.isEmpty(uploadFile.getName())) {
	    throw new ForumException("Could not find upload file: " + uploadFile);
	}

	NodeKey nodeKey = processFile(uploadFile);
	Attachment file = new Attachment();
	file.setFileUuid(nodeKey.getNodeId());
	file.setFileVersionId(nodeKey.getVersion());
	file.setFileName(uploadFile.getName());
	file.setFileDisplayUuid(nodeKey.getUuid());

	return file;
    }

    @Override
    public List<MessageDTO> getTopicThread(Long rootTopicId) {
	List unsortedThread = messageSeqDao.getCompleteTopic(rootTopicId);
	Iterator iter = unsortedThread.iterator();
	MessageSeq msgSeq;
	SortedMap<MessageSeq, Message> map = new TreeMap<>(new TopicComparator());
	while (iter.hasNext()) {
	    msgSeq = (MessageSeq) iter.next();
	    map.put(msgSeq, msgSeq.getMessage());
	}
	return getSortedMessageDTO(map);

    }

    @Override
    public List getTopicThread(Long rootTopicId, Long afterSequenceId, Long pagingSize) {

	long lastThreadMessageUid = afterSequenceId != null ? afterSequenceId.longValue() : 0L;
	long usePagingSize = pagingSize != null ? pagingSize.longValue() : ForumConstants.DEFAULT_PAGE_SIZE;
	SortedMap<MessageSeq, Message> map = new TreeMap<>(new TopicComparator());

	// first time through we need to include the top topic message (level 0)
	if (lastThreadMessageUid == 0) {
	    MessageSeq msgSeq = messageSeqDao.getByTopicId(rootTopicId);
	    map.put(msgSeq, msgSeq.getMessage());
	}

	long count = 0;
	boolean foundEnough = false;
	do {

	    List msgSeqs = messageSeqDao.getNextThreadByThreadId(rootTopicId, lastThreadMessageUid);
	    if (msgSeqs.size() == 0) {
		// no more to come from db
		foundEnough = true;
	    } else {

		Iterator iter = msgSeqs.iterator();
		while (iter.hasNext()) {
		    MessageSeq msgSeq = (MessageSeq) iter.next();
		    if (msgSeq.getMessageLevel() == 1) {
			lastThreadMessageUid = msgSeq.getMessage().getUid().longValue();
		    }
		    map.put(msgSeq, msgSeq.getMessage());
		    count++;
		}
		if ((usePagingSize >= 0) && (count >= usePagingSize)) {
		    foundEnough = true;
		}
	    }
	} while (!foundEnough);

	return getSortedMessageDTO(map);
    }

    @Override
    public List getThread(Long threadId) {
	List msgSeqs = messageSeqDao.getThreadByThreadId(threadId);
	SortedMap<MessageSeq, Message> map = new TreeMap<>(new TopicComparator());
	Iterator iter = msgSeqs.iterator();
	while (iter.hasNext()) {
	    MessageSeq msgSeq = (MessageSeq) iter.next();
	    map.put(msgSeq, msgSeq.getMessage());
	}
	;
	return getSortedMessageDTO(map);
    }

    @Override
    public List<MessageDTO> getRootTopics(Long sessionId) {
	ForumToolSession session = getSessionBySessionId(sessionId);
	if ((session == null) || (session.getForum() == null)) {
	    ForumService.log.error("Failed on getting session by given sessionID:" + sessionId);
	    throw new ForumException("Failed on getting session by given sessionID:" + sessionId);
	}

	List topicsBySession = messageDao.getRootTopics(sessionId);
	List<MessageDTO> messageDTOs = MessageDTO.getMessageDTO(topicsBySession);

	// sort by sequence id
	Set topicSet = new TreeSet<>(new MessageDtoComparator());
	topicSet.addAll(messageDTOs);

	topicsBySession.clear();
	topicsBySession.addAll(topicSet);
	return topicsBySession;
    }

    @Override
    public int getTopicsNum(Long userID, Long sessionId) {
	return messageDao.getTopicsNum(userID, sessionId);
    }

    @Override
    public int getNumOfPostsByTopic(Long userId, Long topicId) {
	return messageSeqDao.getNumOfPostsByTopic(userId, topicId);
    }

    @Override
    public ForumUser getUserByID(Long userId) {
	return forumUserDao.getByUserId(userId);
    }

    @Override
    public ForumUser getUserByUserAndSession(Long userId, Long sessionId) {
	return forumUserDao.getByUserIdAndSessionId(userId, sessionId);
    }

    @Override
    public List<Object[]> getUsersForTablesorter(final Long sessionId, int page, int size, int sorting,
	    String searchString, boolean getNotebookEntries) {
	return forumUserDao.getUsersForTablesorter(sessionId, page, size, sorting, searchString, getNotebookEntries,
		coreNotebookService, userManagementService);
    }

    @Override
    public int getCountUsersBySession(Long sessionId, String searchString) {
	return forumUserDao.getCountUsersBySession(sessionId, searchString);
    }

    @Override
    public void createUser(ForumUser forumUser) {
	forumUserDao.save(forumUser);
    }

    @Override
    public ForumToolSession getSessionBySessionId(Long sessionId) {
	return forumToolSessionDao.getBySessionId(sessionId);
    }

    @Override
    public Long getRootTopicId(Long topicId) {
	MessageSeq seq = messageSeqDao.getByTopicId(topicId);
	if ((seq == null) || (seq.getRootMessage() == null)) {
	    ForumService.log.error("A sequence information can not be found for topic ID:" + topicId);
	    return null;
	}
	return seq.getRootMessage().getUid();
    }

    @Override
    public List<MessageDTO> getAuthoredTopics(Long forumUid) {
	List<Message> list = messageDao.getTopicsFromAuthor(forumUid);

	TreeMap<Date, Message> map = new TreeMap<>(new DateComparator());
	// get all the topics skipping ones with a tool session (we may be editing in monitor) and sort by create date
	Iterator<Message> iter = list.iterator();
	while (iter.hasNext()) {
	    Message topic = iter.next();
	    if (topic.getToolSession() == null) {
		map.put(topic.getCreated(), topic);
	    }

	    for (Attachment attachment : topic.getAttachments()) {
		attachment.setFileDisplayUuid(forumToolContentHandler.getFileUuid(attachment.getFileUuid()));
	    }

	}
	return MessageDTO.getMessageDTO(new ArrayList<>(map.values()));
    }

    @Override
    public List<ForumToolSession> getSessionsByContentId(Long contentID) {
	return forumToolSessionDao.getByContentId(contentID);
    }

    @Override
    public List<ForumUser> getUsersBySessionId(Long sessionID) {
	return forumUserDao.getBySessionId(sessionID);
    }

    @Override
    public List<MessageDTO> getMessagesByUserUid(Long userId, Long sessionId) {
	List<Message> list = messageDao.getByUserAndSession(userId, sessionId);
	for (Message message : list) {
	    for (Attachment attachment : message.getAttachments()) {
		attachment.setFileDisplayUuid(forumToolContentHandler.getFileUuid(attachment.getFileUuid()));
	    }
	}
	return MessageDTO.getMessageDTO(list);
    }

    @Override
    public ForumUser getUser(Long userUid) {
	return forumUserDao.getByUid(userUid);
    }

    @Override
    public void releaseMarksForSession(Long sessionID) {
	// udate release mark date for each message.
	List<Message> messages = messageDao.getBySession(sessionID);
	ForumToolSession session = forumToolSessionDao.getBySessionId(sessionID);
	Forum forum = session.getForum();
	boolean notifyLearnersOnMarkRelease = getEventNotificationService().eventExists(ForumConstants.TOOL_SIGNATURE,
		ForumConstants.EVENT_NAME_NOTIFY_LEARNERS_ON_MARK_RELEASE, forum.getContentId());

	List<ForumUser> users = getUsersBySessionId(sessionID);
	if (users != null) {
	    for (ForumUser user : users) {
		// send marks to gradebook where applicable
		sendMarksToGradebook(user, sessionID);
	    }
	}

	// update session to set MarkRelease flag
	session.setMarkReleased(true);
	forumToolSessionDao.saveOrUpdate(session);

	// notify learners on mark release
	if (notifyLearnersOnMarkRelease) {
	    Map<Integer, StringBuilder> notificationMessages = new TreeMap<>();

	    for (Message message : messages) {
		ForumReport report = message.getReport();
		if (report != null) {
		    ForumUser user = message.getCreatedBy();
		    StringBuilder notificationMessage = notificationMessages.get(user.getUserId().intValue());
		    if (notificationMessage == null) {
			notificationMessage = new StringBuilder();
		    }
		    Object[] notificationMessageParameters = new Object[3];
		    notificationMessageParameters[0] = message.getSubject();
		    notificationMessageParameters[1] = message.getUpdated();
		    notificationMessageParameters[2] = report.getMark();
		    notificationMessage
			    .append(getLocalisedMessage("event.mark.release.mark", notificationMessageParameters));
		    notificationMessages.put(user.getUserId().intValue(), notificationMessage);
		}
	    }

	    for (Integer userID : notificationMessages.keySet()) {
		Object[] notificationMessageParameters = new Object[1];
		notificationMessageParameters[0] = notificationMessages.get(userID).toString();
		getEventNotificationService().triggerForSingleUser(ForumConstants.TOOL_SIGNATURE,
			ForumConstants.EVENT_NAME_NOTIFY_LEARNERS_ON_MARK_RELEASE, forum.getContentId(), userID,
			notificationMessageParameters);

	    }
	}

	//audit log event
	String sessionName = session.getSessionName() + " (toolSessionId=" + session.getSessionId() + ")";
	String message = messageService.getMessage("tool.display.name") + ". "
		+ messageService.getMessage("msg.mark.released", new String[] { sessionName });
	logEventService.logToolEvent(LogEvent.TYPE_TOOL_MARK_RELEASED, forum.getContentId(), null, message);

    }

    @Override
    public void finishUserSession(ForumUser currentUser) {
	currentUser.setSessionFinished(true);
	forumUserDao.save(currentUser);
    }

    @Override
    public AverageRatingDTO rateMessage(Long messageId, Long userId, Long toolSessionID, float rating) {
	ForumUser imageGalleryUser = getUserByUserAndSession(userId, toolSessionID);
	MessageRating messageRating = messageRatingDao.getRatingByMessageAndUser(messageId, userId);
	Message message = messageDao.getById(messageId);

	//persist MessageRating changes in DB
	if (messageRating == null) { // add
	    messageRating = new MessageRating();
	    messageRating.setUser(imageGalleryUser);
	    messageRating.setMessage(message);
	}

	// LDEV-4590 Star Rating can never be more than 5 stars
	if (Float.compare(rating, RatingCriteria.RATING_STYLE_STAR_DEFAULT_MAX_AS_FLOAT) > 0) {
	    rating = RatingCriteria.RATING_STYLE_STAR_DEFAULT_MAX_AS_FLOAT;
	}
	messageRating.setRating(rating);
	messageRatingDao.saveObject(messageRating);
	//to make available new changes be visible in jsp page
	return messageRatingDao.getAverageRatingDTOByMessage(messageId);
    }

    @Override
    public AverageRatingDTO getAverageRatingDTOByMessage(Long messageId) {
	return messageRatingDao.getAverageRatingDTOByMessage(messageId);
    }

    @Override
    public int getNumOfRatingsByUserAndForum(Long userUid, Long forumUid) {
	return messageRatingDao.getNumOfRatingsByUserAndForum(userUid, forumUid);
    }

    // ***************************************************************************************************************
    // Private methods
    // ***************************************************************************************************************
    /**
     * @param map
     * @return
     */
    private List<MessageDTO> getSortedMessageDTO(SortedMap<MessageSeq, Message> map) {
	Iterator iter;
	MessageSeq msgSeq;
	Message message;
	List<MessageDTO> msgDtoList = new ArrayList<>();
	iter = map.entrySet().iterator();
	while (iter.hasNext()) {
	    Map.Entry entry = (Entry) iter.next();
	    msgSeq = (MessageSeq) entry.getKey();
	    message = (Message) entry.getValue();
	    msgDtoList.add(makeDTOSetRating(msgSeq, message));
	}
	return msgDtoList;
    }

    private MessageDTO makeDTOSetRating(MessageSeq msgSeq, Message message) {
	for (Attachment attachment : message.getAttachments()) {
	    attachment.setFileDisplayUuid(forumToolContentHandler.getFileUuid(attachment.getFileUuid()));
	}
	MessageDTO dto = MessageDTO.getMessageDTO(message);
	dto.setLevel(msgSeq.getMessageLevel());
	//set averageRating
	if (message.getForum().isAllowRateMessages()) {
	    AverageRatingDTO averageRating = getAverageRatingDTOByMessage(message.getUid());
	    dto.setAverageRating(averageRating.getRating());
	    dto.setNumberOfVotes(averageRating.getNumberOfVotes());
	}
	return dto;
    }

    /**
     * Process an uploaded file.
     */
    private NodeKey processFile(File file) {
	NodeKey node = null;
	if ((file != null) && !StringUtils.isEmpty(file.getName())) {
	    String fileName = file.getName();
	    try {
		node = getForumToolContentHandler().uploadFile(new FileInputStream(file), fileName, null);
	    } catch (InvalidParameterException e) {
		throw new ForumException("FileNotFoundException occured while trying to upload File" + e.getMessage());
	    } catch (FileNotFoundException e) {
		throw new ForumException("FileNotFoundException occured while trying to upload File" + e.getMessage());
	    } catch (RepositoryCheckedException e) {
		throw new ForumException("FileNotFoundException occured while trying to upload File" + e.getMessage());
	    }
	}
	return node;
    }

    private Forum getDefaultForum() {
	Long defaultForumId = new Long(toolService.getToolDefaultContentIdBySignature(ForumConstants.TOOL_SIGNATURE));
	if (defaultForumId.equals(0L)) {
	    String error = "Could not retrieve default content id for this tool";
	    ForumService.log.error(error);
	    throw new ForumException(error);
	}
	Forum defaultForum = getForumByContentId(defaultForumId);
	if (defaultForum == null) {
	    String error = "Could not retrieve default content record for this tool";
	    ForumService.log.error(error);
	    throw new ForumException(error);
	}

	return defaultForum;

    }

    @Override
    public Long createNotebookEntry(Long sessionId, Integer notebookToolType, String toolSignature, Integer userId,
	    String entryText) {
	return coreNotebookService.createNotebookEntry(sessionId, notebookToolType, toolSignature, userId, "",
		entryText);
    }

    @Override
    public NotebookEntry getEntry(Long sessionId, Integer idType, String signature, Integer userID) {
	List<NotebookEntry> list = coreNotebookService.getEntry(sessionId, idType, signature, userID);
	if ((list == null) || list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    @Override
    public void updateEntry(NotebookEntry notebookEntry) {
	coreNotebookService.updateEntry(notebookEntry);
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

    @Override
    public String getLearnerContentFolder(Long toolSessionId, Long userId) {
	return toolService.getLearnerContentFolder(toolSessionId, userId);
    }

    // ***************************************************************************************************************
    // ToolContentManager and ToolSessionManager methods
    // ***************************************************************************************************************
    @Override
    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {
	if (toContentId == null) {
	    throw new ToolException("Failed to create the ForumFiles tool seession");
	}

	Forum fromContent = null;
	if (fromContentId != null) {
	    fromContent = forumDao.getByContentId(fromContentId);
	}
	if (fromContent == null) {
	    fromContent = getDefaultForum();
	}

	Forum toContent = Forum.newInstance(fromContent, toContentId);

	// remove session Messages from topics
	for (ForumCondition condition : toContent.getConditions()) {
	    Iterator<Message> conditionMessageIter = condition.getTopics().iterator();
	    while (conditionMessageIter.hasNext()) {
		Message conditionMessage = conditionMessageIter.next();
		if (conditionMessage.getToolSession() != null) {
		    conditionMessageIter.remove();
		}
	    }
	}

	// save topics in this forum, only save the author created topic!!! and reset its reply number to zero.
	Set topics = toContent.getMessages();
	if (topics != null) {
	    Iterator iter = topics.iterator();
	    while (iter.hasNext()) {
		Message msg = (Message) iter.next();
		// set this message forum Uid as toContent
		if (!msg.getIsAuthored() || (msg.getToolSession() != null)) {
		    iter.remove();
		    continue;
		}
		msg.setReplyNumber(0);
		// msg.setCreated(new Date()); // need to keep the original create date to maintain correct order
		msg.setUpdated(new Date());
		msg.setLastReplyDate(new Date());
		msg.setHideFlag(false);
		msg.setForum(toContent);
		createRootTopic(toContent.getUid(), (ForumToolSession) null, msg);
	    }
	}
	forumDao.saveOrUpdate(toContent);

    }

    @Override
    public void resetDefineLater(Long toolContentId) throws DataMissingException, ToolException {
	Forum forum = forumDao.getByContentId(toolContentId);
	if (forum == null) {
	    throw new ToolException("No found tool content by given content ID:" + toolContentId);
	}
	forum.setDefineLater(false);
	forum.setContentInUse(false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void removeToolContent(Long toolContentId) throws ToolException {
	Forum forum = forumDao.getByContentId(toolContentId);
	if (forum == null) {
	    ForumService.log.warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}

	for (ForumToolSession session : (List<ForumToolSession>) forumToolSessionDao.getByContentId(toolContentId)) {
	    List<NotebookEntry> entries = coreNotebookService.getEntry(session.getSessionId(),
		    CoreNotebookConstants.NOTEBOOK_TOOL, ForumConstants.TOOL_SIGNATURE);
	    for (NotebookEntry entry : entries) {
		coreNotebookService.deleteEntry(entry);
	    }
	}
	forumDao.delete(forum);
    }

    @Override
    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	// do not remove learner content if it was set up so in sysadmin tool management
	ForumConfigItem keepLearnerContent = getConfigItem(ForumConfigItem.KEY_KEEP_LEARNER_CONTENT);
	if (Boolean.valueOf(keepLearnerContent.getConfigValue())) {
	    return;
	}

	if (ForumService.log.isDebugEnabled()) {
	    ForumService.log.debug(
		    "Hiding or removing Forum messages for user ID " + userId + " and toolContentId " + toolContentId);
	}
	List<ForumToolSession> sessionList = forumToolSessionDao.getByContentId(toolContentId);

	for (ForumToolSession session : sessionList) {
	    Long sessionId = session.getSessionId();
	    ForumUser user = forumUserDao.getByUserIdAndSessionId(userId.longValue(), sessionId);
	    if (user != null) {
		List<Message> messages = messageDao.getByUserAndSession(user.getUid(), sessionId);
		Iterator<Message> messageIterator = messages.iterator();
		while (messageIterator.hasNext()) {
		    Message message = messageIterator.next();

		    if (userOwnMessageTree(message, user.getUid())) {
			messageSeqDao.deleteByTopicId(message.getUid());
			Timestamp timestamp = timestampDao.getTimestamp(message.getUid(), user.getUid());
			if (timestamp != null) {
			    timestampDao.delete(timestamp);
			}
			messageDao.delete(message.getUid());
			messageIterator.remove();
		    } else {
			message.setHideFlag(true);
			messageDao.update(message);
		    }
		}

		NotebookEntry entry = getEntry(session.getSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL,
			ForumConstants.TOOL_SIGNATURE, userId);
		if (entry != null) {
		    // hopefully it understands NotebookEntries
		    activityDAO.delete(entry);
		}

		user.setSessionFinished(false);
		forumUserDao.save(user);

		toolService.removeActivityMark(userId, session.getSessionId());
	    }
	}
    }

    @SuppressWarnings("unchecked")
    private boolean userOwnMessageTree(Message message, Long userUid) {
	if (!message.getCreatedBy().getUid().equals(userUid)) {
	    return false;
	}
	List<Message> children = messageDao.getChildrenTopics(message.getUid());
	for (Message child : children) {
	    if (!userOwnMessageTree(child, userUid)) {
		return false;
	    }
	}

	return true;
    }

    @Override
    public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
	Forum toolContentObj = forumDao.getByContentId(toolContentId);
	if (toolContentObj == null) {
	    toolContentObj = getDefaultForum();
	}
	if (toolContentObj == null) {
	    throw new DataMissingException("Unable to find default content for the forum tool");
	}

	// set ResourceToolContentHandler as null to avoid copy file node in repository again.
	toolContentObj = Forum.newInstance(toolContentObj, toolContentId);
	toolContentObj.setCreatedBy(null);
	Set<Message> items = toolContentObj.getMessages();
	Set<Message> authorItems = new HashSet<>();
	for (Message item : items) {
	    if (item.getIsAuthored() && (item.getToolSession() == null)) {
		authorItems.add(item);
		item.setCreatedBy(null);
		item.setModifiedBy(null);
		item.setForum(null);
		item.setReport(null);
		item.setReplyNumber(0);
		item.setParent(null);
		item.setSessionClones(null);
	    }
	}
	toolContentObj.setMessages(authorItems);
	try {
	    exportContentService.registerFileClassForExport(Attachment.class.getName(), "fileUuid", "fileVersionId");
	    exportContentService.exportToolContent(toolContentId, toolContentObj, forumToolContentHandler, rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {

	try {
	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(ForumImportContentVersionFilter.class);

	    exportContentService.registerFileClassForImport(Attachment.class.getName(), "fileUuid", "fileVersionId",
		    "fileName", "fileType", null);

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, forumToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof Forum)) {
		throw new ImportToolContentException(
			"Import Forum tool content failed. Deserialized object is " + toolPOJO);
	    }
	    Forum toolContentObj = (Forum) toolPOJO;

	    // reset it to new toolContentId
	    toolContentObj.setContentId(toolContentId);
	    ForumUser user = forumUserDao.getByUserId(new Long(newUserUid.longValue()));
	    if (user == null) {
		user = new ForumUser();
		UserDTO sysUser = ((User) userManagementService.findById(User.class, newUserUid)).getUserDTO();
		user.setFirstName(sysUser.getFirstName());
		user.setLastName(sysUser.getLastName());
		user.setLoginName(sysUser.getLogin());
		user.setUserId(new Long(newUserUid.longValue()));
		this.createUser(user);
	    }
	    toolContentObj.setCreatedBy(user);
	    // save forum first
	    forumDao.saveOrUpdate(toolContentObj);

	    // save all authoring message one by one.
	    // reset all resourceItem createBy user
	    Set<Message> items = toolContentObj.getMessages();
	    int sequenceId = 1;
	    for (Message item : items) {
		item.setCreatedBy(user);
		item.setIsAuthored(true);
		item.setForum(toolContentObj);
		item.setSessionClones(new HashSet());
		// very old LDs did not have sequence IDs in Message and the default value is 0
		if (item.getSequenceId() == 0) {
		    item.setSequenceId(sequenceId++);
		}
		createRootTopic(toolContentObj.getUid(), (ForumToolSession) null, item);
	    }
	} catch (ImportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	Forum forum = getForumByContentId(toolContentId);
	if (forum == null) {
	    forum = getDefaultForum();
	}
	return getForumOutputFactory().getToolOutputDefinitions(forum, definitionType);
    }

    @Override
    public String getToolContentTitle(Long toolContentId) {
	return getForumByContentId(toolContentId).getTitle();
    }

    @Override
    public boolean isContentEdited(Long toolContentId) {
	return getForumByContentId(toolContentId).isDefineLater();
    }

    @Override
    public boolean isReadOnly(Long toolContentId) {
	for (ForumToolSession session : (List<ForumToolSession>) forumToolSessionDao.getByContentId(toolContentId)) {
	    for (ForumUser user : (List<ForumUser>) forumUserDao.getBySessionId(session.getSessionId())) {
		// we don't remove users in removeLearnerContent()
		// we just remove or hide messages
		if (!messageDao.getByUserAndSession(user.getUid(), session.getSessionId()).isEmpty()) {
		    return true;
		}
	    }
	}

	return false;
    }

    @Override
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	ForumToolSession session = new ForumToolSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);
	Forum forum = forumDao.getByContentId(toolContentId);
	session.setForum(forum);

	// also clone author created topic from this forum tool content!!!
	// this can avoid topic record information conflict when multiple sessions are against same tool content
	// for example, the reply number maybe various for different sessions.
	ForumService.log.debug("Clone tool content [" + forum.getContentId() + "] topics for session ["
		+ session.getSessionId() + "]");
	Set<Message> contentTopics = forum.getMessages();
	if ((contentTopics != null) && (contentTopics.size() > 0)) {
	    for (Message msg : contentTopics) {
		if (msg.getIsAuthored() && (msg.getToolSession() == null)) {
		    Message newMsg = Message.newInstance(msg);
		    msg.getSessionClones().add(newMsg);
		    createRootTopic(forum.getContentId(), session, newMsg);
		}
	    }
	}
	session.setStatus(ForumConstants.STATUS_CONTENT_COPYED);

	forumToolSessionDao.saveOrUpdate(session);
	if (ForumService.log.isDebugEnabled()) {
	    ForumService.log.debug("tool session [" + session.getSessionId() + "] created.");
	}
    }

    @Override
    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
	if (toolSessionId == null) {
	    ForumService.log.error("Fail to leave tool Session based on null tool session id.");
	    throw new ToolException("Fail to remove tool Session based on null tool session id.");
	}
	if (learnerId == null) {
	    ForumService.log.error("Fail to leave tool Session based on null learner.");
	    throw new ToolException("Fail to remove tool Session based on null learner.");
	}

	ForumToolSession session = forumToolSessionDao.getBySessionId(toolSessionId);
	if (session != null) {
	    forumToolSessionDao.saveOrUpdate(session);
	} else {
	    ForumService.log.error("Fail to leave tool Session.Could not find submit file "
		    + "session by given session id: " + toolSessionId);
	    throw new DataMissingException("Fail to leave tool Session."
		    + "Could not find submit file session by given session id: " + toolSessionId);
	}
	return toolService.completeToolSession(toolSessionId, learnerId);
    }

    @Override
    public ToolSessionExportOutputData exportToolSession(Long toolSessionId)
	    throws DataMissingException, ToolException {
	return null;
    }

    @Override
    public ToolSessionExportOutputData exportToolSession(List toolSessionIds)
	    throws DataMissingException, ToolException {
	return null;
    }

    @Override
    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	forumToolSessionDao.delete(toolSessionId);
    }

    @Override
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {

	return forumOutputFactory.getToolOutput(names, this, toolSessionId, learnerId);

    }

    @Override
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return forumOutputFactory.getToolOutput(name, this, toolSessionId, learnerId);
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

    @Override
    public Forum getDefaultContent(Long contentID) {
	if (contentID == null) {
	    String error = "Could not retrieve default content id for Forum tool";
	    ForumService.log.error(error);
	    throw new ForumException(error);
	}

	Forum defaultContent = getDefaultForum();
	// get default content by given ID.
	Forum content = Forum.newInstance(defaultContent, contentID);

	return content;
    }

    @Override
    public List<MessageDTO> getAllTopicsFromSession(Long sessionID) {
	return MessageDTO.getMessageDTO(messageDao.getBySession(sessionID));
    }

    /**
     * Sends marks straight to gradebook from a forum report
     *
     * @param user
     * @param toolSessionID
     */
    public void sendMarksToGradebook(ForumUser user, Long toolSessionID) {

	List<MessageDTO> messages = getMessagesByUserUid(user.getUid(), toolSessionID);
	if (messages != null) {
	    Float totalMark = null;
	    for (MessageDTO message : messages) {
		if (totalMark == null) {
		    totalMark = message.getMark();
		} else if (message.getMark() != null) {
		    totalMark += message.getMark();
		}
	    }
	    if (totalMark != null) {
		Double mark = new Double(totalMark);
		toolService.updateActivityMark(mark, null, user.getUserId().intValue(), toolSessionID, false);
	    }
	}

    }

    // ***************************************************************************************************************
    // Get / Set methods
    // ***************************************************************************************************************
    public ILamsToolService getToolService() {
	return toolService;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    public IAttachmentDAO getAttachmentDao() {
	return attachmentDao;
    }

    public void setAttachmentDao(IAttachmentDAO attachmentDao) {
	this.attachmentDao = attachmentDao;
    }

    public IForumDAO getForumDao() {
	return forumDao;
    }

    public void setForumDao(IForumDAO forumDao) {
	this.forumDao = forumDao;
    }

    public ITimestampDAO getTimestampDao() {
	return timestampDao;
    }

    public void setTimestampDao(ITimestampDAO timestampDao) {
	this.timestampDao = timestampDao;
    }

    public IMessageDAO getMessageDao() {
	return messageDao;
    }

    public void setMessageDao(IMessageDAO messageDao) {
	this.messageDao = messageDao;
    }

    public IMessageSeqDAO getMessageSeqDao() {
	return messageSeqDao;
    }

    public void setMessageSeqDao(IMessageSeqDAO messageSeqDao) {
	this.messageSeqDao = messageSeqDao;
    }

    public IMessageRatingDAO getMessageRatingDao() {
	return messageRatingDao;
    }

    public void setMessageRatingDao(IMessageRatingDAO messageRatingDao) {
	this.messageRatingDao = messageRatingDao;
    }

    public IForumToolSessionDAO getForumToolSessionDao() {
	return forumToolSessionDao;
    }

    public void setForumToolSessionDao(IForumToolSessionDAO forumToolSessionDao) {
	this.forumToolSessionDao = forumToolSessionDao;
    }

    public IForumUserDAO getForumUserDao() {
	return forumUserDao;
    }

    public void setForumUserDao(IForumUserDAO forumUserDao) {
	this.forumUserDao = forumUserDao;
    }

    public IToolContentHandler getForumToolContentHandler() {
	return forumToolContentHandler;
    }

    public void setForumToolContentHandler(IToolContentHandler toolContentHandler) {
	forumToolContentHandler = toolContentHandler;
    }

    public IExportToolContentService getExportContentService() {
	return exportContentService;
    }

    public void setExportContentService(IExportToolContentService exportContentService) {
	this.exportContentService = exportContentService;
    }

    public IUserManagementService getUserManagementService() {
	return userManagementService;
    }

    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
    }

    public ICoreNotebookService getCoreNotebookService() {
	return coreNotebookService;
    }

    public void setCoreNotebookService(ICoreNotebookService coreNotebookService) {
	this.coreNotebookService = coreNotebookService;
    }

    @Override
    public IEventNotificationService getEventNotificationService() {
	return eventNotificationService;
    }

    public void setEventNotificationService(IEventNotificationService eventNotificationService) {
	this.eventNotificationService = eventNotificationService;
    }

    @Override
    public String getLocalisedMessage(String key, Object[] args) {
	return messageService.getMessage(key, args);
    }

    public void setLessonService(ILessonService lessonService) {
	this.lessonService = lessonService;
    }

    public void setActivityDAO(IActivityDAO activityDAO) {
	this.activityDAO = activityDAO;
    }

    @Override
    public String createTextSearchConditionName(Collection<ForumCondition> existingConditions) {
	String uniqueNumber = null;
	do {
	    uniqueNumber = String.valueOf(Math.abs(generator.nextInt()));
	    for (ForumCondition condition : existingConditions) {
		String[] splittedName = getForumOutputFactory().splitConditionName(condition.getName());
		if (uniqueNumber.equals(splittedName[1])) {
		    uniqueNumber = null;
		}
	    }
	} while (uniqueNumber == null);
	return getForumOutputFactory().buildTopicDatesToAnswersConditionName(uniqueNumber);
    }

    /**
     * Get number of new postings.
     *
     * @param messageId
     * @param userId
     * @return
     */
    @Override
    public int getNewMessagesNum(Message message, Long userId) {
	Timestamp timestamp = timestampDao.getTimestamp(message.getUid(), userId);
	if (timestamp == null) {
	    // if first time - show all postings as new, including root message
	    return message.getReplyNumber() + 1;
	} else {
	    return messageSeqDao.getNumOfPostsNewerThan(message.getUid(), timestamp.getTimestamp());
	}
    }

    @Override
    public void saveTimestamp(Long rootTopicId, ForumUser forumUser) {
	Timestamp timestamp = timestampDao.getTimestamp(rootTopicId, forumUser.getUid());
	if (timestamp != null) {
	    timestamp.setTimestamp(new Date());
	} else {
	    timestamp = new Timestamp();
	    timestamp.setMessage(getMessage(rootTopicId));
	    timestamp.setTimestamp(new Date());
	    timestamp.setForumUser(forumUser);
	}
	timestampDao.saveOrUpdate(timestamp);
    }

    @Override
    public void sendNotificationsOnNewPosting(Long forumId, Long sessionId, Message message) {
	Forum forum = getForum(forumId);
	ForumUser postAuthor = message.getCreatedBy();
	String fullName = postAuthor.getLastName() + " " + postAuthor.getFirstName();
	ToolSession toolSession = toolService.getToolSession(sessionId);
	Long activityId = toolSession.getToolActivity().getActivityId();
	ToolActivity activity = (ToolActivity) activityDAO.getActivityByActivityId(activityId, ToolActivity.class);
	boolean isHtmlFormat = forum.isAllowRichEditor();

	if (forum.isNotifyLearnersOnForumPosting()) {
	    List<User> learners = lessonService.getLearnersAttemptedOrCompletedActivity(activity);
	    if ((learners != null) && !learners.isEmpty()) {
		ArrayList<Integer> learnerIds = new ArrayList<>();
		for (User learner : learners) {
		    learnerIds.add(learner.getUserId());
		}

		getEventNotificationService().sendMessage(null, learnerIds.toArray(new Integer[0]),
			IEventNotificationService.DELIVERY_METHOD_MAIL,
			getLocalisedMessage("event.newposting.subject", new Object[] { forum.getTitle() }),
			getLocalisedMessage("event.newposting.body", new Object[] { fullName, message.getBody() }),
			isHtmlFormat);
	    }
	}

	if (forum.isNotifyTeachersOnForumPosting()) {
	    String emailMessage = getLocalisedMessage("event.newposting.body",
		    new Object[] { fullName, message.getBody() });
	    getEventNotificationService().notifyLessonMonitors(sessionId, emailMessage, isHtmlFormat);
	}
    }

    @Override
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getForumOutputFactory().getSupportedDefinitionClasses(definitionType);
    }

    @Override
    public ToolCompletionStatus getCompletionStatus(Long learnerId, Long toolSessionId) {
	ForumUser learner = getUserByUserAndSession(learnerId, toolSessionId);
	if (learner == null) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_NOT_ATTEMPTED, null, null);
	}

	Object[] dates = messageDao.getDateRangeOfMessages(learner.getUid());
	if (learner.isSessionFinished()) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_COMPLETED, (Date) dates[0], (Date) dates[1]);
	} else {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_ATTEMPTED, (Date) dates[0], null);
	}
    }

    @Override
    public ForumConfigItem getConfigItem(String key) {
	List<ForumConfigItem> result = forumDao.findByProperty(ForumConfigItem.class, "configKey", key, true);
	return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public void saveForumConfigItem(ForumConfigItem item) {
	forumDao.insertOrUpdate(item);
    }

    // ****************** REST methods *************************

    /**
     * Used by the Rest calls to create content. Mandatory fields in toolContentJSON: title, instructions, topics.
     * Topics must contain a ArrayNode of ObjectNode objects, which have the following mandatory fields: subject, body
     * There will usually be at least one topic object in the Topics array but the array may be of zero length.
     */
    @Override
    public void createRestToolContent(Integer userID, Long toolContentID, ObjectNode toolContentJSON) {

	Forum forum = new Forum();
	Date updateDate = new Date();
	forum.setCreated(updateDate);
	forum.setUpdated(updateDate);

	forum.setContentId(toolContentID);
	forum.setTitle(JsonUtil.optString(toolContentJSON, RestTags.TITLE));
	forum.setInstructions(JsonUtil.optString(toolContentJSON, RestTags.INSTRUCTIONS));

	forum.setAllowAnonym(JsonUtil.optBoolean(toolContentJSON, "allowAnonym", Boolean.FALSE));
	forum.setAllowEdit(JsonUtil.optBoolean(toolContentJSON, "allowEdit", Boolean.TRUE)); // defaults to true in the default
	// entry in the db
	forum.setAllowNewTopic(JsonUtil.optBoolean(toolContentJSON, "allowNewTopic", Boolean.TRUE)); // defaults to true in the
	// default entry in the db
	forum.setAllowRateMessages(JsonUtil.optBoolean(toolContentJSON, "allowRateMessages", Boolean.FALSE));
	forum.setAllowRichEditor(JsonUtil.optBoolean(toolContentJSON, RestTags.ALLOW_RICH_TEXT_EDITOR, Boolean.FALSE));
	forum.setAllowUpload(JsonUtil.optBoolean(toolContentJSON, "allowUpload", Boolean.FALSE));
	forum.setContentInUse(false);
	forum.setDefineLater(false);
	forum.setLimitedMaxCharacters(JsonUtil.optBoolean(toolContentJSON, "limitedMaxCharacters", Boolean.TRUE));
	forum.setLimitedMinCharacters(JsonUtil.optBoolean(toolContentJSON, "limitedMinCharacters", Boolean.FALSE));
	forum.setLockWhenFinished(JsonUtil.optBoolean(toolContentJSON, "lockWhenFinished", Boolean.FALSE));
	forum.setMaxCharacters(JsonUtil.optInt(toolContentJSON, "maxCharacters", 5000)); // defaults to 5000 chars in the
											 // default entry in the db.
	forum.setMaximumRate(JsonUtil.optInt(toolContentJSON, "maximumRate", 0));
	forum.setMaximumReply(JsonUtil.optInt(toolContentJSON, "maximumReply", 0));
	forum.setMinCharacters(JsonUtil.optInt(toolContentJSON, "minCharacters", 0));
	forum.setMinimumRate(JsonUtil.optInt(toolContentJSON, "minimumRate", 0));
	forum.setMinimumReply(JsonUtil.optInt(toolContentJSON, "minimumReply", 0));
	forum.setNotifyLearnersOnForumPosting(
		JsonUtil.optBoolean(toolContentJSON, "notifyLearnersOnForumPosting", Boolean.FALSE));
	forum.setNotifyLearnersOnMarkRelease(
		JsonUtil.optBoolean(toolContentJSON, "notifyLearnersOnMarkRelease", Boolean.FALSE));
	forum.setNotifyTeachersOnForumPosting(
		JsonUtil.optBoolean(toolContentJSON, "notifyTeachersOnForumPosting", Boolean.FALSE));
	forum.setReflectInstructions(JsonUtil.optString(toolContentJSON, RestTags.REFLECT_INSTRUCTIONS));
	forum.setReflectOnActivity(JsonUtil.optBoolean(toolContentJSON, RestTags.REFLECT_ON_ACTIVITY, Boolean.FALSE));
	// submissionDeadline is set in monitoring

	// *******************************Handle user*******************
	// Code taken from AuthoringAction TODO
	//	    String contentFolderID = (String) sessionMap.get(AttributeNames.PARAM_CONTENT_FOLDER_ID);
	// check whether it is sysadmin:LDEV-906
	//	if (!StringUtils.equals(contentFolderID, "-1")) {
	// try to get form system session
	//		HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	//		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	ForumUser forumUser = getUserByID(userID.longValue());
	if (forumUser == null) {
	    forumUser = new ForumUser(userID.longValue(), JsonUtil.optString(toolContentJSON, "firstName"),
		    JsonUtil.optString(toolContentJSON, "lastName"), JsonUtil.optString(toolContentJSON, "loginName"));
	    getForumUserDao().save(forumUser);
	}
	forum.setCreatedBy(forumUser);

	updateForum(forum);

	// **************************** Handle topic *********************
	ArrayNode topics = JsonUtil.optArray(toolContentJSON, "topics");
	for (int i = 0; i < topics.size(); i++) {
	    ObjectNode msgData = (ObjectNode) topics.get(i);
	    Message newMsg = new Message();
	    //	    newMsg.setAttachments(attachments); TODO
	    newMsg.setCreatedBy(forumUser);
	    newMsg.setCreated(updateDate);
	    newMsg.setModifiedBy(null);
	    newMsg.setUpdated(updateDate);

	    newMsg.setSubject(JsonUtil.optString(msgData, "subject"));
	    newMsg.setBody(JsonUtil.optString(msgData, "body"));
	    newMsg.setForum(forum);
	    newMsg.setHideFlag(false);
	    // newMsg.setIsAnonymous(false); Does not appear on authoring interface
	    newMsg.setIsAuthored(true);
	    newMsg.setLastReplyDate(updateDate);
	    newMsg.setParent(null);
	    newMsg.setReplyNumber(0);
	    newMsg.setReport(null);
	    newMsg.setSequenceId(i);
	    //	    newMsg.setSessionClones(sessionClones); Used for updating in monitoring
	    newMsg.setToolSession(null);
	    createRootTopic(forum.getUid(), (ForumToolSession) null, newMsg);
	}

	// *******************************
	// TODO - investigate
	//forum.setConditions(conditions);
	// message attachments
    }
}