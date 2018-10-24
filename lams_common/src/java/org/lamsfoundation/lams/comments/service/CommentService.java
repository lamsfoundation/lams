/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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


package org.lamsfoundation.lams.comments.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.comments.Comment;
import org.lamsfoundation.lams.comments.CommentSession;
import org.lamsfoundation.lams.comments.dao.ICommentDAO;
import org.lamsfoundation.lams.comments.dao.ICommentLikeDAO;
import org.lamsfoundation.lams.comments.dao.ICommentSessionDAO;
import org.lamsfoundation.lams.comments.dto.CommentDTO;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;

/**
 *
 * Comments Widget Service
 *
 * @author Fiona Malikoff
 *
 */
public class CommentService implements ICommentService {

    private static Logger log = Logger.getLogger(CommentService.class);

    private static final String MODULE_NAME = "Comments";

    // Services
    private IUserManagementService userService;
    private MessageService messageService;
    private ILogEventService logEventService;
    private ILamsToolService toolService;
    private ICommentSessionDAO commentSessionDAO;
    private ICommentDAO commentDAO;
    private ICommentLikeDAO commentLikeDAO;

    @Override
    public List<CommentDTO> getTopicThread(Long externalId, Long externalSecondaryId, Integer externalType,
	    String externalSignature, Long lastCommentSeqId, Integer pageSize, Integer sortBy, String extraSortParam,
	    Integer userId) {

	long lastThreadMessageUid = lastCommentSeqId != null ? lastCommentSeqId.longValue() : 0L;

	// hidden root of all the threads!
	Comment rootTopic = commentDAO.getRootTopic(externalId, externalSecondaryId, externalType, externalSignature);

	// first time through - no root topic.
	if (rootTopic == null) {
	    return new ArrayList<CommentDTO>();
	}

	SortedSet<Comment> comments = commentDAO.getNextThreadByThreadId(rootTopic.getUid(), lastThreadMessageUid,
		pageSize, sortBy, extraSortParam, userId);
	return getSortedCommentDTO(comments);
    }

    @Override
    public List<CommentDTO> getTopicStickyThread(Long externalId, Long externalSecondaryId, Integer externalType, String externalSignature,
	    Integer sortBy, String extraSortParam, Integer userId) {

	// hidden root of all the threads!
	Comment rootTopic = commentDAO.getRootTopic(externalId, externalSecondaryId, externalType, externalSignature);

	// first time through - no root topic.
	if (rootTopic == null) {
	    return new ArrayList<CommentDTO>();
	}

	SortedSet<Comment> comments = commentDAO.getStickyThreads(rootTopic.getUid(), sortBy, extraSortParam, userId);
	return getSortedCommentDTO(comments);
    }

    private List<CommentDTO> getSortedCommentDTO(SortedSet<Comment> comments) {

	List<CommentDTO> msgDtoList = new ArrayList<CommentDTO>();
	for (Comment comment : comments) {
	    CommentDTO dto = CommentDTO.getCommentDTO(comment);
	    dto.setLevel(comment.getCommentLevel());
	    dto.setThreadNum(comment.getThreadComment().getUid().intValue());
	    msgDtoList.add(dto);
	}
	return msgDtoList;
    }

    @Override
    public List<CommentDTO> getThread(Long threadId, Integer sortBy, Integer userId) {
	SortedSet<Comment> comments = commentDAO.getThreadByThreadId(threadId, sortBy, userId);
	return getSortedCommentDTO(comments);
    }

    // Do we need to synchronize this method? Would be nice but it is the equivalent of tool session creation
    // and we don't synchonize them!
    @Override
    public Comment createOrGetRoot(Long externalId, Long externalSecondaryId, Integer externalIdType, String externalSignature, User user) {
	Comment rootComment = commentDAO.getRootTopic(externalId, externalSecondaryId, externalIdType, externalSignature);
	return (rootComment != null ? rootComment : createRoot(externalId, externalSecondaryId, externalIdType, externalSignature, user));
    }

    @Override
    public Comment getRoot(Long externalId, Long externalSecondaryId, Integer externalIdType, String externalSignature) {
	return commentDAO.getRootTopic(externalId, externalSecondaryId, externalIdType, externalSignature);
    }

    private Comment createRoot(Long externalId, Long externalSecondaryId, Integer externalIdType, String externalSignature, User user) {

	CommentSession session = new CommentSession();
	session.setExternalId(externalId);
	session.setExternalSecondaryId(externalSecondaryId);
	session.setExternalIdType(externalIdType);
	session.setExternalSignature(externalSignature);

	Comment comment = new Comment();
	comment.setBody("Hidden Root Message");
	comment.setHideFlag(true);
	comment.setReplyNumber(0); // no replies yet
	comment.setSession(session);
	comment.updateModificationData(user);
	comment.setCommentLevel((short) 0);
	comment.setRootComment(comment);
	comment.setThreadComment(null); // this one is not part of a thread!

	commentSessionDAO.save(session);
	commentDAO.saveOrUpdate(comment);
	return comment;
    }

    @Override
    public Comment createReply(Comment parent, String replyText, User user, boolean isMonitor, boolean isAnonymous) {

	Comment replyMessage = new Comment();
	replyMessage.setBody(replyText);
	replyMessage.setHideFlag(false);
	replyMessage.updateModificationData(user);
	replyMessage.setMonitor(isMonitor);
	replyMessage.setAnonymous(isAnonymous);

	replyMessage.setParent(parent);
	replyMessage.setSession(parent.getSession());

	Comment root = parent.getRootComment();
	replyMessage.setCommentLevel((short) (parent.getCommentLevel() + 1));
	replyMessage.setRootComment(root);

	// look back up through the parents to find the thread top - will be level 1
	if (replyMessage.getCommentLevel() == 1) {
	    replyMessage.setThreadComment(replyMessage);
	} else {
	    Comment threadComment = parent;
	    while (threadComment.getCommentLevel() > 1) {
		threadComment = threadComment.getParent();
	    }
	    replyMessage.setThreadComment(threadComment);
	}

	commentDAO.saveOrUpdate(replyMessage);

	// update last reply date for root message
	root.setLastReplyDate(new Date());
	// update reply message number for root
	root.setReplyNumber(root.getReplyNumber() + 1);
	commentDAO.saveOrUpdate(root);

	return replyMessage;
    }

    @Override
    public boolean addLike(Long commentUid, User user, Integer likeVote) {

	return commentLikeDAO.addLike(commentUid, user.getUserId(), likeVote);
    }

    @Override
    public Comment hideComment(Long commentUid, User user, boolean status) {

	Comment comment = commentDAO.getById(commentUid);
	comment.setHideFlag(status);
	commentDAO.saveOrUpdate(comment);

	Long learnerUserId = 0L;
	String loginName = "Default";
	if (comment.getCreatedBy() != null) {
	    learnerUserId = comment.getCreatedBy().getUserId().longValue();
	    loginName = comment.getCreatedBy().getLogin();
	}
	Long toolContentId = getToolContentIdForAuditing(commentUid, comment);
	if ( status )
	    getLogEventService().logHideLearnerContent(learnerUserId, loginName, toolContentId, comment.toString());
	else
	    getLogEventService().logShowLearnerContent(learnerUserId, loginName, toolContentId, comment.toString());
	    
	return comment;
    }

    @Override
    public Comment createReply(Long parentId, String replyText, User user, boolean isMonitor, boolean isAnonymous) {

	Comment parent = commentDAO.getById(parentId);
	return (createReply(parent, replyText, user, isMonitor, isAnonymous));

    }

    @Override
    // if isAnonymous is null, do not update the field.
    public Comment updateComment(Long commentUid, String newBody, User user, Boolean isAnonymous, boolean makeAuditEntry) {
	Comment comment = commentDAO.getById(commentUid);

	if (comment != null && user != null) {
	    if (makeAuditEntry) {
		Long learnerUserId = 0L;
		String loginName = "Default";
		if (comment.getCreatedBy() != null) {
		    learnerUserId = comment.getCreatedBy().getUserId().longValue();
		    loginName = comment.getCreatedBy().getLogin();
		}
		Long toolContentId = getToolContentIdForAuditing(commentUid, comment);
		getLogEventService().logChangeLearnerContent(learnerUserId, loginName, toolContentId, comment.getBody(),
			newBody);
	    }

	    comment.setBody(newBody);
	    comment.updateModificationData(user);
	    if ( isAnonymous != null )
		comment.setAnonymous(isAnonymous);
	    commentDAO.saveOrUpdate(comment);

	    return comment;
	} else {
	    log.error("Unable to update comment as comment not found or user missing. Comment uid " + commentUid
		    + " new body " + newBody + " user " + (user != null ? user.getLogin() : " missing"));
	    return null;
	}
    }

    private Long getToolContentIdForAuditing(Long commentUid, Comment comment) {
	Long toolContentId = null;
	String externalSignature = comment.getSession().getExternalSignature();
	if (externalSignature != null) {
	    Long externalId = comment.getSession().getExternalId(); // current this is toolSessionId
	    ToolSession session = getToolService().getToolSession(externalId);
	    if (session != null
		    && externalSignature.equals(session.getToolActivity().getTool().getToolSignature())) {
		// consistent session id and tool signature so we should be safe. If this fails we have something
		// else but a tool using the comment module
		toolContentId = session.getToolActivity().getToolContentId();
	    }
	}
	if (toolContentId == null) {
	    log.error(
		    "Unexpected data for comment. Will create incomplete audit entry - cannot determine toolContentId. CommentUid="
			    + commentUid);
	}
	return toolContentId;
    }

    @Override
    public Comment updateSticky(Long commentUid, Boolean newSticky) {

	Comment comment = commentDAO.getById(commentUid);
	if (comment != null) {
	    comment.setSticky(newSticky);
	    commentDAO.saveOrUpdate(comment);
	    return comment;
	} else {
	    log.error("Unable to update comment as comment not found. Comment uid " + commentUid + " new sticky "
		    + newSticky);
	    return null;
	}
    }

    @Override
    public CommentDTO getComment(Long commentUid) {
	Comment comment = commentDAO.getById(commentUid);
	return comment != null ? CommentDTO.getCommentDTO(comment) : null;
    }

    public IUserManagementService getUserService() {
	return userService;
    }

    public void setUserService(IUserManagementService userService) {
	this.userService = userService;
    }

    @Override
    public MessageService getMessageService() {
	return messageService;
    }

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    public ILogEventService getLogEventService() {
	return logEventService;
    }

    public void setLogEventService(ILogEventService logEventService) {
	this.logEventService = logEventService;
    }

    public ILamsToolService getToolService() {
        return toolService;
    }

    public void setToolService(ILamsToolService toolService) {
        this.toolService = toolService;
    }

    public ICommentSessionDAO getCommentSessionDAO() {
	return commentSessionDAO;
    }

    public void setCommentSessionDAO(ICommentSessionDAO commentSessionDAO) {
	this.commentSessionDAO = commentSessionDAO;
    }

    public ICommentDAO getCommentDAO() {
	return commentDAO;
    }

    public void setCommentDAO(ICommentDAO commentDAO) {
	this.commentDAO = commentDAO;
    }

    public ICommentLikeDAO getCommentLikeDAO() {
	return commentLikeDAO;
    }

    public void setCommentLikeDAO(ICommentLikeDAO commentLikeDAO) {
	this.commentLikeDAO = commentLikeDAO;
    }

    // -------------------------------------------------------------------------
}
