package org.lamsfoundation.lams.lesson;

import org.lamsfoundation.lams.learningdesign.Activity;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="lams_progress_completed"
 *     
*/
public class ProgressCompleted implements Serializable {

    /** identifier field */
    private org.lamsfoundation.lams.lesson.ProgressCompletedPK comp_id;

    /** nullable persistent field */
    private org.lamsfoundation.lams.lesson.LearnerProgress learnerProgress;

    /** nullable persistent field */
    private Activity activity;

    /** full constructor */
    public ProgressCompleted(org.lamsfoundation.lams.lesson.ProgressCompletedPK comp_id, org.lamsfoundation.lams.lesson.LearnerProgress learnerProgress, Activity activity) {
        this.comp_id = comp_id;
        this.learnerProgress = learnerProgress;
        this.activity = activity;
    }

    /** default constructor */
    public ProgressCompleted() {
    }

    /** minimal constructor */
    public ProgressCompleted(org.lamsfoundation.lams.lesson.ProgressCompletedPK comp_id) {
        this.comp_id = comp_id;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *         
     */
    public org.lamsfoundation.lams.lesson.ProgressCompletedPK getComp_id() {
        return this.comp_id;
    }

    public void setComp_id(org.lamsfoundation.lams.lesson.ProgressCompletedPK comp_id) {
        this.comp_id = comp_id;
    }

    /** 
     *            @hibernate.many-to-one
     *             update="false"
     *             insert="false"
     *         
     *            @hibernate.column
     *             name="learner_progress_id"
     *         
     */
    public org.lamsfoundation.lams.lesson.LearnerProgress getLearnerProgress() {
        return this.learnerProgress;
    }

    public void setLearnerProgress(org.lamsfoundation.lams.lesson.LearnerProgress learnerProgress) {
        this.learnerProgress = learnerProgress;
    }

    /** 
     *            @hibernate.many-to-one
     *             update="false"
     *             insert="false"
     *         
     *            @hibernate.column
     *             name="activity_id"
     *         
     */
    public Activity getActivity() {
        return this.activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("comp_id", getComp_id())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof ProgressCompleted) ) return false;
        ProgressCompleted castOther = (ProgressCompleted) other;
        return new EqualsBuilder()
            .append(this.getComp_id(), castOther.getComp_id())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getComp_id())
            .toHashCode();
    }

}
