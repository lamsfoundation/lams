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
package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Date;
import java.util.SortedSet;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.strategy.SequenceActivityStrategy;


/** 
 * @author Manpreet Minhas
 * @hibernate.class 
*/
public class SequenceActivity extends ComplexActivity implements Serializable {

    /** full constructor */
    public SequenceActivity(Long activityId, 
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
            SortedSet activities) {
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
        super.activityStrategy = new SequenceActivityStrategy(this);
    }


    /** default constructor */
    public SequenceActivity() {
        super.activityStrategy = new SequenceActivityStrategy(this);
    }

     /** minimal constructor */
    public SequenceActivity(Long activityId, 
            Boolean defineLater, 
            java.util.Date createDateTime, 
            org.lamsfoundation.lams.learningdesign.LearningLibrary learningLibrary, 
            org.lamsfoundation.lams.learningdesign.Activity parentActivity, 
            org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign, 
            org.lamsfoundation.lams.learningdesign.Grouping grouping, 
            Integer activityTypeId,  
            Transition transitionTo,
            Transition transitionFrom,
            SortedSet activities) {
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
      super.activityStrategy = new SequenceActivityStrategy(this);
    }
    
    /**
     * Makes a copy of the SequenceActivity for authoring, preview and monitoring enviornment
     * @return SequenceActivity Returns a deep-copy of the originalActivity
     */
    public Activity createCopy(){
    	SequenceActivity newSequenceActivity = new SequenceActivity();
    	
    	newSequenceActivity.setActivityUIID(this.getActivityUIID());
    	newSequenceActivity.setDescription(this.getDescription());
    	newSequenceActivity.setTitle(this.getTitle());
    	newSequenceActivity.setHelpText(this.getHelpText());
    	newSequenceActivity.setXcoord(this.getXcoord());
    	newSequenceActivity.setYcoord(this.getYcoord());
    	newSequenceActivity.setActivityTypeId(this.getActivityTypeId());
    	
    	newSequenceActivity.setGroupingSupportType(this.getGroupingSupportType());
    	newSequenceActivity.setApplyGrouping(this.getApplyGrouping());
    	newSequenceActivity.setActivityCategoryID(this.getActivityCategoryID());
    	
    	newSequenceActivity.setGrouping(this.getGrouping());
    	newSequenceActivity.setGroupingUIID(this.getGroupingUIID());
    	newSequenceActivity.setLearningLibrary(this.getLearningLibrary());
    	newSequenceActivity.setDefineLater(this.getDefineLater());
    	newSequenceActivity.setCreateDateTime(new Date());
    	newSequenceActivity.setRunOffline(this.getRunOffline());
    	
    	newSequenceActivity.setLibraryActivity(this.getLibraryActivity());
		newSequenceActivity.setLibraryActivityUiImage(this.getLibraryActivityUiImage());    	
    	newSequenceActivity.setLanguageFile(this.getLanguageFile());
		return newSequenceActivity;
    }

    /**
     * Retrieve the first activity in current sequence activity;
     * @return the first activity;
     */
    public Activity getFirstActivityInSequenceActivity()
    {
        return this.getNextActivityByParent(new NullActivity()); 
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
