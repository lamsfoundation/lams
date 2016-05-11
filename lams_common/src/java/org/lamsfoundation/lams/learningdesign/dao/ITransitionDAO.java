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

package org.lamsfoundation.lams.learningdesign.dao;

import java.util.List;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.Transition;

/**
 * @author Manpreet Minhas
 */
public interface ITransitionDAO extends IBaseDAO {

    /**
     * @param transitionID
     *            The transitionID of the required Transition
     * @return Returns the Transition object corresponding to the transitionID
     */
    public Transition getTransitionByTransitionID(Long transitionID);

    /**
     * @param toActivityID
     *            The to_activity_id of the required Transition
     * @return Returns the list of Transition objects where to_activity_id = activityID
     */
    public Transition getTransitionByToActivityID(Long toActivityID);

    /**
     * @param fromActivityID
     *            The from_activity_id of the required Transition
     * @return Returns the list of Transition objects where from_activity_id = activityID
     */
    public Transition getTransitionByfromActivityID(Long fromActivityID);

    public List getTransitionsByLearningDesignID(Long learningDesignID);

    /**
     * This method returns the next activity corresponding to the given
     * activityID.
     * 
     * @param fromActivityID
     *            The activityID of the activity, whose
     *            next activity has to be fetched
     * @return Activity The activity that comes after fromActivityID
     */
    public Activity getNextActivity(Long fromActivityID);

    public Transition getTransitionByUUID(Integer transitionUUID, LearningDesign learningDesign);

}
