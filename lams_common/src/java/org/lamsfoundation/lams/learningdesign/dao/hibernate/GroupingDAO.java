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
package org.lamsfoundation.lams.learningdesign.dao.hibernate;

import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.learningdesign.ChosenGrouping;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.RandomGrouping;
import org.lamsfoundation.lams.learningdesign.dao.IGroupingDAO;
import org.springframework.dao.DataRetrievalFailureException;

/**
 * @author Manpreet Minhas
 */
public class GroupingDAO extends BaseDAO implements IGroupingDAO {
	
	private static final String FIND_BY_UI_ID ="from "+Grouping.class.getName()+" g where g.groupingUIID=:UIID";

	/**
	 * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.IGroupingDAO#getGroupingById(java.lang.Long)
	 */
	public Grouping getGroupingById(Long groupingID) {
		Grouping grouping = (Grouping)super.find(Grouping.class,groupingID);
		return getNonCGLibGrouping(grouping);
	}

	/**
	 * @see org.lamsfoundation.lams.learningdesign.dao.IGroupingDAO#getGroupingByUIID(java.lang.Integer)
	 */
	public Grouping getGroupingByUIID(Integer groupingUIID) {
		if ( groupingUIID != null ) {
			Grouping grouping = (Grouping) getSession()
									.createQuery(FIND_BY_UI_ID)
									.setInteger("UIID",groupingUIID.intValue())
									.uniqueResult();
			return getNonCGLibGrouping(grouping);
		} 
		return null;
	}

	/** we must return the real grouping, not a Hibernate proxy. So relook
	* it up. This should be quick as it should be in the cache.
	*/
	private Grouping getNonCGLibGrouping(Grouping grouping) {
		if ( grouping != null ) {
			if ( grouping.isRandomGrouping() ) {
				return (Grouping)super.find(RandomGrouping.class,grouping.getGroupingId());
			} else if ( grouping.isChosenGrouping() ) {
				return (Grouping)super.find(ChosenGrouping.class,grouping.getGroupingId());
			}
			throw new DataRetrievalFailureException("Unable to get grouping as the grouping type is unknown or missing. Grouping object is "+grouping);
		}
		return null;
	}


}
