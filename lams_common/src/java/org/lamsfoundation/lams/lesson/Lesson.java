/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/
package org.lamsfoundation.lams.lesson;

import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.lesson.dto.LessonDTO;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
/** 
 * A Lesson is a learning sequence that is assocated with
 * a number of users for use in learning. A lesson needs a run time copy of
 * learning design to interact with.         
 * 
 * 
 * Hibernate definition:
 * 
 * @hibernate.class table="tool_lasr10_survey_session"
 * 
 */
public class Lesson implements Serializable {

    //---------------------------------------------------------------------
    // Class level constants
    //---------------------------------------------------------------------
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
    
    //---------------------------------------------------------------------
    // attributes
    //---------------------------------------------------------------------
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
    
    //---------------------------------------------------------------------
    // constructors
    //---------------------------------------------------------------------
    /** default constructor */
    public Lesson() {
    }
    /** 
     * Minimum constructor that includes all not null attributes to create
     * a new Lesson object.
     * Chain construtor pattern implementation. 
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
     * Factory method that create a new lesson. It initialized all necessary
     * data for a new lesson. It is design for monitor side to create a lesson
     * by teacher.
     * 
     * @param user the teacher who created this lesson
     * @param organisation the organisation associated with this lesson.
     * @param ld the learning design associated with this lesson.
     * @param newLessonClass the lesson class that will run this lesson.
     * @return the new lesson object.
     */
    public static Lesson createNewLesson(User user, 
                                         Organisation organisation, 
                                         LearningDesign ld, 
                                         LessonClass newLessonClass)
    {
        //setup new lesson
        return new Lesson(new Date(System.currentTimeMillis()),
                                   user,
                                   Lesson.CREATED,
                                   ld,
                                   newLessonClass,//lesson class
                                   organisation,
                                   new HashSet());//learner progress
    }

    //---------------------------------------------------------------------
    // Getters and Setters
    //---------------------------------------------------------------------
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

    public Set getAllLearners()
    {
        return lessonClass.getLearners();
    }
    //---------------------------------------------------------------------
    // Domain service methods
    //---------------------------------------------------------------------
    /**
     * Create lesson data transfer object for flash and java interaction.
     * @return the lesson data transfer object.
     */
    public LessonDTO getLessonData()
    {
        return new LessonDTO(this.lessonId,
                             this.getLearningDesign().getTitle(),
                             this.getLearningDesign().getDescription(),
                             this.lessonStateId);
    }
}
