package org.lamsfoundation.lams.lesson;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="lams_lesson_state"
 *     
*/
public class LessonState implements Serializable {

    /** identifier field */
    private Integer lamsLessonStateId;

    /** persistent field */
    private String description;

    /** persistent field */
    private Set lessons;

    /** full constructor */
    public LessonState(Integer lamsLessonStateId, String description, Set lessons) {
        this.lamsLessonStateId = lamsLessonStateId;
        this.description = description;
        this.lessons = lessons;
    }

    /** default constructor */
    public LessonState() {
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.Integer"
     *             column="lams_lesson_state_id"
     *         
     */
    public Integer getLamsLessonStateId() {
        return this.lamsLessonStateId;
    }

    public void setLamsLessonStateId(Integer lamsLessonStateId) {
        this.lamsLessonStateId = lamsLessonStateId;
    }

    /** 
     *            @hibernate.property
     *             column="description"
     *             length="255"
     *             not-null="true"
     *         
     */
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="lams_lesson_state_id"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.lesson.Lesson"
     *         
     */
    public Set getLessons() {
        return this.lessons;
    }

    public void setLessons(Set lessons) {
        this.lessons = lessons;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("lamsLessonStateId", getLamsLessonStateId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof LessonState) ) return false;
        LessonState castOther = (LessonState) other;
        return new EqualsBuilder()
            .append(this.getLamsLessonStateId(), castOther.getLamsLessonStateId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getLamsLessonStateId())
            .toHashCode();
    }

}
