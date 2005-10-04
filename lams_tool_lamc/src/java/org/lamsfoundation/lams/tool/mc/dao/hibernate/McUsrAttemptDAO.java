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
import org.lamsfoundation.lams.tool.mc.McUsrAttempt;
import org.lamsfoundation.lams.tool.mc.dao.IMcUsrAttemptDAO;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;




/**
 * @author ozgurd
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class McUsrAttemptDAO extends HibernateDaoSupport implements IMcUsrAttemptDAO {
	 	static Logger logger = Logger.getLogger(McUsrAttemptDAO.class.getName());
	 	
	 	public McUsrAttempt getMcUserAttemptByUID(Long uid)
		{
			 return (McUsrAttempt) this.getHibernateTemplate()
	         .get(McUsrAttempt.class, uid);
		}
		
		public McUsrAttempt findMcUsrAttemptById(Long attemptId)
		{
		    String query = "from McUsrAttempt as mca where mca.attemptId = ?";
			List content = getHibernateTemplate().find(query,attemptId);
				
			if(content!=null && content.size() == 0)
			{			
				return null;
			}
			else
			{
				return (McUsrAttempt)content.get(0);
			}
		
		}
		
		
		public void saveMcUsrAttempt(McUsrAttempt mcUsrAttempt)
	    {
	    	this.getHibernateTemplate().save(mcUsrAttempt);
	    }
	    
		public void updateMcUsrAttempt(McUsrAttempt mcUsrAttempt)
	    {
	    	this.getHibernateTemplate().update(mcUsrAttempt);
	    }
		
		public void removeMcUsrAttemptByUID(Long uid)
	    {
			McUsrAttempt mca = (McUsrAttempt)getHibernateTemplate().get(McUsrAttempt.class, uid);
	    	this.getHibernateTemplate().delete(mca);
	    }
		
		
		public void removeMcUsrAttempt(Long attemptId)
	    {
	       String query = "from McUsrAttempt as mca where mca.attemptId=";
	       StringBuffer sb = new StringBuffer(query);
	       sb.append(attemptId.longValue());
	       String queryString = sb.toString();
	          
	       this.getHibernateTemplate().delete(queryString);
	    }
		
		
		public void removeMcUsrAttemptById(Long attemptId)
	    {
	        String query = "from mca in class org.lamsfoundation.lams.tool.mc.McUsrAttempt"
	        + " where mca.attemptId = ?";
	        this.getHibernateTemplate().delete(query,attemptId,Hibernate.LONG);
	    }
		
		public void removeMcUsrAttempt(McUsrAttempt mcUsrAttempt)
	    {
	        this.getHibernateTemplate().delete(mcUsrAttempt);
	    }
	 	
} 