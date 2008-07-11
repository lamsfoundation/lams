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
package org.lamsfoundation.lams.tool.spreadsheet.service;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.contentrepository.AccessDeniedException;
import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.contentrepository.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.LoginException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.WorkspaceNotFoundException;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;
import org.lamsfoundation.lams.contentrepository.service.SimpleCredentials;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.tool.ToolContentImport102Manager;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.spreadsheet.SpreadsheetConstants;
import org.lamsfoundation.lams.tool.spreadsheet.dao.SpreadsheetAttachmentDAO;
import org.lamsfoundation.lams.tool.spreadsheet.dao.SpreadsheetDAO;
import org.lamsfoundation.lams.tool.spreadsheet.dao.SpreadsheetMarkDAO;
import org.lamsfoundation.lams.tool.spreadsheet.dao.UserModifiedSpreadsheetDAO;
import org.lamsfoundation.lams.tool.spreadsheet.dao.SpreadsheetSessionDAO;
import org.lamsfoundation.lams.tool.spreadsheet.dao.SpreadsheetUserDAO;
import org.lamsfoundation.lams.tool.spreadsheet.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.spreadsheet.dto.StatisticDTO;
import org.lamsfoundation.lams.tool.spreadsheet.dto.Summary;
import org.lamsfoundation.lams.tool.spreadsheet.model.Spreadsheet;
import org.lamsfoundation.lams.tool.spreadsheet.model.SpreadsheetAttachment;
import org.lamsfoundation.lams.tool.spreadsheet.model.SpreadsheetMark;
import org.lamsfoundation.lams.tool.spreadsheet.model.UserModifiedSpreadsheet;
import org.lamsfoundation.lams.tool.spreadsheet.model.SpreadsheetSession;
import org.lamsfoundation.lams.tool.spreadsheet.model.SpreadsheetUser;
import org.lamsfoundation.lams.tool.spreadsheet.util.ReflectDTOComparator;
import org.lamsfoundation.lams.tool.spreadsheet.util.SpreadsheetToolContentHandler;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.util.wddx.WDDXProcessorConversionException;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtil;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtilException;

/**
 * 
 * @author Andrey Balan
 * 
 */
public class SpreadsheetServiceImpl implements ISpreadsheetService,ToolContentManager, ToolSessionManager, ToolContentImport102Manager {
	static Logger log = Logger.getLogger(SpreadsheetServiceImpl.class.getName());
	private SpreadsheetDAO spreadsheetDao;
	private SpreadsheetAttachmentDAO spreadsheetAttachmentDao;
	private SpreadsheetUserDAO spreadsheetUserDao;
	private SpreadsheetSessionDAO spreadsheetSessionDao;
	private UserModifiedSpreadsheetDAO userModifiedSpreadsheetDao;
	private SpreadsheetMarkDAO spreadsheetMarkDao;
	//tool service
	private SpreadsheetToolContentHandler spreadsheetToolContentHandler;
	private MessageService messageService;
	//system services
	private IRepositoryService repositoryService;
	private ILamsToolService toolService;
	private ILearnerService learnerService;
	private IAuditService auditService;
	private IUserManagementService userManagementService; 
	private IExportToolContentService exportContentService;
	private ICoreNotebookService coreNotebookService;
	
	//*******************************************************************************
	// Service method
	//*******************************************************************************
	/** Try to get the file. If forceLogin = false and an access denied exception occurs, call this method
	 * again to get a new ticket and retry file lookup. If forceLogin = true and it then fails 
	 * then throw exception.
	 * @param uuid
	 * @param versionId
	 * @param relativePath
	 * @param attemptCount
	 * @return file node
	 * @throws ImscpApplicationException
	 */
	private IVersionedNode getFile(Long uuid, Long versionId, String relativePath) throws SpreadsheetApplicationException {
		
		ITicket tic = getRepositoryLoginTicket();
		
		try {
			
			return repositoryService.getFileItem(tic, uuid, versionId, relativePath);
			
		} catch (AccessDeniedException e) {
			
			String error = "Unable to access repository to get file uuid "+uuid
				+" version id "+versionId
				+" path " + relativePath+".";
			
			error = error+"AccessDeniedException: "+e.getMessage()+" Unable to retry further.";
			log.error(error);
			throw new SpreadsheetApplicationException(error,e);
			
		} catch (Exception e) {
			
			String error = "Unable to access repository to get file uuid "+uuid
			+" version id "+versionId
			+" path " + relativePath+"."
			+" Exception: "+e.getMessage();
			log.error(error);
			throw new SpreadsheetApplicationException(error,e);
			
		} 
	}
	/**
	 * This method verifies the credentials of the Spreadsheet Tool and gives it
	 * the <code>Ticket</code> to login and access the Content Repository.
	 * 
	 * A valid ticket is needed in order to access the content from the
	 * repository. This method would be called evertime the tool needs to
	 * upload/download files from the content repository.
	 * 
	 * @return ITicket The ticket for repostory access
	 * @throws SpreadsheetApplicationException
	 */
	private ITicket getRepositoryLoginTicket() throws SpreadsheetApplicationException {
		ICredentials credentials = new SimpleCredentials(
				spreadsheetToolContentHandler.getRepositoryUser(),
				spreadsheetToolContentHandler.getRepositoryId());
		try {
			ITicket ticket = repositoryService.login(credentials,
					spreadsheetToolContentHandler.getRepositoryWorkspaceName());
			return ticket;
		} catch (AccessDeniedException ae) {
			throw new SpreadsheetApplicationException("Access Denied to repository."
					+ ae.getMessage());
		} catch (WorkspaceNotFoundException we) {
			throw new SpreadsheetApplicationException("Workspace not found."
					+ we.getMessage());
		} catch (LoginException e) {
			throw new SpreadsheetApplicationException("Login failed." + e.getMessage());
		}
	}


	public Spreadsheet getSpreadsheetByContentId(Long contentId) {
		Spreadsheet rs = spreadsheetDao.getByContentId(contentId);
		if(rs == null){
			log.error("Could not find the content by given ID:"+contentId);
		}
		return rs; 
	}


	public Spreadsheet getDefaultContent(Long contentId) throws SpreadsheetApplicationException {
    	if (contentId == null)
    	{
    		String error=messageService.getMessage("error.msg.default.content.not.find");
    	    log.error(error);
    	    throw new SpreadsheetApplicationException(error);
    	}
    	
    	Spreadsheet defaultContent = getDefaultSpreadsheet();
    	//save default content by given ID.
    	Spreadsheet content = new Spreadsheet();
    	content = Spreadsheet.newInstance(defaultContent,contentId,spreadsheetToolContentHandler);
		return content;
	}

	public SpreadsheetAttachment uploadInstructionFile(FormFile uploadFile, String fileType) throws UploadSpreadsheetFileException {
		if(uploadFile == null || StringUtils.isEmpty(uploadFile.getFileName()))
			throw new UploadSpreadsheetFileException(messageService.getMessage("error.msg.upload.file.not.found",new Object[]{uploadFile}));
		
		//upload file to repository
		NodeKey nodeKey = processFile(uploadFile,fileType);
		
		//create new attachement
		SpreadsheetAttachment file = new SpreadsheetAttachment();
		file.setFileType(fileType);
		file.setFileUuid(nodeKey.getUuid());
		file.setFileVersionId(nodeKey.getVersion());
		file.setFileName(uploadFile.getFileName());
		file.setCreated(new Date());
		
		return file;
	}

	public void saveOrUpdateUser(SpreadsheetUser spreadsheetUser) {
		spreadsheetUserDao.saveObject(spreadsheetUser);
	}
	
	public void saveOrUpdateUserModifiedSpreadsheet(UserModifiedSpreadsheet userModifiedSpreadsheet) {
		userModifiedSpreadsheetDao.saveObject(userModifiedSpreadsheet);
	}
	
	public SpreadsheetUser getUserByIDAndContent(Long userId, Long contentId) {
		return (SpreadsheetUser) spreadsheetUserDao.getUserByUserIDAndContentID(userId,contentId);
	}
	
	public SpreadsheetUser getUserByIDAndSession(Long userId, Long sessionId)  {
		return (SpreadsheetUser) spreadsheetUserDao.getUserByUserIDAndSessionID(userId,sessionId);
	}
	
	public List<SpreadsheetUser> getUserListBySessionId(Long sessionId) {
		List<SpreadsheetUser> userList = spreadsheetUserDao.getBySessionID(sessionId);
		return userList;
	}
	
	public void deleteFromRepository(Long fileUuid, Long fileVersionId) throws SpreadsheetApplicationException {
		ITicket ticket = getRepositoryLoginTicket();
		try {
			repositoryService.deleteVersion(ticket, fileUuid,fileVersionId);
		} catch (Exception e) {
			throw new SpreadsheetApplicationException(
					"Exception occured while deleting files from"
							+ " the repository " + e.getMessage());
		}
	}

	public void saveOrUpdateSpreadsheet(Spreadsheet spreadsheet) {
		spreadsheetDao.saveObject(spreadsheet);
	}

	public void deleteSpreadsheetAttachment(Long attachmentUid) {
		spreadsheetAttachmentDao.removeObject(SpreadsheetAttachment.class, attachmentUid);
		
	}

	public List<Summary> exportBySessionId(Long sessionId, boolean skipHide) {
		SpreadsheetSession session = spreadsheetSessionDao.getSessionBySessionId(sessionId);
		if(session == null){
			log.error("Failed get SpreadsheetSession by ID [" +sessionId + "]");
			return null;
		}
		//initial spreadsheet items list
		List<Summary> itemList = new ArrayList();
//		Set<SpreadsheetItem> resList = session.getSpreadsheet().getSpreadsheetItems();
//		for(SpreadsheetItem item:resList){
//			if(skipHide && item.isHide())
//				continue;
//			//if item is create by author
//			if(item.isCreateByAuthor()){
//				Summary sum = new Summary(session.getSessionId(), session.getSessionName(),item,false);
//				itemList.add(sum);
//			}
//		}
//
//		//get this session's all spreadsheet items
//		Set<SpreadsheetItem> sessList =session.getSpreadsheetItems();
//		for(SpreadsheetItem item:sessList){
//			if(skipHide && item.isHide())
//				continue;
//			
//			//to skip all item create by author
//			if(!item.isCreateByAuthor()){
//				Summary sum = new Summary(session.getSessionId(), session.getSessionName(),item,false);
//				itemList.add(sum);
//			}
//		}
		
		return itemList;
	}
	
	public List<List<Summary>> exportByContentId(Long contentId) {
		Spreadsheet spreadsheet = spreadsheetDao.getByContentId(contentId);
		List<List<Summary>> groupList = new ArrayList();
		
//		//create init spreadsheet items list
//		List<Summary> initList = new ArrayList();
//		groupList.add(initList);
//		Set<SpreadsheetItem> resList = spreadsheet.getSpreadsheetItems();
//		for(SpreadsheetItem item:resList){
//			if(item.isCreateByAuthor()){
//				Summary sum = new Summary(null, null,item,true);
//				initList.add(sum);
//			}
//		}
//		
//		//session by session
//		List<SpreadsheetSession> sessionList = spreadsheetSessionDao.getByContentId(contentId);
//		for(SpreadsheetSession session:sessionList){
//			List<Summary> group = new ArrayList<Summary>();
//			//get this session's all spreadsheet items
//			Set<SpreadsheetItem> sessList =session.getSpreadsheetItems();
//			for(SpreadsheetItem item:sessList){
//				//to skip all item create by author
//				if(!item.isCreateByAuthor()){
//					Summary sum = new Summary(session.getSessionId(), session.getSessionName(),item,false);
//					group.add(sum);
//				}
//			}
//			if(group.size() == 0){
//				group.add(new Summary(session.getSessionId(), session.getSessionName(),null,false));
//			}
//			groupList.add(group);
//		}
		
		return groupList;
	}
	public Spreadsheet getSpreadsheetBySessionId(Long sessionId){
		SpreadsheetSession session = spreadsheetSessionDao.getSessionBySessionId(sessionId);
		//to skip CGLib problem
		Long contentId = session.getSpreadsheet().getContentId();
		Spreadsheet res = spreadsheetDao.getByContentId(contentId);
		return res;
	}
	public SpreadsheetSession getSessionBySessionId(Long sessionId) {
		return spreadsheetSessionDao.getSessionBySessionId(sessionId);
	}


	public void saveOrUpdateSpreadsheetSession(SpreadsheetSession resSession) {
		spreadsheetSessionDao.saveObject(resSession);
	}

	public String finishToolSession(Long toolSessionId, Long userId) throws SpreadsheetApplicationException {
		SpreadsheetUser user = spreadsheetUserDao.getUserByUserIDAndSessionID(userId, toolSessionId);
		user.setSessionFinished(true);
		spreadsheetUserDao.saveObject(user);
		
//		SpreadsheetSession session = spreadsheetSessionDao.getSessionBySessionId(toolSessionId);
//		session.setStatus(SpreadsheetConstants.COMPLETED);
//		spreadsheetSessionDao.saveObject(session);
		
		String nextUrl = null;
		try {
			nextUrl = this.leaveToolSession(toolSessionId,userId);
		} catch (DataMissingException e) {
			throw new SpreadsheetApplicationException(e);
		} catch (ToolException e) {
			throw new SpreadsheetApplicationException(e);
		}
		return nextUrl;
	}

	public List<Summary> getSummary(Long contentId) {
		Spreadsheet spreadsheet = spreadsheetDao.getByContentId(contentId);
		List<SpreadsheetSession> sessionList = spreadsheetSessionDao.getByContentId(contentId);

		List<Summary> summaryList = new ArrayList<Summary>();
		
		//create the user list of all whom were started this task
		for(SpreadsheetSession session:sessionList) {
			List<SpreadsheetUser> userList = spreadsheetUserDao.getBySessionID(session.getSessionId());
			
			Summary summary = new Summary(session, spreadsheet, userList);
			summaryList.add(summary);
		}

		return summaryList;
	}
	
	public List<StatisticDTO> getStatistics(Long contentId) {
		List<SpreadsheetSession> sessionList = spreadsheetSessionDao.getByContentId(contentId);

		List<StatisticDTO> statisticList = new ArrayList<StatisticDTO>();
		
		for(SpreadsheetSession session:sessionList) {
			List<SpreadsheetUser> userList = spreadsheetUserDao.getBySessionID(session.getSessionId());
			int totalUserModifiedSpreadsheets = 0;
			int totalMarkedSpreadsheets = 0;
			for (SpreadsheetUser user:userList) {
				if (user.getUserModifiedSpreadsheet() != null) {
					totalUserModifiedSpreadsheets++;
					if (user.getUserModifiedSpreadsheet().getMark() != null)
						totalMarkedSpreadsheets++;
				}
			}
			
			StatisticDTO statistic = new StatisticDTO(session.getSessionName(), totalMarkedSpreadsheets, totalUserModifiedSpreadsheets - totalMarkedSpreadsheets, totalUserModifiedSpreadsheets);
			statisticList.add(statistic);
		}

		return statisticList;
	}
	
	public Map<Long, Set<ReflectDTO>> getReflectList(Long contentId, boolean setEntry){
		Map<Long, Set<ReflectDTO>> map = new HashMap<Long, Set<ReflectDTO>>();

		List<SpreadsheetSession> sessionList = spreadsheetSessionDao.getByContentId(contentId);
		for(SpreadsheetSession session:sessionList){
			Long sessionId = session.getSessionId();
			boolean hasRefection = session.getSpreadsheet().isReflectOnActivity();
			Set<ReflectDTO> list = new TreeSet<ReflectDTO>(new ReflectDTOComparator());
			//get all users in this session
			List<SpreadsheetUser> users = spreadsheetUserDao.getBySessionID(sessionId);
			for(SpreadsheetUser user : users){
				ReflectDTO ref = new ReflectDTO(user);
				
				if (setEntry) {
					NotebookEntry entry = getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL, 
							SpreadsheetConstants.TOOL_SIGNATURE, user.getUserId().intValue());
					if (entry != null) {
						ref.setReflect(entry.getEntry());
					}
				}
				
				ref.setHasRefection(hasRefection);
				list.add(ref);
			}
			map.put(sessionId, list);
		}
		
		return map;
	}

	public Long createNotebookEntry(Long sessionId, Integer notebookToolType, String toolSignature, Integer userId, String entryText) {
		return coreNotebookService.createNotebookEntry(sessionId, notebookToolType, toolSignature, userId, "", entryText);
	}
	public NotebookEntry getEntry(Long sessionId, Integer idType, String signature, Integer userID){
		List<NotebookEntry> list = coreNotebookService.getEntry(sessionId, idType, signature, userID);
		if (list == null || list.isEmpty()) {
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
	
	public SpreadsheetUser getUser(Long uid){
		return (SpreadsheetUser) spreadsheetUserDao.getObject(SpreadsheetUser.class, uid);
	}
	
	public void releaseMarksForSession(Long sessionId){
		List<SpreadsheetUser> users = spreadsheetUserDao.getBySessionID(sessionId);
		for (SpreadsheetUser user: users) {
			if ((user.getUserModifiedSpreadsheet() != null) && (user.getUserModifiedSpreadsheet().getMark() != null)) {
				SpreadsheetMark mark = user.getUserModifiedSpreadsheet().getMark();
				mark.setDateMarksReleased(new Date());
				spreadsheetMarkDao.saveObject(mark);
			}
		}
	}
	
	//*****************************************************************************
	// private methods
	//*****************************************************************************
	private Spreadsheet getDefaultSpreadsheet() throws SpreadsheetApplicationException {
    	Long defaultSpreadsheetId = getToolDefaultContentIdBySignature(SpreadsheetConstants.TOOL_SIGNATURE);
    	Spreadsheet defaultSpreadsheet = getSpreadsheetByContentId(defaultSpreadsheetId);
    	if(defaultSpreadsheet == null)
    	{
    	    String error=messageService.getMessage("error.msg.default.content.not.find");
    	    log.error(error);
    	    throw new SpreadsheetApplicationException(error);
    	}
    	
    	return defaultSpreadsheet;
	}
    private Long getToolDefaultContentIdBySignature(String toolSignature) throws SpreadsheetApplicationException
    {
        Long contentId = null;
    	contentId=new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));    
    	if (contentId == null)
    	{
    		String error=messageService.getMessage("error.msg.default.content.not.find");
    	    log.error(error);
    	    throw new SpreadsheetApplicationException(error);
    	}
	    return contentId;
    }
    /**
     * Process an uploaded file.
     * 
     * @throws SpreadsheetApplicationException 
     * @throws FileNotFoundException
     * @throws IOException
     * @throws RepositoryCheckedException
     * @throws InvalidParameterException
     */
    private NodeKey processFile(FormFile file, String fileType) throws UploadSpreadsheetFileException {
    	NodeKey node = null;
        if (file!= null && !StringUtils.isEmpty(file.getFileName())) {
            String fileName = file.getFileName();
            try {
				node = spreadsheetToolContentHandler.uploadFile(file.getInputStream(), fileName, 
				        file.getContentType(), fileType);
			} catch (InvalidParameterException e) {
				throw new UploadSpreadsheetFileException (messageService.getMessage("error.msg.invaid.param.upload"));
			} catch (FileNotFoundException e) {
				throw new UploadSpreadsheetFileException (messageService.getMessage("error.msg.file.not.found"));
			} catch (RepositoryCheckedException e) {
				throw new UploadSpreadsheetFileException (messageService.getMessage("error.msg.repository"));
			} catch (IOException e) {
				throw new UploadSpreadsheetFileException (messageService.getMessage("error.msg.io.exception"));
			}
          }
        return node;
    }
	private NodeKey processPackage(String packageDirectory, String initFile) throws UploadSpreadsheetFileException {
		NodeKey node = null;
		try {
			node = spreadsheetToolContentHandler.uploadPackage(packageDirectory, initFile);
		} catch (InvalidParameterException e) {
			throw new UploadSpreadsheetFileException (messageService.getMessage("error.msg.invaid.param.upload"));
		} catch (RepositoryCheckedException e) {
			throw new UploadSpreadsheetFileException (messageService.getMessage("error.msg.repository"));
		}
        return node;
	}

	/**
	 * Find out default.htm/html or index.htm/html in the given directory folder
	 * @param packageDirectory
	 * @return
	 */
	private String findWebsiteInitialItem(String packageDirectory) {
		File file = new File(packageDirectory);
		if(!file.isDirectory())
			return null;
		
		File[] initFiles = file.listFiles(new FileFilter(){
			public boolean accept(File pathname) {
				if(pathname == null || pathname.getName() == null)
					return false;
				String name = pathname.getName();
				if(name.endsWith("default.html")
					||name.endsWith("default.htm")
					||name.endsWith("index.html")
					||name.endsWith("index.htm"))
					return true;
				return false;
			}
		});
		if(initFiles != null && initFiles.length > 0)
			return initFiles[0].getName();
		else
			return null;
	}


	//*****************************************************************************
	// set methods for Spring Bean
	//*****************************************************************************
	public void setAuditService(IAuditService auditService) {
		this.auditService = auditService;
	}
	public void setLearnerService(ILearnerService learnerService) {
		this.learnerService = learnerService;
	}
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}
	public MessageService getMessageService() {
		return messageService;
	}	
	public void setRepositoryService(IRepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	public void setSpreadsheetAttachmentDao(SpreadsheetAttachmentDAO spreadsheetAttachmentDao) {
		this.spreadsheetAttachmentDao = spreadsheetAttachmentDao;
	}
	public void setSpreadsheetDao(SpreadsheetDAO spreadsheetDao) {
		this.spreadsheetDao = spreadsheetDao;
	}
	public void setSpreadsheetSessionDao(SpreadsheetSessionDAO spreadsheetSessionDao) {
		this.spreadsheetSessionDao = spreadsheetSessionDao;
	}
	public void setSpreadsheetToolContentHandler(SpreadsheetToolContentHandler spreadsheetToolContentHandler) {
		this.spreadsheetToolContentHandler = spreadsheetToolContentHandler;
	}
	public void setSpreadsheetUserDao(SpreadsheetUserDAO spreadsheetUserDao) {
		this.spreadsheetUserDao = spreadsheetUserDao;
	}
	public void setToolService(ILamsToolService toolService) {
		this.toolService = toolService;
	}
	public void setUserModifiedSpreadsheetDao(UserModifiedSpreadsheetDAO userModifiedSpreadsheetDao) {
		this.userModifiedSpreadsheetDao = userModifiedSpreadsheetDao;
	}
	public void setSpreadsheetMarkDao(SpreadsheetMarkDAO spreadsheetMarkDao) {
		this.spreadsheetMarkDao = spreadsheetMarkDao;
	}	

	//*******************************************************************************
	//ToolContentManager, ToolSessionManager methods
	//*******************************************************************************
	
	public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
		Spreadsheet toolContentObj = spreadsheetDao.getByContentId(toolContentId);
 		if(toolContentObj == null) {
 			try {
				toolContentObj = getDefaultSpreadsheet();
			} catch (SpreadsheetApplicationException e) {
				throw new DataMissingException(e.getMessage());
			}
 		}
 		if(toolContentObj == null)
 			throw new DataMissingException("Unable to find default content for the spreadsheet tool");
 		
 		//set SpreadsheetToolContentHandler as null to avoid copy file node in repository again.
 		toolContentObj = Spreadsheet.newInstance(toolContentObj,toolContentId,null);
 		toolContentObj.setToolContentHandler(null);
 		toolContentObj.setOfflineFileList(null);
 		toolContentObj.setOnlineFileList(null);
		try {
			exportContentService.registerFileClassForExport(SpreadsheetAttachment.class.getName(),"fileUuid","fileVersionId");
			exportContentService.exportToolContent( toolContentId, toolContentObj,spreadsheetToolContentHandler, rootPath);
		} catch (ExportToolContentException e) {
			throw new ToolException(e);
		}
	}

	public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath ,String fromVersion,String toVersion) throws ToolException {
	
		try {
			exportContentService.registerFileClassForImport(SpreadsheetAttachment.class.getName()
					,"fileUuid","fileVersionId","fileName","fileType",null,null);
			
			Object toolPOJO =  exportContentService.importToolContent(toolContentPath,spreadsheetToolContentHandler,fromVersion,toVersion);
			if(!(toolPOJO instanceof Spreadsheet))
				throw new ImportToolContentException("Import Share spreadsheet tool content failed. Deserialized object is " + toolPOJO);
			Spreadsheet toolContentObj = (Spreadsheet) toolPOJO;
			
//			reset it to new toolContentId
			toolContentObj.setContentId(toolContentId);
			SpreadsheetUser user = spreadsheetUserDao.getUserByUserIDAndContentID(new Long(newUserUid.longValue()), toolContentId);
			if(user == null){
				user = new SpreadsheetUser();
				UserDTO sysUser = ((User)userManagementService.findById(User.class,newUserUid)).getUserDTO();
				user.setFirstName(sysUser.getFirstName());
				user.setLastName(sysUser.getLastName());
				user.setLoginName(sysUser.getLogin());
				user.setUserId(new Long(newUserUid.longValue()));
				user.setSpreadsheet(toolContentObj);
			}
			toolContentObj.setCreatedBy(user);
			
			spreadsheetDao.saveObject(toolContentObj);
		} catch (ImportToolContentException e) {
			throw new ToolException(e);
		}
	}

	  /** Get the definitions for possible output for an activity, based on the toolContentId. These may be definitions that are always
     * available for the tool (e.g. number of marks for Multiple Choice) or a custom definition created for a particular activity
     * such as the answer to the third question contains the word Koala and hence the need for the toolContentId
     * @return SortedMap of ToolOutputDefinitions with the key being the name of each definition
     */
	public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId) throws ToolException {
		return new TreeMap<String, ToolOutputDefinition>();
	}
 

	public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {
		if (toContentId == null)
			throw new ToolException(
					"Failed to create the SharedSpreadsheetFiles tool seession");

		Spreadsheet spreadsheet = null;
		if ( fromContentId != null ) {
			spreadsheet = 	spreadsheetDao.getByContentId(fromContentId);
		}
		if ( spreadsheet == null ) {
			try {
				spreadsheet = getDefaultSpreadsheet();
			} catch (SpreadsheetApplicationException e) {
				throw new ToolException(e);
			}
		}

		Spreadsheet toContent = Spreadsheet.newInstance(spreadsheet,toContentId,spreadsheetToolContentHandler);
		spreadsheetDao.saveObject(toContent);
	}


	public void setAsDefineLater(Long toolContentId, boolean value) throws DataMissingException, ToolException {
		Spreadsheet spreadsheet = spreadsheetDao.getByContentId(toolContentId);
		if(spreadsheet == null){
			throw new ToolException("No found tool content by given content ID:" + toolContentId);
		}
		spreadsheet.setDefineLater(value);
	}


	public void setAsRunOffline(Long toolContentId, boolean value) throws DataMissingException, ToolException {
		Spreadsheet spreadsheet = spreadsheetDao.getByContentId(toolContentId);
		if(spreadsheet == null){
			throw new ToolException("No found tool content by given content ID:" + toolContentId);
		}
		spreadsheet.setRunOffline(value);		
	}


	public void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException, ToolException {
		Spreadsheet spreadsheet = spreadsheetDao.getByContentId(toolContentId);
		if(removeSessionData){
			List list = spreadsheetSessionDao.getByContentId(toolContentId);
			Iterator iter = list.iterator();
			while(iter.hasNext()){
				SpreadsheetSession session = (SpreadsheetSession ) iter.next();
				spreadsheetSessionDao.delete(session);
			}
		}
		spreadsheetDao.delete(spreadsheet);
	}

	
	public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
		SpreadsheetSession session = new SpreadsheetSession();
		session.setSessionId(toolSessionId);
		session.setSessionName(toolSessionName);
		Spreadsheet spreadsheet = spreadsheetDao.getByContentId(toolContentId);
		session.setSpreadsheet(spreadsheet);
		spreadsheetSessionDao.saveObject(session);
	}


	public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
		if(toolSessionId == null){
			log.error("Fail to leave tool Session based on null tool session id.");
			throw new ToolException("Fail to remove tool Session based on null tool session id.");
		}
		if(learnerId == null){
			log.error("Fail to leave tool Session based on null learner.");
			throw new ToolException("Fail to remove tool Session based on null learner.");
		}
		
		SpreadsheetSession session = spreadsheetSessionDao.getSessionBySessionId(toolSessionId);
		if(session != null){
			session.setStatus(SpreadsheetConstants.COMPLETED);
			spreadsheetSessionDao.saveObject(session);
		}else{
			log.error("Fail to leave tool Session.Could not find shared spreadsheet " +
					"session by given session id: "+toolSessionId);
			throw new DataMissingException("Fail to leave tool Session." +
					"Could not find shared spreadsheet session by given session id: "+toolSessionId);
		}
		return learnerService.completeToolSession(toolSessionId,learnerId);
	}


	public ToolSessionExportOutputData exportToolSession(Long toolSessionId) throws DataMissingException, ToolException {
		return null;
	}


	public ToolSessionExportOutputData exportToolSession(List toolSessionIds) throws DataMissingException, ToolException {
		return null;
	}


	public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
		spreadsheetSessionDao.deleteBySessionId(toolSessionId);
	}
	
	/** 
	 * Get the tool output for the given tool output names.
	 * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.util.List<String>, java.lang.Long, java.lang.Long)
	 */
	public SortedMap<String, ToolOutput> getToolOutput(List<String> names,
			Long toolSessionId, Long learnerId) {
		return new TreeMap<String,ToolOutput>();
	}

	/** 
	 * Get the tool output for the given tool output name.
	 * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.lang.String, java.lang.Long, java.lang.Long)
	 */
	public ToolOutput getToolOutput(String name, Long toolSessionId,
			Long learnerId) {
		return null;
	}

	/* ===============Methods implemented from ToolContentImport102Manager =============== */
	

    /**
     * Import the data for a 1.0.2 Noticeboard or HTMLNoticeboard
     */
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues) { }

    /** Set the description, throws away the title value as this is not supported in 2.0 */
    public void setReflectiveData(Long toolContentId, String title, String description) 
    		throws ToolException, DataMissingException {
    	
    	Spreadsheet toolContentObj = getSpreadsheetByContentId(toolContentId);
    	if ( toolContentObj == null ) {
    		throw new DataMissingException("Unable to set reflective data titled "+title
	       			+" on activity toolContentId "+toolContentId
	       			+" as the tool content does not exist.");
    	}

    	toolContentObj.setReflectOnActivity(Boolean.TRUE);
    	toolContentObj.setReflectInstructions(description);
    }

    
	/* =================================================================================== */

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

}
