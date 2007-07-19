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
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.strategy.SequenceActivityStrategy;


/** 
 * @author Manpreet Minhas
 * @hibernate.class 
*/
public class SequenceActivity extends ComplexActivity implements Serializable {

	private static Logger log = Logger.getLogger(SequenceActivity.class);
	
	/** nullable persistent field */
	private Activity firstActivity;

	private Set branchEntries;

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
            SortedSet activities,
			Boolean stopAfterActivity,
            Set branchEntries) {
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
				stopAfterActivity,
                activities);
        super.activityStrategy = new SequenceActivityStrategy(this);
        this.branchEntries = branchEntries;
        
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
     * Makes a copy of the SequenceActivity for authoring, preview and monitoring environment
     * @return SequenceActivity Returns a deep-copy of the originalActivity
     */
    public Activity createCopy(){
    	SequenceActivity newSequenceActivity = new SequenceActivity();
    	copyToNewActivity(newSequenceActivity);
    	newSequenceActivity.firstActivity = this.firstActivity;
		return newSequenceActivity;
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

    /** Get the first activity in the sequence. A Sequence activity is like a little learning design, and while is it being
     * drawn all the the contained activities may not have transitions between them. So Flash needs to know what the first 
     * activity is!
	 * @hibernate.many-to-one not-null="false"
     * @hibernate.column name="first_activity_id" 
     */
	public Activity getFirstActivity() {
		return firstActivity;
	}


	public void setFirstActivity(Activity firstActivity) {
		this.firstActivity = firstActivity;
	}

	/** 
	 * Get the set of the branch to group mappings used for this branching activity. The set contains GroupBranchActivityEntry entries
	 * 
	 * 	@hibernate.set lazy="true" inverse="true" cascade="none"
	 *		@hibernate.collection-key column="sequence_activity_id" 
	 *		@hibernate.collection-one-to-many class="org.lamsfoundation.lams.learningdesign.GroupBranchActivityEntry"
	*/
	public Set getBranchEntries() {
		return branchEntries;
	}

	public void setBranchEntries(Set branchEntries) {
		this.branchEntries = branchEntries;
	}

	/** Get the groups related to this sequence activity, related via the BranchEntries */
	public SortedSet<Group> getGroupsForBranch() {
		
		Set<GroupBranchActivityEntry> mappingEntries = getBranchEntries();
		TreeSet<Group> sortedGroups = new TreeSet<Group>();
	
		if ( mappingEntries != null ) {
			Iterator mappingIter = mappingEntries.iterator();
			if ( mappingIter.hasNext() )  {
				sortedGroups.add(((GroupBranchActivityEntry) mappingIter.next()).getGroup());
			}
		}
		
		return sortedGroups;
	}

	/** For chosen and tool based branching, we expect there to be only one group per branch, so get the single group.
	 * If there is more than one group, put out a warning and return the first group for this sequence activity, 
	 * related via the BranchEntries */
	public Group getSoleGroupForBranch() {
		
		SortedSet<Group> groups = getGroupsForBranch();
		if ( groups.size() > 1 ) {
			log.warn("Branch "+this+" has more than one group. This is unexpected. Using only the first group.");
		}
		Iterator iter = groups.iterator();
		if ( iter.hasNext() )  {
			return (Group) iter.next();
		}
	
		return null;
	}

}
