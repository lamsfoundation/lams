/*
 * Created on Dec 6, 2004
 */
package org.lamsfoundation.lams.learningdesign.dao;

import org.lamsfoundation.lams.learningdesign.Group;

/**
 * @author manpreet 
 */
public interface IGroupDAO extends IBaseDAO {
	
	/**
	 * @param groupID
	 * @return Group populated Group object
	 */
	public Group getGroupById(Long groupID);
	
	public void saveGroup(Group group);

	public void deleteGroup(Group group);
	
}
