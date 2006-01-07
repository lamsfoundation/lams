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
 */
public class McUsrAttemptDAO extends HibernateDaoSupport implements IMcUsrAttemptDAO {
	 	static Logger logger = Logger.getLogger(McUsrAttemptDAO.class.getName());
	 	
	 	private static final String LOAD_HIGHEST_MARK_BY_USER_ID = "from mcUsrAttempt in class McUsrAttempt where mcUsrAttempt.queUsrId=:queUsrId";
	 	
	 	private static final String LOAD_HIGHEST_ATTEMPT_ORDER_BY_USER_ID = "from mcUsrAttempt in class McUsrAttempt where mcUsrAttempt.queUsrId=:queUsrId";
	 	
	 	private static final String LOAD_ATTEMPT_FOR_QUE_CONTENT = "from mcUsrAttempt in class McUsrAttempt where mcUsrAttempt.queUsrId=:queUsrId and mcUsrAttempt.mcQueContentId=:mcQueContentId";
	 	
	 	private static final String LOAD_ATTEMPT_FOR_USER		 = "from mcUsrAttempt in class McUsrAttempt where mcUsrAttempt.queUsrId=:queUsrId";
	 	
	 	private static final String LOAD_ATTEMPT_FOR_USER_AND_QUESTION_CONTENT	 = "from mcUsrAttempt in class McUsrAttempt where mcUsrAttempt.queUsrId=:queUsrId and mcUsrAttempt.mcQueContentId=:mcQueContentId";
	 	
	 	private static final String LOAD_ATTEMPT_BY_ATTEMPT_ORDER = "from mcUsrAttempt in class McUsrAttempt where mcUsrAttempt.queUsrId=:queUsrId and mcUsrAttempt.mcQueContentId=:mcQueContentId and attemptOrder=:attemptOrder";
	 	
	 	private static final String LOAD_MARK = "from mcUsrAttempt in class McUsrAttempt";
	 	
	 	public McUsrAttempt getMcUserAttemptByUID(Long uid)
		{
			 return (McUsrAttempt) this.getHibernateTemplate()
	         .get(McUsrAttempt.class, uid);
		}
		
		public void saveMcUsrAttempt(McUsrAttempt mcUsrAttempt)
	    {
	    	this.getHibernateTemplate().save(mcUsrAttempt);
	    }
	    
		public List getHighestMark(Long queUsrId)
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
			List list = getSession().createQuery(LOAD_HIGHEST_MARK_BY_USER_ID)
				.setLong("queUsrId", queUsrId.longValue())
				.list();
			
			return list;
	    }
		

		public List getMarks()
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
			List list = getSession().createQuery(LOAD_MARK)
				.list();
			
			return list;
	    }
		
		
		public List getHighestAttemptOrder(Long queUsrId)
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
			List list = getSession().createQuery(LOAD_HIGHEST_ATTEMPT_ORDER_BY_USER_ID)
				.setLong("queUsrId", queUsrId.longValue())
				.list();
			return list;
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
		
		
		public List getAttemptByAttemptOrder(final Long queUsrId, final Long mcQueContentId, final Integer attemptOrder)
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_ATTEMPT_BY_ATTEMPT_ORDER)
			.setLong("queUsrId", queUsrId.longValue())
			.setLong("mcQueContentId", mcQueContentId.longValue())
			.setInteger("attemptOrder", attemptOrder.intValue())
			.list();
			return list;
	    }
		
		public void updateMcUsrAttempt(McUsrAttempt mcUsrAttempt)
	    {
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