package org.lamsfoundation.lams.lesson;

import org.lamsfoundation.lams.learningdesign.Grouping;
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
 */
public class Lesson implements Serializable {

    
    public static final Integer NOT_STARTED_STATE = new Integer(1);
    
    public static final Integer STARTED_STATE = new Integer(2);
    
    public static final Integer SUSPENDED_STATE = new Integer(3);
    
    public static final Integer FINISHED_STATE = new Integer(4);
    
    public static final Integer ARCHIVED_STATE = new Integer(5);
    
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
    private User learners;
    
    

    
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

    /** default constructor */
    public Lesson() {
    }

    /** minimal constructor */
    public Lesson(Long lessonId, Date createDateTime, User user, Integer lessonStateId, LearningDesign learningDesign, LessonClass lessonClass, Organisation organisation, Set learnerProgresses) {
        this.lessonId = lessonId;
        this.createDateTime = createDateTime;
        this.user = user;
        this.lessonStateId = lessonStateId;
        this.learningDesign = learningDesign;
        this.lessonClass = lessonClass;
        this.organisation = organisation;
        this.learnerProgresses = learnerProgresses;        
    }

    /** 
     *           
     *         
     */
    public Long getLessonId() {
        return this.lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }

    /** 
     *           
     *         
     */
    public Date getCreateDateTime() {
        return this.createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    /** 
     *           
     */
    public Date getStartDateTime() {
        return this.startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    /** 
     *           
     *         
     */
    public Date getEndDateTime() {
        return this.endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    /** 
     *              
     *         
     */
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /** 
     *                    
     *         
     */
    public Integer getLessonStateId() {
        return this.lessonStateId;
    }

    public void setLessonStateId(Integer lessonStateId) {
        this.lessonStateId = lessonStateId;
    }

    /** 
     *                
     *         
     */
    public LearningDesign getLearningDesign() {
        return this.learningDesign;
    }

    public void setLearningDesign(LearningDesign learningDesign) {
        this.learningDesign = learningDesign;
    }

    /** 
     *              
     *         
     */
    public LessonClass getLessonClass() {
        return this.lessonClass;
    }

    public void setLessonClass(LessonClass lessonClass) {
        this.lessonClass = lessonClass;
    }

    /** 
     *              
     *         
     */
    public Organisation getOrganisation() {
        return this.organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    /** 
     *           
     *         
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

    /**
     * Getter for property learners.
     * @return Value of property learners.
     */
    public User getLearners()
    {

        return this.learners;
    }

    /**
     * Setter for property learners.
     * @param learners New value of property learners.
     */
    public void setLearners(User learners)
    {

        this.learners = learners;
    }

}
