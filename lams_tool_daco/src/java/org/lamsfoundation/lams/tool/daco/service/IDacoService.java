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
package org.lamsfoundation.lams.tool.daco.service;

import java.util.Collection;
import java.util.List;

import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.daco.dto.MonitoringSummarySessionDTO;
import org.lamsfoundation.lams.tool.daco.dto.QuestionSummaryDTO;
import org.lamsfoundation.lams.tool.daco.model.Daco;
import org.lamsfoundation.lams.tool.daco.model.DacoAnswer;
import org.lamsfoundation.lams.tool.daco.model.DacoAttachment;
import org.lamsfoundation.lams.tool.daco.model.DacoQuestion;
import org.lamsfoundation.lams.tool.daco.model.DacoSession;
import org.lamsfoundation.lams.tool.daco.model.DacoUser;

/**
 * @author Marcin Cieslak
 * 
 * Interface that defines the contract that all Data Collection service provider must follow.
 */
public interface IDacoService {

	/**
	 * Get file <code>IVersiondNode</code> by given package id and path.
	 * 
	 * @param packageId
	 * @param relPathString
	 * @return
	 * @throws DacoApplicationException
	 */
	IVersionedNode getFileNode(Long packageId, String relPathString) throws DacoApplicationException;

	/**
	 * Get <code>Daco</code> by toolContentID.
	 * 
	 * @param contentId
	 * @return
	 */
	Daco getDacoByContentId(Long contentId);

	/**
	 * Get a cloned copy of tool default tool content (Daco) and assign the toolContentId of that copy as the given
	 * <code>contentId</code>
	 * 
	 * @param contentId
	 * @return
	 * @throws DacoApplicationException
	 */
	Daco getDefaultContent(Long contentId) throws DacoApplicationException;

	/**
	 * Upload instruciton file into repository.
	 * 
	 * @param file
	 * @param type
	 * @return
	 * @throws UploadDacoFileException
	 */
	DacoAttachment uploadInstructionFile(FormFile file, String type) throws UploadDacoFileException;

	/**
	 * Upload daco answer file to repository
	 * 
	 * @param answer
	 * @param file
	 * @throws UploadDacoFileException
	 */
	void uploadDacoAnswerFile(DacoAnswer answer, FormFile file) throws UploadDacoFileException;

	// ********** for user methods *************
	/**
	 * Create a new user in database.
	 */
	void createUser(DacoUser dacoUser);

	/**
	 * Get user by given userID and toolContentID.
	 * 
	 * @param long1
	 * @return
	 */
	DacoUser getUserByUserIdAndContentId(Long userID, Long contentId);

	/**
	 * Get user by sessionID and UserID
	 * 
	 * @param long1
	 * @param sessionId
	 * @return
	 */
	DacoUser getUserByUserIdAndSessionId(Long long1, Long sessionId);

	// ********** Repository methods ***********************
	/**
	 * Delete file from repository.
	 */
	void deleteFromRepository(Long fileUuid, Long fileVersionId) throws DacoApplicationException;

	/**
	 * Save or update daco into database.
	 * 
	 * @param Daco
	 */
	void saveOrUpdateDaco(Daco Daco);

	void saveOrUpdateAnswer(DacoAnswer answer);

	/**
	 * Delete reource attachment(i.e., offline/online instruction file) from database. This method does not delete the file from
	 * repository.
	 * 
	 * @param attachmentUid
	 */
	void deleteDacoAttachment(Long attachmentUid);

	/**
	 * Delete question from database.
	 * 
	 * @param uid
	 */
	void deleteDacoQuestion(Long uid);

	void deleteDacoAnswer(Long uid);

	void deleteDacoRecord(List<DacoAnswer> record);

	/**
	 * Return all reource questions within the given toolSessionID.
	 * @param sessionId
	 * 
	 * @return
	 */
	List<List<DacoAnswer>> getDacoAnswersByUserUid(Long userUid);

	/**
	 * Get daco which is relative with the special toolSession.
	 * 
	 * @param sessionId
	 * @return
	 */
	Daco getDacoBySessionId(Long sessionId);

	/**
	 * Get daco toolSession by toolSessionId
	 * 
	 * @param sessionId
	 * @return
	 */
	DacoSession getSessionBySessionId(Long sessionId);

	/**
	 * Save or update daco session.
	 * 
	 * @param resSession
	 */
	void saveOrUpdateDacoSession(DacoSession resSession);

	/**
	 * If success return next activity's url, otherwise return null.
	 * 
	 * @param toolSessionId
	 * @param userId
	 * @return
	 */
	String finishToolSession(Long toolSessionId, Long userId) throws DacoApplicationException;

	DacoQuestion getDacoQuestionByUid(Long questionUid);

	/**
	 * Create refection entry into notebook tool.
	 * 
	 * @param sessionId
	 * @param notebook_tool
	 * @param tool_signature
	 * @param userId
	 * @param entryText
	 */
	public Long createNotebookEntry(Long sessionId, Integer notebookToolType, String toolSignature, Integer userId,
			String entryText);

	/**
	 * Get reflection entry from notebook tool.
	 * 
	 * @param sessionId
	 * @param idType
	 * @param signature
	 * @param userID
	 * @return
	 */
	public NotebookEntry getEntry(Long sessionId, Integer idType, String signature, Integer userID);

	/**
	 * @param notebookEntry
	 */
	public void updateEntry(NotebookEntry notebookEntry);

	/**
	 * Get user by UID
	 * 
	 * @param uid
	 * @return
	 */
	DacoUser getUser(Long uid);

	public String getLocalisedMessage(String key, Object[] args);

	public List<QuestionSummaryDTO> getQuestionSummaries(Long userUid);

	public void releaseDacoFromCache(Daco daco);

	void releaseAnswersFromCache(Collection<DacoAnswer> answers);

	Integer getGroupRecordCount(Long sessionId);

	Integer getGroupRecordCount(MonitoringSummarySessionDTO monitoringSummary);

	List<MonitoringSummarySessionDTO> getMonitoringSummary(Long contentId, Long userUid);
}
