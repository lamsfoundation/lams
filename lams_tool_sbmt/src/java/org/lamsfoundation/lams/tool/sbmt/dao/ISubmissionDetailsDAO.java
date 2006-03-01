/*
 * Created on May 24, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.sbmt.dao;

import java.util.List;

import org.lamsfoundation.lams.learningdesign.dao.IBaseDAO;
import org.lamsfoundation.lams.tool.sbmt.SubmissionDetails;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesSession;

/**
 * @author Manpreet Minhas
 */
public interface ISubmissionDetailsDAO extends IBaseDAO {
	
	/**
	 * Returns a <code>SubmissionDetails</code> object 
	 * corresponding to given <code>submissionID</code>
	 * 
	 * @param submissionID The submission_id to be looked up
	 * @return SubmissionDetails The required populated object
	 */
	public SubmissionDetails getSubmissionDetailsByID(Long submissionID);
	
	public List getUsersForSession(Long contentID);

	/**
	 * Save or update the given <code>SubmitFilesSession</code> value.
	 * @param session
	 */
	public void saveOrUpdate(SubmitFilesSession session);

	/**
	 * @param sessionID
	 * @return
	 */
	public List getSubmissionDetailsBySession(Long sessionID);
}
