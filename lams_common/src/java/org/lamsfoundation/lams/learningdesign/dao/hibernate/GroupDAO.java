/*
 * Created on Dec 6, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learningdesign.dao.hibernate;

import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.dao.IGroupDAO;

/**
 * @author MMINHAS
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GroupDAO extends BaseDAO implements IGroupDAO {

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.IGroupDAO#getGroupById(java.lang.Long)
	 */
	public Group getGroupById(Long groupID) {
		return (Group)super.find(Group.class,groupID);
	}

}
