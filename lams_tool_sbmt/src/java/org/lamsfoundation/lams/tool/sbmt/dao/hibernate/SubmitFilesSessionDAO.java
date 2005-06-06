/*
 * Created on May 30, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.sbmt.dao.hibernate;

import org.lamsfoundation.lams.learningdesign.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesSession;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesSessionDAO;

/**
 * @author Manpreet Minhas
 */
public class SubmitFilesSessionDAO extends BaseDAO implements
		ISubmitFilesSessionDAO {

	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesSessionDAO#getSessionByID(java.lang.Long)
	 */
	public SubmitFilesSession getSessionByID(Long sessionID) {		
		 return (SubmitFilesSession) super.find(SubmitFilesSession.class,sessionID);
	}

}
