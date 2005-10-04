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
import org.lamsfoundation.lams.tool.mc.McOptionsContent;
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
	 	
	 	public McOptionsContentDAO getMcOptionsContentByUID(Long uid)
		{
			 return (McOptionsContentDAO) this.getHibernateTemplate()
	         .get(McOptionsContentDAO.class, uid);
		}
		
		public McOptionsContent findMcOptionsContentById(Long mcQueOptionId)
		{
		    String query = "from McOptionsContent as mco where mco.mcQueOptionId = ?";
			List content = getHibernateTemplate().find(query,mcQueOptionId);
				
			if(content!=null && content.size() == 0)
			{			
				return null;
			}
			else
			{
				return (McOptionsContent)content.get(0);
			}
		
		}
		
		
		public void saveMcOptionsContent(McOptionsContent mcOptionsContent)
	    {
	    	this.getHibernateTemplate().save(mcOptionsContent);
	    }
	    
		public void updateMcOptionsContent(McOptionsContent mcOptionsContent)
	    {
	    	this.getHibernateTemplate().update(mcOptionsContent);
	    }
		
		
		public void removeMcOptionsContentByUID(Long uid)
	    {
			McOptionsContent mco = (McOptionsContent)getHibernateTemplate().get(McOptionsContent.class, uid);
	    	this.getHibernateTemplate().delete(mco);
	    }
		
		
		public void removeMcOptionsContent(Long mcQueOptionId)
	    {
	       String query = "from McOptionsContent as mco where mco.mcQueOptionId=";
	       StringBuffer sb = new StringBuffer(query);
	       sb.append(mcQueOptionId.longValue());
	       String queryString = sb.toString();
	          
	       this.getHibernateTemplate().delete(queryString);
	    }
	
		public void removeMcOptionsContentById(Long mcQueOptionId)
	    {
	        String query = "from mco in class org.lamsfoundation.lams.tool.mc.McOptionsContent"
	        + " where mcq.mcQueOptionId = ?";
	        this.getHibernateTemplate().delete(query,mcQueOptionId,Hibernate.LONG);
	    }
		
		
		public void removeMcOptionsContent(McOptionsContent mcOptionsContent)
	    {
	        this.getHibernateTemplate().delete(mcOptionsContent);
	    }
		
} 