package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
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
			LearningDesign learningDesign,
			Grouping grouping,
			Integer activityTypeId, Set transitionsByToActivityId,
			Set transitionsByFromActivityId, Set activities) {
		super(activityId, id, description, title, xcoord, ycoord, orderId,
				defineLater, createDateTime, offlineInstructions,
				learningLibrary, parentActivity, learningDesign, grouping,
				activityTypeId, transitionsByToActivityId,
				transitionsByFromActivityId);
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
			Integer activityTypeId, Set transitionsByToActivityId,
			Set transitionsByFromActivityId, Set activities) {
		super(activityId, defineLater, createDateTime, learningLibrary,
				parentActivity, learningDesign, grouping, activityTypeId,
				transitionsByToActivityId, transitionsByFromActivityId);
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
		return this.activities;
	}

	public void setActivities(Set activities) {
		this.activities = activities;
	}
}