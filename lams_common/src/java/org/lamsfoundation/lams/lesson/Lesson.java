package org.lamsfoundation.lams.lesson;

import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
/** 
 * A Lesson is a learning sequence that is assocated with
 * a number of users for use in learning.        
 * 
 * Hibernate definition:
 * 
 * @hibernate.class table="tool_lasr10_survey_session"
 * 
 */
public class Lesson implements Serializable {
    //The state for newly created lesson
    public static final Integer CREATED = new Integer(1);
    //The state for lessons that have been scheduled
    public static final Integer NOT_STARTED_STATE = new Integer(2);
    //The state for started lesson
    public static final Integer STARTED_STATE = new Integer(3);
    //The state for lessons that have been suspended by the teacher. 
    //Note that suspension of the entire lesson is not a 1.1 feature.
    public static final Integer SUSPENDED_STATE = new Integer(4);
    //The state for lessons that have been finished.
    public static final Integer FINISHED_STATE = new Integer(5);
    //The state for lesssons that are no longer visible to the learners.
    public static final Integer ARCHIVED_STATE = new Integer(6);
    
    /** identifier field */
    private Long lessonId;

    /** persistent field */
    private Date createDateTime;

    /** nullable persistent field */
    private Date startDateTime;

    /** nullable persistent field */
    private Date endDateTime;

    /** persistent field */
    private User user;

    /** persistent field */
    private Integer lessonStateId;

    /** persistent field */
    private LearningDesign learningDesign;

    /** persistent field */
    private LessonClass lessonClass;

    /** persistent field */
    private Organisation organisation;

    /** persistent field */
    private Set learnerProgresses;

    /**
     * Holds value of property learners.
     */
    private Set learners;
    
    /** default constructor */
    public Lesson() {
    }

    /** 
     * Minimum constructor that includes all not null attributes to create
     * a new Lesson object.
     * Chain construtor implementation. 
     */
    public Lesson(Date createDateTime, User user, Integer lessonStateId, LearningDesign learningDesign, LessonClass lessonClass, Organisation organisation, Set learnerProgresses) {

        this(null,createDateTime,null,null,user,lessonStateId,learningDesign,lessonClass,organisation,learnerProgresses);
    }    
    
    /** full constructor */
    public Lesson(Long lessonId, Date createDateTime, Date startDateTime, Date endDateTime, User user, Integer lessonStateId, LearningDesign learningDesign, LessonClass lessonClass, Organisation organisation, Set learnerProgresses) {
        this.lessonId = lessonId;
        this.createDateTime = createDateTime;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.user = user;
        this.lessonStateId = lessonStateId;
        this.learningDesign = learningDesign;
        this.lessonClass = lessonClass;
        this.organisation = organisation;
        this.learnerProgresses = learnerProgresses;        
    }



    /** 
     * @hibernate.id  generator-class="identity" type="java.lang.Long"
     *                column="lesson_id"
     */
    public Long getLessonId() {
        return this.lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }

    /** 
     * @hibernate.property type="java.sql.Timestamp" column="create_date_time"
     *            		   length="19"
     */
    public Date getCreateDateTime() {
        return this.createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    /** 
     * @hibernate.property type="java.sql.Timestamp" column="start_date_time"
     *            		   length="19"
     */
    public Date getStartDateTime() {
        return this.startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    /** 
     * @hibernate.property type="java.sql.Timestamp"  column="end_date_time"
     *            	       length="19"
     */
    public Date getEndDateTime() {
        return this.endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    /** 
     * @hibernate.many-to-one not-null="true"
     * @hibernate.column name="user_id"     
     */
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /** 
     * @hibernate.property type="java.lang.Integer"  column="lesson_state_id"
     *            	       length="3"
     */
    public Integer getLessonStateId() {
        return this.lessonStateId;
    }

    public void setLessonStateId(Integer lessonStateId) {
        this.lessonStateId = lessonStateId;
    }

    /** 
     * @hibernate.many-to-one not-null="true" cascade="none"
     * @hibernate.column name="learning_design_id"        
     */
    public LearningDesign getLearningDesign() {
        return this.learningDesign;
    }

    public void setLearningDesign(LearningDesign learningDesign) {
        this.learningDesign = learningDesign;
    }

    /** 
     * @hibernate.many-to-one not-null="true" unique="true" 
     * 						  cascade = "save-update"
     * @hibernate.column name="learning_design_id"           
     */
    public LessonClass getLessonClass() {
        return this.lessonClass;
    }

    public void setLessonClass(LessonClass lessonClass) {
        this.lessonClass = lessonClass;
    }

    /** 
     * @hibernate.many-to-one not-null="true" cascade="none"
     * @hibernate.column name="organisation_id"         
     */
    public Organisation getOrganisation() {
        return this.organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }
    /**
     * @hibernate.set lazy="true" inverse="true" cascade="none"
     * 				  table="lams_lesson_learner"
     * @hibernate.collection-key column="lesson_id"
     * @hibernate.collection-many-to-many
     *            class="org.lamsfoundation.lams.usermanagement.User"
     */
    public Set getLearners()
    {
        return this.learners;
    }

    /**
     * Setter for property learners.
     * @param learners New value of property learners.
     */
    public void setLearners(Set learners)
    {
        this.learners = learners;
    }
    
    /** 
     * @hibernate.set lazy="false" inverse="true" cascade="none"
     * @hibernate.collection-key column="lesson_id"
     * @@hibernate.collection-one-to-many
     *            class="org.lamsfoundation.lams.lesson.LearnerProgress"      
     */
    public Set getLearnerProgresses() {
        return this.learnerProgresses;
    }

    public void setLearnerProgresses(Set learnerProgresses) {
        this.learnerProgresses = learnerProgresses;
    }

       public String toString() {
        return new ToStringBuilder(this)
            .append("lessonId", getLessonId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof Lesson) ) return false;
        Lesson castOther = (Lesson) other;
        return new EqualsBuilder()
            .append(this.getLessonId(), castOther.getLessonId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getLessonId())
            .toHashCode();
    }



}
