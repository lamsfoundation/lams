/****************************************************************
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
 * ****************************************************************
 */
package org.lamsfoundation.lams.learningdesign.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.dao.IGroupingDAO;

/**
 * @author Manpreet Minhas
 */
public class GroupingDAO extends BaseDAO implements IGroupingDAO {
	
	private static final String TABLENAME ="lams_grouping";

	private static final String FIND_BY_UI_ID ="from " + TABLENAME +
											   " in class " + Grouping.class.getName() +
											   " where grouping_ui_id=?";

	/**
	 * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.IGroupingDAO#getGroupingById(java.lang.Long)
	 */
	public Grouping getGroupingById(Long groupingID) {
		return (Grouping)super.find(Grouping.class,groupingID);
	}

	/**
	 * @see org.lamsfoundation.lams.learningdesign.dao.IGroupingDAO#getGroupingByUIID(java.lang.Integer)
	 */
	public Grouping getGroupingByUIID(Integer groupingUIID) {
		List list = this.getHibernateTemplate().find(FIND_BY_UI_ID,groupingUIID);	
		if(list!=null && list.size()>0)
			return (Grouping)list.get(0);
		else
			return null;
	}

}
