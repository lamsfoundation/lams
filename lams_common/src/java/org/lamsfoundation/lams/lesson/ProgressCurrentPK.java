package org.lamsfoundation.lams.lesson;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class ProgressCurrentPK implements Serializable {

    /** identifier field */
    private Long learnerProgressId;

    /** identifier field */
    private Long activityId;

    /** full constructor */
    public ProgressCurrentPK(Long learnerProgressId, Long activityId) {
        this.learnerProgressId = learnerProgressId;
        this.activityId = activityId;
    }

    /** default constructor */
    public ProgressCurrentPK() {
    }

    /** 
     *                @hibernate.property
     *                 column="learner_progress_id"
     *                 length="20"
     *             
     */
    public Long getLearnerProgressId() {
        return this.learnerProgressId;
    }

    public void setLearnerProgressId(Long learnerProgressId) {
        this.learnerProgressId = learnerProgressId;
    }

    /** 
     *                @hibernate.property
     *                 column="activity_id"
     *                 length="20"
     *             
     */
    public Long getActivityId() {
        return this.activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("learnerProgressId", getLearnerProgressId())
            .append("activityId", getActivityId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof ProgressCurrentPK) ) return false;
        ProgressCurrentPK castOther = (ProgressCurrentPK) other;
        return new EqualsBuilder()
            .append(this.getLearnerProgressId(), castOther.getLearnerProgressId())
            .append(this.getActivityId(), castOther.getActivityId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getLearnerProgressId())
            .append(getActivityId())
            .toHashCode();
    }

}
