/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
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
 * ****************************************************************
 */

/* $$Id$$ */	

package org.lamsfoundation.lams.tool.sbmt.dao.hibernate;

import java.util.List;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.sbmt.SubmissionDetails;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesSession;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmissionDetailsDAO;
import org.springframework.orm.hibernate3.HibernateCallback;

/**
 * @author Manpreet Minhas
 */
public class SubmissionDetailsDAO extends BaseDAO implements
		ISubmissionDetailsDAO {
	
	private static final String TABLENAME = "tl_lasbmt11_submission_details";
	
	private static final String FIND_BY_SESSION = "from " + TABLENAME +
													" in class " + SubmissionDetails.class.getName() +
													" where session_id=?";
	
	private static final String FIND_DISTINCT_USER = " select distinct learner.userID from SubmissionDetails details " +
													 ", Learner learner " +
													 " where details.submitFileSession =:sessionID " +
													 " and details.learner = learner.learnerID";


	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.sbmt.dao.ISubmissionDetailsDAO#getSubmissionDetailsByID(java.lang.Long)
	 */
	public SubmissionDetails getSubmissionDetailsByID(Long submissionID) {
		return (SubmissionDetails) this.getHibernateTemplate().
								   get(SubmissionDetails.class, submissionID);
	}
	
	public List getUsersForSession(final Long sessionID){			
		return (List) this.getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException{
				return session.createQuery(FIND_DISTINCT_USER)
							  .setLong("sessionID",sessionID.longValue())
							  .list();
			}
		});		
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.sbmt.dao.ISubmissionDetailsDAO#saveOrUpdate(org.lamsfoundation.lams.tool.sbmt.SubmitFilesSession)
	 */
	public void saveOrUpdate(SubmitFilesSession session) {
		
		this.getSession().setFlushMode(FlushMode.AUTO);
		this.getHibernateTemplate().saveOrUpdate(session);
		this.getHibernateTemplate().flush();
		this.getHibernateTemplate().clear();
		
	}
	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.sbmt.dao.ISubmissionDetailsDAO#getSubmissionDetailsBySession(java.lang.Long)
	 */
	public List getSubmissionDetailsBySession(Long sessionID) {
		if ( sessionID != null ) {
			return this.getSession().createQuery(FIND_BY_SESSION)
				.setLong(0, sessionID.longValue())
				.list();
		}
		return null;
	}
}
