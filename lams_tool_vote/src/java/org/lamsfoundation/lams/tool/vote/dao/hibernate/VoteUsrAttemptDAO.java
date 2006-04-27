/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.vote.dao.hibernate;

import java.util.Iterator;
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
	 	
	 	private static final String LOAD_HIGHEST_MARK_BY_USER_ID = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.queUsrId=:queUsrId";
	 	
	 	private static final String LOAD_HIGHEST_ATTEMPT_ORDER_BY_USER_ID = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.queUsrId=:queUsrId";
	 	
	 	private static final String LOAD_ATTEMPT_FOR_QUE_CONTENT = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.queUsrId=:queUsrId and voteUsrAttempt.voteQueContentId=:voteQueContentId";
	 	
	 	private static final String LOAD_ATTEMPT_FOR_USER		 = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.queUsrId=:queUsrId";
	 	
	 	private static final String LOAD_ATTEMPT_FOR_USER_AND_QUESTION_CONTENT	 = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.queUsrId=:queUsrId and voteUsrAttempt.voteQueContentId=:voteQueContentId";
	 	
	 	private static final String LOAD_USER_ENTRIES = "select distinct voteUsrAttempt.userEntry from VoteUsrAttempt voteUsrAttempt";
	 	
	 	private static final String LOAD_USER_ENTRY_RECORDS = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.userEntry=:userEntry and voteUsrAttempt.voteQueContentId=1 ";
            
	 	
	 	public VoteUsrAttempt getVoteUserAttemptByUID(Long uid)
		{
			 return (VoteUsrAttempt) this.getHibernateTemplate()
	         .get(VoteUsrAttempt.class, uid);
		}
		
		public void saveVoteUsrAttempt(VoteUsrAttempt voteUsrAttempt)
	    {
	    	this.getHibernateTemplate().save(voteUsrAttempt);
	    }
	    
		
		public List getAttemptsForUser(final Long queUsrId)
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_ATTEMPT_FOR_USER)
			.setLong("queUsrId", queUsrId.longValue())
			.list();
			return list;
	    }
		
		public List getUserEntries()
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_USER_ENTRIES)
			.list();
			return list;
	    }
		
		public void removeAttemptsForUser(final Long queUsrId)
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_ATTEMPT_FOR_USER)
			.setLong("queUsrId", queUsrId.longValue())
			.list();
			
			if(list != null && list.size() > 0){
				Iterator listIterator=list.iterator();
		    	while (listIterator.hasNext())
		    	{
		    	    VoteUsrAttempt attempt=(VoteUsrAttempt)listIterator.next();
					this.getSession().setFlushMode(FlushMode.AUTO);
		    		templ.delete(attempt);
		    		templ.flush();
		    	}
			}
	    }


		public VoteUsrAttempt getAttemptsForUserAndQuestionContent(final Long queUsrId, final Long voteQueContentId)
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_ATTEMPT_FOR_USER_AND_QUESTION_CONTENT)
			.setLong("queUsrId", queUsrId.longValue())
			.setLong("voteQueContentId", voteQueContentId.longValue())
			.list();
	        
	        if(list != null && list.size() > 0){
			    VoteUsrAttempt voteA = (VoteUsrAttempt) list.get(0);
				return voteA;
			}
	        
			return null;
	    }

		
		public List getAttemptsListForUserAndQuestionContent(final Long queUsrId, final Long voteQueContentId)
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_ATTEMPT_FOR_USER_AND_QUESTION_CONTENT)
			.setLong("queUsrId", queUsrId.longValue())
			.setLong("voteQueContentId", voteQueContentId.longValue())
			.list();
	        return list;
	    }

		
		public int  getLastNominationCount(Long userId)
		{
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_ATTEMPT_FOR_USER)
			.setLong("queUsrId", userId.longValue())
			.list();
			
	        
			if(list != null && list.size() > 0){
			    VoteUsrAttempt vote = (VoteUsrAttempt) list.get(0);
				return vote.getNominationCount();
			}
			return 0;
		}
				
		
		public List getAttemptForQueContent(final Long queUsrId, final Long voteQueContentId)
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_ATTEMPT_FOR_QUE_CONTENT)
			.setLong("queUsrId", queUsrId.longValue())
			.setLong("voteQueContentId", voteQueContentId.longValue())				
			.list();
			return list;
	    }
		

		public List getUserRecords(final String userEntry)
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_USER_ENTRY_RECORDS)
			.setString("userEntry", userEntry)				
			.list();
			return list;
	    }
		
		
		public void updateVoteUsrAttempt(VoteUsrAttempt voteUsrAttempt)
	    {
	    	this.getHibernateTemplate().update(voteUsrAttempt);
	    }
		
		public void removeVoteUsrAttemptByUID(Long uid)
	    {
			VoteUsrAttempt votea = (VoteUsrAttempt)getHibernateTemplate().get(VoteUsrAttempt.class, uid);
			this.getSession().setFlushMode(FlushMode.AUTO);
	    	this.getHibernateTemplate().delete(votea);
	    }
		
		
		public void removeVoteUsrAttempt(VoteUsrAttempt voteUsrAttempt)
	    {
			this.getSession().setFlushMode(FlushMode.AUTO);
	        this.getHibernateTemplate().delete(voteUsrAttempt);
	    }
} 