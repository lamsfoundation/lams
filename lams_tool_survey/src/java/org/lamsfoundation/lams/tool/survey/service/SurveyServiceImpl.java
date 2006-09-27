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
package org.lamsfoundation.lams.tool.survey.service;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.tool.ToolContentImport102Manager;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.tool.survey.SurveyConstants;
import org.lamsfoundation.lams.tool.survey.dao.SurveyAnswerDAO;
import org.lamsfoundation.lams.tool.survey.dao.SurveyAttachmentDAO;
import org.lamsfoundation.lams.tool.survey.dao.SurveyDAO;
import org.lamsfoundation.lams.tool.survey.dao.SurveyQuestionDAO;
import org.lamsfoundation.lams.tool.survey.dao.SurveySessionDAO;
import org.lamsfoundation.lams.tool.survey.dao.SurveyUserDAO;
import org.lamsfoundation.lams.tool.survey.dto.AnswerDTO;
import org.lamsfoundation.lams.tool.survey.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.survey.model.Survey;
import org.lamsfoundation.lams.tool.survey.model.SurveyAnswer;
import org.lamsfoundation.lams.tool.survey.model.SurveyAttachment;
import org.lamsfoundation.lams.tool.survey.model.SurveyOption;
import org.lamsfoundation.lams.tool.survey.model.SurveyQuestion;
import org.lamsfoundation.lams.tool.survey.model.SurveySession;
import org.lamsfoundation.lams.tool.survey.model.SurveyUser;
import org.lamsfoundation.lams.tool.survey.util.SurveySessionComparator;
import org.lamsfoundation.lams.tool.survey.util.SurveyToolContentHandler;
import org.lamsfoundation.lams.tool.survey.util.SurveyWebUtils;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.util.wddx.WDDXProcessorConversionException;

/**
 * 
 * @author Dapeng.Ni 
 * 
 */
public class SurveyServiceImpl implements
                              ISurveyService,ToolContentManager, ToolSessionManager, ToolContentImport102Manager
               
{
	static Logger log = Logger.getLogger(SurveyServiceImpl.class.getName());
	
	//DAO
	private SurveyDAO surveyDao;
	private SurveyQuestionDAO surveyQuestionDao;
	private SurveyAnswerDAO surveyAnswerDao;
	private SurveyAttachmentDAO surveyAttachmentDao;
	private SurveyUserDAO surveyUserDao;
	private SurveySessionDAO surveySessionDao;
	
	//tool service
	private SurveyToolContentHandler surveyToolContentHandler;
	private MessageService messageService;
	//system services
	private IRepositoryService repositoryService;
	private ILamsToolService toolService;
	private ILearnerService learnerService;
	private IAuditService auditService;
	private IUserManagementService userManagementService; 
	private IExportToolContentService exportContentService;
	private ICoreNotebookService coreNotebookService;
	
	
	private class ReflectDTOComparator implements Comparator<ReflectDTO>{
		public int compare(ReflectDTO o1, ReflectDTO o2) {
			if(o1 != null && o2 != null){
				return o1.getFullName().compareTo(o2.getFullName());
			}else if(o1 != null)
				return 1;
			else
				return -1;
		}
	}
	
	//*******************************************************************************
	// Service method
	//*******************************************************************************

	public Survey getSurveyByContentId(Long contentId) {
		Survey rs = surveyDao.getByContentId(contentId);
		if(rs == null){
			log.error("Could not find the content by given ID:"+contentId);
		}
		return rs; 
	}


	public Survey getDefaultContent(Long contentId) throws SurveyApplicationException {
    	if (contentId == null)
    	{
    		String error=messageService.getMessage("error.msg.default.content.not.find");
    	    log.error(error);
    	    throw new SurveyApplicationException(error);
    	}
    	
    	Survey defaultContent = getDefaultSurvey();
    	//save default content by given ID.
    	Survey content = new Survey();
    	content = Survey.newInstance(defaultContent,contentId,surveyToolContentHandler);
		return content;
	}


	public SurveyAttachment uploadInstructionFile(FormFile uploadFile, String fileType) throws UploadSurveyFileException {
		if(uploadFile == null || StringUtils.isEmpty(uploadFile.getFileName()))
			throw new UploadSurveyFileException(messageService.getMessage("error.msg.upload.file.not.found",new Object[]{uploadFile}));
		
		//upload file to repository
		NodeKey nodeKey = processFile(uploadFile,fileType);
		
		//create new attachement
		SurveyAttachment file = new SurveyAttachment();
		file.setFileType(fileType);
		file.setFileUuid(nodeKey.getUuid());
		file.setFileVersionId(nodeKey.getVersion());
		file.setFileName(uploadFile.getFileName());
		
		return file;
	}


	public void createUser(SurveyUser surveyUser) {
		surveyUserDao.saveObject(surveyUser);
	}


	public SurveyUser getUserByIDAndContent(Long userId, Long contentId) {
		
		return (SurveyUser) surveyUserDao.getUserByUserIDAndContentID(userId,contentId);
		
	}
	public SurveyUser getUserByIDAndSession(Long userId, Long sessionId)  {
		
		return (SurveyUser) surveyUserDao.getUserByUserIDAndSessionID(userId,sessionId);
		
	}


	public void deleteFromRepository(Long fileUuid, Long fileVersionId) throws SurveyApplicationException {
		ITicket ticket = getRepositoryLoginTicket();
		try {
			repositoryService.deleteVersion(ticket, fileUuid,fileVersionId);
		} catch (Exception e) {
			throw new SurveyApplicationException(
					"Exception occured while deleting files from"
							+ " the repository " + e.getMessage());
		}
	}


	public void saveOrUpdateSurvey(Survey survey) {
		surveyDao.saveObject(survey);
	}


	public void deleteSurveyAttachment(Long attachmentUid) {
		surveyAttachmentDao.removeObject(SurveyAttachment.class, attachmentUid);
		
	}

	public Survey getSurveyBySessionId(Long sessionId){
		SurveySession session = surveySessionDao.getSessionBySessionId(sessionId);
		//to skip CGLib problem
		Long contentId = session.getSurvey().getContentId();
		Survey res = surveyDao.getByContentId(contentId);
		return res;
	}
	public SurveySession getSurveySessionBySessionId(Long sessionId) {
		return surveySessionDao.getSessionBySessionId(sessionId);
	}


	public void saveOrUpdateSurveySession(SurveySession resSession) {
		surveySessionDao.saveObject(resSession);
	}

	public String finishToolSession(Long toolSessionId, Long userId) throws SurveyApplicationException {
		SurveyUser user = surveyUserDao.getUserByUserIDAndSessionID(userId, toolSessionId);
		user.setSessionFinished(true);
		surveyUserDao.saveObject(user);
		
//		SurveySession session = surveySessionDao.getSessionBySessionId(toolSessionId);
//		session.setStatus(SurveyConstants.COMPLETED);
//		surveySessionDao.saveObject(session);
		
		String nextUrl = null;
		try {
			nextUrl = this.leaveToolSession(toolSessionId,userId);
		} catch (DataMissingException e) {
			throw new SurveyApplicationException(e);
		} catch (ToolException e) {
			throw new SurveyApplicationException(e);
		}
		return nextUrl;
	}

	public Map<Long, Set<ReflectDTO>> getReflectList(Long contentId){
		Map<Long, Set<ReflectDTO>> map = new HashMap<Long, Set<ReflectDTO>>();

		List<SurveySession> sessionList = surveySessionDao.getByContentId(contentId);
		for(SurveySession session:sessionList){
			Long sessionId = session.getSessionId();
			boolean hasRefection = session.getSurvey().isReflectOnActivity();
			Set<ReflectDTO> list = new TreeSet<ReflectDTO>(this.new ReflectDTOComparator());
			//get all users in this session
			List<SurveyUser> users = surveyUserDao.getBySessionID(sessionId);
			for(SurveyUser user : users){
				ReflectDTO ref = new ReflectDTO(user);
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
	
	public SurveyUser getUser(Long uid){
		return (SurveyUser) surveyUserDao.getObject(SurveyUser.class, uid);
	}
	
	public List<SurveyUser> getSessionUsers(Long sessionId){
		return surveyUserDao.getBySessionID(sessionId);
	}

	public void deleteQuestion(Long uid) {
		surveyQuestionDao.removeObject(SurveyQuestion.class, uid);
		
	}

	public List<AnswerDTO> getQuestionAnswers(Long sessionId,Long userUid) {
		List<SurveyQuestion> questions = new ArrayList<SurveyQuestion>();
		SurveySession session = surveySessionDao.getSessionBySessionId(sessionId);
		if(session != null){
			Survey survey = session.getSurvey();
			if(survey != null)
				questions = new ArrayList<SurveyQuestion>(survey.getQuestions());
		}
		
		//set answer for this question acoording
		List<AnswerDTO> answers = new ArrayList<AnswerDTO>();
		for(SurveyQuestion question:questions){
			AnswerDTO answerDTO = new AnswerDTO(question);
			SurveyAnswer answer = surveyAnswerDao.getAnswer(question.getUid(),userUid);
			if(answer != null)
				answer.setChoices(SurveyWebUtils.getChoiceList(answer.getAnswerChoices()));
			answerDTO.setAnswer(answer);
			answers.add(answerDTO);
		}
		return answers;
	}

	public void updateAnswerList(List<SurveyAnswer> answerList) {
		for(SurveyAnswer ans : answerList){
			surveyAnswerDao.saveObject(ans);
		}
	}

	public AnswerDTO getQuestionResponse(Long sessionId, Long questionUid){
		SurveyQuestion question = surveyQuestionDao.getByUid(questionUid);
		AnswerDTO answerDto = new AnswerDTO(question);
		
		//get question all answer from this session
		List<SurveyAnswer> answsers = surveyAnswerDao.getSessionAnswer(sessionId, questionUid);

		//create a map to hold Option UID and sequenceID(start from 0); 
		Map<String, Integer> optMap = new HashMap<String, Integer>();
		Set<SurveyOption> options = answerDto.getOptions();
		int idx=0;
		for (SurveyOption option : options) {
			optMap.put(option.getUid().toString(),idx);
			idx++;
		}
		
		//initial a array to hold how many time chose has been done for a option or open text.
		int optSize = options.size();
		//for appendText and open Text Entry will be the last one of choose[] array.
		if(answerDto.isAppendText() || answerDto.getType() == SurveyConstants.QUESTION_TYPE_TEXT_ENTRY)
			optSize++;
		
		int[] choose = new int[optSize];
		Arrays.fill(choose, 0);

		//sum up all option and open text (if has) have been selected count list
		int answerSum = 0;
		if(answsers != null){
			for (SurveyAnswer answer : answsers) {
				String[] choseOpt = SurveyWebUtils.getChoiceList(answer.getAnswerChoices());
				for (String optUid : choseOpt) {
					//if option has been chosen, the relative index of choose[] array will increase.
					if(optMap.containsKey(optUid)){
						choose[optMap.get(optUid)]++;
						answerSum ++;
					}
				}
				//handle appendText or Open Text Entry
				if((answerDto.isAppendText()
						|| answerDto.getType() == SurveyConstants.QUESTION_TYPE_TEXT_ENTRY)
						&& !StringUtils.isBlank(answer.getAnswerText())){
					choose[optSize-1]++;
					answerSum ++;
				}
			}
		}
		//caculate the percentage of answer response
		idx=0;
		if(answerSum == 0){
			answerSum = 1;
		}
		for (SurveyOption option : options) {
			option.setResponse((double)choose[idx]/(double)answerSum * 100d);
			option.setResponseFormatStr(new Long(Math.round(option.getResponse())).toString());
			option.setResponseCount(choose[idx]);
			idx++;
		}
		if(answerDto.isAppendText() || answerDto.getType() == SurveyConstants.QUESTION_TYPE_TEXT_ENTRY){
			answerDto.setOpenResponse((double)choose[idx]/(double)answerSum * 100d);
			answerDto.setOpenResponseFormatStr(new Long(Math.round(answerDto.getOpenResponse())).toString());
			answerDto.setOpenResponseCount(choose[idx]);
		}
		
		return answerDto;
		
	}
	
	public SortedMap<SurveySession,List<SurveyQuestion>> getSummary(Long toolContentId) {
		
		SortedMap<SurveySession,List<SurveyQuestion>> summary = 
				new TreeMap<SurveySession, List<SurveyQuestion>>(new SurveySessionComparator());
		
		Survey survey = surveyDao.getByContentId(toolContentId);
		//get all question under this survey
		Set<SurveyQuestion> questionList = survey.getQuestions();
		List<SurveySession> sessionList = surveySessionDao.getByContentId(toolContentId);
		//iterator all sessions under this survey content, and get all questions and its answers.
		for (SurveySession session : sessionList) {
			List<SurveyQuestion> responseList = new ArrayList<SurveyQuestion>();
			for (SurveyQuestion question : questionList) {
				SurveyQuestion response = getQuestionResponse(session.getSessionId(), question.getUid());
				responseList.add(response);
			}
			summary.put(session, responseList);
		}
		
		
		return summary;
	}

	public SurveyQuestion getQuestion(Long questionUid) {
		return surveyQuestionDao.getByUid(questionUid);
	}

	//*****************************************************************************
	// private methods
	//*****************************************************************************
	private Survey getDefaultSurvey() throws SurveyApplicationException {
    	Long defaultSurveyId = getToolDefaultContentIdBySignature(SurveyConstants.TOOL_SIGNATURE);
    	Survey defaultSurvey = getSurveyByContentId(defaultSurveyId);
    	if(defaultSurvey == null)
    	{
    	    String error=messageService.getMessage("error.msg.default.content.not.find");
    	    log.error(error);
    	    throw new SurveyApplicationException(error);
    	}
    	
    	return defaultSurvey;
	}
    private Long getToolDefaultContentIdBySignature(String toolSignature) throws SurveyApplicationException
    {
        Long contentId = null;
    	contentId=new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));    
    	if (contentId == null)
    	{
    		String error=messageService.getMessage("error.msg.default.content.not.find");
    	    log.error(error);
    	    throw new SurveyApplicationException(error);
    	}
	    return contentId;
    }
    /**
     * Process an uploaded file.
     * 
     * @throws SurveyApplicationException 
     * @throws FileNotFoundException
     * @throws IOException
     * @throws RepositoryCheckedException
     * @throws InvalidParameterException
     */
    private NodeKey processFile(FormFile file, String fileType) throws UploadSurveyFileException {
    	NodeKey node = null;
        if (file!= null && !StringUtils.isEmpty(file.getFileName())) {
            String fileName = file.getFileName();
            try {
				node = surveyToolContentHandler.uploadFile(file.getInputStream(), fileName, 
				        file.getContentType(), fileType);
			} catch (InvalidParameterException e) {
				throw new UploadSurveyFileException (messageService.getMessage("error.msg.invaid.param.upload"));
			} catch (FileNotFoundException e) {
				throw new UploadSurveyFileException (messageService.getMessage("error.msg.file.not.found"));
			} catch (RepositoryCheckedException e) {
				throw new UploadSurveyFileException (messageService.getMessage("error.msg.repository"));
			} catch (IOException e) {
				throw new UploadSurveyFileException (messageService.getMessage("error.msg.io.exception"));
			}
          }
        return node;
    }

	/**
	 * This method verifies the credentials of the Share Survey Tool and gives it
	 * the <code>Ticket</code> to login and access the Content Repository.
	 * 
	 * A valid ticket is needed in order to access the content from the
	 * repository. This method would be called evertime the tool needs to
	 * upload/download files from the content repository.
	 * 
	 * @return ITicket The ticket for repostory access
	 * @throws SurveyApplicationException
	 */
	private ITicket getRepositoryLoginTicket() throws SurveyApplicationException {
		ICredentials credentials = new SimpleCredentials(
				surveyToolContentHandler.getRepositoryUser(),
				surveyToolContentHandler.getRepositoryId());
		try {
			ITicket ticket = repositoryService.login(credentials,
					surveyToolContentHandler.getRepositoryWorkspaceName());
			return ticket;
		} catch (AccessDeniedException ae) {
			throw new SurveyApplicationException("Access Denied to repository."
					+ ae.getMessage());
		} catch (WorkspaceNotFoundException we) {
			throw new SurveyApplicationException("Workspace not found."
					+ we.getMessage());
		} catch (LoginException e) {
			throw new SurveyApplicationException("Login failed." + e.getMessage());
		}
	}

	//*******************************************************************************
	//ToolContentManager, ToolSessionManager methods
	//*******************************************************************************
	
	public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
		Survey toolContentObj = surveyDao.getByContentId(toolContentId);
 		if(toolContentObj == null)
 			throw new DataMissingException("Unable to find tool content by given id :" + toolContentId);
 		
 		//set SurveyToolContentHandler as null to avoid copy file node in repository again.
 		toolContentObj = Survey.newInstance(toolContentObj,toolContentId,null);
 		toolContentObj.setToolContentHandler(null);
 		toolContentObj.setOfflineFileList(null);
 		toolContentObj.setOnlineFileList(null);
		try {
			exportContentService.registerFileClassForExport(SurveyAttachment.class.getName(),"fileUuid","fileVersionId");
			exportContentService.registerFileClassForExport(SurveyQuestion.class.getName(),"fileUuid","fileVersionId");
			exportContentService.exportToolContent( toolContentId, toolContentObj,surveyToolContentHandler, rootPath);
		} catch (ExportToolContentException e) {
			throw new ToolException(e);
		}
	}


	public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath) throws ToolException {
	
		try {
			exportContentService.registerFileClassForImport(SurveyAttachment.class.getName()
					,"fileUuid","fileVersionId","fileName","fileType",null,null);
			exportContentService.registerFileClassForImport(SurveyQuestion.class.getName()
					,"fileUuid","fileVersionId","fileName","fileType",null,"initialItem");
			
			Object toolPOJO =  exportContentService.importToolContent(toolContentPath,surveyToolContentHandler);
			if(!(toolPOJO instanceof Survey))
				throw new ImportToolContentException("Import Share surveys tool content failed. Deserialized object is " + toolPOJO);
			Survey toolContentObj = (Survey) toolPOJO;
			
//			reset it to new toolContentId
			toolContentObj.setContentId(toolContentId);
			SurveyUser user = surveyUserDao.getUserByUserIDAndSessionID(new Long(newUserUid.longValue()), toolContentId);
			if(user == null){
				user = new SurveyUser();
				UserDTO sysUser = ((User)userManagementService.findById(User.class,newUserUid)).getUserDTO();
				user.setFirstName(sysUser.getFirstName());
				user.setLastName(sysUser.getLastName());
				user.setLoginName(sysUser.getLogin());
				user.setUserId(new Long(newUserUid.longValue()));
				user.setSurvey(toolContentObj);
			}
			toolContentObj.setCreatedBy(user);
			
			//reset all surveyItem createBy user
			Set<SurveyQuestion> items = toolContentObj.getQuestions();
			for(SurveyQuestion item:items){
				item.setCreateBy(user);
			}
			surveyDao.saveObject(toolContentObj);
		} catch (ImportToolContentException e) {
			throw new ToolException(e);
		}
	}

	public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {
		if (toContentId == null)
			throw new ToolException(
					"Failed to create the SharedSurveyFiles tool seession");

		Survey survey = null;
		if ( fromContentId != null ) {
			survey = 	surveyDao.getByContentId(fromContentId);
		}
		if ( survey == null ) {
			try {
				survey = getDefaultSurvey();
			} catch (SurveyApplicationException e) {
				throw new ToolException(e);
			}
		}

		Survey toContent = Survey.newInstance(survey,toContentId,surveyToolContentHandler);
		surveyDao.saveObject(toContent);
		
		//save survey items as well
		Set items = toContent.getQuestions();
		if(items != null){
			Iterator iter = items.iterator();
			while(iter.hasNext()){
				SurveyQuestion item = (SurveyQuestion) iter.next();
//				createRootTopic(toContent.getUid(),null,msg);
			}
		}
	}


	public void setAsDefineLater(Long toolContentId) throws DataMissingException, ToolException {
		Survey survey = surveyDao.getByContentId(toolContentId);
		if(survey == null){
			throw new ToolException("No found tool content by given content ID:" + toolContentId);
		}
		survey.setDefineLater(true);
	}


	public void setAsRunOffline(Long toolContentId) throws DataMissingException, ToolException {
		Survey survey = surveyDao.getByContentId(toolContentId);
		if(survey == null){
			throw new ToolException("No found tool content by given content ID:" + toolContentId);
		}
		survey.setRunOffline(true);		
	}


	public void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException, ToolException {
		Survey survey = surveyDao.getByContentId(toolContentId);
		if(removeSessionData){
			List list = surveySessionDao.getByContentId(toolContentId);
			Iterator iter = list.iterator();
			while(iter.hasNext()){
				SurveySession session = (SurveySession ) iter.next();
				surveySessionDao.delete(session);
			}
		}
		surveyDao.delete(survey);
	}

	
	public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
		SurveySession session = new SurveySession();
		session.setSessionId(toolSessionId);
		session.setSessionName(toolSessionName);
		Survey survey = surveyDao.getByContentId(toolContentId);
		session.setSurvey(survey);
		surveySessionDao.saveObject(session);
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
		return learnerService.completeToolSession(toolSessionId,learnerId);
	}


	public ToolSessionExportOutputData exportToolSession(Long toolSessionId) throws DataMissingException, ToolException {
		return null;
	}


	public ToolSessionExportOutputData exportToolSession(List toolSessionIds) throws DataMissingException, ToolException {
		return null;
	}


	public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
		surveySessionDao.deleteBySessionId(toolSessionId);
	}

	/* ===============Methods implemented from ToolContentImport102Manager =============== */

    /**
     * Import the data for a 1.0.2 Noticeboard or HTMLNoticeboard
     */
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues)
    {
    	Date now = new Date();
    	Survey toolContentObj = new Survey();

    	try {
	    	toolContentObj.setTitle((String)importValues.get(ToolContentImport102Manager.CONTENT_TITLE));
	    	toolContentObj.setContentId(toolContentId);
	    	toolContentObj.setContentInUse(Boolean.FALSE);
	    	toolContentObj.setCreated(now);
	    	toolContentObj.setDefineLater(Boolean.FALSE);
	    	toolContentObj.setInstructions((String)importValues.get(ToolContentImport102Manager.CONTENT_BODY));
	    	toolContentObj.setOfflineInstructions(null);
	    	toolContentObj.setOnlineInstructions(null);
	    	toolContentObj.setRunOffline(Boolean.FALSE);
	    	toolContentObj.setUpdated(now);

	    	toolContentObj.setLockWhenFinished(Boolean.FALSE);
	    	
	    	SurveyUser ruser = new SurveyUser();
	    	ruser.setUserId(new Long(user.getUserID().longValue()));
	    	ruser.setFirstName(user.getFirstName());
	    	ruser.setLastName(user.getLastName());
	    	ruser.setLoginName(user.getLogin());
			createUser(ruser);
		    toolContentObj.setCreatedBy(ruser);
	
	    	//survey questions
	    	Vector urls = (Vector) importValues.get(ToolContentImport102Manager.CONTENT_URL_URLS);
	    	if ( urls != null ) {
	    		Iterator iter = urls.iterator();
	    		while ( iter.hasNext() ) {
	    			Hashtable urlMap = (Hashtable) iter.next();
	    			
	    			SurveyQuestion item = new SurveyQuestion();
	    			item.setCreateDate(now);
	    			item.setCreateBy(ruser);
	
	    			Vector instructions = (Vector) urlMap.get(ToolContentImport102Manager.CONTENT_URL_URL_INSTRUCTION_ARRAY);
	    			if ( instructions != null && instructions.size() > 0 ) {
	    				item.setOptions(new HashSet());
	    				Iterator insIter = instructions.iterator();
	    				while (insIter.hasNext()) {
	    					Hashtable instructionEntry = (Hashtable) insIter.next();
							String instructionText = (String) instructionEntry.get(ToolContentImport102Manager.CONTENT_URL_INSTRUCTION);
							Integer order = WDDXProcessor.convertToInteger(instructionEntry, ToolContentImport102Manager.CONTENT_URL_URL_VIEW_ORDER);
							SurveyOption instruction = new SurveyOption();
							instruction.setDescription(instructionText);
							instruction.setSequenceId(order);
							item.getOptions().add(instruction);
						}
	    			}
	
	    			String surveyType = (String) urlMap.get(ToolContentImport102Manager.CONTENT_URL_URL_TYPE);
	
	    			// TODO add the order field - no support for it in forum at present.
	    			// public static final String CONTENT_URL_URL_VIEW_ORDER = "order";
	    			toolContentObj.getQuestions().add(item);
	    		}
	    	}
	    	
    	} catch (WDDXProcessorConversionException e) {
    		log.error("Unable to content for activity "+toolContentObj.getTitle()+"properly due to a WDDXProcessorConversionException.",e);
    		throw new ToolException("Invalid import data format for activity "+toolContentObj.getTitle()+"- WDDX caused an exception. Some data from the design will have been lost. See log for more details.");
    	}

    	surveyDao.saveObject(toolContentObj);


    }

    /** Set the description, throws away the title value as this is not supported in 2.0 */
    public void setReflectiveData(Long toolContentId, String title, String description) 
    		throws ToolException, DataMissingException {
    	
    	Survey toolContentObj = getSurveyByContentId(toolContentId);
    	if ( toolContentObj == null ) {
    		throw new DataMissingException("Unable to set reflective data titled "+title
	       			+" on activity toolContentId "+toolContentId
	       			+" as the tool content does not exist.");
    	}

    	// toolContentObj.setReflectOnActivity(Boolean.TRUE);
    	// toolContentObj.setReflectInstructions(description);
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
	public void setRepositoryService(IRepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	public void setSurveyAttachmentDao(SurveyAttachmentDAO surveyAttachmentDao) {
		this.surveyAttachmentDao = surveyAttachmentDao;
	}
	public void setSurveyDao(SurveyDAO surveyDao) {
		this.surveyDao = surveyDao;
	}
	public void setSurveyQuestionDao(SurveyQuestionDAO surveyItemDao) {
		this.surveyQuestionDao = surveyItemDao;
	}
	public void setSurveySessionDao(SurveySessionDAO surveySessionDao) {
		this.surveySessionDao = surveySessionDao;
	}
	public void setSurveyToolContentHandler(SurveyToolContentHandler surveyToolContentHandler) {
		this.surveyToolContentHandler = surveyToolContentHandler;
	}
	public void setSurveyUserDao(SurveyUserDAO surveyUserDao) {
		this.surveyUserDao = surveyUserDao;
	}
	public void setToolService(ILamsToolService toolService) {
		this.toolService = toolService;
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


	public void setSurveyAnswerDao(SurveyAnswerDAO surveyAnswerDao) {
		this.surveyAnswerDao = surveyAnswerDao;
	}



}
