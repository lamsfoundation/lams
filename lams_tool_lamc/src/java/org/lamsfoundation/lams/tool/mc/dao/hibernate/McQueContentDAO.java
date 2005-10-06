/*
 * Created on 15/03/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.mc.dao.hibernate;

import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.mc.McContent;
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
	 	private static final String GET_QUESTION_IDS_FOR_CONTENT = "select mcQueContent.mcQueContentId from McQueContent mcQueContent where mcQueContent.mcContentId = :mc";
	 	
	 	
	 	public McQueContent getMcQueContentByUID(Long uid)
		{
			 return (McQueContent) this.getHibernateTemplate()
	         .get(McQueContent.class, uid);
		}
		
		public McQueContent findMcQueContentById(Long mcQueContentId)
		{
		    String query = "from McQueContent as mcq where mcq.mcQueContentId = ?";
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

	 	
	 	public List getQuestionIndsForContent(McContent mc)
	    {
	    	   
			  List listDefaultQuestionIds=(getHibernateTemplate().findByNamedParam(GET_QUESTION_IDS_FOR_CONTENT,
	                "mc",
	                mc));
			  
			  return listDefaultQuestionIds;
	    }
	 	
	 	
	 	public void saveMcQueContent(McQueContent mcQueContent)
	    {
	    	this.getHibernateTemplate().save(mcQueContent);
	    }
	    
		public void updateMcQueContent(McQueContent mcQueContent)
	    {
	    	this.getHibernateTemplate().update(mcQueContent);
	    }
		
		public void removeMcQueContentByUID(Long uid)
	    {
			McQueContent mcq = (McQueContent)getHibernateTemplate().get(McQueContent.class, uid);
	    	this.getHibernateTemplate().delete(mcq);
	    }
		
		
		public void removeMcQueContent(Long mcQueContentId)
	    {
	       String query = "from McQueContent as mcq where mcq.mcQueContentId=";
	       StringBuffer sb = new StringBuffer(query);
	       sb.append(mcQueContentId.longValue());
	       String queryString = sb.toString();
	          
	       this.getHibernateTemplate().delete(queryString);
	    }
		
		
		public void removeMcQueContentById(Long mcQueContentId)
	    {
	        String query = "from mcq in class org.lamsfoundation.lams.tool.mc.McQueContent"
	        + " where mcq.mcQueContentId = ?";
	        this.getHibernateTemplate().delete(query,mcQueContentId,Hibernate.LONG);
	    }
		
		
		public void removeMcQueContent(McQueContent mcQueContent)
	    {
	        this.getHibernateTemplate().delete(mcQueContent);
	    }
		
		 public void flush()
	    {
	        this.getHibernateTemplate().flush();
	    }
		
} 