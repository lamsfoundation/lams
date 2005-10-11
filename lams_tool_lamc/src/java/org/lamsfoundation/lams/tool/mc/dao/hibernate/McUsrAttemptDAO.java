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

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.mc.McUsrAttempt;
import org.lamsfoundation.lams.tool.mc.dao.IMcUsrAttemptDAO;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;




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
		
		
		public void removeMcUsrAttempt(McUsrAttempt mcUsrAttempt)
	    {
	        this.getHibernateTemplate().delete(mcUsrAttempt);
	    }
	 	
} 