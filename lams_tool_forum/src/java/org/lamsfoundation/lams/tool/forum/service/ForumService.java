package org.lamsfoundation.lams.tool.forum.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.contentrepository.AccessDeniedException;
import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.LoginException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.WorkspaceNotFoundException;
import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;
import org.lamsfoundation.lams.contentrepository.service.SimpleCredentials;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.forum.core.PersistenceException;
import org.lamsfoundation.lams.tool.forum.persistence.Attachment;
import org.lamsfoundation.lams.tool.forum.persistence.AttachmentDao;
import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.ForumDao;
import org.lamsfoundation.lams.tool.forum.persistence.ForumException;
import org.lamsfoundation.lams.tool.forum.persistence.Message;
import org.lamsfoundation.lams.tool.forum.persistence.MessageDao;
import org.lamsfoundation.lams.tool.forum.util.ForumConstants;
import org.lamsfoundation.lams.tool.forum.util.ForumToolContentHandler;
import org.lamsfoundation.lams.usermanagement.User;


/**
 * 
 * @author Steve.Ni
 * 
 * @version $Revision$
 */
public class ForumService implements IForumService,ToolContentManager,ToolSessionManager {
	private ForumDao forumDao;
	private AttachmentDao attachmentDao;
	private MessageDao messageDao;
	private ForumToolContentHandler toolContentHandler;
	private IRepositoryService repositoryService;
	
    public Forum createForum(Forum forum, Map attachments, Map topics) throws PersistenceException {
        if (attachments != null && attachments.size() !=0) {
            Set documents = new HashSet();
            Collection attachmentList = attachments.values();
            Iterator it = attachmentList.iterator();
            while (it.hasNext()) {
                Attachment attachment = (Attachment) it.next();
                attachmentDao.saveOrUpdate(attachment);
                documents.add(attachment);
            }
            forum.setAttachments(documents);
        }
        forumDao.saveOrUpdate(forum);

        //save topics of forum
        if (topics != null && topics.size() !=0) {
          Collection topicList = topics.values();
          Iterator it = topicList.iterator();
          while (it.hasNext()) {
                Message message = (Message) it.next();
                message.setIsAuthored(true);
                this.createMessage(forum.getUid(), message);
          }
        }
        return forum;
    }

    public Forum editForum(Forum forum, Map attachments, Map topics) throws PersistenceException {
        Forum reloaded = this.getForum(forum.getUid());
        reloaded.setTitle(forum.getTitle());
        reloaded.setCreatedBy(forum.getCreatedBy());
        reloaded.setLockWhenFinished(forum.getLockWhenFinished());
        reloaded.setRunOffline(forum.getRunOffline());
        reloaded.setAllowAnonym(forum.getAllowAnonym());
        reloaded.setInstructions(forum.getInstructions());
        reloaded.setOnlineInstructions(forum.getOnlineInstructions());
        reloaded.setOfflineInstructions(forum.getOfflineInstructions());
        if (attachments != null && attachments.size() !=0) {
            Set documents = reloaded.getAttachments();
            Collection attachmentList = attachments.values();
            Iterator it = attachmentList.iterator();
            while (it.hasNext()) {
                Attachment attachment = (Attachment) it.next();
                attachmentDao.saveOrUpdate(attachment);
                documents.add(attachment);
            }
            forum.setAttachments(documents);
        }
        forumDao.saveOrUpdate(reloaded);

        //save topics of forum
        if (topics != null && topics.size() !=0) {
          Collection topicList = topics.values();
          Iterator it = topicList.iterator();
          while (it.hasNext()) {
                Message message = (Message) it.next();
                this.createMessage(forum.getUid(), message);
          }
        }

        return forum;
    }

    public Forum getForum(Long forumId) throws PersistenceException {
        return (Forum) forumDao.getById(forumId);
    }

	public Forum getForumByContentId(Long contentID)  throws PersistenceException {
		return (Forum) forumDao.getByContentId(contentID);
	}
    public void deleteForum(Long forumId) throws PersistenceException {
        Forum forum = this.getForum(forumId);
        forumDao.delete(forum);
    }

    public List getTopics(Long forumId) throws PersistenceException {
        return messageDao.findByNamedQuery("allAuthoredMessagesOfForum", forumId);
    }

    public void deleteForumAttachment(Long attachmentId) throws PersistenceException {
        Attachment attachment = (Attachment) attachmentDao.getById(attachmentId);
        attachmentDao.delete(attachment);

    }

    public Message createMessage(Long forumId, Message message) throws PersistenceException {
    	//TODO: need fix
//        message.setToolSession(this.getForum(forumId));
        messageDao.saveOrUpdate(message);
        return message;
    }

     public Message editMessage(Message message) throws PersistenceException {
        Message reloaded = this.getMessage(message.getUid());
        reloaded.setModifiedBy(message.getModifiedBy());
        reloaded.setIsAnonymous(message.getIsAnonymous());
        reloaded.setIsAuthored(message.getIsAuthored());
        reloaded.setSubject(message.getSubject());
        reloaded.setBody(message.getBody());
        messageDao.saveOrUpdate(message);
        return message;
    }

    public Message getMessage(Long messageId) throws PersistenceException {
        return (Message) messageDao.getById(messageId);
    }

    public void deleteMessage(Long messageId) throws PersistenceException {
        Message message = this.getMessage(messageId);
        messageDao.delete(message);
     }

    public Message replyToMessage(Long messageId, Message replyMessage) throws PersistenceException {
        Message message = this.getMessage(messageId);
        replyMessage.setToolSession(message.getToolSession());
        replyMessage.setParent(message);
        messageDao.saveOrUpdate(replyMessage);
        Set replies = message.getReplies();
        if (replies == null) {
            replies = new HashSet();
        }
        replies.add(replyMessage);
        message.setReplies(replies);
        messageDao.saveOrUpdate(message);
        return replyMessage;
    }

	public AttachmentDao getAttachmentDao() {
		return attachmentDao;
	}

	public void setAttachmentDao(AttachmentDao attachmentDao) {
		this.attachmentDao = attachmentDao;
	}

	public ForumDao getForumDao() {
		return forumDao;
	}

	public void setForumDao(ForumDao forumDao) {
		this.forumDao = forumDao;
	}

	public MessageDao getMessageDao() {
		return messageDao;
	}

	public void setMessageDao(MessageDao messageDao) {
		this.messageDao = messageDao;
	}

	public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {
	}

	public void setAsDefineLater(Long toolContentId) throws DataMissingException, ToolException {
	}

	public void setAsRunOffline(Long toolContentId) throws DataMissingException, ToolException {
	}

	public void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException, ToolException {
	}

	public void createToolSession(Long toolSessionId, Long toolContentId) throws ToolException {
	}

	public String leaveToolSession(Long toolSessionId, User learner) throws DataMissingException, ToolException {
		return null;
	}

	public ToolSessionExportOutputData exportToolSession(Long toolSessionId) throws DataMissingException, ToolException {
		return null;
	}

	public ToolSessionExportOutputData exportToolSession(List toolSessionIds) throws DataMissingException, ToolException {
		return null;
	}

	public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	}

	public Attachment uploadInstructionFile(Long contentId, FormFile uploadFile, String fileType) throws PersistenceException{
		Attachment refile = null;
		if(uploadFile == null || StringUtils.isEmpty(uploadFile.getFileName()))
			throw new ForumException("Could not find upload file: " + uploadFile);
		
        Forum content = getForum(contentId);
        if ( content == null || !contentId.equals(content.getContentId())) {
        	content  = new Forum();
        	content.setContentId(contentId);
        	//user firstly upload file without any other input, even the not-null 
        	//field "title". Set title as default title.
        	content.setTitle(ForumConstants.DEFAULT_TITLE);
        }
		NodeKey nodeKey = processFile(uploadFile,fileType);
		
		Set fileSet = content.getAttachments();
		if(fileSet == null){
			fileSet = new HashSet();
			content.setAttachments(fileSet);
		}
		Attachment file = new Attachment();
		file.setType(fileType);
		file.setUid(nodeKey.getUuid());
		file.setVersionId(nodeKey.getVersion());
		file.setFileName(uploadFile.getFileName());
		fileSet.add(file);
		forumDao.saveOrUpdate(content);
		
		refile = new Attachment();
		try {
			PropertyUtils.copyProperties(refile,file);
		} catch (Exception e) {
			throw new ForumException("Could not get return InstructionFile instance" +e.getMessage());
		}
		return refile;

	}
    /**
     * Process an uploaded file.
     * 
     * @param forumForm
     * @throws FileNotFoundException
     * @throws IOException
     * @throws RepositoryCheckedException
     * @throws InvalidParameterException
     */
    private NodeKey processFile(FormFile file, String fileType){
    	NodeKey node = null;
        if (file!= null && !StringUtils.isEmpty(file.getFileName())) {
            String fileName = file.getFileName();
            try {
				node = getToolContentHandler().uploadFile(file.getInputStream(), fileName, 
				        file.getContentType(), fileType);
			} catch (InvalidParameterException e) {
				throw new ForumException("FileNotFoundException occured while trying to upload File" + e.getMessage());
			} catch (FileNotFoundException e) {
				throw new ForumException("FileNotFoundException occured while trying to upload File" + e.getMessage());
			} catch (RepositoryCheckedException e) {
				throw new ForumException("FileNotFoundException occured while trying to upload File" + e.getMessage());
			} catch (IOException e) {
				throw new ForumException("FileNotFoundException occured while trying to upload File" + e.getMessage());
			}
          }
        return node;
    }

	/**
	 * This method deletes the content with the given <code>uuid</code> and
	 * <code>versionID</code> from the content repository
	 * 
	 * @param uuid
	 *            The <code>uuid</code> of the node to be deleted
	 * @param versionID
	 *            The <code>version_id</code> of the node to be deleted.
	 * @throws SubmitFilesException
	 */
	public void deleteFromRepository(Long uuid, Long versionID)
			throws ForumException {
		ITicket ticket = getRepositoryLoginTicket();
		try {
			repositoryService.deleteVersion(ticket, uuid,versionID);
		} catch (Exception e) {
			throw new ForumException(
					"Exception occured while deleting files from"
							+ " the repository " + e.getMessage());
		}
	}

	public void deleteInstructionFile(Long contentID, Long uuid, Long versionID, String type){
		forumDao.deleteInstrcutionFile(contentID, uuid, versionID, type);
	}

	public ForumToolContentHandler getToolContentHandler() {
		return toolContentHandler;
	}

	public void setToolContentHandler(ForumToolContentHandler toolContentHandler) {
		this.toolContentHandler = toolContentHandler;
	}
	/**
	 * This method verifies the credentials of the SubmitFiles Tool and gives it
	 * the <code>Ticket</code> to login and access the Content Repository.
	 * 
	 * A valid ticket is needed in order to access the content from the
	 * repository. This method would be called evertime the tool needs to
	 * upload/download files from the content repository.
	 * 
	 * @return ITicket The ticket for repostory access
	 * @throws SubmitFilesException
	 */
	private ITicket getRepositoryLoginTicket() throws ForumException {
		ICredentials credentials = new SimpleCredentials(
				toolContentHandler.getRepositoryUser(),
				toolContentHandler.getRepositoryId());
		try {
			ITicket ticket = repositoryService.login(credentials,
					toolContentHandler.getRepositoryWorkspaceName());
			return ticket;
		} catch (AccessDeniedException ae) {
			throw new ForumException("Access Denied to repository."
					+ ae.getMessage());
		} catch (WorkspaceNotFoundException we) {
			throw new ForumException("Workspace not found."
					+ we.getMessage());
		} catch (LoginException e) {
			throw new ForumException("Login failed." + e.getMessage());
		}
	}

	public IRepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(IRepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
}
