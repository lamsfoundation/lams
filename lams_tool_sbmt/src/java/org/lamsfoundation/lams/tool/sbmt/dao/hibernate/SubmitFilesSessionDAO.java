/*
 * Created on May 30, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.sbmt.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.learningdesign.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesSession;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesSessionDAO;

/**
 * @author Manpreet Minhas
 */
public class SubmitFilesSessionDAO extends BaseDAO implements
		ISubmitFilesSessionDAO {

    private static final String FIND_LEARNER_BY_CONTENT_ID = 
        " from SubmitFilesSession session " +
        " where session.content.contentID = :contentID";

    
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesSessionDAO#getSessionByID(java.lang.Long)
	 */
	public SubmitFilesSession getSessionByID(Long sessionID) {		
		 return (SubmitFilesSession) super.find(SubmitFilesSession.class,sessionID);
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesSessionDAO#createSession(org.lamsfoundation.lams.tool.sbmt.SubmitFilesSession)
	 */
	public void createSession(SubmitFilesSession submitSession) {
		 this.getHibernateTemplate().save(submitSession);
		 this.getHibernateTemplate().flush();
	}
    
    public List getSubmitFilesSessionByContentID(Long contentID){
        if ( contentID != null ) {
            return this.getSession().createQuery(FIND_LEARNER_BY_CONTENT_ID)
                .setLong("contentID", contentID.longValue())
                .list();
        }
        return null;
    }

}
