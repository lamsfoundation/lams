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
 * A type of Grouping that represents all the Learners in a Lesson. The
 * LessonClass is used as the default Grouping.
 * 
 * @author chris
 */
public class LessonClass extends Grouping {
    
    private Group staffGroup;

	private Lesson lesson;

	/** Creates a new instance of LessonClass */
	public LessonClass() {
	}
	
	/** full constructor */
	public LessonClass(Long groupingId, Set groups,
			Set activities, Group staffGroup, Lesson lesson) {
		super(groupingId, groups, activities);
		this.staffGroup = staffGroup;
		this.lesson = lesson;
	}

	public Group getStaffGroup() {
		return this.staffGroup;
	}

	public void setStaffGroup(Group staffGroup) {
		this.staffGroup = staffGroup;
	}

	public Lesson getLesson() {
		return lesson;
	}
	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
	}
}