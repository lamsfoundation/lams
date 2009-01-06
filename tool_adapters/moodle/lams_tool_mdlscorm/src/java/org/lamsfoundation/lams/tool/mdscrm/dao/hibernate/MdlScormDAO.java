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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */

package org.lamsfoundation.lams.tool.mdscrm.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.mdscrm.dao.IMdlScormDAO;
import org.lamsfoundation.lams.tool.mdscrm.model.MdlScorm;

/**
 * DAO for accessing the MdlScorm objects - Hibernate specific code.
 */
public class MdlScormDAO extends BaseDAO implements IMdlScormDAO {

    private static final String FIND_SCORM_BY_CONTENTID = "from MdlScorm mdlScorm where mdlScorm.toolContentId=?";

    public MdlScorm getByContentId(Long toolContentId) {
	List list = getHibernateTemplate().find(FIND_SCORM_BY_CONTENTID, toolContentId);
	if (list != null && list.size() > 0)
	    return (MdlScorm) list.get(0);
	else
	    return null;
    }

    public void saveOrUpdate(MdlScorm mdlScorm) {
	this.getHibernateTemplate().saveOrUpdate(mdlScorm);
	this.getHibernateTemplate().flush();
    }

}
