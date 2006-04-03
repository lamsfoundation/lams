/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.tool.dao.hibernate;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.dao.IToolDAO;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


/**
 * 
 * @author Jacky Fang 8/02/2005
 * @author Ozgur Demirtas 24/06/2005
 * 
 */
public class ToolDAO extends HibernateDaoSupport implements IToolDAO
{
	private static final String FIND_ALL = "from obj in class " + Tool.class.getName();
	private static final String LOAD_TOOL_BY_SIG = "from tool in class Tool where tool.toolSignature=:toolSignature";
	
	
    /**
     * @see org.lamsfoundation.lams.tool.dao.IToolDAO#getToolByID(java.lang.Long)
     */
    public Tool getToolByID(Long toolID)
    {
        return (Tool)getHibernateTemplate().get(Tool.class,toolID);
    }
   
    public List getAllTools(){    	
    	return this.getHibernateTemplate().find(FIND_ALL);
    }
    
    public Tool getToolBySignature(final String toolSignature)
    {
        return (Tool) getHibernateTemplate().execute(new HibernateCallback()
         {
             public Object doInHibernate(Session session) throws HibernateException
             {
                 return session.createQuery(LOAD_TOOL_BY_SIG)
                               .setString("toolSignature",toolSignature)
                               .uniqueResult();
             }
         });
    }

    public long getToolDefaultContentIdBySignature(final String toolSignature)
    {
    	Tool tool= (Tool) getHibernateTemplate().execute(new HibernateCallback()
         {
             public Object doInHibernate(Session session) throws HibernateException
             {
                 return session.createQuery(LOAD_TOOL_BY_SIG)
                               .setString("toolSignature",toolSignature)
                               .uniqueResult();
             }
         });
        
        if (tool != null)
        	return tool.getDefaultToolContentId();
        else
        	return 0;
    }


}
