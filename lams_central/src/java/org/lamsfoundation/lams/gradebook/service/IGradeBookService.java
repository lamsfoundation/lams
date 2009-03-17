/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */ 
 
/* $Id$ */ 
package org.lamsfoundation.lams.gradebook.service; 

import java.util.ArrayList;
import java.util.Collection;

import org.lamsfoundation.lams.gradebook.GradeBookUserLesson;
import org.lamsfoundation.lams.gradebook.dto.GradeBookActivityDTO;
import org.lamsfoundation.lams.gradebook.dto.GradeBookGridRow;
import org.lamsfoundation.lams.gradebook.dto.GradeBookUserDTO;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;
 
public interface IGradeBookService {
    
    
    public GradeBookActivityDTO getGradeBookActivityDTO(Activity activity, User learner, LearnerProgress learnerProgress);
    
    public Collection<GradeBookGridRow> getUserGradeBookActivityDTOs(Lesson lesson, User learner);
    
    public ArrayList<GradeBookUserDTO> getGradeBookLessonData(Lesson lesson);

    public void updateUserLessonGradeBookData(Lesson lesson, User learner, Double mark);
    
    public void updateUserActivityGradeBookData(Lesson lesson, User learner, Activity activity, Double mark);
    
}
 