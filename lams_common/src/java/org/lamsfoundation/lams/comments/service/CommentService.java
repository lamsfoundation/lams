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

/* $Id$ */
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
import org.lamsfoundation.lams.comments.util.CommentConstants;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.audit.IAuditService;

/**
 * 
 * Comments Widget Service 
 * 
 * @author Fiona Malikoff
 * 
 */
public class CommentService implements ICommentService {

    private static Logger log = Logger.getLogger(CommentService.class);

    // Services
    private IUserManagementService userService;
    private MessageService messageService;
    private IAuditService auditService;
    private ICommentSessionDAO commentSessionDAO;
    private ICommentDAO commentDAO;
    private ICommentLikeDAO commentLikeDAO;

    @Override
    public List<CommentDTO> getTopicThread(Long externalId, Integer externalType, String externalSignature, Long lastCommentSeqId, Integer pageSize, Integer userId) {

	long lastThreadMessageUid = lastCommentSeqId != null ? lastCommentSeqId.longValue() : 0L;
	Integer usePagingSize = pageSize != null ? pageSize : CommentConstants.DEFAULT_PAGE_SIZE;
	
	// hidden root of all the threads!
	Comment rootTopic = commentDAO.getRootTopic(externalId, externalType, externalSignature);

	// first time through - no root topic.
	if ( rootTopic == null ) {
	    return new ArrayList<CommentDTO>();
	}

	SortedSet<Comment> comments =  commentDAO.getNextThreadByThreadId(rootTopic.getUid(), lastThreadMessageUid, usePagingSize, userId);
	return getSortedCommentDTO(comments);
    }
    
    private List<CommentDTO> getSortedCommentDTO(SortedSet<Comment>  comments) {
	
	List<CommentDTO> msgDtoList = new ArrayList<CommentDTO>();
	for ( Comment comment : comments ) {
	    CommentDTO dto = CommentDTO.getCommentDTO(comment);
	    dto.setLevel(comment.getCommentLevel());
	    dto.setThreadNum(comment.getThreadComment().getUid().intValue());
	    msgDtoList.add(dto);
	}
	return msgDtoList;
    }

    public List<CommentDTO> getThread( Long threadId, Integer userId ) {	
	SortedSet<Comment> comments =  commentDAO.getThreadByThreadId(threadId, userId);
	return getSortedCommentDTO(comments);
    }

    // Do we need to synchronize this method? Would be nice but it is the equivalent of tool session creation 
    // and we don't synchonize them!
    public Comment createOrGetRoot(Long externalId, Integer externalIdType, String externalSignature, User user) {
	Comment rootComment = commentDAO.getRootTopic(externalId, externalIdType, externalSignature);
	return ( rootComment != null ? rootComment : 
	    createRoot(externalId, externalIdType, externalSignature, user) );
    }
    
    public Comment getRoot(Long externalId, Integer externalIdType, String externalSignature) {
	return commentDAO.getRootTopic(externalId, externalIdType, externalSignature);
    }

    private Comment createRoot(Long externalId, Integer externalIdType, String externalSignature, User user ) {

	CommentSession session = new CommentSession();
	session.setExternalId(externalId);
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
    
    public Comment createReply(Comment parent, String replyText, User user) {

	Comment replyMessage = new Comment();
	replyMessage.setBody(replyText);
	replyMessage.setHideFlag(false);
	replyMessage.updateModificationData(user);
	
	replyMessage.setParent(parent);
	replyMessage.setSession(parent.getSession());

	Comment root = parent.getRootComment();
	replyMessage.setCommentLevel((short) (parent.getCommentLevel() + 1));
	replyMessage.setRootComment(root);

	// look back up through the parents to find the thread top - will be level 1
	if ( replyMessage.getCommentLevel() == 1 ) {
	    replyMessage.setThreadComment(replyMessage);
	} else {
	    Comment threadComment = parent;
	    while ( threadComment.getCommentLevel() > 1 ) {
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
    
    public boolean addLike(Long commentUid, User user, Integer likeVote) {

 	return commentLikeDAO.addLike(commentUid, user.getUserId(), likeVote);
     }

    public Comment hideComment(Long commentUid, User user, boolean status) {

 	Comment comment = commentDAO.getById(commentUid);
 	comment.setHideFlag(status);
 	commentDAO.saveOrUpdate(comment);
 	return comment;
     }


    public Comment createReply(Long parentId, String replyText, User user) {
	
	Comment parent = commentDAO.getById(parentId);
	return(createReply(parent, replyText, user));

    }
    
    public Comment updateComment(Long commentUid, String newBody, User user, boolean makeAuditEntry) {
	Comment comment = commentDAO.getById(commentUid);

	if ( comment != null && user != null ) {
	    if ( makeAuditEntry ) {
		Long userId = 0L;
		String loginName = "Default";
		if (comment.getCreatedBy() != null) {
		    userId = comment.getCreatedBy().getUserId().longValue();
		    loginName = comment.getCreatedBy().getLogin();
		}
		getAuditService().logChange(CommentConstants.MODULE_NAME, userId, loginName,
			    comment.getBody(), newBody);
	    }
	    
	    comment.setBody(newBody);
	    comment.updateModificationData(user);
	    commentDAO.saveOrUpdate(comment);

	    return comment;
	} else {
	    log.error("Unable to update comment as comment not found or user missing. Comment uid "+commentUid
		    +" new body "+newBody
		    +" user "+(user!=null?user.getLogin():" missing"));
	    return null;
	}
    }
    
    public CommentDTO getComment(Long commentUid){
	Comment comment = commentDAO.getById(commentUid);
	return comment != null ? CommentDTO.getCommentDTO(comment) : null;
    }

    public IUserManagementService getUserService() {
	return userService;
    }

    public void setUserService(IUserManagementService userService) {
	this.userService = userService;
    }


    public MessageService getMessageService() {
	return messageService;
    }

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    public IAuditService getAuditService() {
        return auditService;
    }

    public void setAuditService(IAuditService auditService) {
        this.auditService = auditService;
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
