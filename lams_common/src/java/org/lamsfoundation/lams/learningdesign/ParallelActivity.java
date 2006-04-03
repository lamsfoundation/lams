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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.strategy.ParallelActivityStrategy;

/** 
 * @author Manpreet Minhas
 * @hibernate.class 
*/
public class ParallelActivity extends ComplexActivity implements Serializable {

    /** full constructor */
    public ParallelActivity(Long activityId, 
            Integer id, 
            String description, 
            String title, 
            Integer xcoord, 
            Integer ycoord, 
            Integer orderId, 
            Boolean defineLater, 
            java.util.Date createDateTime, 
            LearningLibrary learningLibrary, 
            Activity parentActivity,
            Activity libraryActivity,
			Integer parentUIID,
            LearningDesign learningDesign, 
            Grouping grouping, 
            Integer activityTypeId,  
            Transition transitionTo,
            Transition transitionFrom,
            String languageFile,
            Set activities) {
        super(activityId, 
                id, 
                description, 
                title, 
                xcoord, 
                ycoord, 
                orderId, 
                defineLater, 
                createDateTime, 
                learningLibrary, 
                parentActivity, 
				libraryActivity,
				parentUIID,
                learningDesign, 
                grouping, 
                activityTypeId,  
                transitionTo,
				transitionFrom,
				languageFile,
                activities);
        super.activityStrategy = new ParallelActivityStrategy(this);
    }

    /** default constructor */
    public ParallelActivity() {
        super.activityStrategy = new ParallelActivityStrategy(this);
    }

    /** minimal constructor */
    public ParallelActivity(Long activityId, 
            Boolean defineLater, 
            java.util.Date createDateTime, 
            org.lamsfoundation.lams.learningdesign.LearningLibrary learningLibrary, 
            org.lamsfoundation.lams.learningdesign.Activity parentActivity, 
            org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign, 
            org.lamsfoundation.lams.learningdesign.Grouping grouping, 
            Integer activityTypeId,  
            Transition transitionTo,
            Transition transitionFrom,
            Set activities) {
      super(activityId, 
              defineLater, 
              createDateTime, 
              learningLibrary, 
              parentActivity, 
              learningDesign, 
              grouping, 
              activityTypeId, 
              transitionTo,
			  transitionFrom,
              activities);
      super.activityStrategy = new ParallelActivityStrategy(this);
    }
    /**
     * Makes a copy of the ParallelActivity for authoring, preview and monitoring enviornment 
     * @return ParallelActivity Returns a deep-copy of the originalActivity
     */
    public Activity createCopy(){
    	
    	ParallelActivity newParallelActivity = new ParallelActivity();
    	
    	newParallelActivity.setActivityUIID(this.getActivityUIID());
    	newParallelActivity.setDescription(this.getDescription());
    	newParallelActivity.setTitle(this.getTitle());
    	newParallelActivity.setHelpText(this.getHelpText());
    	newParallelActivity.setXcoord(this.getXcoord());
    	newParallelActivity.setYcoord(this.getYcoord());
    	newParallelActivity.setActivityTypeId(this.getActivityTypeId());
    	
    	newParallelActivity.setGroupingSupportType(this.getGroupingSupportType());
    	newParallelActivity.setApplyGrouping(this.getApplyGrouping());
    	newParallelActivity.setActivityCategoryID(this.getActivityCategoryID());
    	
    	newParallelActivity.setGrouping(this.getGrouping());
    	newParallelActivity.setLearningLibrary(this.getLearningLibrary());
    	newParallelActivity.setGroupingUIID(this.getGroupingUIID());
    	
    	newParallelActivity.setDefineLater(this.getDefineLater());
    	newParallelActivity.setCreateDateTime(new Date());
    	newParallelActivity.setRunOffline(this.getRunOffline());
    	
    	newParallelActivity.setLibraryActivityUiImage(this.getLibraryActivityUiImage());    	
    	newParallelActivity.setLibraryActivity(this.getLibraryActivity());	
    	newParallelActivity.setLanguageFile(this.getLanguageFile());
    	
    	return newParallelActivity;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("activityId", getActivityId())
            .toString();
    }

    /**
     * @see org.lamsfoundation.lams.util.Nullable#isNull()
     */
    public boolean isNull()
    {
        return false;
    }

}
