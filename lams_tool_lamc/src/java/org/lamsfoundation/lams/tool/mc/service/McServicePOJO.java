/***************************************************************************
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
 * ***********************************************************************/
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.lamsfoundation.lams.contentrepository.AccessDeniedException;
import org.lamsfoundation.lams.contentrepository.FileException;
import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.contentrepository.ItemExistsException;
import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.LoginException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.WorkspaceNotFoundException;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;
import org.lamsfoundation.lams.contentrepository.service.SimpleCredentials;
import org.lamsfoundation.lams.gradebook.service.IGradebookService;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.tool.IToolVO;
import org.lamsfoundation.lams.tool.ToolContentImport102Manager;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McApplicationException;
import org.lamsfoundation.lams.tool.mc.McCandidateAnswersDTO;
import org.lamsfoundation.lams.tool.mc.McLearnerAnswersDTO;
import org.lamsfoundation.lams.tool.mc.McSessionMarkDTO;
import org.lamsfoundation.lams.tool.mc.McStringComparator;
import org.lamsfoundation.lams.tool.mc.McUserMarkDTO;
import org.lamsfoundation.lams.tool.mc.McUtils;
import org.lamsfoundation.lams.tool.mc.ReflectionDTO;
import org.lamsfoundation.lams.tool.mc.dao.IMcContentDAO;
import org.lamsfoundation.lams.tool.mc.dao.IMcOptionsContentDAO;
import org.lamsfoundation.lams.tool.mc.dao.IMcQueContentDAO;
import org.lamsfoundation.lams.tool.mc.dao.IMcSessionDAO;
import org.lamsfoundation.lams.tool.mc.dao.IMcUploadedFileDAO;
import org.lamsfoundation.lams.tool.mc.dao.IMcUserDAO;
import org.lamsfoundation.lams.tool.mc.dao.IMcUsrAttemptDAO;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McOptsContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueUsr;
import org.lamsfoundation.lams.tool.mc.pojos.McSession;
import org.lamsfoundation.lams.tool.mc.pojos.McUploadedFile;
import org.lamsfoundation.lams.tool.mc.pojos.McUsrAttempt;
import org.lamsfoundation.lams.tool.mc.web.MonitoringUtil;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.util.wddx.WDDXProcessorConversionException;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.dao.DataAccessException;

/**
 * 
 * @author Ozgur Demirtas
 * 
 *         The POJO implementation of Mc service. All business logics of MCQ tool are implemented in this class. It
 *         translate the request from presentation layer and perform appropriate database operation.
 * 
 */
public class McServicePOJO implements IMcService, ToolContentManager, ToolSessionManager, ToolContentImport102Manager,
	McAppConstants {
    static Logger logger = Logger.getLogger(McServicePOJO.class.getName());

    /* repository access related constants */
    private final String repositoryUser = "lamc11";
    private final char[] repositoryId = { 'l', 'a', 'm', 'c', '_', '1', '1' };
    private final String repositoryWorkspace = "lamc11";
    private IRepositoryService repositoryService;
    private ICredentials cred;

    private IMcContentDAO mcContentDAO;
    private IMcQueContentDAO mcQueContentDAO;
    private IMcOptionsContentDAO mcOptionsContentDAO;
    private IMcSessionDAO mcSessionDAO;
    private IMcUserDAO mcUserDAO;
    private IMcUsrAttemptDAO mcUsrAttemptDAO;
    private IMcUploadedFileDAO mcUploadedFileDAO;
    private MCOutputFactory mcOutputFactory;

    private IAuditService auditService;
    private IUserManagementService userManagementService;
    private ILearnerService learnerService;
    private ILamsToolService toolService;
    private IToolContentHandler mcToolContentHandler = null;
    private IExportToolContentService exportContentService;
    private IGradebookService gradebookService;

    private ICoreNotebookService coreNotebookService;

    private MessageService messageService;

    public McServicePOJO() {
    }

    public void configureContentRepository() throws McApplicationException {
	cred = new SimpleCredentials(repositoryUser, repositoryId);
	try {
	    repositoryService.createCredentials(cred);
	    repositoryService.addWorkspace(cred, repositoryWorkspace);
	} catch (ItemExistsException ie) {
	    McServicePOJO.logger.warn("Tried to configure repository but it "
		    + " appears to be already configured. Exception thrown by repository being ignored. ", ie);
	} catch (RepositoryCheckedException e) {
	    String error = "Error occured while trying to configure repository." + " Unable to recover from error: "
		    + e.getMessage();
	    McServicePOJO.logger.error(error, e);
	    throw new McApplicationException(error, e);
	}
    }

    public void createMc(McContent mcContent) throws McApplicationException {
	try {
	    mcContentDAO.saveMcContent(mcContent);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is creating mc content: " + e.getMessage(), e);
	}
    }

    public McContent retrieveMc(Long toolContentId) throws McApplicationException {
	try {
	    return mcContentDAO.findMcContentById(toolContentId);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is loading mc content: " + e.getMessage(), e);
	}
    }

    public void updateMcContent(McContent mcContent) throws McApplicationException {
	try {
	    mcContentDAO.updateMcContent(mcContent);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is updating mc content: " + e.getMessage(), e);
	}
    }

    public void createMcQue(McQueContent mcQueContent) throws McApplicationException {
	try {
	    mcQueContentDAO.saveMcQueContent(mcQueContent);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is creating mc que content: "
		    + e.getMessage(), e);
	}
    }

    public void updateMcQueContent(McQueContent mcQueContent) throws McApplicationException {
	try {
	    mcQueContentDAO.updateMcQueContent(mcQueContent);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is updating mc que content: "
		    + e.getMessage(), e);
	}

    }

    public List retrieveMcQueContentsByToolContentId(long mcContentId) throws McApplicationException {
	try {
	    return mcQueContentDAO.getMcQueContentsByContentId(mcContentId);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is loading mc que usr: " + e.getMessage(), e);
	}
    }

    public McQueContent getQuestionByDisplayOrder(final Long displayOrder, final Long mcContentUid)
	    throws McApplicationException {
	try {
	    return mcQueContentDAO.getQuestionContentByDisplayOrder(displayOrder, mcContentUid);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is getting mc que content by display order: "
		    + e.getMessage(), e);
	}
    }

    public McQueContent getMcQueContentByUID(Long uid) throws McApplicationException {
	try {
	    return mcQueContentDAO.getMcQueContentByUID(uid);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is getting mc que content by uid: "
		    + e.getMessage(), e);
	}
    }

    public List getAllQuestionEntriesSorted(final long mcContentId) throws McApplicationException {
	try {
	    return mcQueContentDAO.getAllQuestionEntriesSorted(mcContentId);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is getting all question entries: "
		    + e.getMessage(), e);
	}
    }

    public void saveOrUpdateMcQueContent(McQueContent mcQueContent) throws McApplicationException {
	try {
	    mcQueContentDAO.saveOrUpdateMcQueContent(mcQueContent);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is updating mc que content: "
		    + e.getMessage(), e);
	}
    }

    public void removeQuestionContentByMcUid(final Long mcContentUid) throws McApplicationException {
	try {
	    mcQueContentDAO.removeQuestionContentByMcUid(mcContentUid);
	} catch (DataAccessException e) {
	    throw new McApplicationException(
		    "Exception occured when lams is removing mc que content by mc content id: " + e.getMessage(), e);
	}
    }

    public void resetAllQuestions(final Long mcContentUid) throws McApplicationException {
	try {
	    mcQueContentDAO.resetAllQuestions(mcContentUid);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is resetting all questions: "
		    + e.getMessage(), e);
	}
    }

    public List getNextAvailableDisplayOrder(final long mcContentId) throws McApplicationException {
	try {
	    return mcQueContentDAO.getNextAvailableDisplayOrder(mcContentId);
	} catch (DataAccessException e) {
	    throw new McApplicationException(
		    "Exception occured when lams is getting the next available display order: " + e.getMessage(), e);
	}
    }

    public void createMcSession(McSession mcSession) throws McApplicationException {
	try {
	    mcSessionDAO.saveMcSession(mcSession);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is creating mc session: " + e.getMessage(), e);
	}
    }

    public McSession getMcSessionByUID(Long uid) throws McApplicationException {
	try {
	    return mcSessionDAO.getMcSessionByUID(uid);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is getting mcSession my uid: "
		    + e.getMessage(), e);
	}
    }
    
    public McQueUsr createMcUser(Long toolSessionID) throws McApplicationException {
	try {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    Long userId = toolUser.getUserID().longValue();
	    String userName = toolUser.getLogin();
	    String fullName = toolUser.getFirstName() + " " + toolUser.getLastName();
	    McSession mcSession = getMcSessionById(toolSessionID.longValue());

	    McQueUsr user = new McQueUsr(userId, userName, fullName, mcSession, new TreeSet());
	    mcUserDAO.saveMcUser(user);
	    
	    return user;
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is creating mc QueUsr: " + e.getMessage(), e);
	}
    }

    public void updateMcQueUsr(McQueUsr mcQueUsr) throws McApplicationException {
	try {
	    mcUserDAO.updateMcUser(mcQueUsr);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is updating mc QueUsr: " + e.getMessage(), e);
	}
    }

    public McQueUsr getMcUserBySession(final Long queUsrId, final Long mcSessionUid) throws McApplicationException {
	try {
	    return mcUserDAO.getMcUserBySession(queUsrId, mcSessionUid);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is getting mc QueUsr: " + e.getMessage(), e);
	}
    }

    public McQueUsr getMcUserByUID(Long uid) throws McApplicationException {
	try {
	    return mcUserDAO.getMcUserByUID(uid);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is getting the mc QueUsr by uid."
		    + e.getMessage(), e);
	}
    }

    public McQueUsr retrieveMcQueUsr(Long userId) throws McApplicationException {
	try {
	    McQueUsr mcQueUsr = mcUserDAO.findMcUserById(userId);
	    return mcQueUsr;
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is retrieving McQueUsr: " + e.getMessage(), e);
	}
    }

    @Override
    public void saveUserAttempt(McQueUsr user, List<McLearnerAnswersDTO> selectedQuestionAndCandidateAnswersDTO) {

	Date attemptTime = McUtils.getGMTDateTime();

	for (McLearnerAnswersDTO mcLearnerAnswersDTO : selectedQuestionAndCandidateAnswersDTO) {

	    Long questionUid = mcLearnerAnswersDTO.getQuestionUid();
	    McQueContent question = this.getQuestionByUid(questionUid);
	    if (question == null) {
		throw new McApplicationException("Can't find question with specified question uid: " + mcLearnerAnswersDTO.getQuestionUid());
	    }

	    McOptsContent answerOption = mcLearnerAnswersDTO.getAnswerOption();
	    if (answerOption != null) {
		    
		Integer mark = mcLearnerAnswersDTO.getMark();
		boolean passed = user.isMarkPassed(mark);
		boolean isAttemptCorrect = new Boolean(mcLearnerAnswersDTO.getAttemptCorrect());

		McUsrAttempt userAttempt = this.getUserAttemptByQuestion(user.getUid(), questionUid);
		if (userAttempt != null) {

		    McOptsContent previosAnswer = userAttempt.getMcOptionsContent();
		    // check if answer hasn't been changed since the last time
		    if (previosAnswer.getUid().equals(answerOption.getUid())) {
			// don't save anything
			continue;

		    } else {
			// in case answer has been changed update userttempt
			userAttempt.setAttemptTime(attemptTime);
			userAttempt.setMcOptionsContent(answerOption);
			userAttempt.setMark(mark);
			userAttempt.setPassed(passed);
			userAttempt.setAttemptCorrect(isAttemptCorrect);
		    }

		} else {
		    // create new userAttempt
		    userAttempt = new McUsrAttempt(attemptTime, question, user, answerOption, mark, passed, isAttemptCorrect);

		}

		mcUsrAttemptDAO.saveMcUsrAttempt(userAttempt);

	    }
	}
    }

    public void updateMcUsrAttempt(McUsrAttempt mcUsrAttempt) throws McApplicationException {
	try {
	    mcUsrAttemptDAO.updateMcUsrAttempt(mcUsrAttempt);
	} catch (DataAccessException e) {
	    throw new McApplicationException(
		    "Exception occured when lams is updating mc UsrAttempt: " + e.getMessage(), e);
	}
    }
    
    @Override
    public List<McLearnerAnswersDTO> buildLearnerAnswersDTOList(McContent mcContent, McQueUsr user) {
	List<McLearnerAnswersDTO> learnerAnswersDTOList = new LinkedList<McLearnerAnswersDTO>();
	List<McQueContent> questions = this.getAllQuestionEntries(mcContent.getUid());

	for (McQueContent question : questions) {
	    McLearnerAnswersDTO learnerAnswersDTO = new McLearnerAnswersDTO();
	    Set<McOptsContent> optionSet = question.getMcOptionsContents();
	    List<McOptsContent> optionList = new LinkedList<McOptsContent>(optionSet);
	    
	    boolean randomize = mcContent.isRandomize();
	    if (randomize) {
		ArrayList<McOptsContent> shuffledList = new ArrayList<McOptsContent>(optionList);
		Collections.shuffle(shuffledList);
		optionList = new LinkedList<McOptsContent>(shuffledList);
	    }

	    learnerAnswersDTO.setQuestion(question.getQuestion());
	    learnerAnswersDTO.setDisplayOrder(question.getDisplayOrder().toString());
	    learnerAnswersDTO.setQuestionUid(question.getUid());

	    learnerAnswersDTO.setMark(question.getMark());
	    learnerAnswersDTO.setOptions(optionList);

	    learnerAnswersDTOList.add(learnerAnswersDTO);
	}
	
	//populate answers
	if (user != null) {

	    for (McLearnerAnswersDTO learnerAnswersDTO : learnerAnswersDTOList) {
		Long questionUid = learnerAnswersDTO.getQuestionUid();
		
		McUsrAttempt dbAttempt = this.getUserAttemptByQuestion(user.getUid(), questionUid);
		if (dbAttempt != null) {
		    Long selectedOptionUid = dbAttempt.getMcOptionsContent().getUid();
		    
		    //mark selected option as selected
		    for (McOptsContent option : learnerAnswersDTO.getOptions()) {
			if (selectedOptionUid.equals(option.getUid())) {
			    option.setSelected(true);
			}
		    }
		}
	    }
	}

	return learnerAnswersDTOList;
    }
    
    @Override
    public List<McSessionMarkDTO> buildGroupsMarkData(McContent mcContent, boolean isFullAttemptDetailsRequired) {
	List<McSessionMarkDTO> listMonitoredMarksContainerDTO = new LinkedList<McSessionMarkDTO>();
	Set<McSession> sessions = mcContent.getMcSessions();
	Iterator<McSession> sessionsIterator = sessions.iterator();
	int numQuestions = mcContent.getMcQueContents().size();

	while (sessionsIterator.hasNext()) {
	    McSession mcSession = sessionsIterator.next();

	    McSessionMarkDTO mcSessionMarkDTO = new McSessionMarkDTO();
	    mcSessionMarkDTO.setSessionId(mcSession.getMcSessionId().toString());
	    mcSessionMarkDTO.setSessionName(mcSession.getSession_name().toString());

	    Set<McQueUsr> sessionUsers = mcSession.getMcQueUsers();
	    Iterator<McQueUsr> usersIterator = sessionUsers.iterator();

	    Map<String, McUserMarkDTO> mapSessionUsersData = new TreeMap<String, McUserMarkDTO>(new McStringComparator());
	    Long mapIndex = new Long(1);

	    while (usersIterator.hasNext()) {
		McQueUsr user = usersIterator.next();

		McUserMarkDTO mcUserMarkDTO = new McUserMarkDTO();
		mcUserMarkDTO.setSessionId(mcSession.getMcSessionId().toString());
		mcUserMarkDTO.setSessionName(mcSession.getSession_name().toString());
		mcUserMarkDTO.setFullName(user.getFullname());
		mcUserMarkDTO.setUserName(user.getUsername());
		mcUserMarkDTO.setQueUsrId(user.getUid().toString());

		if (isFullAttemptDetailsRequired) {

		    // The marks for the user must be listed in the display order of the question.
		    // Other parts of the code assume that the questions will be in consecutive display
		    // order starting 1 (e.g. 1, 2, 3, not 1, 3, 4) so we set up an array and use
		    // the ( display order - 1) as the index (arrays start at 0, rather than 1 hence -1)
		    // The user must answer all questions, so we can assume that they will have marks
		    // for all questions or no questions.
		    // At present there can only be one answer for each question but there may be more
		    // than one in the future and if so, we don't want to count the mark twice hence
		    // we need to check if we've already processed this question in the total.
		    Integer[] userMarks = new Integer[numQuestions];
		    String[] answeredOptions = new String[numQuestions];
		    Date attemptTime = null;
		    List<McUsrAttempt> finalizedUserAttempts = this.getFinalizedUserAttempts(user);
		    long totalMark = 0;
		    for (McUsrAttempt attempt : finalizedUserAttempts) {
			Integer displayOrder = attempt.getMcQueContent().getDisplayOrder();
			int arrayIndex = displayOrder != null && displayOrder.intValue() > 0 ? displayOrder.intValue() - 1
				: 1;
			if (userMarks[arrayIndex] == null) {

			    // We get the mark for the attempt if the answer is correct and we don't allow
			    // retries, or if the answer is correct and the learner has met the passmark if
			    // we do allow retries.
			    boolean isRetries = mcSession.getMcContent().isRetries();
			    Integer mark = attempt.getMarkForShow(isRetries);
			    userMarks[arrayIndex] = mark;
			    totalMark += mark.intValue();

			    // find out the answered option's sequential letter - A,B,C...
			    String answeredOptionLetter = "";
			    int optionCount = 1;
			    for (McOptsContent option : (Set<McOptsContent>) attempt.getMcQueContent()
				    .getMcOptionsContents()) {
				if (attempt.getMcOptionsContent().getUid().equals(option.getUid())) {
				    answeredOptionLetter = String.valueOf((char) (optionCount + 'A' - 1));
				    break;
				}
				optionCount++;
			    }
			    answeredOptions[arrayIndex] = answeredOptionLetter;
			}
			// get the attempt time, (NB all questions will have the same attempt time)
			// Not efficient, since we assign this value for each attempt
			attemptTime = attempt.getAttemptTime();
		    }

		    mcUserMarkDTO.setMarks(userMarks);
		    mcUserMarkDTO.setAnsweredOptions(answeredOptions);
		    mcUserMarkDTO.setAttemptTime(attemptTime);
		    mcUserMarkDTO.setTotalMark(new Long(totalMark));

		} else {
		    int totalMark = mcUsrAttemptDAO.getUserTotalMark(user.getUid());
		    mcUserMarkDTO.setTotalMark(new Long(totalMark));
		}

		mapSessionUsersData.put(mapIndex.toString(), mcUserMarkDTO);
		mapIndex = new Long(mapIndex.longValue() + 1);
	    }

	    mcSessionMarkDTO.setUserMarks(mapSessionUsersData);
	    listMonitoredMarksContainerDTO.add(mcSessionMarkDTO);
	}

	return listMonitoredMarksContainerDTO;
    }

    public List<McUsrAttempt> getFinalizedUserAttempts(final McQueUsr user) throws McApplicationException {
	try {
	    return mcUsrAttemptDAO.getFinalizedUserAttempts(user.getUid());
	} catch (DataAccessException e) {
	    throw new McApplicationException(
		    "Exception occured when lams is getting the learner's attempts by user id and que content id and attempt order: "
			    + e.getMessage(), e);
	}
    }

    public McUsrAttempt getUserAttemptByQuestion(Long queUsrUid, Long mcQueContentId)
	    throws McApplicationException {
	try {
	    return mcUsrAttemptDAO.getUserAttemptByQuestion(queUsrUid, mcQueContentId);
	} catch (DataAccessException e) {
	    throw new McApplicationException(
		    "Exception occured when lams is getting the learner's attempts by user id and que content id and attempt order: "
			    + e.getMessage(), e);
	}
    }

    public McQueContent retrieveMcQueContentByUID(Long uid) throws McApplicationException {
	try {
	    return mcQueContentDAO.getMcQueContentByUID(uid);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is retrieving by uid  mc question content: "
		    + e.getMessage(), e);
	}
    }

    public List<McQueContent> getAllQuestionEntries(final Long uid) throws McApplicationException {
	try {
	    return mcQueContentDAO.getAllQuestionEntries(uid.longValue());
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is getting by uid  mc question content: "
		    + e.getMessage(), e);
	}
    }

    public void removeMcQueContentByUID(Long uid) throws McApplicationException {
	try {
	    mcQueContentDAO.removeMcQueContentByUID(uid);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is removing by uid  mc question content: "
		    + e.getMessage(), e);
	}
    }

    public List refreshQuestionContent(final Long mcContentId) throws McApplicationException {
	try {
	    return mcQueContentDAO.refreshQuestionContent(mcContentId);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is refreshing  mc question content: "
		    + e.getMessage(), e);
	}

    }

    public void removeMcQueContent(McQueContent mcQueContent) throws McApplicationException {
	try {
	    mcQueContentDAO.removeMcQueContent(mcQueContent);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is removing mc question content: "
		    + e.getMessage(), e);
	}
    }

    public void removeMcOptionsContent(McOptsContent mcOptsContent) throws McApplicationException {
	try {
	    mcOptionsContentDAO.removeMcOptionsContent(mcOptsContent);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is removing" + " the mc options content: "
		    + e.getMessage(), e);
	}
    }

    public List<McCandidateAnswersDTO> populateCandidateAnswersDTO(Long mcQueContentId) throws McApplicationException {
	try {
	    return mcOptionsContentDAO.populateCandidateAnswersDTO(mcQueContentId);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is populating candidate answers dto"
		    + e.getMessage(), e);
	}
    }

    public McQueContent getQuestionContentByQuestionText(final String question, final Long mcContentId) {
	try {
	    return mcQueContentDAO.getQuestionContentByQuestionText(question, mcContentId);
	} catch (DataAccessException e) {
	    throw new McApplicationException(
		    "Exception occured when lams is retrieving question content by question text: " + e.getMessage(), e);
	}
    }

    public McSession getMcSessionById(Long mcSessionId) throws McApplicationException {
	try {
	    return mcSessionDAO.getMcSessionById(mcSessionId);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is retrieving by id mc session : "
		    + e.getMessage(), e);
	}
    }

    public McContent retrieveMcBySessionId(Long mcSessionId) throws McApplicationException {
	try {
	    return mcContentDAO.getMcContentBySession(mcSessionId);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is retrieving mc by session id: "
		    + e.getMessage(), e);
	}
    }

    public void updateMc(McContent mc) throws McApplicationException {
	try {
	    mcContentDAO.updateMcContent(mc);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is updating" + " the mc content: "
		    + e.getMessage(), e);
	}
    }

    public void updateMcSession(McSession mcSession) throws McApplicationException {
	try {
	    mcSessionDAO.updateMcSession(mcSession);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is updating mc session : " + e.getMessage(),
		    e);
	}
    }

    public void deleteMc(McContent mc) throws McApplicationException {
	try {
	    mcContentDAO.removeMc(mc);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is removing" + " the mc content: "
		    + e.getMessage(), e);
	}
    }

    public void deleteMcById(Long mcId) throws McApplicationException {
	try {
	    mcContentDAO.removeMcById(mcId);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is removing by id" + " the mc content: "
		    + e.getMessage(), e);
	}
    }

    /**
     * Return the top, lowest and average mark for all learners for one particular tool session.
     * 
     * @param request
     * @return top mark, lowest mark, average mark in that order
     */
    public Integer[] getMarkStatistics(McSession mcSession) {
	return mcUserDAO.getMarkStatisticsForSession(mcSession.getUid());
    }

    public void deleteMcQueUsr(McQueUsr mcQueUsr) throws McApplicationException {
	try {
	    mcUserDAO.removeMcUser(mcQueUsr);
	} catch (DataAccessException e) {
	    throw new McApplicationException(
		    "Exception occured when lams is removing" + " the user: " + e.getMessage(), e);
	}
    }

    public void saveMcContent(McContent mc) throws McApplicationException {
	try {
	    mcContentDAO.saveMcContent(mc);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is saving" + " the mc content: "
		    + e.getMessage(), e);
	}
    }

    public List findMcOptionsContentByQueId(Long mcQueContentId) throws McApplicationException {
	try {
	    List list = mcOptionsContentDAO.findMcOptionsContentByQueId(mcQueContentId);
	    return list;
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is finding by que id" + " the mc options: "
		    + e.getMessage(), e);
	}
    }

    public McOptsContent getMcOptionsContentByUID(Long uid) throws McApplicationException {
	try {
	    return mcOptionsContentDAO.getMcOptionsContentByUID(uid);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is getting opt content by uid"
		    + e.getMessage(), e);
	}
    }

    public McQueContent getQuestionByUid(Long uid) throws McApplicationException {
	try {
	    return mcQueContentDAO.findMcQuestionContentByUid(uid);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is getting que content by uid"
		    + e.getMessage(), e);
	}
    }

    public void saveMcOptionsContent(McOptsContent mcOptsContent) throws McApplicationException {
	try {
	    mcOptionsContentDAO.saveMcOptionsContent(mcOptsContent);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is saving" + " the mc options content: "
		    + e.getMessage(), e);
	}
    }

    public McOptsContent getOptionContentByOptionText(final String option, final Long mcQueContentUid) {
	try {
	    return mcOptionsContentDAO.getOptionContentByOptionText(option, mcQueContentUid);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is returning the"
		    + " option by option text: " + e.getMessage(), e);
	}
    }

    public void updateMcOptionsContent(McOptsContent mcOptsContent) throws McApplicationException {
	try {
	    mcOptionsContentDAO.updateMcOptionsContent(mcOptsContent);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is updating" + " the mc options content: "
		    + e.getMessage(), e);
	}
    }

    public List findMcOptionCorrectByQueId(Long mcQueContentId) throws McApplicationException {
	try {
	    return mcOptionsContentDAO.findMcOptionCorrectByQueId(mcQueContentId);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is getting correct/incorrect options list"
		    + " the mc options content: " + e.getMessage(), e);
	}

    }

    public void deleteMcOptionsContent(McOptsContent mcOptsContent) throws McApplicationException {
	try {
	    mcOptionsContentDAO.removeMcOptionsContent(mcOptsContent);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is removing" + " the mc options content: "
		    + e.getMessage(), e);
	}
    }

    public void removeMcOptionsContentByQueId(Long mcQueContentId) throws McApplicationException {
	try {
	    mcOptionsContentDAO.removeMcOptionsContentByQueId(mcQueContentId);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is removing by que id"
		    + " the mc options content: " + e.getMessage(), e);
	}
    }

    public void deleteMcOptionsContentByUID(Long uid) throws McApplicationException {
	try {
	    mcOptionsContentDAO.removeMcOptionsContentByUID(uid);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is removing by uid"
		    + " the mc options content: " + e.getMessage(), e);
	}
    }

    /**
     * checks the parameter content in the user responses table
     * 
     * @param mcContent
     * @return boolean
     * @throws McApplicationException
     */
    public boolean studentActivityOccurredGlobal(McContent mcContent) throws McApplicationException {
	Iterator questionIterator = mcContent.getMcQueContents().iterator();
	while (questionIterator.hasNext()) {
	    McQueContent mcQueContent = (McQueContent) questionIterator.next();
	    Iterator attemptsIterator = mcQueContent.getMcUsrAttempts().iterator();
	    while (attemptsIterator.hasNext()) {
		/**
		 * proved the fact that there is at least one attempt for this content.
		 */
		return true;
	    }
	}
	return false;
    }
    
    @Override
    public void changeUserAttemptMark(Long userAttemptUid, Integer newMark) {
	if (newMark == null) {
	    return;
	}
	
	McUsrAttempt userAttempt = mcUsrAttemptDAO.getUserAttemptByUid(userAttemptUid);
	Integer userId = userAttempt.getMcQueUsr().getQueUsrId().intValue();
	Long userUid = userAttempt.getMcQueUsr().getUid();
	Long toolSessionId = userAttempt.getMcQueUsr().getMcSession().getMcSessionId();	
	Integer oldMark = userAttempt.getMark();
	int oldTotalMark = mcUsrAttemptDAO.getUserTotalMark(userUid);
	
	userAttempt.setMark(newMark);
	mcUsrAttemptDAO.saveMcUsrAttempt(userAttempt);

	// propagade changes to Gradebook
	int totalMark = (oldMark == null) ? oldTotalMark + newMark : oldTotalMark - oldMark + newMark;
	gradebookService.updateActivityMark(new Double(totalMark), null, userId, toolSessionId, true);
	
	//record mark change with audit service
	auditService.logMarkChange(McAppConstants.MY_SIGNATURE, userAttempt.getMcQueUsr().getQueUsrId(), userAttempt
		.getMcQueUsr().getUsername(), "" + oldMark, "" + totalMark);

    }
    
    @Override
    public byte[] prepareSessionDataSpreadsheet(McContent mcContent) throws IOException {
	
	Set<McQueContent> questions = mcContent.getMcQueContents();
	int maxOptionsInQuestion = 0;
	for (McQueContent question : questions) {
	    if (question.getMcOptionsContents().size() > maxOptionsInQuestion) {
		maxOptionsInQuestion = question.getMcOptionsContents().size();
	    }
	}
	
	int totalNumberOfUsers = 0;
	for (McSession session : (Set<McSession>) mcContent.getMcSessions()) {
	    totalNumberOfUsers += session.getMcQueUsers().size();
	}
	
	List<McSessionMarkDTO> sessionMarkDTOs = this.buildGroupsMarkData(mcContent, true);
	
	// create an empty excel file
	HSSFWorkbook wb = new HSSFWorkbook();
	HSSFCellStyle greenColor = wb.createCellStyle();
	greenColor.setFillForegroundColor(IndexedColors.GREEN.getIndex());
	greenColor.setFillPattern(CellStyle.SOLID_FOREGROUND);
	
	// ======================================================= Report by question IRA page
	// =======================================
	
	HSSFSheet sheet = wb.createSheet(messageService.getMessage("label.report.by.question"));

	HSSFRow row;
	HSSFCell cell;
	int rowCount = 0;
	
	row = sheet.createRow(rowCount++);
	int count = 0;
	cell = row.createCell(count++);
	cell.setCellValue(messageService.getMessage("label.question"));
	for (int optionCount = 0; optionCount < maxOptionsInQuestion; optionCount++) {
	    cell = row.createCell(count++);
	    cell.setCellValue(String.valueOf((char)(optionCount + 'A')));
	}
	cell = row.createCell(count++);
	cell.setCellValue(messageService.getMessage("label.not.available"));
	
	for (McQueContent question : questions) {

	    row = sheet.createRow(rowCount);
	    count = 0;

	    cell = row.createCell(count++);
	    cell.setCellValue(rowCount);
	    rowCount++;

	    int totalPercentage = 0;
	    for (McOptsContent option : (Set<McOptsContent>) question.getMcOptionsContents()) {
		int optionAttemptCount = mcUsrAttemptDAO.getAttemptsCountPerOption(option.getUid());
		cell = row.createCell(count++);
		int percentage = optionAttemptCount * 100 / totalNumberOfUsers;
		cell.setCellValue(percentage + "%");
		totalPercentage += percentage;
		if (option.isCorrectOption()) {
		    cell.setCellStyle(greenColor);
		}
	    }
	    cell = row.createCell(maxOptionsInQuestion + 1);
	    cell.setCellValue(100 - totalPercentage + "%");
	}
	
	rowCount++;
	row = sheet.createRow(rowCount++);
	cell = row.createCell(0);
	cell.setCellValue(messageService.getMessage("label.legend"));
	row = sheet.createRow(rowCount++);
	cell = row.createCell(0);
	cell.setCellValue(messageService.getMessage("label.denotes.correct.answer"));
	cell.setCellStyle(greenColor);
	cell = row.createCell(1);
	cell.setCellStyle(greenColor);
	cell = row.createCell(2);
	cell.setCellStyle(greenColor);
	
	// ======================================================= Report by student IRA page
	// =======================================
	
	sheet = wb.createSheet(messageService.getMessage("label.report.by.student"));
	rowCount = 0;
	
	row = sheet.createRow(rowCount++);
	count = 2;
	for (int questionCount = 1; questionCount <= questions.size(); questionCount++) {
	    cell = row.createCell(count++);
	    cell.setCellValue(messageService.getMessage("label.question") + questionCount);
	}
	cell = row.createCell(count++);
	cell.setCellValue(messageService.getMessage("label.total"));
	cell = row.createCell(count++);
	cell.setCellValue(messageService.getMessage("label.total") + " %");

	row = sheet.createRow(rowCount++);
	count = 1;
	ArrayList<String> correctAnswers = new ArrayList<String>();
	cell = row.createCell(count++);
	cell.setCellValue(messageService.getMessage("label.correct.answer"));
	for (McQueContent question : questions) {

	    // find out the correct answer's sequential letter - A,B,C...
	    String correctAnswerLetter = "";
	    int answerCount = 1;
	    for (McOptsContent option : (Set<McOptsContent>) question.getMcOptionsContents()) {
		if (option.isCorrectOption()) {
		    correctAnswerLetter = String.valueOf((char) (answerCount + 'A' - 1));
		    break;
		}
		answerCount++;
	    }
	    cell = row.createCell(count++);
	    cell.setCellValue(correctAnswerLetter);
	    correctAnswers.add(correctAnswerLetter);
	}
	
	row = sheet.createRow(rowCount++);
	count = 0;
	cell = row.createCell(count++);
	cell.setCellValue(messageService.getMessage("group.label"));
	cell = row.createCell(count++);
	cell.setCellValue(messageService.getMessage("label.learner"));
	
	int groupCount = 0;
	ArrayList<Integer> totalPercentList = new ArrayList<Integer>();
	int[] numberOfCorrectAnswersPerQuestion = new int[questions.size()];
	for (McSessionMarkDTO sessionMarkDTO : sessionMarkDTOs) {
	    Map<String, McUserMarkDTO> usersMarksMap = sessionMarkDTO.getUserMarks();
	    groupCount++;

	    for (McUserMarkDTO userMark : usersMarksMap.values()) {
		row = sheet.createRow(rowCount++);
		count = 0;
		cell = row.createCell(count++);
		cell.setCellValue(groupCount);
		
		cell = row.createCell(count++);
		cell.setCellValue(userMark.getFullName());
		
		String[] answeredOptions = userMark.getAnsweredOptions();
		int numberOfCorrectlyAnsweredByUser = 0;
		for (int i = 0; i < answeredOptions.length; i++) {
		    String answeredOption = answeredOptions[i];
		    cell = row.createCell(count++);
		    cell.setCellValue(answeredOption);
		    if (StringUtils.equals(answeredOption, correctAnswers.get(i))) {
			cell.setCellStyle(greenColor);
			numberOfCorrectlyAnsweredByUser++;
			numberOfCorrectAnswersPerQuestion[count-3]++;
		    }
		}
		
		cell = row.createCell(count++);
		cell.setCellValue(new Long(userMark.getTotalMark()));

		int totalPercents = numberOfCorrectlyAnsweredByUser * 100 / questions.size(); 
		totalPercentList.add(totalPercents);
		cell = row.createCell(count++);
		cell.setCellValue(totalPercents + "%");
	    }
	    
	    rowCount++;
	}
	
	//ave
	row = sheet.createRow(rowCount++);
	count = 1;
	cell = row.createCell(count++);
	cell.setCellValue(messageService.getMessage("label.ave"));
	for (int numberOfCorrectAnswers : numberOfCorrectAnswersPerQuestion) {
	    cell = row.createCell(count++);
	    cell.setCellValue(numberOfCorrectAnswers * 100 / totalPercentList.size() + "%");
	}

	//class mean
	Integer[] totalPercents = totalPercentList.toArray(new Integer[0]);
	Arrays.sort(totalPercents);
	int sum = 0;
	for (int i = 0; i < totalPercents.length; i++) {
	    sum += totalPercents[i];
	}
	row = sheet.createRow(rowCount++);
	cell = row.createCell(1);
	cell.setCellValue(messageService.getMessage("label.class.mean"));
	if (totalPercents.length != 0) {
	    int classMean = sum / totalPercents.length;
	    cell = row.createCell(questions.size() + 3);
	    cell.setCellValue(classMean + "%");
	}
	
	// median
	row = sheet.createRow(rowCount++);
	cell = row.createCell(1);
	cell.setCellValue(messageService.getMessage("label.median"));
	if (totalPercents.length != 0) {
	    int median;
	    int middle = totalPercents.length / 2;
	    if (totalPercents.length % 2 == 1) {
		median = totalPercents[middle];
	    } else {
		median = (int) ((totalPercents[middle - 1] + totalPercents[middle]) / 2.0);
	    }
	    cell = row.createCell(questions.size() + 3);
	    cell.setCellValue(median + "%");
	}

	row = sheet.createRow(rowCount++);
	cell = row.createCell(0);
	cell.setCellValue(messageService.getMessage("label.legend"));

	row = sheet.createRow(rowCount++);
	cell = row.createCell(0);
	cell.setCellValue(messageService.getMessage("label.denotes.correct.answer"));
	cell.setCellStyle(greenColor);
	cell = row.createCell(1);
	cell.setCellStyle(greenColor);
	cell = row.createCell(2);
	cell.setCellStyle(greenColor);
	
	// ======================================================= Marks page
	// =======================================
	
	sheet = wb.createSheet("Marks");

	rowCount = 0;
	count = 0;
	
	row = sheet.createRow(rowCount++);
	for (McQueContent question : questions) {
	    cell = row.createCell(2 + count++);
	    cell.setCellValue(messageService.getMessage("label.monitoring.downloadMarks.question.mark",
		    new Object[] { count, question.getMark() }));
	}
	
	for (McSessionMarkDTO sessionMarkDTO : sessionMarkDTOs) {
	    Map<String, McUserMarkDTO> usersMarksMap = sessionMarkDTO.getUserMarks();

	    String currentSessionName = sessionMarkDTO.getSessionName();

	    row = sheet.createRow(rowCount++);

	    cell = row.createCell(0);
	    cell.setCellValue(messageService.getMessage("group.label"));

	    cell = row.createCell(1);
	    cell.setCellValue(currentSessionName);
	    cell.setCellStyle(greenColor);

	    rowCount++;
	    count = 0;

	    row = sheet.createRow(rowCount++);

	    cell = row.createCell(count++);
	    cell.setCellValue(messageService.getMessage("label.learner"));

	    cell = row.createCell(count++);
	    cell.setCellValue(messageService.getMessage("label.monitoring.downloadMarks.username"));

	    cell = row.createCell(questions.size() + 2);
	    cell.setCellValue(messageService.getMessage("label.total"));

	    for (McUserMarkDTO userMark : usersMarksMap.values()) {
		row = sheet.createRow(rowCount++);
		count = 0;

		cell = row.createCell(count++);
		cell.setCellValue(userMark.getFullName());

		cell = row.createCell(count++);
		cell.setCellValue(userMark.getUserName());

		Integer[] marks = userMark.getMarks();
		for (int i = 0; i < marks.length; i++) {
		    cell = row.createCell(count++);
		    Integer mark = (marks[i] == null) ? 0 : marks[i];
		    cell.setCellValue(mark);
		}

		cell = row.createCell(count++);
		cell.setCellValue(userMark.getTotalMark());
	    }

	    rowCount++;

	}

	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	wb.write(bos);

	byte[] data = bos.toByteArray();

	return data;
    }

    /**
     * implemented as part of the Tool Contract copyToolContent(Long fromContentId, Long toContentId) throws
     * ToolException
     * 
     * @param fromContentId
     * @param toContentId
     * @return
     * @throws ToolException
     * 
     */
    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {

	if (fromContentId == null) {
	    McServicePOJO.logger.warn("fromContentId is null.");
	    long defaultContentId = 0;
	    try {
		defaultContentId = getToolDefaultContentIdBySignature(McAppConstants.MY_SIGNATURE);
		fromContentId = new Long(defaultContentId);
	    } catch (Exception e) {
		McServicePOJO.logger.error("default content id has not been setup for signature: "
			+ McAppConstants.MY_SIGNATURE);
		throw new ToolException("WARNING! default content has not been setup for signature"
			+ McAppConstants.MY_SIGNATURE + " Can't continue!");
	    }
	}

	if (toContentId == null) {
	    McServicePOJO.logger.error("throwing ToolException: toContentId is null");
	    throw new ToolException("toContentId is missing");
	}

	try {
	    McContent fromContent = mcContentDAO.findMcContentById(fromContentId);

	    if (fromContent == null) {
		McServicePOJO.logger.warn("fromContent is null.");
		long defaultContentId = 0;
		try {
		    defaultContentId = getToolDefaultContentIdBySignature(McAppConstants.MY_SIGNATURE);
		    fromContentId = new Long(defaultContentId);
		} catch (Exception e) {
		    McServicePOJO.logger.error("default content id has not been setup for signature: "
			    + McAppConstants.MY_SIGNATURE);
		    throw new ToolException("WARNING! default content has not been setup for signature"
			    + McAppConstants.MY_SIGNATURE + " Can't continue!");
		}

		fromContent = mcContentDAO.findMcContentById(fromContentId);
	    }

	    try {
		McContent toContent = McContent.newInstance(mcToolContentHandler, fromContent, toContentId);
		if (toContent == null) {
		    McServicePOJO.logger.error("throwing ToolException: WARNING!, retrieved toContent is null.");
		    throw new ToolException("WARNING! Fail to create toContent. Can't continue!");
		} else {
		    mcContentDAO.saveMcContent(toContent);
		}

	    } catch (ItemNotFoundException e) {
		McServicePOJO.logger.error("exception occurred: " + e);
	    } catch (RepositoryCheckedException e) {
		McServicePOJO.logger.error("exception occurred: " + e);
	    }
	} catch (DataAccessException e) {
	    McServicePOJO.logger
		    .error("throwing ToolException: Exception occured when lams is copying content between content ids.");
	    throw new ToolException("Exception occured when lams is copying content between content ids.");
	}
    }

    /**
     * implemented as part of the tool contract. Removes content and uploaded files from the content repository.
     * removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException, ToolException
     * 
     * @param toContentId
     * @param removeSessionData
     * @return
     * @throws ToolException
     */
    public void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException,
	    ToolException {

	if (toolContentId == null) {
	    McServicePOJO.logger.error("toolContentId is null");
	    throw new ToolException("toolContentId is missing");
	}

	McContent mcContent = mcContentDAO.findMcContentById(toolContentId);

	if (mcContent != null) {
	    Iterator filesIterator = mcContent.getMcAttachments().iterator();
	    while (filesIterator.hasNext()) {
		McUploadedFile mcUploadedFile = (McUploadedFile) filesIterator.next();
		String filesUuid = mcUploadedFile.getUuid();
		if (filesUuid != null && filesUuid.length() > 0) {
		    try {
			mcToolContentHandler.deleteFile(new Long(filesUuid));
		    } catch (RepositoryCheckedException e) {
			McServicePOJO.logger.error("exception occured deleting files from content repository : " + e);
			throw new ToolException("undeletable file in the content repository");
		    }
		}
	    }

	    Iterator sessionIterator = mcContent.getMcSessions().iterator();
	    while (sessionIterator.hasNext()) {
		if (removeSessionData == false) {
		    throw new SessionDataExistsException();
		}

		McSession mcSession = (McSession) sessionIterator.next();

		Iterator sessionUsersIterator = mcSession.getMcQueUsers().iterator();
		while (sessionUsersIterator.hasNext()) {
		    McQueUsr mcQueUsr = (McQueUsr) sessionUsersIterator.next();
		    
		    mcUsrAttemptDAO.removeAllUserAttempts(mcQueUsr.getUid());
		}
	    }
	    mcContentDAO.removeMcById(toolContentId);
	} else {
	    McServicePOJO.logger.error("Warning!!!, We should have not come here. mcContent is null.");
	    throw new ToolException("toolContentId is missing");
	}
    }

    /**
     * Implemented as part of the tool contract. Sets the defineLater to true on this content. setAsDefineLater(Long
     * toolContentId) throws DataMissingException, ToolException
     * 
     * @param toolContentId
     * @return
     * @throws ToolException
     */
    public void setAsDefineLater(Long toolContentId, boolean value) throws DataMissingException, ToolException {
	if (toolContentId == null) {
	    McServicePOJO.logger.error("throwing DataMissingException: WARNING!: retrieved toolContentId is null.");
	    throw new DataMissingException("toolContentId is missing");
	}

	McContent mcContent = retrieveMc(toolContentId);
	if (mcContent == null) {
	    McServicePOJO.logger.error("throwing DataMissingException: WARNING!: retrieved mcContent is null.");
	    throw new DataMissingException("mcContent is missing");
	}
	mcContent.setDefineLater(value);
	saveMcContent(mcContent);
    }

    /**
     * Implemented as part of the tool contract. Sets the runOffline to true on this content. setAsRunOffline(Long
     * toolContentId) throws DataMissingException, ToolException
     * 
     * @param toolContentId
     *            return
     * @throws ToolException
     */
    public void setAsRunOffline(Long toolContentId, boolean value) throws DataMissingException, ToolException {
	if (toolContentId == null) {
	    McServicePOJO.logger.error("throwing DataMissingException: WARNING!: retrieved toolContentId is null.");
	    throw new DataMissingException("toolContentId is missing");
	}
	McContent mcContent = mcContentDAO.findMcContentById(toolContentId);
	if (mcContent == null) {
	    McServicePOJO.logger.error("throwing DataMissingException: WARNING!: retrieved mcContent is null.");
	    throw new DataMissingException("mcContent is missing");
	}
	mcContent.setRunOffline(value);
	saveMcContent(mcContent);
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
	McContent toolContentObj = mcContentDAO.findMcContentById(toolContentId);
	if (toolContentObj == null) {
	    long defaultContentId = getToolDefaultContentIdBySignature(McAppConstants.MY_SIGNATURE);
	    toolContentObj = mcContentDAO.findMcContentById(defaultContentId);
	}
	if (toolContentObj == null) {
	    throw new DataMissingException("Unable to find default content for the multiple choice tool");
	}

	try {
	    // set ToolContentHandler as null to avoid copy file node in repository again.
	    toolContentObj = McContent.newInstance(null, toolContentObj, toolContentId);
	    toolContentObj.setMcSessions(null);
	    Set<McUploadedFile> files = toolContentObj.getMcAttachments();
	    for (McUploadedFile file : files) {
		file.setMcContent(null);
	    }
	    exportContentService.registerFileClassForExport(McUploadedFile.class.getName(), "uuid", null);
	    exportContentService.exportToolContent(toolContentId, toolContentObj, mcToolContentHandler, rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	} catch (ItemNotFoundException e) {
	    throw new ToolException(e);
	} catch (RepositoryCheckedException e) {
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
	    // register File node class
	    exportContentService.registerFileClassForImport(McUploadedFile.class.getName(), "uuid", null, "fileName",
		    "fileProperty", null, null);

	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(McImportContentVersionFilter.class);

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, mcToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof McContent)) {
		throw new ImportToolContentException("Import MC tool content failed. Deserialized object is "
			+ toolPOJO);
	    }
	    McContent toolContentObj = (McContent) toolPOJO;

	    // reset it to new toolContentId
	    toolContentObj.setMcContentId(toolContentId);
	    toolContentObj.setCreatedBy(newUserUid);
	    Set<McUploadedFile> files = toolContentObj.getMcAttachments();
	    for (McUploadedFile file : files) {
		// file.setMcContentId(toolContentId);
		file.setMcContent(toolContentObj);
	    }
	    mcContentDAO.saveMcContent(toolContentObj);
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
	McContent content = retrieveMc(toolContentId);
	if (content == null) {
	    long defaultToolContentId = getToolDefaultContentIdBySignature(McAppConstants.MY_SIGNATURE);
	    content = retrieveMc(defaultToolContentId);
	}
	return getMcOutputFactory().getToolOutputDefinitions(content, definitionType);
    }

    public String getToolContentTitle(Long toolContentId) {
	return  mcContentDAO.findMcContentById(toolContentId).getTitle();
    }
    
    /**
     * it is possible that the tool session id already exists in the tool sessions table as the users from the same
     * session are involved. existsSession(long toolSessionId)
     * 
     * @param toolSessionId
     * @return boolean
     */
    public boolean existsSession(Long toolSessionId) {
	McSession mcSession = getMcSessionById(toolSessionId);
	return mcSession != null;
    }

    /**
     * Implemented as part of the tool contract. Gets called only in the Learner mode. All the learners in the same
     * group have the same toolSessionId.
     * 
     * @param toolSessionId
     *            the generated tool session id.
     * @param toolSessionName
     *            the tool session name.
     * @param toolContentId
     *            the tool content id specified.
     * @throws ToolException
     *             if an error occurs e.g. defaultContent is missing.
     * 
     */
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {

	if (toolSessionId == null) {
	    McServicePOJO.logger.error("toolSessionId is null");
	    throw new ToolException("toolSessionId is missing");
	}

	long defaultContentId = 0;
	if (toolContentId == null) {

	    try {
		defaultContentId = getToolDefaultContentIdBySignature(McAppConstants.MY_SIGNATURE);
		toolContentId = new Long(defaultContentId);
	    } catch (Exception e) {
		McServicePOJO.logger.error("default content id has not been setup for signature: "
			+ McAppConstants.MY_SIGNATURE);
		throw new ToolException("WARNING! default content has not been setup for signature"
			+ McAppConstants.MY_SIGNATURE + " Can't continue!");
	    }
	}

	McContent mcContent = mcContentDAO.findMcContentById(toolContentId);

	if (mcContent == null) {

	    try {
		defaultContentId = getToolDefaultContentIdBySignature(McAppConstants.MY_SIGNATURE);
		toolContentId = new Long(defaultContentId);
	    } catch (Exception e) {
		McServicePOJO.logger.error("default content id has not been setup for signature: "
			+ McAppConstants.MY_SIGNATURE);
		throw new ToolException("WARNING! default content has not been setup for signature"
			+ McAppConstants.MY_SIGNATURE + " Can't continue!");
	    }

	    mcContent = mcContentDAO.findMcContentById(toolContentId);
	}

	/*
	 * create a new a new tool session if it does not already exist in the tool session table
	 */
	if (!existsSession(toolSessionId)) {
	    try {
		McSession mcSession = new McSession(toolSessionId, new Date(System.currentTimeMillis()),
			McSession.INCOMPLETE, toolSessionName, mcContent, new TreeSet());

		mcSessionDAO.saveMcSession(mcSession);

	    } catch (Exception e) {
		McServicePOJO.logger.error("Error creating new toolsession in the db");
		throw new ToolException("Error creating new toolsession in the db: " + e);
	    }
	}
    }

    /**
     * Implemented as part of the tool contract. removeToolSession(Long toolSessionId) throws DataMissingException,
     * ToolException
     * 
     * @param toolSessionId
     * @param toolContentId
     *            return
     * @throws ToolException
     */
    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	if (toolSessionId == null) {
	    McServicePOJO.logger.error("toolSessionId is null");
	    throw new DataMissingException("toolSessionId is missing");
	}

	McSession mcSession = null;
	try {
	    mcSession = getMcSessionById(toolSessionId);
	} catch (McApplicationException e) {
	    throw new DataMissingException("error retrieving mcSession: " + e);
	} catch (Exception e) {
	    throw new ToolException("error retrieving mcSession: " + e);
	}

	if (mcSession == null) {
	    McServicePOJO.logger.error("mcSession is null");
	    throw new DataMissingException("mcSession is missing");
	}

	try {
	    mcSessionDAO.removeMcSession(mcSession);
	    McServicePOJO.logger.debug("mcSession " + mcSession + " has been deleted successfully.");
	} catch (McApplicationException e) {
	    throw new ToolException("error deleting mcSession:" + e);
	}
    }

    /**
     * Implemtented as part of the tool contract. leaveToolSession(Long toolSessionId,Long learnerId) throws
     * DataMissingException, ToolException
     * 
     * @param toolSessionId
     * @param learnerId
     *            return String
     * @throws ToolException
     * 
     */
    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {

	if (learnerService == null) {
	    return "dummyNextUrl";
	}

	if (learnerId == null) {
	    McServicePOJO.logger.error("learnerId is null");
	    throw new DataMissingException("learnerId is missing");
	}

	if (toolSessionId == null) {
	    McServicePOJO.logger.error("toolSessionId is null");
	    throw new DataMissingException("toolSessionId is missing");
	}

	McSession mcSession = null;
	try {
	    mcSession = getMcSessionById(toolSessionId);
	} catch (McApplicationException e) {
	    throw new DataMissingException("error retrieving mcSession: " + e);
	} catch (Exception e) {
	    throw new ToolException("error retrieving mcSession: " + e);
	}
	mcSession.setSessionStatus(McAppConstants.COMPLETED);
	mcSessionDAO.updateMcSession(mcSession);

	String nextUrl = learnerService.completeToolSession(toolSessionId, learnerId);
	if (nextUrl == null) {
	    McServicePOJO.logger.error("nextUrl is null");
	    throw new ToolException("nextUrl is null");
	}
	return nextUrl;
    }

    /**
     * exportToolSession(Long toolSessionId) throws DataMissingException, ToolException
     * 
     * @param toolSessionId
     *            return ToolSessionExportOutputData
     * @throws ToolException
     */
    public ToolSessionExportOutputData exportToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	throw new ToolException("not yet implemented");
    }

    /**
     * exportToolSession(Long toolSessionId) throws DataMissingException, ToolException
     * 
     * @param toolSessionIds
     *            return ToolSessionExportOutputData
     * @throws ToolException
     */
    public ToolSessionExportOutputData exportToolSession(List toolSessionIds) throws DataMissingException,
	    ToolException {
	throw new ToolException("not yet implemented");

    }

    /**
     * Get the tool output for the given tool output names.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.util.List<String>, java.lang.Long,
     *      java.lang.Long)
     */
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {

	return mcOutputFactory.getToolOutput(names, this, toolSessionId, learnerId);
    }

    /**
     * Get the tool output for the given tool output name.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.lang.String, java.lang.Long,
     *      java.lang.Long)
     */
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return mcOutputFactory.getToolOutput(name, this, toolSessionId, learnerId);
    }

    public IToolVO getToolBySignature(String toolSignature) throws McApplicationException {
	IToolVO tool = toolService.getToolBySignature(toolSignature);
	return tool;
    }

    public long getToolDefaultContentIdBySignature(String toolSignature) throws McApplicationException {
	long contentId = 0;
	contentId = toolService.getToolDefaultContentIdBySignature(toolSignature);
	return contentId;
    }

    public McQueContent getToolDefaultQuestionContent(long contentId) throws McApplicationException {
	McQueContent mcQueContent = mcQueContentDAO.getToolDefaultQuestionContent(contentId);
	return mcQueContent;
    }

    public void removeAttachment(McContent content, McUploadedFile attachment) throws RepositoryCheckedException {
	try {
	    attachment.setMcContent(null);
	    content.getMcAttachments().remove(attachment);
	    mcToolContentHandler.deleteFile(new Long(attachment.getUuid()));
	    saveMcContent(content);
	} catch (DataAccessException e) {
	    throw new McApplicationException(
		    "EXCEPTION: An exception has occurred while trying to remove this attachment" + e.getMessage(), e);
	}
    }

    public NodeKey uploadFile(InputStream istream, String filename, String contentType, String fileType)
	    throws RepositoryCheckedException {
	return mcToolContentHandler.uploadFile(istream, filename, contentType, fileType);
    }

    public NodeKey copyFile(Long uuid) throws RepositoryCheckedException {
	return mcToolContentHandler.copyFile(uuid);
    }

    /**
     * This method verifies the credentials of the SubmitFiles Tool and gives it the <code>Ticket</code> to login and
     * access the Content Repository.
     * 
     * A valid ticket is needed in order to access the content from the repository. This method would be called evertime
     * the tool needs to upload/download files from the content repository.
     * 
     * @return ITicket The ticket for repostory access
     * @throws SubmitFilesException
     */
    public ITicket getRepositoryLoginTicket() throws McApplicationException {
	ICredentials credentials = new SimpleCredentials(repositoryUser, repositoryId);
	try {
	    ITicket ticket = repositoryService.login(credentials, repositoryWorkspace);
	    return ticket;
	} catch (AccessDeniedException e) {
	    throw new McApplicationException("Access Denied to repository." + e.getMessage());
	} catch (WorkspaceNotFoundException e) {
	    throw new McApplicationException("Workspace not found." + e.getMessage());
	} catch (LoginException e) {
	    throw new McApplicationException("Login failed." + e.getMessage());
	}
    }

    /**
     * This method deletes the content with the given <code>uuid</code> and <code>versionID</code> from the content
     * repository
     * 
     * @param uuid
     *            The <code>uuid</code> of the node to be deleted
     * @param versionID
     *            The <code>version_id</code> of the node to be deleted.
     * @throws SubmitFilesException
     */
    public void deleteFromRepository(Long uuid, Long versionID) throws McApplicationException {
	ITicket ticket = getRepositoryLoginTicket();
	try {
	    String files[] = repositoryService.deleteVersion(ticket, uuid, versionID);
	} catch (Exception e) {
	    throw new McApplicationException("Exception occured while deleting files from" + " the repository "
		    + e.getMessage());
	}
    }

    /**
     * This method is called everytime a new content has to be added to the repository. In order to do so first of all a
     * valid ticket is obtained from the Repository hence authenticating the tool(SubmitFiles) and then the
     * corresponding file is added to the repository.
     * 
     * @param stream
     *            The <code>InputStream</code> representing the data to be added
     * @param fileName
     *            The name of the file being added
     * @param mimeType
     *            The MIME type of the file (eg. TXT, DOC, GIF etc)
     * @return NodeKey Represents the two part key - UUID and Version.
     * @throws SubmitFilesException
     */
    public NodeKey uploadFileToRepository(InputStream stream, String fileName) throws McApplicationException {
	ITicket ticket = getRepositoryLoginTicket();

	try {
	    NodeKey nodeKey = repositoryService.addFileItem(ticket, stream, fileName, null, null);
	    return nodeKey;
	} catch (Exception e) {
	    throw new McApplicationException("Exception occured while trying to" + " upload file into the repository"
		    + e.getMessage());
	}
    }

    public InputStream downloadFile(Long uuid, Long versionID) throws McApplicationException {
	ITicket ticket = getRepositoryLoginTicket();
	try {
	    IVersionedNode node = repositoryService.getFileItem(ticket, uuid, null);
	    return node.getFile();
	} catch (AccessDeniedException e) {
	    throw new McApplicationException("AccessDeniedException occured while trying to download file "
		    + e.getMessage());
	} catch (FileException e) {
	    throw new McApplicationException("FileException occured while trying to download file " + e.getMessage());
	} catch (ItemNotFoundException e) {
	    throw new McApplicationException("ItemNotFoundException occured while trying to download file "
		    + e.getMessage());
	}
    }

    /**
     * adds a new entry to the uploaded files table
     */
    public void persistFile(String uuid, boolean isOnlineFile, String fileName, McContent mcContent)
	    throws McApplicationException {

	McUploadedFile mcUploadedFile = new McUploadedFile(uuid, isOnlineFile, fileName, mcContent);
	mcUploadedFileDAO.saveUploadFile(mcUploadedFile);
    }

    public boolean isGroupedActivity(long toolContentID) {
	return toolService.isGroupedActivity(toolContentID);
    }

    /**
     * @return Returns the cred.
     */
    public ICredentials getCred() {
	return cred;
    }

    /**
     * @param cred
     *            The cred to set.
     */
    public void setCred(ICredentials cred) {
	this.cred = cred;
    }

    /*
     * !!! COMPLETE THIS !!! public IMcUploadedFileDAO getMcUploadedFileDAO() { return mcUploadedFileDAO; }
     * 
     * public void setMcUploadedFileDAO(IMcUploadedFileDAO mcUploadedFileDAO) { this.mcUploadedFileDAO =
     * mcUploadedFileDAO; }
     */

    /**
     * @return Returns the repositoryId.
     */
    public char[] getRepositoryId() {
	return repositoryId;
    }

    /**
     * @return Returns the repositoryUser.
     */
    public String getRepositoryUser() {
	return repositoryUser;
    }

    /**
     * @return Returns the repositoryWorkspace.
     */
    public String getRepositoryWorkspace() {
	return repositoryWorkspace;
    }

    /**
     * @return Returns the toolService.
     */
    public ILamsToolService getToolService() {
	return toolService;
    }

    /**
     * @return Returns the userManagementService.
     */
    public IUserManagementService getUserManagementService() {
	return userManagementService;
    }

    /**
     * @return Returns the mcContentDAO.
     */
    public IMcContentDAO getMcContentDAO() {
	return mcContentDAO;
    }

    /**
     * @param mcContentDAO
     *            The mcContentDAO to set.
     */
    public void setMcContentDAO(IMcContentDAO mcContentDAO) {
	this.mcContentDAO = mcContentDAO;
    }

    /**
     * @return Returns the mcOptionsContentDAO.
     */
    public IMcOptionsContentDAO getMcOptionsContentDAO() {
	return mcOptionsContentDAO;
    }

    /**
     * @param mcOptionsContentDAO
     *            The mcOptionsContentDAO to set.
     */
    public void setMcOptionsContentDAO(IMcOptionsContentDAO mcOptionsContentDAO) {
	this.mcOptionsContentDAO = mcOptionsContentDAO;
    }

    /**
     * @return Returns the mcQueContentDAO.
     */
    public IMcQueContentDAO getMcQueContentDAO() {
	return mcQueContentDAO;
    }

    /**
     * @param mcQueContentDAO
     *            The mcQueContentDAO to set.
     */
    public void setMcQueContentDAO(IMcQueContentDAO mcQueContentDAO) {
	this.mcQueContentDAO = mcQueContentDAO;
    }

    /**
     * @return Returns the mcSessionDAO.
     */
    public IMcSessionDAO getMcSessionDAO() {
	return mcSessionDAO;
    }

    /**
     * @param mcSessionDAO
     *            The mcSessionDAO to set.
     */
    public void setMcSessionDAO(IMcSessionDAO mcSessionDAO) {
	this.mcSessionDAO = mcSessionDAO;
    }

    /**
     * @return Returns the mcUserDAO.
     */
    public IMcUserDAO getMcUserDAO() {
	return mcUserDAO;
    }

    /**
     * @param mcUserDAO
     *            The mcUserDAO to set.
     */
    public void setMcUserDAO(IMcUserDAO mcUserDAO) {
	this.mcUserDAO = mcUserDAO;
    }

    /**
     * @return Returns the mcUsrAttemptDAO.
     */
    public IMcUsrAttemptDAO getMcUsrAttemptDAO() {
	return mcUsrAttemptDAO;
    }

    /**
     * @param mcUsrAttemptDAO
     *            The mcUsrAttemptDAO to set.
     */
    public void setMcUsrAttemptDAO(IMcUsrAttemptDAO mcUsrAttemptDAO) {
	this.mcUsrAttemptDAO = mcUsrAttemptDAO;
    }

    /**
     * @return Returns the repositoryService.
     */
    public IRepositoryService getRepositoryService() {
	return repositoryService;
    }

    /**
     * @param repositoryService
     *            The repositoryService to set.
     */
    public void setRepositoryService(IRepositoryService repositoryService) {
	this.repositoryService = repositoryService;
    }

    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    /**
     * @return Returns the mcUploadedFileDAO.
     */
    public IMcUploadedFileDAO getMcUploadedFileDAO() {
	return mcUploadedFileDAO;
    }

    /**
     * @param mcUploadedFileDAO
     *            The mcUploadedFileDAO to set.
     */
    public void setMcUploadedFileDAO(IMcUploadedFileDAO mcUploadedFileDAO) {
	this.mcUploadedFileDAO = mcUploadedFileDAO;
    }

    /**
     * @return Returns the mcToolContentHandler.
     */
    public IToolContentHandler getMcToolContentHandler() {
	return mcToolContentHandler;
    }

    /**
     * @param mcToolContentHandler
     *            The mcToolContentHandler to set.
     */
    public void setMcToolContentHandler(IToolContentHandler mcToolContentHandler) {
	this.mcToolContentHandler = mcToolContentHandler;
    }

    /**
     * @return Returns the learnerService.
     */
    public ILearnerService getLearnerService() {
	return learnerService;
    }

    /**
     * @param learnerService
     *            The learnerService to set.
     */
    public void setLearnerService(ILearnerService learnerService) {
	this.learnerService = learnerService;
    }

    public IExportToolContentService getExportContentService() {
	return exportContentService;
    }

    public void setExportContentService(IExportToolContentService exportContentService) {
	this.exportContentService = exportContentService;
    }
    
    public void setGradebookService(IGradebookService gradebookService) {
	this.gradebookService = gradebookService;
    }

    public MCOutputFactory getMcOutputFactory() {
	return mcOutputFactory;
    }

    public void setMcOutputFactory(MCOutputFactory mcOutputFactory) {
	this.mcOutputFactory = mcOutputFactory;
    }

    /* ===============Methods implemented from ToolContentImport102Manager =============== */

    /**
     * Import the data for a 1.0.2 Chat
     */
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues) {
	Date now = new Date();
	McContent toolContentObj = new McContent();
	toolContentObj.setContentInUse(false);
	toolContentObj.setCreatedBy(user.getUserID().longValue());
	toolContentObj.setCreationDate(now);
	toolContentObj.setDefineLater(false);
	toolContentObj.setInstructions(WebUtil.convertNewlines((String) importValues
		.get(ToolContentImport102Manager.CONTENT_BODY)));
	toolContentObj.setOfflineInstructions(null);
	toolContentObj.setOnlineInstructions(null);
	toolContentObj.setReflect(false);
	toolContentObj.setReflectionSubject(null);
	toolContentObj.setRunOffline(false);
	toolContentObj.setTitle((String) importValues.get(ToolContentImport102Manager.CONTENT_TITLE));

	toolContentObj.setContent(null);
	toolContentObj.setUpdateDate(now);
	toolContentObj.setMcContentId(toolContentId);
	toolContentObj.setQuestionsSequenced(false);
	toolContentObj.setShowMarks(false);
	toolContentObj.setRandomize(false);
	toolContentObj.setDisplayAnswers(false);
	// I can't find a use for setShowReport anywhere
	toolContentObj.setShowReport(false);

	try {
	    Boolean bool = WDDXProcessor.convertToBoolean(importValues,
		    ToolContentImport102Manager.CONTENT_Q_ALLOW_REDO);
	    toolContentObj.setRetries(bool != null ? bool : false);

	    bool = WDDXProcessor.convertToBoolean(importValues, ToolContentImport102Manager.CONTENT_Q_FEEDBACK);
	    // toolContentObj.setShowFeedback(bool!=null?bool:false);

	    Integer minPassMark = WDDXProcessor.convertToInteger(importValues,
		    ToolContentImport102Manager.CONTENT_Q_MIN_PASSMARK);
	    toolContentObj.setPassMark(minPassMark != null ? minPassMark : new Integer(0));

	    // leave as empty, no need to set them to anything.
	    // setMcUploadedFiles(Set mcSessions);
	    // setMcSessions(Set mcSessions);

	    mcContentDAO.saveMcContent(toolContentObj);

	    // set up questions
	    Vector questions = (Vector) importValues.get(ToolContentImport102Manager.CONTENT_Q_QUESTION_INFO);
	    if (questions != null) {

		Iterator iter = questions.iterator();
		while (iter.hasNext()) {
		    Hashtable questionMap = (Hashtable) iter.next();
		    create102Question(questionMap, toolContentObj);
		}

	    }

	} catch (WDDXProcessorConversionException e) {
	    McServicePOJO.logger.error("Unable to content for activity " + toolContentObj.getTitle()
		    + "properly due to a WDDXProcessorConversionException.", e);
	    throw new ToolException(
		    "Invalid import data format for activity "
			    + toolContentObj.getTitle()
			    + "- WDDX caused an exception. Some data from the design will have been lost. See log for more details.");
	}

	mcContentDAO.saveMcContent(toolContentObj);
    }

    private void create102Question(Hashtable questionMap, McContent toolContentObj)
	    throws WDDXProcessorConversionException {
	McQueContent question = new McQueContent();
	question.setDisplayOrder(WDDXProcessor.convertToInteger(questionMap,
		ToolContentImport102Manager.CONTENT_Q_ORDER));

	question.setFeedback((String) questionMap.get(ToolContentImport102Manager.CONTENT_Q_FEEDBACK));
	question.setQuestion(WebUtil.convertNewlines((String) questionMap
		.get(ToolContentImport102Manager.CONTENT_Q_QUESTION)));

	// In 1.0.2 all questions are implicitly assumed to be 1 and be of equal weight
	question.setMark(new Integer(1));

	String correctAnswer = (String) questionMap.get(ToolContentImport102Manager.CONTENT_Q_ANSWER);

	Vector candidates = (Vector) questionMap.get(ToolContentImport102Manager.CONTENT_Q_CANDIDATES);
	if (candidates != null) {
	    Iterator candIterator = candidates.iterator();
	    while (candIterator.hasNext()) {
		Hashtable candidate = (Hashtable) candIterator.next();
		String optionText = (String) candidate.get(ToolContentImport102Manager.CONTENT_Q_ANSWER);
		if (optionText != null && optionText.length() > 0) {
		    // 1.0.2 has a display order but 2.0 doesn't ToolContentImport102Manager.CONTENT_Q_ORDER
		    McOptsContent options = new McOptsContent();
		    options.setCorrectOption(correctAnswer != null && correctAnswer.equals(optionText));
		    options.setMcQueOptionText(optionText);
		    options.setMcQueContent(question);
		    question.getMcOptionsContents().add(options);
		}
	    }
	}

	toolContentObj.getMcQueContents().add(question);
	question.setMcContent(toolContentObj);
	question.setMcContentId(toolContentObj.getUid());
    }
    
    @Override
    public List<ReflectionDTO> getReflectionList(McContent mcContent, Long userID) {
	List<ReflectionDTO> reflectionsContainerDTO = new LinkedList<ReflectionDTO>();
	if (userID == null) {
	    // all users mode
	    for (McSession mcSession : (Set<McSession>)mcContent.getMcSessions()) {

		for (McQueUsr user : (Set<McQueUsr>)mcSession.getMcQueUsers()) {

		    NotebookEntry notebookEntry = this.getEntry(mcSession.getMcSessionId(),
			    CoreNotebookConstants.NOTEBOOK_TOOL, MY_SIGNATURE, new Integer(user.getQueUsrId()
				    .toString()));

		    if (notebookEntry != null) {
			ReflectionDTO reflectionDTO = new ReflectionDTO();
			reflectionDTO.setUserId(user.getQueUsrId().toString());
			reflectionDTO.setSessionId(mcSession.getMcSessionId().toString());
			reflectionDTO.setUserName(user.getFullname());
			reflectionDTO.setReflectionUid(notebookEntry.getUid().toString());
			String notebookEntryPresentable = McUtils.replaceNewLines(notebookEntry.getEntry());
			reflectionDTO.setEntry(notebookEntryPresentable);
			reflectionsContainerDTO.add(reflectionDTO);
		    }
		}
	    }
	} else {
	    // single user mode
	    for (Iterator sessionIter = mcContent.getMcSessions().iterator(); sessionIter.hasNext();) {
		McSession mcSession = (McSession) sessionIter.next();
		for (Iterator userIter = mcSession.getMcQueUsers().iterator(); userIter.hasNext();) {
		    McQueUsr user = (McQueUsr) userIter.next();
		    if (user.getQueUsrId().equals(userID)) {
			NotebookEntry notebookEntry = this.getEntry(mcSession.getMcSessionId(),
				CoreNotebookConstants.NOTEBOOK_TOOL, MY_SIGNATURE, new Integer(user.getQueUsrId()
					.toString()));

			if (notebookEntry != null) {
			    ReflectionDTO reflectionDTO = new ReflectionDTO();
			    reflectionDTO.setUserId(user.getQueUsrId().toString());
			    reflectionDTO.setSessionId(mcSession.getMcSessionId().toString());
			    reflectionDTO.setUserName(user.getFullname());
			    reflectionDTO.setReflectionUid(notebookEntry.getUid().toString());
			    String notebookEntryPresentable = McUtils.replaceNewLines(notebookEntry.getEntry());
			    reflectionDTO.setEntry(notebookEntryPresentable);
			    reflectionsContainerDTO.add(reflectionDTO);
			}
		    }
		}
	    }
	}

	return reflectionsContainerDTO;
    }

    /** Set the description, throws away the title value as this is not supported in 2.0 */
    public void setReflectiveData(Long toolContentId, String title, String description) throws ToolException,
	    DataMissingException {

	McContent toolContentObj = null;
	if (toolContentId != null) {
	    toolContentObj = retrieveMc(toolContentId);
	}
	if (toolContentObj == null) {
	    throw new DataMissingException("Unable to set reflective data titled " + title
		    + " on activity toolContentId " + toolContentId + " as the tool content does not exist.");
	}

	toolContentObj.setReflect(true);
	toolContentObj.setReflectionSubject(description);
    }

    public List<McUploadedFile> retrieveMcUploadedFiles(McContent mc) throws McApplicationException {
	try {
	    return mcUploadedFileDAO.retrieveMcUploadedFiles(mc);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is loading mc uploaded files: "
		    + e.getMessage(), e);
	}
    }

    /**
     * adds a new entry to the uploaded files table
     */
    public void persistFile(McContent content, McUploadedFile file) throws McApplicationException {

	content.getMcAttachments().add(file);
	file.setMcContent(content);
	mcContentDAO.saveOrUpdateMc(content);
    }

    /**
     * removes an entry from the uploaded files table
     */
    public void removeFile(Long submissionId) throws McApplicationException {
	mcUploadedFileDAO.removeUploadFile(submissionId);
    }

    public Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry) {
	return coreNotebookService.createNotebookEntry(id, idType, signature, userID, "", entry);
    }

    public NotebookEntry getEntry(Long id, Integer idType, String signature, Integer userID) {

	List<NotebookEntry> list = coreNotebookService.getEntry(id, idType, signature, userID);
	if (list == null || list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    public void updateEntry(NotebookEntry notebookEntry) {
	coreNotebookService.updateEntry(notebookEntry);
    }

    /**
     * @return Returns the coreNotebookService.
     */
    public ICoreNotebookService getCoreNotebookService() {
	return coreNotebookService;
    }

    /**
     * @param coreNotebookService
     *            The coreNotebookService to set.
     */
    public void setCoreNotebookService(ICoreNotebookService coreNotebookService) {
	this.coreNotebookService = coreNotebookService;
    }

    /**
     * @return Returns the auditService.
     */
    public IAuditService getAuditService() {
	return auditService;
    }

    /**
     * @param auditService
     *            The auditService to set.
     */
    public void setAuditService(IAuditService auditService) {
	this.auditService = auditService;
    }

    /**
     * @return Returns the MessageService.
     */
    public MessageService getMessageService() {
	return messageService;
    }

    /**
     * @param messageService
     *            The MessageService to set.
     */
    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getMcOutputFactory().getSupportedDefinitionClasses(definitionType);
    }

}