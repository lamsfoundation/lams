package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="lams_learning_transition"
 *     
*/
public class Transition implements Serializable {

    /** identifier field */
    private Long transitionId;

    /** nullable persistent field */
    private Integer id;

    /** nullable persistent field */
    private String description;

    /** nullable persistent field */
    private String title;

    /** persistent field */
    private Date createDateTime;

    /** persistent field */
    private org.lamsfoundation.lams.learningdesign.Activity activityByToActivityId;

    /** persistent field */
    private org.lamsfoundation.lams.learningdesign.Activity activityByFromActivityId;

    /** persistent field */
    private org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign;

    /** full constructor */
    public Transition(Long transitionId, Integer id, String description, String title, Date createDateTime, org.lamsfoundation.lams.learningdesign.Activity activityByToActivityId, org.lamsfoundation.lams.learningdesign.Activity activityByFromActivityId, org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign) {
        this.transitionId = transitionId;
        this.id = id;
        this.description = description;
        this.title = title;
        this.createDateTime = createDateTime;
        this.activityByToActivityId = activityByToActivityId;
        this.activityByFromActivityId = activityByFromActivityId;
        this.learningDesign = learningDesign;
    }

    /** default constructor */
    public Transition() {
    }

    /** minimal constructor */
    public Transition(Long transitionId, Date createDateTime, org.lamsfoundation.lams.learningdesign.Activity activityByToActivityId, org.lamsfoundation.lams.learningdesign.Activity activityByFromActivityId, org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign) {
        this.transitionId = transitionId;
        this.createDateTime = createDateTime;
        this.activityByToActivityId = activityByToActivityId;
        this.activityByFromActivityId = activityByFromActivityId;
        this.learningDesign = learningDesign;
    }

    /** 
     *            @hibernate.id
     *             generator-class="identity"
     *             type="java.lang.Long"
     *             column="transition_id"
     *         
     */
    public Long getTransitionId() {
        return this.transitionId;
    }

    public void setTransitionId(Long transitionId) {
        this.transitionId = transitionId;
    }

    /** 
     *            @hibernate.property
     *             column="id"
     *             length="11"
     *         
     */
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /** 
     *            @hibernate.property
     *             column="description"
     *             length="65535"
     *         
     */
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /** 
     *            @hibernate.property
     *             column="title"
     *             length="255"
     *         
     */
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /** 
     *            @hibernate.property
     *             column="create_date_time"
     *             length="19"
     *             not-null="true"
     *         
     */
    public Date getCreateDateTime() {
        return this.createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="to_activity_id"         
     *         
     */
    public org.lamsfoundation.lams.learningdesign.Activity getActivityByToActivityId() {
        return this.activityByToActivityId;
    }

    public void setActivityByToActivityId(org.lamsfoundation.lams.learningdesign.Activity activityByToActivityId) {
        this.activityByToActivityId = activityByToActivityId;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="from_activity_id"         
     *         
     */
    public org.lamsfoundation.lams.learningdesign.Activity getActivityByFromActivityId() {
        return this.activityByFromActivityId;
    }

    public void setActivityByFromActivityId(org.lamsfoundation.lams.learningdesign.Activity activityByFromActivityId) {
        this.activityByFromActivityId = activityByFromActivityId;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="learning_design_id"         
     *         
     */
    public org.lamsfoundation.lams.learningdesign.LearningDesign getLearningDesign() {
        return this.learningDesign;
    }

    public void setLearningDesign(org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign) {
        this.learningDesign = learningDesign;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("transitionId", getTransitionId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof Transition) ) return false;
        Transition castOther = (Transition) other;
        return new EqualsBuilder()
            .append(this.getTransitionId(), castOther.getTransitionId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getTransitionId())
            .toHashCode();
    }

}
