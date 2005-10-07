/*
 * Created on 15/03/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.mc.dao.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.lamsfoundation.lams.tool.mc.McUsrAttempt;
import org.lamsfoundation.lams.tool.mc.dao.IMcUsrAttemptDAO;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;




/**
 * @author ozgurd
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class McUsrAttemptDAO extends HibernateDaoSupport implements IMcUsrAttemptDAO {
	 	static Logger logger = Logger.getLogger(McUsrAttemptDAO.class.getName());
	 	
	 	private static final String FIND_USR_ATTEMPT = "from " + McUsrAttempt.class.getName() + " as mca where attempt_id=?";
	 	
	 	public McUsrAttempt getMcUserAttemptByUID(Long uid)
		{
			 return (McUsrAttempt) this.getHibernateTemplate()
	         .get(McUsrAttempt.class, uid);
		}
		
		public McUsrAttempt findMcUsrAttemptById(Long attemptId)
		{
		    String query = "from McUsrAttempt as mca where mca.attemptId = ?";

		    return (McUsrAttempt) getSession().createQuery(query)
			.setLong(0,attemptId.longValue())
			.uniqueResult();
		
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
		
		
		public void removeMcUsrAttemptById(Long attemptId)
	    {
			HibernateTemplate templ = this.getHibernateTemplate();
			if ( attemptId != null) {
				List list = getSession().createQuery(FIND_USR_ATTEMPT)
					.setLong(0,attemptId.longValue())
					.list();
				
				if(list != null && list.size() > 0){
					McUsrAttempt mcu = (McUsrAttempt) list.get(0);
					this.getSession().setFlushMode(FlushMode.AUTO);
					templ.delete(mcu);
					templ.flush();
				}
			}
	    }
		
		public void removeMcUsrAttempt(McUsrAttempt mcUsrAttempt)
	    {
	        this.getHibernateTemplate().delete(mcUsrAttempt);
	    }
	 	
} 