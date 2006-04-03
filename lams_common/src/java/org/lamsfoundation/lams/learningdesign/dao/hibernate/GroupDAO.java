/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * =============================================================
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
package org.lamsfoundation.lams.learningdesign.dao.hibernate;

import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.dao.IGroupDAO;

/**
 * @author Manpreet Minhas
 */
public class GroupDAO extends BaseDAO implements IGroupDAO {

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.IGroupDAO#getGroupById(java.lang.Long)
	 */
	public Group getGroupById(Long groupID) {
		return (Group)super.find(Group.class,groupID);
	}
	
	public void saveGroup(Group group) {
        getHibernateTemplate().save(group);
	}
	
	public void deleteGroup(Group group) {
        getHibernateTemplate().delete(group);
	}

}
