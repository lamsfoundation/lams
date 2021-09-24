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
import org.lamsfoundation.lams.learningdesign.ToolActivity;

/**
 * @author Manpreet Minhas
 *
 */
public interface IActivityDAO extends IBaseDAO {

    /**
     * Returns an object that may be assigned to Activity.
     * This must return a true Activity object, rather than
     * the Hibernate CGLIB proxy, or other code gets too difficult.
     * <p>
     * If a new type of activity is added, then this method must be
     * updated.
     * 
     * @param activityId
     *            The activityId of the activity
     * @return Activity populated Activity object
     * @throws DataRetrievalFailureException
     *             if it can't work out the correct Activity class.
     */
    public Activity getActivityByActivityId(Long activityId);

    /**
     * Returns an object that may be assigned to the Class clasz.
     * Returns a true Activity object, rather than the Hibernate CGLIB proxy.
     * 
     * @param activityId
     *            The activityId of the activity
     * @param clasz
     *            The expected class - must be a subclass of Activity
     * @return Activity populated object
     */
    public Activity getActivityByActivityId(Long activityId, Class clasz);

    /**
     * Returns an object that may be assigned to Activity, based
     * on the UI id set by Authoring
     * <p>
     * This must return a true Activity object, rather than
     * the Hibernate CGLIB proxy, or other code gets too difficult.
     * <p>
     * 
     * @param uiID
     *            The internal id (Authoring generated) of the activity
     *            being looked for
     * @param design
     *            The learning_design_id of the design
     *            whose first activity we are fetching
     * @return Activity The populated activity object matching the specified criteria
     */
    public Activity getActivityByUIID(Integer uiID, LearningDesign design);

    /**
     * @param parentActivityId
     *            The activityId of the parent activity
     * @return Activity populated Activity object
     */
    public List getActivitiesByParentActivityId(Long parentActivityId);

    /**
     * @param learningDesignId
     *            The id of the learningDesign
     * @return List of all the activities
     */
    public List<Activity> getActivitiesByLearningDesignId(Long learningDesignId);

    /**
     * Get all the grouping activities for this learning design.
     * 
     * @param learningDesignId
     *            The id of the learningDesign
     * @return List of all the activities
     */
    public List getGroupingActivitiesByLearningDesignId(Long learningDesignId);

    /**
     * Returns a list of activities associated with the LearningLibrary.
     * It only returns the template activities and not all activities
     * with learning_library_id =libraryID. Template activity is one which
     * is not a part of any learning Design but is used as a blue-print/
     * template for creating new activities. Single LearningLibrary
     * in a normal case has one template activity.
     * 
     * @param libraryID
     *            The learning_library_id of the LearningLibrary
     * @return List List of activities that belong to
     *         the given LearningLibrary
     */
    public List getActivitiesByLibraryID(Long libraryID);

    /**
     * Returns the template activity that belongs to the LearningLibrary.
     * A template activity is one where learning_library_id=libraryID AND
     * learning_design_id IS NULL. This is what distinguishes between a normal
     * activity and a template activity.
     * If a template doesnt exist for that particular LearningLibrary then
     * and error messaged is logged (an exception is not thrown).
     * 
     * @param libraryID
     *            The learning_library_id of the LearningLibrary
     * @return Activity the template activity for the learning library.
     */
    public Activity getTemplateActivityByLibraryID(Long libraryID);

    /**
     * Returns ToolActivity by the specified toolContentId
     * 
     * @param toolContentId
     * @throws NonUniqueResultException
     *             if there is more than one matching result
     * @return
     */
    ToolActivity getToolActivityByToolContentId(Long toolContentId);

}
