/*
 * Created on 15/03/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.mc.dao.hibernate;

import java.util.List;

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
		
	 		 	
	 	public McUsrAttempt getMcUsrAttemptById(long attemptId)
	 	{
	 		return (McUsrAttempt) this.getHibernateTemplate().load(McUsrAttempt.class, new Long(attemptId));
	 	}
	 	
		public void createUsrAttempt(McUsrAttempt mcUsrAttempt) 
	    {
	    	this.getHibernateTemplate().save(mcUsrAttempt);
	    }
		
		public void removeUsrAttempt(long attemptId) 
	    {
			McUsrAttempt mcUsrAttempt= (McUsrAttempt) this.getHibernateTemplate().load(McUsrAttempt.class, new Long(attemptId));
	    	this.getHibernateTemplate().delete(mcUsrAttempt);
	    }
} 