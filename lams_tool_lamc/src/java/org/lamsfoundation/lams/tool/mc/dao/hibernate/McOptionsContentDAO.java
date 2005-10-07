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
import org.lamsfoundation.lams.tool.mc.McOptsContent;
import org.lamsfoundation.lams.tool.mc.dao.IMcOptionsContentDAO;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;



/**
 * @author ozgurd
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class McOptionsContentDAO extends HibernateDaoSupport implements IMcOptionsContentDAO {
	 	static Logger logger = Logger.getLogger(McOptionsContentDAO.class.getName());
	 	
	 	private static final String FIND_MC_OPTS_CONTENT = "from " + McOptsContent.class.getName() + " as mc where mc_que_option_id=?";
	 	
	 	public McOptsContent getMcOptionsContentByUID(Long uid)
		{
			 return (McOptsContent) this.getHibernateTemplate()
	         .get(McOptsContent.class, uid);
		}
		
		public McOptsContent findMcOptionsContentById(Long mcQueOptionId)
		{
			String query = "from McOptsContent as mco where mco.mcQueOptionId = ?";
			
			return (McOptsContent) getSession().createQuery(query)
			.setLong(0,mcQueOptionId.longValue())
			.uniqueResult();
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
		
		
		public void removeMcOptionsContentById(Long mcQueOptionId)
	    {
			HibernateTemplate templ = this.getHibernateTemplate();
			if ( mcQueOptionId != null) {
				List list = getSession().createQuery(FIND_MC_OPTS_CONTENT)
					.setLong(0,mcQueOptionId.longValue())
					.list();
				
				if(list != null && list.size() > 0){
					McOptsContent mco = (McOptsContent) list.get(0);
					this.getSession().setFlushMode(FlushMode.AUTO);
					templ.delete(mco);
					templ.flush();
				}
			}
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