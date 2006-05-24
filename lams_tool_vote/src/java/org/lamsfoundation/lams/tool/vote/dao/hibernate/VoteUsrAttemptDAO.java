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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.lamsfoundation.lams.tool.vote.dao.IVoteUserDAO;
import org.lamsfoundation.lams.tool.vote.dao.IVoteUsrAttemptDAO;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUsrAttempt;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author Ozgur Demirtas
 * <p>Hibernate implementation for database access to VoteUsrAttemptDAO for the voting tool.</p>
 */
public class VoteUsrAttemptDAO extends HibernateDaoSupport implements IVoteUsrAttemptDAO {
	 	static Logger logger = Logger.getLogger(VoteUsrAttemptDAO.class.getName());
	 	
	 	private IVoteUserDAO			voteUserDAO;
	 	
	 	private static final String LOAD_ATTEMPT_FOR_QUE_CONTENT = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.queUsrId=:queUsrId and voteUsrAttempt.voteQueContentId=:voteQueContentId";
	 	
	 	private static final String LOAD_ATTEMPT_FOR_USER		 = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.queUsrId=:queUsrId";

	 	private static final String LOAD_ATTEMPT_FOR_QUESTION_CONTENT	 = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.voteQueContentId=:voteQueContentId";
	 	
	 	private static final String LOAD_ATTEMPT_FOR_USER_AND_QUESTION_CONTENT	 = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.queUsrId=:queUsrId and voteUsrAttempt.voteQueContentId=:voteQueContentId";
	 	
	 	private static final String LOAD_ATTEMPT_FOR_USER_AND_QUESTION_CONTENT_AND_SESSION	 = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.queUsrId=:queUsrId and voteUsrAttempt.voteQueContentId=:voteQueContentId";
	 	
	 	private static final String LOAD_USER_ENTRIES = "select distinct voteUsrAttempt.userEntry from VoteUsrAttempt voteUsrAttempt";
	 	
	 	private static final String LOAD_USER_ENTRY_RECORDS = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.userEntry=:userEntry and voteUsrAttempt.voteQueContentId=1 ";
	 	
	 	private static final String LOAD_DISTINCT_USER_ENTRY_RECORDS = "select distinct voteUsrAttempt.queUsrId from VoteUsrAttempt voteUsrAttempt where voteUsrAttempt.userEntry=:userEntry";
	 	
	 	private static final String LOAD_ALL_ENTRIES = "from voteUsrAttempt in class VoteUsrAttempt";
	 	
	 	private static final String LOAD_DISTINCT_USER_ENTRIES = "select distinct voteUsrAttempt.queUsrId from VoteUsrAttempt voteUsrAttempt";
	 	
	 	
	 	public VoteUsrAttempt getVoteUserAttemptByUID(Long uid)
		{
			 return (VoteUsrAttempt) this.getHibernateTemplate()
	         .get(VoteUsrAttempt.class, uid);
		}
	 	
	 	
	    public VoteUsrAttempt getAttemptByUID(Long uid)
		{
			String query = "from VoteUsrAttempt attempt where attempt.uid=?";
			
			HibernateTemplate templ = this.getHibernateTemplate();
			List list = getSession().createQuery(query)
			.setLong(0,uid.longValue())
			.list();
			
			if(list != null && list.size() > 0){
			    VoteUsrAttempt attempt = (VoteUsrAttempt) list.get(0);
				return attempt;
			}
			return null;	
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

		public List getContentEntries(final Long voteContentUid)
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_ALL_ENTRIES)
			.list();

	        List contentEntries= new ArrayList();
	        Iterator listIterator=list.iterator();
	        logger.debug("looking for voteContentUid: " + voteContentUid);
	    	while (listIterator.hasNext())
	    	{
	    	    VoteUsrAttempt attempt=(VoteUsrAttempt)listIterator.next();
	    	    logger.debug("attempt: " + attempt);
	    	    if (attempt.getVoteQueUsr().getVoteSession().getVoteContent().getUid().toString().equals(voteContentUid.toString()))
	    	    {
	    	        logger.debug("found content: " + voteContentUid);
	    	        if (attempt.getVoteQueContentId().toString().equals("1"))
	    	        {
	    	            logger.debug("user entered nomination: " + attempt.getUserEntry());
	    	            if ((attempt.getUserEntry() != null) && (attempt.getUserEntry().length() > 0))
			    	        contentEntries.add(attempt.getUserEntry());    
	    	        }
	    	        else
	    	        {
	    	            logger.debug("standard content nomination: " + attempt.getVoteQueContent().getQuestion());
	    	            contentEntries.add(attempt.getVoteQueContent().getQuestion());
	    	        }
	    	    }
	    	}
	    	
	    	return contentEntries;
	    }

		
		public int getUserEnteredVotesCountForContent(final Long voteContentUid)
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_ALL_ENTRIES)
			.list();

	        int count=0;
	        Iterator listIterator=list.iterator();
	        logger.debug("looking for voteContentUid: " + voteContentUid);
	    	while (listIterator.hasNext())
	    	{
	    	    VoteUsrAttempt attempt=(VoteUsrAttempt)listIterator.next();
	    	    logger.debug("attempt: " + attempt);
	    	    
	    	    if (attempt.getVoteQueUsr().getVoteSession().getVoteContent().getUid().toString().equals(voteContentUid.toString()))
	    	    {
	    	        ++count;
	    	    }
	    	}
	    	
	    	return count;
	    }

		
		
		public Set getUserEntries()
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_USER_ENTRIES)
			.list();
	        
	        Set set= new HashSet();
	        
	        Set userEntries= new HashSet();
			if(list != null && list.size() > 0){
				Iterator listIterator=list.iterator();
		    	while (listIterator.hasNext())
		    	{
		    	    String entry=(String)listIterator.next();
		    	    logger.debug("entry: " + entry);
		    	    if ((entry != null) && (entry.length() > 0))  
		    	        userEntries.add(entry);
		    	}
			}
			return userEntries;
	    }
		

		public List getSessionUserEntries(final Long voteSessionUid)
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_ALL_ENTRIES)
			.list();

	        List sessionUserEntries= new ArrayList();
			if(list != null && list.size() > 0){
				Iterator listIterator=list.iterator();
		    	while (listIterator.hasNext())
		    	{
		    	    VoteUsrAttempt attempt=(VoteUsrAttempt)listIterator.next();
		    	    logger.debug("attempt: " + attempt);
		    	    if (attempt.getVoteQueUsr().getVoteSession().getUid().toString().equals(voteSessionUid.toString()))
		    	    {
			    	    if ((attempt.getUserEntry() != null) && (attempt.getUserEntry().length() > 0))
			    	        sessionUserEntries.add(attempt.getUserEntry());
		    	    }
		    	}
			}
			return sessionUserEntries;
	    }

		public Set getSessionUserEntriesSet(final Long voteSessionUid)
		{
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_ALL_ENTRIES)
			.list();

	        Set sessionUserEntries= new HashSet();
			if(list != null && list.size() > 0){
				Iterator listIterator=list.iterator();
		    	while (listIterator.hasNext())
		    	{
		    	    VoteUsrAttempt attempt=(VoteUsrAttempt)listIterator.next();
		    	    logger.debug("attempt: " + attempt);
		    	    if (attempt.getVoteQueUsr().getVoteSession().getUid().toString().equals(voteSessionUid.toString()))
		    	    {
			    	    if ((attempt.getUserEntry() != null) && (attempt.getUserEntry().length() > 0))
			    	        sessionUserEntries.add(attempt.getUserEntry());
		    	    }
		    	}
			}
			return sessionUserEntries;		    
		}
		
		public int getUserRecordsEntryCount(final String userEntry)
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_DISTINCT_USER_ENTRY_RECORDS)
			.setString("userEntry", userEntry)				
			.list();

			if(list != null && list.size() > 0){
			    return list.size();
			}
			
			return 0;
	    }
		

		public int getSessionUserRecordsEntryCount(final String userEntry, final Long voteSessionUid, IVoteService voteService)
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_DISTINCT_USER_ENTRY_RECORDS)
			.setString("userEntry", userEntry)				
			.list();
	        
	        int entryCount=0;

			if(list != null && list.size() > 0){
				Iterator listIterator=list.iterator();
		    	while (listIterator.hasNext())
		    	{
		    	    Long userId=(Long)listIterator.next();
		    	    logger.debug("userId: " + userId);
		    	    logger.debug("voteService: " + voteService);
		    	    VoteQueUsr voteQueUsr=voteService.getVoteUserByUID(userId);
		    	    logger.debug("voteQueUsr: " + voteQueUsr);
		    	    
		    	    if (voteQueUsr.getVoteSession().getUid().toString().equals(voteSessionUid.toString()))
		    	    {
		    	        ++entryCount;		    	        
		    	    }
		    	}
			}
			return entryCount;
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

		public void removeAttemptsForUserandSession(final Long queUsrId, final Long voteSessionId)
	    {
		    String strGetUser = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.queUsrId=:queUsrId";		    
	        HibernateTemplate templ = this.getHibernateTemplate();
			List list = getSession().createQuery(strGetUser)
				.setLong("queUsrId", queUsrId.longValue())
				.list();
			
			if(list != null && list.size() > 0){
				Iterator listIterator=list.iterator();
		    	while (listIterator.hasNext())
		    	{
		    	    VoteUsrAttempt attempt=(VoteUsrAttempt)listIterator.next();
		    	    if (attempt.getVoteQueUsr().getVoteSession().getUid().toString().equals(voteSessionId.toString()))
		    	    {
						this.getSession().setFlushMode(FlushMode.AUTO);
			    		templ.delete(attempt);
			    		templ.flush();
		    	        
		    	    }
		    	}
			}
	    }


		public int getAttemptsForQuestionContent(final Long voteQueContentId)
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_ATTEMPT_FOR_QUESTION_CONTENT)
			.setLong("voteQueContentId", voteQueContentId.longValue())
			.list();
	        
	        if(list != null && list.size() > 0){
			    return list.size();
			}
	        
			return 0;
	    }

		
		public int getStandardAttemptsForQuestionContentAndSessionUid(final Long voteQueContentId, final Long voteSessionUid)
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_ATTEMPT_FOR_QUESTION_CONTENT)
			.setLong("voteQueContentId", voteQueContentId.longValue())
			.list();
	        
	        List userEntries= new ArrayList();
			if(list != null && list.size() > 0){
				Iterator listIterator=list.iterator();
		    	while (listIterator.hasNext())
		    	{
		    	    VoteUsrAttempt attempt=(VoteUsrAttempt)listIterator.next();
		    	    if (attempt.getVoteQueUsr().getVoteSession().getUid().toString().equals(voteSessionUid.toString()))
		    	    {
		    	            userEntries.add(attempt);    
		    	    }
		    	}
			}
			return userEntries.size();

	    }
		
		
		public boolean isVoteVisibleForSession(final String userEntry, final Long voteSessionUid)
		{
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_USER_ENTRY_RECORDS)
			.setString("userEntry", userEntry)				
			.list();
	        
	        
	        List sessionUserEntries= new ArrayList();
			if(list != null && list.size() > 0){
				Iterator listIterator=list.iterator();
		    	while (listIterator.hasNext())
		    	{
		    	    VoteUsrAttempt attempt=(VoteUsrAttempt)listIterator.next();
		    	    logger.debug("attempt: " + attempt);
		    	    if (attempt.getVoteQueUsr().getVoteSession().getUid().toString().equals(voteSessionUid.toString()))
		    	    {
			    	    boolean isVoteVisible= attempt.isVisible();
			    	    logger.debug("isVoteVisible: " + isVoteVisible);
			    	    if  (isVoteVisible == false)
			    	        return false;
		    	    }
		    	}
			}
			
			return true;
		}

		public int getStandardAttemptsForQuestionContentAndContentUid(final Long voteQueContentId, final Long voteContentUid)
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_ATTEMPT_FOR_QUESTION_CONTENT)
			.setLong("voteQueContentId", voteQueContentId.longValue())
			.list();
	        
	        List userEntries= new ArrayList();
			if(list != null && list.size() > 0){
				Iterator listIterator=list.iterator();
		    	while (listIterator.hasNext())
		    	{
		    	    VoteUsrAttempt attempt=(VoteUsrAttempt)listIterator.next();
		    	    
		    	    if (attempt.getVoteQueUsr().getVoteSession().getVoteContent().getUid().toString().equals(voteContentUid.toString()))
		    	    {
		    	        userEntries.add(attempt);
		    	    }
		    	}
			}
			return userEntries.size();

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


		public VoteUsrAttempt getAttemptsForUserAndQuestionContentAndSession(final Long queUsrId, final Long voteQueContentId, final Long voteSessionId)
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_ATTEMPT_FOR_USER_AND_QUESTION_CONTENT_AND_SESSION)
			.setLong("queUsrId", queUsrId.longValue())
			.setLong("voteQueContentId", voteQueContentId.longValue())
			.list();
	        
			if(list != null && list.size() > 0){
				Iterator listIterator=list.iterator();
		    	while (listIterator.hasNext())
		    	{
		    	    VoteUsrAttempt attempt=(VoteUsrAttempt)listIterator.next();
		    	    logger.debug("attempt: " + attempt);
		    	    if (attempt.getVoteQueUsr().getVoteSession().getUid().toString().equals(voteSessionId.toString()))
		    	    {
		    	        return attempt;
		    	    }
		    	}
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

		public List getUserEnteredVotesForSession(final String userEntry, final Long voteSessionUid)
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_USER_ENTRY_RECORDS)
			.setString("userEntry", userEntry)				
			.list();
	        
	        
	        List sessionUserEntries= new ArrayList();
			if(list != null && list.size() > 0){
				Iterator listIterator=list.iterator();
		    	while (listIterator.hasNext())
		    	{
		    	    VoteUsrAttempt attempt=(VoteUsrAttempt)listIterator.next();
		    	    logger.debug("attempt: " + attempt);
		    	    if (attempt.getVoteQueUsr().getVoteSession().getUid().toString().equals(voteSessionUid.toString()))
		    	    {
			    	        sessionUserEntries.add(attempt.getUserEntry());
		    	    }
		    	}
			}
			return sessionUserEntries;
	    }

		public int getAllEntriesCount()
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_ALL_ENTRIES)
			.list();
			return list.size();
	    }
		

		public int getSessionEntriesCount(final Long voteSessionUid)
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_ALL_ENTRIES)
			.list();
	        
	        int totalSessionAttemptsCount=0;
			if(list != null && list.size() > 0){
				Iterator listIterator=list.iterator();
		    	while (listIterator.hasNext())
		    	{
		    	    VoteUsrAttempt attempt=(VoteUsrAttempt)listIterator.next();
		    	    logger.debug("attempt: " + attempt);
		    	    if (attempt.getVoteQueUsr().getVoteSession().getUid().toString().equals(voteSessionUid.toString()))
		    	    {
		    	        ++totalSessionAttemptsCount;
		    	    }
		    	}
			}
			return totalSessionAttemptsCount;
	    }

		
		public int getCompletedSessionEntriesCount(final Long voteSessionUid)
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_ALL_ENTRIES)
			.list();
	        
	        int completedSessionCount=0;
			if(list != null && list.size() > 0){
				Iterator listIterator=list.iterator();
		    	while (listIterator.hasNext())
		    	{
		    	    VoteUsrAttempt attempt=(VoteUsrAttempt)listIterator.next();
		    	    logger.debug("attempt: " + attempt);
		    	    if (attempt.getVoteQueUsr().getVoteSession().getUid().toString().equals(voteSessionUid.toString()))
		    	    {
		    	        String sessionStatus=attempt.getVoteQueUsr().getVoteSession().getSessionStatus();
		    	        logger.debug("sessionStatus: " + sessionStatus);
		    	        if (sessionStatus.equals("COMPLETED"))
		    	        {
		    	            logger.debug("this is a completed session: " + sessionStatus);
		    	            ++completedSessionCount;    
		    	        }
		    	    }
		    	}
			}
			return completedSessionCount;
	    }

		
		public void updateVoteUsrAttempt(VoteUsrAttempt voteUsrAttempt)
	    {
			this.getSession().setFlushMode(FlushMode.AUTO);
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