/***************************************************************************
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
 * ************************************************************************
 */
package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.dto.MonitoringActivityDTO;
import org.lamsfoundation.lams.learningdesign.dto.ProgressActivityDTO;
import org.lamsfoundation.lams.learningdesign.strategy.ComplextActivityStrategy;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.Nullable;
/**
 * @hibernate.class table="lams_learning_activity"
 */
public abstract class Activity implements Serializable,Nullable {

	/**
	 * static final variables indicating the type of activities
	 * available for a LearningDesign 
	 * */
	/******************************************************************/
	public static final int TOOL_ACTIVITY_TYPE = 1;
	public static final int GROUPING_ACTIVITY_TYPE = 2;
	public static final int SYNCH_GATE_ACTIVITY_TYPE = 3;
	public static final int SCHEDULE_GATE_ACTIVITY_TYPE = 4;
	public static final int PERMISSION_GATE_ACTIVITY_TYPE = 5;
	public static final int PARALLEL_ACTIVITY_TYPE = 6;
	public static final int OPTIONS_ACTIVITY_TYPE = 7;
	public static final int SEQUENCE_ACTIVITY_TYPE = 8;
	/******************************************************************/
	
	/**
	* static final variables indicating the the category of activities
    *******************************************************************/
	public static final int CATEGORY_SYSTEM = 1;
	public static final int CATEGORY_COLLABORATION = 2;
	public static final int CATEGORY_ASSESSMENT = 3;
	public static final int CATEGORY_CONTENT = 4;
	public static final int CATEGORY_SPLIT = 5;
    /*******************************************************************/
	
	/** WDDX packet specific attribute created to identify the
	 * type of object being passed.*/
	public static final String OBJECT_TYPE ="Activity";

	/** identifier field */
	private Long activityId;

	/** FLASH generated value. Unique per LearningDesign.
	 * Required by flash only.*/
	private Integer activityUIID;

	/** Description of the activity*/
	private String description;

	/** Title of the activity*/
	private String title;
	
	/** Help text for the activity*/
	private String helpText;

	/** UI specific attribute indicating the
	 * position of the activity*/
	private Integer xcoord;

	/** UI specific attribute indicating the
	 * position of the activity*/
	private Integer ycoord;

	/** Indicates the order in which the activities
	 * appear inside complex activities. Starts from 
	 * 0, 1 and so on.*/
	private Integer orderId;

	/** Indicates whether the content of this activity 
	 * would be defined later in the monitoring enviornment or not.*/
	private Boolean defineLater;
	
	/** Indicates whether this activity is available offline*/
	private Boolean runOffline;

	/** Date this activity was created */
	private Date createDateTime;

	/** Offline Instruction for this activity*/
	private String offlineInstructions;
	
	 /** Online Instructions for this activity*/
    private String onlineInstructions;
	
	/** The image that represents the icon of this 
	 * activity in the UI*/
	private String libraryActivityUiImage;

	/** The LearningLibrary of which this activity is a part*/
	private LearningLibrary learningLibrary;

	/** The activity that acts as a container/parent for
	 * this activity. Normally would be one of the 
	 * complex activities which have child activities 
	 * defined inside them. */
	private Activity parentActivity;
	
	/** Single Library can have one or more activities
	 * defined inside it. This field indicates which
	 * activity is this.*/
	private Activity libraryActivity;

	/** The LearningDesign to which this activity belongs*/
	private LearningDesign learningDesign;

	/** The Grouping that applies to this activity*/
	private Grouping grouping;
	
	/** The grouping_ui_id of the Grouping that
	 * applies that to this activity
	 * */
	private Integer groupingUIID;

	/** The type of activity */
	private Integer activityTypeId;
	
	/** The category of activity */
	private Integer activityCategoryID;

	/** persistent field */
	private Transition transitionTo; 
	
	/** persistent field */
	private Transition transitionFrom;
	
	/** the activity_ui_id of the parent activity */
	private Integer parentUIID;
	
	private Boolean applyGrouping;
	
	private Integer groupingSupportType;
	
	/**
	 * @return Returns the applyGrouping.
	 */
	public Boolean getApplyGrouping() {
		return applyGrouping;
	}
	/**
	 * @param applyGrouping The applyGrouping to set.
	 */
	public void setApplyGrouping(Boolean applyGrouping) {
		this.applyGrouping = applyGrouping;
	}
	/**
	 * @return Returns the groupingSupportType.
	 */
	public Integer getGroupingSupportType() {
		return groupingSupportType;
	}
	/**
	 * @param groupingSupportType The groupingSupportType to set.
	 */
	public void setGroupingSupportType(Integer groupingSupportType) {
		this.groupingSupportType = groupingSupportType;
	}
	/** full constructor */
	public Activity(
			Long activityId,
			Integer id,
			String description,
			String title,
			Integer xcoord,
			Integer ycoord,
			Integer orderId,
			Boolean defineLater,
			Date createDateTime,
			String offlineInstructions,
			LearningLibrary learningLibrary,
			Activity parentActivity,
			Activity libraryActivity,
			Integer parentUIID,
			LearningDesign learningDesign,
			Grouping grouping,
			Integer activityTypeId, 
			Transition transitionTo,
			Transition transitionFrom) {
		this.activityId = activityId;
		this.activityUIID = id;
		this.description = description;
		this.title = title;
		this.xcoord = xcoord;
		this.ycoord = ycoord;
		this.orderId = orderId;
		this.defineLater = defineLater;
		this.createDateTime = createDateTime;
		this.offlineInstructions = offlineInstructions;
		this.learningLibrary = learningLibrary;
		this.parentActivity = parentActivity;
		this.parentUIID = parentUIID;
		this.libraryActivity = libraryActivity;
		this.learningDesign = learningDesign;
		this.grouping = grouping;
		this.activityTypeId = activityTypeId;
		this.transitionTo = transitionTo;
		this.transitionFrom = transitionFrom;
	}	
	/** default constructor */
	public Activity() {
		this.grouping = null;		
	}

	/** minimal constructor */
	public Activity(
			Long activityId,
			Boolean defineLater,
			Date createDateTime,
			LearningLibrary learningLibrary,
			Activity parentActivity,
			LearningDesign learningDesign,
			Grouping grouping,
			Integer activityTypeId,
			Transition transitionTo,
			Transition transitionFrom) {
		this.activityId = activityId;
		this.defineLater = defineLater;
		this.createDateTime = createDateTime;
		this.learningLibrary = learningLibrary;
		this.parentActivity = parentActivity;
		this.learningDesign = learningDesign;
		this.grouping = grouping;
		this.activityTypeId = activityTypeId;
		this.transitionTo = transitionTo;
		this.transitionFrom = transitionFrom;
	}

	/**
	 * @hibernate.activityUIID generator-class="identity" type="java.lang.Long"
	 *               column="activity_id"
	 */
	public Long getActivityId() {
		return this.activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	/**
	 * @hibernate.property column="activityUIID" length="11"
	 */
	public Integer getActivityUIID() {
		return this.activityUIID;
	}

	public void setActivityUIID(Integer id) {
		this.activityUIID = id;
	}

	/**
	 * @hibernate.property column="description" length="65535"
	 */
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @hibernate.property column="title" length="255"
	 */
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @hibernate.property column="xcoord" length="11"
	 */
	public Integer getXcoord() {
		return this.xcoord;
	}

	public void setXcoord(Integer xcoord) {
		this.xcoord = xcoord;
	}

	/**
	 * @hibernate.property column="ycoord" length="11"
	 */
	public Integer getYcoord() {
		return this.ycoord;
	}

	public void setYcoord(Integer ycoord) {
		this.ycoord = ycoord;
	}

	/**
	 * @hibernate.property column="order_id" length="11"
	 */
	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	/**
	 * @hibernate.property column="define_later_flag" length="4" not-null="true"
	 */
	public Boolean getDefineLater() {
		return this.defineLater;
	}

	public void setDefineLater(Boolean defineLater) {
		this.defineLater = defineLater;
	}

	/**
	 * @hibernate.property column="create_date_time" length="19" not-null="true"
	 */
	public Date getCreateDateTime() {
		return this.createDateTime;
	}

	public void setCreateDateTime(Date createDateTime) {
		this.createDateTime = createDateTime;
	}

	/**
	 * @hibernate.property column="offline_instructions" length="65535"
	 */
	public String getOfflineInstructions() {
		return this.offlineInstructions;
	}

	public void setOfflineInstructions(String offlineInstructions) {
		this.offlineInstructions = offlineInstructions;
	}

	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="learning_library_id"
	 *  
	 */
	public org.lamsfoundation.lams.learningdesign.LearningLibrary getLearningLibrary() {
		return this.learningLibrary;
	}

	public void setLearningLibrary(
			org.lamsfoundation.lams.learningdesign.LearningLibrary learningLibrary) {
		this.learningLibrary = learningLibrary;
	}

	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="parent_activity_id"
	 *  
	 */
	public org.lamsfoundation.lams.learningdesign.Activity getParentActivity() {
		return this.parentActivity;
	}

	public void setParentActivity(Activity parentActivity) {
		this.parentActivity = parentActivity;
	}

	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="learning_design_id"
	 *  
	 */
	public org.lamsfoundation.lams.learningdesign.LearningDesign getLearningDesign() {
		return this.learningDesign;
	}

	public void setLearningDesign(
			org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign) {
		this.learningDesign = learningDesign;
	}

	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="grouping_id"
	 *  
	 */
	public org.lamsfoundation.lams.learningdesign.Grouping getGrouping() {
		return this.grouping;
	}

	public void setGrouping(
			org.lamsfoundation.lams.learningdesign.Grouping grouping) {
		this.grouping = grouping;
	}

	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="learning_activity_type_id"
	 *  
	 */
	public Integer getActivityTypeId() {
		return this.activityTypeId;
	}

	public void setActivityTypeId(Integer activityTypeId) {
		this.activityTypeId = activityTypeId;
	}
	public String toString() {
		return new ToStringBuilder(this).append("activityId", getActivityId())
				.toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Activity))
			return false;
		Activity castOther = (Activity) other;
		return new EqualsBuilder().append(this.getActivityId(),
				castOther.getActivityId()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getActivityId()).toHashCode();
	}

	/**
	 * @hibernate.property column="library_activity_ui_image" length="255"
	 * @return Returns the libraryActivityUiImage.
	 */
	public String getLibraryActivityUiImage() {
		return libraryActivityUiImage;
	}

	/**
	 * @param libraryActivityUiImage
	 *            The libraryActivityUiImage to set.
	 */
	public void setLibraryActivityUiImage(String libraryActivityUiImage) {
		this.libraryActivityUiImage = libraryActivityUiImage;
	}	
	
	/**
	 * This function returns the Transition that 
	 * STARTS FROM THIS ACTIVITY. In simpler words the
	 * next activity in the transition.
	 * 
	 * For example, if we have a transition as  following
	 * A --> B --> C. For activity B this function would
	 * return C. That is the Transition FROM activity B.
	 * 
	 * @return Returns the transitionFrom.
	 */
	public Transition getTransitionFrom() {
		return transitionFrom;
	}
	/**
	 * @param transitionFrom The transitionFrom to set.
	 */
	public void setTransitionFrom(Transition transitionFrom) {
		this.transitionFrom = transitionFrom;
	}
	/**
	 * This function returns the Transition that 
	 * POINTS TO THIS ACTIVITY and NOT the transition 
	 * that this activity points to.
	 * 
	 * For example, if we have a transition as  following
	 * A --> B --> C. For activity B this function would
	 * return A. That is the Transition that points TO 
	 * activity B.
	 * 
	 * @return Returns the transitionTo.
	 */
	public Transition getTransitionTo() {
		return transitionTo;
	}
	/**
	 * @param transitionTo The transitionTo to set.
	 */
	public void setTransitionTo(Transition transitionTo) {
		this.transitionTo = transitionTo;
	}	
	public Integer getParentUIID() {
		return parentUIID;
	}
	public void setParentUIID(Integer parent_ui_id) {
		this.parentUIID = parent_ui_id;
	}	
	public Activity getLibraryActivity() {
		return libraryActivity;
	}
	public void setLibraryActivity(Activity libraryActivity) {
		this.libraryActivity = libraryActivity;
	}
	public String getHelpText() {
		return helpText;
	}
	public void setHelpText(String helpText) {
		this.helpText = helpText;
	}
	public Integer getGroupingUIID() {
		return groupingUIID;
	}
	public void setGroupingUIID(Integer groupingUIID) {
		this.groupingUIID = groupingUIID;
	}
	public Boolean getRunOffline() {
		return runOffline;
	}
	public void setRunOffline(Boolean runOffline) {
		this.runOffline = runOffline;
	}
	
	/**
	 * This method that get all tool activities belong to a particular activity. 
	 * 
	 * As the activity object structure might be infinite, we recursively loop
	 * through the entire structure and added all tool activities into the set 
	 * that we want to return.
	 * 
	 * @param activity the requested activity.
	 * @return the set of all tool activities.
	 */
	public Set getAllToolActivitiesFrom(Activity activity)
	{
	    Set toolActivities = new TreeSet(new ActivityOrderComparator());
	    //tool activity is usually leaf node activity, we return it right away
	    if(activity.getActivityTypeId().intValue()==Activity.TOOL_ACTIVITY_TYPE)
	    {
	        toolActivities.add(activity);
	        return toolActivities;
	    }
	    //recursively get tool activity from its children if it is complex activity.
	    else if(activity instanceof ComplexActivity)
	    {
	        ComplexActivity cActivity = (ComplexActivity)activity;
	        for(Iterator i = cActivity.getActivities().iterator();i.hasNext();)
	        {
	            Activity child = (Activity)i.next();
	            toolActivities.addAll(getAllToolActivitiesFrom(child));
	        }
	    }

	    return toolActivities;
	    
	}
	
	/**
	 * Return the group information for the requested user when he is running
	 * current activity instance.
	 * @param learner the requested user	
	 * @return the group that this user belongs to.
	 */
	public Group getGroupFor(User learner)
	{
	    if(this.getGrouping()==null)
	        throw new IllegalArgumentException("Exception occured in " +
	        		"getGroupFor, no grouping has been defined");
	    
	    for(Iterator i=this.getGrouping().getGroups().iterator();i.hasNext();)
	    {
	        Group group = (Group)i.next();
	        if(this.getGrouping().isLearnerGroup(group)&&group.hasLearner(learner))
	            return group;
	    }
	    
	    return new NullGroup();
	}
	/**
	 * Check up whether an activity is tool activity or not.
	 * @return is this activity a tool activity?
	 */
	public boolean isToolActivity()
	{
	    return getActivityTypeId().intValue()==TOOL_ACTIVITY_TYPE;
	}
	
	/**
	 * Check up whether an activity is sequence activity or not.
	 * @return is this activity a sequence activity?
	 */
	public boolean isSequenceActivity()
	{
	    return getActivityTypeId().intValue()==SEQUENCE_ACTIVITY_TYPE;
	}
	public boolean isComplexActivity(){
		return getActivityTypeId().intValue()== SEQUENCE_ACTIVITY_TYPE || 
			   getActivityTypeId().intValue()== PARALLEL_ACTIVITY_TYPE ||
			   getActivityTypeId().intValue()== OPTIONS_ACTIVITY_TYPE;
	}
	public boolean isGateActivity(){
		return getActivityTypeId().intValue()== SCHEDULE_GATE_ACTIVITY_TYPE ||
			   getActivityTypeId().intValue()== PERMISSION_GATE_ACTIVITY_TYPE ||
			   getActivityTypeId().intValue()== SYNCH_GATE_ACTIVITY_TYPE;
				
	}
	public boolean isGroupingActivity(){
		return getActivityTypeId().intValue()== GROUPING_ACTIVITY_TYPE;
	}
	
	/**
	 * Delegate to activity strategy to check up the status of all children.
	 * 
     * @param learnerProgress the progress data that record what has been 
     * 						  completed
     * @return true if all children are completed.

	 */
/**	public boolean areChildrenCompleted(LearnerProgress learnerProgress)
	{
	    //return activityStrategy.areChildrenCompleted(this,learnerProgress);
	    return ((ComplexActivity)this).areChildrenCompleted(learnerProgress);
	}
	
	/**
	 * <p>Delegate to activity strategy to calculate what is the next activity
	 * within the parent activity.</p>
	 * 
	 * <b>Note:</b> The logic of what is the next activity here is progress
	 * 				enigne specific now. Please see the <code>ActivityStrategy</code>
	 * 				for details explaination of what is next.
	 * 
	 * @param currentChild the current child activity in a complex activity.
	 * @return the next activity within a parent activity
	 */
/**	public Activity getNextActivityByParent(Activity currentChild)
	{
	    return ((ComplexActivity)this).getNextActivityByParent(currentChild);
	}	
	*/
	public Integer getActivityCategoryID() {
		return activityCategoryID;
	}
	public void setActivityCategoryID(Integer activityCategoryID) {
		this.activityCategoryID = activityCategoryID;
	}

	public MonitoringActivityDTO getMonitoringActivityDTO(){
		return new MonitoringActivityDTO(this);
	}
	public MonitoringActivityDTO getMonitoringActivityDTO(Activity activity){
		Integer contributionType[]= null;
		if(activity.isGateActivity() || activity.isToolActivity() || activity.isGroupingActivity()){
			contributionType = ((SimpleActivity)activity).getContributionType();			
			return new MonitoringActivityDTO(activity,contributionType);
		}
		else
			return new MonitoringActivityDTO(activity);
	}
	public Vector getMonitoringActivityDTOSet(Set activities){
		Vector children = new Vector();		
		Iterator iterator = activities.iterator();
		while(iterator.hasNext()){
			Activity activity = (Activity)iterator.next();
			children.add(getMonitoringActivityDTO(activity));
		}
		return children;
	}	
	/**
	 * @return Returns the onlineInstructions.
	 */
	public String getOnlineInstructions() {
		return onlineInstructions;
	}
	/**
	 * @param onlineInstructions The onlineInstructions to set.
	 */
	public void setOnlineInstructions(String onlineInstructions) {
		this.onlineInstructions = onlineInstructions;
	}
	
	/**
	 * Return the activity dto for progress view.
	 * @return the activity dto.
	 */
	public ProgressActivityDTO getProgressActivityData()
	{
	    return new ProgressActivityDTO(this.activityId);
	}
}