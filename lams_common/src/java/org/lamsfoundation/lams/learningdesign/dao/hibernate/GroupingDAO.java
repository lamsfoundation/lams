/*
 * Created on Dec 6, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learningdesign.dao.hibernate;

import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.dao.IGroupingDAO;

/**
 * @author MMINHAS
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GroupingDAO extends BaseDAO implements IGroupingDAO {

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.IGroupingDAO#getGroupingById(java.lang.Long)
	 */
	public Grouping getGroupingById(Long groupingID) {
		return (Grouping)super.find(Grouping.class,groupingID);
	}

}
