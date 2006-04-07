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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.lamsfoundation.lams.tool.vote.dao.IVoteOptionsContentDAO;
import org.lamsfoundation.lams.tool.vote.pojos.VoteOptsContent;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


/**
 * @author Ozgur Demirtas
 * <p>Hibernate implementation for database access to voteOptionsContent for the voting tool.</p>
 */
public class VoteOptionsContentDAO extends HibernateDaoSupport implements IVoteOptionsContentDAO {
	 	static Logger logger = Logger.getLogger(VoteOptionsContentDAO.class.getName());
	 	
	 	private static final String FIND_VOTE_OPTIONS_CONTENT = "from " + VoteOptsContent.class.getName() + " as mco where mc_que_content_id=?";
	 	
	 	private static final String LOAD_VOTE_CONTENT_BY_OPTION_TEXT = "from mcOptsContent in class VoteOptsContent where mcOptsContent.mcQueOptionText=:option and mcOptsContent.mcQueContentId=:mcQueContentUid";
	 	
	 	private static final String LOAD_PERSISTED_SELECTED_OPTIONS = "from mcOptsContent in class VoteOptsContent where mcOptsContent.mcQueContentId=:mcQueContentUid and  mcOptsContent.correctOption = 1";
	 	
	 	private static final String LOAD_CORRECT_OPTION = "from mcOptsContent in class VoteOptsContent where mcOptsContent.mcQueContentId=:mcQueContentUid and  mcOptsContent.correctOption = 1";
	 	
	 	public VoteOptsContent getMcOptionsContentByUID(Long uid)
		{
			 return (VoteOptsContent) this.getHibernateTemplate()
	         .get(VoteOptsContent.class, uid);
		}
		
	 	
	 	public List findMcOptionsContentByQueId(Long mcQueContentId)
	    {
			HibernateTemplate templ = this.getHibernateTemplate();
			if ( mcQueContentId != null) {
				List list = getSession().createQuery(FIND_VOTE_OPTIONS_CONTENT)
					.setLong(0,mcQueContentId.longValue())
					.list();
				return list;
			}
			return null;
	    }

	 	public List findMcOptionNamesByQueId(Long mcQueContentId)
	    {
	 		
	 		List listOptionNames= new LinkedList();
	 		
			HibernateTemplate templ = this.getHibernateTemplate();
			if ( mcQueContentId != null) {
				List list = getSession().createQuery(FIND_VOTE_OPTIONS_CONTENT)
					.setLong(0,mcQueContentId.longValue())
					.list();
				
				if(list != null && list.size() > 0){
					Iterator listIterator=list.iterator();
			    	while (listIterator.hasNext())
			    	{
			    		VoteOptsContent mcOptsContent=(VoteOptsContent)listIterator.next();
			    		listOptionNames.add(mcOptsContent.getVoteQueOptionText());
			    	}
				}
			}
			return listOptionNames;
	    }
	 	
	 	public VoteOptsContent getOptionContentByOptionText(final String option, final Long mcQueContentUid)
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
			List list = getSession().createQuery(LOAD_VOTE_CONTENT_BY_OPTION_TEXT)
				.setString("option", option)
				.setLong("mcQueContentUid", mcQueContentUid.longValue())				
				.list();
			
			if(list != null && list.size() > 0){
				VoteOptsContent mcq = (VoteOptsContent) list.get(0);
				return mcq;
			}
			return null;
	    }
	 	
	 	
	 	public List getPersistedSelectedOptions(Long mcQueContentId) 
	 	{
	 		HibernateTemplate templ = this.getHibernateTemplate();
			List list = getSession().createQuery(LOAD_PERSISTED_SELECTED_OPTIONS)
				.setLong("mcQueContentUid", mcQueContentId.longValue())
				.list();
			
			return list;
	 	}
	 	
	 	public List getCorrectOption(Long mcQueContentId) 
	 	{
	 		HibernateTemplate templ = this.getHibernateTemplate();
			List list = getSession().createQuery(LOAD_CORRECT_OPTION)
				.setLong("mcQueContentUid", mcQueContentId.longValue())
				.list();
			
			return list;
	 	}
		
		public void saveMcOptionsContent(VoteOptsContent mcOptsContent)
	    {
	    	this.getHibernateTemplate().save(mcOptsContent);
	    }
	    
		public void updateMcOptionsContent(VoteOptsContent mcOptsContent)
	    {
	    	this.getHibernateTemplate().update(mcOptsContent);
	    }
		
		
		public void removeMcOptionsContentByUID(Long uid)
	    {
			VoteOptsContent mco = (VoteOptsContent)getHibernateTemplate().get(VoteOptsContent.class, uid);
	    	this.getHibernateTemplate().delete(mco);
	    }
		
		
		public void removeMcOptionsContentByQueId(Long mcQueContentId)
	    {
			HibernateTemplate templ = this.getHibernateTemplate();
			List list = getSession().createQuery(FIND_VOTE_OPTIONS_CONTENT)
				.setLong(0,mcQueContentId.longValue())
				.list();

			if(list != null && list.size() > 0){
				Iterator listIterator=list.iterator();
		    	while (listIterator.hasNext())
		    	{
		    		VoteOptsContent mcOptsContent=(VoteOptsContent)listIterator.next();
					this.getSession().setFlushMode(FlushMode.AUTO);
		    		templ.delete(mcOptsContent);
		    	}
			}
	    }
				
		
		public void removeMcOptionsContent(VoteOptsContent mcOptsContent)
	    {
			this.getSession().setFlushMode(FlushMode.AUTO);
	        this.getHibernateTemplate().delete(mcOptsContent);
	    }
		
		 public void flush()
	    {
	        this.getHibernateTemplate().flush();
	    }
	} 