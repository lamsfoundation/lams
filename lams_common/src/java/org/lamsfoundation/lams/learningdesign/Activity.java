package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @hibernate.class table="lams_learning_activity"
 */
public abstract class Activity implements Serializable {

	public static final int TOOL_ACTIVITY_TYPE = 1;

	public static final int RANDOM_GROUPING_ACTIVITY_TYPE = 2;

	public static final int CHOSEN_GROUPING_ACTIVITY_TYPE = 3;

	public static final int PERMISSION_GATE_ACTIVITY_TYPE = 4;

	public static final int SCHEDULE_GATE_ACTIVITY_TYPE = 5;

	public static final int SYNCH_GATE_ACTIVITY_TYPE = 6;

	public static final int PARALLEL_ACTIVITY_TYPE = 7;

	public static final int OPTIONS_ACTIVITY_TYPE = 8;

	public static final int SEQUENCE_ACTIVITY_TYPE = 9;

	/** identifier field */
	private Long activityId;

	/** nullable persistent field */
	private Integer id;

	/** nullable persistent field */
	private String description;

	/** nullable persistent field */
	private String title;

	/** nullable persistent field */
	private Integer xcoord;

	/** nullable persistent field */
	private Integer ycoord;

	/** nullable persistent field */
	private Integer orderId;

	/** persistent field */
	private Boolean defineLater;

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
	private LearningDesign learningDesign;

	/** persistent field */
	private Grouping grouping;

	/** persistent field */
	private Integer activityTypeId;

	/** persistent field */
	private Set transitionsByToActivityId;

	/** persistent field */
	private Set transitionsByFromActivityId;

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
			org.lamsfoundation.lams.learningdesign.LearningLibrary learningLibrary,
			org.lamsfoundation.lams.learningdesign.Activity parentActivity,
			org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign,
			org.lamsfoundation.lams.learningdesign.Grouping grouping,
			Integer activityTypeId, Set transitionsByToActivityId,
			Set transitionsByFromActivityId) {
		this.activityId = activityId;
		this.id = id;
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
		this.learningDesign = learningDesign;
		this.grouping = grouping;
		this.activityTypeId = activityTypeId;
		this.transitionsByToActivityId = transitionsByToActivityId;
		this.transitionsByFromActivityId = transitionsByFromActivityId;
	}

	/** default constructor */
	public Activity() {
	}

	/** minimal constructor */
	public Activity(
			Long activityId,
			Boolean defineLater,
			Date createDateTime,
			org.lamsfoundation.lams.learningdesign.LearningLibrary learningLibrary,
			org.lamsfoundation.lams.learningdesign.Activity parentActivity,
			org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign,
			org.lamsfoundation.lams.learningdesign.Grouping grouping,
			Integer activityTypeId, Set transitionsByToActivityId,
			Set transitionsByFromActivityId) {
		this.activityId = activityId;
		this.defineLater = defineLater;
		this.createDateTime = createDateTime;
		this.learningLibrary = learningLibrary;
		this.parentActivity = parentActivity;
		this.learningDesign = learningDesign;
		this.grouping = grouping;
		this.activityTypeId = activityTypeId;
		this.transitionsByToActivityId = transitionsByToActivityId;
		this.transitionsByFromActivityId = transitionsByFromActivityId;
	}

	/**
	 * @hibernate.id generator-class="identity" type="java.lang.Long"
	 *               column="activity_id"
	 */
	public Long getActivityId() {
		return this.activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	/**
	 * @hibernate.property column="id" length="11"
	 */
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	/**
	 * @hibernate.set lazy="true" inverse="true" cascade="none"
	 * @hibernate.collection-key column="to_activity_id"
	 * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.learningdesign.Transition"
	 *  
	 */
	public Set getTransitionsByToActivityId() {
		return this.transitionsByToActivityId;
	}

	public void setTransitionsByToActivityId(Set transitionsByToActivityId) {
		this.transitionsByToActivityId = transitionsByToActivityId;
	}

	/**
	 * @hibernate.set lazy="true" inverse="true" cascade="none"
	 * @hibernate.collection-key column="from_activity_id"
	 * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.learningdesign.Transition"
	 *  
	 */
	public Set getTransitionsByFromActivityId() {
		return this.transitionsByFromActivityId;
	}

	public void setTransitionsByFromActivityId(Set transitionsByFromActivityId) {
		this.transitionsByFromActivityId = transitionsByFromActivityId;
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
}
