/****************************************************************
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
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.lesson;

import java.util.Set;

import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.Grouping;

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
	    //don't think lesson class need perform doGrouping. set grouper to null.
		super(groupingId, groups, activities,null);
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

    /**
     * @see org.lamsfoundation.lams.learningdesign.Grouping#isLearnerGroup(org.lamsfoundation.lams.learningdesign.Group)
     */
    public boolean isLearnerGroup(Group group)
    {
        if(group.getGroupId()==null||staffGroup.getGroupId()==null)
            throw new IllegalArgumentException("Can't check up whether group" +
            		" is learner group without group id.");
        
        return staffGroup.getGroupId()!=group.getGroupId();
    }

    /**
     * This method creates a deep copy of the LessonClass 
     * @return LessonClass The deep copied LessonClass object
     */
    public Grouping createCopy()
    {
    	LessonClass lessonClass = new LessonClass();
    	lessonClass.staffGroup = this.staffGroup;
    	lessonClass.lesson = this.lesson;
    	return lessonClass;
    }

}