/*
 * Created on 15/03/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.mc.dao.hibernate;

import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.mc.McQueContent;
import org.lamsfoundation.lams.tool.mc.dao.IMcQueContentDAO;
import org.springframework.orm.hibernate.HibernateCallback;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;




/**
 * @author ozgurd
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class McQueContentDAO extends HibernateDaoSupport implements IMcQueContentDAO {
	 	static Logger logger = Logger.getLogger(McQueContentDAO.class.getName());
	 	private static final String LOAD_QUESTION_CONTENT_BY_CONTENT_ID = "from mcQueContent in class McQueContent where mcQueContent.mcContentId=:mcContentId";
	 	
	 	
	 	/** @see org.lamsfoundation.lams.tool.mc.dao.IMcContentDAO#getMcQueContentByUID(java.lang.Long) */
		public McQueContent getMcQueContentByUID(Long uid)
		{
			 return (McQueContent) this.getHibernateTemplate()
	         .get(McQueContent.class, uid);
		}
		
		/** @see org.lamsfoundation.lams.tool.mc.dao.IMcContentDAO#findMcQueContentById(java.lang.Long) */
		public McQueContent findMcQueContentById(Long mcQueContentId)
		{
		    String query = "from McContent as mc where mc.mcContentId = ?";
			List content = getHibernateTemplate().find(query,mcQueContentId);
				
			if(content!=null && content.size() == 0)
			{			
				return null;
			}
			else
			{
				return (McQueContent)content.get(0);
			}
		
		}
		
	 	public McQueContent getToolDefaultQuestionContent(final long mcContentId)
	    {
	        return (McQueContent) getHibernateTemplate().execute(new HibernateCallback()
	         {
	             public Object doInHibernate(Session session) throws HibernateException
	             {
	                 return session.createQuery(LOAD_QUESTION_CONTENT_BY_CONTENT_ID)
	                               .setLong("mcContentId", mcContentId)
	                               .uniqueResult();
	             }
	         });
	    }

	 	
	 	public McQueContent getMcQueById(long mcQueContentId)
	 	{
	 		return (McQueContent) this.getHibernateTemplate().load(McQueContent.class, new Long(mcQueContentId));
	 	}
	 	
		public void createQueContent(McQueContent queContent) 
	    {
	    	this.getHibernateTemplate().save(queContent);
	    }
		
		public void removeQueContent(long mcQueContentId) 
	    {
			McQueContent mcQueContent= (McQueContent) this.getHibernateTemplate().load(McQueContent.class, new Long(mcQueContentId));
	    	this.getHibernateTemplate().delete(mcQueContent);
	    }
} 