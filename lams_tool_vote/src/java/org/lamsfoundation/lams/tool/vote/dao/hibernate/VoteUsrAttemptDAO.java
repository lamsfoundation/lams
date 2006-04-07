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

package org.lamsfoundation.lams.tool.vote.dao.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.lamsfoundation.lams.tool.vote.dao.IVoteUsrAttemptDAO;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUsrAttempt;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author Ozgur Demirtas
 * <p>Hibernate implementation for database access to VoteUsrAttemptDAO for the voting tool.</p>
 */
public class VoteUsrAttemptDAO extends HibernateDaoSupport implements IVoteUsrAttemptDAO {
	 	static Logger logger = Logger.getLogger(VoteUsrAttemptDAO.class.getName());
	 	
	 	private static final String LOAD_HIGHEST_MARK_BY_USER_ID = "from mcUsrAttempt in class VoteUsrAttempt where mcUsrAttempt.queUsrId=:queUsrId";
	 	
	 	private static final String LOAD_HIGHEST_ATTEMPT_ORDER_BY_USER_ID = "from mcUsrAttempt in class VoteUsrAttempt where mcUsrAttempt.queUsrId=:queUsrId";
	 	
	 	private static final String LOAD_ATTEMPT_FOR_QUE_CONTENT = "from mcUsrAttempt in class VoteUsrAttempt where mcUsrAttempt.queUsrId=:queUsrId and mcUsrAttempt.mcQueContentId=:mcQueContentId";
	 	
	 	private static final String LOAD_ATTEMPT_FOR_USER		 = "from mcUsrAttempt in class VoteUsrAttempt where mcUsrAttempt.queUsrId=:queUsrId";
	 	
	 	private static final String LOAD_ATTEMPT_FOR_USER_AND_QUESTION_CONTENT	 = "from mcUsrAttempt in class VoteUsrAttempt where mcUsrAttempt.queUsrId=:queUsrId and mcUsrAttempt.mcQueContentId=:mcQueContentId";
	 	
	 	
	 	public VoteUsrAttempt getMcUserAttemptByUID(Long uid)
		{
			 return (VoteUsrAttempt) this.getHibernateTemplate()
	         .get(VoteUsrAttempt.class, uid);
		}
		
		public void saveMcUsrAttempt(VoteUsrAttempt mcUsrAttempt)
	    {
	    	this.getHibernateTemplate().save(mcUsrAttempt);
	    }
	    
		
		public List getAttemptsForUser(final Long queUsrId)
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_ATTEMPT_FOR_USER)
			.setLong("queUsrId", queUsrId.longValue())
			.list();
			return list;
	    }
		
		public List getAttemptsForUserAndQuestionContent(final Long queUsrId, final Long mcQueContentId)
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_ATTEMPT_FOR_USER_AND_QUESTION_CONTENT)
			.setLong("queUsrId", queUsrId.longValue())
			.setLong("mcQueContentId", mcQueContentId.longValue())
			.list();
	        
			return list;
	    }
		
		
		public List getAttemptForQueContent(final Long queUsrId, final Long mcQueContentId)
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_ATTEMPT_FOR_QUE_CONTENT)
			.setLong("queUsrId", queUsrId.longValue())
			.setLong("mcQueContentId", mcQueContentId.longValue())				
			.list();
			return list;
	    }
		
		
		
		public void updateMcUsrAttempt(VoteUsrAttempt mcUsrAttempt)
	    {
	    	this.getHibernateTemplate().update(mcUsrAttempt);
	    }
		
		public void removeMcUsrAttemptByUID(Long uid)
	    {
			VoteUsrAttempt mca = (VoteUsrAttempt)getHibernateTemplate().get(VoteUsrAttempt.class, uid);
			this.getSession().setFlushMode(FlushMode.AUTO);
	    	this.getHibernateTemplate().delete(mca);
	    }
		
		
		public void removeMcUsrAttempt(VoteUsrAttempt mcUsrAttempt)
	    {
			this.getSession().setFlushMode(FlushMode.AUTO);
	        this.getHibernateTemplate().delete(mcUsrAttempt);
	    }
	 	
} 