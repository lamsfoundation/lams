package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @hibernate.class
 */
public abstract class ComplexActivity extends Activity implements Serializable {

	/** persistent field */
	private Set activities;

	/** full constructor */
	public ComplexActivity(
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
			Transition transitionFrom,
			Set activities) {
		super(activityId, id, description, title, xcoord, ycoord, orderId,
				defineLater, createDateTime, offlineInstructions,
				learningLibrary, parentActivity, libraryActivity,parentUIID,learningDesign, grouping,
				activityTypeId, transitionTo,transitionFrom);
		this.activities = activities;
	}

	/** default constructor */
	public ComplexActivity() {
		super();
	}

	/** minimal constructor */
	public ComplexActivity(
			Long activityId,
			Boolean defineLater,
			Date createDateTime,
			LearningLibrary learningLibrary,
			Activity parentActivity,
			LearningDesign learningDesign,
			Grouping grouping,
			Integer activityTypeId, 
			Transition transitionTo,
			Transition transitionFrom,Set activities) {
		super(activityId, defineLater, createDateTime, learningLibrary,
				parentActivity, learningDesign, grouping, activityTypeId,
				transitionTo,transitionFrom);
		this.activities = activities;
	}

	public String toString() {
		return new ToStringBuilder(this).append("activityId", getActivityId())
				.toString();
	}

	/**
	 * @hibernate.set lazy="true" inverse="true" cascade="none"
	 * @hibernate.collection-key column="parent_activity_id"
	 * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.learningdesign.Activity"
	 *  
	 */
	public Set getActivities() {
	    if(this.activities==null){
	        setActivities(new TreeSet(new ActivityOrderComparator()));
	        return this.activities;
	    }	    
	    else{
	    	TreeSet sortedActivities = new TreeSet(new ActivityOrderComparator());
	    	sortedActivities.addAll(this.activities);
	    	return sortedActivities;	    	
	    }
	}

	public void setActivities(Set activities) {
	    this.activities=activities;
	}
	public void addActivity(Activity activity){
		this.activities.add(activity);
	}
	
	/**
	 * Return the requested child activity based on the id.
	 * @param activityId the requested activity id.
	 * @return the child activity.
	 */
	public Activity getChildActivityById(long activityId)
	{
	    for(Iterator i = this.activities.iterator();i.hasNext();)
	    {
	        Activity child = (Activity)i.next();
	        if(child.getActivityId().longValue()==activityId)
	            return child;
	    }
	    return new NullActivity();
	}
}