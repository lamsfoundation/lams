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
	 	
	 	private static final String FIND_VOTE_OPTIONS_CONTENT = "from " + VoteOptsContent.class.getName() + " as voteo where vote_que_content_id=?";
	 	
	 	private static final String LOAD_VOTE_CONTENT_BY_OPTION_TEXT = "from voteOptsContent in class VoteOptsContent where voteOptsContent.voteQueOptionText=:option and voteOptsContent.voteQueContentId=:voteQueContentUid";
	 	
	 	public VoteOptsContent getVoteOptionsContentByUID(Long uid)
		{
			 return (VoteOptsContent) this.getHibernateTemplate()
	         .get(VoteOptsContent.class, uid);
		}
		
	 	
	 	public List findVoteOptionsContentByQueId(Long voteQueContentId)
	    {
			HibernateTemplate templ = this.getHibernateTemplate();
			if ( voteQueContentId != null) {
				List list = getSession().createQuery(FIND_VOTE_OPTIONS_CONTENT)
					.setLong(0,voteQueContentId.longValue())
					.list();
				return list;
			}
			return null;
	    }

	 	public List findVoteOptionNamesByQueId(Long voteQueContentId)
	    {
	 		
	 		List listOptionNames= new LinkedList();
	 		
			HibernateTemplate templ = this.getHibernateTemplate();
			if ( voteQueContentId != null) {
				List list = getSession().createQuery(FIND_VOTE_OPTIONS_CONTENT)
					.setLong(0,voteQueContentId.longValue())
					.list();
				
				if(list != null && list.size() > 0){
					Iterator listIterator=list.iterator();
			    	while (listIterator.hasNext())
			    	{
			    		VoteOptsContent voteOptsContent=(VoteOptsContent)listIterator.next();
			    		listOptionNames.add(voteOptsContent.getVoteQueOptionText());
			    	}
				}
			}
			return listOptionNames;
	    }
	 	
	 	public VoteOptsContent getOptionContentByOptionText(final String option, final Long voteQueContentUid)
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
			List list = getSession().createQuery(LOAD_VOTE_CONTENT_BY_OPTION_TEXT)
				.setString("option", option)
				.setLong("voteQueContentUid", voteQueContentUid.longValue())				
				.list();
			
			if(list != null && list.size() > 0){
				VoteOptsContent voteq = (VoteOptsContent) list.get(0);
				return voteq;
			}
			return null;
	    }
	 	

		public void saveVoteOptionsContent(VoteOptsContent voteOptsContent)
	    {
	    	this.getHibernateTemplate().save(voteOptsContent);
	    }
	    
		public void updateVoteOptionsContent(VoteOptsContent voteOptsContent)
	    {
	    	this.getHibernateTemplate().update(voteOptsContent);
	    }
		
		
		public void removeVoteOptionsContentByUID(Long uid)
	    {
			VoteOptsContent voteo = (VoteOptsContent)getHibernateTemplate().get(VoteOptsContent.class, uid);
	    	this.getHibernateTemplate().delete(voteo);
	    }
		
		
		public void removeVoteOptionsContentByQueId(Long voteQueContentId)
	    {
			HibernateTemplate templ = this.getHibernateTemplate();
			List list = getSession().createQuery(FIND_VOTE_OPTIONS_CONTENT)
				.setLong(0,voteQueContentId.longValue())
				.list();

			if(list != null && list.size() > 0){
				Iterator listIterator=list.iterator();
		    	while (listIterator.hasNext())
		    	{
		    		VoteOptsContent voteOptsContent=(VoteOptsContent)listIterator.next();
					this.getSession().setFlushMode(FlushMode.AUTO);
		    		templ.delete(voteOptsContent);
		    	}
			}
	    }
				
		
		public void removeVoteOptionsContent(VoteOptsContent voteOptsContent)
	    {
			this.getSession().setFlushMode(FlushMode.AUTO);
	        this.getHibernateTemplate().delete(voteOptsContent);
	    }
		
		 public void flush()
	    {
	        this.getHibernateTemplate().flush();
	    }
	} 