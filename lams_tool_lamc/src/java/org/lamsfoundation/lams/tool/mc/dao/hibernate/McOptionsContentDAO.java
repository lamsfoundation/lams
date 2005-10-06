/*
 * Created on 15/03/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.mc.dao.hibernate;

import java.util.List;

import net.sf.hibernate.Hibernate;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.mc.McOptsContent;
import org.lamsfoundation.lams.tool.mc.dao.IMcOptionsContentDAO;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;


/**
 * @author ozgurd
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class McOptionsContentDAO extends HibernateDaoSupport implements IMcOptionsContentDAO {
	 	static Logger logger = Logger.getLogger(McOptionsContentDAO.class.getName());
	 	
	 	public McOptsContent getMcOptionsContentByUID(Long uid)
		{
			 return (McOptsContent) this.getHibernateTemplate()
	         .get(McOptsContent.class, uid);
		}
		
		public McOptsContent findMcOptionsContentById(Long mcQueOptionId)
		{
		    String query = "from McOptsContent as mco where mco.mcQueOptionId = ?";
			List content = getHibernateTemplate().find(query,mcQueOptionId);
				
			if(content!=null && content.size() == 0)
			{			
				return null;
			}
			else
			{
				return (McOptsContent)content.get(0);
			}
		
		}
		
		
		public void saveMcOptionsContent(McOptsContent mcOptsContent)
	    {
	    	this.getHibernateTemplate().save(mcOptsContent);
	    }
	    
		public void updateMcOptionsContent(McOptsContent mcOptsContent)
	    {
	    	this.getHibernateTemplate().update(mcOptsContent);
	    }
		
		
		public void removeMcOptionsContentByUID(Long uid)
	    {
			McOptsContent mco = (McOptsContent)getHibernateTemplate().get(McOptsContent.class, uid);
	    	this.getHibernateTemplate().delete(mco);
	    }
		
		
		public void removeMcOptionsContent(Long mcQueOptionId)
	    {
	       String query = "from McOptsContent as mco where mco.mcQueOptionId=";
	       StringBuffer sb = new StringBuffer(query);
	       sb.append(mcQueOptionId.longValue());
	       String queryString = sb.toString();
	          
	       this.getHibernateTemplate().delete(queryString);
	    }
	
		public void removeMcOptionsContentById(Long mcQueOptionId)
	    {
	        String query = "from mco in class org.lamsfoundation.lams.tool.mc.McOptsContent"
	        + " where mcq.mcQueOptionId = ?";
	        this.getHibernateTemplate().delete(query,mcQueOptionId,Hibernate.LONG);
	    }
		
		
		public void removeMcOptionsContent(McOptsContent mcOptsContent)
	    {
	        this.getHibernateTemplate().delete(mcOptsContent);
	    }
		
		 public void flush()
	    {
	        this.getHibernateTemplate().flush();
	    }
} 