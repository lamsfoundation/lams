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
/* $$Id$$ */
package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.strategy.SynchGateActivityStrategy;
import org.lamsfoundation.lams.tool.SystemTool;


/** 
 * @author Manpreet Minhas
 * @hibernate.class 
*/
public class SynchGateActivity extends GateActivity implements Serializable {

    /** full constructor */
    public SynchGateActivity(Long activityId, 
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
            org.lamsfoundation.lams.learningdesign.Grouping grouping, 
            Integer activityTypeId, 
            Transition transitionTo,
            Transition transitionFrom,
            String languageFile,
            Integer gateActivityLevelId,
            Set waitingLearners,
            SystemTool sysTool) {
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
                gateActivityLevelId,
                waitingLearners,
                sysTool);
        super.simpleActivityStrategy = new SynchGateActivityStrategy(this);
    }

    /** default constructor */
    public SynchGateActivity() {
        super.simpleActivityStrategy = new SynchGateActivityStrategy(this);
    }

    /** minimal constructor */
    public SynchGateActivity(Long activityId, 
            Boolean defineLater,
            java.util.Date createDateTime, 
            org.lamsfoundation.lams.learningdesign.LearningLibrary learningLibrary, 
            org.lamsfoundation.lams.learningdesign.Activity parentActivity, 
            org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign, 
            org.lamsfoundation.lams.learningdesign.Grouping grouping, 
            Integer activityTypeId, 
            Transition transitionTo,
            Transition transitionFrom,
            Integer gateActivityLevelId,
            Set waitingLearners) 
    {
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
              gateActivityLevelId,
              waitingLearners);
      super.simpleActivityStrategy = new SynchGateActivityStrategy(this);
    }
    
    /**
     * Makes a copy of the SynchGateActivity for authoring, preview and monitoring enviornment 
     * @return SynchGateActivity Returns a deep-copy of the originalActivity
     */    
    public Activity createCopy() {
    	SynchGateActivity newSynchGateActivity = new SynchGateActivity();
    	copyToNewActivity(newSynchGateActivity);
    	newSynchGateActivity.setGateActivityLevelId(this.getGateActivityLevelId());
    	newSynchGateActivity.setGateOpen(new Boolean(false));
    	return newSynchGateActivity;    	
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
