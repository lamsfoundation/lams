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
import org.lamsfoundation.lams.tool.mc.McQueContent;
import org.lamsfoundation.lams.tool.mc.dao.IMcQueContentDAO;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;




/**
 * @author ozgurd
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class McQueContentDAO extends HibernateDaoSupport implements IMcQueContentDAO {
	 	static Logger logger = Logger.getLogger(McQueContentDAO.class.getName());
	 	
	 	private static final String LOAD_QUESTION_CONTENT_BY_CONTENT_ID = "from mcQueContent in class McQueContent where mcQueContent.mcContentId=:mcContentId";
	 	
	 	private static final String LOAD_QUESTION_CONTENT_BY_QUESTION_TEXT = "from mcQueContent in class McQueContent where mcQueContent.question=:question and mcQueContent.mcContentId=:mcContentUid";
	 		 	
	 	
	 	public McQueContent getMcQueContentByUID(Long uid)
		{
			 return (McQueContent) this.getHibernateTemplate()
	         .get(McQueContent.class, uid);
		}
		
		
	 	public McQueContent getToolDefaultQuestionContent(final long mcContentId)
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
			List list = getSession().createQuery(LOAD_QUESTION_CONTENT_BY_CONTENT_ID)
				.setLong("mcContentId", mcContentId)
				.list();
			
			if(list != null && list.size() > 0){
				McQueContent mcq = (McQueContent) list.get(0);
				return mcq;
			}
			return null;
	    }
	 	
	 	
	 	public McQueContent getQuestionContentByQuestionText(final String question, final Long mcContentUid)
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
			List list = getSession().createQuery(LOAD_QUESTION_CONTENT_BY_QUESTION_TEXT)
				.setString("question", question)
				.setLong("mcContentUid", mcContentUid.longValue())				
				.list();
			
			if(list != null && list.size() > 0){
				McQueContent mcq = (McQueContent) list.get(0);
				return mcq;
			}
			return null;
	    }
	 	
	 	
	 	public void saveMcQueContent(McQueContent mcQueContent)
	    {
	    	this.getHibernateTemplate().save(mcQueContent);
	    }
	    
		public void updateMcQueContent(McQueContent mcQueContent)
	    {
	    	this.getHibernateTemplate().update(mcQueContent);
	    }
		
		public void saveOrUpdateMcQueContent(McQueContent mcQueContent)
	    {
	    	this.getHibernateTemplate().saveOrUpdate(mcQueContent);
	    }
		
		public void removeMcQueContentByUID(Long uid)
	    {
			McQueContent mcq = (McQueContent)getHibernateTemplate().get(McQueContent.class, uid);
			this.getSession().setFlushMode(FlushMode.AUTO);
	    	this.getHibernateTemplate().delete(mcq);
	    }
		
		
		public void removeMcQueContent(McQueContent mcQueContent)
	    {
			this.getSession().setFlushMode(FlushMode.AUTO);
	        this.getHibernateTemplate().delete(mcQueContent);
	    }
		
		 public void flush()
	    {
	        this.getHibernateTemplate().flush();
	    }
		
} 