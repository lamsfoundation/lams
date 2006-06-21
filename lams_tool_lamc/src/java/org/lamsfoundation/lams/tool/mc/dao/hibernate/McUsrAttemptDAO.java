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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc.dao.hibernate;

import java.util.ArrayList;
import java.util.Iterator;
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
	 	
	 	private static final String LOAD_ATTEMPT_FOR_QUESTION_CONTENT	 = "from mcUsrAttempt in class McUsrAttempt where mcUsrAttempt.mcQueContentId=:mcQueContentId";
	 	
	 	private static final String LOAD_MARK = "from mcUsrAttempt in class McUsrAttempt";
	 	
	 	private static final String LOAD_ATTEMPTS_ON_HIGHEST_ATTEMPT_ORDER = "from mcUsrAttempt in class McUsrAttempt where mcUsrAttempt.attemptOrder=:attemptOrder";
	 	
	 	private static final String LOAD_HIGHEST_MARK = "from mcUsrAttempt in class McUsrAttempt";
	 	
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
		
		public List getUserAttemptsForQuestionContentAndSessionUid(final Long queUsrUid,  final Long mcQueContentId, final Long mcSessionUid)
	    {
		    logger.debug("starting getUserAttemptsForQuestionContentAndSessionUid:");
		    logger.debug("queUsrUid:"  + queUsrUid);
		    logger.debug("mcQueContentId:"  + mcQueContentId);
		    logger.debug("mcSessionUid:"  + mcSessionUid);
		    
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_ATTEMPT_FOR_QUESTION_CONTENT)
			.setLong("mcQueContentId", mcQueContentId.longValue())
			.list();
	        
	        List userEntries= new ArrayList();
			if(list != null && list.size() > 0){
				Iterator listIterator=list.iterator();
		    	while (listIterator.hasNext())
		    	{
		    	    McUsrAttempt attempt=(McUsrAttempt)listIterator.next();
		    	    logger.debug("attempt:"  + attempt);
		    	    
		    	    if (attempt.getMcQueUsr().getUid().toString().equals(queUsrUid.toString()))
		    	    {
		    	        logger.debug("queUsrUid equal:"  + queUsrUid);
			    	    if (attempt.getMcQueUsr().getMcSession().getUid().toString().equals(mcSessionUid.toString()))
			    	    {
			    	        logger.debug("user belong to this session:"  + mcSessionUid);
			    	        userEntries.add(attempt);    
			    	    }
		    	        
		    	    }
		    	}
			}
			logger.debug("userEntries:"  + userEntries);
			return userEntries;
	    }
		

		public List getAttemptsOnHighestAttemptOrder(final Long queUsrUid,  final Long mcQueContentId, final Long mcSessionUid, final Integer attemptOrder)
	    {
		    logger.debug("starting getUserAttemptsForQuestionContentAndSessionUid:");
		    logger.debug("queUsrUid:"  + queUsrUid);
		    logger.debug("mcQueContentId:"  + mcQueContentId);
		    logger.debug("mcSessionUid:"  + mcSessionUid);
		    logger.debug("attemptOrder:"  + attemptOrder);
		    
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_ATTEMPT_FOR_QUESTION_CONTENT)
			.setLong("mcQueContentId", mcQueContentId.longValue())
			.list();
	        
	        List userEntries= new ArrayList();
			if(list != null && list.size() > 0){
				Iterator listIterator=list.iterator();
		    	while (listIterator.hasNext())
		    	{
		    	    McUsrAttempt attempt=(McUsrAttempt)listIterator.next();
		    	    logger.debug("attempt:"  + attempt);
		    	    
		    	    if (attempt.getMcQueUsr().getUid().toString().equals(queUsrUid.toString()))
		    	    {
		    	        logger.debug("queUsrUid equal:"  + queUsrUid);
			    	    if (attempt.getMcQueUsr().getMcSession().getUid().toString().equals(mcSessionUid.toString()))
			    	    {
			    	        logger.debug("user belongs to this session:"  + mcSessionUid);
			    	        
			    	        if (attempt.getAttemptOrder().intValue() == attemptOrder.intValue()) 
			    	            userEntries.add(attempt);    
			    	    }
		    	        
		    	    }
		    	}
			}
			logger.debug("userEntries:"  + userEntries);
			return userEntries;
	    }

		
		public List getAttemptsForUserInSession(final Long queUsrUid, final Long mcSessionUid)
	    {
		    logger.debug("starting getAttemptsForUserInSession:");
		    logger.debug("queUsrUid:"  + queUsrUid);
		    logger.debug("mcSessionUid:"  + mcSessionUid);
		    
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_MARK)
			.list();
	        
	        List userEntries= new ArrayList();
			if(list != null && list.size() > 0){
				Iterator listIterator=list.iterator();
		    	while (listIterator.hasNext())
		    	{
		    	    McUsrAttempt attempt=(McUsrAttempt)listIterator.next();
		    	    logger.debug("attempt:"  + attempt);
		    	    
		    	    if (attempt.getMcQueUsr().getUid().toString().equals(queUsrUid.toString()))
		    	    {
		    	        logger.debug("queUsrUid equal:"  + queUsrUid);
			    	    if (attempt.getMcQueUsr().getMcSession().getUid().toString().equals(mcSessionUid.toString()))
			    	    {
			    	        logger.debug("user belong to this session:"  + mcSessionUid);
			    	        userEntries.add(attempt);    
			    	    }
		    	        
		    	    }
		    	}
			}
			logger.debug("userEntries:"  + userEntries);
			return userEntries;
	    }

		
		public List getAttemptsForUserOnHighestAttemptOrderInSession(final Long queUsrUid, final Long mcSessionUid, final Integer attemptOrder)
	    {
		    logger.debug("starting getAttemptsForUserOnHighestAttemptOrderInSession:");
		    logger.debug("queUsrUid:"  + queUsrUid);
		    logger.debug("mcSessionUid:"  + mcSessionUid);
		    
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_ATTEMPTS_ON_HIGHEST_ATTEMPT_ORDER)
			.setInteger("attemptOrder", attemptOrder.intValue())
			.list();
	        
	        List userEntries= new ArrayList();
			if(list != null && list.size() > 0){
				Iterator listIterator=list.iterator();
		    	while (listIterator.hasNext())
		    	{
		    	    McUsrAttempt attempt=(McUsrAttempt)listIterator.next();
		    	    logger.debug("attempt:"  + attempt);
		    	    
		    	    if (attempt.getMcQueUsr().getUid().toString().equals(queUsrUid.toString()))
		    	    {
		    	        logger.debug("queUsrUid equal:"  + queUsrUid);
			    	    if (attempt.getMcQueUsr().getMcSession().getUid().toString().equals(mcSessionUid.toString()))
			    	    {
			    	        logger.debug("user belong to this session:"  + mcSessionUid);
			    	        userEntries.add(attempt);    
			    	    }
		    	        
		    	    }
		    	}
			}
			logger.debug("userEntries:"  + userEntries);
			return userEntries;
	    }


		public boolean getUserAttemptCorrectForQuestionContentAndSessionUid(final Long queUsrUid,  final Long mcQueContentId, final Long mcSessionUid, final Integer attemptOrder)
	    {
		    logger.debug("starting getUserAttemptsForQuestionContentAndSessionUid:");
		    logger.debug("queUsrUid:"  + queUsrUid);
		    logger.debug("mcQueContentId:"  + mcQueContentId);
		    logger.debug("mcSessionUid:"  + mcSessionUid);
		    logger.debug("attemptOrder:"  + attemptOrder);
		    
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_ATTEMPT_FOR_QUESTION_CONTENT)
			.setLong("mcQueContentId", mcQueContentId.longValue())
			.list();
	        
	        if(list != null && list.size() > 0){
				Iterator listIterator=list.iterator();
		    	while (listIterator.hasNext())
		    	{
		    	    McUsrAttempt attempt=(McUsrAttempt)listIterator.next();
		    	    logger.debug("attempt:"  + attempt);
		    	    
		    	    if (attempt.getMcQueUsr().getUid().toString().equals(queUsrUid.toString()))
		    	    {
		    	        logger.debug("queUsrUid equal:"  + queUsrUid);
			    	    if (attempt.getMcQueUsr().getMcSession().getUid().toString().equals(mcSessionUid.toString()))
			    	    {
			    	        logger.debug("user belong to this session:"  + mcSessionUid);
			    	        logger.debug("isAttemptCorrect:"  + attempt.isAttemptCorrect());
			    	        if (attempt.getAttemptOrder().intValue() == attemptOrder.intValue())  
			    	            return attempt.isAttemptCorrect(); 
			    	    }
		    	    }
		    	}
			}
	        return false;
	    }


		public McUsrAttempt getAttemptWithLastAttemptOrderForUserInSession(Long queUsrUid, final Long mcSessionUid)
	    {
		    logger.debug("starting getLastAttemptOrderForUserInSession:");
		    logger.debug("queUsrUid:"  + queUsrUid);
		    logger.debug("mcSessionUid:"  + mcSessionUid);
		    
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_MARK)
			.list();
	        
		    logger.debug("list:"  + list);
	        
	        List userEntries= new ArrayList();
			if(list != null && list.size() > 0){
				Iterator listIterator=list.iterator();
		    	while (listIterator.hasNext())
		    	{
		    	    McUsrAttempt attempt=(McUsrAttempt)listIterator.next();
		    	    logger.debug("attempt:"  + attempt);
		    	    
		    	    if (attempt.getMcQueUsr().getUid().toString().equals(queUsrUid.toString()))
		    	    {
		    	        logger.debug("queUsrUid equal:"  + queUsrUid);
			    	    if (attempt.getMcQueUsr().getMcSession().getUid().toString().equals(mcSessionUid.toString()))
			    	    {
			    	        logger.debug("user belong to this session:"  + mcSessionUid);
			    	        userEntries.add(attempt);    
			    	    }
		    	        
		    	    }
		    	}
			}
			logger.debug("userEntries:"  + userEntries);
			
			Iterator itAttempts=userEntries.iterator();
			int highestOrder=0;
			McUsrAttempt mcHighestUsrAttempt=null;
			
			while (itAttempts.hasNext())
			{
	    		McUsrAttempt mcUsrAttempt=(McUsrAttempt)itAttempts.next();
	    		logger.debug("mcUsrAttempt: " + mcUsrAttempt);
	    		int currentOrder=mcUsrAttempt.getAttemptOrder().intValue();
	    		logger.debug("currentOrder: " + currentOrder);
	    		
	    		if (currentOrder > highestOrder)
	    		{
	    		    mcHighestUsrAttempt=mcUsrAttempt;
	    		    highestOrder=currentOrder;
	    		    logger.debug("highestOrder is updated to: " + highestOrder);
	    		}
			}
			
			logger.debug("returning mcHighestUsrAttempt: " + mcHighestUsrAttempt);
			logger.debug("highestOrder has become: " + highestOrder);
			logger.debug("returning mcHighestUsrAttempt: " + mcHighestUsrAttempt);
			return mcHighestUsrAttempt;
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