/*
 * Created on Dec 6, 2004
 */
package org.lamsfoundation.lams.learningdesign.dao;

import org.lamsfoundation.lams.learningdesign.Grouping;

/**
 * @author manpreet
 */
public interface IGroupingDAO extends IBaseDAO {
	
	/**
	 * @param groupingID
	 * @return Grouping populated Grouping object
	 */
	public Grouping getGroupingById(Long groupingID);	

}
