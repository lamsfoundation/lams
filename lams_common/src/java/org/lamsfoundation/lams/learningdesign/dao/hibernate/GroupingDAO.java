/*
 * Created on Dec 6, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learningdesign.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.dao.IGroupingDAO;

/**
 * @author MMINHAS
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
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
		if(list!=null)
			return (Grouping)list.get(0);
		else
			return null;
	}

}
