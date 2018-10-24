/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.learningdesign.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.learningdesign.dao.ITransitionDAO;
import org.springframework.stereotype.Repository;

/**
 * @author Manpreet Minhas
 */
@Repository
public class TransitionDAO extends LAMSBaseDAO implements ITransitionDAO {

    private static final String TABLENAME = "lams_learning_transition";
    private static final String FIND_BY_TO_ACTIVITY = "from " + TABLENAME + " in class " + Transition.class.getName()
	    + " where to_activity_id =:activity_id";
    private static final String FIND_BY_FROM_ACTIVITY = "from " + TABLENAME + " in class " + Transition.class.getName()
	    + " where from_activity_id =:activity_id";
    private static final String FIND_BY_LEARNING_DESIGN_ID = "from " + TABLENAME + " in class "
	    + Transition.class.getName() + " where learning_design_id=:ldId";
    private static final String FIND_BY_UI_ID = "from " + TABLENAME + " in class " + Transition.class.getName()
	    + " where transition_ui_id=:uiid" + " AND " + " learning_design_id=:ldId";

    /**
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.learningdesign.dao.ITransitionDAO#getTransitionByTransitionID(java.lang.Long)
     */
    @Override
    public Transition getTransitionByTransitionID(Long transitionID) {
	return (Transition) this.getSession().get(Transition.class, transitionID);
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.learningdesign.dao.ITransitionDAO#getTransitionByToActivityID(java.lang.Long)
     */
    @Override
    public Transition getTransitionByToActivityID(Long toActivityID) {
	if (toActivityID != null) {
	    Query query = getSessionFactory().getCurrentSession().createQuery(FIND_BY_TO_ACTIVITY);
	    query.setLong("activity_id", toActivityID.longValue());
	    return (Transition) query.uniqueResult();
	}
	return null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.learningdesign.dao.ITransitionDAO#getTransitionByfromActivityID(java.lang.Long)
     */
    @Override
    public Transition getTransitionByfromActivityID(Long fromActivityID) {
	if (fromActivityID != null) {
	    return (Transition) getSessionFactory().getCurrentSession().createQuery(FIND_BY_FROM_ACTIVITY)
		    .setLong("activity_id", fromActivityID.longValue()).uniqueResult();
	}
	return null;
    }

    @Override
    public List getTransitionsByLearningDesignID(Long learningDesignID) {
	if (learningDesignID != null) {
	    return getSessionFactory().getCurrentSession().createQuery(FIND_BY_LEARNING_DESIGN_ID)
		    .setLong("ldId", learningDesignID.longValue()).list();
	}
	return null;
    }

    @Override
    public Activity getNextActivity(Long fromActivityID) {
	Transition transition = getTransitionByfromActivityID(fromActivityID);
	if (transition != null) {
	    return transition.getToActivity();
	} else {
	    return null;
	}
    }

    @Override
    public Transition getTransitionByUUID(Integer transitionUUID, LearningDesign learningDesign) {
	if (transitionUUID != null && learningDesign != null) {
	    Long designID = learningDesign.getLearningDesignId();
	    Query query = getSessionFactory().getCurrentSession().createQuery(FIND_BY_UI_ID);
	    query.setInteger("uiid", transitionUUID.intValue());
	    query.setLong("ldId", designID.longValue());
	    return (Transition) query.uniqueResult();
	}
	return null;
    }
}
