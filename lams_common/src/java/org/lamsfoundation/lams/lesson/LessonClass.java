/*
 * LessonClass.java
 *
 * Created on 14 January 2005, 10:56
 */

package org.lamsfoundation.lams.lesson;

import java.util.Set;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.Group;
/**
 * A type of Grouping that represents all the Learners in a Lesson.
 * The LessonClass is used as the default Grouping.
 * 
 * @author chris
 */
public class LessonClass extends Grouping
{
    
    private Group staffGroup;

    /**
     * Holds value of property lessons.
     */
    private Set lessons;
    
    
    public Group getStaffGroup()
    {
        return this.staffGroup;
    }
    
    public void setStaffGroup(Group staffGroup)
    {
        this.staffGroup =  staffGroup;
    }

    /**
     * Getter for property lessons.
     * @return Value of property lessons.
     */
    public Set getLessons()
    {

        return this.lessons;
    }

    /**
     * Setter for property lessons.
     * @param lessons New value of property lessons.
     */
    public void setLessons(Set lessons)
    {

        this.lessons = lessons;
    }
    
    /** Creates a new instance of LessonClass */
    public LessonClass()
    {
    }
    
    /** full constructor */
    public LessonClass(Long groupingId, Integer groupingTypeId, Set groups, Set activities, Group staffGroup, Set lessons)
    {
        super(groupingId, groupingTypeId, groups, activities);
        this.staffGroup = staffGroup;
        this.lessons = lessons;
    }
    
}
