/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
/* $$Id$$ */
package org.lamsfoundation.lams.lesson;

import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.lesson.dto.LessonDTO;
import org.lamsfoundation.lams.lesson.dto.LessonDetailsDTO;
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
    /** The state for newly created lesson. The learning design has been copied. 
     * The lesson class may or may not have been configured. It is 
     * seen on the staff interface but not on the learning interface. */ 
    public static final Integer CREATED = new Integer(1);
    /** The state for lessons that have been scheduled.  */ 
    public static final Integer NOT_STARTED_STATE = new Integer(2);
    /** The state for started lesson */
    public static final Integer STARTED_STATE = new Integer(3);
    /** The state for lessons that have been suspended by the teacher. 
     * The lesson can be seen on the staff interface but not on the learning interface */ 
    public static final Integer SUSPENDED_STATE = new Integer(4);
    /** The state for lessons that have been finished. A finished lesson
    * is shown as inactive on the staff interface, and is shown on the learner interface
    * but the learner is to only see the overall progress and be able to export
    * data - they should not be able to iteract with the tools */ 
    public static final Integer FINISHED_STATE = new Integer(5);
    /** The state for lesssons that are shown as inactive on the staff interface
     * but no longer visible to the learners. */
    public static final Integer ARCHIVED_STATE = new Integer(6);
    /** The state for lesssons that are removed and never can be accessed again */
    public static final Integer DISABLED_STATE  = new Integer(7);
    
    //---------------------------------------------------------------------
    // attributes
    //---------------------------------------------------------------------
    /** identifier field */
    private Long lessonId;
    
    /** persistent field */
    private String lessonName;
    
    /** persistent field */
    private String lessonDescription;
    
    /** persistent field */
    private Date createDateTime;

    /** nullable persistent field */
    private Date startDateTime;

    /** nullable persistent field */
    private Date endDateTime;

    /** nullable persistent field */
    private Date scheduleStartDate;
    
    /** nullable persistent field */
    private Date scheduleEndDate;
    
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
     * Minimum constructor that initialize the lesson data. It doesn't include
     * organization and class information.
     * Cain constructor pattern implementation.
     */
    public Lesson(String name,String description,Date createDateTime, User user, Integer lessonStateId, LearningDesign learningDesign,Set learnerProgresses) 
    {
        this(null,name,description,createDateTime,null,null,user,lessonStateId,learningDesign,null,null,learnerProgresses);
    }     
    
    /** 
     * Constructor that creates a new lesson with organization and class 
     * information.
     * Chain construtor pattern implementation. 
     */
    public Lesson(String name,String description,Date createDateTime, User user, Integer lessonStateId, LearningDesign learningDesign, LessonClass lessonClass, Organisation organisation, Set learnerProgresses) 
    {
        this(null,name,description,createDateTime,null,null,user,lessonStateId,learningDesign,lessonClass,organisation,learnerProgresses);
    }    
    
    /** full constructor */
    public Lesson(Long lessonId,String name,String description, Date createDateTime, Date startDateTime, Date endDateTime, User user, Integer lessonStateId, LearningDesign learningDesign, LessonClass lessonClass, Organisation organisation, Set learnerProgresses) 
    {
        this.lessonId = lessonId;
        this.lessonName = name;
        this.lessonDescription = description;
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
     * data for a new lesson with organization and lesson class information.
     * It is designed for monitor side to create a lesson by teacher.
     * 
     * @param user the teacher who created this lesson
     * @param organisation the organisation associated with this lesson.
     * @param ld the learning design associated with this lesson.
     * @param newLessonClass the lesson class that will run this lesson.
     * @return the new lesson object.
     */
    public static Lesson createNewLesson(String lessonName,
                                         String lessonDescription,
                                         User user, 
                                         Organisation organisation, 
                                         LearningDesign ld, 
                                         LessonClass newLessonClass)
    {
        //setup new lesson
        return new Lesson(lessonName,
                          lessonDescription,
                          new Date(System.currentTimeMillis()),
                          user,
                          Lesson.CREATED,
                          ld,
                          newLessonClass,//lesson class
                          organisation,
                          new HashSet());//learner progress
    }

    /**
     * Factory method that create a new lesson with lesson class and organization.
     * It is design to allow user create a lesson first and modify organization
     * and lesson class data later.
     * 
     * @param user the user who want to create a lesson.
     * @param ld the learning design that this lesson is based on.
     * @return the lesson created.
     */
    public static Lesson createNewLessonWithoutClass(String lessonName,
                                                     String lessonDescription,
                                                     User user,
                                                     LearningDesign ld)
    {
        return new Lesson(lessonName,
                          lessonDescription,
                          new Date(System.currentTimeMillis()),
                          user,
                          Lesson.CREATED,
                          ld,
                          new HashSet());
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
	 * @hibernate.property column="name" length="255" not-null="true"
     * @return Returns the lessonName.
     */
    public String getLessonName()
    {
        return lessonName;
    }
    /**
     * @param lessonName The lessonName to set.
     */
    public void setLessonName(String lessonName)
    {
        this.lessonName = lessonName;
    }
    
    /**
	 * @hibernate.property column="description" length="65535"
     * @return Returns the lessonDescription.
     */
    public String getLessonDescription()
    {
        return lessonDescription;
    }
    /**
     * @param lessonDescription The lessonDescription to set.
     */
    public void setLessonDescription(String lessonDescription)
    {
        this.lessonDescription = lessonDescription;
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
     * @hibernate.property type="java.sql.Timestamp"  column="schedule_end_date_time"
     *            	       length="19"
     * 
     * @return Returns the scheduleEndDate.
     */
    public Date getScheduleEndDate()
    {
        return scheduleEndDate;
    }
    /**
     * @param scheduleEndDate The scheduleEndDate to set.
     */
    public void setScheduleEndDate(Date scheduleEndDate)
    {
        this.scheduleEndDate = scheduleEndDate;
    }
    /**
     * @hibernate.property type="java.sql.Timestamp" column="schedule_start_date_time"
     *            		   length="19"
     * @return Returns the scheduleStartDate.
     */
    public Date getScheduleStartDate()
    {
        return scheduleStartDate;
    }
    /**
     * @param scheduleStartDate The scheduleStartDate to set.
     */
    public void setScheduleStartDate(Date scheduleStartDate)
    {
        this.scheduleStartDate = scheduleStartDate;
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
     * @hibernate.many-to-one not-null="false" unique="true" 
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
     * @hibernate.many-to-one not-null="false" cascade="none"
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
                             this.getLessonName(),
                             this.getLessonDescription(),
                             this.lessonStateId);
    }
    public LessonDetailsDTO getLessonDetails(){
    	return new LessonDetailsDTO(this);
    }
}
