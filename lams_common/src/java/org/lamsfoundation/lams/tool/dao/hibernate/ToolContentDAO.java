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

import org.lamsfoundation.lams.tool.ToolContent;
import org.lamsfoundation.lams.tool.dao.IToolContentDAO;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


/**
 * 
 * @author Jacky Fang 8/02/2005
 * 
 */
public class ToolContentDAO extends HibernateDaoSupport implements IToolContentDAO
{

    /**
     * 
     */
    public ToolContentDAO()
    {
        super();
    }

    /**
     * @see org.lamsfoundation.lams.tool.dao.IToolContentDAO#saveToolContent(org.lamsfoundation.lams.tool.ToolContent)
     */
    public void saveToolContent(ToolContent toolContent)
    {
        getHibernateTemplate().save(toolContent);
    }

}
