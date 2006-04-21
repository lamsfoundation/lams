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
import org.lamsfoundation.lams.tool.vote.dao.IVoteQueContentDAO;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


/**
 * @author Ozgur Demirtas
 * 
 * <p>Hibernate implementation for database access to VoteQueContent for the vote tool.</p>
 */
public class VoteQueContentDAO extends HibernateDaoSupport implements IVoteQueContentDAO {
	 	static Logger logger = Logger.getLogger(VoteQueContentDAO.class.getName());
	 	
	 	private static final String CLEAN_QUESTION_CONTENT_BY_CONTENT_ID_SIMPLE = "from voteQueContent in class VoteQueContent where voteQueContent.voteContentId=:voteContentId";
	 	
	 	private static final String LOAD_QUESTION_CONTENT_BY_CONTENT_ID = "from voteQueContent in class VoteQueContent where voteQueContent.voteContentId=:voteContentId";
	 	
	 	private static final String LOAD_QUESTION_CONTENT_BY_QUESTION_TEXT = "from voteQueContent in class VoteQueContent where voteQueContent.question=:question and voteQueContent.voteContentId=:voteContentUid";
	 	
	 	private static final String LOAD_QUESTION_CONTENT_BY_DISPLAY_ORDER = "from voteQueContent in class VoteQueContent where voteQueContent.displayOrder=:displayOrder and voteQueContent.voteContentId=:voteContentUid";
	 	
	 		 	
	 	public VoteQueContent getVoteQueContentByUID(Long uid)
		{
			 return (VoteQueContent) this.getHibernateTemplate()
	         .get(VoteQueContent.class, uid);
		}
		
		
	 	public VoteQueContent getToolDefaultQuestionContent(final long voteContentId)
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
			List list = getSession().createQuery(LOAD_QUESTION_CONTENT_BY_CONTENT_ID)
				.setLong("voteContentId", voteContentId)
				.list();
			
			if(list != null && list.size() > 0){
				VoteQueContent voteq = (VoteQueContent) list.get(0);
				return voteq;
			}
			return null;
	    }
	 	
	 	
        public List getVoteQueContentsByContentId(long voteContentId){
            return getHibernateTemplate().findByNamedParam(LOAD_QUESTION_CONTENT_BY_CONTENT_ID, "voteContentId", new Long(voteContentId));
        }
	 	
	 	public List getAllQuestionEntries(final long voteContentId)
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
			List list = getSession().createQuery(LOAD_QUESTION_CONTENT_BY_CONTENT_ID)
				.setLong("voteContentId", voteContentId)
				.list();

			return list;
	    }
	 	
	 	
	 	public VoteQueContent getQuestionContentByQuestionText(final String question, final Long voteContentUid)
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
			List list = getSession().createQuery(LOAD_QUESTION_CONTENT_BY_QUESTION_TEXT)
				.setString("question", question)
				.setLong("voteContentUid", voteContentUid.longValue())				
				.list();
			
			if(list != null && list.size() > 0){
				VoteQueContent voteq = (VoteQueContent) list.get(0);
				return voteq;
			}
			return null;
	    }
	 	
	 	
	 	public VoteQueContent getQuestionContentByDisplayOrder(final Long displayOrder, final Long voteContentUid)
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
			List list = getSession().createQuery(LOAD_QUESTION_CONTENT_BY_DISPLAY_ORDER)
				.setLong("displayOrder", displayOrder.longValue())
				.setLong("voteContentUid", voteContentUid.longValue())				
				.list();
			
			if(list != null && list.size() > 0){
				VoteQueContent voteq = (VoteQueContent) list.get(0);
				return voteq;
			}
			return null;
	    }

	 	
	 	public void removeQuestionContentByVoteUid(final Long voteContentUid)
	    {
			HibernateTemplate templ = this.getHibernateTemplate();
			List list = getSession().createQuery(LOAD_QUESTION_CONTENT_BY_CONTENT_ID)
				.setLong("voteContentId", voteContentUid.longValue())
				.list();

			if(list != null && list.size() > 0){
				Iterator listIterator=list.iterator();
		    	while (listIterator.hasNext())
		    	{
		    		VoteQueContent voteQueContent=(VoteQueContent)listIterator.next();
					this.getSession().setFlushMode(FlushMode.AUTO);
		    		templ.delete(voteQueContent);
		    		templ.flush();
		    	}
			}
	    }
	 	

	 	public void resetAllQuestions(final Long voteContentUid)
	    {
			HibernateTemplate templ = this.getHibernateTemplate();
			List list = getSession().createQuery(LOAD_QUESTION_CONTENT_BY_CONTENT_ID)
				.setLong("voteContentId", voteContentUid.longValue())
				.list();

			if(list != null && list.size() > 0){
				Iterator listIterator=list.iterator();
		    	while (listIterator.hasNext())
		    	{
		    		VoteQueContent voteQueContent=(VoteQueContent)listIterator.next();
					this.getSession().setFlushMode(FlushMode.AUTO);
		    		templ.update(voteQueContent);
		    	}
			}
	    }
	 	

	 	public void cleanAllQuestions(final Long voteContentUid)
	    {
			HibernateTemplate templ = this.getHibernateTemplate();
			List list = getSession().createQuery(LOAD_QUESTION_CONTENT_BY_CONTENT_ID)
				.setLong("voteContentId", voteContentUid.longValue())
				.list();

			if(list != null && list.size() > 0){
				Iterator listIterator=list.iterator();
		    	while (listIterator.hasNext())
		    	{
		    		VoteQueContent voteQueContent=(VoteQueContent)listIterator.next();
	    			this.getSession().setFlushMode(FlushMode.AUTO);
	    			logger.debug("deleting voteQueContent: " + voteQueContent);
		    		templ.delete(voteQueContent);	
		    	}
			}
	    }

	 	
	 	public void cleanAllQuestionsSimple(final Long voteContentUid)
	    {
			HibernateTemplate templ = this.getHibernateTemplate();
			List list = getSession().createQuery(CLEAN_QUESTION_CONTENT_BY_CONTENT_ID_SIMPLE)
				.setLong("voteContentId", voteContentUid.longValue())
				.list();

			if(list != null && list.size() > 0){
				Iterator listIterator=list.iterator();
		    	while (listIterator.hasNext())
		    	{
		    		VoteQueContent voteQueContent=(VoteQueContent)listIterator.next();
	    			this.getSession().setFlushMode(FlushMode.AUTO);
	    			logger.debug("deleting voteQueContent: " + voteQueContent);
		    		templ.delete(voteQueContent);	
		    	}
			}
	    }

	 	
	 	public void saveVoteQueContent(VoteQueContent voteQueContent)
	    {
	    	this.getHibernateTemplate().save(voteQueContent);
	    }
	    
		public void updateVoteQueContent(VoteQueContent voteQueContent)
	    {
	    	this.getHibernateTemplate().update(voteQueContent);
	    }
		
		public void saveOrUpdateVoteQueContent(VoteQueContent voteQueContent)
	    {
	    	this.getHibernateTemplate().saveOrUpdate(voteQueContent);
	    }
		
		public void removeVoteQueContentByUID(Long uid)
	    {
			VoteQueContent voteq = (VoteQueContent)getHibernateTemplate().get(VoteQueContent.class, uid);
			this.getSession().setFlushMode(FlushMode.AUTO);
	    	this.getHibernateTemplate().delete(voteq);
	    }
		
		
		public void removeVoteQueContent(VoteQueContent voteQueContent)
	    {
			this.getSession().setFlushMode(FlushMode.AUTO);
	        this.getHibernateTemplate().delete(voteQueContent);
	    }
		
		 public void flush()
	    {
	        this.getHibernateTemplate().flush();
	    }
	} 