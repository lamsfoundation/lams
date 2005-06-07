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

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.type.Type;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.learningdesign.dao.ITransitionDAO;

/**
 * @author Manpreet Minhas
 */
public class TransitionDAO extends BaseDAO implements ITransitionDAO {

	private static final String TABLENAME ="lams_learning_transition";
	private static final String FIND_BY_TO_ACTIVITY = "from " + TABLENAME +" in class " + Transition.class.getName()+ " where to_activity_id =?";
	private static final String FIND_BY_FROM_ACTIVITY = "from " + TABLENAME +" in class " + Transition.class.getName()+ " where from_activity_id =?";
	private static final String FIND_BY_LEARNING_DESIGN_ID = "from " + TABLENAME +" in class " + Transition.class.getName()+
															" where learning_design_id=?";
	
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.learningdesign.dao.ITransitionDAO#getTransitionByTransitionID(java.lang.Long)
	 */
	public Transition getTransitionByTransitionID(Long transitionID) {
		return (Transition)this.getHibernateTemplate().get(Transition.class,transitionID);
	}

	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.learningdesign.dao.ITransitionDAO#getTransitionByToActivityID(java.lang.Long)
	 */
	public Transition getTransitionByToActivityID(Long toActivityID) {		
		List list = this.getHibernateTemplate().find(FIND_BY_TO_ACTIVITY,new Object[]{toActivityID}, new Type[]{Hibernate.LONG});
		return (Transition)list.get(0);
	}

	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.learningdesign.dao.ITransitionDAO#getTransitionByfromActivityID(java.lang.Long)
	 */
	public Transition getTransitionByfromActivityID(Long fromActivityID) {
		List list = this.getHibernateTemplate().find(FIND_BY_FROM_ACTIVITY,new Object[]{fromActivityID}, new Type[]{Hibernate.LONG});
		if(list.size()!=0)
			return (Transition)list.get(0);
		else
			return null;
	}
	
	public List getTransitionsByLearningDesignID(Long learningDesignID){
		List list = this.getHibernateTemplate().find(FIND_BY_LEARNING_DESIGN_ID, new Object[]{learningDesignID}, new Type[]{Hibernate.LONG});
		return list;
	}
	public Activity getNextActivity(Long fromActivityID){
		Transition transition = getTransitionByfromActivityID(fromActivityID);
		if(transition!=null)
			return transition.getToActivity();
		else
			return null;
	}	
}
