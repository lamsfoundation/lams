package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.Nullable;

/**
 * @hibernate.class table="lams_learning_activity"
 */
public abstract class Activity implements Serializable,Nullable {

	public static final int TOOL_ACTIVITY_TYPE = 1;

	public static final int RANDOM_GROUPING_ACTIVITY_TYPE = 2;

	public static final int CHOSEN_GROUPING_ACTIVITY_TYPE = 3;

	public static final int PERMISSION_GATE_ACTIVITY_TYPE = 4;

	public static final int SCHEDULE_GATE_ACTIVITY_TYPE = 5;

	public static final int SYNCH_GATE_ACTIVITY_TYPE = 6;

	public static final int PARALLEL_ACTIVITY_TYPE = 7;

	public static final int OPTIONS_ACTIVITY_TYPE = 8;

	public static final int SEQUENCE_ACTIVITY_TYPE = 9;
	
	public static final String OBJECT_TYPE ="Activity";

	/** identifier field */
	private Long activityId;

	/** nullable persistent field */
	private Integer activityUIID;

	/** nullable persistent field */
	private String description;

	/** nullable persistent field */
	private String title;
	
	/** nullable persistent field */
	private String helpText;

	/** nullable persistent field */
	private Integer xcoord;

	/** nullable persistent field */
	private Integer ycoord;

	/** nullable persistent field */
	private Integer orderId;

	/** persistent field */
	private Boolean defineLater;
	
	/** persistent field */
	private Boolean runOffline;

	/** persistent field */
	private Date createDateTime;

	/** nullable persistent field */
	private String offlineInstructions;

	/** nullable persistent field */
	private String libraryActivityUiImage;

	/** persistent field */
	private LearningLibrary learningLibrary;

	/** persistent field */
	private Activity parentActivity;
	
	/** persistent field */
	private Activity libraryActivity;

	/** persistent field */
	private LearningDesign learningDesign;

	/** persistent field */
	private Grouping grouping;
	
	/** nullable persistent field */
	private Integer groupingUIID;

	/** persistent field */
	private Integer activityTypeId;

	/** persistent field */
	private Transition transitionTo; 
	
	/** persistent field */
	private Transition transitionFrom;
	
	/** persistent field */
	private Integer parentUIID;
		
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
	 * Returns the Transition FROM this activity
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
	 * Returns the Transition TO this Activity
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
	 * Check up whether a is tool activity or not.
	 * @return
	 */
	public boolean isToolActivity()
	{
	    return getActivityTypeId().intValue()==TOOL_ACTIVITY_TYPE;
	}
}