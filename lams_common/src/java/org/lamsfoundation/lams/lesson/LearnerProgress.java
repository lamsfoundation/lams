package org.lamsfoundation.lams.lesson;

import org.lamsfoundation.lams.usermanagement.User;
import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 * Temp roll back to rev 1.2
 *        @hibernate.class
 *         table="lams_learner_progress"
 *     
*/
public class LearnerProgress implements Serializable {

    /** identifier field */
    private Long learnerProgressId;

    /** persistent field */
    private User user;

    /** persistent field */
    private org.lamsfoundation.lams.lesson.Lesson lesson;

    /** persistent field */
    private Set progressCurrents;

    /** persistent field */
    private Set progressCompleteds;

    /** full constructor */
    public LearnerProgress(Long learnerProgressId, User user, org.lamsfoundation.lams.lesson.Lesson lesson, Set progressCurrents, Set progressCompleteds) {
        this.learnerProgressId = learnerProgressId;
        this.user = user;
        this.lesson = lesson;
        this.progressCurrents = progressCurrents;
        this.progressCompleteds = progressCompleteds;
    }

    /** default constructor */
    public LearnerProgress() {
    }

    /** 
     *            @hibernate.id
     *             generator-class="identity"
     *             type="java.lang.Long"
     *             column="learner_progress_id"
     *         
     */
    public Long getLearnerProgressId() {
        return this.learnerProgressId;
    }

    public void setLearnerProgressId(Long learnerProgressId) {
        this.learnerProgressId = learnerProgressId;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="user_id"         
     *         
     */
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="lesson_id"         
     *         
     */
    public org.lamsfoundation.lams.lesson.Lesson getLesson() {
        return this.lesson;
    }

    public void setLesson(org.lamsfoundation.lams.lesson.Lesson lesson) {
        this.lesson = lesson;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="learner_progress_id"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.lesson.ProgressCurrent"
     *         
     */
    public Set getProgressCurrents() {
        return this.progressCurrents;
    }

    public void setProgressCurrents(Set progressCurrents) {
        this.progressCurrents = progressCurrents;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="learner_progress_id"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.lesson.ProgressCompleted"
     *         
     */
    public Set getProgressCompleteds() {
        return this.progressCompleteds;
    }

    public void setProgressCompleteds(Set progressCompleteds) {
        this.progressCompleteds = progressCompleteds;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("learnerProgressId", getLearnerProgressId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof LearnerProgress) ) return false;
        LearnerProgress castOther = (LearnerProgress) other;
        return new EqualsBuilder()
            .append(this.getLearnerProgressId(), castOther.getLearnerProgressId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getLearnerProgressId())
            .toHashCode();
    }

}
