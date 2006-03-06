/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/
package org.lamsfoundation.lams.tool.qa.service;

import java.io.InputStream;
import java.util.List;

import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.tool.BasicToolVO;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.qa.QaApplicationException;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.QaQueUsr;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.QaUsrResp;
import org.lamsfoundation.lams.tool.qa.QaUploadedFile;
import org.lamsfoundation.lams.usermanagement.User;



/**
 * This interface define the contract that all Q/A service provider must
 * follow.
 * 
 * @author Ozgur Demirtas
 */
public interface IQaService 
{
	/**
     * Return the qa object according to the requested content id.
     * @param toolContentId the tool content id
     * @return the qa object
     */
    
	public QaContent retrieveQa(long toolContentId) throws QaApplicationException;;
	
	
	/**
     * Return the qa object according to the requested content id.
     * @param toolContentId the tool content id
     * @return the qa object or null
     */
    
	public QaContent loadQa(long toolContentId) throws QaApplicationException;;

	public QaContent getQaContentByUID(Long uid) throws QaApplicationException;;
	
	public int getTotalNumberOfUsers() throws QaApplicationException;;
	
	public int countSessionComplete() throws QaApplicationException;;
	
	
	/**
     * Return the question content object according to the requested question content id.
     * @param qaQueContentId qa question content id
     * @return the qa question object
     */
	public QaQueContent retrieveQaQue(long qaQueContentId) throws QaApplicationException;;
	
	public QaQueUsr loadQaQueUsr(Long userId) throws QaApplicationException;;
	
	public List getAttemptsForUserAndQuestionContent(final Long queUsrId, final Long qaQueContentId) throws QaApplicationException;;
		
	public void createQaQue(QaQueContent qaQueContent) throws QaApplicationException;;
	
	public void createQaUsrResp(QaUsrResp qaUsrResp) throws QaApplicationException;;
	
	public QaUsrResp retrieveQaUsrResp(long responseId) throws QaApplicationException;;
	
	public void updateQaUsrResp(QaUsrResp qaUsrResp) throws QaApplicationException;;
	
	
	/**
     * Return the qa session object according to the requested session id.
     * @param qaSessionId qa session id
     * @return the qa session object
     */
	public QaSession retrieveQaSession(long qaSessionId) throws QaApplicationException;;
	
	public QaSession retrieveQaSessionOrNullById(long qaSessionId) throws QaApplicationException;
		
	public void createQaSession(QaSession qaSession) throws QaApplicationException;;
	
	public List getSessionNamesFromContent(QaContent qaContent) throws QaApplicationException;;
	
	public String getSessionNameById(long qaSessionId) throws QaApplicationException;;
	
	public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException;
	
	public List getSessionsFromContent(QaContent qaContent) throws QaApplicationException;;
	
	public void createQaQueUsr(QaQueUsr qaQueUsr) throws QaApplicationException;;
	
	public void updateQaSession(QaSession qaSession) throws QaApplicationException;;
	
	
	/**
     * Return the qa que user object according to the requested usr id.
     * @param qaQaUsrId qa usr id
     * @return the qa que usr object
     */
	public QaQueUsr retrieveQaQueUsr(long qaQaUsrId) throws QaApplicationException;;
	
	public QaQueUsr getQaQueUsrById(long qaQueUsrId) throws QaApplicationException;;
	
	public void updateQa(QaContent qa) throws QaApplicationException;;
	
	public void createQa(QaContent qa) throws QaApplicationException;;
	
	public void deleteQa(QaContent qa) throws QaApplicationException;;
    
	public void deleteQaSession(QaSession QaSession) throws QaApplicationException;
	
	public void deleteUsrRespByQueId(Long qaQueId) throws QaApplicationException;;
	
	public void deleteQaById(Long qaId) throws QaApplicationException;;
	
	public void deleteQaQueUsr(QaQueUsr qaQueUsr) throws QaApplicationException;;
	
	public void removeUserResponse(QaUsrResp resp) throws QaApplicationException;;
	
	public List getAllQuestionEntries(final Long uid) throws QaApplicationException;
	
    public User getCurrentUserData(String username) throws QaApplicationException;;
    
    public List getUserBySessionOnly(final QaSession qaSession) throws QaApplicationException;;
    
    /**
     * copyToolContent(Long fromContentId, Long toContentId)
     * return void
     * @param fromContentId
     * @param toContentId
     */
    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException;
    
    public void setAsDefineLater(Long toolContentId) throws DataMissingException, ToolException;
    
    public void unsetAsDefineLater(Long toolContentId) throws QaApplicationException;;
    
    public void setAsRunOffline(Long toolContentId) throws DataMissingException, ToolException;
    
    public void setAsForceComplete(Long userId) throws QaApplicationException;;
    
    public void setAsForceCompleteSession(Long toolSessionId) throws QaApplicationException;;
    
    public boolean studentActivityOccurred(QaContent qa) throws QaApplicationException;;
    
    public boolean studentActivityOccurredGlobal(QaContent qaContent) throws QaApplicationException;;
    
    
    /**
     * removeToolContent(Long toolContentId) 
     * return void
     * @param toolContentId
     */
    public void removeToolContent(Long toolContentId) throws QaApplicationException;;
    
    
    /**
     * createToolSession(Long toolSessionId,String toolSessionName, Long toolContentId) 
     * 
     * It is also defined here since in development we want to be able call it directly from the web-layer 
     * instead of it being called by the container.
     * @param toolSessionId
     * @param toolContentId
     */
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException;
    
    /**
     * leaveToolSession(Long toolSessionId, Long learnerId) 
     * 
     * It is also defined here since in development we want to be able call it directly from our web-layer 
     * instead of it being called by the container.
     * @param toolSessionId
     * @param toolContentId
     */
    public String leaveToolSession(Long toolSessionId,Long learnerId) throws DataMissingException, ToolException;
    
    public BasicToolVO getToolBySignature(String toolSignature) throws QaApplicationException;;
    
    public long getToolDefaultContentIdBySignature(String toolSignature) throws QaApplicationException;;
    
    public int countSessionUser(QaSession qaSession) throws QaApplicationException;;
    
    public List getToolSessionsForContent(QaContent qa) throws QaApplicationException;;
    
    public QaQueContent getToolDefaultQuestionContent(long contentId) throws QaApplicationException;;
    
    public void configureContentRepository() throws QaApplicationException;
    
    public ITicket getRepositoryLoginTicket() throws QaApplicationException;
    
    public void deleteFromRepository(Long uuid, Long versionID) throws QaApplicationException;

    public InputStream downloadFile(Long uuid, Long versionID)throws QaApplicationException;
	
    public void persistFile(String uuid, boolean isOnlineFile, String fileName, QaContent qaContent) throws QaApplicationException;
    
    public void persistFile(QaContent content, QaUploadedFile file) throws QaApplicationException;
    
    public void removeFile(Long submissionId) throws QaApplicationException;
    
    public List retrieveQaUploadedFiles(QaContent qa) throws QaApplicationException;
    
    public void cleanUploadedFilesMetaData() throws QaApplicationException;
    
    public List retrieveQaQueContentsByToolContentId(long toolContentId);
}

