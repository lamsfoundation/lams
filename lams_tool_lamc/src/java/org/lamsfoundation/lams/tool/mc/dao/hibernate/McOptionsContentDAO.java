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
import org.lamsfoundation.lams.tool.mc.McOptsContent;
import org.lamsfoundation.lams.tool.mc.dao.IMcOptionsContentDAO;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;



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
		
				
		
		public void removeMcOptionsContent(McOptsContent mcOptsContent)
	    {
	        this.getHibernateTemplate().delete(mcOptsContent);
	    }
		
		 public void flush()
	    {
	        this.getHibernateTemplate().flush();
	    }
} 