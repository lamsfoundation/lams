/*
 * Created on Dec 6, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learningdesign.dao.hibernate;

import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.type.Type;

import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.learningdesign.dao.ITransitionDAO;

/**
 * @author MMINHAS
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TransitionDAO extends BaseDAO implements ITransitionDAO {

	private static final String TABLENAME ="lams_learning_transition";
	private static final String FIND_BY_TO_ACTIVITY = "from " + TABLENAME +" in class " + Transition.class.getName()+ " where to_activity_id =?";
	private static final String FIND_BY_FROM_ACTIVITY = "from " + TABLENAME +" in class " + Transition.class.getName()+ " where from_activity_id =?";
	
	
	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.learningdesign.dao.ITransitionDAO#getTransitionById(java.lang.Integer)
	 */
	public List getTransitionById(Integer ID) {		
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.learningdesign.dao.ITransitionDAO#getTransitionByTransitionID(java.lang.Long)
	 */
	public Transition getTransitionByTransitionID(Long transitionID) {
		return (Transition)this.getHibernateTemplate().get(Transition.class,transitionID);
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.learningdesign.dao.ITransitionDAO#getTransitionByToActivityID(java.lang.Long)
	 */
	public List getTransitionByToActivityID(Long toActivityID) {		
		List list = this.getHibernateTemplate().find(FIND_BY_TO_ACTIVITY,new Object[]{toActivityID}, new Type[]{Hibernate.LONG});
		return list;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.learningdesign.dao.ITransitionDAO#getTransitionByfromActivityID(java.lang.Long)
	 */
	public List getTransitionByfromActivityID(Long fromActivityID) {
		List list = this.getHibernateTemplate().find(FIND_BY_FROM_ACTIVITY,new Object[]{fromActivityID}, new Type[]{Hibernate.LONG});
		return list;
	}

}
