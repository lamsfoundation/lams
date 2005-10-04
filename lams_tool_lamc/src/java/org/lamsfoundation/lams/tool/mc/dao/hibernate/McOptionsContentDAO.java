/*
 * Created on 15/03/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.mc.dao.hibernate;

import java.util.List;

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
		    String query = "from McOptionsContent as mco where mco.mcContentId = ?";
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
		
	 	public McOptionsContent getMcOptionById(long mcQueOptionId)
	 	{
	 		return (McOptionsContent) this.getHibernateTemplate().load(McOptionsContent.class, new Long(mcQueOptionId));
	 	}
	 	
		public void createOptionsContent(McOptionsContent mcOptionsContent) 
	    {
	    	this.getHibernateTemplate().save(mcOptionsContent);
	    }
		
		public void removeOptionsContent(long mcQueOptionId) 
	    {
			McOptionsContent mcQueContent= (McOptionsContent) this.getHibernateTemplate().load(McOptionsContent.class, new Long(mcQueOptionId));
	    	this.getHibernateTemplate().delete(mcQueContent);
	    }
} 