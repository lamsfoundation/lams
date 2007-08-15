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
package org.lamsfoundation.lams.tool.mc.dao.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.lamsfoundation.lams.tool.mc.dao.IMcUsrAttemptDAO;
import org.lamsfoundation.lams.tool.mc.pojos.McUsrAttempt;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author Ozgur Demirtas
 * <p>Hibernate implementation for database access to McUsrAttempt for the mc tool.</p>
 * <p>Be very careful about the queUserId and the McQueUser.uid fields. McQueUser.queUsrId is the core's user id for this user and McQueUser.uid 
 * is the primary key for McQueUser. McUsrAttempt.queUsrId = McQueUser.uid, not McUsrAttempt.queUsrId = McQueUser.queUsrId
 * as you would expect. A new McQueUser object is created for each new tool session, so if the McQueUser.uid is supplied, then this
 * denotes one user in a particular tool session, but if McQueUser.queUsrId is supplied, then this is just the user, not the session and the session
 * must also be checked.
 */
public class McUsrAttemptDAO extends HibernateDaoSupport implements IMcUsrAttemptDAO {
	 	static Logger logger = Logger.getLogger(McUsrAttemptDAO.class.getName());
	 	
	 	private static final String LOAD_ATTEMPT_BY_USER_SESSION = "from mcUsrAttempt in class McUsrAttempt where mcUsrAttempt.queUsrId=:queUsrUid";
	 		 	
	 	private static final String LOAD_ATTEMPT_BY_ATTEMPT_ORDER = "from mcUsrAttempt in class McUsrAttempt where mcUsrAttempt.queUsrId=:queUsrUid"
	 		+" and mcUsrAttempt.mcQueContentId=:mcQueContentId and mcUsrAttempt.attemptOrder=:attemptOrder"
	 		+" order by mcUsrAttempt.attemptOrder, mcUsrAttempt.mcOptionsContent.uid";
	 	
	 	private static final String LOAD_LAST_ATTEMPT_BY_ATTEMPT_ORDER = "from mcUsrAttempt in class McUsrAttempt where mcUsrAttempt.mcQueUsr.uid=:queUsrUid"
	 		+" and mcUsrAttempt.mcQueContentId=:mcQueContentId and mcUsrAttempt.attemptOrder=mcUsrAttempt.mcQueUsr.lastAttemptOrder" 
	 		+" order by mcUsrAttempt.mcOptionsContent.uid";

	 	private static final String LOAD_ATTEMPT_FOR_QUESTION_CONTENT	 = "from mcUsrAttempt in class McUsrAttempt "
	 		+" where mcUsrAttempt.mcQueContentId=:mcQueContentId and mcUsrAttempt.queUsrId=:queUsrUid"
	 		+" order by mcUsrAttempt.attemptOrder";

	 	private static final String LOAD_LAST_ATTEMPTS = "from mcUsrAttempt in class McUsrAttempt where mcUsrAttempt.mcQueUsr.uid=:queUsrUid"
	 		+" and mcUsrAttempt.attemptOrder=mcUsrAttempt.mcQueUsr.lastAttemptOrder"
 			+" order by mcUsrAttempt.mcQueContentId, mcUsrAttempt.mcOptionsContent.uid";
	 	
	 	public McUsrAttempt getMcUserAttemptByUID(Long uid)
		{
			 return (McUsrAttempt) this.getHibernateTemplate()
	         .get(McUsrAttempt.class, uid);
		}
		
		public void saveMcUsrAttempt(McUsrAttempt mcUsrAttempt)
	    {
	    	this.getHibernateTemplate().save(mcUsrAttempt);
	    }
	    
		public List getUserAttemptsForSession(Long queUsrUid)
	    {
			List list = getSession().createQuery(LOAD_ATTEMPT_BY_USER_SESSION)
				.setLong("queUsrUid", queUsrUid.longValue())
				.list();
			
			return list;
	    }
		
		public List getLatestAttemptsForAUser(final Long queUserUid) {
			return (List) getSession().createQuery(LOAD_LAST_ATTEMPTS)
				.setLong("queUsrUid", queUserUid.longValue())
				.list();
		}

		// should be able to get rid of this one by rewriting export portfolio
		@SuppressWarnings("unchecked")
		public List<McUsrAttempt> getLatestAttemptsForAUserForOneQuestionContent(final Long queUsrUid, final Long mcQueContentId) {
			return (List<McUsrAttempt>) getSession().createQuery(LOAD_LAST_ATTEMPT_BY_ATTEMPT_ORDER)
				.setLong("queUsrUid", queUsrUid.longValue())
				.setLong("mcQueContentId", mcQueContentId.longValue())
				.list();
		}

		@SuppressWarnings("unchecked")
		public List<McUsrAttempt> getAllAttemptsForAUserForOneQuestionContentOrderByAttempt(final Long queUsrUid,  final Long mcQueContentId)
	    {
	        return (List<McUsrAttempt>)getSession().createQuery(LOAD_ATTEMPT_FOR_QUESTION_CONTENT)
			.setLong("mcQueContentId", mcQueContentId.longValue())
			.setLong("queUsrUid", queUsrUid.longValue())
			.list();
	    }

		public List getAttemptByAttemptOrder(final Long queUsrUid, final Long mcQueContentId, final Integer attemptOrder)
	    {
	        List list = getSession().createQuery(LOAD_ATTEMPT_BY_ATTEMPT_ORDER)
			.setLong("queUsrUid", queUsrUid.longValue())
			.setLong("mcQueContentId", mcQueContentId.longValue())
			.setInteger("attemptOrder", attemptOrder.intValue())
			.list();
			return list;
	    }
		
		public void updateMcUsrAttempt(McUsrAttempt mcUsrAttempt)
	    {
		    this.getSession().setFlushMode(FlushMode.AUTO);
	    	this.getHibernateTemplate().update(mcUsrAttempt);
	    }
		
		public void removeMcUsrAttemptByUID(Long uid)
	    {
			McUsrAttempt mca = (McUsrAttempt)getHibernateTemplate().get(McUsrAttempt.class, uid);
			this.getSession().setFlushMode(FlushMode.AUTO);
	    	this.getHibernateTemplate().delete(mca);
	    }
		
		
		public void removeMcUsrAttempt(McUsrAttempt mcUsrAttempt)
	    {
			this.getSession().setFlushMode(FlushMode.AUTO);
	        this.getHibernateTemplate().delete(mcUsrAttempt);
	    }
		
} 