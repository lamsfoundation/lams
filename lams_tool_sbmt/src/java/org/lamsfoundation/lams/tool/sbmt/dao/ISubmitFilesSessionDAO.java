/*
 * Created on May 30, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.sbmt.dao;

import org.lamsfoundation.lams.learningdesign.dao.IBaseDAO;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesSession;

/**
 * @author Manpreet Minhas
 */
public interface ISubmitFilesSessionDAO extends IBaseDAO {
	
	/**
	 * Returns the session record corresponding to the
	 * given <code>sessionID</code>
	 * 
	 * @param sessionID The <code>session_id</code> to be looked up
	 * @return SubmitFilesSession The required populated object
	 */
	public SubmitFilesSession getSessionByID(Long sessionID);

}
