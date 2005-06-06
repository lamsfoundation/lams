/*
 * Created on May 24, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.sbmt.dao.hibernate;

import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;

import org.lamsfoundation.lams.learningdesign.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.sbmt.SubmissionDetails;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmissionDetailsDAO;
import org.springframework.orm.hibernate.HibernateCallback;

/**
 * @author Manpreet Minhas
 */
public class SubmissionDetailsDAO extends BaseDAO implements
		ISubmissionDetailsDAO {
	
	private static final String TABLENAME = "tl_lasbmt11_submission_details";
	private static final String FIND_BY_CONTENT_ID = "from " + TABLENAME +
													 " in class " + SubmissionDetails.class.getName() +
													 " where content_id=? ORDER BY user_id";
	private static final String FIND_FOR_USER_BY_CONTENT = "from " + TABLENAME +
															" in class " + SubmissionDetails.class.getName() +
															" where user_id=? AND content_id=?";
	private static final String FIND_DISTINCT_USER = " select distinct details.userID from SubmissionDetails details " +
													 " where details.content =:contentID";


	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.sbmt.dao.ISubmissionDetailsDAO#getSubmissionDetailsByID(java.lang.Long)
	 */
	public SubmissionDetails getSubmissionDetailsByID(Long submissionID) {
		return (SubmissionDetails) this.getHibernateTemplate().
								   get(SubmissionDetails.class, submissionID);
	}
	
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.sbmt.dao.ISubmissionDetailsDAO#getSubmissionDetailsByContentID(java.lang.Long)
	 */
	public List getSubmissionDetailsByContentID(Long contentID){
		return this.getHibernateTemplate().find(FIND_BY_CONTENT_ID,contentID);
	}
	
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.sbmt.dao.ISubmissionDetailsDAO#getSubmissionDetailsForUserByContent(java.lang.Long, java.lang.Long)
	 */
	public List getSubmissionDetailsForUserByContent(Long userID,Long contentID){
		List list = this.getHibernateTemplate().find(FIND_FOR_USER_BY_CONTENT, 
													 new Object[]{userID, contentID},
													 new Type[]{Hibernate.LONG,Hibernate.LONG});
		return list;
	}
	
	public List getUsersForContent(final Long contentID){			
		return (List) this.getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException{
				return session.createQuery(FIND_DISTINCT_USER)
							  .setLong("contentID",contentID.longValue())
							  .list();
			}
		});		
	}
}
