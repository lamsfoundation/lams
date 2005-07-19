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
	
	/**
	 * Returns a list of records coresponding to the given contentID.
	 * 
	 * @param contentID The content_id to be looked up
	 * @return List The list of required details
	 */
	public List getSubmissionDetailsByContentID(Long contentID);
	
	/**
	 * This method returns a list of files that were uploaded by the
	 * given <code>User<code> for given <code>contentID</code>.
	 * 
	 * @param userID The <code>user_id</code> of the <code>User</code>
	 * @param contentID The <code>content_id</code> to be looked up
	 * @return List The list of required objects.
	 */
	public List getSubmissionDetailsForUserBySession(Long userID,Long contentID);
	
	public List getUsersForSession(Long contentID);

	/**
	 * Save or update the given <code>SubmitFilesSession</code> value.
	 * @param session
	 */
	public void saveOrUpdate(SubmitFilesSession session);
}
